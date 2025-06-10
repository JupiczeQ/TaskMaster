package RightPanels;

import Database.CategoryDAO;
import Designs.buttonStyler;
import Designs.comboBoxStyler;
import Fonts.FontAwesome;
import Models.BaseTableModel;
import Models.Category;
import Utils.MessageUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CategoriesPanel extends JPanel {
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JComboBox filterComboBox;
    private JTextField searchField;
    private JButton addCategoryButton;
    private JTable categoriesTable;

    private CategoryTableModel tableModel;
    private int userID;
    private List<Category> allCategories;

    private static class CategoryTableModel extends BaseTableModel<Category> {
        private List<Integer> taskCounts = new ArrayList<>();

        public CategoryTableModel() {
            super(new String[]{"Nazwa", "Liczba zadań"});
        }

        @Override
        protected void onItemsChanged() {
            loadTaskCounts();
        }

        private void loadTaskCounts() {
            taskCounts.clear();
            for (Category category : items) {
                try {
                    int count = CategoryDAO.getTaskCountForCategory(category.getId());
                    taskCounts.add(count);
                } catch (SQLException e) {
                    taskCounts.add(0);
                }
            }
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (rowIndex >= items.size()) return null;

            Category category = items.get(rowIndex);
            switch (columnIndex) {
                case 0: return category.getName();
                case 1: return taskCounts.get(rowIndex);
                default: return null;
            }
        }

        public Category getCategoryAt(int rowIndex) {
            return getItemAt(rowIndex);
        }

        public int getTaskCountAt(int rowIndex) {
            return (rowIndex >= 0 && rowIndex < taskCounts.size()) ? taskCounts.get(rowIndex) : 0;
        }
    }

    public CategoriesPanel(int userID) {

        this.userID = userID;
        this.setLayout(new BorderLayout());
        this.add(mainPanel, BorderLayout.CENTER);

        setupComponents();
        setupEventListeners();
        loadCategories();
    }

    public CategoriesPanel() {
        this(1);
    }

    private void setupComponents() {
        buttonStyler.styleButton(addCategoryButton, FontAwesome.Icons.PLUS, " Dodaj kategorię");
        addCategoryButton.setBackground(new Color(29, 78, 216));
        addCategoryButton.setForeground(Color.WHITE);

        filterComboBox.removeAllItems();
        filterComboBox.addItem("Wszystkie");
        filterComboBox.addItem("Z zadaniami");
        filterComboBox.addItem("Bez zadań");
        comboBoxStyler.styleComboBox(filterComboBox);

        tableModel = new CategoryTableModel();
        categoriesTable.setModel(tableModel);

        categoriesTable.setRowHeight(35);
        categoriesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        categoriesTable.getTableHeader().setReorderingAllowed(false);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        categoriesTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        categoriesTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
    }

    private void setupEventListeners() {
        Window parentWindow = SwingUtilities.getWindowAncestor(CategoriesPanel.this);
        JFrame parentFrame;

        if (parentWindow instanceof JFrame) {
            parentFrame = (JFrame) parentWindow;
        } else {
            parentFrame = null;
        }
        addCategoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCategoryPanel(null);
            }
        });

        filterComboBox.addActionListener(e -> filterCategories());
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { filterCategories(); }
            public void removeUpdate(DocumentEvent e) { filterCategories(); }
            public void insertUpdate(DocumentEvent e) { filterCategories(); }
        });

        // Obsługa podwójnego kliknięcia w tabelę - edycja kategorii
        categoriesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = categoriesTable.getSelectedRow();
                    if (row >= 0) {
                        Category category = tableModel.getCategoryAt(row);
                        if (category != null) {
                            showCategoryPanel(category);
                        }
                    }
                }
            }
        });
    }

    private void showCategoryPanel(Category category) {
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        JFrame parentFrame = null;

        if (parentWindow instanceof JFrame) {
            parentFrame = (JFrame) parentWindow;
        }

        ModCategoryPanel dialog = new ModCategoryPanel(parentFrame, userID, category);

        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                refreshCategories();
            }
        });

        dialog.setVisible(true);
    }

    private void loadCategories() {
        try {
            allCategories = CategoryDAO.getCategoriesByUserId(userID);
            filterCategories();
        } catch (SQLException e) {
            MessageUtils.showDatabaseError(CategoriesPanel.this, e, "Błąd podczas ładowania kategorii:");
        }
    }

    private void filterCategories() {
        if (allCategories == null) return;

        List<Category> filteredCategories = new ArrayList<>(allCategories);

        String selectedFilter = (String) filterComboBox.getSelectedItem();
        if ("Z zadaniami".equals(selectedFilter)) {
            filteredCategories = filteredCategories.stream()
                    .filter(category -> {
                        try {
                            return CategoryDAO.getTaskCountForCategory(category.getId()) > 0;
                        } catch (SQLException e) {
                            return false;
                        }
                    })
                    .collect(Collectors.toList());
        } else if ("Bez zadań".equals(selectedFilter)) {
            filteredCategories = filteredCategories.stream()
                    .filter(category -> {
                        try {
                            return CategoryDAO.getTaskCountForCategory(category.getId()) == 0;
                        } catch (SQLException e) {
                            return true;
                        }
                    })
                    .collect(Collectors.toList());
        }

        String searchText = searchField.getText().trim().toLowerCase();
        if (!searchText.isEmpty()) {
            filteredCategories = filteredCategories.stream()
                    .filter(category -> category.getName().toLowerCase().contains(searchText))
                    .collect(Collectors.toList());
        }

        tableModel.setItems(filteredCategories);
    }

    public void refreshCategories() {
        loadCategories();
    }
}
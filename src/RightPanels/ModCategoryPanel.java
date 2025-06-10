package RightPanels;

import Database.CategoryDAO;
import Designs.buttonStyler;
import Models.Category;
import Utils.MessageUtils;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class ModCategoryPanel extends JDialog {
    private JPanel mainPanel;
    private JTextField nameField;
    private JLabel nameLabel;
    private JButton saveButton;
    private JButton cancelButton;
    private JButton deleteButton;

    private Category category;
    private int userID;
    private boolean isEditMode;

    // Konstruktor dla dodawania nowej kategorii
    public ModCategoryPanel(JFrame parent, int userID) {
        this(parent, userID, null);
    }

    public ModCategoryPanel(JFrame parent, int userID, Category categoryToEdit) {
        super(parent, categoryToEdit == null ? "Dodaj" : "Edytuj", true);
        this.userID = userID;
        this.category = categoryToEdit;
        this.isEditMode = categoryToEdit != null;

        this.setContentPane(mainPanel);

        setupComponents();
        setupEventListeners();
        loadCategoryData();

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(parent);
        this.setResizable(false);
    }

    private void setupComponents() {
        buttonStyler.styleButton(cancelButton);
        buttonStyler.styleButton(saveButton);
        buttonStyler.styleButton(deleteButton);

        saveButton.setBackground(new Color(29, 78, 216));
        saveButton.setHorizontalAlignment(SwingConstants.CENTER);
        saveButton.setForeground(Color.WHITE);

        deleteButton.setBackground(new Color(220, 38, 38));
        deleteButton.setHorizontalAlignment(SwingConstants.CENTER);
        deleteButton.setForeground(Color.WHITE);

        saveButton.setText(isEditMode ? "Zapisz" : "Dodaj");
        deleteButton.setVisible(isEditMode);

        getRootPane().setDefaultButton(saveButton);
    }

    private void setupEventListeners() {
        cancelButton.addActionListener(e -> dispose());
        saveButton.addActionListener(e -> saveCategory());
        deleteButton.addActionListener(e -> deleteCategory());
    }

    private void loadCategoryData() {
        if (isEditMode && category != null) {
            nameField.setText(category.getName());
            nameField.selectAll();
        }
        nameField.requestFocus();
    }

    private void saveCategory() {
        String name = nameField.getText().trim();

        if (name.isEmpty()) {
            MessageUtils.showError(this, "Nazwa kategorii nie może być pusta!");
            nameField.requestFocus();
            return;
        }

        if (name.length() > 100) {
            MessageUtils.showError(this, "Nazwa kategorii nie może być dłuższa niż 100 znaków!");
            nameField.requestFocus();
            nameField.selectAll();
            return;
        }

        try {
            if (!isEditMode || !name.equals(category.getName())) {
                if (CategoryDAO.categoryExists(name, userID)) {
                    MessageUtils.showError(this, "Kategoria o nazwie '" + name + "' już istnieje!");
                    nameField.requestFocus();
                    nameField.selectAll();
                    return;
                }
            }

            boolean success;
            if (isEditMode) {
                category.setName(name);
                success = CategoryDAO.updateCategory(category);

                if (success) {
                    MessageUtils.showSuccess(this, "Kategoria została zaktualizowana pomyślnie!");
                } else {
                    MessageUtils.showError(this, "Nie udało się zaktualizować kategorii.");
                    return;
                }
            } else {
                Category newCategory = new Category();
                newCategory.setName(name);
                newCategory.setUserID(userID);

                success = CategoryDAO.saveCategory(newCategory);

                if (success) {
                    MessageUtils.showSuccess(this, "Kategoria '" + name + "' została dodana pomyślnie!");
                } else {
                    MessageUtils.showError(this, "Nie udało się dodać kategorii.");
                    return;
                }
            }

            dispose();

        } catch (SQLException e) {
            MessageUtils.showDatabaseError(this, e, "Błąd podczas zapisywania kategorii:");
        }
    }

    private void deleteCategory() {
        if (!isEditMode || category == null) return;

        try {
            int taskCount = CategoryDAO.getTaskCountForCategory(category.getId());

            String message = String.format(
                    "Czy na pewno chcesz usunąć kategorię '%s'?",
                    category.getName()
            );

            int result = JOptionPane.showConfirmDialog(
                    this, message, "Potwierdź usunięcie kategorii",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE
            );

            if (result == JOptionPane.YES_OPTION) {
                boolean success = CategoryDAO.deleteCategory(category.getId(), userID);

                if (success) {
                    MessageUtils.showSuccess(this, "Kategoria została usunięta pomyślnie!");
                    dispose();
                } else {
                    message = String.format(
                            "Kategoria '%s' ma przypisane %d zadań.\n" +
                                    "Nie można usunąć kategorii, która ma przypisane zadania.",
                            category.getName(), taskCount
                    );
                    MessageUtils.showError(this, message);
                    return;
                }
            }

        } catch (SQLException e) {
            MessageUtils.showDatabaseError(this, e, "Błąd podczas usuwania kategorii:");
        }
    }
}
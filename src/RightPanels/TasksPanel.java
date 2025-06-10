package RightPanels;

import Database.TaskDAO;
import Designs.buttonStyler;
import Designs.comboBoxStyler;
import Fonts.FontAwesome;
import Models.BaseTableModel;
import Models.Task;
import Models.TaskPriority;
import Models.TaskStatus;
import Utils.MessageUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TasksPanel extends JPanel{
    private JPanel rightPanel;
    private JLabel sidePanelName;
    private JComboBox statusComboBox;
    private JComboBox priorityComboBox;
    private JTextField searchField;
    private JButton addTaskButton;
    private JTable tasksTable;
    private JPanel mainFrame;
    private JFrame parentFrame;
    private int userID;
    private boolean isAdmin;
    private TaskTableModel tableModel;
    private List<Task> allTasks;

    private static class TaskTableModel extends BaseTableModel<Task> {
        private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        private final boolean isAdmin;

        public TaskTableModel(boolean isAdmin) {
            super(isAdmin ?
                    new String[]{"Użytkownik", "Tytuł", "Status", "Priorytet", "Kategoria", "Data utworzenia"} :
                    new String[]{"Tytuł", "Status", "Priorytet", "Kategoria", "Data utworzenia"}
            );
            this.isAdmin = isAdmin;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (rowIndex >= items.size()) return null;

            Task task = items.get(rowIndex);

            if (isAdmin) {
                switch (columnIndex) {
                    case 0: return task.getUsername();
                    case 1: return task.getTitle();
                    case 2: return task.getStatus();
                    case 3: return task.getPriority();
                    case 4: return task.getCategory() != null ? task.getCategory().getName() : "Brak kategorii";
                    case 5: return task.getCreatedAt() != null ? dateFormat.format(task.getCreatedAt()) : "";
                    default: return null;
                }
            } else {
                switch (columnIndex) {
                    case 0: return task.getTitle();
                    case 1: return task.getStatus();
                    case 2: return task.getPriority();
                    case 3: return task.getCategory() != null ? task.getCategory().getName() : "Brak kategorii";
                    case 4: return task.getCreatedAt() != null ? dateFormat.format(task.getCreatedAt()) : "";
                    default: return null;
                }
            }
        }

        public Task getTaskAt(int rowIndex) {
            return getItemAt(rowIndex);
        }
    }

    private void filterTasks() {
        if (allTasks == null) return;

        List<Task> filteredTasks = new ArrayList<>(allTasks);

        Object selectedStatusObj = statusComboBox.getSelectedItem();
        if (selectedStatusObj instanceof TaskStatus) {
            TaskStatus selectedStatus = (TaskStatus) selectedStatusObj;
            filteredTasks = filteredTasks.stream()
                    .filter(task -> task.getStatus() == selectedStatus)
                    .collect(Collectors.toList());
        }

        Object selectedPriorityObj = priorityComboBox.getSelectedItem();
        if (selectedPriorityObj instanceof TaskPriority) {
            TaskPriority selectedPriority = (TaskPriority) selectedPriorityObj;
            filteredTasks = filteredTasks.stream()
                    .filter(task -> task.getPriority() == selectedPriority)
                    .collect(Collectors.toList());
        }

        String searchText = searchField.getText().trim().toLowerCase();
        if (!searchText.isEmpty()) {
            filteredTasks = filteredTasks.stream()
                    .filter(task -> {
                        boolean matches = task.getTitle().toLowerCase().contains(searchText) ||
                                (task.getDescription() != null && task.getDescription().toLowerCase().contains(searchText));

                        if (isAdmin && task.getUsername() != null) {
                            matches = matches || task.getUsername().toLowerCase().contains(searchText);
                        }

                        return matches;
                    })
                    .collect(Collectors.toList());
        }

        tableModel.setItems(filteredTasks);
    }

    private static class TaskTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (!isSelected) {
                TaskTableModel model = (TaskTableModel) table.getModel();
                Task task = model.getTaskAt(row);

                if (task != null) {
                    // Kolory dla statusu
                    switch (task.getStatus()) {
                        case TODO:
                            setBackground(new Color(254, 242, 242));
                            break;
                        case IN_PROGRESS:
                            setBackground(new Color(255, 251, 235));
                            break;
                        case COMPLETED:
                            setBackground(new Color(240, 253, 244));
                            break;
                        default:
                            setBackground(Color.WHITE);
                    }

                    // Kolor dla priorytetu
                    switch (task.getPriority()) {
                        case HIGH:
                            setForeground(new Color(185, 28, 28)); // Czerwony tekst
                            break;
                        case MEDIUM:
                            setForeground(new Color(161, 98, 7)); // Pomarańczowy tekst
                            break;
                        case LOW:
                            setForeground(new Color(21, 128, 61)); // Zielony tekst
                            break;
                    }
                }
            } else {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            }

            return this;
        }
    }

    private static class CenteredTaskTableCellRenderer extends TaskTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setHorizontalAlignment(SwingConstants.CENTER);
            return this;
        }
    }

    public void loadTasks() {
        try {
            if (isAdmin) {
                allTasks = TaskDAO.getAllTasks();
            } else {
                allTasks = TaskDAO.getTasksByUserId(userID);
            }
            filterTasks();
        } catch (SQLException e) {
            MessageUtils.showDatabaseError(TasksPanel.this, e, "Błąd podczas ładowania zadań:");
        }
    }

    public TasksPanel(int userID, boolean isAdmin){
        this.setLayout(new BorderLayout());
        this.add(rightPanel, BorderLayout.CENTER);

        this.userID = userID;
        this.isAdmin = isAdmin;

        statusComboBox.removeAllItems();
        statusComboBox.addItem("Wszystkie");
        for (TaskStatus status : TaskStatus.values()) {
            statusComboBox.addItem(status);
        }

        priorityComboBox.removeAllItems();
        priorityComboBox.addItem("Wszystkie");
        for (TaskPriority priority : TaskPriority.values()) {
            priorityComboBox.addItem(priority);
        }

        tableModel = new TaskTableModel(isAdmin);
        loadTasks();

        tasksTable.setModel(tableModel);
        tasksTable.setRowHeight(30);
        tasksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tasksTable.getTableHeader().setReorderingAllowed(false);

        // Renderery dla kolorów i wysrodkowania
        tasksTable.setDefaultRenderer(Object.class, new CenteredTaskTableCellRenderer());

        comboBoxStyler.styleComboBox(statusComboBox);
        comboBoxStyler.styleComboBox(priorityComboBox);

        buttonStyler.styleButton(addTaskButton, FontAwesome.Icons.PLUS, " Dodaj zadanie");
        addTaskButton.setBackground(new Color(29, 78, 216));
        addTaskButton.setForeground(Color.WHITE);

        sidePanelName.setText(isAdmin ? "Wszystkie zadania" : "Moje zadania");

        Window parentWindow = SwingUtilities.getWindowAncestor(mainFrame);
        if(parentWindow instanceof JFrame){
            parentFrame = (JFrame) parentWindow;
        }

        setupEventListeners();
    }

    public TasksPanel(int userID){
        this(userID, false); // Domyślnie nie jest adminem
    }

    private void setupEventListeners() {
        addTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ModTaskPanel modTaskPanel = new ModTaskPanel(parentFrame, userID, null, isAdmin);

                modTaskPanel.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        loadTasks();
                    }
                });
                modTaskPanel.setVisible(true);
            }
        });

        // Podwójne kliknięcie - edycja zadania
        tasksTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tasksTable.getSelectedRow();
                    if (row >= 0) {
                        Task task = tableModel.getTaskAt(row);
                        if (task != null) {
                            ModTaskPanel modTaskPanel = new ModTaskPanel(parentFrame, userID, task, isAdmin);

                            modTaskPanel.addWindowListener(new WindowAdapter() {
                                @Override
                                public void windowClosed(WindowEvent e) {
                                    loadTasks();
                                }
                            });

                            modTaskPanel.setVisible(true);
                        }
                    }
                }
            }
        });

        statusComboBox.addActionListener(e -> filterTasks());
        priorityComboBox.addActionListener(e -> filterTasks());

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { filterTasks(); }
            public void removeUpdate(DocumentEvent e) { filterTasks(); }
            public void insertUpdate(DocumentEvent e) { filterTasks(); }
        });
    }
}
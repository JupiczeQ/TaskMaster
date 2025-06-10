package RightPanels;

import Database.CategoryDAO;
import Database.TaskDAO;
import Designs.buttonStyler;
import Designs.comboBoxStyler;
import Models.Category;
import Models.Task;
import Models.TaskPriority;
import Models.TaskStatus;
import Utils.MessageUtils;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ModTaskPanel extends JDialog {
    private JPanel mainPanel;
    private JTextField nameField;
    private JTextArea descriptionField;
    private JComboBox<TaskPriority> priorityComboBox;
    private JComboBox<TaskStatus> statusComboBox;
    private JComboBox<String> categoryComboBox;
    private JComboBox<String> userComboBox;
    private JPanel userPanel;
    private JLabel nameLabel;
    private JLabel descriptionLabel;
    private JLabel priorityLabel;
    private JLabel statusLabel;
    private JLabel categoryLabel;
    private JLabel userLabel;
    private JLabel createdAtLabel;
    private JButton saveButton;
    private JButton cancelButton;
    private JButton deleteButton;

    private Task task;
    private int userID;
    private boolean isEditMode;
    private boolean isAdmin;
    private List<Category> categories = new ArrayList<>();
    private List<String> usernames = new ArrayList<>();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    public ModTaskPanel(JFrame parent, int userID) {
        this(parent, userID, null, false);
    }

    public ModTaskPanel(JFrame parent, int userID, Task taskToEdit) {
        this(parent, userID, taskToEdit, false);
    }

    public ModTaskPanel(JFrame parent, int userID, Task taskToEdit, boolean isAdmin) {
        super(parent, taskToEdit == null ? "Dodaj zadanie" : "Edytuj zadanie", true);
        this.userID = userID;
        this.task = taskToEdit;
        this.isEditMode = taskToEdit != null;
        this.isAdmin = isAdmin;

        this.setContentPane(mainPanel);

        if (isAdmin && userPanel != null) {
            userPanel.setVisible(true);
        }

        setupComponents();
        setupEventListeners();
        loadCategories();

        if (isAdmin) {
            loadUsernames();
        }

        loadTaskData();

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
        createdAtLabel.setVisible(isEditMode);

        descriptionField.setBorder(UIManager.getBorder("TextField.border"));

        comboBoxStyler.styleComboBox(priorityComboBox);
        comboBoxStyler.styleComboBox(statusComboBox);
        comboBoxStyler.styleComboBox(categoryComboBox);

        if (isAdmin && userComboBox != null) {
            comboBoxStyler.styleComboBox(userComboBox);
        }

        setupComboBoxes();

        getRootPane().setDefaultButton(saveButton);
    }

    private void setupComboBoxes() {
        priorityComboBox.removeAllItems();
        for (TaskPriority priority : TaskPriority.values()) {
            priorityComboBox.addItem(priority);
        }

        statusComboBox.removeAllItems();
        for (TaskStatus status : TaskStatus.values()) {
            statusComboBox.addItem(status);
        }

        categoryComboBox.removeAllItems();
        categoryComboBox.addItem("Brak kategorii");

        if (isAdmin && userComboBox != null) {
            userComboBox.removeAllItems();
        }
    }

    private void setupEventListeners() {
        cancelButton.addActionListener(e -> dispose());
        saveButton.addActionListener(e -> saveTask());
        deleteButton.addActionListener(e -> deleteTask());

        if (isAdmin && userComboBox != null) {
            userComboBox.addActionListener(e -> loadCategoriesForSelectedUser());
        }
    }

    private void loadCategories() {
        try {
            categories = CategoryDAO.getCategoriesByUserId(userID);

            categoryComboBox.removeAllItems();
            categoryComboBox.addItem("Brak kategorii");
            for (Category category : categories) {
                categoryComboBox.addItem(category.getName());
            }
        } catch (SQLException e) {
            MessageUtils.showDatabaseError(this, e, "Błąd podczas ładowania kategorii:");
        }
    }

    private void loadCategoriesForSelectedUser() {
        String selectedUsername = (String) userComboBox.getSelectedItem();
        if (selectedUsername == null) return;

        try {
            int selectedUserId = TaskDAO.getUserIdByUsername(selectedUsername);

            categories = CategoryDAO.getCategoriesByUserId(selectedUserId);

            categoryComboBox.removeAllItems();
            categoryComboBox.addItem("Brak kategorii");
            for (Category category : categories) {
                categoryComboBox.addItem(category.getName());
            }

            categoryComboBox.setSelectedItem("Brak kategorii");

        } catch (SQLException e) {
            MessageUtils.showDatabaseError(this, e, "Błąd podczas ładowania kategorii użytkownika:");
        }
    }

    private void loadUsernames() {
        if (!isAdmin || userComboBox == null) return;

        try {
            usernames = TaskDAO.getAllUsernames();

            userComboBox.removeAllItems();
            for (String username : usernames) {
                userComboBox.addItem(username);
            }
        } catch (SQLException e) {
            MessageUtils.showDatabaseError(this, e, "Błąd podczas ładowania użytkowników:");
        }
    }

    private void loadTaskData() {
        if (isEditMode && task != null) {
            nameField.setText(task.getTitle());
            descriptionField.setText(task.getDescription() != null ? task.getDescription() : "");

            priorityComboBox.setSelectedItem(task.getPriority());
            statusComboBox.setSelectedItem(task.getStatus());

            if (task.getCategory() != null) {
                categoryComboBox.setSelectedItem(task.getCategory().getName());
            } else {
                categoryComboBox.setSelectedItem("Brak kategorii");
            }

            if (isAdmin && userComboBox != null && task.getUsername() != null) {
                userComboBox.setSelectedItem(task.getUsername());
            }

            if (task.getCreatedAt() != null) {
                createdAtLabel.setText(dateFormat.format(task.getCreatedAt()));
            }

            nameField.selectAll();
        } else {
            priorityComboBox.setSelectedItem(TaskPriority.MEDIUM);
            statusComboBox.setSelectedItem(TaskStatus.TODO);
            categoryComboBox.setSelectedItem("Brak kategorii");

            if (isAdmin && userComboBox != null && userComboBox.getItemCount() > 0) {
                userComboBox.setSelectedIndex(0);
            }
        }

        nameField.requestFocus();
    }

    private void saveTask() {
        String title = nameField.getText().trim();
        if (title.isEmpty()) {
            MessageUtils.showError(this, "Nazwa zadania nie może być pusta!");
            nameField.requestFocus();
            return;
        }

        if (title.length() > 200) {
            MessageUtils.showError(this, "Nazwa zadania nie może być dłuższa niż 200 znaków!");
            nameField.requestFocus();
            nameField.selectAll();
            return;
        }

        String description = descriptionField.getText().trim();
        TaskPriority priority = (TaskPriority) priorityComboBox.getSelectedItem();
        TaskStatus status = (TaskStatus) statusComboBox.getSelectedItem();

        Integer categoryId = getCategoryIdFromSelection();
        int taskOwnerUserId = getTaskOwnerUserId();

        try {
            boolean success;

            if (isEditMode) {
                success = updateExistingTask(title, description, priority, status, categoryId, taskOwnerUserId);

                if (success) {
                    MessageUtils.showSuccess(this, "Zadanie zostało zaktualizowane pomyślnie!");
                } else {
                    MessageUtils.showError(this, "Nie udało się zaktualizować zadania.");
                    return;
                }
            } else {
                success = createNewTask(title, description, priority, status, categoryId, taskOwnerUserId);

                if (success) {
                    MessageUtils.showSuccess(this, "Zadanie '" + title + "' zostało dodane pomyślnie!");
                } else {
                    MessageUtils.showError(this, "Nie udało się dodać zadania.");
                    return;
                }
            }

            dispose();

        } catch (SQLException e) {
            MessageUtils.showDatabaseError(this, e, "Błąd podczas zapisywania zadania:");
        }
    }

    private Integer getCategoryIdFromSelection() {
        String selectedCategoryName = (String) categoryComboBox.getSelectedItem();
        if (selectedCategoryName != null && !selectedCategoryName.equals("Brak kategorii")) {
            for (Category category : categories) {
                if (category.getName().equals(selectedCategoryName)) {
                    return category.getId();
                }
            }
        }
        return null;
    }

    private int getTaskOwnerUserId() {
        if (isAdmin && userComboBox != null) {
            String selectedUsername = (String) userComboBox.getSelectedItem();
            if (selectedUsername != null) {
                try {
                    return TaskDAO.getUserIdByUsername(selectedUsername);
                } catch (SQLException e) {
                    MessageUtils.showDatabaseError(this, e, "Błąd podczas pobierania ID użytkownika:");
                    return userID;
                }
            }
        }
        return userID;
    }

    private boolean updateExistingTask(String title, String description, TaskPriority priority, TaskStatus status, Integer categoryId, int taskOwnerUserId) throws SQLException {
        task.setTitle(title);
        task.setDescription(description.isEmpty() ? null : description);
        task.setPriority(priority);
        task.setStatus(status);
        task.setCategoryId(categoryId);
        task.setUserID(taskOwnerUserId);

        if (categoryId != null) {
            for (Category category : categories) {
                if (category.getId() == categoryId) {
                    task.setCategory(category);
                    break;
                }
            }
        } else {
            task.setCategory(null);
        }

        if (isAdmin) {
            return TaskDAO.updateTaskByAdmin(task);
        } else {
            return TaskDAO.updateTask(task);
        }
    }

    private boolean createNewTask(String title, String description, TaskPriority priority, TaskStatus status, Integer categoryId, int taskOwnerUserId) throws SQLException {
        Task newTask = new Task();
        newTask.setTitle(title);
        newTask.setDescription(description.isEmpty() ? null : description);
        newTask.setPriority(priority);
        newTask.setStatus(status);
        newTask.setUserID(taskOwnerUserId);
        newTask.setCategoryId(categoryId);
        newTask.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        return TaskDAO.saveTask(newTask);
    }

    private void deleteTask() {
        if (!isEditMode || task == null) return;

        String message = String.format(
                "Czy na pewno chcesz usunąć zadanie '%s'?",
                task.getTitle()
        );

        int result = JOptionPane.showConfirmDialog(
                this, message, "Potwierdź usunięcie zadania",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE
        );

        if (result == JOptionPane.YES_OPTION) {
            try {
                boolean success;

                if (isAdmin) {
                    success = TaskDAO.deleteTaskByAdmin(task.getId());
                } else {
                    success = TaskDAO.deleteTask(task.getId(), userID);
                }

                if (success) {
                    MessageUtils.showSuccess(this, "Zadanie zostało usunięte pomyślnie!");
                    dispose();
                } else {
                    MessageUtils.showError(this, "Nie udało się usunąć zadania.");
                }
            } catch (SQLException e) {
                MessageUtils.showDatabaseError(this, e, "Błąd podczas usuwania zadania:");
            }
        }
    }
}
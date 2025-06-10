package Database;

import Models.Category;
import Models.Task;
import Models.TaskPriority;
import Models.TaskStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {
    public static boolean saveTask(Task task) throws SQLException {
        String sql = "INSERT INTO tasks (title, description, priority, status, category_id, user_id, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, task.getTitle());
            pstmt.setString(2, task.getDescription());
            pstmt.setString(3, task.getPriority().getDisplayName());
            pstmt.setString(4, task.getStatus().getDisplayName());
            if (task.getCategoryId() != null) {
                pstmt.setInt(5, task.getCategoryId());
            } else {
                pstmt.setNull(5, Types.INTEGER);
            }
            pstmt.setInt(6, task.getUserID());
            pstmt.setTimestamp(7, task.getCreatedAt());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public static List<Task> getTasksByUserId(int userId) throws SQLException {
        return getTasksByUserId(userId, null, null);
    }

    public static List<Task> getAllTasks() throws SQLException {
        return getAllTasks(null, null);
    }

    public static List<Task> getAllTasks(TaskStatus statusFilter, TaskPriority priorityFilter) throws SQLException {
        List<Task> tasks = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT t.id, t.title, t.description, t.priority, t.status, t.category_id, t.user_id, t.created_at, " +
                        "c.name as category_name, u.username " +
                        "FROM tasks t " +
                        "LEFT JOIN categories c ON t.category_id = c.id " +
                        "LEFT JOIN users u ON t.user_id = u.id " +
                        "WHERE 1=1" // Zawsze prawdziwy warunek dla uproszczenia dodawania filtrów
        );

        if (statusFilter != null) {
            sql.append(" AND t.status = ?");
        }
        if (priorityFilter != null) {
            sql.append(" AND t.priority = ?");
        }

        sql.append(" ORDER BY t.created_at DESC");

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;

            if (statusFilter != null) {
                pstmt.setString(paramIndex++, statusFilter.getDisplayName());
            }
            if (priorityFilter != null) {
                pstmt.setString(paramIndex++, priorityFilter.getDisplayName());
            }

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Task task = createTaskFromResultSet(rs);
                tasks.add(task);
            }
        }

        return tasks;
    }

    public static List<Task> getTasksByUserId(int userID, TaskStatus statusFilter, TaskPriority priorityFilter) throws SQLException {
        List<Task> tasks = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT t.id, t.title, t.description, t.priority, t.status, t.category_id, t.user_id, t.created_at, " +
                        "c.name as category_name, u.username " +
                        "FROM tasks t " +
                        "LEFT JOIN categories c ON t.category_id = c.id " +
                        "LEFT JOIN users u ON t.user_id = u.id " +
                        "WHERE t.user_id = ?"
        );

        if (statusFilter != null) {
            sql.append(" AND t.status = ?");
        }
        if (priorityFilter != null) {
            sql.append(" AND t.priority = ?");
        }

        sql.append(" ORDER BY t.created_at DESC");

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            pstmt.setInt(paramIndex++, userID);

            if (statusFilter != null) {
                pstmt.setString(paramIndex++, statusFilter.getDisplayName());
            }
            if (priorityFilter != null) {
                pstmt.setString(paramIndex++, priorityFilter.getDisplayName());
            }

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Task task = createTaskFromResultSet(rs);
                tasks.add(task);
            }
        }

        return tasks;
    }

    private static Task createTaskFromResultSet(ResultSet rs) throws SQLException {
        Task task = new Task();
        task.setId(rs.getInt("id"));
        task.setTitle(rs.getString("title"));
        task.setDescription(rs.getString("description"));
        task.setPriority(TaskPriority.fromDisplayName(rs.getString("priority")));
        task.setStatus(TaskStatus.fromDisplayName(rs.getString("status")));
        task.setUserID(rs.getInt("user_id"));
        task.setCreatedAt(rs.getTimestamp("created_at"));

        int categoryId = rs.getInt("category_id");
        if (!rs.wasNull()) {
            Category category = new Category(
                    categoryId,
                    rs.getString("category_name"),
                    rs.getInt("user_id")
            );
            task.setCategory(category);
        }

        task.setUsername(rs.getString("username"));

        return task;
    }

    public static boolean updateTask(Task task) throws SQLException {
        String sql = "UPDATE tasks SET title = ?, description = ?, priority = ?, status = ?, category_id = ? WHERE id = ? AND user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, task.getTitle());
            pstmt.setString(2, task.getDescription());
            pstmt.setString(3, task.getPriority().getDisplayName());
            pstmt.setString(4, task.getStatus().getDisplayName());

            if (task.getCategoryId() != null) {
                pstmt.setInt(5, task.getCategoryId());
            } else {
                pstmt.setNull(5, Types.INTEGER);
            }

            pstmt.setInt(6, task.getId());
            pstmt.setInt(7, task.getUserID());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public static boolean updateTaskByAdmin(Task task) throws SQLException {
        String sql = "UPDATE tasks SET title = ?, description = ?, priority = ?, status = ?, category_id = ?, user_id = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, task.getTitle());
            pstmt.setString(2, task.getDescription());
            pstmt.setString(3, task.getPriority().getDisplayName());
            pstmt.setString(4, task.getStatus().getDisplayName());

            if (task.getCategoryId() != null) {
                pstmt.setInt(5, task.getCategoryId());
            } else {
                pstmt.setNull(5, Types.INTEGER);
            }

            pstmt.setInt(6, task.getUserID()); // Admin może zmienić właściciela zadania
            pstmt.setInt(7, task.getId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public static boolean deleteTask(int taskId, int userID) throws SQLException {
        String sql = "DELETE FROM tasks WHERE id = ? AND user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, taskId);
            pstmt.setInt(2, userID);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public static boolean deleteTaskByAdmin(int taskId) throws SQLException {
        String sql = "DELETE FROM tasks WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, taskId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public static List<String> getAllUsernames() throws SQLException {
        List<String> usernames = new ArrayList<>();
        String sql = "SELECT username FROM users ORDER BY username";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                usernames.add(rs.getString("username"));
            }
        }

        return usernames;
    }

    public static int getUserIdByUsername(String username) throws SQLException {
        String sql = "SELECT id FROM users WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            } else {
                return -1;
            }
        }
    }
}
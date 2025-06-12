package Database;

import Models.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    public static boolean saveCategory(Category category) throws SQLException {
        String sql = "INSERT INTO categories (name, user_id) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, category.getName());
            pstmt.setInt(2, category.getUserID());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public static List<Category> getCategoriesByUserId(int userID) throws SQLException {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT id, name, user_id FROM categories WHERE user_id = ? ORDER BY name";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Category category = new Category(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("user_id")
                );
                categories.add(category);
            }
        }

        return categories;
    }

    public static boolean updateCategory(Category category) throws SQLException {
        String sql = "UPDATE categories SET name = ? WHERE id = ? AND user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, category.getName());
            pstmt.setInt(2, category.getId());
            pstmt.setInt(3, category.getUserID());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public static boolean deleteCategory(int categoryId, int userID) throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM tasks WHERE category_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setInt(1, categoryId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                return false; // Kategoria ma przypisane zadania
            }
        }

        String deleteSql = "DELETE FROM categories WHERE id = ? AND user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(deleteSql)) {

            pstmt.setInt(1, categoryId);
            pstmt.setInt(2, userID);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public static boolean categoryExists(String name, int userID) throws SQLException {
        String sql = "SELECT COUNT(*) FROM categories WHERE name = ? AND user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setInt(2, userID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        }
    }

    public static int getTaskCount(int categoryId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM tasks WHERE category_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, categoryId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }
}
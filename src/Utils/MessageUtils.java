package Utils;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class MessageUtils {
    public static void showError(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Błąd", JOptionPane.ERROR_MESSAGE);
    }

    public static void showSuccess(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Sukces", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showDatabaseError(Component parent, SQLException e) {
        JOptionPane.showMessageDialog(parent,
                "Błąd bazy danych: " + e.getMessage(),
                "Błąd", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }

    public static void showDatabaseError(Component parent, SQLException e, String message) {
        JOptionPane.showMessageDialog(parent,
                message + " " + e.getMessage(),
                "Błąd", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}
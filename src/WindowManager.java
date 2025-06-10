import javax.swing.*;
import java.awt.*;

public class WindowManager {
    public static void switchToLogin(JFrame currentWindow) {
        currentWindow.dispose();
        new LoginMenu().setVisible(true);
    }

    public static void switchToRegister(JFrame currentWindow) {
        currentWindow.dispose();
        new RegisterMenu().setVisible(true);
    }

    public static void switchToDashboard(JFrame currentWindow, int userID) {
        currentWindow.dispose();
        new Dashboard(userID).setVisible(true);
    }

    public static boolean confirmLogout(Component parent) {
        int result = JOptionPane.showConfirmDialog(
                parent,
                "Czy na pewno chcesz się wylogować?",
                "Potwierdzenie wylogowania",
                JOptionPane.YES_NO_OPTION
        );
        return result == JOptionPane.YES_OPTION;
    }
}
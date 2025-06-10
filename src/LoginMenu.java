import Database.UserDAO;
import Designs.buttonStyler;
import Fonts.FontAwesome;
import Utils.MessageUtils;
import Utils.UIUtils;
import Utils.ValidationResult;
import Utils.ValidationUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

public class LoginMenu extends JFrame{
    private JPanel mainFrame;
    private JTextField loginField;
    private JPasswordField passwordField;
    private JLabel iconLabel;
    private JLabel registerLabel;
    private JButton loginButton;
    private JLabel passEye;

    private char defaultEchoChar;
    private boolean[] isPassVisible = {false};

    private ImageIcon iconTM = new ImageIcon(getClass().getResource("Figures/TaskMaster.png"));

    private static final String LOGIN = "admin";
    private static final String PASSWORD = "admin";

    public LoginMenu() {
        super("Menu");
        this.setContentPane(mainFrame);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int width = 500, height = 500;
        this.setSize(width,height);

        buttonStyler.styleButton(loginButton);

        iconLabel.setIcon(UIUtils.resize(iconTM,150,150));

        passEye.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        passEye.setPreferredSize(new Dimension(16,16));
        FontAwesome.setLabelIcon(passEye, FontAwesome.Icons.EYE,16f);
        defaultEchoChar = passwordField.getEchoChar();

        UIUtils.setupLinkLabel(registerLabel);
        UIUtils.addLinkHoverEffect(registerLabel);

        UIUtils.setupPasswordToggle(passEye,passwordField,isPassVisible,0,defaultEchoChar);

        this.getRootPane().setDefaultButton(loginButton); // przycisk 'enter' wciska loginButton

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String loginInput = loginField.getText();
                char[] passwordChars = passwordField.getPassword();
                String passwordInput = new String(passwordChars);

                ValidationResult validationResult = ValidationUtils.validateLogin(loginInput,passwordInput);
                if(!validationResult.isValid()){
                    String error = validationResult.getErrorMessage();
                    MessageUtils.showError(LoginMenu.this,error);
                    return;
                }

                try {
                    int userID = UserDAO.authenticateUser(loginInput.trim(), passwordInput.trim());
                    if (userID != -1) {
                        String welcomeMessage = userID == 1 ? "Zalogowano jako admin" : "Zalogowano jako " + loginInput.trim();
                        MessageUtils.showSuccess(LoginMenu.this, welcomeMessage);
                        WindowManager.switchToDashboard(LoginMenu.this, userID);
                    } else {
                        MessageUtils.showError(LoginMenu.this, "Nieprawidłowy login lub hasło");
                        passwordField.setText("");
                        loginField.requestFocus();
                    }
                } catch (SQLException e) {
                    MessageUtils.showDatabaseError(LoginMenu.this, e);
                }
            }
        });

        registerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                WindowManager.switchToRegister(LoginMenu.this);
            }
        });
    }
}
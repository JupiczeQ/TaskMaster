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

public class RegisterMenu extends JFrame{
    private JPanel mainFrame;
    private JTextField loginField;
    private JPasswordField confirmPasswordField;
    private JButton registerButton;
    private JPasswordField passwordField;
    private JLabel iconLabel;
    private JLabel loginLabel;
    private JLabel confirmPassEye;
    private JLabel passEye;
    private char defaultEchoChar;
    private boolean[] isPassVisible = {false,false};


    private ImageIcon iconTM = new ImageIcon(getClass().getResource("Figures/TaskMaster.png"));

    public RegisterMenu() {
        super("Register Menu");
        this.setContentPane(mainFrame);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int width = 500, height = 600;
        this.setSize(width,height);

        buttonStyler.styleButton(registerButton);

        iconLabel.setIcon(UIUtils.resize(iconTM,150,150));

        passEye.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        confirmPassEye.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        passEye.setPreferredSize(new Dimension(16,16));
        confirmPassEye.setPreferredSize(new Dimension(16,16));
        FontAwesome.setLabelIcon(passEye, FontAwesome.Icons.EYE,16f);
        FontAwesome.setLabelIcon(confirmPassEye, FontAwesome.Icons.EYE,16f);
        defaultEchoChar = passwordField.getEchoChar();

        UIUtils.setupLinkLabel(loginLabel);
        UIUtils.addLinkHoverEffect(loginLabel);

        UIUtils.setupPasswordToggle(passEye,passwordField,isPassVisible,0,defaultEchoChar);
        UIUtils.setupPasswordToggle(confirmPassEye,confirmPasswordField,isPassVisible,1,defaultEchoChar);

        this.getRootPane().setDefaultButton(registerButton);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String loginInput = loginField.getText();
                char[] passwordChars = passwordField.getPassword();
                char[] confirmPasswordChars = confirmPasswordField.getPassword();
                String passwordInput = new String(passwordChars);
                String confirmPasswordInput = new String(confirmPasswordChars);

                ValidationResult validationResult = ValidationUtils.validateRegistration(loginInput,passwordInput,confirmPasswordInput);
                if(!validationResult.isValid()){
                    String error = validationResult.getErrorMessage();
                    MessageUtils.showError(RegisterMenu.this, error);
                    return;
                }

                try {
                    if (UserDAO.userExists(loginInput.trim())) {
                        MessageUtils.showError(RegisterMenu.this, "Użytkownik o takiej nazwie już istnieje!");
                        loginField.requestFocus();
                        loginField.selectAll();
                        return;
                    }

                    boolean success = UserDAO.registerUser(loginInput.trim(), passwordInput);

                    if (success) {
                        MessageUtils.showSuccess(RegisterMenu.this, "Konto zostało utworzone pomyślnie!\nMożesz się teraz zalogować.");

                        loginField.setText("");
                        passwordField.setText("");
                        confirmPasswordField.setText("");

                        WindowManager.switchToLogin(RegisterMenu.this);
                    } else {
                        MessageUtils.showError(RegisterMenu.this, "Wystąpił błąd podczas tworzenia konta.");
                    }

                } catch (SQLException e) {
                    MessageUtils.showDatabaseError(RegisterMenu.this,e);
                }
            }
        });
        loginLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                WindowManager.switchToLogin(RegisterMenu.this);
            }
        });
    }
}

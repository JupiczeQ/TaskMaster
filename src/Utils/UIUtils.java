package Utils;

import Fonts.FontAwesome;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;

public class UIUtils {
    private static final Color LINKCOLOR = new Color(59,130,246);
    private static final Color HLCOLOR = new Color(245,101,101);

    public static ImageIcon resize(ImageIcon src, int width, int height) {
        return new ImageIcon(src.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }

    public static void setupLinkLabel(JLabel label) {
        label.setForeground(LINKCOLOR);
        Font originalFont = label.getFont();
        Map<TextAttribute, Object> attr = new HashMap<>(originalFont.getAttributes());
        attr.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        label.setFont(originalFont.deriveFont(attr));
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public static void addLinkHoverEffect(JLabel label) {
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                label.setForeground(HLCOLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                label.setForeground(LINKCOLOR);
            }
        });
    }

    public static void setupPasswordToggle(JLabel eyeLabel, JPasswordField passwordField, boolean[] visibilityState, int index, char defaultEchoChar) {
        eyeLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        eyeLabel.setPreferredSize(new Dimension(16, 16));
        FontAwesome.setLabelIcon(eyeLabel, FontAwesome.Icons.EYE, 16f);

        eyeLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (visibilityState[index]) {
                    visibilityState[index] = false;
                    passwordField.setEchoChar(defaultEchoChar);
                    FontAwesome.setLabelIcon(eyeLabel, FontAwesome.Icons.EYE, 16f);
                } else {
                    visibilityState[index] = true;
                    passwordField.setEchoChar((char) 0);
                    FontAwesome.setLabelIcon(eyeLabel, FontAwesome.Icons.EYE_SLASH, 16f);
                }
            }
        });
    }
}

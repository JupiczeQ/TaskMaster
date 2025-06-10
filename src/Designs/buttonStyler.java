package Designs;

import Fonts.FontAwesome;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class buttonStyler {
    private static final Color WHITE = new Color(255, 255, 255);
    private static final Color BLUE = new Color(29, 78, 216);

    public static void styleButton(JButton button) {
        styleButton(button, null, null);
    }

    public static String createMixedText(String unicode, String regularText) {
        return String.format(
                "<html><span style='font-family: \"%s\";'>%s</span>" +
                        "<span style='font-family: Dialog;'> %s</span></html>",
                FontAwesome.getSolidFont().getFontName(), unicode, regularText
        );
    }

    public static void styleButton(JButton button, String unicode, String text) {
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(8, 12, 8, 12));
        button.setBackground(WHITE);
        button.setForeground(BLUE);
        button.setFont(new Font(Font.DIALOG, Font.PLAIN, 16));

        if (unicode != null && text != null) {
            button.setText(createMixedText(unicode, text));
        } else if (unicode != null && text == null) {
            button.setText(unicode);
        }

    }
}
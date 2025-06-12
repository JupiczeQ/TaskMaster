package Fonts;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

public class FontAwesome {
    private static Font solidFont;

    static {
        loadFont();
    }

    private static void loadFont() {
        try {
            InputStream fontStream = FontAwesome.class.getResourceAsStream("fa-solid-900.otf");
            if (fontStream == null) {
                System.err.println("Nie znaleziono czcionki Font Awesome");
                solidFont = new Font(Font.SANS_SERIF, Font.PLAIN, 16);
                return;
            }

            solidFont = Font.createFont(Font.TRUETYPE_FONT, fontStream);
            solidFont = solidFont.deriveFont(16f);

            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(solidFont);

            fontStream.close();

        } catch (Exception e) {
            System.err.println("Błąd ładowania Font Awesome: " + e.getMessage());
            solidFont = new Font(Font.SANS_SERIF, Font.PLAIN, 16);
        }
    }

    public static JLabel createIcon(String unicode, float size) {
        return createIcon(unicode, size, Color.DARK_GRAY);
    }

    public static JLabel createIcon(String unicode, float size, Color color) {
        JLabel label = new JLabel(unicode);
        label.setFont(solidFont.deriveFont(size));
        label.setForeground(color);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    public static Font getFont(float size){
        return solidFont.deriveFont(size);
    }

    public static Font getSolidFont() {
        return solidFont;
    }

    public static void setLabelIcon(JLabel label, String unicode, float size) {
        setLabelIcon(label, unicode, size, Color.DARK_GRAY);
    }

    public static void setLabelIcon(JLabel label, String unicode, float size, Color color) {
        label.setText(unicode);
        label.setFont(solidFont.deriveFont(size));
        label.setForeground(color);
        label.setHorizontalAlignment(SwingConstants.CENTER);
    }

    public static class Icons {
        public static final String EYE = "\uf06e";
        public static final String EYE_SLASH = "\uf070";
        public static final String USER = "\uf007";
        public static final String SIGN_OUT_ALT = "\uf2f5";

        public static final String PLUS = "\uf067";
        public static final String CHECK = "\uf00c";
        public static final String FILTER = "\uf0b0";
    }
}
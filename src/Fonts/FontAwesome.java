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
        public static final String LOCK = "\uf023";
        public static final String SIGN_OUT_ALT = "\uf2f5";
        public static final String COG = "\uf013";
        public static final String BELL = "\uf0f3";

        public static final String HOME = "\uf015";
        public static final String TASKS = "\uf0ae";
        public static final String FOLDER = "\uf07b";
        public static final String TAG = "\uf02b";
        public static final String CALENDAR_ALT = "\uf073";
        public static final String CHART_BAR = "\uf080";

        public static final String PLUS = "\uf067";
        public static final String EDIT = "\uf044";
        public static final String TRASH_ALT = "\uf2ed";
        public static final String CHECK = "\uf00c";
        public static final String TIMES = "\uf00d";
        public static final String SEARCH = "\uf002";
        public static final String FILTER = "\uf0b0";

        public static final String BARS = "\uf0c9";
        public static final String ELLIPSIS_V = "\uf142";
        public static final String ARROW_LEFT = "\uf060";
        public static final String ARROW_RIGHT = "\uf061";
        public static final String CHEVRON_UP = "\uf077";
        public static final String CHEVRON_DOWN = "\uf078";

        public static final String EXCLAMATION_TRIANGLE = "\uf071";
        public static final String INFO_CIRCLE = "\uf05a";
        public static final String CHECK_CIRCLE = "\uf058";
        public static final String TIMES_CIRCLE = "\uf057";

        public static final String SAVE = "\uf0c7";
        public static final String DOWNLOAD = "\uf019";
        public static final String UPLOAD = "\uf093";
        public static final String COPY = "\uf0c5";
        public static final String PRINT = "\uf02f";
        public static final String SHARE = "\uf064";
    }
}
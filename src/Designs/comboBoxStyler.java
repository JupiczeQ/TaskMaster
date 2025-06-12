package Designs;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;

public class comboBoxStyler {
    private static final Color WHITE = new Color(255, 255, 255);
    private static final Color BLUE = new Color(29, 78, 216);
    private static final Color LIGHT_BLUE = new Color(219, 234, 254);



    public static void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setBackground(WHITE);
        comboBox.setForeground(BLUE);
        comboBox.setFont(new Font(Font.DIALOG, Font.PLAIN, 16));
        comboBox.setCursor(new Cursor(Cursor.HAND_CURSOR));
        comboBox.setOpaque(true);

        comboBox.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = new JButton("▼");
                button.setFont(new Font(Font.DIALOG, Font.PLAIN, 12));
                button.setForeground(BLUE);
                button.setBackground(WHITE);
                button.setBorder(new EmptyBorder(0, 4, 0, 4));
                button.setContentAreaFilled(false);
                button.setFocusPainted(false);
                button.setOpaque(true);
                return button;
            }

            @Override
            public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus) {
                ListCellRenderer renderer = comboBox.getRenderer();
                Component c = renderer.getListCellRendererComponent(
                        listBox, comboBox.getSelectedItem(), -1, false, false);

                if (c instanceof JLabel) {
                    ((JLabel) c).setForeground(BLUE);
                    ((JLabel) c).setBackground(WHITE);
                    ((JLabel) c).setOpaque(false);
                }

                c.setFont(comboBox.getFont());

                currentValuePane.paintComponent(g, c, comboBox, bounds.x, bounds.y,
                        bounds.width, bounds.height, false);
            }
        });

        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                setFont(new Font(Font.DIALOG, Font.PLAIN, 16));
                setBorder(new EmptyBorder(8, 12, 8, 12));
                setOpaque(true);

                if (isSelected) {
                    setBackground(LIGHT_BLUE);
                    setForeground(BLUE);
                } else {
                    setBackground(WHITE);
                    setForeground(BLUE);
                }

                return this;
            }
        });
    }
}
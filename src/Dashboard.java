import Database.TaskDAO;
import Database.UserDAO;
import Designs.buttonStyler;
import Fonts.FontAwesome;
import Models.Task;
import RightPanels.CategoriesPanel;
import RightPanels.TasksPanel;
import Utils.UIUtils;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;


public class Dashboard extends JFrame{
    private JPanel mainFrame;
    private JButton tasksButton;
    private JButton categoriesButton;
    private JLabel iconLabel;
    private JLabel userLabel;
    private JLabel signOutLabel;
    private JPanel rightPanel;
    private JPanel statsPanel;
    private JLabel totalTasksLabel;
    private JLabel highPriorityLabel;
    private JLabel mediumPriorityLabel;
    private JLabel lowPriorityLabel;
    private TasksPanel tasksPanel;
    private CategoriesPanel categoriesPanel;
    private CardLayout cardLayout;
    private JButton activeButton;
    private static final Color LIGHT_BLUE = new Color(219, 234, 254);
    private static final Color HIGH_PRIORITY_COLOR = new Color(185, 28, 28);
    private static final Color MEDIUM_PRIORITY_COLOR = new Color(161, 98, 7);
    private static final Color LOW_PRIORITY_COLOR = new Color(21, 128, 61);
    private String username;
    private int userID;
    private boolean isAdmin;

    private boolean checkIfAdmin(int userID) {
        return userID == 1; // Admin ma ID = 1
    }

    private ImageIcon iconTM = new ImageIcon(getClass().getResource("Figures/TaskMaster.png"));

    public void updateStats() {
        try {
            List<Task> allTasks;

            if (isAdmin) {
                allTasks = TaskDAO.getAllTasks();
                categoriesButton.setVisible(false);
            } else {
                allTasks = TaskDAO.getTasksByUserId(userID);
            }
            int totalTasks = allTasks.size();

            int highCount = 0, mediumCount = 0, lowCount = 0;

            for (Task task : allTasks) {
                switch (task.getPriority()) {
                    case HIGH:
                        highCount++;
                        break;
                    case MEDIUM:
                        mediumCount++;
                        break;
                    case LOW:
                        lowCount++;
                        break;
                }
            }

            totalTasksLabel.setText("Łączna ilość zadań: " + totalTasks);
            highPriorityLabel.setText("Wysoki priorytet: " + highCount);
            mediumPriorityLabel.setText("Średni priorytet: " + mediumCount);
            lowPriorityLabel.setText("Niski priorytet: " + lowCount);

        } catch (SQLException e) {
            e.printStackTrace();
            totalTasksLabel.setText("Łączna ilość zadań: 0");
            highPriorityLabel.setText("Wysoki priorytet: 0");
            mediumPriorityLabel.setText("Średni priorytet: 0");
            lowPriorityLabel.setText("Niski priorytet: 0");
        }
    }

    private void createStatsPanel() {
        Font titleFont = new Font(Font.DIALOG, Font.BOLD, 20);
        Font statsFont = new Font(Font.DIALOG, Font.PLAIN, 16);

        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setBackground(Color.WHITE);
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Statystyki");
        titledBorder.setTitleFont(titleFont);
        statsPanel.setBorder(titledBorder);

        totalTasksLabel = new JLabel("Łączna ilość zadań: 0");
        highPriorityLabel = new JLabel("Wysoki priorytet: 0");
        mediumPriorityLabel = new JLabel("Średni priorytet: 0");
        lowPriorityLabel = new JLabel("Niski priorytet: 0");

        totalTasksLabel.setForeground(Color.BLACK);
        highPriorityLabel.setForeground(HIGH_PRIORITY_COLOR);
        mediumPriorityLabel.setForeground(MEDIUM_PRIORITY_COLOR);
        lowPriorityLabel.setForeground(LOW_PRIORITY_COLOR);

        totalTasksLabel.setFont(statsFont);
        highPriorityLabel.setFont(statsFont);
        mediumPriorityLabel.setFont(statsFont);
        lowPriorityLabel.setFont(statsFont);

        statsPanel.add(totalTasksLabel);
        statsPanel.add(highPriorityLabel);
        statsPanel.add(mediumPriorityLabel);
        statsPanel.add(lowPriorityLabel);

    }

    private void createPanels(){
        cardLayout = new CardLayout();
        rightPanel.setLayout(cardLayout);

        tasksPanel = new TasksPanel(userID, isAdmin);
        categoriesPanel = new CategoriesPanel(userID);

        rightPanel.add(tasksPanel, "TASKS");
        rightPanel.add(categoriesPanel, "CATEGORIES");

        cardLayout.show(rightPanel, "TASKS");

        activeButton = tasksButton;
    }

    private void setActiveButton(JButton button, String panelName) {
        if ("CATEGORIES".equals(panelName)) {
            categoriesPanel.refreshCategories();
        }
        if("TASKS".equals(panelName)) {
            tasksPanel.loadTasks();
        }
        if (activeButton != null) {
            activeButton.setBackground(Color.WHITE);
        }

        updateStats();

        button.setBackground(LIGHT_BLUE);
        activeButton = button;

        cardLayout.show(rightPanel, panelName);

        button.repaint();
    }

    private void setupButtonListeners(){
        tasksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setActiveButton(tasksButton, "TASKS");
            }
        });

        categoriesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setActiveButton(categoriesButton, "CATEGORIES");
            }
        });
    }

    private void setupDesigns(){
        userLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        signOutLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        String tasksButtonText = isAdmin ? " Wszystkie\nzadania" : " Moje\nzadania";
        buttonStyler.styleButton(tasksButton,FontAwesome.Icons.CHECK,tasksButtonText);
        buttonStyler.styleButton(categoriesButton,FontAwesome.Icons.FILTER," Kategorie");

        Dimension buttonSize = new Dimension(140, 50);
        tasksButton.setPreferredSize(buttonSize);
        tasksButton.setMinimumSize(buttonSize);
        tasksButton.setMaximumSize(buttonSize);

        tasksButton.setBackground(LIGHT_BLUE);

        FontAwesome.setLabelIcon(userLabel, FontAwesome.Icons.USER, 16f);
        FontAwesome.setLabelIcon(signOutLabel, FontAwesome.Icons.SIGN_OUT_ALT, 16f);
    }

    public Dashboard(int userID){
        super("DashBoard");
        this.setContentPane(mainFrame);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int width = 1080, height = 800;
        this.setSize(width,height);

        this.userID = userID;
        this.isAdmin = checkIfAdmin(userID);

        createPanels();
        createStatsPanel();
        setupButtonListeners();
        setupDesigns();

        username = UserDAO.getUsernameByID(userID);
        userLabel.setText(buttonStyler.createMixedText(userLabel.getText(),username));
        userLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        iconLabel.setIcon(UIUtils.resize(iconTM,150,150));

        updateStats();

        signOutLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(WindowManager.confirmLogout(Dashboard.this)){
                    WindowManager.switchToLogin(Dashboard.this);
                }
            }
        });
    }
}
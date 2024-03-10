package guipack;

import javax.swing.*;

import appstructure.IMDB;
import java.awt.*;

public class MainWindow extends JFrame {
    private IMDB imdb = IMDB.getInstance();
    private OptionsPanel options_panel;
    private ActionPanel action_panel;
    private static final Dimension frame_size = new Dimension(1280, 720);
    public OptionsPanel getOptionsPanel() {
        return options_panel;
    }
    public ActionPanel getActionPanel() {
        return action_panel;
    }
    public static Dimension getFrameSize() {
        return frame_size;
    }
    public MainWindow() {
        this.setTitle("IMDB");
        this.setSize(frame_size);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setBackground(Color.GRAY);
        this.setLayout(null);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        options_panel = new OptionsPanel();
        this.add(options_panel);

        action_panel = new ActionPanel();
        this.add(action_panel);

        this.closeMainWindow();
    }
    public void openMainWindow() {
        prepareMainWindowForUser();
        this.show();
    }
    public void closeMainWindow() {
        this.dispose();
    }
    public void prepareMainWindowForUser () {
        this.getOptionsPanel().setUserMode(imdb.getLoggedUser().getAccountType());
        this.getActionPanel().setActionMode(GuiMode.PERSONAL_DATA_MODE);
//        this.getActionPanel().getFavouritesPanel().prepareDataForUser();
//        this.getActionPanel().getNotificationsPanel().prepareDataForUser();
//        this.getActionPanel().getRequestsPanel().prepareDataForUser();
    }
}

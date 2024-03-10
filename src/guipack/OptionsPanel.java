package guipack;

import javax.swing.*;

import appstructure.IMDB;
import enumpack.AccountType;
import java.awt.*;


public class OptionsPanel extends JPanel {
    private JButton logout_button;
    private JButton view_personal_data_button;
    private JButton productions_button;
    private JButton actors_button;
    private JButton favourites_button;
    private JButton notifications_button;
    private JButton requests_button;
    private JButton users_button;
    private boolean cant_logout = false;
    public void setCantLogout(boolean cant_logout) {
        this.cant_logout = cant_logout;
    }
    public JButton getLogoutButton() {
        return logout_button;
    }
    public JButton getViewPersonalDataButton() {
        return view_personal_data_button;
    }
    public JButton getProductionsButton() {
        return productions_button;
    }
    public JButton getActorsButton() {
        return actors_button;
    }
    public JButton getFavouritesButton() {
        return favourites_button;
    }
    public JButton getNottificationsButton() {
        return notifications_button;
    }
    public JButton getRequestsButton() {
        return requests_button;
    }
    public JButton getUserButton() {
        return users_button;
    }
    public OptionsPanel() {
        this.setBounds(0, 0, 1280, 60);
        this.setBackground(Color.LIGHT_GRAY);
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        this.setLayout(new GridLayout(1, 8, 10, 10));
        this.setVisible(true);

        logout_button = new JButton("Logout");
        logout_button.addActionListener(ActionListener -> logout());
        this.add(logout_button);
        view_personal_data_button = new JButton("View personal data");
        view_personal_data_button.addActionListener(e -> handleViewPersonalDataButton());
        this.add(view_personal_data_button);
        productions_button = new JButton("Productions");
        productions_button.addActionListener(e -> handleProductionsButton());
        this.add(productions_button);
        actors_button = new JButton("Actors");
        actors_button.addActionListener(e -> handleActorsButton());
        this.add(actors_button);
        favourites_button = new JButton("Favourites");
        favourites_button.addActionListener(e -> handleFavouritesButton());
        this.add(favourites_button);
        notifications_button = new JButton("Notifications");
        notifications_button.addActionListener(e -> handleNotificationsButton());
        this.add(notifications_button);
        requests_button = new JButton("Requests");
        requests_button.addActionListener(e -> handleRequestsButton());
        this.add(requests_button);
        users_button = new JButton("Users");
        users_button.addActionListener(e -> handleUsersButton());
        this.add(users_button);
    }
    public void setUserMode(AccountType account_type) {
        switch (account_type) {
            case ADMIN:
                users_button.setEnabled(true);
                break;
            case CONTRIBUTOR:
                users_button.setEnabled(false);
                break;
            case REGULAR:
                users_button.setEnabled(false);
                break;
        }
    }
    private void logout() {
        if (cant_logout == true) {
            JOptionPane.showMessageDialog(null, "Close other windows first", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        IMDB.getInstance().setLoggedUser(null);
        GUI.getMainWindow().closeMainWindow();
        GUI.getLoginWindow().openLoginWindow();
    }
    private void handleViewPersonalDataButton() {
        GUI.getMainWindow().getActionPanel().setActionMode(GuiMode.PERSONAL_DATA_MODE);
    }
    private void handleProductionsButton() {
        GUI.getMainWindow().getActionPanel().setActionMode(GuiMode.PRODUCTION_MODE);
    }
    private void handleActorsButton() {
        GUI.getMainWindow().getActionPanel().setActionMode(GuiMode.ACTOR_MODE);
    }
    private void handleFavouritesButton() {
        GUI.getMainWindow().getActionPanel().getFavouritesPanel().prepareDataForUser();
        GUI.getMainWindow().getActionPanel().setActionMode(GuiMode.FAVOURITE_MODE);
    }
    private void handleNotificationsButton() {
        GUI.getMainWindow().getActionPanel().getNotificationsPanel().prepareDataForUser();
        GUI.getMainWindow().getActionPanel().setActionMode(GuiMode.NOTIFICATION_MODE);
    }
    private void handleRequestsButton() {
        GUI.getMainWindow().getActionPanel().getRequestsPanel().prepareDataForUser();
        GUI.getMainWindow().getActionPanel().setActionMode(GuiMode.REQUEST_MODE);
    }
    private void handleUsersButton() {
        GUI.getMainWindow().getActionPanel().getUsersPanel().prepareDataForUser();
        GUI.getMainWindow().getActionPanel().setActionMode(GuiMode.USER_MODE);
    }

}

package guipack;

import javax.swing.*;

import appstructure.IMDB;
import enumpack.AccountType;
import enumpack.Genre;
import enumpack.RequestTypes;
import java.awt.*;
import observerpack.Request;
import usefulpack.ComparableItem;
import userpack.RequestManager;
import userpack.Staff;

public class ActionPanel extends JPanel{
    private GuiMode mode;
    private PersonalDataPanel personal_data_panel;
    private ProductionsPanel productions_panel;
    private ActorsPanel actors_panel;
    private FavouritesPanel favourites_panel;
    private NotificationsPanel notifications_panel;
    private RequestsPanel requests_panel;
    private UsersPanel users_panel;
    private JScrollPane items_display;
    private JPanel action_options;
    private JLabel search_prompt;
    private JTextField search_field;
    private JComboBox<String> production_view_option;
    private JComboBox<String> actor_view_option;
    private JButton search_button;
    private JButton add_button;
    public ProductionsPanel getProductionsPanel() {
        return productions_panel;
    }
    public ActorsPanel getActorsPanel() {
        return actors_panel;
    }
    public  FavouritesPanel getFavouritesPanel() {
        return favourites_panel;
    }
    public NotificationsPanel getNotificationsPanel() {
        return notifications_panel;
    }
    public RequestsPanel getRequestsPanel() {
        return requests_panel;
    }
    public UsersPanel getUsersPanel() {
        return users_panel;
    }
    public ActionPanel () {
        this.setBounds(0, 60, 1280, 660);
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        this.setLayout(null);
        this.setVisible(true);

        personal_data_panel = new PersonalDataPanel();
        productions_panel = new ProductionsPanel();
        actors_panel = new ActorsPanel();
        favourites_panel = new FavouritesPanel();
        notifications_panel = new NotificationsPanel();
        requests_panel = new RequestsPanel();
        users_panel = new UsersPanel();

        items_display = new JScrollPane();
        items_display.setBounds(0, 0, 1270, 560);
        items_display.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        items_display.setVisible(true);
        UIManager.put("ScrollBar.width", 20);
        this.add(items_display);

        action_options = new JPanel();
        action_options.setBounds(0, 560, 1280, 100);
        action_options.setBackground(Color.LIGHT_GRAY);
        action_options.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        action_options.setVisible(true);
        this.add(action_options);

        add_button = new JButton("Add");
        add_button.setFont(new Font("Serif", Font.PLAIN, 20));
        add_button.setPreferredSize(new Dimension(200, 40));
        add_button.addActionListener(e -> HandleAddButton());
        action_options.add(add_button);

        search_prompt = new JLabel("Name:");
        search_prompt.setFont(new Font("Serif", Font.PLAIN, 20));
        action_options.add(search_prompt);

        search_field = new JTextField();
        search_field.setFont(new Font("Serif", Font.PLAIN, 20));
        search_field.setPreferredSize(new Dimension(200, 40));
        action_options.add(search_field);

        search_button = new JButton("Search");
        search_button.setFont(new Font("Serif", Font.PLAIN, 20));
        search_button.setPreferredSize(new Dimension(200, 40));
        search_button.addActionListener(e -> HandleSearchButton());
        action_options.add(search_button);

        production_view_option = new JComboBox<>();
        production_view_option.setFont(new Font("Serif", Font.PLAIN, 20));
        production_view_option.setPreferredSize(new Dimension(200, 40));
        production_view_option.addItem("All Productions");
        production_view_option.addItem("Filter by Genre");
        production_view_option.addItem("Filter by number of ratings");
        production_view_option.addActionListener(e -> HandleProductionViewOption());
        action_options.add(production_view_option);

        actor_view_option = new JComboBox<>();
        actor_view_option.setFont(new Font("Serif", Font.PLAIN, 20));
        actor_view_option.setPreferredSize(new Dimension(200, 40));
        actor_view_option.addItem("All Actors");
        actor_view_option.addItem("View sorted actors");
        actor_view_option.addActionListener(e -> HandleActorViewOption());
        action_options.add(actor_view_option);
    }
    public void setActionMode(GuiMode mode) {
        this.mode = mode;
        switch (mode) {
            case PERSONAL_DATA_MODE:
                personal_data_panel.updatePersonalDataText();
                items_display.setViewportView(personal_data_panel);
                JViewport viewport = items_display.getViewport();
                viewport.setViewPosition(new Point(0, 0));
                actor_view_option.setVisible(false);
                production_view_option.setVisible(false);
                add_button.setVisible(false);
                search_prompt.setVisible(false);
                search_field.setVisible(false);
                search_button.setVisible(false);
                break;
            case PRODUCTION_MODE:
                items_display.setViewportView(productions_panel);
                actor_view_option.setVisible(false);
                production_view_option.setVisible(true);
                add_button.setText("Add a production");
                add_button.setVisible(true);
                search_prompt.setVisible(true);
                search_field.setVisible(true);
                search_button.setVisible(true);
                break;
            case ACTOR_MODE:
                items_display.setViewportView(actors_panel);
                production_view_option.setVisible(false);
                actor_view_option.setVisible(true);
                add_button.setText("Add an actor");
                add_button.setVisible(true);
                search_prompt.setVisible(true);
                search_field.setVisible(true);
                search_button.setVisible(true);
                break;
            case FAVOURITE_MODE:
                items_display.setViewportView(favourites_panel);
                actor_view_option.setVisible(false);
                production_view_option.setVisible(false);
                add_button.setVisible(false);
                search_prompt.setVisible(false);
                search_field.setVisible(false);
                search_button.setVisible(false);
                break;
            case NOTIFICATION_MODE:
                items_display.setViewportView(notifications_panel);
                actor_view_option.setVisible(false);
                production_view_option.setVisible(false);
                add_button.setVisible(false);
                search_prompt.setVisible(false);
                search_field.setVisible(false);
                search_button.setVisible(false);
                break;
            case REQUEST_MODE:
                items_display.setViewportView(requests_panel);
                actor_view_option.setVisible(false);
                production_view_option.setVisible(false);
                add_button.setText("Add a request");
                add_button.setVisible(true);
                search_prompt.setVisible(false);
                search_field.setVisible(false);
                search_button.setVisible(false);
                break;
            case USER_MODE:
                items_display.setViewportView(users_panel);
                actor_view_option.setVisible(false);
                production_view_option.setVisible(false);
                add_button.setText("Add a user");
                add_button.setVisible(true);
                search_prompt.setVisible(false);
                search_field.setVisible(false);
                search_button.setVisible(false);
                break;
            default:
                break;
        }
        action_options.revalidate();
        action_options.repaint();
    }

    public void HandleProductionViewOption() {
        switch (production_view_option.getSelectedIndex()) {
            case 0:
                productions_panel.ShowAllProductions();
                break;
            case 1:
                String genre_string = JOptionPane.showInputDialog(null, "Enter genre:",
                        "Genre selection", JOptionPane.QUESTION_MESSAGE);
                Genre picked_genre;
                try {
                    picked_genre = Genre.valueOf(genre_string.toUpperCase());
                } catch (IllegalArgumentException e) {
                    JOptionPane.showMessageDialog(null, "Invalid genre!", "Error", JOptionPane.ERROR_MESSAGE);
                    productions_panel.ShowAllProductions();
                    break;
                }
                productions_panel.FilterByGenre(picked_genre);
                break;
            case 2:
                String nr_reviews_string = JOptionPane.showInputDialog(null, "Enter minimun number of reviews:",
                        "Minimum selection", JOptionPane.QUESTION_MESSAGE);
                int nr_reviews;
                try {
                    nr_reviews = Integer.parseInt(nr_reviews_string);
                } catch (IllegalArgumentException e) {
                    JOptionPane.showMessageDialog(null, "Invalid genre!", "Error", JOptionPane.ERROR_MESSAGE);
                    productions_panel.ShowAllProductions();
                    break;
                }
                productions_panel.FilterByNumberOfRatings(nr_reviews);
                break;
            default:
                break;
        }
    }
    public void HandleActorViewOption() {
        switch (actor_view_option.getSelectedIndex()) {
            case 0:
                actors_panel.ShowAllActors();
                break;
            case 1:
                actors_panel.ShowSortedActors();
                break;
            default:
                break;
        }
    }
    public void HandleSearchButton() {
        switch (mode) {
            case PRODUCTION_MODE:
                productions_panel.SearchProduction(search_field.getText());
                break;
            case ACTOR_MODE:
                actors_panel.SearchActor(search_field.getText());
                break;
            default:
                break;
        }
    }
    public void HandleAddButton() {
        switch (mode) {
            case PRODUCTION_MODE:
                if (IMDB.getInstance().getLoggedUser().getAccountType() == AccountType.REGULAR) {
                    JOptionPane.showMessageDialog(null, "Only contributors can add productions!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    String type = (String) JOptionPane.showInputDialog(null, "Choose type:", "Type",
                            JOptionPane.PLAIN_MESSAGE, null, new String[]{"Movie", "Series"}, "Movie");
                    new ProductionFrame(null, type);
                }
                break;
            case ACTOR_MODE:
                if (IMDB.getInstance().getLoggedUser().getAccountType() == AccountType.REGULAR) {
                    JOptionPane.showMessageDialog(null, "Only contributors can add actors!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    new ActorFrame(null);
                }
                break;
            case REQUEST_MODE:
                if (IMDB.getInstance().getLoggedUser().getAccountType() == AccountType.ADMIN) {
                    JOptionPane.showMessageDialog(null, "Admins don't need to add requests :)) !", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    Object[] options = {"DELETE_ACCOUNT", "OTHERS"};
                    String type = (String) JOptionPane.showInputDialog(null, "Choose type:", "Type",
                            JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
                    String description = JOptionPane.showInputDialog(null, "Enter description:",
                            "Description", JOptionPane.QUESTION_MESSAGE);
                    Staff<ComparableItem> request_target = IMDB.getInstance().getCommonAdmin();
                    RequestTypes request_type;
                    try {
                        request_type = RequestTypes.valueOf(type);
                    } catch (IllegalArgumentException e) {
                        JOptionPane.showMessageDialog(null, "Invalid request type!", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                    }
                    Request request = new Request(request_type, request_target, description,
                            IMDB.getInstance().getLoggedUser(), description);
                    try {
                        ((RequestManager)IMDB.getInstance().getLoggedUser()).createRequest(request);
                        RequestsPanel requests_panel = GUI.getMainWindow().getActionPanel().getRequestsPanel();
                        requests_panel.AddRequestBox(request, false);
                        requests_panel.revalidate();
                        requests_panel.repaint();
                    } catch (IllegalArgumentException e) {
                        JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                break;
            case USER_MODE:
                if (IMDB.getInstance().getLoggedUser().getAccountType() != AccountType.ADMIN) {
                    JOptionPane.showMessageDialog(null, "You don't have permission to add users", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                new UserFrame(null);
                break;
            default:
                break;
        }
    }
}

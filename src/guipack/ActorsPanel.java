package guipack;

import appstructure.IMDB;
import enumpack.AccountType;
import enumpack.RequestTypes;
import java.util.*;
import java.util.List;
import observerpack.Request;
import productionpack.Actor;
import enumpack.DataInputMode;
import javax.swing.*;
import java.awt.*;
import productionpack.Production;
import usefulpack.ComparableItem;
import usefulpack.UtilFunction;
import userpack.RequestManager;
import userpack.Staff;
import usefulpack.Pair;

public class ActorsPanel extends JPanel {
    private IMDB imdb = IMDB.getInstance();
    private List<ActorContainer> actors_containers;
    public ActorsPanel() {
        actors_containers = new LinkedList<>();
        this.setBackground(Color.BLACK);
        this.setLayout(new GridLayout(0, 1));
        this.setVisible(true);

        for (int i = 0; i < imdb.getActors().size(); i++) {
            addActorBox(imdb.getActors().get(i));
        }
    }
    public void deleteAllActorsFromScreen() {
        Iterator<ActorContainer> iterator= actors_containers.iterator();
        while (iterator.hasNext()) {
            ActorContainer cur = iterator.next();
            iterator.remove();
            this.remove(cur.getActorBox());
        }
    }
    public void addActorBox (Actor actor) {
        JPanel actor_box = new JPanel();
        actor_box.setLayout(new FlowLayout(FlowLayout.LEFT));
        actor_box.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        actor_box.setSize(new Dimension(1270, 300));
        actor_box.setBackground(Color.BLACK);
        actor_box.setVisible(true);

        ImageIcon actor_image = new ImageIcon(actor.getImagePath());
        JLabel actor_image_label = new JLabel(actor_image);
        actor_image_label.setPreferredSize(new Dimension(300, 300));
        actor_image_label.setVisible(true);
        actor_box.add(actor_image_label);

        JTextArea actor_info_text = new JTextArea(actor.toString());
        actor_info_text.setFont(new Font("Serif", Font.PLAIN, 15));
        actor_info_text.setPreferredSize(new Dimension(750, 500));
        actor_info_text.setForeground(Color.WHITE);
        actor_info_text.setBackground(Color.BLACK);
        actor_info_text.setLineWrap(true);
        actor_info_text.setWrapStyleWord(true);
        actor_info_text.setEditable(false);

        JScrollPane actor_info_scroll = new JScrollPane(actor_info_text);
        //actor_info_scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        actor_info_scroll.setPreferredSize(new Dimension(750, 300));
        actor_info_scroll.setVisible(true);
        actor_box.add(actor_info_scroll);

        JPanel actor_options = new JPanel();
        actor_options.setLayout(new GridLayout(0, 1));
        actor_options.setPreferredSize(new Dimension(200, 300));
        actor_options.setBackground(Color.BLACK);
        actor_options.setVisible(true);
        actor_box.add(actor_options);

        JButton add_to_favorites_button = new JButton("Add to favorites");
        add_to_favorites_button.setFont(new Font("Serif", Font.PLAIN, 20));
        add_to_favorites_button.setPreferredSize(new Dimension(200, 60));
        actor_options.add(add_to_favorites_button);

        JButton issue_a_request = new JButton("Issue a request");
        issue_a_request.setFont(new Font("Serif", Font.PLAIN, 20));
        issue_a_request.setPreferredSize(new Dimension(200, 60));
        actor_options.add(issue_a_request);

        JButton modify_actor = new JButton("Modify actor");
        modify_actor.setFont(new Font("Serif", Font.PLAIN, 20));
        modify_actor.setPreferredSize(new Dimension(200, 60));
        actor_options.add(modify_actor);

        JButton remove_actor = new JButton("Remove actor");
        remove_actor.setFont(new Font("Serif", Font.PLAIN, 20));
        remove_actor.setPreferredSize(new Dimension(200, 60));
        actor_options.add(remove_actor);

        JButton add_filmography = new JButton("Add filmography");
        add_filmography.setFont(new Font("Serif", Font.PLAIN, 20));
        add_filmography.setPreferredSize(new Dimension(200, 60));
        actor_options.add(add_filmography);

        ActorContainer actor_container = new ActorContainer(actor_box, actor_info_text, actor);
        add_to_favorites_button.addActionListener(e -> HandleAddFavouriteAction(actor_container));
        issue_a_request.addActionListener(e -> HandleIssueRequestAction(actor_container));
        remove_actor.addActionListener(e -> HandleActorRemoveAction(actor_container));
        modify_actor.addActionListener(e -> HandleActorEditAction(actor_container));
        add_filmography.addActionListener(e -> HandleAddFilmographyAction(actor_container));

        actors_containers.add(actor_container);
        this.add(actor_box);
    }
    public void updateData() {
        for (ActorContainer actorsContainer : actors_containers) {
            actorsContainer.getActorInfoText().setText(actorsContainer.getActorData().toString());
        }
    }
    private void removeActorBox(ActorContainer actor_container) {
        actors_containers.remove(actor_container);
        this.remove(actor_container.getActorBox());
        //Maybe refresh the panel
        //this.updateUI();
    }
    private static class ActorContainer {
        private JPanel actor_box;
        private JTextArea actor_info_text;
        private Actor actor_data;
        private ActorContainer(JPanel actor_box, JTextArea actor_info_text, Actor actor_data) {
            this.actor_box = actor_box;
            this.actor_info_text = actor_info_text;
            this.actor_data = actor_data;
        }
        private JPanel getActorBox() {
            return actor_box;
        }
        private JTextArea getActorInfoText() {
            return actor_info_text;
        }
        private Actor getActorData() {
            return actor_data;
        }
    }
    public void ShowAllActors() {
        this.deleteAllActorsFromScreen();
        for (Actor actor : imdb.getActors()) {
            this.addActorBox(actor);
        }
        this.revalidate();
        this.repaint();
    }
    public void ShowSortedActors() {
        this.deleteAllActorsFromScreen();
        List<ComparableItem> actors_to_sort = new LinkedList<>(imdb.getActors());
        Collections.sort(actors_to_sort);
        for (ComparableItem actor : actors_to_sort) {
            this.addActorBox((Actor) actor);
        }
        this.revalidate();
        this.repaint();
    }
    public void SearchActor(String actor_name) {
        this.deleteAllActorsFromScreen();
        Actor actor = Actor.getActorByName(imdb.getActors(), actor_name);
        if (actor == null) {
            //Show closest matches
            List<ComparableItem> comparableActors = new ArrayList<>();
            comparableActors.addAll(imdb.getActors());
            String best_matches = UtilFunction.getBestMatchesString(actor_name, comparableActors);
            JOptionPane.showMessageDialog(null, "No actor found !\nBest matches:\n"
                    + best_matches, "Nothing found", JOptionPane.INFORMATION_MESSAGE);
            ShowAllActors();
        } else {
            this.addActorBox(actor);
            this.revalidate();
            this.repaint();
        }
    }
    private void HandleAddFavouriteAction (ActorContainer actor_container) {
        try {
            imdb.getLoggedUser().addFavoriteByName(actor_container.getActorData().getStringForSorting());
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        GUI.getMainWindow().getActionPanel().getUsersPanel().updateData();
        //favourites_panel.prepareDataForUser();
    }
    private void HandleIssueRequestAction (ActorContainer actor_container) {
        if (!(imdb.getLoggedUser() instanceof RequestManager)) {
            JOptionPane.showMessageDialog(null, "You can't issue requests as admin!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            String request_description = JOptionPane.showInputDialog(null, "Enter request description:",
                                                                    "Request description", JOptionPane.QUESTION_MESSAGE);
            Request aux_request = new Request(  RequestTypes.ACTOR_ISSUE, actor_container.getActorData().getContributor(),
                                                actor_container.getActorData().getName(), imdb.getLoggedUser(), request_description);
            aux_request.setRequestTimeNow();
            ((RequestManager)imdb.getLoggedUser()).createRequest(aux_request);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void HandleActorRemoveAction (ActorContainer actor_container) {
        if (imdb.getLoggedUser().getAccountType() == AccountType.REGULAR) {
            JOptionPane.showMessageDialog(null, "You can't remove actors as a regular user !", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            ((Staff<ComparableItem>)imdb.getLoggedUser()).removeActorSystem(actor_container.getActorData().getName());
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        removeActorBox(actor_container);
        GUI.getMainWindow().getActionPanel().getProductionsPanel().updateData();
        GUI.getMainWindow().getActionPanel().getUsersPanel().updateData();
        this.revalidate();
        this.repaint();
    }
    private void HandleActorEditAction (ActorContainer actor_container) {
        if (actor_container.getActorData().getContributor() != imdb.getLoggedUser()) {
            if (actor_container.getActorData().getContributor().equals("ADMIN")) {
                if (imdb.getLoggedUser().getAccountType() != AccountType.ADMIN) {
                    JOptionPane.showMessageDialog(null, "You can't edit actors you didn't contribute !",
                                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                JOptionPane.showMessageDialog(null, "You can't edit actors you didn't contribute !",
                                            "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        new ActorFrame(actor_container.getActorData());
    }
    private void HandleAddFilmographyAction (ActorContainer actor_container) {
        if (actor_container.getActorData().getContributor() != imdb.getLoggedUser()) {
            if (actor_container.getActorData().getContributor().equals("ADMIN")) {
                if (imdb.getLoggedUser().getAccountType() != AccountType.ADMIN) {
                    JOptionPane.showMessageDialog(null, "You can't edit actors you didn't contribute !",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                JOptionPane.showMessageDialog(null, "You can't edit actors you didn't contribute !",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        Object[] options = {"Movie", "Series"};
        String type = (String) JOptionPane.showInputDialog(null, "Choose type:", "Type",
                                                            JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        String title = JOptionPane.showInputDialog(null, "Enter title:", "Title", JOptionPane.QUESTION_MESSAGE);
        actor_container.getActorData().addFilmography(new Pair<>(type, title));
        this.updateData();
    }
}

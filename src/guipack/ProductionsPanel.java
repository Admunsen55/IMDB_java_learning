package guipack;

import javax.swing.*;

import appstructure.IMDB;
import enumpack.AccountType;
import enumpack.Genre;
import enumpack.RequestTypes;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import observerpack.Request;
import productionpack.*;
import usefulpack.ComparableItem;
import usefulpack.UtilFunction;
import userpack.RequestManager;
import userpack.Staff;


public class ProductionsPanel extends JPanel{
    private IMDB imdb = IMDB.getInstance();
    private List<ProductionContainer> productions_containers;
    public ProductionsPanel() {
        productions_containers = new LinkedList<>();
        this.setBackground(Color.BLACK);
        this.setLayout(new GridLayout(0, 1));
        this.setVisible(true);

        for (int i = 0; i < imdb.getProductions().size(); i++) {
            addProductionBox(imdb.getProductions().get(i));
        }
    }
    private void deleteAllProductionsFromScreen() {
        Iterator<ProductionContainer> iterator = productions_containers.iterator();
        while (iterator.hasNext()) {
            ProductionContainer cur = iterator.next();
            iterator.remove();
            this.remove(cur.getProductionBox());
        }
    }
    public void addProductionBox (Production production) {
        JPanel production_box = new JPanel();
        production_box.setLayout(new FlowLayout(FlowLayout.LEFT));
        production_box.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        production_box.setPreferredSize(new Dimension(1270, 300));
        production_box.setBackground(Color.BLACK);
        production_box.setVisible(true);

        ImageIcon production_image = new ImageIcon(production.getImagePath());
        JLabel production_image_label = new JLabel(production_image);
        production_image_label.setPreferredSize(new Dimension(300, 300));
        production_image_label.setVisible(true);
        production_box.add(production_image_label);

        int aprox_nr_of_lines;
        if (production instanceof Movie) {
            aprox_nr_of_lines = 1000;
        } else {
            aprox_nr_of_lines = 2000;
        }
        JTextArea production_info_text = new JTextArea(production.toString());
        production_info_text.setFont(new Font("Serif", Font.PLAIN, 15));
        production_info_text.setPreferredSize(new Dimension(750, aprox_nr_of_lines));
        production_info_text.setForeground(Color.WHITE);
        production_info_text.setBackground(Color.BLACK);
        production_info_text.setLineWrap(true);
        production_info_text.setWrapStyleWord(true);
        production_info_text.setEditable(false);

        JScrollPane production_info_scroll = new JScrollPane(production_info_text);
        //production_info_scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        production_info_scroll.setPreferredSize(new Dimension(750, 300));
        production_info_scroll.setVisible(true);
        production_box.add(production_info_scroll);

        JPanel production_options = new JPanel();
        production_options.setLayout(new GridLayout(0, 1));
        production_options.setPreferredSize(new Dimension(200, 300));
        production_options.setBackground(Color.BLACK);
        production_options.setVisible(true);
        production_box.add(production_options);

        int button_height;
        if (production instanceof Movie) {
            button_height = 75;
        } else {
            button_height = 50;
        }

        JButton add_to_favourites_button = new JButton("Add to favourites");
        add_to_favourites_button.setFont(new Font("Serif", Font.PLAIN, 20));
        add_to_favourites_button.setPreferredSize(new Dimension(200, button_height));
        production_options.add(add_to_favourites_button);

        JButton add_a_review = new JButton("Add a review");
        add_a_review.setFont(new Font("Serif", Font.PLAIN, 20));
        add_a_review.setPreferredSize(new Dimension(200, button_height));
        production_options.add(add_a_review);

        JButton create_a_request = new JButton("Issue a request");
        create_a_request.setFont(new Font("Serif", Font.PLAIN, 20));
        create_a_request.setPreferredSize(new Dimension(200, button_height));
        production_options.add(create_a_request);

        JButton modify_production = new JButton("Modify production");
        modify_production.setFont(new Font("Serif", Font.PLAIN, 20));
        modify_production.setPreferredSize(new Dimension(200, button_height));
        production_options.add(modify_production);

        JButton add_season = new JButton("Add season");
        if (production instanceof Series) {
            add_season.setFont(new Font("Serif", Font.PLAIN, 20));
            add_season.setPreferredSize(new Dimension(200, button_height));
            production_options.add(add_season);
        }

        JButton delete_production = new JButton("Delete production");
        delete_production.setFont(new Font("Serif", Font.PLAIN, 20));
        delete_production.setPreferredSize(new Dimension(200, button_height));
        production_options.add(delete_production);

        ProductionContainer production_container = new ProductionContainer(production_box, production_info_text, production);
        add_to_favourites_button.addActionListener(e -> AddFavouriteButtonAction(production_container));
        create_a_request.addActionListener(e -> HandleIssueRequestAction(production_container));
        add_a_review.addActionListener(e -> HandleAddReviewAction(production_container));
        delete_production.addActionListener(e -> HandleDeleteProductionAction(production_container));
        add_season.addActionListener(e -> HandleAddSeasonAction(production_container));
        modify_production.addActionListener(e -> HandProductionEditAction(production_container));

        productions_containers.add(production_container);
        this.add(production_box);
    }
    public void updateData() {
        for (ProductionContainer productionContainer : productions_containers) {
            productionContainer.getProductionInfoText().setText(productionContainer.getProductionData().toString());
        }
    }
    private void removeProductionBox(ProductionContainer production_container) {
        productions_containers.remove(production_container);
        this.remove(production_container.getProductionBox());
        //Maybe refresh the panel
        //this.updateUI();
    }
    private static class ProductionContainer {
        private JPanel production_box;
        private JTextArea production_info_text;
        private Production production_data;
        private ProductionContainer(JPanel production_box, JTextArea production_info_text, Production production_data) {
            this.production_box = production_box;
            this.production_info_text = production_info_text;
            this.production_data = production_data;
        }
        private JPanel getProductionBox() {
            return production_box;
        }
        private JTextArea getProductionInfoText() {
            return production_info_text;
        }
        private Production getProductionData() {
            return production_data;
        }
    }
    private void AddFavouriteButtonAction (ProductionContainer production_container) {
        try {
            imdb.getLoggedUser().addFavoriteByName(production_container.getProductionData().getStringForSorting());
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        GUI.getMainWindow().getActionPanel().getUsersPanel().updateData();
        //favourites_panel.prepareDataForUser();
    }
    public void ShowAllProductions() {
        this.deleteAllProductionsFromScreen();
        for (Production production : imdb.getProductions()) {
            this.addProductionBox(production);
        }
        this.revalidate();
        this.repaint();
    }
    public void FilterByNumberOfRatings(int nr_ratings) {
        this.deleteAllProductionsFromScreen();
        for (Production production : imdb.getProductions()) {
            if (production.getRatings().size() >= nr_ratings) {
                this.addProductionBox(production);
            }
        }
        this.revalidate();
        this.repaint();
    }
    public void FilterByGenre(Genre genre) {
        this.deleteAllProductionsFromScreen();
        for (Production production : imdb.getProductions()) {
            if (production.getGenres().contains(genre)) {
                this.addProductionBox(production);
            }
        }
        this.revalidate();
        this.repaint();
    }
    public void SearchProduction(String production_name) {
        this.deleteAllProductionsFromScreen();
        Production production = Production.getProductionByTitle(imdb.getProductions(), production_name);
        if (production == null) {
            //Show closest matches
            List<ComparableItem> comparableProductions = new ArrayList<>();
            comparableProductions.addAll(imdb.getProductions());
            String best_matches = UtilFunction.getBestMatchesString(production_name, comparableProductions);
            JOptionPane.showMessageDialog(null, "No production found !\nBest matches:\n"
                    + best_matches, "Nothing found", JOptionPane.INFORMATION_MESSAGE);
            ShowAllProductions();
        } else {
            this.addProductionBox(production);
            this.revalidate();
            this.repaint();
        }
    }
    private void HandleIssueRequestAction (ProductionContainer production_container) {
        if (!(imdb.getLoggedUser() instanceof RequestManager)) {
            JOptionPane.showMessageDialog(null, "You can't issue requests as admin!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            String request_description = JOptionPane.showInputDialog(null, "Enter request description:",
                                                                    "Request description", JOptionPane.QUESTION_MESSAGE);
            Request aux_request = new Request(  RequestTypes.MOVIE_ISSUE, production_container.getProductionData().getContributor(),
                                                production_container.getProductionData().getTitle(), imdb.getLoggedUser(), request_description);
            aux_request.setRequestTimeNow();
            ((RequestManager)imdb.getLoggedUser()).createRequest(aux_request);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void HandleAddReviewAction (ProductionContainer production_container) {
        if (imdb.getLoggedUser().getAccountType() != AccountType.REGULAR) {
            JOptionPane.showMessageDialog(null, "You can only add reviews as a regular user !", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String review_grade_string = JOptionPane.showInputDialog(null, "Enter review grade:",
                "Review grade", JOptionPane.QUESTION_MESSAGE);
        int review_grade;
        try {
            review_grade = Integer.parseInt(review_grade_string);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Review grade must be a number !", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String review_description = JOptionPane.showInputDialog(null, "Enter review description:",
                "Review description", JOptionPane.QUESTION_MESSAGE);
        try {
            production_container.production_data.addOrUpdateRating(new Rating(imdb.getLoggedUser().getUsername(), review_grade, review_description));
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        production_container.production_info_text.setText(production_container.production_data.toString());
    }
    private void HandleDeleteProductionAction (ProductionContainer production_container) {
        if (imdb.getLoggedUser().getAccountType() == AccountType.REGULAR) {
            JOptionPane.showMessageDialog(null, "You can't delete productions as a regular user !",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            ((Staff<ComparableItem>)imdb.getLoggedUser()).removeProductionSystem(production_container.getProductionData().getTitle());
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        removeProductionBox(production_container);
        GUI.getMainWindow().getActionPanel().getActorsPanel().updateData();
        GUI.getMainWindow().getActionPanel().getUsersPanel().updateData();
        this.revalidate();
        this.repaint();
    }
    private void HandleAddSeasonAction (ProductionContainer production_container) {
        if (imdb.getLoggedUser().getAccountType() == AccountType.REGULAR) {
            JOptionPane.showMessageDialog(null, "You can't add a season as a regular user !",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!imdb.getLoggedUser().getUsername().equals(production_container.getProductionData().getContributor().getUsername())) {
            if (production_container.getProductionData().getContributor().getUsername().equals("ADMIN")) {
                if (imdb.getLoggedUser().getAccountType() != AccountType.ADMIN) {
                    JOptionPane.showMessageDialog(null, "You don't have permission to add a season !",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                JOptionPane.showMessageDialog(null, "You don't have permission to add a season !",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        try {
            seasonVerbose((Series) production_container.getProductionData());
            production_container.getProductionInfoText().setText(production_container.getProductionData().toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Not a season", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void HandProductionEditAction (ProductionContainer production_container) {
        if (production_container.getProductionData().getContributor() != imdb.getLoggedUser()) {
            if (production_container.getProductionData().getContributor().equals("ADMIN")) {
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
        String type;
        if (production_container.getProductionData() instanceof Movie) {
            type = "Movie";
        } else {
            type = "Series";
        }
        new ProductionFrame(production_container.getProductionData(), type);
    }
    private void seasonVerbose (Series series) {
        String season_name = JOptionPane.showInputDialog(null, "Enter season name:",
                "Season name", JOptionPane.QUESTION_MESSAGE);
        String nr_episodes_string = JOptionPane.showInputDialog(null, "Enter number of episodes:",
                "Number of episodes", JOptionPane.QUESTION_MESSAGE);
        int nr_episodes;
        try {
            nr_episodes = Integer.parseInt(nr_episodes_string);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,  "Number of episodes must be a number !",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        List<Episode> episodes = new ArrayList<>();
        for (int i = 0; i < nr_episodes; i++) {
            String episode_name = JOptionPane.showInputDialog(null, "Enter name of the episode number " + (i + 1) + ":",
                    "Episode name", JOptionPane.QUESTION_MESSAGE);
            String episode_duration = JOptionPane.showInputDialog(null, "Enter episode duration:",
                    "Episode duration", JOptionPane.QUESTION_MESSAGE);
            episodes.add(new Episode(episode_name, episode_duration));
        }
        series.addEpisodesForSeason(season_name,episodes);
        series.setNumberOfSeasons(series.getNumberOfSeasons() + 1);
    }
}

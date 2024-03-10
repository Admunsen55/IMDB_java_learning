package guipack;

import appstructure.IMDB;
import enumpack.Genre;
import java.awt.*;
import java.util.*;
import java.util.List;
import productionpack.*;
import usefulpack.ComparableItem;
import usefulpack.UtilFunction;
import userpack.Staff;

import javax.swing.*;

public class ProductionFrame extends JFrame{
    Production production;
    String type;
    PromptPanel production_title_panel;
    PromptPanel production_directors_panel;
    PromptPanel production_actors_panel;
    PromptPanel production_genres_panel;
    PromptPanel production_description_panel;
    PromptPanel production_year_of_release_panel;
    PromptPanel optional_movie_duration_panel;
    public ProductionFrame(Production production, String type) {
        JOptionPane.showMessageDialog(null, "Introduce elements in list separated by spaces", "Information", JOptionPane.INFORMATION_MESSAGE);
        this.type = type;
        this.production = production;
        GUI.getMainWindow().getOptionsPanel().setCantLogout(true);

        this.setTitle("Production Dialog");
        this.setBackground(Color.LIGHT_GRAY);
        this.setSize(1000, 400);
        this.setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(0, 2));
        contentPanel.setVisible(true);

        String production_name = production == null ? "" : production.getTitle();
        production_title_panel = new PromptPanel("Name", production_name);
        contentPanel.add(production_title_panel);

        String directors_list = production == null ? "" : UtilFunction.turnListToSingleString(production.getDirectors());
        production_directors_panel = new PromptPanel("Directors", directors_list);
        contentPanel.add(production_directors_panel);

        String actors_list = production == null ? "" : UtilFunction.turnListToSingleString(production.getActors());
        production_actors_panel = new PromptPanel("Actors", actors_list);
        contentPanel.add(production_actors_panel);

        String genres_list = production == null ? "" : UtilFunction.turnGenreListToSingleString(production.getGenres());
        production_genres_panel = new PromptPanel("Genres", genres_list);
        contentPanel.add(production_genres_panel);

        String production_description = production == null ? "" : production.getSubjectDescription();
        production_description_panel = new PromptPanel("Description", production_description);
        contentPanel.add(production_description_panel);

        if (type.equals("Movie")) {
            if (production == null) {
                production_year_of_release_panel = new PromptPanel("Year of release", "");
                contentPanel.add(production_year_of_release_panel);
            } else {
                String production_year_of_release = String.valueOf(((Movie)production).getYearOfRelease());
                production_year_of_release_panel = new PromptPanel("Year of release", production_year_of_release);
                contentPanel.add(production_year_of_release_panel);
            }
            if (production == null) {
                optional_movie_duration_panel = new PromptPanel("Duration", "");
                contentPanel.add(optional_movie_duration_panel);
            } else {
                String movie_duration = ((Movie)production).getDuration();
                optional_movie_duration_panel = new PromptPanel("Duration", movie_duration);
                contentPanel.add(optional_movie_duration_panel);
            }
        } else {
           if (production == null) {
                    production_year_of_release_panel = new PromptPanel("Year of release", "");
                    contentPanel.add(production_year_of_release_panel);
                } else {
                    String production_year_of_release = String.valueOf(((Series)production).getYearOfRelease());
                    production_year_of_release_panel = new PromptPanel("Year of release", production_year_of_release);
                    contentPanel.add(production_year_of_release_panel);
                }
        }

        JButton submit_button = new JButton("Submit");
        submit_button.addActionListener(e -> handleSubmitAction());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(submit_button);
        buttonPanel.setVisible(true);

        this.add(contentPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setLocationRelativeTo(null); // Center the frame on the screen
        this.setVisible(true);

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                JOptionPane.showMessageDialog(null, "Submit text first", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public void handleSubmitAction() {
        GUI.getMainWindow().getOptionsPanel().setCantLogout(false);
        if (production == null) {
            try {
                String title = production_title_panel.getText();
                String description = production_description_panel.getText();
                List<String> directors = UtilFunction.turnSingleStringToList(production_directors_panel.getText());
                List<String> actors = UtilFunction.turnSingleStringToList(production_actors_panel.getText());
                List<Genre> genres = UtilFunction.turnSingleStringToGenreList(production_genres_panel.getText());
                int year_of_release = Integer.parseInt(production_year_of_release_panel.getText());
                LinkedList<Rating> empty_ratings = new LinkedList<>();
                if (type.equals("Movie")) {
                    String duration = optional_movie_duration_panel.getText();
                    production = new Movie(title, directors, actors, genres, empty_ratings, description, 0, duration, year_of_release);
                } else {
                    Map<String, List<Episode>> empty_seasons = new HashMap<>();
                    production = new Series(title, directors, actors, genres, empty_ratings, description, 0, year_of_release, 0);
                    ((Series)production).setEpisodes(empty_seasons);
                }
                ((Staff<ComparableItem>) IMDB.getInstance().getLoggedUser()).addProductionSystem(production);
                ProductionsPanel productions_panel = GUI.getMainWindow().getActionPanel().getProductionsPanel();
                productions_panel.addProductionBox(production);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(null, "Invalid types", "Error", JOptionPane.ERROR_MESSAGE);
                this.dispose();
                return;
            }
        } else {
            try {
                production.setTitle(production_title_panel.getText());
                production.setSubjectDescription(production_description_panel.getText());
                production.setDirectors(UtilFunction.turnSingleStringToList(production_directors_panel.getText()));
                production.setActors(UtilFunction.turnSingleStringToList(production_actors_panel.getText()));
                production.setGenres(UtilFunction.turnSingleStringToGenreList(production_genres_panel.getText()));
                if (type.equals("Movie")) {
                    ((Movie)production).setYearOfRelease(Integer.parseInt(production_year_of_release_panel.getText()));
                    ((Movie)production).setDuration(optional_movie_duration_panel.getText());
                } else {
                    ((Series)production).setYearOfRelease(Integer.parseInt(production_year_of_release_panel.getText()));
                }
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(null, "Invalid types", "Error", JOptionPane.ERROR_MESSAGE);
                this.dispose();
                return;
            }
            GUI.getMainWindow().getActionPanel().getProductionsPanel().updateData();
        }
        this.dispose();
    }
}

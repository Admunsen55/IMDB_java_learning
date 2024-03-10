package guipack;

import javax.swing.*;
import javax.swing.text.StyleConstants;

import appstructure.IMDB;
import java.awt.*;
import java.util.*;
import java.util.TreeSet;
import productionpack.Movie;
import productionpack.Production;
import usefulpack.ComparableItem;

public class FavouritesPanel extends JPanel {
    private IMDB imdb = IMDB.getInstance();
    private SortedSet<FavouriteContainer> favourites_containers;

    public FavouritesPanel() {
        favourites_containers = new TreeSet<>();
        this.setBackground(Color.BLACK);
        this.setLayout(new GridLayout(0, 1));
        this.setVisible(true);
//        for (ComparableItem item : imdb.getLoggedUser().getFavorites()) {
//            addFavouriteBox(item);
//        }
    }
    public void prepareDataForUser () {
        Iterator<FavouriteContainer> iterator = favourites_containers.iterator();
        while (iterator.hasNext()) {
            FavouriteContainer cur = iterator.next();
            iterator.remove();
            this.remove(cur.getFavouriteBox());
        }
        for (ComparableItem item : imdb.getLoggedUser().getFavorites()) {
            this.addFavouriteBox(item);
        }
        this.revalidate();
        this.repaint();
    }
    public void addFavouriteBox (ComparableItem item) {
        JPanel favourite_box = new JPanel();
        favourite_box.setLayout(new FlowLayout(FlowLayout.LEFT));
        favourite_box.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        favourite_box.setSize(new Dimension(1270, 100));
        favourite_box.setBackground(Color.BLACK);
        favourite_box.setVisible(true);

//        JPanel just_for_aligning = new JPanel();
//        just_for_aligning.setPreferredSize(new Dimension(200, 100));
//        just_for_aligning.setVisible(true);
//        favourite_box.add(just_for_aligning);

        ImageIcon fav_icon = new ImageIcon("images/star_icon.png");
        JLabel fav_icon_label = new JLabel(fav_icon);
        fav_icon_label.setPreferredSize(new Dimension(200, 100));
        fav_icon_label.setVisible(true);
        favourite_box.add(fav_icon_label);

        String type;
        if (item instanceof Production) {
            if (item instanceof Movie) {
                type = "Movie: ";
            } else {
                type = "Series: ";
            }
        } else {
            type = "Actor: ";
        }
        JTextArea favourite_info_text = new JTextArea(type + item.getStringForSorting());
        favourite_info_text.setFont(new Font("Serif", Font.PLAIN, 45));
        favourite_info_text.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
        favourite_info_text.setAlignmentX(StyleConstants.ALIGN_CENTER);
        favourite_info_text.setAlignmentY(StyleConstants.ALIGN_CENTER);
        favourite_info_text.setPreferredSize(new Dimension(800, 100));
        favourite_info_text.setForeground(Color.WHITE);
        favourite_info_text.setBackground(Color.BLACK);
        favourite_info_text.setEditable(false);
        favourite_box.add(favourite_info_text);

        JButton remove_from_favourites_button = new JButton("Remove favourite");
        remove_from_favourites_button.setFont(new Font("Serif", Font.PLAIN, 25));
        remove_from_favourites_button.setPreferredSize(new Dimension(250, 100));
        favourite_box.add(remove_from_favourites_button);

        FavouriteContainer favourite_data = new FavouriteContainer(favourite_box, favourite_info_text, item);
        remove_from_favourites_button.addActionListener(e -> RemoveFavouriteButtonAction(favourite_data));

        favourites_containers.add(favourite_data);
        this.add(favourite_box);
    }
    public void updateData () {
        for (FavouriteContainer cur : favourites_containers) {
            cur.getFavouriteInfoText().setText(cur.getFavouritItem().getStringForSorting());
        }
    }
    private void removeFavouriteBox(FavouriteContainer favourite_container) {
        favourites_containers.remove(favourite_container);
        this.remove(favourite_container.getFavouriteBox());
        //Maybe refresh the panel
        //this.updateUI();
    }
    private static class FavouriteContainer implements Comparable<FavouriteContainer>{
        private JPanel favourite_box;
        private JTextArea favourite_info_text;
        private ComparableItem favourite_item;
        private ComparableItem getFavouritItem() {
            return favourite_item;
        }
        private JTextArea getFavouriteInfoText() { return favourite_info_text; }
        private JPanel getFavouriteBox() { return favourite_box; }
        private FavouriteContainer(JPanel favourite_box, JTextArea favourite_info_text, ComparableItem favourite_item) {
            this.favourite_box = favourite_box;
            this.favourite_info_text = favourite_info_text;
            this.favourite_item = favourite_item;
        }
        @Override
        public int compareTo(FavouriteContainer favouriteContainer) {
            return favourite_item.compareTo(favouriteContainer.getFavouritItem());
        }
    }
    private void RemoveFavouriteButtonAction (FavouriteContainer favourite_container) {
        try {
            imdb.getLoggedUser().removeFavoriteByName(favourite_container.getFavouritItem().getStringForSorting());
        } catch (NoSuchElementException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        this.removeFavouriteBox(favourite_container);
        this.revalidate();
        this.repaint();
    }
}

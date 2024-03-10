package guipack;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import productionpack.Actor;
import usefulpack.ComparableItem;
import java.util.*;
import usefulpack.UtilFunction;
import userpack.User;
import appstructure.IMDB;
import userpack.Admin;
import enumpack.AccountType;

public class UsersPanel extends JPanel {
    private IMDB imdb = IMDB.getInstance();
    private List<UserContainer> users_containers;
    public UsersPanel() {
        users_containers = new LinkedList<>();
        this.setBackground(Color.BLACK);
        this.setLayout(new GridLayout(0, 1));
        this.setVisible(true);

        for (int i = 0; i < imdb.getUsers().size(); i++) {
            addUserBox(imdb.getUsers().get(i));
        }
    }
    public void prepareDataForUser () {
        for (int i = 0; i < users_containers.size(); i++) {
            users_containers.get(i).getUserInfoText().setText(users_containers.get(i).getUserItem().toString());
        }
        this.revalidate();
        this.repaint();
    }
    public void addUserBox(User<ComparableItem> user) {
        JPanel user_box = new JPanel();
        user_box.setLayout(new FlowLayout(FlowLayout.LEFT));
        user_box.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        user_box.setSize(new Dimension(1270, 300));
        user_box.setBackground(Color.BLACK);
        user_box.setVisible(true);

        ImageIcon user_image = new ImageIcon(user.getImagePath());
        JLabel user_image_label = new JLabel(user_image);
        user_image_label.setPreferredSize(new Dimension(300, 300));
        user_image_label.setVisible(true);
        user_box.add(user_image_label);

        JTextArea user_info_text = new JTextArea(user.toString());
        user_info_text.setFont(new Font("Serif", Font.PLAIN, 15));
        user_info_text.setPreferredSize(new Dimension(750, 750));
        user_info_text.setForeground(Color.WHITE);
        user_info_text.setBackground(Color.BLACK);
        user_info_text.setLineWrap(true);
        user_info_text.setWrapStyleWord(true);
        user_info_text.setEditable(false);

        JScrollPane user_info_scroll = new JScrollPane(user_info_text);
        //user_info_scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        user_info_scroll.setPreferredSize(new Dimension(750, 300));
        user_info_scroll.setVisible(true);
        user_box.add(user_info_scroll);

        JPanel user_options = new JPanel();
        user_options.setLayout(new GridLayout(0, 1));
        user_options.setPreferredSize(new Dimension(200, 300));
        user_options.setBackground(Color.BLACK);
        user_options.setVisible(true);
        user_box.add(user_options);

        JButton add_to_favorites_button = new JButton("Edit user");
        add_to_favorites_button.setPreferredSize(new Dimension(200, 150));
        add_to_favorites_button.setVisible(true);
        user_options.add(add_to_favorites_button);

        JButton remove_user_button = new JButton("Remove user");
        remove_user_button.setPreferredSize(new Dimension(200, 150));
        remove_user_button.setVisible(true);
        user_options.add(remove_user_button);

        UserContainer user_container = new UserContainer(user_box, user_info_text, user);
        add_to_favorites_button.addActionListener(e -> handleEditUserButton(user_container));
        remove_user_button.addActionListener(e -> handleRemoveUserButton(user_container));

        users_containers.add(user_container);
        this.add(user_box);
    }
    public void updateData() {
        for (int i = 0; i < users_containers.size(); i++) {
            users_containers.get(i).getUserInfoText().setText(users_containers.get(i).getUserItem().toString());
        }
    }
    private void removeUserBox(UserContainer user_container) {
        users_containers.remove(user_container);
        this.remove(user_container.getUserBox());
        //Maybe refresh the panel
        //this.updateUI();
    }
    private void deleteAllUsersFromScreen() {
        Iterator<UserContainer> iterator= users_containers.iterator();
        while (iterator.hasNext()) {
            UserContainer cur = iterator.next();
            this.remove(cur.getUserBox());
            iterator.remove();
        }
    }
    private static class UserContainer {
        private JPanel user_box;
        private JTextArea user_info_text;
        private User<ComparableItem> user_item;
        public UserContainer (JPanel user_box, JTextArea user_info_text, User<ComparableItem> user_item) {
            this.user_box = user_box;
            this.user_info_text = user_info_text;
            this.user_item = user_item;
        }
        private JPanel getUserBox() {
            return user_box;
        }
        private JTextArea getUserInfoText() {
            return user_info_text;
        }
        private User<ComparableItem> getUserItem() {
            return user_item;
        }
    }
    private void handleEditUserButton(UserContainer user_container) {
        if (imdb.getLoggedUser().getAccountType() != AccountType.ADMIN) {
            JOptionPane.showMessageDialog(null, "You don't have permission to edit users", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        new UserFrame(user_container.getUserItem());
    }
    private void handleRemoveUserButton(UserContainer user_container) {
        try {
            ((Admin<ComparableItem>)imdb.getLoggedUser()).removeUserSystem(user_container.getUserItem());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        removeUserBox(user_container);
        GUI.getMainWindow().getActionPanel().getProductionsPanel().updateData();
        GUI.getMainWindow().getActionPanel().getActorsPanel().updateData();
        this.revalidate();
        this.repaint();
    }
}

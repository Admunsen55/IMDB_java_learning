package guipack;

import javax.management.Notification;
import javax.swing.*;
import javax.swing.text.StyleConstants;

import appstructure.IMDB;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.Iterator;
import productionpack.Movie;
import productionpack.Production;
import usefulpack.ComparableItem;

public class NotificationsPanel extends JPanel {
    private IMDB imdb = IMDB.getInstance();
    private List<NotificationContainer> notifications_containers;

    public NotificationsPanel() {
        notifications_containers = new LinkedList<>();
        this.setBackground(Color.BLACK);
        this.setLayout(new GridLayout(0, 1));
        this.setVisible(true);
    }
    public void prepareDataForUser () {
        Iterator<NotificationContainer> iterator = notifications_containers.iterator();
        while (iterator.hasNext()) {
            NotificationContainer cur = iterator.next();
            iterator.remove();
            this.remove(cur.getNotificationBox());
        }
        for (String not : imdb.getLoggedUser().getNotifications()) {
            this.addNotificationBox(not);
        }
        this.revalidate();
        this.repaint();
    }
    public void addNotificationBox (String notification) {
        JPanel notification_box = new JPanel();
        notification_box.setLayout(new FlowLayout(FlowLayout.LEFT));
        notification_box.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        notification_box.setSize(new Dimension(1270, 100));
        notification_box.setBackground(Color.BLACK);
        notification_box.setVisible(true);

        ImageIcon notification_icon = new ImageIcon("images/notification_icon.png");
        JLabel fav_icon_label = new JLabel(notification_icon);
        fav_icon_label.setPreferredSize(new Dimension(200, 100));
        fav_icon_label.setVisible(true);
        notification_box.add(fav_icon_label);

        JTextArea notification_info_text = new JTextArea(notification);
        notification_info_text.setPreferredSize(new Dimension(800, 100));
        notification_info_text.setFont(new Font("Serif", Font.PLAIN, 25));
        notification_info_text.setBackground(Color.BLACK);
        notification_info_text.setForeground(Color.WHITE);
        notification_info_text.setLineWrap(true);
        notification_info_text.setWrapStyleWord(true);
        notification_info_text.setEditable(false);
        notification_box.add(notification_info_text);

        JButton remove_notification_button = new JButton("Remove notification");
        remove_notification_button.setFont(new Font("Serif", Font.PLAIN, 25));
        remove_notification_button.setPreferredSize(new Dimension(250, 100));
        notification_box.add(remove_notification_button);

        NotificationContainer notification_container = new NotificationContainer(notification_box, notification);
        remove_notification_button.addActionListener(e -> RemoveNotificationButtonAction(notification_container));

        notifications_containers.add(notification_container);
        this.add(notification_box);
    }
    private void removeNotificationBox(NotificationContainer notification_container) {
        notifications_containers.remove(notification_container);
        this.remove(notification_container.getNotificationBox());
        //Maybe refresh the panel
        //this.updateUI();
    }
    private static class NotificationContainer {
        private JPanel notification_box;
        private String notification_item;
        private NotificationContainer(JPanel notification_box, String notification_item) {
            this.notification_box = notification_box;
            this.notification_item = notification_item;
        }
        private JPanel getNotificationBox() {
            return notification_box;
        }
        private String getNotificationItem() {
            return notification_item;
        }
    }
    private void RemoveNotificationButtonAction(NotificationContainer notification_container) {
        imdb.getLoggedUser().getNotifications().remove(notification_container.getNotificationItem());
        this.removeNotificationBox(notification_container);
        this.revalidate();
        this.repaint();
    }
}

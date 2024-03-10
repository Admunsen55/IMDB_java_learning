package guipack;

import java.util.List;
import java.util.LinkedList;
import observerpack.Request;
import appstructure.IMDB;
import enumpack.AccountType;

import javax.swing.*;
import java.awt.*;
import usefulpack.ComparableItem;
import userpack.RequestManager;
import userpack.Staff;
import userpack.StaffInterface;
import java.util.Iterator;

public class RequestsPanel extends JPanel {
    private IMDB imdb = IMDB.getInstance();
    private List<RequestContainer> requests_containers;
    public RequestsPanel() {
        requests_containers = new LinkedList<>();
        this.setBackground(Color.BLACK);
        this.setLayout(new GridLayout(0, 1));
        this.setVisible(true);
    }
    public void prepareDataForUser () {
        Iterator<RequestContainer> iterator = requests_containers.iterator();
        while (iterator.hasNext()) {
            RequestContainer cur = iterator.next();
            iterator.remove();
            this.remove(cur.getRequestBox());
        }
        List<Request> issued_requests = imdb.getLoggedUser().getIssuedRequests();
        for (Request cur : issued_requests) {
            AddRequestBox(cur, false);
        }
        if (imdb.getLoggedUser().getAccountType() != AccountType.REGULAR) {
            List<Request> personal_requests = ((Staff<ComparableItem>) imdb.getLoggedUser()).getPersonalRequests();
            if (personal_requests != null) {
                for (Request cur : personal_requests) {
                    AddRequestBox(cur, true);
                }
            }
            if (imdb.getLoggedUser().getAccountType() == AccountType.ADMIN) {
                List<Request> common_requests = Request.RequestHolder.getRequests();
                for (Request cur : common_requests) {
                    AddRequestBox(cur, true);
                }
            }
        }
        this.revalidate();
        this.repaint();
    }
    public void AddRequestBox (Request request, boolean to_solve) {
        JPanel request_box = new JPanel();
        request_box.setLayout(new FlowLayout(FlowLayout.LEFT));
        request_box.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        request_box.setPreferredSize(new Dimension(1270, 150));
        request_box.setBackground(Color.BLACK);
        request_box.setVisible(true);

        ImageIcon request_icon = new ImageIcon("images/request_icon.png");
        JLabel request_icon_label = new JLabel(request_icon);
        request_icon_label.setPreferredSize(new Dimension(200, 150));
        request_icon_label.setVisible(true);
        request_box.add(request_icon_label);

        JTextArea request_info_text = new JTextArea(request.toString());
        request_info_text.setPreferredSize(new Dimension(800, 150));
        request_info_text.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
        request_info_text.setFont(new Font("Serif", Font.PLAIN, 12));
        request_info_text.setBackground(Color.BLACK);
        request_info_text.setForeground(Color.WHITE);
        request_info_text.setLineWrap(true);
        request_info_text.setWrapStyleWord(true);
        request_info_text.setEditable(false);
        request_box.add(request_info_text);


        JPanel request_options = new JPanel();
        request_options.setLayout(new GridLayout(0, 1));
        request_options.setPreferredSize(new Dimension(200, 150));
        request_options.setBackground(Color.BLACK);
        request_options.setVisible(true);
        request_box.add(request_options);

        JButton accept_button = new JButton("Accept Request");
        accept_button.setFont(new Font("Serif", Font.PLAIN, 20));
        accept_button.setPreferredSize(new Dimension(200, 50));
        request_options.add(accept_button);
        if (!to_solve) {
            accept_button.setEnabled(false);
        }

        JButton decline_button = new JButton("Decline Request");
        decline_button.setFont(new Font("Serif", Font.PLAIN, 20));
        decline_button.setPreferredSize(new Dimension(200, 50));
        request_options.add(decline_button);
        if (!to_solve) {
            decline_button.setEnabled(false);
        }

        JButton remove_button = new JButton("Remove Request");
        remove_button.setFont(new Font("Serif", Font.PLAIN, 20));
        remove_button.setPreferredSize(new Dimension(200, 50));
        request_options.add(remove_button);
        if (to_solve) {
            remove_button.setEnabled(false);
        }

        RequestContainer request_container = new RequestContainer(request_box, request);
        accept_button.addActionListener(e -> handleAcceptButton(request_container));
        decline_button.addActionListener(e -> handleDeclineButton(request_container));
        remove_button.addActionListener(e -> handleRemoveButton(request_container));

        requests_containers.add(request_container);
        this.add(request_box);
    }
    public void removeRequestBox(RequestContainer request_container) {
        requests_containers.remove(request_container);
        this.remove(request_container.getRequestBox());
        //Maybe refresh the panel
        //this.updateUI();
    }
    public static class RequestContainer {
        private JPanel request_box;
        private Request request_item;
        private RequestContainer(JPanel request_box, Request request_item) {
            this.request_box = request_box;
            this.request_item = request_item;
        }
        private JPanel getRequestBox() {
            return request_box;
        }
        private Request getRequestItem() {
            return request_item;
        }
    }
    public void handleAcceptButton(RequestContainer request_container) {
        try {
            ((Staff<ComparableItem>)imdb.getLoggedUser()).solveRequest(request_container.getRequestItem(), true);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        this.removeRequestBox(request_container);
        this.revalidate();
        this.repaint();
    }
    public void handleDeclineButton(RequestContainer request_container) {
        try {
            ((Staff<ComparableItem>)imdb.getLoggedUser()).solveRequest(request_container.getRequestItem(), false);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        this.removeRequestBox(request_container);
        this.revalidate();
        this.repaint();
    }
    public void handleRemoveButton(RequestContainer request_container) {
        try {
            ((RequestManager)imdb.getLoggedUser()).removeRequest(request_container.getRequestItem());
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        this.removeRequestBox(request_container);
        this.revalidate();
        this.repaint();
    }
}

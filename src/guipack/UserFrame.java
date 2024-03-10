package guipack;

import javax.swing.*;

import appstructure.IMDB;
import enumpack.AccountType;
import enumpack.Gender;
import java.awt.*;
import java.util.*;
import usefulpack.ComparableItem;
import usefulpack.UtilFunction;
import userpack.Credentials;
import userpack.User;
import userpack.UserFactory;
import userpack.Admin;

public class UserFrame extends JFrame {
    User<ComparableItem> user;
    PromptPanel username_panel;
    PromptPanel email_panel;
    PromptPanel password_panel;
    PromptPanel name_panel;
    PromptPanel country_panel;
    PromptPanel age_panel;
    PromptPanel gender_panel;
    PromptPanel birthdate_panel;
    public UserFrame(User<ComparableItem> user) {
        this.user = user;
        GUI.getMainWindow().getOptionsPanel().setCantLogout(true);
        JOptionPane.showMessageDialog(null, "Introduce birtdate in format yyyy-mm-dd\n Gender can be: MALE, FEMALE, OTHER", "Information", JOptionPane.INFORMATION_MESSAGE);

        this.setTitle("User Dialog");
        this.setBackground(Color.LIGHT_GRAY);
        this.setSize(1000, 400);
        this.setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(0, 2));
        contentPanel.setVisible(true);

        String username = user == null ? "" : user.getUsername();
        username_panel = new PromptPanel("Username", username);
        contentPanel.add(username_panel);

        String email = user == null ? "" : user.getInfo().getCredentials().getEmail();
        email_panel = new PromptPanel("Email", email);
        contentPanel.add(email_panel);

        String password = user == null ? Credentials.suggestStrongPassword() : "SECRET";
        password_panel = new PromptPanel("Password", password);
        contentPanel.add(password_panel);

        String name = user == null ? "" : user.getInfo().getName();
        name_panel = new PromptPanel("Name", name);
        contentPanel.add(name_panel);

        String country = user == null ? "" : user.getInfo().getCountry();
        country_panel = new PromptPanel("Country", country);
        contentPanel.add(country_panel);

        String age = user == null ? "" : Integer.toString(user.getInfo().getAge());
        age_panel = new PromptPanel("Age", age);
        contentPanel.add(age_panel);

        String gender_string = user == null ? "" : user.getInfo().getGender().toString();
        gender_panel = new PromptPanel("Gender", gender_string);
        contentPanel.add(gender_panel);

        String birthdate = user == null ? "" : user.getInfo().getBirthDate().toString();
        birthdate_panel = new PromptPanel("Birthdate", birthdate);
        contentPanel.add(birthdate_panel);

        JButton submit_button = new JButton("Submit");
        submit_button.addActionListener(e -> handleSubmitAction());

        JButton sugest_username = new JButton("Suggest username based on name");
        sugest_username.addActionListener(e -> handleSuggestUsernameAction());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(submit_button);
        buttonPanel.add(sugest_username);
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
        if (user == null) {
            User.Information info;
            try {
                info = new User.InformationBuilder()
                        .credentials(new Credentials(email_panel.getText(), password_panel.getText()))
                        .name(name_panel.getText())
                        .country(country_panel.getText())
                        .age(Integer.parseInt(age_panel.getText()))
                        .gender(Gender.valueOf(gender_panel.getText().toUpperCase()))
                        .birthDate(UtilFunction.getDateFromString(birthdate_panel.getText()))
                        .build();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Invalid data", "Error", JOptionPane.ERROR_MESSAGE);
                this.dispose();
                return;
            }
            UserFactory user_factory = new UserFactory();
            user = user_factory.addUser(info, AccountType.REGULAR, username_panel.getText(), 0,
                                        new LinkedList<>(), new TreeSet<>(), null);
            try {
                ((Admin<ComparableItem>)(IMDB.getInstance().getLoggedUser())).addUserSystem(user);
                GUI.getMainWindow().getActionPanel().getUsersPanel().addUserBox(user);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                this.dispose();
                return;
            }
        } else {
            try {
                user.setUsername(username_panel.getText());
                user.getInfo().setName(name_panel.getText());
                user.getInfo().setCountry(country_panel.getText());
                user.getInfo().setAge(Integer.parseInt(age_panel.getText()));
                user.getInfo().setGender(Gender.valueOf(gender_panel.getText().toUpperCase()));
                user.getInfo().setBirthDate(UtilFunction.getDateFromString(birthdate_panel.getText()));
                user.getInfo().getCredentials().setEmail(email_panel.getText());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Invalid data", "Error", JOptionPane.ERROR_MESSAGE);
                this.dispose();
                return;
            }
            GUI.getMainWindow().getActionPanel().getUsersPanel().updateData();
        }
        this.dispose();
    }
    public void handleSuggestUsernameAction() {
        String name = name_panel.getText();
        if (name.equals("")) {
            JOptionPane.showMessageDialog(null, "Name field is empty", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String[] name_parts = name.split(" ");
        StringBuilder username = new StringBuilder();
        for (String name_part : name_parts) {
            username.append(name_part.toLowerCase()).append("_");
        }
        username.deleteCharAt(username.length() - 1);
        boolean username_exists = true;
        Random random = new Random();
        while (username_exists) {
            username_exists = false;
            for (User<ComparableItem> user : IMDB.getInstance().getUsers()) {
                if (user.getUsername().contentEquals(username)) {
                    username_exists = true;
                    break;
                }
            }
            if (username_exists) {
                username.append(random.nextInt(10));
            }
        }
        username_panel.setText(username.toString());
    }
}


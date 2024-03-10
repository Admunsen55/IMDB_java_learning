package guipack;

import appstructure.IMDB;
import java.awt.*;
import userpack.User;

import javax.swing.*;

public class LoginWindow extends JFrame {
    private JLabel email_label;
    private JLabel password_label;
    private JTextField email_textfield;
    private JPasswordField password_textfield;
    private JButton login_button;
    private JButton exit_app_button;
    private IMDB imdb = IMDB.getInstance();
    public LoginWindow() {
        this.setTitle("LoginWindow");
        this.setSize(600, 300);
        this.getContentPane().setBackground(Color.WHITE);
        this.setLayout(null);
        this.setResizable(false);
        this.setVisible(true);
        this.setLocationRelativeTo(null);

        email_label = new JLabel("Email:");
        email_label.setBounds(20, 20, 160, 50);
        email_label.setFont(new Font("Serif", Font.PLAIN, 20));
        this.add(email_label);

        email_textfield = new JTextField();
        email_textfield.setBounds(200, 20, 320, 50);
        email_textfield.setFont(new Font("Serif", Font.PLAIN, 20));
        this.add(email_textfield);

        password_label = new JLabel("Password:");
        password_label.setBounds(20, 80, 160, 50);
        password_label.setFont(new Font("Serif", Font.PLAIN, 20));
        this.add(password_label);

        password_textfield = new JPasswordField();
        password_textfield.setBounds(200, 80, 320, 50);
        password_textfield.setFont(new Font("Serif", Font.PLAIN, 20));
        this.add(password_textfield);

        login_button = new JButton("Login");
        login_button.setBounds(20, 160, 240, 50);
        login_button.addActionListener(e -> handleLoginButton());
        this.add(login_button);

        exit_app_button = new JButton("EXIT IMDB");
        exit_app_button.setBounds(280, 160, 240, 50);
        exit_app_button.addActionListener(e -> handleExitAppButton());
        this.add(exit_app_button);

        this.closeLoginWindow();
    }
    private void handleLoginButton() {
        try {
            String email = email_textfield.getText();
            String password = password_textfield.getText();
            imdb.setLoggedUser(User.getUserByCredentials(email, password, imdb.getUsers()));
            if (imdb.getLoggedUser() != null) {
                System.out.println("Logged in as " + imdb.getLoggedUser().getUsername());
                this.closeLoginWindow();
                GUI.getMainWindow().openMainWindow();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private void handleExitAppButton() {
        System.exit(0);
    }
    public void openLoginWindow() {
        this.show();
    }
    public void closeLoginWindow() {
        email_textfield.setText("");
        password_textfield.setText("");
        this.dispose();
    }

}

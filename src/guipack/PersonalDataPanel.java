package guipack;

import javax.swing.*;

import appstructure.IMDB;
import java.awt.*;

public class PersonalDataPanel extends JPanel {
    JTextArea personal_data_text;
    JTextArea welcome_text;
    public PersonalDataPanel() {
        this.setSize(500, 1200);
        this.setBackground(Color.BLACK);
        this.setLayout(new BorderLayout());
        this.setVisible(true);

        welcome_text = new JTextArea("Welcome back !!!");
        welcome_text.setSize(new Dimension(500, 150));
        welcome_text.setFont(new Font("Serif", Font.PLAIN, 50));
        welcome_text.setForeground(Color.YELLOW);
        welcome_text.setBackground(Color.BLACK);
        welcome_text.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
        welcome_text.setVisible(true);
        this.add(welcome_text, BorderLayout.NORTH);

        personal_data_text = new JTextArea();
        personal_data_text.setSize(new Dimension(500, 1050));
        personal_data_text.setFont(new Font("Serif", Font.PLAIN, 15));
        personal_data_text.setBackground(Color.BLACK);
        personal_data_text.setForeground(Color.WHITE);
        personal_data_text.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
        personal_data_text.setLineWrap(true);
        personal_data_text.setWrapStyleWord(true);
        personal_data_text.setEditable(false);
        this.add(personal_data_text, BorderLayout.CENTER);
    }
    public void updatePersonalDataText () {
        welcome_text.setText("Welcome back "
                + IMDB.getInstance().getLoggedUser().getInfo().getName() + " !!!");
        personal_data_text.setText(IMDB.getInstance().getLoggedUser().toString());
        personal_data_text.setAlignmentX(Component.CENTER_ALIGNMENT);
        personal_data_text.setAlignmentY(Component.CENTER_ALIGNMENT);
    }
}

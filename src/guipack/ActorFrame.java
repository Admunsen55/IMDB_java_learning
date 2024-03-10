package guipack;

import javax.swing.*;

import appstructure.IMDB;
import java.awt.*;
import java.util.ArrayList;
import productionpack.Actor;
import usefulpack.Pair;
import java.util.List;
import usefulpack.Pair;
import userpack.StaffInterface;

public class ActorFrame extends JFrame {
    Actor actor;
    PromptPanel actor_name_panel;
    PromptPanel actor_biography_panel;
    public ActorFrame(Actor actor) {
        this.actor = actor;
        GUI.getMainWindow().getOptionsPanel().setCantLogout(true);

        this.setTitle("Actor Dialog");
        this.setBackground(Color.LIGHT_GRAY);
        this.setSize(500, 400);
        this.setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(0, 1));
        contentPanel.setVisible(true);

        String actor_name = actor == null ? "" : actor.getName();
        actor_name_panel = new PromptPanel("Name", actor_name);
        contentPanel.add(actor_name_panel);

        String actor_biography = actor == null ? "" : actor.getBiography();
        actor_biography_panel = new PromptPanel("Biography", actor_biography);
        contentPanel.add(actor_biography_panel);

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
        if (actor == null) {
            List<Pair<String, String>> empty_filmography = new ArrayList<>();
            actor = new Actor(actor_name_panel.getText(), empty_filmography, actor_biography_panel.getText());
            try {
                ((StaffInterface)IMDB.getInstance().getLoggedUser()).addActorSystem(actor);
                ActorsPanel actors_panel = GUI.getMainWindow().getActionPanel().getActorsPanel();
                actors_panel.addActorBox(actor);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            actor.setName(actor_name_panel.getText());
            actor.setBiography(actor_biography_panel.getText());
            GUI.getMainWindow().getActionPanel().getActorsPanel().updateData();
        }
        this.dispose();
    }
}

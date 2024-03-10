package guipack;

import javax.swing.*;
import java.awt.*;

public class PromptPanel extends JPanel{
    JTextArea textArea;
    public PromptPanel(String prompt_text, String text) {
        this.setLayout(null);
        this.setSize(new Dimension(500, 150));
        this.setBackground(Color.GRAY);
        this.setVisible(true);

        JLabel prompt = new JLabel(prompt_text + ":");
        prompt.setHorizontalAlignment(JLabel.CENTER);
        prompt.setVerticalAlignment(JLabel.CENTER);
        prompt.setFont(new Font("Serif", Font.PLAIN, 13));
        prompt.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        prompt.setBounds(0, 0, 150, 150);
        prompt.setBackground(Color.GRAY);
        this.add(prompt);

        textArea = new JTextArea(text);
        textArea.setFont(new Font("Serif", Font.PLAIN, 15));
        textArea.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        textArea.setBounds(150, 0, 350, 150);
        textArea.setBackground(Color.WHITE);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        this.add(textArea);
    }
    public String getText() {
        return textArea.getText();
    }
    public void setText(String text) {
        textArea.setText(text);
    }
}

import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class Swing {
    public static void main() throws IOException {
        Config config = Json.load();

        JFrame window = new JFrame("Searcher");
        JPanel panel = new JPanel();
        JTextField text = new JTextField();
        JLabel preview = new JLabel("No match");
        JButton button = new JButton("Search");

        text.getDocument().addDocumentListener(new DocumentListener() {

            private void update() {

                try {

                    App app = FuzzySearch.check(
                        text.getText(),
                        AppScanner.getApps(config)
                    );

                    if (app == null) {
                        preview.setText("No match");
                        return;
                    }

                    preview.setText("Launch: " + app.name);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();
            }
        });
        
        button.addActionListener(e -> {
            App app = null;
            try {
                app = FuzzySearch.check(text.getText(), AppScanner.getApps(config));
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            if (app == null) {
                System.out.println("No match found.");
                return;
            }

            try {
                new ProcessBuilder(app.exec.split(" ")).start();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        text.setPreferredSize(new Dimension(250, 30));
        text.setAlignmentX(JTextField.LEFT_ALIGNMENT);
        text.setBorder(new LineBorder(Color.BLACK, 2));

        window.setSize(400, 125);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel.add(text);
        panel.add(Box.createVerticalStrut(10));
        panel.add(preview);
        panel.add(Box.createVerticalStrut(10));
        panel.add(button);

        window.setResizable(false);
        window.getRootPane().setDefaultButton(button);
        window.setBackground(Color.GRAY);
        window.add(panel);
        window.setVisible(true);
    }
}

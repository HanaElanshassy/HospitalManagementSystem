import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI {
    public static void main(String[] args) {
                JFrame frame = new JFrame("Simple GUI Example");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel) {
        panel.setLayout(null);


        JLabel userLabel = new JLabel("Click the button:");
        userLabel.setBounds(10, 20, 150, 25);
        panel.add(userLabel);


        JButton button = new JButton("Click Me");
        button.setBounds(10, 50, 150, 25);
        panel.add(button);


        JLabel messageLabel = new JLabel("");
        messageLabel.setBounds(10, 80, 300, 25);
        panel.add(messageLabel);


        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                messageLabel.setText("Button clicked! Hello World!");
            }
        });
    }
}

import javax.swing.*;
import java.awt.*;

public class mainWindow {
    private JFrame window;
    public mainWindow() {
        window = new JFrame();
        window.setTitle("Cash Register");
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setSize(1520,720);
        window.setResizable(false);
        window.setLocationRelativeTo(null);

        JPanel keypadPanel = new JPanel(new GridLayout(2,1,5,30));
        JPanel yelKeypadPanel = new JPanel(new GridLayout(4,4,5,5));
        JPanel numKeypadPanel = new JPanel(new GridBagLayout());
        keypadPanel.setBackground(Color.GRAY);

        String[] yelButtonLabels = {
                "<html>VENDOR<br>COUPON</html>", "Qty",
                "<html>PS<br>MERC RETURN</html>", "<html>ITEM<br>INQUIRY</html>",
                "<html>Walmart<br>Pay</html>", "<html>SHOP<br>CARD</html>",
                "<html>PRICE<br>OVER</html>", "<html>ERROR<br>CORRECT</html>",
                "CHECK", "<html>CREDIT<br>DEBIT</html>",
                "<html>TRANS<br>DISC</html>", "<html>VOID<br>ITEM</html>",
                "CASH", "TOTAL", "<html>ACTION<br>CODE</html>",
                "<html>ABORT<br>TRANS</html>"
        };

        for (String label : yelButtonLabels) {
            JButton button = new JButton(label);
            button.setBackground(Color.yellow);
            button.setPreferredSize(new Dimension(100, 100));
            yelKeypadPanel.add(button);
        }

        Object[][] numKeypadLabels = {
                {"7", 0, 0, 1, 1},
                {"8", 1, 0, 1, 1},
                {"9", 2, 0, 1, 1},
                {"CTRL", 3, 0, 1, 1},
                {"4", 0, 1, 1, 1},
                {"5", 1, 1, 1, 1},
                {"6", 2, 1, 1, 1},
                {"CLEAR", 3, 1, 1, 1},
                {"1", 0, 2, 1, 1},
                {"2", 1, 2, 1, 1},
                {"3", 2, 2, 1, 1},
                {"ENTER", 3, 2, 1, 2},
                {"0", 0, 3, 2, 1},
                {"/", 2, 3, 1, 1}
        };

        for (Object[]data : numKeypadLabels) {
            String label = (String) data[0];
            int gridx = (int) data[1];
            int gridy = (int) data[2];
            int gridwidth = (int) data[3];
            int gridheight = (int) data[4];

            JButton button = new JButton(label);
            button.setPreferredSize(new Dimension(100, 100));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = gridx;
            gbc.gridy = gridy;
            gbc.gridwidth = gridwidth;
            gbc.gridheight = gridheight;
            gbc.fill = GridBagConstraints.BOTH;
            gbc.weightx = 1.0;
            gbc.weighty = 1.0;
            gbc.insets = new Insets(5,5,5,5);

            numKeypadPanel.add(button, gbc);
            gbc.gridwidth = 1;
            gbc.gridheight = 1;
        }

        keypadPanel.add(yelKeypadPanel, BorderLayout.CENTER);
        keypadPanel.add(numKeypadPanel, BorderLayout.CENTER);

        window.add(keypadPanel, BorderLayout.EAST);
    }
    public void show() {
        window.setVisible(true);
    }
}

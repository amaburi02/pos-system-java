import javax.swing.*;

public class mainWindow {
    private JFrame window;
    public mainWindow() {
        window = new JFrame();
        window.setTitle("Cash Register");
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setSize(800,800);
        window.setLocationRelativeTo(null);
    }
    public void show() {
        window.setVisible(true);
    }
}

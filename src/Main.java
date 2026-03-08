import ui.LoginForm;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Run the GUI creation on the Event Dispatch Thread (EDT) for thread safety
        SwingUtilities.invokeLater(() -> {
            try {
                // Set system look and feel for a more native appearance (optional)
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Open the initial Login Form
            new LoginForm().setVisible(true);
        });
    }
}

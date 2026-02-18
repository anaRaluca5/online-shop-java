package shop;

import shop.service.ShopService;
import shop.ui.ShopFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {
        }

        // Pornim UI-ul
        SwingUtilities.invokeLater(() -> {
            ShopService service = new ShopService();
            ShopFrame frame = new ShopFrame(service);
            frame.setVisible(true);
        });
    }
}


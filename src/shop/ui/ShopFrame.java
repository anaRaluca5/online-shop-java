package shop.ui;

import shop.model.CartItem;
import shop.model.Product;
import shop.service.ShopService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Fereastra principală a aplicației.
 * UI modernizat: Nimbus, fonturi mai ok, header, spațiere, culori pe butoane.
 */
public class ShopFrame extends JFrame {

    private final ShopService shopService;

    private final DefaultListModel<Product> productListModel = new DefaultListModel<>();
    private final JList<Product> productList = new JList<>(productListModel);

    private final DefaultListModel<CartItem> cartListModel = new DefaultListModel<>();
    private final JList<CartItem> cartList = new JList<>(cartListModel);

    private final JTextField quantityField = new JTextField("1", 5);
    private final JLabel totalLabel = new JLabel("Total: 0.00 RON");

    private JButton addButton;
    private JButton removeButton;
    private JButton clearButton;
    private JButton checkoutButton;

    public ShopFrame(ShopService shopService) {
        this.shopService = shopService;
        initWindow();
        initLayout();
        loadProducts();
        refreshCart();
        applyStyling();
    }

    private void initWindow() {
        setTitle("Magazin online - Coș de cumpărături (Demo)");
        setSize(850, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(750, 450));

        setLayout(new BorderLayout(10, 10));

        // margine în jurul conținutului
        ((JComponent) getContentPane()).setBorder(
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        );
    }

    private void initLayout() {
        // ===== HEADER SUS =====
        JLabel header = new JLabel("Magazin Online — Demo Coș de Cumpărături");
        header.setHorizontalAlignment(SwingConstants.CENTER);
        header.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        header.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.setForeground(new Color(41, 128, 185));
        add(header, BorderLayout.NORTH);

        // ===== STÂNGA: PRODUSE =====
        productList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane productScroll = new JScrollPane(productList);
        productScroll.setBorder(BorderFactory.createTitledBorder("Produse disponibile"));

        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        leftPanel.add(productScroll, BorderLayout.CENTER);

        // ===== DREAPTA: COȘ =====
        cartList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane cartScroll = new JScrollPane(cartList);
        cartScroll.setBorder(BorderFactory.createTitledBorder("Coș de cumpărături"));

        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
        rightPanel.add(cartScroll, BorderLayout.CENTER);

        // ===== PANELELE PRINCIPALE STÂNGA / DREAPTA =====
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);

        // ===== BOTTOM GLOBAL (o singură bară jos) =====
        JPanel bottomPanel = new JPanel(new BorderLayout());

        // stânga: cantitate + Adaugă
        JPanel leftBottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        leftBottom.add(new JLabel("Cantitate:"));
        leftBottom.add(quantityField);
        addButton = new JButton("Adaugă în coș");
        addButton.setToolTipText("Adaugă produsul selectat în coș");
        addButton.addActionListener(this::onAddToCart);
        leftBottom.add(addButton);

        // centru: total
        JPanel centerBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));
        centerBottom.add(totalLabel);

        // dreapta: butoane coș
        removeButton = new JButton("Șterge din coș");
        removeButton.setToolTipText("Șterge produsul selectat din coș");
        removeButton.addActionListener(this::onRemoveFromCart);

        clearButton = new JButton("Golește coșul");
        clearButton.setToolTipText("Șterge toate produsele din coș");
        clearButton.addActionListener(this::onClearCart);

        checkoutButton = new JButton("Finalizează comanda");
        checkoutButton.setToolTipText("Simulează plasarea comenzii");
        checkoutButton.addActionListener(this::onCheckout);

        JPanel rightBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 5));
        rightBottom.add(removeButton);
        rightBottom.add(clearButton);
        rightBottom.add(checkoutButton);

        bottomPanel.add(leftBottom, BorderLayout.WEST);
        bottomPanel.add(centerBottom, BorderLayout.CENTER);
        bottomPanel.add(rightBottom, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

        // 🔹 ENTER în câmpul Cantitate = Adaugă în coș
        quantityField.addActionListener(e -> performAddToCart());
    }

    private void applyStyling() {
        Font baseFont = new Font("Segoe UI", Font.PLAIN, 14);
        productList.setFont(baseFont);
        cartList.setFont(baseFont);
        quantityField.setFont(baseFont);
        totalLabel.setFont(baseFont.deriveFont(Font.BOLD, 16f));

        styleButton(addButton, new Color(52, 152, 219));      // albastru
        styleButton(removeButton, new Color(231, 76, 60));    // roșu
        styleButton(clearButton, new Color(149, 165, 166));   // gri
        styleButton(checkoutButton, new Color(46, 204, 113)); // verde
    }

    private void styleButton(JButton button, Color bg) {
        button.setBackground(bg);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void loadProducts() {
        productListModel.clear();
        for (Product p : shopService.getAvailableProducts()) {
            productListModel.addElement(p);
        }
    }

    private void refreshCart() {
        cartListModel.clear();
        for (CartItem item : shopService.getCartItems()) {
            cartListModel.addElement(item);
        }
        totalLabel.setText("Total: " + String.format("%.2f", shopService.getCartTotal()) + " RON");
    }

    // ==== LOGICA ADD TO CART (folosită și de buton și de Enter) ====

    private void onAddToCart(ActionEvent e) {
        performAddToCart();
    }

    private void performAddToCart() {
        Product selected = productList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this,
                    "Te rog selectează un produs din listă.",
                    "Niciun produs selectat",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Cantitatea trebuie să fie un număr întreg.",
                    "Format invalid",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (quantity <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Cantitatea trebuie să fie mai mare ca 0.",
                    "Cantitate invalidă",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        shopService.addToCart(selected, quantity);
        refreshCart();
    }

    // ===== HANDLERE BUTOANE =====

    private void onRemoveFromCart(ActionEvent e) {
        CartItem selected = cartList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this,
                    "Te rog selectează un produs din coș.",
                    "Niciun produs selectat",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Ești sigur(ă) că vrei să ștergi „" + selected.getProduct().getName() + "” din coș?",
                "Confirmare ștergere",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            shopService.removeFromCart(selected.getProduct());
            refreshCart();
        }
    }

    private void onClearCart(ActionEvent e) {
        if (shopService.isCartEmpty()) {
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Ești sigur(ă) că vrei să golești întreg coșul?",
                "Confirmare",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            shopService.clearCart();
            refreshCart();
        }
    }

    private void onCheckout(ActionEvent e) {
        if (shopService.isCartEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Coșul este gol. Adaugă întâi produse.",
                    "Coș gol",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Acesta este un demo.\nVrei să simulezi plasarea comenzii?",
                "Finalizare comandă",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this,
                    "Comandă „plasată” cu succes! (simulare)\nCoșul va fi golit.",
                    "Succes",
                    JOptionPane.INFORMATION_MESSAGE);
            shopService.clearCart();
            refreshCart();
        }
    }
}

package shop.service;

import shop.model.CartItem;
import shop.model.Product;
import shop.model.ShoppingCart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Layer de service: conține logica aplicației.
 * - gestionează lista de produse disponibile
 * - gestionează coșul de cumpărături
 *
 * UI-ul apelează metodele de aici, fără să modifice direct modelul.
 */
public class ShopService {

    private final List<Product> availableProducts = new ArrayList<>();
    private final ShoppingCart cart = new ShoppingCart();

    public ShopService() {
        initProducts();
    }

    private void initProducts() {
        // Pentru proiect demonstrativ: încărcăm produsele „hardcodat”.
        availableProducts.add(new Product(1, "Laptop", 3500.0));
        availableProducts.add(new Product(2, "Mouse wireless", 80.0));
        availableProducts.add(new Product(3, "Căști Bluetooth", 250.0));
        availableProducts.add(new Product(4, "Telefon", 2800.0));
        availableProducts.add(new Product(5, "Monitor 24\"", 700.0));
    }

    public List<Product> getAvailableProducts() {
        return Collections.unmodifiableList(availableProducts);
    }

    public void addToCart(Product product, int quantity) {
        cart.addProduct(product, quantity);
    }

    public void removeFromCart(Product product) {
        cart.removeProduct(product.getId());
    }

    public List<CartItem> getCartItems() {
        return cart.getItems();
    }

    public double getCartTotal() {
        return cart.getTotal();
    }

    public boolean isCartEmpty() {
        return cart.isEmpty();
    }

    public void clearCart() {
        cart.clear();
    }
}

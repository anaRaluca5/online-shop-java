package shop.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * cos de cumparaturi tinut in memorie
 * fara UI aici construim logica de baza
 */

public class ShoppingCart {
    private final List<CartItem> items= new ArrayList<>();

    /**
     * adauga un produs in cos
     * daca produsul exista deja foar mareste cantitatea
     */

    public void addProduct(Product product, int quantity){
        if (quantity <= 0)
            throw new IllegalArgumentException("Cantitatea trebuie sa fie mai mare ca zero.");

        for (CartItem item : items)
            if (item.getProduct().getId() == product.getId()){
                item.addQuantity(quantity);
                return;
            }
        items.add(new CartItem(product, quantity));
    }

    /**
     * sterge complet un produs din cos (indiferent de cantitate)
     */
    public void removeProduct(int productId){
        items.removeIf(item -> item.getProduct().getId() == productId);
    }

    public void clear(){
        items.clear();
    }

    public boolean isEmpty(){
        return items.isEmpty();
    }

    public double getTotal(){
        double total= 0.0;
        for( CartItem item : items)
            total += item.getSubtotal();

        return total;
    }

    /**
     * lista nu poate fi modificata din exterior
     * read-only
     */

    public List<CartItem> getItems(){
        return  Collections.unmodifiableList(items);
    }
}

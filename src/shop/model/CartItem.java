package shop.model;

/**
 * un element din cos;
 * un produs + o cantitate
 */

public class CartItem {
    private final Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        if ( quantity <= 0 ) {
            throw new IllegalArgumentException("Cantitatea trebuie sa fie mai mare ca zero.");
        }
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void addQuantity(int delta){
        int newQty= this.quantity + delta;
        if ( newQty <= 0)
            throw new IllegalArgumentException("Cantitatea rezultata trebuie sa fie mai mare ca zero.");
        this.quantity = newQty;
    }

    public double getSubtotal(){
        return product.getPrice() * quantity;
    }

    @Override
    public String toString(){
        return product.getName() + " x " + quantity + " = " + String.format("%.2f", getSubtotal()) + " RON";
    }
}

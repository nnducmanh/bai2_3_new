package vn.fit.jakartaee_baitap2_tuan3.dto;

public class CartItemBillDTO {
    private int cartId;
    private String productName;
    private double price;
    private int quantity;
    private double subTotal;

    public CartItemBillDTO() {}

    public CartItemBillDTO(int cartId, String productName, double price, int quantity, double subTotal) {
        this.cartId = cartId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.subTotal = subTotal;
    }

    public int getCartId() { return cartId; }
    public void setCartId(int cartId) { this.cartId = cartId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getSubTotal() { return subTotal; }
    public void setSubTotal(double subTotal) { this.subTotal = subTotal; }
}

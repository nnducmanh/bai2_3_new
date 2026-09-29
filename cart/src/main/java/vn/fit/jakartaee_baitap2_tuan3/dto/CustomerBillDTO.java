package vn.fit.jakartaee_baitap2_tuan3.dto;

import java.util.List;

public class CustomerBillDTO {
    private String customerName;
    private List<CartItemBillDTO> items;
    private double subTotal;
    private double discount;
    private double finalTotal;

    public CustomerBillDTO() {}

    public CustomerBillDTO(String customerName, List<CartItemBillDTO> items, double subTotal, double discount, double finalTotal) {
        this.customerName = customerName;
        this.items = items;
        this.subTotal = subTotal;
        this.discount = discount;
        this.finalTotal = finalTotal;
    }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public List<CartItemBillDTO> getItems() { return items; }
    public void setItems(List<CartItemBillDTO> items) { this.items = items; }

    public double getSubTotal() { return subTotal; }
    public void setSubTotal(double subTotal) { this.subTotal = subTotal; }

    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }

    public double getFinalTotal() { return finalTotal; }
    public void setFinalTotal(double finalTotal) { this.finalTotal = finalTotal; }
}

package vn.fit.jakartaee_baitap2_tuan3.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import vn.fit.jakartaee_baitap2_tuan3.dto.AddCartResponseDTO;
import vn.fit.jakartaee_baitap2_tuan3.dto.CartItemBillDTO;
import vn.fit.jakartaee_baitap2_tuan3.dto.CheckoutSummaryDTO;
import vn.fit.jakartaee_baitap2_tuan3.dto.CustomerBillDTO;
import vn.fit.jakartaee_baitap2_tuan3.model.Product;
import vn.fit.jakartaee_baitap2_tuan3.model.ShoppingCart;
import vn.fit.jakartaee_baitap2_tuan3.repository.ProductRepository;
import vn.fit.jakartaee_baitap2_tuan3.repository.ShoppingCartRepository;
import vn.fit.jakartaee_baitap2_tuan3.repository.impl.ProductRepositoryImpl;
import vn.fit.jakartaee_baitap2_tuan3.repository.impl.ShoppingCartRepositoryImpl;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ShoppingCartService {

    @Inject
    private ShoppingCartRepository cartRepository = new ShoppingCartRepositoryImpl();

    @Inject
    private ProductRepository productRepository = new ProductRepositoryImpl();

    public List<ShoppingCart> getAllCarts() {
        return cartRepository.findAll();
    }

    public ShoppingCart getCartById(int id) {
        return cartRepository.findById(id);
    }

    public boolean createCart(ShoppingCart cart) {
        return cartRepository.save(cart);
    }

    public boolean updateCart(ShoppingCart cart) {
        return cartRepository.update(cart);
    }

    public boolean deleteCart(int id) {
        return cartRepository.delete(id);
    }

    /**
     * Yêu cầu 1: Tính tổng hóa đơn giỏ hàng của một khách hàng (Customer Cart Bill Breakdown)
     */
    public CustomerBillDTO calculateCustomerBill(String customerName) {
        List<ShoppingCart> items = cartRepository.findByCustomerName(customerName);
        List<CartItemBillDTO> itemDTOs = new ArrayList<>();
        double subTotal = 0.0;

        for (ShoppingCart item : items) {
            Product product = productRepository.findById(item.getProductId());
            double price = (product != null) ? product.getPrice() : 0.0;
            String productName = (product != null) ? product.getName() : "Unknown Product";
            double itemTotal = price * item.getQuantity();
            subTotal += itemTotal;

            itemDTOs.add(new CartItemBillDTO(
                    item.getId(),
                    productName,
                    price,
                    item.getQuantity(),
                    itemTotal
            ));
        }

        double discount = 0.0;
        if (subTotal > 2000.0) {
            discount = Math.round(subTotal * 0.10 * 100.0) / 100.0;
        }

        double finalTotal = Math.round((subTotal - discount) * 100.0) / 100.0;
        subTotal = Math.round(subTotal * 100.0) / 100.0;

        return new CustomerBillDTO(customerName, itemDTOs, subTotal, discount, finalTotal);
    }

    /**
     * Yêu cầu 2: Thêm sản phẩm vào giỏ hàng có kiểm tra ràng buộc giá trị tối đa (Add Item with Value Cap)
     */
    public AddCartResponseDTO addItemToCart(ShoppingCart cart) {
        Product product = productRepository.findById(cart.getProductId());
        if (product == null) {
            throw new IllegalArgumentException("Sản phẩm không tồn tại!");
        }

        double estimatedTotal = product.getPrice() * cart.getQuantity();
        if (estimatedTotal > 5000.0) {
            throw new IllegalStateException("Giá trị đơn hàng vượt quá hạn mức cho phép ($5,000)!");
        }

        cartRepository.save(cart);
        return new AddCartResponseDTO(cart.getId(), product.getName(), cart.getQuantity(), estimatedTotal);
    }

    /**
     * Yêu cầu 4: Chuyển đổi toàn bộ giỏ hàng của khách thành Đơn đặt mua và Xóa giỏ (Checkout & Clear Cart)
     */
    public CheckoutSummaryDTO checkout(String customerName) {
        List<ShoppingCart> items = cartRepository.findByCustomerName(customerName);
        if (items.isEmpty()) {
            return new CheckoutSummaryDTO(customerName, 0, 0, 0.0, "NO_ITEMS_IN_CART");
        }

        int totalItems = items.size();
        int totalQuantity = 0;
        double totalPaid = 0.0;

        for (ShoppingCart item : items) {
            totalQuantity += item.getQuantity();
            Product p = productRepository.findById(item.getProductId());
            if (p != null) {
                totalPaid += p.getPrice() * item.getQuantity();
            }
        }

        totalPaid = Math.round(totalPaid * 100.0) / 100.0;

        cartRepository.deleteByCustomerName(customerName);

        return new CheckoutSummaryDTO(
                customerName,
                totalItems,
                totalQuantity,
                totalPaid,
                "CHECKOUT_COMPLETED_AND_CART_CLEARED"
        );
    }
}

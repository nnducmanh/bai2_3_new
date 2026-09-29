package vn.fit.jakartaee_baitap2_tuan3.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import vn.fit.jakartaee_baitap2_tuan3.dto.RepriceReportDTO;
import vn.fit.jakartaee_baitap2_tuan3.dto.RevenueShareDTO;
import vn.fit.jakartaee_baitap2_tuan3.model.Product;
import vn.fit.jakartaee_baitap2_tuan3.model.ShoppingCart;
import vn.fit.jakartaee_baitap2_tuan3.repository.ProductRepository;
import vn.fit.jakartaee_baitap2_tuan3.repository.ShoppingCartRepository;
import vn.fit.jakartaee_baitap2_tuan3.repository.impl.ProductRepositoryImpl;
import vn.fit.jakartaee_baitap2_tuan3.repository.impl.ShoppingCartRepositoryImpl;

import java.util.*;

@ApplicationScoped
public class ProductService {

    @Inject
    private ProductRepository productRepository = new ProductRepositoryImpl();

    @Inject
    private ShoppingCartRepository cartRepository = new ShoppingCartRepositoryImpl();

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(int id) {
        return productRepository.findById(id);
    }

    public boolean createProduct(Product product) {
        return productRepository.save(product);
    }

    public boolean updateProduct(Product product) {
        return productRepository.update(product);
    }

    public boolean deleteProduct(int id) {
        return productRepository.delete(id);
    }

    /**
     * Yêu cầu 3: Điều chỉnh giá sản phẩm hàng loạt theo doanh số giỏ hàng (Dynamic Repricing by Popularity)
     */
    public List<RepriceReportDTO> applyDynamicRepricing() {
        List<Product> products = productRepository.findAll();
        Map<Integer, Integer> totalQtyMap = cartRepository.getTotalQuantityPerProduct();
        List<RepriceReportDTO> reportList = new ArrayList<>();

        for (Product p : products) {
            int totalQty = totalQtyMap.getOrDefault(p.getId(), 0);
            double oldPrice = p.getPrice();
            double newPrice = oldPrice;
            String adjustment = "0%";

            if (totalQty >= 5) {
                newPrice = Math.round(oldPrice * 1.10 * 100.0) / 100.0;
                adjustment = "+10%";
            } else if (totalQty == 0) {
                newPrice = Math.round(oldPrice * 0.95 * 100.0) / 100.0;
                adjustment = "-5%";
            }

            if (newPrice != oldPrice) {
                p.setPrice(newPrice);
                productRepository.update(p);
            }

            reportList.add(new RepriceReportDTO(
                    p.getId(),
                    p.getName(),
                    totalQty,
                    oldPrice,
                    newPrice,
                    adjustment
            ));
        }

        return reportList;
    }

    /**
     * Yêu cầu 5: Thống kê Doanh thu dự kiến & Đóng góp % từng danh mục sản phẩm (Revenue Share Analytics)
     */
    public List<RevenueShareDTO> getRevenueShareReport() {
        List<ShoppingCart> allCarts = cartRepository.findAll();
        double grandTotalRevenue = 0.0;

        Map<Integer, Double> productRevenueMap = new HashMap<>();
        Map<Integer, Integer> productUnitsMap = new HashMap<>();

        for (ShoppingCart cart : allCarts) {
            Product p = productRepository.findById(cart.getProductId());
            if (p != null) {
                double revenue = p.getPrice() * cart.getQuantity();
                grandTotalRevenue += revenue;

                productRevenueMap.put(p.getId(), productRevenueMap.getOrDefault(p.getId(), 0.0) + revenue);
                productUnitsMap.put(p.getId(), productUnitsMap.getOrDefault(p.getId(), 0) + cart.getQuantity());
            }
        }

        List<RevenueShareDTO> reportList = new ArrayList<>();

        for (Map.Entry<Integer, Double> entry : productRevenueMap.entrySet()) {
            int productId = entry.getKey();
            double revenueContribution = Math.round(entry.getValue() * 100.0) / 100.0;
            Product p = productRepository.findById(productId);
            String productName = (p != null) ? p.getName() : "Unknown Product";
            int totalUnits = productUnitsMap.getOrDefault(productId, 0);

            double percentageVal = (grandTotalRevenue > 0) ? (revenueContribution / grandTotalRevenue) * 100.0 : 0.0;
            String percentageStr = String.format(Locale.US, "%.2f%%", percentageVal);

            reportList.add(new RevenueShareDTO(
                    productId,
                    productName,
                    totalUnits,
                    revenueContribution,
                    percentageStr
            ));
        }

        reportList.sort((a, b) -> Double.compare(b.getRevenueContribution(), a.getRevenueContribution()));

        return reportList;
    }
}

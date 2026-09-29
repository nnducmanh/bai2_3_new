package vn.fit.jakartaee_baitap2_tuan3.repository;

import vn.fit.jakartaee_baitap2_tuan3.model.Product;
import java.util.List;

public interface ProductRepository {
    List<Product> findAll();
    Product findById(int id);
    boolean save(Product product);
    boolean update(Product product);
    boolean delete(int id);
}

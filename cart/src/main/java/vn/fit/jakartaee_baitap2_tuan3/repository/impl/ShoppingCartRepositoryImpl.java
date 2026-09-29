package vn.fit.jakartaee_baitap2_tuan3.repository.impl;

import jakarta.enterprise.context.ApplicationScoped;
import vn.fit.jakartaee_baitap2_tuan3.model.ShoppingCart;
import vn.fit.jakartaee_baitap2_tuan3.repository.ShoppingCartRepository;
import vn.fit.jakartaee_baitap2_tuan3.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class ShoppingCartRepositoryImpl implements ShoppingCartRepository {

    @Override
    public List<ShoppingCart> findAll() {
        List<ShoppingCart> list = new ArrayList<>();
        String sql = "SELECT * FROM ShoppingCart";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new ShoppingCart(
                        rs.getInt("id"),
                        rs.getInt("product_id"),
                        rs.getString("customer_name"),
                        rs.getInt("quantity"),
                        rs.getTimestamp("created_at")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public ShoppingCart findById(int id) {
        String sql = "SELECT * FROM ShoppingCart WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new ShoppingCart(
                            rs.getInt("id"),
                            rs.getInt("product_id"),
                            rs.getString("customer_name"),
                            rs.getInt("quantity"),
                            rs.getTimestamp("created_at")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ShoppingCart> findByCustomerName(String customerName) {
        List<ShoppingCart> list = new ArrayList<>();
        String sql = "SELECT * FROM ShoppingCart WHERE customer_name = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, customerName);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new ShoppingCart(
                            rs.getInt("id"),
                            rs.getInt("product_id"),
                            rs.getString("customer_name"),
                            rs.getInt("quantity"),
                            rs.getTimestamp("created_at")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean save(ShoppingCart cart) {
        String sql = "INSERT INTO ShoppingCart (product_id, customer_name, quantity) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, cart.getProductId());
            ps.setString(2, cart.getCustomerName());
            ps.setInt(3, cart.getQuantity());
            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        cart.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(ShoppingCart cart) {
        String sql = "UPDATE ShoppingCart SET product_id = ?, customer_name = ?, quantity = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cart.getProductId());
            ps.setString(2, cart.getCustomerName());
            ps.setInt(3, cart.getQuantity());
            ps.setInt(4, cart.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM ShoppingCart WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteByCustomerName(String customerName) {
        String sql = "DELETE FROM ShoppingCart WHERE customer_name = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, customerName);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Map<Integer, Integer> getTotalQuantityPerProduct() {
        Map<Integer, Integer> map = new HashMap<>();
        String sql = "SELECT product_id, SUM(quantity) AS total_qty FROM ShoppingCart GROUP BY product_id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                map.put(rs.getInt("product_id"), rs.getInt("total_qty"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }
}

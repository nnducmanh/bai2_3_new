package vn.fit.jakartaee_baitap2_tuan3.config;

import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import vn.fit.jakartaee_baitap2_tuan3.repository.ProductRepository;
import vn.fit.jakartaee_baitap2_tuan3.repository.ShoppingCartRepository;
import vn.fit.jakartaee_baitap2_tuan3.repository.impl.ProductRepositoryImpl;
import vn.fit.jakartaee_baitap2_tuan3.repository.impl.ShoppingCartRepositoryImpl;
import vn.fit.jakartaee_baitap2_tuan3.service.ProductService;
import vn.fit.jakartaee_baitap2_tuan3.service.ShoppingCartService;

@ApplicationPath("/api")
public class RestApplication extends ResourceConfig {

    public RestApplication() {
        packages("vn.fit.jakartaee_baitap2_tuan3.controller");

        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(ProductRepositoryImpl.class).to(ProductRepository.class);
                bind(ShoppingCartRepositoryImpl.class).to(ShoppingCartRepository.class);
                bind(ProductService.class).to(ProductService.class);
                bind(ShoppingCartService.class).to(ShoppingCartService.class);
            }
        });
    }
}

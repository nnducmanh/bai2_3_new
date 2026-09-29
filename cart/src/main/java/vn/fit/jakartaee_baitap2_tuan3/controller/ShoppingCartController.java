package vn.fit.jakartaee_baitap2_tuan3.controller;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import vn.fit.jakartaee_baitap2_tuan3.dto.AddCartResponseDTO;
import vn.fit.jakartaee_baitap2_tuan3.dto.CheckoutSummaryDTO;
import vn.fit.jakartaee_baitap2_tuan3.dto.CustomerBillDTO;
import vn.fit.jakartaee_baitap2_tuan3.model.ShoppingCart;
import vn.fit.jakartaee_baitap2_tuan3.service.ShoppingCartService;

import java.util.List;

@RequestScoped
@Path("/carts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ShoppingCartController {

    @Inject
    private ShoppingCartService cartService;

    @GET
    public Response getAll() {
        return Response.ok(cartService.getAllCarts()).build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") int id) {
        ShoppingCart cart = cartService.getCartById(id);
        if (cart == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"message\": \"Cart item not found\"}")
                    .build();
        }
        return Response.ok(cart).build();
    }

    @POST
    public Response create(ShoppingCart cart) {
        boolean success = cartService.createCart(cart);
        if (success) {
            return Response.status(Response.Status.CREATED)
                    .entity("{\"message\": \"Cart item created\", \"id\": " + cart.getId() + "}")
                    .build();
        }
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"message\": \"Failed to create cart item\"}")
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") int id, ShoppingCart cart) {
        cart.setId(id);
        boolean success = cartService.updateCart(cart);
        if (success) {
            return Response.ok("{\"message\": \"Cart item updated\"}").build();
        }
        return Response.status(Response.Status.NOT_MODIFIED).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) {
        boolean success = cartService.deleteCart(id);
        if (success) {
            return Response.ok("{\"message\": \"Cart item deleted\"}").build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    /**
     * Yêu cầu 1: GET /api/carts/bill/{customerName}
     */
    @GET
    @Path("/bill/{customerName}")
    public Response getBillByCustomer(@PathParam("customerName") String customerName) {
        CustomerBillDTO bill = cartService.calculateCustomerBill(customerName);
        return Response.ok(bill).build();
    }

    /**
     * Yêu cầu 2: POST /api/carts/add-with-cap
     */
    @POST
    @Path("/add-with-cap")
    public Response addWithCap(ShoppingCart cart) {
        try {
            AddCartResponseDTO result = cartService.addItemToCart(cart);
            return Response.status(Response.Status.CREATED).entity(result).build();
        } catch (IllegalStateException | IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    /**
     * Yêu cầu 4: POST /api/carts/checkout/{customerName}
     */
    @POST
    @Path("/checkout/{customerName}")
    public Response checkout(@PathParam("customerName") String customerName) {
        CheckoutSummaryDTO summary = cartService.checkout(customerName);
        return Response.ok(summary).build();
    }
}

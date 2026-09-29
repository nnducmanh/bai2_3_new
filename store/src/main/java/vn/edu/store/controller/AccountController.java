package vn.edu.store.controller;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import vn.edu.store.model.Account;
import vn.edu.store.service.AccountService;

import java.util.List;
import java.util.Optional;

@RequestScoped
@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccountController {

    @Inject
    private AccountService accountService = new AccountService();

    @GET
    public Response getAll() {
        List<Account> accounts = accountService.getAllAccounts();
        return Response.ok(accounts).build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") int id) {
        Optional<Account> account = accountService.getAccountById(id);
        if (account.isPresent()) {
            return Response.ok(account.get()).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("{\"message\": \"Account not found\"}")
                .build();
    }

    @POST
    public Response create(Account account) {
        boolean success = accountService.saveAccount(account);
        if (success) {
            return Response.status(Response.Status.CREATED)
                    .entity(account)
                    .build();
        }
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"message\": \"Failed to create account\"}")
                .build();
    }

    @POST
    @Path("/register")
    public Response register(Account account) {
        if (account.getEmail() == null || account.getEmail().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"message\": \"Email is required\"}")
                    .build();
        }

        Optional<Account> existing = accountService.getAccountByEmail(account.getEmail());
        if (existing.isPresent()) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"message\": \"Email already registered\"}")
                    .build();
        }

        boolean success = accountService.saveAccount(account);
        if (success) {
            return Response.status(Response.Status.CREATED).entity(account).build();
        }
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"message\": \"Registration failed\"}")
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") int id, Account account) {
        account.setId(id);
        boolean success = accountService.updateAccount(account);
        if (success) {
            return Response.ok("{\"message\": \"Account updated successfully\"}").build();
        }
        return Response.status(Response.Status.NOT_MODIFIED).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) {
        boolean success = accountService.deleteAccount(id);
        if (success) {
            return Response.ok("{\"message\": \"Account deleted successfully\"}").build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}

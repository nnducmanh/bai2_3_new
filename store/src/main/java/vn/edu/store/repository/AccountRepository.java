package vn.edu.store.repository;

import vn.edu.store.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {

    List<Account> findAll();

    Optional<Account> findById(int id);

    Optional<Account> findByEmail(String email);

    boolean save(Account account);

    boolean update(Account account);

    boolean deleteById(int id);
}

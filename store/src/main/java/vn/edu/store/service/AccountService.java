package vn.edu.store.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import vn.edu.store.model.Account;
import vn.edu.store.repository.AccountRepository;
import vn.edu.store.repository.impl.AccountRepositoryImpl;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class AccountService {

    @Inject
    private AccountRepository accountRepository = new AccountRepositoryImpl();

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Optional<Account> getAccountById(int id) {
        return accountRepository.findById(id);
    }

    public Optional<Account> getAccountByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    public boolean saveAccount(Account account) {
        return accountRepository.save(account);
    }

    public boolean updateAccount(Account account) {
        return accountRepository.update(account);
    }

    public boolean deleteAccount(int id) {
        return accountRepository.deleteById(id);
    }
}

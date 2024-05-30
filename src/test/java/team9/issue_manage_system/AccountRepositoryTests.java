package team9.issue_manage_system;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import team9.issue_manage_system.entity.Account;
import team9.issue_manage_system.repository.AccountRepository;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class AccountRepositoryTests {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void addAdminAccountTest() {
        Account account = new Account("new admin", "password", "admin");
        accountRepository.save(account);
        Optional<Account> optionalAccount = accountRepository.findById(account.getId());
        assertTrue(optionalAccount.isPresent());
        assertEquals(account, optionalAccount.get());
    }

    @Test
    void addPLAccountTest() {
        Account account = new Account("new pl", "password", "pl");
        accountRepository.save(account);
        Optional<Account> optionalAccount = accountRepository.findById(account.getId());
        assertTrue(optionalAccount.isPresent());
        assertEquals(account, optionalAccount.get());
    }

    @Test
    void addDevAccountTest() {
        Account account = new Account("new dev", "password", "dev");
        accountRepository.save(account);
        Optional<Account> optionalAccount = accountRepository.findById(account.getId());
        assertTrue(optionalAccount.isPresent());
        assertEquals(account, optionalAccount.get());
    }

    @Test
    void addTesterAccountTest() {
        Account account = new Account("new tester", "password", "tester");
        accountRepository.save(account);
        Optional<Account> optionalAccount = accountRepository.findById(account.getId());
        assertTrue(optionalAccount.isPresent());
        assertEquals(account, optionalAccount.get());
    }
}

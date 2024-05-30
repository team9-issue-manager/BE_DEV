package team9.issue_manage_system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team9.issue_manage_system.entity.Account;
import team9.issue_manage_system.entity.AdminAuth;
import team9.issue_manage_system.repository.AccountRepository;
import team9.issue_manage_system.repository.AdminAuthRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final AdminAuthRepository adminAuthRepository;

    public void printAccount(Account account) {
        System.out.println("USER INPUT DETECTED");
        System.out.println("INPUT ACCOUNT ID : " + account.getId());
        System.out.println("INPUT ACCOUNT PW : " + account.getPassword());
        System.out.println("INPUT ACCOUNT ROLE : " + account.getRole());
    }


    public Optional<Account> findUser(Account account) {
        Optional<Account> foundAccount = accountRepository.findById(account.getId());
        if (foundAccount.isPresent()) {
            Account user = foundAccount.get();
            if (Objects.equals(user.getPassword(), account.getPassword())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }


    public List<Account> findUserByAccount(String role) {
        return accountRepository.findAllByRole(role);
    }

    public boolean findUserIdCheck(Account account) {
        return accountRepository.existsById(account.getId());
    }

    public boolean uploadAccount(Account account) {
        if (!accountRepository.existsById(account.getId())) {
            Account newAccount = new Account(account.getId(), account.getPassword(), "tester"); // 생성할 떼는 일단 tester로 생성.
            accountRepository.save(newAccount);

            AdminAuth adminAuth = new AdminAuth();
            adminAuth.setRequestAccount(account);
            adminAuth.setRole(account.getRole());
            adminAuthRepository.save(adminAuth);
            return true;
        }
        return false;
    }

    public boolean updateUserRole(Account account) {
        Optional<Account> foundAccount = accountRepository.findById(account.getId());
        if (foundAccount.isPresent()) {
            Account account1 = foundAccount.get();
            account1.setRole(account.getRole());
            accountRepository.save(account1);
            return true;
        }
        return false;
    }
}

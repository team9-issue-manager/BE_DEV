package team9.issue_manage_system.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team9.issue_manage_system.dto.AccountReturnDto;
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


    public List<AccountReturnDto> findUserByAccount(String role) {
        System.out.println(role);

        List<AccountReturnDto> accountReturnDtos = new ArrayList<>();
        List<Account> accounts= accountRepository.findAllByRole(role);
        for (Account account : accounts) {
            AccountReturnDto accountReturnDto = new AccountReturnDto();
            accountReturnDto.setId(account.getId());
            accountReturnDto.setRole(account.getRole());
            accountReturnDtos.add(accountReturnDto);
        }
        return accountReturnDtos;
    }

    public boolean findUserIdCheck(Account account) {
        return accountRepository.existsById(account.getId());
    }

    @Transactional
    public boolean uploadAccount(Account account) {
        System.out.println(account);
        Optional<Account> foundAccount = accountRepository.findById(account.getId());
        if (foundAccount.isEmpty()) {
            Account newAccount = new Account(account.getId(), account.getPassword(), "tester"); // 생성할 떼는 일단 tester로 생성.
            accountRepository.save(newAccount);

            if (account.getRole().equals("tester"))
                return true;
            AdminAuth adminAuth = new AdminAuth();
            adminAuth.setRequestAccount(newAccount);
            adminAuth.setRole(account.getRole());
            System.out.println(adminAuth);
            adminAuthRepository.save(adminAuth);
            return true;
        }
        return false;
    }

    public boolean updateUserRole(Account account) {
        Optional<Account> foundAccount = accountRepository.findById(account.getId());
        if (foundAccount.isPresent()) {
            int number = accountRepository.updateUserRole(foundAccount.get().getId(), account.getRole());
            return (number > 0);
        }
        return false;
    }
}

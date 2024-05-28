package team9.issue_manage_system.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import team9.issue_manage_system.entity.Account;
import team9.issue_manage_system.repository.AccountRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    };
    public void printAccount(Account account) {
        System.out.println("USER INPUT DETECTED");
        System.out.println("INPUT ACCOUNT ID : " + account.getId());
        System.out.println("INPUT ACCOUNT PW : " + account.getPassword());
        System.out.println("INPUT ACCOUNT ROLE : " + account.getRole());
    }
    public ResponseEntity<Map<String, Object>> findUser(Account account) {
        Optional<Account> foundAccount = accountRepository.findById(account.getId());

        Map<String, Object> response = new HashMap<>();
        if (foundAccount.isPresent()) {
            Account user = foundAccount.get();
            if (Objects.equals(user.getPassword(), account.getPassword())) {
                response.put("success", true);
                response.put("id", user.getId());
                response.put("role", user.getRole());
                return ResponseEntity.ok(response);
            }
        }
        response.put("success", false);
        response.put("id", null);
        response.put("role", null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); // 이거 404 not found 어처피 success로 판단하는 거면 .status(HttpStatus.NOT_FOUND)빼 버리기
    }
    public ResponseEntity<Map<String, Boolean>> findUserIdCheck(Account account){
        boolean check_exist = accountRepository.existsById(account.getId());
        Map<String, Boolean> response = new HashMap<String, Boolean>();
        if (check_exist) {
            response.put("exist", true);
            return ResponseEntity.ok(response);
        }
        else {
            response.put("exist", false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    public ResponseEntity<Map<String, Boolean>> uploadAccount(Account account){
        Account newAccount = new Account(account.getId(), account.getPassword(), account.getRole());
        Map<String, Boolean> response = new HashMap<String, Boolean>();

        if (!accountRepository.existsById(account.getId())){ // 해당하는 id의 회원 생성 성공
            System.out.println("account.id: " + newAccount.getId());
            System.out.println("account.password: " + newAccount.getPassword());
            System.out.println("account.role: " + newAccount.getRole());

            // 일단은 account1의 role을 tester로 바꾸고 요청 보내는 걸 추가 해야할듯

            accountRepository.save(newAccount);
            response.put("success", true);
            return ResponseEntity.ok(response);
        }
        else { // 이미 해당하는 id의 회원이 이미 존재 (생성 실패)
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }
    public ResponseEntity<Map<String, Boolean>> updateUserRole(@RequestBody Account account){
        Optional<Account> foundAccount = accountRepository.findById(account.getId());
        Map<String, Boolean> response = new HashMap<String, Boolean>();

        if (foundAccount.isPresent()) {
            Account account1 = foundAccount.get();
            System.out.println("inputRole: " + account);
            System.out.println("foundRole: " + account1);

            account1.setRole(account.getRole());
            response.put("success", true);
            accountRepository.save(account1);
            return ResponseEntity.ok(response);
        }
        else {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}

package team9.issue_manage_system.controller;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team9.issue_manage_system.entity.Account;
import team9.issue_manage_system.repository.AccountRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController  //@Controller + @ResponseBody
@RequiredArgsConstructor // final이 선언된 모든 필드를 인자값으로 하는 생성자를 자동(대신) 생성
public class AccountController {
    private final AccountRepository accountRepository;

    // 그 유저가 있는지 확인하는 거니까 POST, GET 메서드 다 허용.
    @RequestMapping( value = "/userFind", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<Map<String, Object>> findUser(@RequestBody Account account){
        System.out.println("account.id: " + account.getId());
        System.out.println("account.password: " + account.getPassword());
        System.out.println("account.role: " + account.getRole());

        Optional<Account> foundAccount = accountRepository.findById(account.getId());

        Map<String, Object> response = new HashMap<>();
        if (foundAccount.isPresent()) {
            Account user = foundAccount.get();
            response.put("success", true);
            response.put("id", user.getId());
            response.put("role", user.getRole());
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("id", null);
            response.put("role", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); // 이거 404 not found 어처피 success로 판단하는 거면 .status(HttpStatus.NOT_FOUND) 없애 버리기
        }
    }

    /**
     * ID 중복 여부 확인 : 존재하면 true, 새로운 경우 false
     * @param account : Account타입의 클래스 (이 부분은 수정 필요 : ID체크에는 Password가 없으므로)
     */
    @PostMapping("/userIdCheck")
    public Boolean findUserIdCheck(@RequestBody Account account){
        return accountRepository.existsById(account.getId());
    }

    @PostMapping("/userAdd")
    public void uploadAccount(@RequestBody Account account){
        Account account1 = new Account(account.getId(), account.getPassword(), account.getRole());
        if (!accountRepository.existsById(account.getId())){
            System.out.println("account.id: " + account1.getId());
            System.out.println("account.password: " + account1.getPassword());
            System.out.println("account.role: " + account1.getRole());
            accountRepository.save(account1);
        }
    }
}

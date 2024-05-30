package team9.issue_manage_system.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team9.issue_manage_system.entity.Account;
import team9.issue_manage_system.service.AccountService;

import java.util.*;

@RestController  //@Controller + @ResponseBody
@RequiredArgsConstructor // final이 선언된 모든 필드를 인자값으로 하는 생성자를 자동(대신) 생성
@RequestMapping("/user")
public class AccountController {

    private final AccountService accountService;

    // 그 유저가 있는지 확인하는 거니까 POST, GET 메서드 다 허용.
    // 일단 로그인으로 짰어요
    @PostMapping("/find")
    public ResponseEntity<Map<String, Object>> findUser(@RequestBody Account account) {
        accountService.printAccount(account);
        Optional<Account> newAccount = accountService.findUser(account);
        Map<String, Object> response = new HashMap<>();

        if (newAccount.isPresent()) {
            Account user = newAccount.get();
            response.put("success", true);
            response.put("id", user.getId());
            response.put("role", user.getRole());
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("id", null);
            response.put("role", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); // 여기서 404 대신 200을 반환하고 success로 판단
        }
    }

    @GetMapping("/findByRole/{role}")
    public ResponseEntity<Map<String, Object>> findAccountByRole(@PathVariable String role) {
        List<Account> accounts = accountService.findUserByAccount(role);
        Map<String, Object> response = new HashMap<>();
        if (accounts.isEmpty()) {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); // 404 대신 200을 반환하고 success로 판단
        } else {
            response.put("success", true);
            response.put("accounts", accounts);
            return ResponseEntity.ok(response);
        }
    }


    /**
     * ID 중복 여부 확인 : 존재하면 true, 새로운 경우 false
     * @param account : Account타입의 클래스 (이 부분은 수정 필요 : ID체크에는 Password가 없으므로)
     *                -> 어처피 account의 primary key가 id라 딱히 상관없을 듯?
     */
    @PostMapping("/IdCheck")
    public ResponseEntity<Map<String, Boolean>> findUserIdCheck(@RequestBody Account account) {
        accountService.printAccount(account);
        boolean exists = accountService.findUserIdCheck(account);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exist", exists);
        return ResponseEntity.ok(response);
    }

    // 회원가입
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addAccount(@RequestBody Account account){
        accountService.printAccount(account);
        boolean success = accountService.uploadAccount(account);
        Map<String, Object> response = new HashMap<>();
        if (success) {
            response.put("success", true);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            response.put("success", false);
            response.put("errorString", "해당하는 아이디가 이미 존재합니다.");
            return ResponseEntity.ok(response);
        }
    }

    // admin의 user 권한 수정
    @PostMapping("/admin/updateUserRole")
    public ResponseEntity<Map<String, Boolean>> updateUserRole(@RequestBody Account account) {
        accountService.printAccount(account);
        boolean success = accountService.updateUserRole(account);
        Map<String, Boolean> response = new HashMap<>();
        if (success) {
            response.put("success", true);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            response.put("success", false);
            return ResponseEntity.ok(response);
        }
    }
}

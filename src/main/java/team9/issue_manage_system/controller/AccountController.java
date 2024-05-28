package team9.issue_manage_system.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team9.issue_manage_system.entity.Account;
import team9.issue_manage_system.repository.AccountRepository;
import team9.issue_manage_system.repository.AdminAuthRepository;

import java.util.*;

@RestController  //@Controller + @ResponseBody
@RequiredArgsConstructor // final이 선언된 모든 필드를 인자값으로 하는 생성자를 자동(대신) 생성
public class AccountController {

    private final AccountRepository accountRepository;
    private final AdminAuthRepository adminAuthRepository;

    // 그 유저가 있는지 확인하는 거니까 POST, GET 메서드 다 허용.
    // 일단 로그인으로 짰어요
    @RequestMapping( value = "/userFind", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<Map<String, Object>> findUser(@RequestBody Account account){
        System.out.println("account.id: " + account.getId());
        System.out.println("account.password: " + account.getPassword());
        // System.out.println("account.role: " + account.getRole());

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

    //@G

    /**
     * ID 중복 여부 확인 : 존재하면 true, 새로운 경우 false
     * @param account : Account타입의 클래스 (이 부분은 수정 필요 : ID체크에는 Password가 없으므로)
     *                -> 어처피 account의 primary key가 id라 딱히 상관없을 듯?
     */
    @PostMapping("/userIdCheck")
    public ResponseEntity<Map<String, Boolean>> findUserIdCheck(@RequestBody Account account){
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

    // 회원가입
    @PostMapping("/userAdd")
    public ResponseEntity<Map<String, Boolean>> uploadAccount(@RequestBody Account account){
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

    // admin의 user 권한 수정
    @PostMapping("/admin/updateUserRole")
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

package team9.issue_manage_system.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team9.issue_manage_system.entity.Account;
import team9.issue_manage_system.repository.AccountRepository;
import team9.issue_manage_system.repository.AdminAuthRepository;
import team9.issue_manage_system.service.AccountService;

import java.util.*;

@RestController  //@Controller + @ResponseBody
@RequiredArgsConstructor // final이 선언된 모든 필드를 인자값으로 하는 생성자를 자동(대신) 생성
@RequestMapping("/user")
public class AccountController {

    private final AccountRepository accountRepository;
    private final AdminAuthRepository adminAuthRepository;
    private final AccountService accountService;

    // 그 유저가 있는지 확인하는 거니까 POST, GET 메서드 다 허용.
    // 일단 로그인으로 짰어요
    @RequestMapping( value = "/find", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<Map<String, Object>> findUser(@RequestBody Account account){
        accountService.printAccount(account);
        return accountService.findUser(account);
    }

    /**
     * ID 중복 여부 확인 : 존재하면 true, 새로운 경우 false
     * @param account : Account타입의 클래스 (이 부분은 수정 필요 : ID체크에는 Password가 없으므로)
     *                -> 어처피 account의 primary key가 id라 딱히 상관없을 듯?
     */
    @PostMapping("/IdCheck")
    public ResponseEntity<Map<String, Boolean>> findUserIdCheck(@RequestBody Account account){
        accountService.printAccount(account);
        return accountService.findUserIdCheck(account);
    }

    // 회원가입
    @PostMapping("/add")
    public ResponseEntity<Map<String, Boolean>> uploadAccount(@RequestBody Account account){
        accountService.printAccount(account);
        return accountService.uploadAccount(account);
    }

    // admin의 user 권한 수정
    @PostMapping("/admin/updateUserRole")
    public ResponseEntity<Map<String, Boolean>> updateUserRole(@RequestBody Account account) {
        accountService.printAccount(account);
        return accountService.updateUserRole(account);
    }
}

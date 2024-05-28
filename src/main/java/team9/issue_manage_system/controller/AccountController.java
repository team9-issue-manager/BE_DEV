package team9.issue_manage_system.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team9.issue_manage_system.entity.Account;
import team9.issue_manage_system.repository.AccountRepository;
import team9.issue_manage_system.service.AccountService;
import team9.issue_manage_system.repository.AdminAuthRepository;


import java.util.*;

@RestController  //@Controller + @ResponseBody
@RequiredArgsConstructor // final이 선언된 모든 필드를 인자값으로 하는 생성자를 자동(대신) 생성
public class AccountController {

    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final AdminAuthRepository adminAuthRepository;

    // 그 유저가 있는지 확인하는 거니까 POST, GET 메서드 다 허용.
    // 일단 로그인으로 짰어요
    @RequestMapping( value = "/userFind", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<Map<String, Object>> findUser(@RequestBody Account account){
        accountService.debugAccount(account);
        return accountService.findUser(account);
        // 이거 404 not found 어처피 success로 판단하는 거면 .status(HttpStatus.NOT_FOUND)빼 버리기
    }

    /**
     * ID 중복 여부 확인 : 존재하면 true, 새로운 경우 false
     * @param account : Account타입의 클래스 (이 부분은 수정 필요 : ID체크에는 Password가 없으므로)
     *                -> 어처피 account의 primary key가 id라 딱히 상관없을 듯?
     */
    @PostMapping("/userIdCheck")
    public ResponseEntity<Map<String, Boolean>> findUserIdCheck(@RequestBody Account account){
        accountService.debugAccount(account);
        return accountService.findUserIdCheck(account);
    }

    // 회원가입
    @PostMapping("/userAdd")
    public ResponseEntity<Map<String, Boolean>> uploadAccount(@RequestBody Account account){
        accountService.debugAccount(account);
        return accountService.uploadAccount(account);
    }

    // admin의 user 권한 수정
    @PostMapping("/admin/updateUserRole")
    public ResponseEntity<Map<String, Boolean>> updateUserRole(@RequestBody Account account){
        accountService.debugAccount(account);
        return accountService.updateUserRole(account);
    }
}

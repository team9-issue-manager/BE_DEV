package team9.issue_manage_system.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import team9.issue_manage_system.entity.Account;
import team9.issue_manage_system.repository.AccountRepository;

import java.util.List;
import java.util.Optional;

@RestController  //@Controller + @ResponseBody
@RequiredArgsConstructor // final이 선언된 모든 필드를 인자값으로 하는 생성자를 자동(대신) 생성
public class AccountController {
    private final AccountRepository accountRepository;

    @PostMapping("/userFind")
    public Optional<Account> findUser(@RequestBody Account account){
        return accountRepository.findById(account.getId());
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
        Account account1 = new Account(account.getId(), account.getPassword(), "tester");
        accountRepository.save(account1);
    }
}

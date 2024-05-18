package team9.issue_manage_system.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team9.issue_manage_system.entity.Account;
import team9.issue_manage_system.repository.AccountRepository;

import java.util.List;
import java.util.Optional;

@RestController  //@Controller + @ResponseBody
@RequiredArgsConstructor // final이 선언된 모든 필드를 인자값으로 하는 생성자를 자동(대신) 생성
public class AccountController {
    private final AccountRepository accountRepository;

    @GetMapping(value = "/userfind/{id}") //user 찾기
    public Optional<Account> findUser(@PathVariable("id") String id) { // 먼저 존재여부를 파악하고 Optional을 써야할 듯
        return accountRepository.findById(id);
    }

    @GetMapping("/useradd/{id},{password}")
    public void uploadAccount(@PathVariable("id") String id, @PathVariable("password") String password){
        Account account = new Account(id, password); //수정필요

        accountRepository.save(account);
        //return accountRepository.findAll();
        //public List<Account>
    }
}

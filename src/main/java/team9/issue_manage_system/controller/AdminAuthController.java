package team9.issue_manage_system.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team9.issue_manage_system.dto.AdminAuthDto;
import team9.issue_manage_system.entity.Account;
import team9.issue_manage_system.entity.AdminAuth;
import team9.issue_manage_system.service.AccountService;
import team9.issue_manage_system.service.AdminAuthService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminAuthController {

    private final AdminAuthService adminAuthService;
    private final AccountService accountService;

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> adminAuthListAll() {
        List<AdminAuthDto> adminAuthServiceList = adminAuthService.adminAuthListAll();
        Map<String, Object> response = new HashMap<>();
        if (!adminAuthServiceList.isEmpty()) {
            response.put("success", true);
            response.put("requestList", adminAuthServiceList);
            return ResponseEntity.ok(response);
        }
        response.put("success", false);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // admin의 user 권한 수정
    @PostMapping("/updateUserRole")
    public ResponseEntity<Map<String, Boolean>> updateUserRole(@RequestBody Account account) {
        boolean success = accountService.updateUserRole(account);
        boolean delSuccess = false;
        Map<String, Boolean> response = new HashMap<>();
        if (success)
            delSuccess = adminAuthService.adminAuthDelete(account.getId());
        if (delSuccess) {
            response.put("success", true);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            response.put("success", false);
            return ResponseEntity.ok(response);
        }
    }
}

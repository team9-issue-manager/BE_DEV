package team9.issue_manage_system.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team9.issue_manage_system.entity.AdminAuth;
import team9.issue_manage_system.service.AdminAuthService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminAuthController {

    private AdminAuthService adminAuthService;

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> adminAuthListAll() {
        List<AdminAuth> adminAuthServiceList = adminAuthService.adminAuthListAll();
        Map<String, Object> response = new HashMap<>();
        if (!adminAuthServiceList.isEmpty()) {
            response.put("success", true);
            response.put("requestList", adminAuthServiceList);
            return ResponseEntity.ok(response);
        }
        response.put("success", false);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}

package team9.issue_manage_system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import team9.issue_manage_system.entity.AdminAuth;
import team9.issue_manage_system.repository.AdminAuthRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminAuthService {

    private final AdminAuthRepository adminAuthRepository;

    public List<AdminAuth> adminAuthListAll() {
        return adminAuthRepository.findAll();
    }
}

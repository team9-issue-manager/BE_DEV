package team9.issue_manage_system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import team9.issue_manage_system.entity.AdminAuth;
import team9.issue_manage_system.repository.AdminAuthRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminAuthService {

    private final AdminAuthRepository adminAuthRepository;

    public List<AdminAuth> adminAuthListAll() {
        return adminAuthRepository.findAll();
    }

    public boolean adminAuthDelete(String id) {
        Optional<AdminAuth> adminAuth = adminAuthRepository.findByRequestAccount_Id(id);
        if (adminAuth.isPresent()) {
            adminAuthRepository.deleteById(adminAuth.get().getRequestNum());
            return true;
        }
        return false;
    }
}

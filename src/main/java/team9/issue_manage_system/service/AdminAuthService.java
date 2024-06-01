package team9.issue_manage_system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team9.issue_manage_system.dto.AccountReturnDto;
import team9.issue_manage_system.dto.AdminAuthDto;
import team9.issue_manage_system.entity.Account;
import team9.issue_manage_system.entity.AdminAuth;
import team9.issue_manage_system.repository.AdminAuthRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminAuthService {

    private final AdminAuthRepository adminAuthRepository;
    private final AccountService accountService;

    public List<AdminAuthDto> adminAuthListAll() {
        List<AdminAuth> adminAuths = adminAuthRepository.findAll();
        List<AdminAuthDto> adminAuthDtos = new ArrayList<>();
        if (!adminAuths.isEmpty()) {
            for (AdminAuth adminAuth : adminAuths) {
                Account account = adminAuth.getRequestAccount();
                AccountReturnDto accountReturnDto = new AccountReturnDto();
                AdminAuthDto adminAuthDto = new AdminAuthDto();
                accountReturnDto.setId(account.getId());
                accountReturnDto.setRole(account.getRole());
                adminAuthDto.setRequestAccount(accountReturnDto);
                adminAuthDto.setRequestNum(adminAuth.getRequestNum());
                adminAuthDtos.add(adminAuthDto);
            }
        }
        return adminAuthDtos;
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

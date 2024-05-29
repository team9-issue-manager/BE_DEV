package team9.issue_manage_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team9.issue_manage_system.entity.Account;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, String> {
    List<Account> findAllByRole(String role);
}

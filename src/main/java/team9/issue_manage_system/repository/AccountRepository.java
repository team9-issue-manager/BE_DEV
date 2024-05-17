package team9.issue_manage_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team9.issue_manage_system.entity.Account;

public interface AccountRepository extends JpaRepository<Account, String> {
}

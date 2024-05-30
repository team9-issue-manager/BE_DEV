package team9.issue_manage_system.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team9.issue_manage_system.entity.Account;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, String> {
    List<Account> findAllByRole(String role);
    @Modifying
    @Transactional
    @Query("UPDATE Account a SET a.role = :role WHERE a.id = :id")
    int updateUserRole(@Param("id") String id, @Param("role") String role);
}

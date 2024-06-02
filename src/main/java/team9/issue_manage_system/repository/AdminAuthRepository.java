package team9.issue_manage_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team9.issue_manage_system.entity.AdminAuth;

import java.util.Optional;

@Repository
public interface AdminAuthRepository extends JpaRepository<AdminAuth, Long>{
    Optional<AdminAuth> findByRequestAccount_Id(String accountId);
}

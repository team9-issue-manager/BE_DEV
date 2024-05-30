package team9.issue_manage_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team9.issue_manage_system.entity.AdminAuth;

public interface AdminAuthRepository extends JpaRepository<AdminAuth, Long>{
}

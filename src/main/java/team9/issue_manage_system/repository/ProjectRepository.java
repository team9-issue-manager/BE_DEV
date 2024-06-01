package team9.issue_manage_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team9.issue_manage_system.entity.Project;


@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
}

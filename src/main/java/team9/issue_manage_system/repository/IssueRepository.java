package team9.issue_manage_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team9.issue_manage_system.entity.Account;
import team9.issue_manage_system.entity.Issue;
public interface IssueRepository extends JpaRepository<Issue, String> {
}

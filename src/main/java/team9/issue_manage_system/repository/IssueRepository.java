package team9.issue_manage_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team9.issue_manage_system.entity.Account;
import team9.issue_manage_system.entity.Issue;

import java.util.List;
public interface IssueRepository extends JpaRepository<Issue, Long> {
    List<Issue> findByTitleContaining(String title);
    int countByDeveloperAndStateBetween(Account developer, int startState, int endState);
}

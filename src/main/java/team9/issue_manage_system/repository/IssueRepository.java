package team9.issue_manage_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team9.issue_manage_system.entity.Account;
import team9.issue_manage_system.entity.Issue;

import java.util.List;
public interface IssueRepository extends JpaRepository<Issue, Long> {
    List<Issue> findAllByTitleContaining(String title);
    List<Issue> findAllByTagContaining(String tag);
    List<Issue> findAllByAccountIdContaining(String accountId);
    int countByDeveloperAndStateBetween(Account developer, int startState, int endState);
}

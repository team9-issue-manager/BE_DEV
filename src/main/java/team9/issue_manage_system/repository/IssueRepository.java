package team9.issue_manage_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team9.issue_manage_system.entity.Account;
import team9.issue_manage_system.entity.Issue;

import java.util.List;


@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {
    List<Issue> findAllByTitleContaining(String title);
    List<Issue> findAllByTagContaining(String tag);
    List<Issue> findAllByAccountIdContaining(String accountId);
    int countByDeveloperAndStateBetween(Account developer, int startState, int endState);
}

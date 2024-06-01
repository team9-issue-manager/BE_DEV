package team9.issue_manage_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team9.issue_manage_system.entity.Comment;
import team9.issue_manage_system.entity.Issue;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByIssue_IssueNum(Long issueNum);

}

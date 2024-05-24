package team9.issue_manage_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team9.issue_manage_system.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}

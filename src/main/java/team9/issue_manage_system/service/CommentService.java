package team9.issue_manage_system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team9.issue_manage_system.dto.CommentDto;
import team9.issue_manage_system.entity.Comment;
import team9.issue_manage_system.entity.Issue;
import team9.issue_manage_system.repository.CommentRepository;
import team9.issue_manage_system.repository.IssueRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final IssueRepository issueRepository;

    public List<Comment> getCommentsByIssueId(Long issueNum) {
        return commentRepository.findByIssue_IssueNum(issueNum);
    }

    public Optional<Comment> getCommentById(Long issueNum, Long commentId) {
        return commentRepository.findById(commentId);
    }

    public Optional<Comment> uploadComment(CommentDto commentDto) {
        Optional<Issue> issueOpt = issueRepository.findById(commentDto.getIssueNum());

        if (issueOpt.isPresent()) {
            Issue issue = issueOpt.get();
            Comment comment = new Comment();
            comment.setContent(commentDto.getContent());
            comment.setDate(commentDto.getDate());
            comment.setIssue(issue);

            return Optional.of(commentRepository.save(comment));
        } else {
            return Optional.empty();
        }
    }
}


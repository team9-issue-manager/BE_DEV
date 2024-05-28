package team9.issue_manage_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team9.issue_manage_system.entity.Comment;
import team9.issue_manage_system.repository.CommentRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    // 특정 이슈의 모든 댓글 가져오기
    @GetMapping("/issue/{issueId}/comments")
    public List<Comment> getCommentsByIssueId(@PathVariable int issueId) {
        // 여기서는 단순히 모든 댓글을 반환하는 것으로 가정합니다.
        return commentRepository.findAllByIssueId(issueId);
    }

    // 특정 이슈의 특정 댓글 하나 가져오기
    @GetMapping("/issue/{issueId}/comment/{commentId}")
    public Optional<Comment> getCommentById(@PathVariable int issueId, @PathVariable long commentId) {
        // 특정 이슈의 특정 댓글을 가져오는 로직을 구현해야 합니다.
        // 여기서는 단순히 ID로 댓글을 찾는 것으로 가정합니다.
        return commentRepository.findById(commentId);
    }

    // 새로운 댓글 추가하기
    @PostMapping("/issue/{issueId}/comment")
    public Comment addComment(@PathVariable int issueId, @RequestBody Comment newComment) {
        // 새로운 댓글을 추가하는 로직을 구현해야 합니다.
        newComment.setIssue(issueId);
        return commentRepository.save(newComment);
    }
}

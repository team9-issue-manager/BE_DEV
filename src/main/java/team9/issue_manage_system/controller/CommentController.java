package team9.issue_manage_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team9.issue_manage_system.dto.CommentDto;
import team9.issue_manage_system.entity.Comment;
import team9.issue_manage_system.entity.Issue;
import team9.issue_manage_system.repository.CommentRepository;
import team9.issue_manage_system.repository.IssueRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;
    private IssueRepository issueRepository;

    // 특정 이슈의 모든 댓글 가져오기
    @GetMapping("/issue/{issueNum}/comments")
    public List<Comment> getCommentsByIssueId(@PathVariable int issueNum) {
        // 여기서는 단순히 모든 댓글을 반환하는 것으로 가정합니다.
        return commentRepository.findAllByIssueId(issueNum);
    }

    // 특정 이슈의 특정 댓글 하나 가져오기
    @GetMapping("/issue/{issueNum}/comment/{commentId}")
    public Optional<Comment> getCommentById(@PathVariable int issueNum, @PathVariable long commentId) {
        // 특정 이슈의 특정 댓글을 가져오는 로직을 구현해야 합니다.
        // 여기서는 단순히 ID로 댓글을 찾는 것으로 가정합니다.
        return commentRepository.findById(commentId);
    }

    // 새로운 댓글 추가하기
    @PostMapping("/issue/{issueNum}/comment")
    public ResponseEntity<Map<String, Object>> uploadComment(@RequestBody CommentDto commentDto) {
        Optional<Issue> issueOpt = issueRepository.findById(commentDto.getIssueNum());
        System.out.println("check issue: " + issueOpt);
        Map<String, Object> response = new HashMap<>();

        if (issueOpt.isPresent()) {
            Issue issue = issueOpt.get();
            Comment comment = new Comment();
            comment.setContent(commentDto.getContent());
            comment.setDate(commentDto.getDate());
            comment.setIssue(issue);

            issueRepository.save(issue);
            response.put("success", true);
            response.put("comment", comment);
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}

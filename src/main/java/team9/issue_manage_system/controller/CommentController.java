package team9.issue_manage_system.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team9.issue_manage_system.dto.CommentDto;
import team9.issue_manage_system.entity.Comment;
import team9.issue_manage_system.service.CommentService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/issue")
public class CommentController {
    private final CommentService commentService;

    // 특정 이슈의 모든 댓글 가져오기
    @GetMapping("/{issueNum}/comments")
    public ResponseEntity<Map<String, Object>> getCommentsByIssueId(@PathVariable Long issueNum) {
        List<Comment> comments = commentService.getCommentsByIssueId(issueNum);
        Map<String, Object> response = new HashMap<>();
        response.put("success", comments);
        return ResponseEntity.ok(response);
    }

    // 특정 이슈의 특정 댓글 하나 가져오기
    @GetMapping("/{issueNum}/comments/{commentId}")
    public Optional<Comment> getCommentById(@PathVariable Long issueNum, @PathVariable long commentId) {
        return commentService.getCommentById(issueNum, commentId);
    }

    // 새로운 댓글 추가하기
    @PostMapping("/{issueNum}/comments")
    public ResponseEntity<Map<String, Object>> uploadComment(@RequestBody CommentDto commentDto) {
        Optional<Comment> commentOpt = commentService.uploadComment(commentDto);
        Map<String, Object> response = new HashMap<>();

        if (commentOpt.isPresent()) {
            response.put("success", true);
            response.put("comment", commentOpt.get());
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}


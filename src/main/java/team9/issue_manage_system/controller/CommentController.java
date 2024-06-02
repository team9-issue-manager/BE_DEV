package team9.issue_manage_system.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team9.issue_manage_system.dto.CommentCreateDto;
import team9.issue_manage_system.dto.CommentReturnDto;
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
        System.out.println(issueNum);
        List<CommentReturnDto> commentReturnDtos = commentService.getCommentsByIssueId(issueNum);
        Map<String, Object> response = new HashMap<>();
        if (commentReturnDtos.isEmpty()) {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        response.put("success", true);
        response.put("comment", commentReturnDtos);
        return ResponseEntity.ok(response);
    }

    // 특정 이슈의 특정 댓글 하나 가져오기
    @GetMapping("/{issueNum}/comments/{commentId}")
    public ResponseEntity<Map<String, Object>> getCommentById(@PathVariable Long issueNum, @PathVariable Long commentId) {
        Optional<CommentReturnDto> commentReturnDto = commentService.getCommentById(issueNum, commentId);
        Map<String, Object> response = new HashMap<>();

        if (commentReturnDto.isPresent()) {
            response.put("success", true);
            response.put("comment", commentReturnDto.get());
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // 새로운 댓글 추가하기
    @PostMapping("/comments")
    public ResponseEntity<Map<String, Object>> uploadComment(@RequestBody CommentCreateDto commentCreateDto) {
        Optional<CommentReturnDto> commentReturnDto = commentService.uploadComment(commentCreateDto);
        Map<String, Object> response = new HashMap<>();

        if (commentReturnDto.isPresent()) {
            response.put("success", true);
            response.put("comment", commentReturnDto.get());
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}


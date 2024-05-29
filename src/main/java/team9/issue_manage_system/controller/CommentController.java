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
import java.util.stream.Collectors;

@RestController
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private IssueRepository issueRepository;

    //특정 이슈의 모든 댓글 가져오기
    @GetMapping("/issue/{issueNum}/comments")
    public List<CommentDto> getCommentsByIssueId(@PathVariable Long issueNum) {
        List<Comment> comments = commentRepository.findByIssue_IssueNum(issueNum);
        return comments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CommentDto convertToDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setIssueNum(comment.getIssue().getIssueNum());
        commentDto.setContent(comment.getContent());
        commentDto.setDate(comment.getDate());
        commentDto.setId(comment.getCommentId());
        return commentDto;
    }

    // 특정 이슈의 특정 댓글 하나 가져오기
    @GetMapping("/issue/{issueNum}/comments/{commentId}")
    public Optional<Comment> getCommentById(@PathVariable Long issueNum, @PathVariable long commentId) {
        // 여기서는 단순히 ID로 댓글을 찾는 것으로 가정합니다.
        return commentRepository.findById(commentId);
    }

    // 새로운 댓글 추가하기
    @PostMapping("/issue/{issueNum}/comments")
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

            commentRepository.save(comment);
            response.put("success", true);
            response.put("comment", comment);
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}

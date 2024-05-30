package team9.issue_manage_system.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team9.issue_manage_system.dto.IssueAssignDevAutoDto;
import team9.issue_manage_system.dto.IssueAssignDevDto;
import team9.issue_manage_system.dto.IssueDto;
import team9.issue_manage_system.dto.IssueSearchDto;
import team9.issue_manage_system.entity.Issue;
import team9.issue_manage_system.service.IssueService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/issue")
public class IssueController {

    private final IssueService issueService;

    @PostMapping("/find")
    public ResponseEntity<Map<String, Object>> searchIssueByFilter(@RequestBody IssueSearchDto issueSearchDto) {
        return issueService.searchIssueByFilter(issueSearchDto);
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> issueListAll() {
        List<Issue> issues = issueService.issueListAll();
        Map<String, Object> response = new HashMap<>();
        response.put("issues", issues);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> uploadIssue(@RequestBody IssueDto issueDto) {
        return issueService.uploadIssue(issueDto);
    }

    @PostMapping("/assignDev")
    public ResponseEntity<Map<String, Object>> assignDev(@RequestBody IssueAssignDevDto issueAssignDev) {
        return issueService.assignDev(issueAssignDev);
    }

    @PostMapping("/assignDevAuto")
    public ResponseEntity<Map<String, Object>> assignDevAuto(@RequestBody IssueAssignDevAutoDto issueAssignDevAuto) {
        return issueService.assignDevAuto(issueAssignDevAuto);
    }
}


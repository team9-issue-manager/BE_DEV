package team9.issue_manage_system.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team9.issue_manage_system.dto.IssueAssignDevDto;
import team9.issue_manage_system.dto.IssueDto;
import team9.issue_manage_system.dto.IssueSearchDto;
import team9.issue_manage_system.entity.Issue;
import team9.issue_manage_system.service.IssueService;

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
    public List<Issue> issueListAll() {
        return issueService.issueListAll();
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> uploadIssue(@RequestBody IssueDto issueDto) {
        return issueService.uploadIssue(issueDto);
    }

    @PostMapping("/assignDev")
    public ResponseEntity<Map<String, Object>> assignDev(@RequestBody IssueAssignDevDto request) {
        return issueService.assignDev(request);
    }

    @PostMapping("/assignDevAuto")
    public ResponseEntity<Map<String, Object>> assignDevAuto(@RequestBody Issue.IssueAssignDev issueAssignDev) {
        return issueService.assignDevAuto(issueAssignDev);
    }
}


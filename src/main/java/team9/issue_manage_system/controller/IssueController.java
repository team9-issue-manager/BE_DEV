package team9.issue_manage_system.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team9.issue_manage_system.dto.*;
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
        Map<String, Object> response = new HashMap<>();
        List<IssueReturnDto> issueReturnDto = issueService.searchIssueByFilter(issueSearchDto);
        response.put("success", !issueReturnDto.isEmpty());
        if (!issueReturnDto.isEmpty()) {
            response.put("issues", issueReturnDto);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> issueListAll() {
        List<IssueReturnDto> issueReturnDtos = issueService.issueListAll();
        Map<String, Object> response = new HashMap<>();
        response.put("issues", issueReturnDtos);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/changeState")
    public ResponseEntity<Map<String, Object>> changeState(@RequestBody IssueChangeStateDto issueChangeStateDto) {
        System.out.println(issueChangeStateDto);
        boolean result = issueService.changeState(issueChangeStateDto);
        Map<String, Object> response = new HashMap<>();
        if (result) {
            response.put("success", true);
        }
        else {
            response.put("success", false);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> uploadIssue(@RequestBody IssueCreateDto issueCreateDto) {
        return issueService.uploadIssue(issueCreateDto);
    }

    @PostMapping("/assignDev")
    public ResponseEntity<Map<String, Object>> assignDev(@RequestBody IssueAssignDevDto issueAssignDev) {
        return issueService.assignDev(issueAssignDev);
    }

    @PostMapping("/assignDevAuto")
    public ResponseEntity<Map<String, Object>> assignDevAuto(@RequestBody IssueAssignDevAutoDto issueAssignDevAuto) {
        return issueService.assignDevAuto(issueAssignDevAuto);
    }

    @GetMapping("/statistics")
    public ResponseEntity<IssueStatisticsDto> getIssueStatistics() {
        IssueStatisticsDto statistics = issueService.getIssueStatistics();
        return ResponseEntity.ok(statistics);
    }
}


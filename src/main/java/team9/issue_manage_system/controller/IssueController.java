package team9.issue_manage_system.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team9.issue_manage_system.dto.*;
import team9.issue_manage_system.entity.Account;
import team9.issue_manage_system.service.IssueService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        Map<String, Object> response = new HashMap<>();
        boolean issueCreateCheck = issueService.uploadIssue(issueCreateDto);

        if (issueCreateCheck) {
            response.put("success", true);
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping("/assignDev")
    public ResponseEntity<Map<String, Object>> assignDev(@RequestBody IssueAssignDevDto issueAssignDev) {
        Map<String, Object> response = new HashMap<>();
        boolean success = issueService.assignDev(issueAssignDev);

        if (success) {
            response.put("success", true);
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/assignDevAuto")
    public ResponseEntity<Map<String, Object>> assignDevAuto(@RequestBody IssueAssignDevAutoDto issueAssignDevAuto) {
        Map<String, Object> response = new HashMap<>();
        Optional<Account> selectedDevOpt = issueService.assignDevAuto(issueAssignDevAuto);

        if (selectedDevOpt.isPresent()) {
            response.put("success", true);
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/statistics")
    public ResponseEntity<IssueStatisticsDto> getIssueStatistics() {
        IssueStatisticsDto statistics = issueService.getIssueStatistics();
        return ResponseEntity.ok(statistics);
    }
}


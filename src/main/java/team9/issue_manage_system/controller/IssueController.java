package team9.issue_manage_system.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team9.issue_manage_system.dto.IssueAssignDevDto;
import team9.issue_manage_system.entity.Account;
import team9.issue_manage_system.entity.Issue;
import team9.issue_manage_system.dto.IssueDto;
import team9.issue_manage_system.repository.AccountRepository;
import team9.issue_manage_system.repository.IssueRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class IssueController {

    private final IssueRepository issueRepository;
    private final AccountRepository accountRepository;

    @GetMapping("/issueFind")
    public List<Issue> searchIssueByTitle(@RequestBody Issue.IssueSearchRequest issueSearchRequest) {
        String title = issueSearchRequest.getTitle();
        if (title == null) {
            throw new IllegalArgumentException("Title cannot be null");
        }
        return issueRepository.findByTitleContaining(title);
    }

    @GetMapping("/issueList")
    public List<Issue> issueList() {
        return issueRepository.findAll();
    }
//
//    @PostMapping("/issue/Add")
//    public void uploadIssue(@RequestBody Issue issue){
//        System.out.println("issue: " + issue);
//        issueRepository.save(issue);
//    }

    @PostMapping("/issueAdd")
    public ResponseEntity<Map<String, Object>> uploadIssue(@RequestBody IssueDto issueDto) {
        Optional<Account> accountOpt = accountRepository.findById(issueDto.getAccountId());
        Map<String, Object> response = new HashMap<>();

        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();
            Issue issue = new Issue();
            issue.setTitle(issueDto.getTitle());
            issue.setContent(issueDto.getContent());
            issue.setAccount(account);
            issue.setTags(issueDto.getTags());
            issue.setState(0); // 기본값을 0으로 설정

            issueRepository.save(issue);
            response.put("success", true);
            response.put("issue", issue);
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }


    @PostMapping("/issue/assignDev") // 직접 dev 입력
    public ResponseEntity<Map<String, Object>> assignDev(@RequestBody IssueAssignDevDto request) {
        Optional<Issue> issueOpt = issueRepository.findById(request.getIssueNum());
        Optional<Account> devOpt = accountRepository.findById(request.getDevId());
        Map<String, Object> response = new HashMap<>();

        if (issueOpt.isPresent() && devOpt.isPresent()) {
            Issue issue = issueOpt.get();
            Account developer = devOpt.get();

            if (!"dev".equals(developer.getRole())) {
                response.put("success", false);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            issue.setDeveloper(developer);
            issue.setState(1); // state: assigned
            issueRepository.save(issue);

            response.put("success", true);
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }


//    @PostMapping("/issue/assignDevAuto") // 직접 dev 입력
//    public ResponseEntity<Map<String, Object>> assignDevAuto(@RequestBody IssueAssignDevDto request) {
//        Optional<Issue> issueOpt = issueRepository.findById(request.getIssueNum());
//        //Optional<Account> devOpt = accountRepository.findById(request.getDevId());
//        // 윗부분 알고리즘으로 특정 dev를 찾아 넣어야 할듯.? 테스트시 해당부분 전체 주석하고 하자.
//        Map<String, Object> response = new HashMap<>();
//
//        if (issueOpt.isPresent() && devOpt.isPresent()) {
//            Issue issue = issueOpt.get();
//            Account developer = devOpt.get();
//
//            if (!"dev".equals(developer.getRole())) {
//                response.put("success", false);
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//            }
//
//            issue.setDeveloper(developer);
//            issue.setState(1); // state: assigned
//            issueRepository.save(issue);
//
//            response.put("success", true);
//            return ResponseEntity.ok(response);
//        } else {
//            response.put("success", false);
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
//        }
//    }
}

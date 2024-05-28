package team9.issue_manage_system.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team9.issue_manage_system.dto.IssueAssignDevDto;
import team9.issue_manage_system.dto.IssueSearchDto;
import team9.issue_manage_system.entity.Account;
import team9.issue_manage_system.entity.Issue;
import team9.issue_manage_system.dto.IssueDto;
import team9.issue_manage_system.repository.AccountRepository;
import team9.issue_manage_system.repository.IssueRepository;

import java.util.*;

@RestController
@RequiredArgsConstructor
public class IssueController {

    private final IssueRepository issueRepository;
    private final AccountRepository accountRepository;

    @PostMapping("/issueFind")
    public ResponseEntity<Map<String, Object>> searchIssueByFilter(@RequestBody IssueSearchDto issueSearchDto) {
        String filterValue = issueSearchDto.getFilter(); // title, tag, writer
        Map<String, Object> response = new HashMap<>();
        List<Issue> issueList = Collections.emptyList();
        if (filterValue.equals("title")) {
            issueList= searchByTitle(issueSearchDto.getValue());
        }
        else if (filterValue.equals("tag")) {
            issueList = searchByTag(issueSearchDto.getValue());
        }
        else if (filterValue.equals("writer")) {
            issueList= searchByWritter(issueSearchDto.getValue());
        }

        response.put("success", !issueList.isEmpty());
        response.put("issues", issueList);
        return ResponseEntity.ok(response); // 해당 title을 포함하는 모드 issue find
    }

    private List<Issue> searchByTitle(String filterValue) {
        return issueRepository.findByTitleContaining(filterValue);
    }

    private List<Issue> searchByTag(String filterValue) {
        return issueRepository.findByTagContaining(filterValue);
    }

    private List<Issue> searchByWritter(String filterValue) {
        return issueRepository.findByAccountIdContaining(filterValue);
    }

    // 모든 issue를 나열
    @GetMapping("/issueList")
    public List<Issue> issueListAll() {
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
        System.out.println("check issue: " +  accountOpt);
        Map<String, Object> response = new HashMap<>();

        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();
            Issue issue = new Issue();
            issue.setTitle(issueDto.getTitle());
            issue.setContent(issueDto.getContent());
            issue.setAccount(account);
            issue.setTag(issueDto.getTag());
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


    @PostMapping("/issue/assignDev") // 직접 dev 입력 // pl의 권한
    public ResponseEntity<Map<String, Object>> assignDev(@RequestBody IssueAssignDevDto request) {
        Optional<Issue> issueOpt = issueRepository.findById(request.getIssueNum());
        Optional<Account> devOpt = accountRepository.findById(request.getDevId());
        Map<String, Object> response = new HashMap<>();
        // devId = null인 경우만 추가시킬지 아니면 그냥 다 바꿀 수 있도록 냅둘지 고민해봐야할 듯.
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


    @PostMapping("/issue/assignDevAuto") // pl의 권한 자동으로 dev 배정
    public ResponseEntity<Map<String, Object>> assignDevAuto(@RequestBody Issue.IssueAssignDev issueAssignDev) {
        Long issueNum = issueAssignDev.getIssueNum();
        Map<String, Object> response = new HashMap<>();

        Optional<Issue> issueOpt = issueRepository.findById(issueNum);
        if (issueOpt.isEmpty()) { // 해당하는 issue가 없음
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        Issue issue = issueOpt.get();
        List<Account> developers = accountRepository.findByRole("dev");

        Account selectedDev = null;
        int minAssignedIssues = Integer.MAX_VALUE;
        int maxResolvedIssues = -1;

        for (Account developer : developers) {
            int assignedIssuesCount = issueRepository.countByDeveloperAndStateBetween(developer, 1, 2);
            int resolvedIssuesCount = issueRepository.countByDeveloperAndStateBetween(developer, 3, 4);

            if (assignedIssuesCount < minAssignedIssues ||
                    (assignedIssuesCount == minAssignedIssues && resolvedIssuesCount > maxResolvedIssues)) {
                selectedDev = developer;
                minAssignedIssues = assignedIssuesCount;
                maxResolvedIssues = resolvedIssuesCount;
            }
        }

        if (selectedDev == null) { // dev가 없음.
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        issue.setDeveloper(selectedDev);
        issue.setState(1); // state: assigned
        issueRepository.save(issue);

        response.put("success", true); // dev가 잘 할당 됨.
        return ResponseEntity.ok(response);
    }
}

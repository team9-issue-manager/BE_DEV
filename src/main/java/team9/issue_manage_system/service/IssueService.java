package team9.issue_manage_system.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import team9.issue_manage_system.dto.IssueAssignDevDto;
import team9.issue_manage_system.dto.IssueDto;
import team9.issue_manage_system.dto.IssueSearchDto;
import team9.issue_manage_system.entity.Account;
import team9.issue_manage_system.entity.Issue;
import team9.issue_manage_system.repository.AccountRepository;
import team9.issue_manage_system.repository.IssueRepository;

import java.util.*;

@Service
public class IssueService {
    private final IssueRepository issueRepository;
    private final AccountRepository accountRepository;

    public IssueService(IssueRepository issueRepository, AccountRepository accountRepository) {
        this.issueRepository = issueRepository;
        this.accountRepository = accountRepository;
    }

    public ResponseEntity<Map<String, Object>> searchIssueByFilter(IssueSearchDto issueSearchDto) {
        String filterValue = issueSearchDto.getFilter();
        Map<String, Object> response = new HashMap<>();
        List<Issue> issueList = Collections.emptyList();

        if (filterValue.equals("title")) {
            issueList = issueRepository.findByTitleContaining(issueSearchDto.getValue());
        } else if (filterValue.equals("tag")) {
            issueList = issueRepository.findByTagContaining(issueSearchDto.getValue());
        } else if (filterValue.equals("writer")) {
            issueList = issueRepository.findByAccountIdContaining(issueSearchDto.getValue());
        }

        response.put("success", !issueList.isEmpty());
        response.put("issues", issueList);
        return ResponseEntity.ok(response);
    }

    public List<Issue> issueListAll() {
        return issueRepository.findAll();
    }

    public ResponseEntity<Map<String, Object>> uploadIssue(IssueDto issueDto) {
        Optional<Account> accountOpt = accountRepository.findById(issueDto.getAccountId());
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

    public ResponseEntity<Map<String, Object>> assignDev(IssueAssignDevDto request) {
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

    public ResponseEntity<Map<String, Object>> assignDevAuto(Issue.IssueAssignDev issueAssignDev) {
        Long issueNum = issueAssignDev.getIssueNum();
        Map<String, Object> response = new HashMap<>();

        Optional<Issue> issueOpt = issueRepository.findById(issueNum);
        if (issueOpt.isEmpty()) {
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

        if (selectedDev == null) {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        issue.setDeveloper(selectedDev);
        issue.setState(1); // state: assigned
        issueRepository.save(issue);

        response.put("success", true);
        return ResponseEntity.ok(response);
    }
}


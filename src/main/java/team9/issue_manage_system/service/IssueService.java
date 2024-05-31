package team9.issue_manage_system.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import team9.issue_manage_system.dto.*;
import team9.issue_manage_system.entity.Account;
import team9.issue_manage_system.entity.Issue;
import team9.issue_manage_system.entity.Project;
import team9.issue_manage_system.repository.AccountRepository;
import team9.issue_manage_system.repository.IssueRepository;
import team9.issue_manage_system.repository.ProjectRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IssueService {
    private final IssueRepository issueRepository;
    private final AccountRepository accountRepository;
    private final ProjectRepository projectRepository;

    public List<IssueReturnDto> searchIssueByFilter(IssueSearchDto issueSearchDto) {
        System.out.println(issueSearchDto);

        String filterValue = issueSearchDto.getFilter();
        List<Issue> issueList = Collections.emptyList();
        List<IssueReturnDto> issueReturnDtos = new ArrayList<>();

        if (filterValue.equals("title")) {
            issueList = issueRepository.findAllByTitleContaining(issueSearchDto.getValue());
        }
        else if (filterValue.equals("tag")) {
            issueList = issueRepository.findAllByTagContaining(issueSearchDto.getValue());
        }
        else if (filterValue.equals("writer")) {
            issueList = issueRepository.findALLByAccount_Id(issueSearchDto.getValue());
        }
        if (!issueList.isEmpty())
        {
            for (Issue issue : issueList){
                IssueReturnDto dto = makeIssueReturnDto(issue);
                issueReturnDtos.add(dto);
            }
        }
        return issueReturnDtos;
    }

    public List<IssueReturnDto> issueListAll() {
        List<Issue> issues = issueRepository.findAll();
        List<IssueReturnDto> issueReturnDtos = new ArrayList<>();
        for (Issue issue : issues){
            IssueReturnDto dto = makeIssueReturnDto(issue);
            issueReturnDtos.add(dto);
        }

        return issueReturnDtos;
    }

    public ResponseEntity<Map<String, Object>> uploadIssue(IssueCreateDto issueCreateDto) {
        System.out.println(issueCreateDto);
        Optional<Account> accountOpt = accountRepository.findById(issueCreateDto.getAccountId());
        Optional<Project> projectOpt = projectRepository.findById(issueCreateDto.getProjectNum());
        Map<String, Object> response = new HashMap<>();

        if (accountOpt.isPresent() && projectOpt.isPresent()) {
            Account account = accountOpt.get();
            Issue issue = new Issue();
            issue.setTitle(issueCreateDto.getTitle());
            issue.setContent(issueCreateDto.getContent());
            issue.setAccount(account);
            issue.setProject(projectOpt.get());
            issue.setTag(issueCreateDto.getTag());
            issue.setState(0); // 기본값을 0으로 설정
            issueRepository.save(issue);

            IssueReturnDto issueReturnDto = makeIssueReturnDto(issue);

            response.put("success", true);
            response.put("issue", issueReturnDto);
            return ResponseEntity.ok(response);
        } else {
            response.put("result", "이슈를 생성할 수 없습니다."); // 실패 시.
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }


    public IssueReturnDto makeIssueReturnDto(Issue issue) {
        IssueReturnDto issueReturnDto = new IssueReturnDto();
        issueReturnDto.setIssueNum(issue.getIssueNum());
        issueReturnDto.setTitle(issue.getTitle());
        issueReturnDto.setContent(issue.getContent());
        issueReturnDto.setAccountId(issue.getAccount().getId());
        issueReturnDto.setProjectNum(issue.getProject().getProjectNum());
        issueReturnDto.setState(issue.getState());
        issueReturnDto.setDate(issue.getDate());
        issueReturnDto.setTag(issue.getTag());

        // Null 체크
        if (issue.getDeveloper() != null) {
            issueReturnDto.setDevId(issue.getDeveloper().getId());
        } else {
            issueReturnDto.setDevId(""); // 기본 값으로 빈 문자열 설정
        }

        return issueReturnDto;
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

    public ResponseEntity<Map<String, Object>> assignDevAuto(IssueAssignDevAutoDto issueAssignDevAuto) {
        Long issueNum = issueAssignDevAuto.getIssueNum();
        Map<String, Object> response = new HashMap<>();

        Optional<Issue> issueOpt = issueRepository.findById(issueNum);
        if (issueOpt.isEmpty()) {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        Issue issue = issueOpt.get();
        List<Account> developers = accountRepository.findAllByRole("dev");

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

    public IssueStatisticsDto getIssueStatistics() {
        Long totalIssues = issueRepository.count();

        Map<String, Long> issuesByStatus = issueRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        issue -> {
                            switch (issue.getState()) {
                                case 0: return "new";
                                case 1: return "assigned";
                                case 2: return "fixed";
                                case 3: return "resolved";
                                case 4: return "closed";
                                default: return "unknown";
                            }
                        }, Collectors.counting()
                ));

        Map<String, Long> issuesByDeveloper = issueRepository.findAll()
                .stream()
                .filter(issue -> issue.getDeveloper() != null)
                .collect(Collectors.groupingBy(
                        issue -> issue.getDeveloper().getId(),
                        Collectors.counting()
                ));

        return new IssueStatisticsDto(totalIssues, issuesByStatus, issuesByDeveloper);
    }

}


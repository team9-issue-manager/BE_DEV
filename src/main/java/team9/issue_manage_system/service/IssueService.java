package team9.issue_manage_system.service;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;


@Service
@RequiredArgsConstructor
public class IssueService {
    private static final Logger logger = LoggerFactory.getLogger("test-logger");

    private final IssueRepository issueRepository;
    private final AccountRepository accountRepository;
    private final ProjectRepository projectRepository;

    public List<IssueReturnDto> searchIssueByFilter(IssueSearchDto issueSearchDto) {
        logger.info("Searching issues with filter: {}", issueSearchDto);

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
        else if (filterValue.equals("devId")) {
            issueList = issueRepository.findAllByDeveloper_Id(issueSearchDto.getValue());
        }
        else if (filterValue.equals("state")) {
            issueList = issueRepository.findAllByState(Integer.valueOf(issueSearchDto.getValue()));
        }
        else if (filterValue.equals("priority")) {
            issueList = issueRepository.findAllByPriority(Integer.valueOf(issueSearchDto.getValue()));
        }
        if (!issueList.isEmpty())
        {
            for (Issue issue : issueList){
                IssueReturnDto dto = makeIssueReturnDto(issue);
                issueReturnDtos.add(dto);
            }
        }
        return sortIssueReturnDto(issueReturnDtos);
    }

    public List<IssueReturnDto> issueListAll() {
        logger.info("Retrieving all issues");
        List<Issue> issues = issueRepository.findAll();
        List<IssueReturnDto> issueReturnDtos = new ArrayList<>();
        for (Issue issue : issues){
            IssueReturnDto dto = makeIssueReturnDto(issue);
            issueReturnDtos.add(dto);
        }
        return sortIssueReturnDto(issueReturnDtos);
    }

    public boolean changeState(IssueChangeStateDto issueChangeStateDto) {
        logger.info("Changing state for issue: {}", issueChangeStateDto);
        Optional<Issue> issueOpt = issueRepository.findById(issueChangeStateDto.getIssueNum());
        if (issueOpt.isPresent()) {
            Issue issue = issueOpt.get();
            if (issue.getState() == 1 && issueChangeStateDto.getAccountId().equals(issue.getDeveloper().getId())) { //issue의 devId와 입력된 accountId가 일치하면, fixed 로 변경
                issue.setState(2);
                issueRepository.save(issue);
                logger.info("Issue state changed to fixed: {}", issue);
                return true;
            }
            else if (issue.getState() == 2 && issueChangeStateDto.getAccountId().equals(issue.getAccount().getId())) {
                issue.setState(3);
                issueRepository.save(issue);
                logger.info("Issue state changed to resolved: {}", issue);
                return true;
            }
            else if (issue.getState() == 3 && issueChangeStateDto.getAccountId().equals(issue.getProject().getProjectLeader().getId())) {
                issue.setState(4);
                issueRepository.save(issue);
                logger.info("Issue state changed to closed: {}", issue);
                return true;
            }
            else if (issue.getState() == 4 && issueChangeStateDto.getAccountId().equals(issue.getProject().getProjectLeader().getId())) {
                issue.setState(1);
                issueRepository.save(issue);
                logger.info("Issue state changed to assigned: {}", issue);
                return true;

            }
            else {
                logger.warn("Issue state change failed due to invalid state or account ID: {}", issueChangeStateDto);
                return false;
            }
        }
        logger.warn("Issue not found: {}", issueChangeStateDto);
        return false;
    }

    public boolean uploadIssue(IssueCreateDto issueCreateDto) {
        logger.info("Uploading new issue: {}", issueCreateDto);
        Optional<Account> accountOpt = accountRepository.findById(issueCreateDto.getAccountId());
        Optional<Project> projectOpt = projectRepository.findById(issueCreateDto.getProjectNum());

        if (accountOpt.isPresent() && projectOpt.isPresent()) {
            Account account = accountOpt.get();
            Issue issue = new Issue();
            issue.setTitle(issueCreateDto.getTitle());
            issue.setContent(issueCreateDto.getContent());
            issue.setAccount(account);
            issue.setProject(projectOpt.get());
            issue.setTag(issueCreateDto.getTag());
            issue.setPriority(issueCreateDto.getPriority());
            issue.setState(0); // 기본값을 0으로 설정
            issueRepository.save(issue);
            logger.info("Issue created successfully: {}", issue);
            return true;
        } else {
            logger.warn("Failed to create issue. Account or Project not found: {}", issueCreateDto);
            return false;
        }
    }

    public IssueReturnDto makeIssueReturnDto(Issue issue) {
        IssueReturnDto issueReturnDto = new IssueReturnDto();
        issueReturnDto.setIssueNum(issue.getIssueNum());
        issueReturnDto.setTitle(issue.getTitle());
        issueReturnDto.setContent(issue.getContent());
        issueReturnDto.setAccountId(issue.getAccount().getId());
        issueReturnDto.setProjectNum(issue.getProject().getProjectNum());
        issueReturnDto.setPriority(issue.getPriority());
        issueReturnDto.setState(issue.getState());
        issueReturnDto.setDate(issue.getDate());
        issueReturnDto.setTag(issue.getTag());

        // Null 체크
        if (issue.getDeveloper() != null) {
            issueReturnDto.setDevId(issue.getDeveloper().getId());
        } else {
            issueReturnDto.setDevId(""); // 기본 값으로 빈 문자열 설정
        }

        logger.debug("IssueReturnDto created: {}", issueReturnDto);
        return issueReturnDto;
    }

    public List<IssueReturnDto> sortIssueReturnDto(List<IssueReturnDto> issueReturnDtos) {
        logger.info("Sorting IssueReturnDto list");
        return issueReturnDtos.stream()
                .sorted(Comparator.comparingInt(IssueReturnDto::getPriority))
                .collect(Collectors.toList());
    }

    public boolean assignDev(IssueAssignDevDto request) {
        logger.info("Assigning developer: {}", request);
        Optional<Issue> issueOpt = issueRepository.findById(request.getIssueNum());
        Optional<Account> devOpt = accountRepository.findById(request.getDevId());

        if (issueOpt.isPresent() && devOpt.isPresent()) {
            Issue issue = issueOpt.get();
            Account developer = devOpt.get();

            if ("dev".equals(developer.getRole()) && issue.getProject().getProjectLeader().getId().equals(request.getAccountId())) {
                issue.setDeveloper(developer);
                issue.setState(1); // state: assigned
                issueRepository.save(issue);
                logger.info("Developer assigned successfully: {}", developer);
                return true;
            }
            logger.warn("Failed to assign developer due to role or project leader mismatch: {}", request);
            return false;
        } else {
            logger.warn("Issue or Developer not found: {}", request);
            return false;
        }
    }

    public Optional<Account> assignDevAuto(IssueAssignDevAutoDto issueAssignDevAuto) {
        logger.info("Auto-assigning developer for issue: {}", issueAssignDevAuto);
        Long issueNum = issueAssignDevAuto.getIssueNum();
        Optional<Issue> issueOpt = issueRepository.findById(issueNum);

        if (issueOpt.isEmpty()) {
            logger.warn("Issue not found for auto-assign: {}", issueAssignDevAuto);
            return Optional.empty();
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

        if (selectedDev != null && issue.getProject().getProjectLeader().getId().equals(issueAssignDevAuto.getAccountId())) {
            issue.setDeveloper(selectedDev);
            issue.setState(1); // state: assigned
            issueRepository.save(issue);
            logger.info("Developer auto-assigned successfully: {}", selectedDev);
            return Optional.of(selectedDev);
        }
        logger.warn("Auto-assign failed due to project leader mismatch or no developer selected: {}", issueAssignDevAuto);
        return Optional.empty();
    }

    public IssueStatisticsDto getIssueStatistics() {
        logger.info("Getting issue statistics");

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

        Map<String, Long> issuesByDay = issueRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        issue -> issue.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(DateTimeFormatter.ISO_DATE),
                        Collectors.counting()
                ));

        Map<String, Long> issuesByMonth = issueRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        issue -> issue.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM")),
                        Collectors.counting()
                ));

        IssueStatisticsDto statisticsDto = new IssueStatisticsDto(totalIssues, issuesByStatus, issuesByDeveloper, issuesByDay, issuesByMonth);
        logger.info("Issue statistics retrieved: {}", statisticsDto);

        return statisticsDto;
    }
}

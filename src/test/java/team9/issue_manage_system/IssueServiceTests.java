package team9.issue_manage_system;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import team9.issue_manage_system.dto.*;
import team9.issue_manage_system.entity.Account;
import team9.issue_manage_system.entity.Issue;
import team9.issue_manage_system.entity.Project;
import team9.issue_manage_system.repository.AccountRepository;
import team9.issue_manage_system.repository.IssueRepository;
import team9.issue_manage_system.repository.ProjectRepository;
import team9.issue_manage_system.service.IssueService;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IssueServiceTests {

    @Mock
    private IssueRepository issueRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private IssueService issueService;

    private Issue issue;
    private Account account;
    private Project project;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setId("new dev");
        account.setRole("dev");

        project = new Project();
        project.setProjectNum(1L);

        issue = new Issue();
        issue.setIssueNum(1L);
        issue.setTitle("Test Issue");
        issue.setContent("Test Content");
        issue.setAccount(account);
        issue.setProject(project);
        issue.setTag("bug");
        issue.setState(0);
        issue.setDate(Date.from(Instant.now()));
    }

    @Test
    void searchIssueByFilterTitle() {
        // Given
        IssueSearchDto searchDto = new IssueSearchDto();
        searchDto.setFilter("title");
        searchDto.setValue("Test Issue");

        when(issueRepository.findAllByTitleContaining("Test Issue")).thenReturn(Collections.singletonList(issue));

        // When
        List<IssueReturnDto> results = issueService.searchIssueByFilter(searchDto);

        // Then
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("Test Issue", results.get(0).getTitle());

        verify(issueRepository, times(1)).findAllByTitleContaining("Test Issue");
    }

    @Test
    void issueListAll() {
        // Given
        when(issueRepository.findAll()).thenReturn(Collections.singletonList(issue));

        // When
        List<IssueReturnDto> results = issueService.issueListAll();

        // Then
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("Test Issue", results.get(0).getTitle());

        verify(issueRepository, times(1)).findAll();
    }


    @Test
    void uploadIssueSuccess() {
        // Given
        IssueCreateDto createDto = new IssueCreateDto();
        createDto.setTitle("Test Issue");
        createDto.setContent("Test Content");
        createDto.setAccountId("new dev");
        createDto.setProjectNum(1L);
        createDto.setTag("bug");

        when(accountRepository.findById("new dev")).thenReturn(Optional.of(account));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(issueRepository.save(any(Issue.class))).thenReturn(issue);

        // When
        boolean result = issueService.uploadIssue(createDto);

        // Then
        assertTrue(result);
        verify(issueRepository, times(1)).save(any(Issue.class));
    }


    @Test
    void assignDevSuccess() {
        // Given
        IssueAssignDevDto assignDto = new IssueAssignDevDto();
        assignDto.setIssueNum(1L);
        assignDto.setDevId("new dev");
        assignDto.setAccountId("project_leader");

        Account projectLeader = new Account();
        projectLeader.setId("project_leader");
        project.setProjectLeader(projectLeader);

        when(issueRepository.findById(1L)).thenReturn(Optional.of(issue));
        when(accountRepository.findById("new dev")).thenReturn(Optional.of(account));

        // When
        boolean result = issueService.assignDev(assignDto);

        // Then
        assertTrue(result);
        assertEquals(account, issue.getDeveloper());
        assertEquals(1, issue.getState());

        verify(issueRepository, times(1)).save(issue);
    }

    @Test
    void assignDevFailRole() {
        // Given
        IssueAssignDevDto assignDto = new IssueAssignDevDto();
        assignDto.setIssueNum(1L);
        assignDto.setDevId("new dev");
        assignDto.setAccountId("project_leader");

        Account nonDev = new Account();
        nonDev.setId("new dev");
        nonDev.setRole("non-dev");

        when(issueRepository.findById(1L)).thenReturn(Optional.of(issue));
        when(accountRepository.findById("new dev")).thenReturn(Optional.of(nonDev));

        // When
        boolean result = issueService.assignDev(assignDto);

        // Then
        assertFalse(result);
        verify(issueRepository, never()).save(any(Issue.class));
    }

    @Test
    void assignDevAutoSuccess() {
        // Given
        IssueAssignDevAutoDto autoAssignDto = new IssueAssignDevAutoDto();
        autoAssignDto.setIssueNum(1L);
        autoAssignDto.setAccountId("project_leader");

        Account projectLeader = new Account();
        projectLeader.setId("project_leader");
        project.setProjectLeader(projectLeader);

        Account dev1 = new Account();
        dev1.setId("dev1");
        dev1.setRole("dev");

        Account dev2 = new Account();
        dev2.setId("dev2");
        dev2.setRole("dev");

        List<Account> developers = Arrays.asList(dev1, dev2);

        when(issueRepository.findById(1L)).thenReturn(Optional.of(issue));
        when(accountRepository.findAllByRole("dev")).thenReturn(developers);
        when(issueRepository.countByDeveloperAndStateBetween(dev1, 1, 2)).thenReturn(2);
        when(issueRepository.countByDeveloperAndStateBetween(dev2, 1, 2)).thenReturn(1);
        when(issueRepository.countByDeveloperAndStateBetween(dev1, 3, 4)).thenReturn(3);
        when(issueRepository.countByDeveloperAndStateBetween(dev2, 3, 4)).thenReturn(2);

        // When
        Optional<Account> result = issueService.assignDevAuto(autoAssignDto);

        // Then
        assertTrue(result.isPresent());
        assertEquals("dev2", result.get().getId());
        assertEquals(dev2, issue.getDeveloper());
        assertEquals(1, issue.getState());

        verify(issueRepository, times(1)).save(issue);
    }

    @Test
    void getIssueStatistics() {
        // Given
        when(issueRepository.count()).thenReturn(5L);
        when(issueRepository.findAll()).thenReturn(Collections.singletonList(issue));

        // When
        IssueStatisticsDto stats = issueService.getIssueStatistics();

        // Then
        assertNotNull(stats);
        assertEquals(5, stats.getTotalIssues());
        assertTrue(stats.getIssuesByStatus().containsKey("new"));
        assertEquals(1, stats.getIssuesByStatus().get("new"));
    }
}

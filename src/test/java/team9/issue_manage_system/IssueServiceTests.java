package team9.issue_manage_system;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import team9.issue_manage_system.dto.IssueAssignDevDto;
import team9.issue_manage_system.dto.IssueCreateDto;
import team9.issue_manage_system.dto.IssueReturnDto;
import team9.issue_manage_system.entity.Account;
import team9.issue_manage_system.entity.Issue;
import team9.issue_manage_system.entity.Project;
import team9.issue_manage_system.repository.AccountRepository;
import team9.issue_manage_system.repository.IssueRepository;
import team9.issue_manage_system.repository.ProjectRepository;
import team9.issue_manage_system.service.IssueService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
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

    private IssueCreateDto issueCreateDto;
    private Issue issue;
    private Account account;
    private Project project;

    @BeforeEach
    void setUp() {
        issueCreateDto = new IssueCreateDto();
        issueCreateDto.setTitle("Test Issue");
        issueCreateDto.setContent("Test Content");
        issueCreateDto.setAccountId("new dev");
        issueCreateDto.setProjectNum(1L);
        issueCreateDto.setTag("bug");

        issue = new Issue();
        issue.setIssueNum(1L);
        issue.setTitle("Test Issue");
        issue.setContent("Test Content");
        issue.setAccount(account);
        issue.setProject(project);
        issue.setTag("bug");
        issue.setState(0);

        account = new Account();
        account.setId("new dev");

        project = new Project();
        project.setProjectNum(1L);
    }

    @Test
    void uploadIssueSuccess() {
        when(accountRepository.findById("new dev")).thenReturn(Optional.of(account));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(issueRepository.save(any(Issue.class))).thenReturn(issue);

        Map<String, Object> response = issueService.uploadIssue(issueCreateDto).getBody();

        assertNotNull(response);
        assertTrue((Boolean) response.get("success"));
        IssueReturnDto issueReturnDto = (IssueReturnDto) response.get("issue");
        assertEquals("Test Issue", issueReturnDto.getTitle());

        verify(issueRepository, times(1)).save(any(Issue.class));
    }

    @Test
    void uploadIssueFail() {
        when(accountRepository.findById("new dev")).thenReturn(Optional.empty());

        Map<String, Object> response = issueService.uploadIssue(issueCreateDto).getBody();

        assertNotNull(response);
        assertEquals("이슈를 생성할 수 없습니다.", response.get("result"));

        verify(issueRepository, never()).save(any(Issue.class));
    }

    @Test
    void assignDevSuccess() {
        when(issueRepository.findById(1L)).thenReturn(Optional.of(issue));
        when(accountRepository.findById("new dev")).thenReturn(Optional.of(account));
        account.setRole("dev");

        Map<String, Object> response = issueService.assignDev(new IssueAssignDevDto()).getBody();

        assertNotNull(response);
        assertTrue((Boolean) response.get("success"));
        assertEquals(1, issue.getState());

        verify(issueRepository, times(1)).save(issue);
    }

    @Test
    void assignDevFailRole() {
        when(issueRepository.findById(1L)).thenReturn(Optional.of(issue));
        when(accountRepository.findById("new dev")).thenReturn(Optional.of(account));
        account.setRole("user");

        Map<String, Object> response = issueService.assignDev(new IssueAssignDevDto()).getBody();

        assertNotNull(response);
        assertFalse((Boolean) response.get("success"));

        verify(issueRepository, never()).save(issue);
    }

    @Test
    void assignDevFailNotFound() {
        when(issueRepository.findById(1L)).thenReturn(Optional.empty());

        Map<String, Object> response = issueService.assignDev(new IssueAssignDevDto()).getBody();

        assertNotNull(response);
        assertFalse((Boolean) response.get("success"));

        verify(issueRepository, never()).save(any(Issue.class));
    }
}

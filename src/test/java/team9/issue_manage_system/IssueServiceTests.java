package team9.issue_manage_system;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import team9.issue_manage_system.dto.*;
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
    private IssueReturnDto issueReturnDto;
    private Issue issue;
    private Account account;
    private Project project;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setId("new dev");

        project = new Project();
        project.setProjectNum(1L);

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
    }


    @Test
    void uploadIssueSuccess() {
        // Given
        when(accountRepository.findById("new dev")).thenReturn(Optional.of(account));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(issueRepository.save(any(Issue.class))).thenReturn(issue);

        // When
        ResponseEntity<Map<String, Object>> responseEntity = issueService.uploadIssue(issueCreateDto);
        Map<String, Object> response = responseEntity.getBody();

        // Then
        assertNotNull(response);
        assertTrue(response.containsKey("success"), "Response should contain 'success' key");
        assertTrue((Boolean) response.get("success"), "Success value should be true");
        IssueReturnDto issueReturnDto = (IssueReturnDto) response.get("issue");
        assertEquals("Test Issue", issueReturnDto.getTitle());

        verify(issueRepository, times(1)).save(any(Issue.class));
    }


    @Test
    void uploadIssueFail() {
        // Given
        when(accountRepository.findById("new dev")).thenReturn(Optional.empty());

        // When
        ResponseEntity<Map<String, Object>> responseEntity = issueService.uploadIssue(issueCreateDto);
        Map<String, Object> response = responseEntity.getBody();

        // Then
        assertNotNull(response);
        assertEquals("이슈를 생성할 수 없습니다.", response.get("result"));

        verify(issueRepository, never()).save(any(Issue.class));
    }

    @Test
    void assignDevSuccess() {
        // Given
        when(issueRepository.findById(1L)).thenReturn(Optional.of(issue));
        when(accountRepository.findById("new dev")).thenReturn(Optional.of(account));
        account.setRole("dev");

        IssueAssignDevDto request = new IssueAssignDevDto();
        request.setIssueNum(1L);
        request.setDevId("new dev");

        // When
        ResponseEntity<Map<String, Object>> responseEntity = issueService.assignDev(request);
        Map<String, Object> response = responseEntity.getBody();

        // Then
        assertNotNull(response);
        assertTrue((Boolean) response.get("success"), "Response should be successful");
        assertEquals(1, issue.getState(), "Issue state should be updated to 1");

        verify(issueRepository, times(1)).save(issue);
    }


    @Test
    void assignDevFailRole() {
        when(issueRepository.findById(1L)).thenReturn(Optional.of(issue));
        when(accountRepository.findById("new dev")).thenReturn(Optional.of(account));
        account.setRole("dev");

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

//    searchIssueByFilter에 대한 테스트를 작성
    @Test
    void searchIssueByTitle() {
        // Given
        IssueSearchDto issueSearchDto = new IssueSearchDto();
        issueSearchDto.setFilter("title");
        issueSearchDto.setValue("Test Issue");

        when(issueRepository.findAllByTitleContaining("Test Issue")).thenReturn(Collections.singletonList(issue));

        // When
        List<IssueReturnDto> results = issueService.searchIssueByFilter(issueSearchDto);

        // Then
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("Test Issue", results.get(0).getTitle());

        verify(issueRepository, times(1)).findAllByTitleContaining("Test Issue");
    }

    @Test
    void searchIssueByTag() {
        // Given
        IssueSearchDto issueSearchDto = new IssueSearchDto();
        issueSearchDto.setFilter("tag");
        issueSearchDto.setValue("bug");

        when(issueRepository.findAllByTagContaining("bug")).thenReturn(Collections.singletonList(issue));

        // When
        List<IssueReturnDto> results = issueService.searchIssueByFilter(issueSearchDto);

        // Then
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("bug", results.get(0).getTag());

        verify(issueRepository, times(1)).findAllByTagContaining("bug");
    }

    @Test
    void searchIssueByWriter() {
        // Given
        IssueSearchDto issueSearchDto = new IssueSearchDto();
        issueSearchDto.setFilter("writer");
        issueSearchDto.setValue("new dev");

        when(issueRepository.findALLByAccount_Id("new dev")).thenReturn(Collections.singletonList(issue));

        // When
        List<IssueReturnDto> results = issueService.searchIssueByFilter(issueSearchDto);

        // Then
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("new dev", results.get(0).getAccountId());

        verify(issueRepository, times(1)).findALLByAccount_Id("new dev");
    }

//    issueListAll에 대한 테스트를 작성
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

//    assignDevAuto에 대한 테스트를 작성
    @Test
    void assignDevAutoSuccess() {
        // Given
        IssueAssignDevAutoDto dto = new IssueAssignDevAutoDto();
        dto.setIssueNum(1L);

        when(issueRepository.findById(1L)).thenReturn(Optional.of(issue));
        Account dev1 = new Account();
        dev1.setId("dev1");
        dev1.setRole("dev");

        Account dev2 = new Account();
        dev2.setId("dev2");
        dev2.setRole("dev");

        List<Account> developers = Arrays.asList(dev1, dev2);

        when(accountRepository.findAllByRole("dev")).thenReturn(developers);
        when(issueRepository.countByDeveloperAndStateBetween(dev1, 1, 2)).thenReturn(2);
        when(issueRepository.countByDeveloperAndStateBetween(dev2, 1, 2)).thenReturn(1);
        when(issueRepository.countByDeveloperAndStateBetween(dev1, 3, 4)).thenReturn(3);
        when(issueRepository.countByDeveloperAndStateBetween(dev2, 3, 4)).thenReturn(2);

        // When
        ResponseEntity<Map<String, Object>> responseEntity = issueService.assignDevAuto(dto);
        Map<String, Object> response = responseEntity.getBody();

        // Then
        assertNotNull(response);
        assertTrue((Boolean) response.get("success"));
        assertEquals("dev2", issue.getDeveloper().getId());
        assertEquals(1, issue.getState());

        verify(issueRepository, times(1)).save(issue);
    }

}

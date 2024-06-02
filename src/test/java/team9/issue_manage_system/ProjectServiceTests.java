package team9.issue_manage_system;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import team9.issue_manage_system.dto.ProjectCreateDto;
import team9.issue_manage_system.dto.ProjectDeleteDto;
import team9.issue_manage_system.dto.ProjectReturnDto;
import team9.issue_manage_system.entity.Account;
import team9.issue_manage_system.entity.Project;
import team9.issue_manage_system.repository.AccountRepository;
import team9.issue_manage_system.repository.ProjectRepository;
import team9.issue_manage_system.service.IssueService;
import team9.issue_manage_system.service.ProjectService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ProjectServiceTests {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private IssueService issueService;

    @InjectMocks
    private ProjectService projectService;

    private ProjectCreateDto validProjectCreateDto;
    private ProjectDeleteDto validProjectDeleteDto;
    private Account validAccount;
    private Project validProject;

    @BeforeEach
    void setUp() {
        validProjectCreateDto = new ProjectCreateDto();
        validProjectCreateDto.setTitle("Test Project");
        validProjectCreateDto.setPlId("pl_id");

        validProjectDeleteDto = new ProjectDeleteDto();
        validProjectDeleteDto.setAdminId("admin_id");
        validProjectDeleteDto.setProjectNum(1L);

        validAccount = new Account();
        validAccount.setId("pl_id");
        validAccount.setRole("pl");

        validProject = new Project();
        validProject.setProjectNum(1L);
        validProject.setTitle("Test Project");
        validProject.setProjectLeader(validAccount);
    }

    @Test
    void projectCreate_ValidInput_Success() {
        // Given
        validProjectCreateDto.setPlId("pl_id");

        // Ensure the account has the required role
        validAccount.setRole("pl");

        // Stub the behavior of the repository methods
        when(accountRepository.findById(validProjectCreateDto.getPlId())).thenReturn(Optional.of(validAccount));
        when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> {
            Project project = invocation.getArgument(0);
            project.setProjectNum(1L); // Assign an ID to simulate saving
            return project;
        });

        // When
        Optional<ProjectReturnDto> result = projectService.projectCreate(validProjectCreateDto);

        // Then
        assertTrue(result.isPresent());
        assertEquals(validProject.getProjectNum(), result.get().getProjectNum());
        assertEquals(validProject.getTitle(), result.get().getTitle());
        assertEquals(validAccount.getId(), result.get().getPlId());

        // Verify that the save method of projectRepository is called once
        verify(projectRepository, times(1)).save(any(Project.class));
    }





    @Test
    void projectCreate_InvalidAccountRole_ReturnsEmpty() {
        // Given
        validAccount.setRole("invalid_role");
        when(accountRepository.findById(validProjectCreateDto.getPlId())).thenReturn(Optional.of(validAccount));

        // When
        Optional<ProjectReturnDto> result = projectService.projectCreate(validProjectCreateDto);

        // Then
        assertFalse(result.isPresent());
        verify(projectRepository, never()).save(any(Project.class));
    }

    @Test
    void projectCreate_AccountNotFound_ReturnsEmpty() {
        // Given
        when(accountRepository.findById(validProjectCreateDto.getPlId())).thenReturn(Optional.empty());

        // When
        Optional<ProjectReturnDto> result = projectService.projectCreate(validProjectCreateDto);

        // Then
        assertFalse(result.isPresent());
        verify(projectRepository, never()).save(any(Project.class));
    }

    @Test
    void projectDelete_ValidInput_Success() {
        // Given
        validAccount.setRole("admin"); // 관리자로 설정
        when(accountRepository.findById(validProjectDeleteDto.getAdminId())).thenReturn(Optional.of(validAccount));
        when(projectRepository.findById(validProjectDeleteDto.getProjectNum())).thenReturn(Optional.of(validProject));

        // When
        boolean result = projectService.projectDelete(validProjectDeleteDto);

        // Then
        assertTrue(result);
        verify(projectRepository, times(1)).deleteById(validProjectDeleteDto.getProjectNum());
    }

    @Test
    void projectDelete_InvalidAccountRole_ReturnsFalse() {
        // Given
        validAccount.setRole("invalid_role");
        when(accountRepository.findById(validProjectDeleteDto.getAdminId())).thenReturn(Optional.of(validAccount));
        when(projectRepository.findById(validProjectDeleteDto.getProjectNum())).thenReturn(Optional.of(validProject));

        // When
        boolean result = projectService.projectDelete(validProjectDeleteDto);

        // Then
        assertFalse(result);
        verify(projectRepository, never()).deleteById(anyLong());
    }

    @Test
    void projectDelete_ProjectNotFound_ReturnsFalse() {
        // Given
        when(accountRepository.findById(validProjectDeleteDto.getAdminId())).thenReturn(Optional.of(validAccount));
        when(projectRepository.findById(validProjectDeleteDto.getProjectNum())).thenReturn(Optional.empty());

        // When
        boolean result = projectService.projectDelete(validProjectDeleteDto);

        // Then
        assertFalse(result);
        verify(projectRepository, never()).deleteById(anyLong());
    }
}


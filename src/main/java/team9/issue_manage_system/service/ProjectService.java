package team9.issue_manage_system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team9.issue_manage_system.dto.IssueReturnDto;
import team9.issue_manage_system.dto.ProjectCreateDto;
import team9.issue_manage_system.dto.ProjectDeleteDto;
import team9.issue_manage_system.dto.ProjectReturnDto;
import team9.issue_manage_system.entity.Account;
import team9.issue_manage_system.entity.Issue;
import team9.issue_manage_system.entity.Project;
import team9.issue_manage_system.repository.AccountRepository;
import team9.issue_manage_system.repository.IssueRepository;
import team9.issue_manage_system.repository.ProjectRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final AccountRepository accountRepository;
    private final IssueService issueService;

    public void printProject(Project project) {
        System.out.println(project);
    }

    public Optional<ProjectReturnDto> projectCreate(ProjectCreateDto projectCreateDto) {
        Optional<Account> accountOpt = accountRepository.findById(projectCreateDto.getPlId());

        if (accountOpt.isPresent() && accountOpt.get().getRole().equals("pl")) {
            Account account = accountOpt.get();
            Project project = new Project();
            project.setTitle(projectCreateDto.getTitle());
            project.setProjectLeader(account);
            projectRepository.save(project);

            ProjectReturnDto projectReturnDto = makeProjectReturnDto(project);
            return Optional.of(projectReturnDto);
        }
        return Optional.empty();
    }


    public boolean projectDelete(ProjectDeleteDto projectDeleteDto) {
        Optional<Account> accountOpt = accountRepository.findById(projectDeleteDto.getAdminId());
        Optional<Project> projectOpt = projectRepository.findById(projectDeleteDto.getProjectNum());
        System.out.println("check account: " +  accountOpt);

        if (accountOpt.isPresent() && accountOpt.get().getRole().equals("admin") && projectOpt.isPresent()) {
            System.out.println(projectOpt.get());
            projectRepository.deleteById(projectOpt.get().getProjectNum());
            return true;
        }
        return false;
    }

    private ProjectReturnDto makeProjectReturnDto(Project project) {
        ProjectReturnDto projectReturnDto = new ProjectReturnDto();
        projectReturnDto.setProjectNum(project.getProjectNum());
        projectReturnDto.setTitle(project.getTitle());
        projectReturnDto.setPlId(project.getProjectLeader().getId());
        projectReturnDto.setDate(project.getDate());
        List<IssueReturnDto> issueReturnDtos = new ArrayList<>();
        Set<Issue> issues = project.getIssues();
        if (issues == null) {
            issues = new HashSet<>();
        }
        for (Issue issue : issues) {
            IssueReturnDto issueReturnDto = issueService.makeIssueReturnDto(issue);
            issueReturnDtos.add(issueReturnDto);
        }
        projectReturnDto.setIssues(issueReturnDtos);
        return projectReturnDto;
    }
}

package team9.issue_manage_system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import team9.issue_manage_system.dto.ProjectDto;
import team9.issue_manage_system.entity.Account;
import team9.issue_manage_system.entity.Project;
import team9.issue_manage_system.repository.AccountRepository;
import team9.issue_manage_system.repository.ProjectRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final AccountRepository accountRepository;

    public void printProject(Project project) {
        System.out.println(project);
    }

    public Optional<Project> projectCreate(ProjectDto projectDto) {
        Optional<Account> accountOpt = accountRepository.findById(projectDto.getPlId());
        System.out.println("check issue: " +  accountOpt);
        Map<String, Object> response = new HashMap<>();

        if (accountOpt.isPresent() && accountOpt.get().getRole().equals("pl")) {
            Account account = accountOpt.get();
            Project project = new Project();
            project.setTitle(projectDto.getTitle());
            project.setProjectLeader(account);
            projectRepository.save(project);
            return Optional.of(project);
        }
        return Optional.empty();
    }
}

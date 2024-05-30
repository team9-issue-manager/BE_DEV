package team9.issue_manage_system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team9.issue_manage_system.dto.ProjectCreateDto;
import team9.issue_manage_system.dto.ProjectDeleteDto;
import team9.issue_manage_system.entity.Account;
import team9.issue_manage_system.entity.Project;
import team9.issue_manage_system.repository.AccountRepository;
import team9.issue_manage_system.repository.ProjectRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final AccountRepository accountRepository;

    public void printProject(Project project) {
        System.out.println(project);
    }

    public Optional<Project> projectCreate(ProjectCreateDto projectCreateDto) {
        Optional<Account> accountOpt = accountRepository.findById(projectCreateDto.getPlId());
        System.out.println("check account: " +  accountOpt);

        if (accountOpt.isPresent() && accountOpt.get().getRole().equals("pl")) {
            Account account = accountOpt.get();
            Project project = new Project();
            project.setTitle(projectCreateDto.getTitle());
            project.setProjectLeader(account);
            projectRepository.save(project);
            return Optional.of(project);
        }
        return Optional.empty();
    }


    public boolean projectDelete(ProjectDeleteDto projectDeleteDto) {
        Optional<Account> accountOpt = accountRepository.findById(projectDeleteDto.getAdminId());
        System.out.println("check account: " +  accountOpt);

        if (accountOpt.isPresent() && accountOpt.get().getRole().equals("admin")) {
            projectRepository.deleteById(projectDeleteDto.getProjectNum());
            return true;
        }
        return false;
    }
}

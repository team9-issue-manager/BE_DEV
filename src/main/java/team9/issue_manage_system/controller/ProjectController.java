package team9.issue_manage_system.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team9.issue_manage_system.dto.ProjectDto;
import team9.issue_manage_system.entity.Account;
import team9.issue_manage_system.entity.Project;
import team9.issue_manage_system.service.ProjectService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@RestController  //@Controller + @ResponseBody
@RequiredArgsConstructor // final이 선언된 모든 필드를 인자값으로 하는 생성자를 자동(대신) 생성
@RequestMapping("/project")
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> uploadProject(@RequestBody ProjectDto projectDto) {
        Map<String, Object> response = new HashMap<>();
        Optional<Project> project = projectService.projectCreate(projectDto);

        if (project.isPresent()) {
            response.put("success", true);
            response.put("project", project);
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    //@PostMapping("/")
}
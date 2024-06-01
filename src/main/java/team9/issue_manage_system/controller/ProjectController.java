package team9.issue_manage_system.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team9.issue_manage_system.dto.ProjectCreateDto;
import team9.issue_manage_system.dto.ProjectDeleteDto;
import team9.issue_manage_system.dto.ProjectReturnDto;
import team9.issue_manage_system.entity.Project;
import team9.issue_manage_system.service.ProjectService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController  //@Controller + @ResponseBody
@RequiredArgsConstructor // final이 선언된 모든 필드를 인자값으로 하는 생성자를 자동(대신) 생성
@RequestMapping("/project")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> uploadProject(@RequestBody ProjectCreateDto projectCreateDto) {
        Map<String, Object> response = new HashMap<>();
        System.out.println(projectCreateDto);
        Optional<ProjectReturnDto> projectReturnDto = projectService.projectCreate(projectCreateDto);
        if (projectReturnDto.isPresent()) {
            response.put("success", true);
            response.put("project", projectReturnDto);
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> listProject() {
        Map<String, Object> response = new HashMap<>();
        List<ProjectReturnDto> projectReturnDtos = projectService.projectList();
        response.put("projects", projectReturnDtos);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteProject(@RequestBody ProjectDeleteDto projectDeleteDto) {
        Map<String, Object> response = new HashMap<>();
        boolean checkProject = projectService.projectDelete(projectDeleteDto);

        if (checkProject) {
            response.put("success", true);
            return ResponseEntity.ok(response);
        }
        response.put("success", false);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
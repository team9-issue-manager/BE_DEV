package team9.issue_manage_system.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team9.issue_manage_system.dto.ProjectDto;
import team9.issue_manage_system.service.ProjectService;

import java.util.Map;


@RestController  //@Controller + @ResponseBody
@RequiredArgsConstructor // final이 선언된 모든 필드를 인자값으로 하는 생성자를 자동(대신) 생성
@RequestMapping("/project")
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> uploadProject(@RequestBody ProjectDto projectDto) {
        return projectService.projectCreate(projectDto);
    }
}
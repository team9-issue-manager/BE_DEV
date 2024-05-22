package team9.issue_manage_system.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import team9.issue_manage_system.entity.Issue;
import team9.issue_manage_system.repository.IssueRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class IssueController {
    private final IssueRepository issueRepository;
    @GetMapping("/searchIssue")
    public List<Issue> searchIssueByTitle(@RequestBody Issue.IssueSearchRequest issueSearchRequest) {
        String title = issueSearchRequest.getTitle();
        if (title == null) {
            throw new IllegalArgumentException("Title cannot be null");
        }
        return issueRepository.findByTitleContaining(title);
    }


    @PostMapping("/issueAdd")
    public void uploadIssue(@RequestBody Issue issue){
        issueRepository.save(issue);
    }
}

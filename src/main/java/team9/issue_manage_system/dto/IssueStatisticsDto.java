package team9.issue_manage_system.dto;

import lombok.Data;

import java.util.Map;

@Data
public class IssueStatisticsDto {
    private Long totalIssues;
    private Map<String, Long> issuesByStatus;
    private Map<String, Long> issuesByDeveloper;

    // 생성자, getter, setter
    public IssueStatisticsDto() {
    }

    public IssueStatisticsDto(Long totalIssues, Map<String, Long> issuesByStatus, Map<String, Long> issuesByDeveloper) {
        this.totalIssues = totalIssues;
        this.issuesByStatus = issuesByStatus;
        this.issuesByDeveloper = issuesByDeveloper;

    }

}

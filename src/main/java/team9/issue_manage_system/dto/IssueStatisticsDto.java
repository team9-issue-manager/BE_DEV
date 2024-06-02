package team9.issue_manage_system.dto;

import lombok.Data;

import java.util.Map;

@Data
public class IssueStatisticsDto {
    private Long totalIssues;
    private Map<String, Long> issuesByStatus;
    private Map<String, Long> issuesByDeveloper;
    private Map<String, Long> issuesByDay;
    private Map<String, Long> issuesByMonth;

    // Constructor
    public IssueStatisticsDto(Long totalIssues, Map<String, Long> issuesByStatus, Map<String, Long> issuesByDeveloper, Map<String, Long> issuesByDay, Map<String, Long> issuesByMonth) {
        this.totalIssues = totalIssues;
        this.issuesByStatus = issuesByStatus;
        this.issuesByDeveloper = issuesByDeveloper;
        this.issuesByDay = issuesByDay;
        this.issuesByMonth = issuesByMonth;
    }

    // Getters and Setters
    public Long getTotalIssues() {
        return totalIssues;
    }

    public void setTotalIssues(Long totalIssues) {
        this.totalIssues = totalIssues;
    }

    public Map<String, Long> getIssuesByStatus() {
        return issuesByStatus;
    }

    public void setIssuesByStatus(Map<String, Long> issuesByStatus) {
        this.issuesByStatus = issuesByStatus;
    }

    public Map<String, Long> getIssuesByDeveloper() {
        return issuesByDeveloper;
    }

    public void setIssuesByDeveloper(Map<String, Long> issuesByDeveloper) {
        this.issuesByDeveloper = issuesByDeveloper;
    }

    public Map<String, Long> getIssuesByDay() {
        return issuesByDay;
    }

    public void setIssuesByDay(Map<String, Long> issuesByDay) {
        this.issuesByDay = issuesByDay;
    }

    public Map<String, Long> getIssuesByMonth() {
        return issuesByMonth;
    }

    public void setIssuesByMonth(Map<String, Long> issuesByMonth) {
        this.issuesByMonth = issuesByMonth;
    }

    @Override
    public String toString() {
        return "IssueStatisticsDto{" +
                "totalIssues=" + totalIssues +
                ", issuesByStatus=" + issuesByStatus +
                ", issuesByDeveloper=" + issuesByDeveloper +
                ", issuesByDay=" + issuesByDay +
                ", issuesByMonth=" + issuesByMonth +
                '}';
    }
}

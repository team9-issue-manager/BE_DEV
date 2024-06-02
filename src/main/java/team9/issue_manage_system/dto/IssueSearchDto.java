package team9.issue_manage_system.dto;

import lombok.Data;

@Data
public class IssueSearchDto {
    private String filter; //title, writer, state, devId, priority
    private String value;
}

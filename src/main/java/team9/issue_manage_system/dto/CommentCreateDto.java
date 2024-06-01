package team9.issue_manage_system.dto;

import lombok.Data;

@Data
public class CommentCreateDto {
    private Long issueNum;
    private String content;
    private String accountId;
}

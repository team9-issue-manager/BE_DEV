package team9.issue_manage_system.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class CommentReturnDto {
    private Long issueNum;
    private Long commentNum;
    private String content;
    private String accountId;
    private Date date;
}

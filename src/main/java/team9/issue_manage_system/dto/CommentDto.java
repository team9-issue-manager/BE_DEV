package team9.issue_manage_system.dto;

import lombok.Data;
import team9.issue_manage_system.entity.Comment;

import java.util.Date;

@Data
public class CommentDto {
    private Long issueNum;
    private String content;
    private Date date;
    private Long id;
}

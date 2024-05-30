package team9.issue_manage_system.dto;


import lombok.Data;

import java.util.Date;

@Data
public class IssueReturnDto {
    private Long issueNum;
    private String title;
    private String content;
    private Date date;
    private Integer state;
    private String accountId;
    private Long projectNum;
    private String tag;

}

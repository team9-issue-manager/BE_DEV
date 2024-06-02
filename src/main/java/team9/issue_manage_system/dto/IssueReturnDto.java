package team9.issue_manage_system.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class IssueReturnDto {
    private Long issueNum;
    private Integer priority;
    private String title;
    private String content;
    private Date date;
    private Integer state;
    private String accountId;

    @JsonProperty(defaultValue = "")
    private String devId;

    private Long projectNum;
    private Integer priority;
    private String tag;

}

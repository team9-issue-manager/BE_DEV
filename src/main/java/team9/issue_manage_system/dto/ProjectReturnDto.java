package team9.issue_manage_system.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ProjectReturnDto {
    private Long projectNum;
    private String title;
    private String plId;
    private Date date;

    @JsonProperty(defaultValue = "[]") // JSON 역직렬화 시 빈 리스트로 초기화
    private List<IssueReturnDto> issues = new ArrayList<>(); // 빈 리스트로 초기화
}

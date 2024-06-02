package team9.issue_manage_system.dto;

import lombok.Data;

@Data // @Getter @Setter @ToString @EqualsAndHashCode @RequiredArgsConstructor 를 포함함.
public class IssueCreateDto {

    private String title;
    private String content;
    private String accountId;
    private Long projectNum;
    private Integer priority;
    private String tag;
}
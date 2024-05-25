package team9.issue_manage_system.dto;

import lombok.Data;
import team9.issue_manage_system.entity.Issue;

import java.util.Set;

@Data // @Getter @Setter @ToString @EqualsAndHashCode @RequiredArgsConstructor 를 포함함.
public class IssueDto {

    private String title;
    private String content;
    private String accountId;
    private Set<Issue.Tag> tags;

}
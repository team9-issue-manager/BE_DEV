package team9.issue_manage_system.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AdminAuthDto {
    private Long requestNum;
    private AccountReturnDto requestAccount;
}

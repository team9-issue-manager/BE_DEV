package team9.issue_manage_system;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginForm {
    public LoginForm() {};
    private String userID;
    private String userPW;
}

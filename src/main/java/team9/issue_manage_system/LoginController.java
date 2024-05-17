package team9.issue_manage_system;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @PostMapping("/login")
    public String login(@RequestBody LoginForm loginForm) {
        // 받은 JSON 데이터를 LoginForm 객체로 자동으로 변환하여 사용합니다.
        String username = loginForm.getUserID();
        String password = loginForm.getUserPW();

        // 받은 데이터를 로그에 출력합니다.
        System.out.println("Received username: " + username);
        System.out.println("Received password: " + password);

        // 로그인 성공 메시지를 반환합니다.
        return "Login successful for user: " + username;
    }
}

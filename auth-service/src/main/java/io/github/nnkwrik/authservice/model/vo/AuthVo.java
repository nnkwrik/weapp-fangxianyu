package io.github.nnkwrik.authservice.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author nnkwrik
 * @date 18/11/19 14:28
 */
@Data
@NoArgsConstructor
public class AuthVo {
    private String token;
    private UserInfo userInfo;

    public AuthVo(String token, String nickname, String avatar) {
        this.token = token;
        this.userInfo = new UserInfo(nickname, avatar);
    }


}
@Data
@AllArgsConstructor
class UserInfo {
    private String nickname;
    private String avatar;
}
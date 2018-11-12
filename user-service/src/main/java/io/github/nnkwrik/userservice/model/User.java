package io.github.nnkwrik.userservice.model;

import lombok.Data;
import org.apache.ibatis.annotations.Param;

import javax.validation.constraints.NotNull;

/**
 * @author nnkwrik
 * @date 18/11/11 15:01
 */
@Data
public class User {
    private Integer id;
    private String openId;
    @NotNull
    private String nickName;
    @NotNull
    private String avatarUrl;
    @NotNull
    private Integer gender;
    private String language;
    private String city;
    private String province;
    private String country;
    //address
    //phone
}

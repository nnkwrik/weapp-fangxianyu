package io.github.nnkwrik.userservice.model;

import lombok.Data;

import java.util.Date;

/**
 * @author nnkwrik
 * @date 18/11/11 15:01
 */
@Data
public class User {
    private Integer id;
    private String openId;
    private String nickName;
    private String avatarUrl;
    private Integer gender;
    private String language;
    private String city;
    private String province;
    private String country;
    private Date registerTime;
    //address
    //phone
}

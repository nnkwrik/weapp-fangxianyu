package io.github.nnkwrik.userservice.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.WxMaUserService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import io.github.nnkwrik.userservice.config.WxMaConfiguration;
import io.github.nnkwrik.userservice.entity.User;
import io.github.nnkwrik.userservice.service.UserService;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author nnkwrik
 * @date 18/11/10 21:51
 */
@RestController
@RequestMapping("/wx/user")
public class UserController {

//    @Autowired
//    private UserService userService;


    @PostMapping("/login/{code}")
    public String login(@PathVariable String code, @RequestBody User user) {

        WxMaUserService wxUserService = WxMaConfiguration.getMaServices().getUserService();
        try {
            WxMaJscode2SessionResult sessionInfo = wxUserService.getSessionInfo(code);

            String openid = sessionInfo.getOpenid();
            user.setOpenId(openid);

            System.out.println(user);
            return "ok";
        } catch (WxErrorException e) {
            e.printStackTrace();
            return e.toString();
        }
    }

}

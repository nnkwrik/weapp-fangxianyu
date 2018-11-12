package io.github.nnkwrik.authservice.controller;

import cn.binarywang.wx.miniapp.api.WxMaUserService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.nnkwrik.authservice.config.WxMaConfiguration;
import io.github.nnkwrik.common.dto.AuthDTO;
import io.github.nnkwrik.authservice.util.TokenFactory;
import io.github.nnkwrik.common.dto.JWTUser;
import io.github.nnkwrik.common.dto.Response;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @author nnkwrik
 * @date 18/11/12 15:32
 */
@RestController
public class AuthController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());


    @GetMapping("/test")
    public String test() {
        return "test is success";
    }

    @PostMapping("/login")
    public Response login(@RequestBody AuthDTO authDTO) {
        log.info("用户登录 ： {}", authDTO);
        WxMaUserService wxUserService = WxMaConfiguration.getMaServices().getUserService();

        //验证用户登录凭证
        WxMaJscode2SessionResult sessionInfo = null;
        try {
            sessionInfo = wxUserService.getSessionInfo(authDTO.getJsCode());
        } catch (WxErrorException e) {
            return Response.fail("凭证失效，登录失败");
        }

        //验证用户给出的userInfo是否正确
        String sessionKey = sessionInfo.getSessionKey();
        String openId = sessionInfo.getOpenid();
        if (!wxUserService.checkUserInfo(sessionKey, authDTO.getRawData(), authDTO.getSignature())) {
            String message = "userInfo 和 session 中的不一致";
            log.info(message);
            return Response.fail(message);
        }

        //构造JWT token
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> jsonMap = null;
        try {
            jsonMap = mapper.readValue(authDTO.getRawData(), new TypeReference<Map<String, String>>() {
            });
        } catch (IOException e) {
            String message = "Json转Map失败";
            log.info(message);
            e.printStackTrace();
            return Response.fail(message);
        }

        JWTUser jwtUser = new JWTUser();
        jwtUser.setOpenId(openId);
        jwtUser.setNickName(jsonMap.get("nickName"));
        jwtUser.setAvatarUrl(jsonMap.get("avatarUrl"));

        String token = TokenFactory.createTokenFromUser(jwtUser);
        return Response.ok(token);
    }
}

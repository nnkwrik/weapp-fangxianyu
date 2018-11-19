package io.github.nnkwrik.authservice.controller;

import cn.binarywang.wx.miniapp.api.WxMaUserService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.nnkwrik.authservice.config.WxMaConfiguration;
import io.github.nnkwrik.authservice.dto.AuthDTO;
import io.github.nnkwrik.authservice.dto.DetailAuthDTO;
import io.github.nnkwrik.authservice.model.vo.AuthVo;
import io.github.nnkwrik.authservice.mq.RegisterStreamSender;
import io.github.nnkwrik.authservice.token.TokenCreator;
import io.github.nnkwrik.common.dto.JWTUser;
import io.github.nnkwrik.common.dto.Response;
import io.github.nnkwrik.common.mq.UserRegisterStream;
import io.github.nnkwrik.common.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

/**
 * @author nnkwrik
 * @date 18/11/12 15:32
 */
@Slf4j
@RestController
@EnableBinding(UserRegisterStream.class)
public class AuthController {


    @Autowired
    private TokenCreator creator;


    @Autowired
    private RegisterStreamSender registerSender;

//    @GetMapping("/test/{message}")
//    public String test(@PathVariable String message) {
//        registerSender.send(message);
//        return "test is success";
//    }


    @PostMapping("/auth/loginByWeixin")
    public Response loginByWeixin(@RequestBody AuthDTO authDTO) throws IOException {

        log.info("用户登录 ： {}", authDTO);
        WxMaUserService wxUserService = WxMaConfiguration.getMaServices().getUserService();

        //验证用户登录凭证
        WxMaJscode2SessionResult sessionInfo = null;
        try {
            sessionInfo = wxUserService.getSessionInfo(authDTO.getCode());
        } catch (WxErrorException e) {
            String message = "jscode失效，登录失败";
            log.info(message);
            return Response.fail(Response.WRONG_JS_CODE, message);
        }

        //验证用户给出的userInfo是否正确
        String sessionKey = sessionInfo.getSessionKey();
        String openId = sessionInfo.getOpenid();
        DetailAuthDTO detail = authDTO.getDetail();
        if (!wxUserService.checkUserInfo(sessionKey, detail.getRawData(), detail.getSignature())) {
            String message = "userInfo 和 session 中的不一致";
            log.info(message);
            return Response.fail(Response.CHECK_USER_WITH_SESSION_FAIL, message);
        }

        String userData = setOpenId4Data(detail.getRawData(), openId);
        if (authDTO.getFirstLogin()) {
            //异步调用user-service注册到数据库
            registerSender.send(userData);
        }

        //构造JWT token
        AuthVo vo = createToken(userData);

        log.info("认证成功,用户信息 ： {}", vo);
        return Response.ok(vo);
    }

    /**
     * 在原有的rawData中附上openId
     *
     * @param rawData
     * @param openId
     * @return
     * @throws IOException
     */
    private String setOpenId4Data(String rawData, String openId) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = (ObjectNode) mapper.readTree(rawData);
        node.remove("openId");
        node.put("openId", openId);
        return node.toString();
    }

    /**
     * 通过json用户信息构造vo
     * @param userData
     * @return
     */
    private AuthVo createToken(String userData) {
        Map<String, String> rawData =
                JsonUtil.fromJson(userData, new TypeReference<Map<String, String>>() {
                });
        String openId = rawData.get("openId");
        String nickname = rawData.get("nickName");
        String avatar = rawData.get("avatarUrl");
        String token = creator.create(openId, nickname, avatar);
        return new AuthVo(token, nickname, avatar);
    }

}

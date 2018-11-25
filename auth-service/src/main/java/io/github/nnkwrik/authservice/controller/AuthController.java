package io.github.nnkwrik.authservice.controller;

import cn.binarywang.wx.miniapp.api.WxMaUserService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.nnkwrik.authservice.config.WxMaConfiguration;
import io.github.nnkwrik.authservice.dto.AuthDTO;
import io.github.nnkwrik.authservice.dto.DetailAuthDTO;
import io.github.nnkwrik.authservice.model.vo.AuthVo;
import io.github.nnkwrik.authservice.mq.RegisterStreamSender;
import io.github.nnkwrik.authservice.service.AuthService;
import io.github.nnkwrik.common.dto.Response;
import io.github.nnkwrik.common.mq.UserRegisterStream;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author nnkwrik
 * @date 18/11/12 15:32
 */
@Slf4j
@RestController
@EnableBinding(UserRegisterStream.class)
@RequestMapping("/auth")
public class AuthController {


    @Autowired
    private AuthService authService;


    @Autowired
    private RegisterStreamSender registerSender;


    @PostMapping("/loginByWeixin")
    public Response loginByWeixin(@RequestBody AuthDTO authDTO) {

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

        String userData = authService.setOpenId4Data(detail.getRawData(), openId);
        //异步调用user-service注册到数据库
        registerSender.send(userData);

        //构造JWT token
        AuthVo vo = authService.createToken(userData);

        log.info("认证成功,用户信息 ： {}", vo);
        return Response.ok(vo);
    }

    /**
     * 测试环境专用
     *
     * @param
     * @return
     */
    @PostMapping("/loginByWeixinDev")
    public Response loginByWeixinDev(@RequestBody Map<String, String> jsonMap, @RequestHeader("Authorization") String token) throws JsonProcessingException {
        log.info("用户登录 ： {}", "测试");
//        JsonUtil.fromJson()
        ObjectMapper mapper = new ObjectMapper();
        String userData = mapper.writeValueAsString(jsonMap);

        //异步调用user-service注册到数据库
        registerSender.send(userData);

        AuthVo vo = authService.createToken(userData);
        return Response.ok(vo);

    }


}

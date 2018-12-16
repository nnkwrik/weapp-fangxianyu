package io.github.nnkwrik.authservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.nnkwrik.authservice.model.vo.AuthVo;
import io.github.nnkwrik.authservice.service.AuthService;
import io.github.nnkwrik.authservice.token.TokenCreator;
import io.github.nnkwrik.common.dto.JWTUser;
import io.github.nnkwrik.common.token.TokenSolver;
import io.github.nnkwrik.common.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

/**
 * @author nnkwrik
 * @date 18/11/24 14:48
 */
@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Autowired
    private TokenCreator tokenCreator;

    @Autowired
    private TokenSolver tokenSolver;


    /**
     * 在原有的rawData中附上openId
     *
     * @param rawData
     * @param openId
     * @return
     * @throws IOException
     */
    @Override
    public String setOpenId4Data(String rawData, String openId) {

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = null;
        try {
            node = (ObjectNode) mapper.readTree(rawData);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        node.remove("openId");
        node.put("openId", openId);
        return node.toString();
    }

    /**
     * 通过json用户信息构造vo
     *
     * @param userData
     * @return
     */
    @Override
    public AuthVo createToken(String userData) {
        Map<String, String> rawData =
                JsonUtil.fromJson(userData, JsonUtil.simpleJsonMap);
        String openId = rawData.get("openId");
        String nickname = rawData.get("nickName");
        String avatar = rawData.get("avatarUrl");
        JWTUser jwtUser = new JWTUser(openId, nickname, avatar);
        String token = null;
        try {
            token = tokenCreator.create(jwtUser);
        } catch (IllegalAccessException e) {
            log.info("token构造失败");
            e.printStackTrace();
        }
        return new AuthVo(token, jwtUser);
    }

}

package io.github.nnkwrik.userservice.service;

import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;

/**
 * @author nnkwrik
 * @date 18/11/10 22:15
 */
public interface UserService {
    void register(WxMaUserInfo userInfo);
}

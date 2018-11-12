package io.github.nnkwrik.authservice.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.WxMaInMemoryConfig;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@Configuration
@EnableConfigurationProperties(WxMaProperties.class)
public class WxMaConfiguration {

    private WxMaProperties properties;
    private static WxMaService maServices;


    @Autowired
    public WxMaConfiguration(WxMaProperties properties) {
        this.properties = properties;
    }


    public static WxMaService getMaServices() {
        return maServices;
    }

    @Bean
    public Object services() {
        WxMaInMemoryConfig config = new WxMaInMemoryConfig();
        BeanUtils.copyProperties(properties,config);
        maServices = new WxMaServiceImpl();
        maServices.setWxMaConfig(config);

        return Boolean.TRUE;
    }


}

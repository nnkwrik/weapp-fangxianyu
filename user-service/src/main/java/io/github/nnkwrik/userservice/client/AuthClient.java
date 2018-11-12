package io.github.nnkwrik.userservice.client;

import io.github.nnkwrik.common.dto.AuthDTO;
import io.github.nnkwrik.userservice.util.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author nnkwrik
 * @date 18/11/12 17:44
 */
@FeignClient(name = "auth-service")
public interface AuthClient {

    @GetMapping("/test")
    String test();

    @PostMapping("/login")
    Response login(@RequestBody AuthDTO authDTO);
}

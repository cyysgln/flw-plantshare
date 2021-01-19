package com.flower.component.jwt.controller;

import com.flower.component.jwt.enums.JwtResultEnums;
import com.flower.component.jwt.utlis.JwtUtil;
import com.flower.common.enums.StatusEnum;
import com.flower.common.exception.ResultException;
import com.flower.common.utils.EncryptUtil;
import com.flower.common.utils.ResultVoUtil;
import com.flower.common.vo.ResultVo;
import com.flower.component.jwt.annotation.IgnorePermissions;
import com.flower.component.jwt.config.properties.JwtProjectProperties;
import com.flower.modules.system.domain.User;
import com.flower.modules.system.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 默认登录验证控制器
 * 说明：默认采用系统用户进行登录验证
 *
 * @author 小懒虫
 * @date 2019/4/9
 */
@RestController
@Api(tags = "登录接口")
public class AuthController {

    @Autowired
    private JwtProjectProperties properties;

    @Autowired
    private UserService userService;

    @IgnorePermissions
    @PostMapping("/api/auth")
    @ApiOperation(value = "jwt登录")
    public ResultVo auth(
            @ApiParam(value = "用户名", required = true) String username,
            @ApiParam(value = "密码", required = true) String password) {
        // 根据用户名获取系统用户数据
        User user = userService.getByName(username);
        if (user == null) {
            throw new ResultException(JwtResultEnums.AUTH_REQUEST_ERROR);
        } else if (user.getStatus().equals(StatusEnum.FREEZED.getCode())) {
            throw new ResultException(JwtResultEnums.AUTH_REQUEST_LOCKED);
        } else {
            // 对明文密码加密处理
            String encrypt = EncryptUtil.encrypt(password, user.getSalt());
            // 判断密码是否正确
            if (encrypt.equals(user.getPassword())) {
                String token = JwtUtil.getToken(username, properties.getSecret(), properties.getExpired());
                return ResultVoUtil.success("登录成功", token);
            } else {
                throw new ResultException(JwtResultEnums.AUTH_REQUEST_ERROR);
            }
        }
    }
}

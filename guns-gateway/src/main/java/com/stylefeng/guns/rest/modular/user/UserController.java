package com.stylefeng.guns.rest.modular.user;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.api.user.UserAPI;
import com.stylefeng.guns.api.user.UserModel;
import com.stylefeng.guns.rest.modular.vo.ResponseVO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/")
public class UserController {
    @Reference(interfaceClass = UserAPI.class)
    private UserAPI userAPI;

    @RequestMapping("register")
    public ResponseVO register(UserModel userModel){
        if (userModel.getUsername() == null && userModel.getUsername().trim().length() == 0) {
            return ResponseVO.serviceFail("用户名不能为空");
        }
        if (userModel.getPassword() == null || userModel.getPassword().trim().length() == 0) {
            return ResponseVO.serviceFail("密码不能为空");
        }
        boolean isSuccess = userAPI.register(userModel);
        if (isSuccess) {
            return ResponseVO.sussess("注册成功");
        } else {
            return ResponseVO.serviceFail("注册失败");
        }
    }
}

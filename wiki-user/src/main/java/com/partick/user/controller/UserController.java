package com.partick.user.controller;


import com.partick.common.req.UserLoginReq;
import com.partick.common.req.UserQueryReq;
import com.partick.common.req.UserResetPasswordReq;
import com.partick.common.req.UserSaveReq;
import com.partick.common.resp.CommonResp;
import com.partick.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/list")
    public CommonResp list(@Valid UserQueryReq req){
        return new CommonResp(true, "", userService.selectByExample(req));
    }

    @PostMapping("/save")
    public CommonResp save(@RequestBody @Valid UserSaveReq req){
        userService.save(req);
        return new CommonResp(true, "");
    }

    @PostMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id){
        userService.delete(id);
        return new CommonResp(true, "");
    }

    @PostMapping("/reset-password")
    public CommonResp resetPassword(@RequestBody @Valid UserResetPasswordReq req){
        userService.resetPassword(req);
        return new CommonResp(true, "");
    }

    @PostMapping("/login")
    public CommonResp login(@RequestBody @Valid UserLoginReq req){
        return new CommonResp(true, "",userService.login(req));
    }

    @GetMapping("/logout/{token}")
    public CommonResp logout(@PathVariable String token){
        userService.logout(token);
        return new CommonResp(true, "");
    }
}

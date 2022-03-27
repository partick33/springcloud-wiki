package com.partick.user.service;


import com.partick.common.req.UserLoginReq;
import com.partick.common.req.UserQueryReq;
import com.partick.common.req.UserResetPasswordReq;
import com.partick.common.req.UserSaveReq;
import com.partick.common.resp.PageResp;
import com.partick.common.resp.UserLoginResp;
import com.partick.common.resp.UserQueryResp;

public interface UserService {
    //查询书本列表
    PageResp<UserQueryResp> selectByExample(UserQueryReq req);

    //保存书本
    public void save(UserSaveReq req);

    //删除书本
    public void delete(Long id);

    void resetPassword(UserResetPasswordReq req);

    UserLoginResp login(UserLoginReq req);

    void logout(String token);
}

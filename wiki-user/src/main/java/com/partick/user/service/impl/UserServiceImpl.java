package com.partick.user.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.partick.common.exception.BusinessException;
import com.partick.common.exception.BusinessExceptionCode;
import com.partick.common.req.UserLoginReq;
import com.partick.common.req.UserQueryReq;
import com.partick.common.req.UserResetPasswordReq;
import com.partick.common.req.UserSaveReq;
import com.partick.common.resp.PageResp;
import com.partick.common.resp.UserLoginResp;
import com.partick.common.resp.UserQueryResp;
import com.partick.common.utils.CopyUtil;
import com.partick.common.utils.SnowFlake;
import com.partick.database.entity.User;
import com.partick.database.entity.UserExample;
import com.partick.database.mapper.UserMapper;
import com.partick.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    UserMapper userMapper;

    @Resource
    SnowFlake snowFlake;

    @Resource
    RedisTemplate redisTemplate;

    @Override
    public PageResp<UserQueryResp> selectByExample(UserQueryReq req) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getLoginName())) {
            criteria.andLoginNameEqualTo(req.getLoginName());
        }
        PageHelper.startPage(req.getPage(), req.getSize());
        List<User> userList = userMapper.selectByExample(userExample);
        List<UserQueryResp> userQueryRespList = CopyUtil.copy(userList, UserQueryResp.class);

        PageInfo<User> pageInfo = new PageInfo<>(userList);
        PageResp<UserQueryResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(userQueryRespList);
        return pageResp;
    }

    @Override
    public void save(UserSaveReq req) {
        User user = CopyUtil.copy(req, User.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            if (ObjectUtils.isEmpty(selectByLoginName(req.getLoginName()))) {
                //??????
                user.setId(snowFlake.nextId());
                //??????Spring?????????DigestUtils??????md5??????
                user.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
                userMapper.insert(user);
            } else {
                //??????????????????
                throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
            }
        } else {
            //??????
            user.setLoginName(null);
            user.setPassword(null);
            userMapper.updateByPrimaryKeySelective(user);
        }
    }

    @Override
    public void delete(Long id) {
        userMapper.deleteByPrimaryKey(id);
    }

    public User selectByLoginName(String username) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andLoginNameEqualTo(username);
        List<User> users = userMapper.selectByExample(userExample);
        if (CollectionUtils.isEmpty(users)) {
            return null;
        } else {
            return users.get(0);
        }
    }

    @Override
    public void resetPassword(UserResetPasswordReq req) {
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        User user = CopyUtil.copy(req, User.class);
        userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public UserLoginResp login(UserLoginReq req) {
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        User user = selectByLoginName(req.getLoginName());
        if (ObjectUtils.isEmpty(user)) {
            //??????????????????
            LOG.info("??????????????????,{}", req.getLoginName());
            throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
        } else {
            if (user.getPassword().equals(req.getPassword())) {
                //????????????
                UserLoginResp resp = CopyUtil.copy(user, UserLoginResp.class);
                Long token = snowFlake.nextId();
                LOG.info("??????token?????????redis???");
                resp.setToken(token.toString());
                redisTemplate.opsForValue().set(token.toString(), JSONObject.toJSONString(resp),3600*24, TimeUnit.SECONDS);
                return resp;
            } else {
                //???????????????
                LOG.info("??????????????????????????????{},??????????????????{}", req.getPassword(), user.getPassword());
                throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
            }
        }
    }

    @Override
    public void logout(String token) {
        LOG.info("???redis?????????token???{}",token);
        redisTemplate.delete(token);
    }
}

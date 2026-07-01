package com.example.picturebackend.service;

import cn.hutool.http.server.HttpServerRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.picturebackend.model.dto.user.UserQueryRequest;
import com.example.picturebackend.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.picturebackend.model.vo.LoginUserVO;
import com.example.picturebackend.model.vo.UserVO;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author Dell
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2026-06-08 15:15:52
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @return
     */
    long userRegister(String userAccount,String userPassword,String checkPassword);

    /**
     * 密码加密
     * @param userPassword
     * @return
     */
    String getEncryptPassword(String userPassword);

    /**
     * 用户登录
     * @param userAccount
     * @param userPassword
     * @return
     */
    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 获得脱敏后的用户登录信息
     * @param user
     * @return
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 用户登录
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 用户登出
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);
    /**
     * 获取脱敏后的用户
     * @param user
     * @return
     */
    UserVO getUserVO(User user);

    /**
     * 获取脱敏后的用户列表
     * @param userList
     * @return
     */
    List<UserVO> getUserVOList(List<User> userList);

    /**
     * 将查询请求转为QueryWrapper对象
     * @param userQueryRequest
     * @return
     */
    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);

    /**
     * 判断是否是管理员
     * @param user
     * @return
     */
    boolean isAdmin(User user);
}

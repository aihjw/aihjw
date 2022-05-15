package com.hjw.service.user;

import com.hjw.pojo.Talk;
import com.hjw.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.sql.Connection;
import java.util.List;

public interface UserService {

    //注册用户
    public User getLoginUser( String userCode, String userPassword);
    //更改密码
    public boolean updatePwd( int id,  String password);
    //得到用户的数量
    public int getUserCount( String username, int userRole);
    //获得分页用户列表
    public List<User> getUserList( String userName, int userRole);
    //用户管理模块中的 子模块—— 添加用户
    public  boolean addUser(User user);
    //用户管理模块中的子模块 —— 删除用户
    public  boolean deleteUser( int id);
    //根据用户id 查询用户信息
    public  User findById(String id);
    //用户管理模块中的子模块 —— 更改用户信息
    public boolean modify(User user);
    //搜索Code是否存在
    public User UserExist(String userCode);
    public List<User> getAllUser();


    public List<Talk> getTalkList( String talkName, String talkTitle);
    public boolean addTalk(Talk talk);
    public Talk getTalkById(String id);
    public  boolean deleteTalk( int id);
    public  boolean updateTalk(Talk talk);
}


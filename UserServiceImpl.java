package com.hjw.service.user;

import com.hjw.dao.user.UserDao;
import com.hjw.pojo.Talk;
import com.hjw.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(propagation= Propagation.REQUIRED,rollbackForClassName="Exception")
@Service
public class UserServiceImpl implements UserService{
        @Autowired
       private UserDao userDao;
    //查用户
    @Override
    public User getLoginUser( String userCode, String userPassword){
        System.out.println(userCode+"----------------"+userPassword);
        return userDao.getLoginUser(userCode,userPassword);
    }


    //更改密码
    @Override
    public boolean updatePwd( int id,  String password) {
        return userDao.updatePwd(id,password)>=1?true:false;
    }

    //得到用户的数量
    @Override
    public int getUserCount( String username, int userRole) {
        return  userDao.getUserCount(username,userRole);
    }


    //获得分页用户列表
    @Override
    public List<User> getUserList( String userName, int userRole) {
        System.out.println("userlist :"+userDao.getUserList(userName,userRole));
        return userDao.getUserList(userName,userRole);
    }

    //用户管理模块中的 子模块—— 添加用户
    @Override
    public  boolean addUser(User user) {
        return userDao.addUser(user)>=1?true:false;
    }


    //用户管理模块中的子模块 —— 删除用户
    @Override
    public  boolean deleteUser( int id) {
        return userDao.deleteUser(id)>=1?true:false;
    }


    //根据用户id 查询用户信息
    @Override
    public  User findById(String id) {
        System.out.println(userDao.findById(id));
        return userDao.findById(id);
    }
    public List<User> getAllUser(){
        return userDao.getAllUser();
    }

    //用户管理模块中的子模块 —— 更改用户信息
    @Override
    public boolean modify(User user){
        return userDao.modify(user)>=1?true:false;
    }


    //搜索Code是否存在
    public User UserExist(String userCode) {
        return userDao.UserExist(userCode);
    }


    public List<Talk> getTalkList( String talkName, String talkTitle){
        return userDao.getTalkList(talkName,talkTitle);
    }

    public boolean addTalk(Talk talk){
        return  userDao.addTalk(talk)>=1?true:false;
    }
    public Talk getTalkById(String id){
        return userDao.getTalkById(id);
    }
    public  boolean deleteTalk( int id){
        return userDao.deleteTalk(id)>=1?true:false;
    }
    public  boolean updateTalk(Talk talk){return  userDao.updateTalk(talk)>=1?true:false;}













}

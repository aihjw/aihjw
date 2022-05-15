package com.hjw.servlet.user;

import com.alibaba.fastjson.JSONArray;
import com.hjw.pojo.*;
import com.hjw.service.bill.BillServiceImpl;
import com.hjw.service.provider.ProviderserviceImpl;
import com.hjw.service.role.RoleServiceImpl;
import com.hjw.service.user.UserServiceImpl;
import com.hjw.utils.Constants;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Controller
public class UserServlet{
    @Autowired
    BillServiceImpl billService;
    @Autowired
    ProviderserviceImpl providerService;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    RoleServiceImpl roleService;
    @RequestMapping(value="/user.do",method = {RequestMethod.POST,RequestMethod.GET})   //匹配user.do请求 获得前端参数method进行对应
    public String doPost(HttpSession session,User user, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response, @RequestParam(value="method",required = false) String method, Model model)
            throws ServletException, IOException {
        System.out.println("--------------->"+method);
        if(method != null && method.equals("add")){   //添加用户方法
            return this.add(request, response,user,bindingResult,session);
        }else if(method != null && method.equals("query")){    //查询用户方法
            return this.query(request, response);
        }else if(method != null && method.equals("getrolelist")){  //ajax方法获得用户的相关参数（js）
            this.getRoleList(request, response);
        }else if(method != null && method.equals("ucexist")){  //ajax方法判断是否用户已存在（useradd.js）
            this.userCodeExist(request, response);
        }else if(method != null && method.equals("deluser")){  // 删除用户方法（userlist.js）
            this.delUser(request, response);
        }else if(method != null && method.equals("view")){  //用户视图（userlist.js) url为返回的路径
            return this.getUserById(request, response,"userview",model);
        }else if(method != null && method.equals("modify")){          //修改前查询用户（userlist.js) url为返回的路径
            return this.getUserById(request, response,"usermodify",model);
        }else if(method != null && method.equals("modifyexe")){  //将查找的用户更新完成修改
            return this.modify(request, response);
        }else if(method != null && method.equals("pwdmodify")){  //修改密码前获得用户的密码
            this.getPwdByUserId(request, response);
        }else if(method != null && method.equals("savepwd")){   //修改用户的密码
            return this.updatePwd(request, response);
        }else if(method != null && method.equals("talkfind")){   //查找留言
            return this.talkfind(request, response);
        }else if(method != null && method.equals("talkadd")){  //添加留言
            return this.talkadd(request, response);
        }else if(method != null && method.equals("viewTalk")){  //留言视图（talklist.js) url为返回的路径
            return this.getTalkById(request, response,"talkview",model);
        }
        else if(method != null && method.equals("modifyTalk")){  //获得数据返回留言修改页面（talklist.js) url为返回的路径
            return this.getTalkById(request, response,"talkmodify",model);
        }
        else if(method != null && method.equals("delTalk")){  // 删除用户方法（talklist.js）
            this.delTalk(request, response);
        }
        else if(method != null && method.equals("talkmodify")){  //修改留言
            return this.talkmodify(request, response);
        }
        return "error";
    }





    private String add( HttpServletRequest request, HttpServletResponse response,User user,BindingResult bindingResult,HttpSession session)
            throws ServletException, IOException {
        if(bindingResult.hasErrors()){
            System.out.println("输入错误");
            return "useradd";
        }
        user.setCreatedBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
        user.setCreationDate(new Date());
        if(userService.addUser(user))
        {return "redirect:/user.do?method=query";}
        return "useradd";
    }  //增加用户的实现

    private String query(HttpServletRequest request, HttpServletResponse response)  //查询功能的实现
            throws ServletException, IOException {//查询用户列表
        String queryUserName = request.getParameter("queryname");  //从前端得到用户参数
        String temp = request.getParameter("queryUserRole");
        int queryUserRole = 0;
        List<User> userList = null;
        System.out.println("queryUserName servlet--------"+queryUserName);
        System.out.println("queryUserRole servlet--------"+queryUserRole);
        if(queryUserName == null){
            queryUserName = "";
        }
        if(temp != null && !temp.equals("")){
            queryUserRole = Integer.parseInt(temp);
        }


        userList = userService.getUserList(queryUserName,queryUserRole);  //获得查询用户的列表
        request.setAttribute("userList", userList);  //将列表返回前端
        List<Role> roleList = null;
        roleList = roleService.getAllRole();//获得用户的角色列表
        request.setAttribute("roleList", roleList);     //将参数返回给前端
        request.setAttribute("queryUserName", queryUserName);
        request.setAttribute("queryUserRole", queryUserRole);
        return "userlist";  //匹配userlist.jsp
    }

    private void getRoleList(HttpServletRequest request, HttpServletResponse response)//获得用户角色的参数列表
            throws ServletException, IOException {
        List<Role> roleList = null;
        roleList = roleService.getAllRole();
        response.setContentType("application/json");//把roleList转换成json对象输出
        PrintWriter outPrintWriter = response.getWriter();
        outPrintWriter.write(JSONArray.toJSONString(roleList));
        outPrintWriter.flush();
        outPrintWriter.close();
    }

    private void userCodeExist(HttpServletRequest request, HttpServletResponse response)  //判断用户账号是否可用
            throws ServletException, IOException {
        String userCode = request.getParameter("userCode");
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if(StringUtils.isNullOrEmpty(userCode)){
            resultMap.put("userCode", "no");
        }else{
            User user = userService.UserExist(userCode);
            if(null != user){
                resultMap.put("userCode","exist");
            }else{
                resultMap.put("userCode", "notexist");
            }
        }
        response.setContentType("application/json");//把resultMap转为json字符串以json的形式输出
        //从response对象中获取往外输出的writer对象
        PrintWriter outPrintWriter = response.getWriter(); //把resultMap转为json字符串输出
        outPrintWriter.write(JSONArray.toJSONString(resultMap));
        outPrintWriter.flush();//刷新
        outPrintWriter.close();//关闭流
    }

    private void delUser(HttpServletRequest request, HttpServletResponse response)  //删除用户的实现
            throws ServletException, IOException {
        String id = request.getParameter("uid");
        Integer delId = 0;
        try{
            delId = Integer.parseInt(id);
        }catch (Exception e) {
            // TODO: handle exception
            delId = 0;
        }
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if(delId <= 0){
            resultMap.put("delResult", "notexist");
        }else{

            if(userService.deleteUser(delId)){
                resultMap.put("delResult", "true");
            }else{
                resultMap.put("delResult", "false");
            }
        }

        //把resultMap转换成json对象输出
        response.setContentType("application/json");
        PrintWriter outPrintWriter = response.getWriter();
        outPrintWriter.write(JSONArray.toJSONString(resultMap));
        outPrintWriter.flush();
        outPrintWriter.close();
    }

    private String getUserById(HttpServletRequest request, HttpServletResponse response,String url,Model model) //通过id找用户
            throws ServletException, IOException {
        String id = request.getParameter("uid");
        if(!StringUtils.isNullOrEmpty(id)){
            User user = userService.findById(id); //调用方法得到user对象
            System.out.println(user);
            model.addAttribute("user", user); //往前端传数据user
            return url;
        }
        return "error";
    }

    private String modify(HttpServletRequest request, HttpServletResponse response)  //修改用户的实现
            throws ServletException, IOException {
        String id = request.getParameter("uid");
        String userName = request.getParameter("userName");
        String gender = request.getParameter("gender");
        String birthday = request.getParameter("birthday");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String userRole = request.getParameter("userRole");

        User user = new User();
        user.setId(Integer.valueOf(id));
        user.setUserName(userName);
        user.setGender(Integer.valueOf(gender));
        user.setBirthday(birthday);
        user.setPhone(phone);
        user.setAddress(address);
        user.setUserRole(Integer.valueOf(userRole));
        user.setModifyBy(((User)request.getSession().getAttribute(Constants.USER_SESSION)).getId());
        user.setModifyDate(new Date());
        System.out.println(user);
        if(userService.modify(user)){
            return "redirect:/user.do?method=query";

        }else{
            return "usermodify";

        }

    }

    private void getPwdByUserId(HttpServletRequest request, HttpServletResponse response) //获得用户密码的实现
            throws ServletException, IOException {
        Object o = request.getSession().getAttribute(Constants.USER_SESSION);
        String oldpassword = request.getParameter("oldpassword");
        Map<String, String> resultMap = new HashMap<String, String>();

        if(null == o ){//session过期
            resultMap.put("result", "sessionerror");
        }else if(StringUtils.isNullOrEmpty(oldpassword)&&oldpassword.length()<=6){//旧密码输入为空
            resultMap.put("result", "error");
        }else{
            String sessionPwd = ((User)o).getUserPassword();
            if(oldpassword.equals(sessionPwd)){
                resultMap.put("result", "true");
            }else{//旧密码输入不正确
                resultMap.put("result", "false");
            }
        }

        response.setContentType("application/json");
        PrintWriter outPrintWriter = response.getWriter();
        outPrintWriter.write(JSONArray.toJSONString(resultMap));
        outPrintWriter.flush();
        outPrintWriter.close();
    }

    private String updatePwd(HttpServletRequest request, HttpServletResponse response)  //修改密码的实现
            throws ServletException, IOException {

        Object o = request.getSession().getAttribute(Constants.USER_SESSION);
                String oldpassword = request.getParameter("oldpassword");
                String sessionPwd = ((User)o).getUserPassword();
                System.out.println(oldpassword);
                System.out.println(sessionPwd);


                String newpassword = request.getParameter("newpassword");
                String password1 = request.getParameter("rnewpassword");
        boolean flag = false;
        if(o != null && !StringUtils.isNullOrEmpty(newpassword)&&!StringUtils.isNullOrEmpty(password1)){
           if(!(newpassword.equals(password1))||newpassword.length()<=6||!(oldpassword.equals(sessionPwd)))
           {
               request.setAttribute(Constants.SYS_MESSAGE, "修改密码失败！");
           }
           else {
               flag = userService.updatePwd(((User) o).getId(), newpassword);

               if (flag) {
                   request.setAttribute(Constants.SYS_MESSAGE, "修改密码成功,请退出并使用新密码重新登录！");
                   request.getSession().removeAttribute(Constants.USER_SESSION);//session注销
                   return "login";

               } else {
                   request.setAttribute(Constants.SYS_MESSAGE, "修改密码失败！");
               }
           }
        }else{

            request.setAttribute(Constants.SYS_MESSAGE, "请输入数据,修改成功将自动退出");
        }
        return "pwdmodify";

    }





    private String talkmodify(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("kkkk");
        String id = request.getParameter("id");
        String talkName = request.getParameter("talkName");
        String talkRole = request.getParameter("talkRole");
        String userId = request.getParameter("userId");
        String talktitle= request.getParameter("talktitle");
        String talkContext = request.getParameter("talkContext");


        Talk talk = new Talk();
        talk.setId(Integer.parseInt(id));
        talk.setTalkName(talkName);
        talk.setTalkRole(Integer.parseInt(talkRole));
        talk.setUserId(Integer.parseInt(userId));
        talk.setTalkTitle(talktitle);
        talk.setTalkContext(talkContext);

        talk.setModifyDate(new Date());
        boolean flag = false;
        System.out.println(flag);

        flag =userService.updateTalk(talk);
        System.out.println("add flag -- > " + flag);
        if(flag){
            return "redirect:/user.do?method=talkfind";

        }else{
            return "talkmodify";
        }
    } //修改留言功能

    private void delTalk(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String id = request.getParameter("talkid");
        System.out.println(id);
        Integer delId = 0;
        try{
            delId = Integer.parseInt(id);
        }catch (Exception e) {
            // TODO: handle exception
            delId = 0;
        }
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if(delId <= 0){
            resultMap.put("delResult", "notexist");
        }else{

            if(userService.deleteTalk(delId)){
                resultMap.put("delResult", "true");
            }else{
                resultMap.put("delResult", "false");
            }
        }

        //把resultMap转换成json对象输出
        response.setContentType("application/json");
        PrintWriter outPrintWriter = response.getWriter();
        outPrintWriter.write(JSONArray.toJSONString(resultMap));
        outPrintWriter.flush();
        outPrintWriter.close();

    } //删除留言

    private String getTalkById(HttpServletRequest request, HttpServletResponse response, String url, Model model) {
        String id = request.getParameter("tid");
        if(!StringUtils.isNullOrEmpty(id)){
            Talk talk = userService.getTalkById(id); //调用方法得到user对象
            System.out.println(talk);
            model.addAttribute("talk", talk); //往前端传数据user
            return url;
        }
        return "error";
    } //根据id找留言数据

    private String talkadd(HttpServletRequest request, HttpServletResponse response) {
        String talkName = request.getParameter("talkName");
        String talkRole = request.getParameter("talkRole");
        String userId = request.getParameter("userId");
        String talktitle= request.getParameter("talktitle");
        String talkContext = request.getParameter("talkContext");


        Talk talk = new Talk();
        talk.setTalkName(talkName);
        talk.setTalkRole(Integer.parseInt(talkRole));
        talk.setUserId(Integer.parseInt(userId));
        talk.setTalkTitle(talktitle);
        talk.setTalkContext(talkContext);

        talk.setTalkDate(new Date());
        boolean flag = false;
        System.out.println(flag);

        flag =userService.addTalk(talk);
        System.out.println("add flag -- > " + flag);
        if(flag){
            return "redirect:/user.do?method=talkfind";

        }else{
            return "talkadd";
        }
    }//添加留言

    private String talkfind(HttpServletRequest request, HttpServletResponse response) {

        String talkname = request.getParameter("talkname");  //从前端得到用户参数
        String mytalk = request.getParameter("mytalk");
        String talkTitle= request.getParameter("talkTitle");
        List<Talk> talkList = null;
        System.out.println("talkname servlet--------"+talkname);
        System.out.println("talkTitle servlet--------"+talkTitle);
        if(talkname  == null){
            talkname  = "";
        }
        if(talkTitle == null){
            talkTitle  = "";
        }
        if(mytalk!=null)
        {
            talkList = userService.getTalkList(mytalk,talkTitle);  //获得查询用户的列表
            request.setAttribute("talkList", talkList);  //将列表返回前端

            return "talkmy";  //匹配talkmy.jsp
        }
        else {
            talkList = userService.getTalkList(talkname, talkTitle);  //获得查询用户的列表
            request.setAttribute("talkList", talkList);  //将列表返回前端
            request.setAttribute("talkname", talkname);
            request.setAttribute("talkTitle", talkTitle);
            return "talk";  //匹配talk.jsp
        }
    }   //查找留言













}
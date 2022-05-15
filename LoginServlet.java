package com.hjw.servlet.user;

import com.hjw.pojo.User;
import com.hjw.service.bill.BillServiceImpl;
import com.hjw.service.provider.ProviderserviceImpl;
import com.hjw.service.role.RoleServiceImpl;
import com.hjw.service.user.UserServiceImpl;
import com.hjw.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginServlet {
    @Autowired
    UserServiceImpl userService;
    @Autowired
    ProviderserviceImpl providerservice;
    @Autowired
    BillServiceImpl billService;
    @Autowired
    RoleServiceImpl roleService;
    @RequestMapping(value ="login.do",method = {RequestMethod.GET,RequestMethod.POST})
    public String dopost(HttpServletRequest req, HttpServletResponse resp, Model model){
        String userCode = req.getParameter("userCode");
        String userPassword = req.getParameter("userPassword");
        System.out.println(userCode+" "+userPassword);
        //调用service方法，进行用户匹配
        User user = userService.getLoginUser(userCode,userPassword);
        System.out.println(user);
        if(null != user){//登录成功
            //放入session
            req.getSession().setAttribute(Constants.USER_SESSION, user);
            //页面跳转（frame.jsp）
            return "frame";
        }else{
            //页面跳转（login.jsp）带出提示信息--转发
            model.addAttribute("error", "登录失败请重试");
            return "login";
            //.forward(request, response);
        }
    }








    }









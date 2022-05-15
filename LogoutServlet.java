package com.hjw.servlet.user;

import com.hjw.service.bill.BillServiceImpl;
import com.hjw.service.provider.ProviderserviceImpl;
import com.hjw.service.role.RoleServiceImpl;
import com.hjw.service.user.UserServiceImpl;
import com.hjw.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Controller
public class LogoutServlet {
    @Autowired
    UserServiceImpl userService;
    @Autowired
    ProviderserviceImpl providerservice;
    @Autowired
    BillServiceImpl billService;
    @Autowired
    RoleServiceImpl roleService;
    @RequestMapping(value = "/logout.do",method = {RequestMethod.GET,RequestMethod.POST})
    public  String doPost(HttpServletRequest req,HttpServletResponse resp){
        req.getSession().removeAttribute(Constants.USER_SESSION);
        return "/login";

    }

}

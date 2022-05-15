package com.hjw.servlet.user;

import com.hjw.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UserPageServlet {
    @RequestMapping(value="/useradd",method = {RequestMethod.POST,RequestMethod.GET})
    public String useradd(Model model){
        model.addAttribute("user", new User());//这里给视图层提供了数据的对象，用来数据绑定
        return "useradd";
    }
}

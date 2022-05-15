package com.hjw.servlet.provider;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ProviderPageServlet {
    @RequestMapping(value="/provideradd",method = {RequestMethod.POST,RequestMethod.GET})
    public String provideradd(){
        return "provideradd";
    }
}

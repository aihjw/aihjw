package com.hjw.servlet.bill;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class BillPageServlet {
    @RequestMapping(value="/billadd",method = {RequestMethod.POST,RequestMethod.GET})
    public String billadd(){
        return "billadd";
    }
}

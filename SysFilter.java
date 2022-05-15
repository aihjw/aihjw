package com.hjw.filter;

import com.hjw.pojo.User;
import com.hjw.utils.Constants;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SysFilter implements Filter {  //过滤器

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {


    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        System.out.println("SysFilter doFilter()===========");
        HttpServletRequest rq = (HttpServletRequest)request;
        HttpServletResponse rp = (HttpServletResponse)response;
        User userSession = (User)rq.getSession().getAttribute(Constants.USER_SESSION);
        if(null == userSession){
            rp.sendRedirect("/login.jsp");




            System.out.println("ddd");
        }else{
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }

}
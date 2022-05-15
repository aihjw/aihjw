package com.hjw.servlet.provider;

import com.alibaba.fastjson.JSONArray;
import com.hjw.pojo.Provider;
import com.hjw.pojo.User;
import com.hjw.service.bill.BillServiceImpl;
import com.hjw.service.provider.ProviderserviceImpl;
import com.hjw.service.user.UserServiceImpl;
import com.hjw.utils.Constants;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
public class ProviderServlet {
	@Autowired
	BillServiceImpl billService;
	@Autowired
	ProviderserviceImpl providerService;
	@Autowired
	UserServiceImpl userService;

	@RequestMapping(value="/provider.do",method = {RequestMethod.POST,RequestMethod.GET})
	public String doPost(HttpServletRequest request, HttpServletResponse response, @RequestParam(value="method",required = false) String method)
			throws ServletException, IOException {
		if(method != null && method.equals("query")){
			return this.query(request,response);
		}else if(method != null && method.equals("add")){
			return this.add(request,response);
		}else if(method != null && method.equals("view")){
			return this.getProviderById(request,response,"providerview");
		}else if(method != null && method.equals("modify")){
			return this.getProviderById(request,response,"providermodify");
		}else if(method != null && method.equals("modifysave")){
			return this.modify(request,response);
		}else if(method != null && method.equals("delprovider")){
			 this.delProvider(request,response);
		}
		return "error";
	}

	private void delProvider(HttpServletRequest request, HttpServletResponse response)  //删除供应商
			throws ServletException, IOException {
		String id = request.getParameter("proid");
		HashMap<String, String> resultMap = new HashMap<String, String>();
		if(!StringUtils.isNullOrEmpty(id)){

			int flag = providerService.getProviderAndBillList(id);
			if(flag == 0){//删除成功
				if(providerService.delete(id)==1)
				resultMap.put("delResult", "true");
			}else if(flag == -1){//删除失败
				resultMap.put("delResult", "false");
			}else if(flag > 0){//该供应商下有订单，不能删除，返回订单数
				resultMap.put("delResult", String.valueOf(flag));
			}
		}else{
			resultMap.put("delResult", "notexit");
		}
		//把resultMap转换成json对象输出
		response.setContentType("application/json");
		PrintWriter outPrintWriter = response.getWriter();
		outPrintWriter.write(JSONArray.toJSONString(resultMap));
		outPrintWriter.flush();
		outPrintWriter.close();
	}

	private String modify(HttpServletRequest request, HttpServletResponse response)  //修改供应商
			throws ServletException, IOException {
		System.out.println("ok");
		String proContact = request.getParameter("proContact");
		System.out.println(proContact);
		String proPhone = request.getParameter("proPhone");
		String proAddress = request.getParameter("proAddress");
		String proFax = request.getParameter("proFax");
		String proDesc = request.getParameter("proDesc");
		String id = request.getParameter("id");
		String proName= request.getParameter("proName");
		Provider provider = new Provider();
		provider.setId(Integer.valueOf(id));
		provider.setProContact(proContact);
		provider.setProPhone(proPhone);
		provider.setProFax(proFax);
		provider.setProAddress(proAddress);
		provider.setProDesc(proDesc);
		provider.setProName(proName);
		provider.setModifyBy(((User)request.getSession().getAttribute(Constants.USER_SESSION)).getId());
		provider.setModifyDate(new Date());
		System.out.println(provider);
		boolean flag = false;

		flag = providerService.modify(provider);
		System.out.println("--------flag="+flag);
		if(flag){
			return "redirect:/provider.do?method=query";

		}else{
			return "providermodify";

		}

	}

	private String getProviderById(HttpServletRequest request, HttpServletResponse response, String url)
			throws ServletException, IOException {

		String id = request.getParameter("proid");
		System.out.println("-------------"+id);
		if(!StringUtils.isNullOrEmpty(id)){

			Provider provider = null;
			provider = providerService.getProviderById(id);
			request.setAttribute("provider", provider);
			System.out.println("-------------"+id);
			if(url=="providermodify")
			return "providermodify";
			else return "providerview";
//			request.getRequestDispatcher(url).forward(request, response);
		}
		return "error";
	}
	private String add(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String proCode = request.getParameter("proCode");
		String proName = request.getParameter("proName");
		String proContact = request.getParameter("proContact");
		String proPhone = request.getParameter("proPhone");
		String proAddress = request.getParameter("proAddress");
		String proFax = request.getParameter("proFax");
		String proDesc = request.getParameter("proDesc");

		Provider provider = new Provider();
		provider.setProCode(proCode);
		provider.setProName(proName);
		provider.setProContact(proContact);
		provider.setProPhone(proPhone);
		provider.setProFax(proFax);
		provider.setProAddress(proAddress);
		provider.setProDesc(proDesc);
		provider.setCreatedBy(((User)request.getSession().getAttribute(Constants.USER_SESSION)).getId());
		provider.setCreationDate(new Date());
		boolean flag = false;

		flag = providerService.add(provider);
		if(flag){
			return "redirect:/provider.do?method=query";

		}else{
			return "provideradd";

		}
	}

	private String query(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String queryProName = request.getParameter("queryProName");
		String queryProCode = request.getParameter("queryProCode");
		if(StringUtils.isNullOrEmpty(queryProName)){
			queryProName = "";
		}
		if(StringUtils.isNullOrEmpty(queryProCode)){
			queryProCode = "";
		}
		List<Provider> providerList = new ArrayList<Provider>();
		providerList = providerService.getAllProvider(queryProName,queryProCode);
		request.setAttribute("providerList", providerList);
		request.setAttribute("queryProName", queryProName);
		request.setAttribute("queryProCode", queryProCode);
		return "providerlist";

	}


}

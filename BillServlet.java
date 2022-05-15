package com.hjw.servlet.bill;

import com.alibaba.fastjson.JSONArray;

import com.hjw.pojo.Bill;
import com.hjw.pojo.Good;
import com.hjw.pojo.Provider;
import com.hjw.pojo.User;
import com.hjw.service.bill.BillServiceImpl;
import com.hjw.service.good.GoodService;
import com.hjw.service.good.GoodServiceImpl;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
public class BillServlet{
	@Autowired
	BillServiceImpl billService;
	@Autowired
	ProviderserviceImpl providerService;
	@Autowired
	UserServiceImpl userService;
	@Autowired
	GoodServiceImpl goodService;

	@RequestMapping(value="/bill.do",method = {RequestMethod.POST,RequestMethod.GET})
	public String doPost(HttpServletRequest request, HttpServletResponse response, @RequestParam(value="method",required = false) String method,@RequestParam(value="ProviderSelect",required = false) String id,@RequestParam(value="goodid",required = false) String goodid)
			throws ServletException, IOException {
		System.out.println("=================== "+method);
		if(method != null && method.equals("query")){
			return this.query(request,response);
		}else if(method != null && method.equals("add")){
			return  this.add(request,response);
		}else if(method != null && method.equals("view")){
			return  this.getBillById(request,response,"billview");
		}else if(method != null && method.equals("modify")){
			return this.getBillById(request,response,"billmodify");
		}else if(method != null && method.equals("modifysave")){
			return this.modify(request,response);
		}else if(method != null && method.equals("delbill")) {
			this.delBill(request, response);
		}else if(method != null && method.equals("getproviderlist")){
			this.getProviderlist(request,response);
		}else if(id!=null){
			this.getGoodlist(request,response, id);
		}
		else if(goodid!=null){
			this.getGooddw(request,response, goodid);
		}
		else if(method!=null&&method.equals("getAllGood"))
		{
			this.getAllGood(request,response);
		}
		return "error";
	}

	private void getAllGood(HttpServletRequest request, HttpServletResponse response)throws IOException {
		System.out.println("getAllGood ========================= ");

		List<Good> goodList = goodService.getGoods("");

		System.out.println(goodList);

		//把goodList转换成json对象输出
		response.setContentType("application/json");
		PrintWriter outPrintWriter = response.getWriter();
		outPrintWriter.write(JSONArray.toJSONString(goodList));
		outPrintWriter.flush();
		outPrintWriter.close();
	}


	private void getGooddw(HttpServletRequest request, HttpServletResponse response, String goodid) throws IOException {
		System.out.println(goodid);
		Good goodById = goodService.getGoodById(goodid);
		System.out.println(goodById);
		response.setContentType("application/json");
		PrintWriter outPrintWriter = response.getWriter();
		outPrintWriter.write(JSONArray.toJSONString(goodById));
		outPrintWriter.flush();
		outPrintWriter.close();

	}

	private void getGoodlist(HttpServletRequest request, HttpServletResponse response,String id) throws IOException {
		System.out.println("getproviderlist ========================= ");
		System.out.println(id);
		List<Good> goodList = goodService.getGoods(id);

		System.out.println(goodList);

		//把goodList转换成json对象输出
		response.setContentType("application/json");
		PrintWriter outPrintWriter = response.getWriter();
		outPrintWriter.write(JSONArray.toJSONString(goodList));
		outPrintWriter.flush();
		outPrintWriter.close();
	}

	private void getProviderlist(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("getproviderlist ========================= ");

		List<Provider> providerList = new ArrayList<Provider>();
		providerList = providerService.getAllProvider("","");

		System.out.println(providerList);
		System.out.println("getProName--------------"+providerList.get(0).getProName());
		//把providerList转换成json对象输出
		response.setContentType("application/json");
		PrintWriter outPrintWriter = response.getWriter();
		outPrintWriter.write(JSONArray.toJSONString(providerList));
		outPrintWriter.flush();
		outPrintWriter.close();
	}
	private String getBillById(HttpServletRequest request, HttpServletResponse response,String url)
			throws ServletException, IOException {
		String id = request.getParameter("billid");
		if(!StringUtils.isNullOrEmpty(id)){

			Bill bill = null;
			bill = billService.getBill(id);
			System.out.println("bill================>  "+bill);
			request.setAttribute("bill", bill);
			return url;

		}
		return "error";
	}

	private String modify(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("modify===============");
		String id = request.getParameter("id");
		String providerId = request.getParameter("ProviderSelect");
		String Goodid = request.getParameter("queryGoodId");
		Good goodById = goodService.getGoodById(Goodid);
		String productName=goodById.getGoodsName();
		String productUnit = request.getParameter("productUnit");
		String productDesc = request.getParameter("productdj");
	;String productCount = request.getParameter("productCount");
		String totalPrice = request.getParameter("totalPrice");
		String isPayment = request.getParameter("isPayment");

		Bill bill = new Bill();
		bill.setId(Integer.valueOf(id));
		bill.setProductName(productName);
		bill.setProductDesc(productDesc);
		bill.setProductUnit(productUnit);
		bill.setProductCount(new BigDecimal(productCount).setScale(2,BigDecimal.ROUND_DOWN));
		bill.setIsPayment(Integer.parseInt(isPayment));
		bill.setTotalPrice(new BigDecimal(totalPrice).setScale(2,BigDecimal.ROUND_DOWN));
		bill.setProviderId(Integer.parseInt(providerId));

		bill.setModifyBy(((User)request.getSession().getAttribute(Constants.USER_SESSION)).getId());
		bill.setModifyDate(new Date());
		boolean flag = false;

		flag = billService.modify(bill);
		if(flag){
			return "redirect:/bill.do?method=query";

		}else{
			return "billmodify";

		}
	}

	private void delBill(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String id = request.getParameter("billid");
		HashMap<String, String> resultMap = new HashMap<String, String>();
		if(!StringUtils.isNullOrEmpty(id)){

			boolean flag = billService.delete(id);
			if(flag){//删除成功
				resultMap.put("delResult", "true");
			}else{//删除失败
				resultMap.put("delResult", "false");
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
	private String add(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String providerId = request.getParameter("ProviderSelect");
		String Goodid = request.getParameter("queryGoodId");
		String billCode = request.getParameter("billCode");
		String productUnit = request.getParameter("productUnit");
		String productdj = request.getParameter("productdj");
		String productCount = request.getParameter("productCount");
		String totalPrice = request.getParameter("totalPrice");
		String isPayment = request.getParameter("isPayment");
		Good goodById = goodService.getGoodById(Goodid);
		String productName=goodById.getGoodsName();
		Bill bill = new Bill();
		bill.setBillCode(billCode);
		bill.setProductName(productName);
		bill.setProductDesc(productdj);
		bill.setProductUnit(productUnit);
		bill.setProductCount(new BigDecimal(productCount).setScale(2,BigDecimal.ROUND_DOWN));
		bill.setIsPayment(Integer.parseInt(isPayment));
		bill.setTotalPrice(new BigDecimal(totalPrice).setScale(2,BigDecimal.ROUND_DOWN));
		bill.setProviderId(Integer.parseInt(providerId));
		bill.setCreatedBy(((User)request.getSession().getAttribute(Constants.USER_SESSION)).getId());
		bill.setCreationDate(new Date());
		boolean flag = false;
		System.out.println(flag);

		flag = billService.add(bill);
		System.out.println("add flag -- > " + flag);
		if(flag){
			return "redirect:/bill.do?method=query";

		}else{
			return "billadd";
		}
	}


	private String query(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("=======================");
		List<Provider> providerList = new ArrayList<Provider>();
		providerList = providerService.getAllProvider("","");
		request.setAttribute("providerList", providerList);
		String queryProductName = request.getParameter("queryProductName");
		String queryProviderId = request.getParameter("queryProviderId");
		System.out.println(queryProviderId);
		String queryIsPayment = request.getParameter("queryIsPayment");
		if(StringUtils.isNullOrEmpty(queryProductName)){
			queryProductName = "";
		}

		List<Bill> billList = new ArrayList<Bill>();
		Bill bill = new Bill();
		if(StringUtils.isNullOrEmpty(queryIsPayment)){
			bill.setIsPayment(0);
		}else{
			bill.setIsPayment(Integer.parseInt(queryIsPayment));
		}

		if(StringUtils.isNullOrEmpty(queryProviderId)){
			bill.setProviderId(0);
		}else{
			bill.setProviderId(Integer.parseInt(queryProviderId));
		}
		bill.setProductName(queryProductName);
		billList = billService.getBillList(bill.getProductName(), String.valueOf(bill.getProviderId()), String.valueOf(bill.getIsPayment()));
        System.out.println("=------------------"+billList);
		request.setAttribute("billList", billList);
		request.setAttribute("queryProductName", queryProductName);
		request.setAttribute("queryProviderId", queryProviderId);
		request.setAttribute("queryIsPayment", queryIsPayment);
		return "billlist";


	}




}

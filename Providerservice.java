package com.hjw.service.provider;

import com.hjw.dao.provider.ProviderDao;
import com.hjw.pojo.Provider;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


public interface Providerservice {

    boolean add(Provider provider);  //增加供应商
    boolean modify(Provider provider); //修改供应商信息
    List<Provider> getAllProvider();  //获得所有的供应商
    int delete( String id);  //删除供应商
    Provider getProviderById(String id); //根据id查供应商
    List<Provider> getAllProvider( String Name,  String Code); //根据条件差供应商
   int getProviderAndBillList( String id); //根据id得到账单和供应商

}

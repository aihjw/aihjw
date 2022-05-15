package com.hjw.service.provider;

import com.hjw.dao.provider.ProviderDao;
import com.hjw.pojo.Provider;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional(propagation= Propagation.REQUIRED,rollbackForClassName="Exception")
@Service
public class ProviderserviceImpl implements Providerservice{
   @Autowired
    private ProviderDao providerDao;
   @Override
    public boolean add(Provider provider){
       return providerDao.add(provider)>=1?true:false;
    }  //增加供应商
    @Override
   public boolean modify(Provider provider){
      return providerDao.modify(provider)>=1?true:false;
   } //修改供应商信息
    @Override
   public List<Provider> getAllProvider(){
       return providerDao.getAllProvider();
    }  //获得所有的供应商
    @Override
    public int delete(@Param("id") String id){
       return providerDao.delete(id);
    } //删除供应商
    @Override
    public Provider getProviderById(String id){
       return providerDao.getProviderById(id);
    } //根据id查供应商
    @Override
    public List<Provider> getAllProvider(@Param("Name") String Name, @Param("Code") String Code) //根据条件差供应商
    {
        return providerDao.getAllProvider(Name,Code);
    }


    @Override
    public int getProviderAndBillList(String id)
    {
        return providerDao.getProviderAndBillList(id);
    } //根据id得到账单和供应商
}

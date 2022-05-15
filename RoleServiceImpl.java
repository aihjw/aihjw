package com.hjw.service.role;

import com.hjw.dao.role.RoleDao;
import com.hjw.pojo.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional(propagation= Propagation.REQUIRED,rollbackForClassName="Exception")
@Service
public class RoleServiceImpl implements RoleService{
    @Autowired
    private RoleDao roleDao;
    @Override
    public List<Role> getAllRole(){
        List<Role> allRole = roleDao.getAllRole();
        return allRole;
    }
}

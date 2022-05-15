package com.hjw.dao.role;

import com.hjw.pojo.Role;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
@Repository
public interface RoleDao {
    public List<Role> getAllRole();
}

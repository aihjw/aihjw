package com.hjw.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class User {
    private Integer id; //用户id
    @NotEmpty(message="编码不能为空")
    private String userCode; //用户编码
    @NotEmpty(message="名称不能为空")
    private String userName; //用户名称
    @NotEmpty(message="密码不能为空")
    @Size(min=6, max=10 ,message = "请输入6到8位的密码")
    private String userPassword; //用户密码
    private Integer gender;  //性别
    private String birthday;  //年龄
    @NotEmpty(message="电话不能为空")
    private String phone;   //电话
    @NotEmpty(message="地址不能为空")
    private String address; //地址
    private Integer userRole;    //用户角色
    private Integer createdBy;   //创建者
    private Date creationDate; //创建时间
    private Integer modifyBy;     //更新者
    private Date modifyDate;   //更新时间
    @NotNull(message="年龄不能为空")
    private Integer age;//年龄
    private String userRoleName;    //用户角色名称
    private Role role; //角色

    public Role getRole() {
        System.out.println(role);
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
    public String getUserRoleName() {
        return userRoleName;
    }
    public void setUserRoleName(String userRoleName) {
        this.userRoleName = userRoleName;
    }
    public Integer getUserRole() {
        return userRole;
    }
    public void setUserRole(Integer userRole) {
        this.userRole = userRole;
    }
}


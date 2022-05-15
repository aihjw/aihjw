package com.hjw.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Component;


import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class Role {
    private Integer id;   //id
    @NotEmpty(message = "编码不能为空")
    private String roleCode; //角色编码
    @NotEmpty(message = "名称不能为空")
    private String roleName; //角色名称
    private Integer createdBy; //创建者
    private Date creationDate; //创建时间
    private Integer modifyBy; //更新者
    private Date modifyDate;//更新时间


}

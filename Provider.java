package com.hjw.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component  //容器
public class Provider {
    private Integer id;   //id
    @NotEmpty(message = "编码不能为空")
    private String proCode; //供应商编码
    @NotEmpty(message = "名称不能为空")
    private String proName; //供应商名称
    private String proDesc; //供应商描述
    private String proContact; //供应商联系人
    @NotEmpty(message = "电话不能为空")
    private String proPhone; //供应商电话
    @NotEmpty(message="地址不能为空")
    private String proAddress; //供应商地址
    private String proFax; //供应商传真
    private Integer createdBy; //创建者
    private Date creationDate; //创建时间
    private Integer modifyBy; //更新者
    private Date modifyDate;//更新时间
    private List<Bill> billList;  //账单信息



}


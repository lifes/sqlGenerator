package com.hikvision.kapu.modules.config.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 数据字典
 * @author shanguoming 2015年12月29日 下午4:51:15
 * @version V1.0   
 * @modify: {原因} by shanguoming 2015年12月29日 下午4:51:15
 */
@Entity
@Table(name = "bms_data_dictionary")
public class DataDictionary implements Serializable{
	
	/**
	 * 序列化ID
	 */
	private static final long serialVersionUID = 1L;
	/** 系统编码ID */
	private Integer id;
	/** 分组 */
	private String group;
	/** 编码 */
	private Integer code;
	/** 名称 */
	private String name;
	/** 备注 */
	private String remark;
	/** 所属子品牌 */
	private Integer subLogoCode;
	/** 所属品牌 */
	private Integer autoLogoCode;
	/** 拼音*/
	private String pinYing;
	/** 简拼*/
	private String jianPin;
	
	/**
	 */
	@Id
	@SequenceGenerator(name = "generator_data_dictionary", sequenceName = "S_data_dictionary", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "generator_data_dictionary")
	@Column(name = "type_list_id")
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name = "type_list_group", length = 64)
	public String getGroup() {
		return group;
	}
	
	public void setGroup(String group) {
		this.group = group;
	}
	
	@Column(name = "type_list_code")
	public Integer getCode() {
		return code;
	}
	
	public void setCode(Integer code) {
		this.code = code;
	}
	
	@Column(name = "type_list_name", length = 64)
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "type_list_remark", length = 64)
	public String getRemark() {
		return remark;
	}
	
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Column(name = "type_list_code_subLogo")
	public Integer getSubLogoCode() {
		return subLogoCode;
	}
	
	public void setSubLogoCode(Integer subLogoCode) {
		this.subLogoCode = subLogoCode;
	}
	
	@Column(name = "type_list_code_autoLogo")
	public Integer getAutoLogoCode() {
		return autoLogoCode;
	}
	
	public void setAutoLogoCode(Integer autoLogoCode) {
		this.autoLogoCode = autoLogoCode;
	}

	@Column(name = "type_list_pinying" ,length=250,nullable=true)
    public String getPinYing() {
    	return pinYing;
    }
	
    public void setPinYing(String pinYing) {
    	this.pinYing = pinYing;
    }

	@Column(name = "type_list_jianpin" ,length=250,nullable=true)
    public String getJianPin() {
    	return jianPin;
    }
	
    public void setJianPin(String jianPin) {
    	this.jianPin = jianPin;
    }
}

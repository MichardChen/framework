package com.framework.entity;

import java.io.Serializable;
import java.sql.Timestamp;



/**
 * 
 * 
 * @author R & D
 * @email 908350381@qq.com
 * @date 2018-05-16 23:00:02
 */
public class TBrandEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;
	//
	private String brand;
	//
	private String brandIcon;
	//
	private Integer createBy;
	//
	private Integer updateBy;
	//
	private Timestamp createTime;
	//
	private Timestamp updateTime;
	
	private String word;
	
	private int flg;
	
	private int showflg;
	
	private int importflg;
	
	public int getImportflg() {
		return importflg;
	}
	public void setImportflg(int importflg) {
		this.importflg = importflg;
	}
	public int getShowflg() {
		return showflg;
	}
	public void setShowflg(int showflg) {
		this.showflg = showflg;
	}
	public int getFlg() {
		return flg;
	}
	public void setFlg(int flg) {
		this.flg = flg;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Integer getId() {
		return id;
	}
	public String getBrand() {
		return brand;
	}
	public String getBrandIcon() {
		return brandIcon;
	}
	public Integer getCreateBy() {
		return createBy;
	}
	public Integer getUpdateBy() {
		return updateBy;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public void setBrandIcon(String brandIcon) {
		this.brandIcon = brandIcon;
	}
	public void setCreateBy(Integer createBy) {
		this.createBy = createBy;
	}
	public void setUpdateBy(Integer updateBy) {
		this.updateBy = updateBy;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	
}

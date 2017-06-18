package com.leven.dmd.pro.pref.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DBConfigList implements Serializable {
	/**
	 * @author leven
	 * 2012-12-5 ����01:01:00
	 */
	private static final long serialVersionUID = -3186809271640055521L;
	private List<DBConfigModel> configs = new ArrayList<DBConfigModel>();

	public DBConfigList(){}
	
	public List<DBConfigModel> getConfigs() {
		return configs;
	}
	public void setConfigs(List<DBConfigModel> configs) {
		this.configs = configs;
	}
}

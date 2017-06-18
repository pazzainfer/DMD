package com.leven.dmd.pro.pref.action;

import java.util.Map;

import org.eclipse.jface.wizard.Wizard;

import com.leven.dmd.pro.Messages;
import com.leven.dmd.pro.pref.model.DBConfigModel;

public class DBConfigEditWizard extends Wizard {
	private Map<String, DBConfigModel> map;
	private Map<String, DBConfigModel> configMap;
	private DBConfigEditWizardPage page;
	
	private DBConfigModel config;

	public DBConfigEditWizard(Map<String, DBConfigModel> map,
			Map<String, DBConfigModel> configMap,DBConfigModel config) {
		this.map = map;
		this.configMap = configMap;
		this.config = config;
		this.setNeedsProgressMonitor(false);
		this.setWindowTitle(Messages.DBConfigEditWizard_0);
	}
	@Override
	public void addPages() {
		page = new DBConfigEditWizardPage(this);
		addPage(page);
	}

	@Override
	public boolean performFinish() {
		DBConfigModel config = page.getDBConfigModel();
		if(config != null){
			map.put("data", config); //$NON-NLS-1$
			return true;
		}else {
			return false;
		}
	}
	
	public Map<String, DBConfigModel> getMap() {
		return map;
	}
	public void setMap(Map<String, DBConfigModel> map) {
		this.map = map;
	}
	public Map<String, DBConfigModel> getConfigMap() {
		return configMap;
	}
	public void setConfigMap(Map<String, DBConfigModel> configMap) {
		this.configMap = configMap;
	}
	public DBConfigModel getConfig() {
		return config;
	}
	public void setConfig(DBConfigModel config) {
		this.config = config;
	}
}

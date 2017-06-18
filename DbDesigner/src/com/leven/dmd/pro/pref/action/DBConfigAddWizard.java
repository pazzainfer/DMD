package com.leven.dmd.pro.pref.action;

import java.util.Map;

import org.eclipse.jface.wizard.Wizard;

import com.leven.dmd.pro.Messages;
import com.leven.dmd.pro.pref.model.DBConfigModel;

public class DBConfigAddWizard extends Wizard {
	private Map<String, DBConfigModel> map;
	private Map<String, DBConfigModel> configMap;
	private DBConfigAddWizardPage page;

	public DBConfigAddWizard(Map<String, DBConfigModel> map,
			Map<String, DBConfigModel> configMap) {
		this.map = map;
		this.configMap = configMap;
		this.setNeedsProgressMonitor(false);
		this.setWindowTitle(Messages.DBConfigAddWizard_0);
	}
	@Override
	public void addPages() {
		page = new DBConfigAddWizardPage(this);
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
}

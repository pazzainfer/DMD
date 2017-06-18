package com.leven.dmd.gef.tmpfile.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.tmpfile.util.SchemaTemplateConstants;

@SuppressWarnings("serial")
public class SchemaTemplate implements Serializable {
	private String name;
	private String filepath;
	
	private ArrayList<SchemaTemplateType> schemaTemplateTypes = new ArrayList<SchemaTemplateType>();
	private Map<String,SchemaTemplateType> schemaTemplateTypesMap = new HashMap<String,SchemaTemplateType>();
	
	private ArrayList<ColumnTemplate> columnTemplates = new ArrayList<ColumnTemplate>();
	private Map<String,ColumnTemplate> columnTemplatesMap = new HashMap<String,ColumnTemplate>();
	
	private ArrayList<SequenceTemplate> sequenceTemplates = new ArrayList<SequenceTemplate>();
	private Map<String,SequenceTemplate> sequenceTemplatesMap = new HashMap<String,SequenceTemplate>();
	
	//预留属性
	private Object tempField1;
	private Object tempField2;
	private Object tempField3;
	private Object tempField4;
	
	
	public SchemaTemplate(String name){
		this.name = name;
		SchemaTemplateType stDomain = new SchemaTemplateType();
		stDomain.setType(SchemaTemplateConstants.TYPE_COLUMN_TYPE_TEMPLATE);
		stDomain.setDescription(Messages.SchemaTemplate_0);
		this.addSchemaTemplateType(stDomain);
		SchemaTemplateType stColumn = new SchemaTemplateType();
		stColumn.setType(SchemaTemplateConstants.TYPE_COLUMN_TEMPLATE);
		stColumn.setDescription(Messages.SchemaTemplate_1);
		this.addSchemaTemplateType(stColumn);
		SchemaTemplateType stSequence = new SchemaTemplateType();
		stSequence.setType(SchemaTemplateConstants.TYPE_SEQUENCE_TEMPLATE);
		stSequence.setDescription(Messages.SchemaTemplate_2);
		this.addSchemaTemplateType(stSequence);
	}
	/**
	 * ����ֶ�ģ��,��������Ƿ�ɹ���ʶ
	 * @author leven
	 * 2012-11-1 ����11:31:49
	 * @param obj
	 * @return return true if success,false or not.
	 */
	public boolean addColumnTemplate(ColumnTemplate obj){
		if(!columnTemplatesMap.containsKey(obj.getCode())){
			for(ColumnTemplate temp : columnTemplates){
				if(temp.getName().equals(obj.getName())){
					return false;
				}
			}
			obj.setSchemaTemplate(this);
			columnTemplates.add(obj);
			columnTemplatesMap.put(obj.getCode(), obj);
			return true;
		}else {
			return false;
		}
	}
	/**
	 * ɾ���ֶ�ģ��,�����Ƿ�ɹ���ʶ
	 * @author leven
	 * 2012-11-1 ����11:31:49
	 * @param obj
	 * @return return true if success,false or not.
	 */
	public boolean removeColumnTemplate(ColumnTemplate obj){
		if(obj!=null){
			columnTemplatesMap.remove(obj.getCode());
			return columnTemplates.remove(obj);
		}else {
			return false;
		}
	}
	
	/**
	 * ���sequenceģ��,��������Ƿ�ɹ���ʶ
	 * @param obj
	 * @return return true if success,false or not.
	 */
	public boolean addSequenceTemplate(SequenceTemplate obj){
		if(!sequenceTemplatesMap.containsKey(obj.getCode())){
			obj.setSchemaTemplate(this);
			sequenceTemplates.add(obj);
			sequenceTemplatesMap.put(obj.getCode(), obj);
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * ɾ��domainģ��,�����Ƿ�ɹ���ʶ
	 * @param obj
	 * @return return true if success,false or not.
	 */
	public boolean removeSequenceTemplate(SequenceTemplate obj){
		if(obj!=null){
			sequenceTemplatesMap.remove(obj.getCode());
			return sequenceTemplates.remove(obj);
		}else {
			return false;
		}
	}
	
	
	public boolean addSchemaTemplateType(SchemaTemplateType obj){
		if(schemaTemplateTypesMap.get(obj.getType())==null){
			schemaTemplateTypes.add(obj);
			schemaTemplateTypesMap.put(obj.getType(), obj);
			return true;
		}else {
			return false;
		}
	}
	
	
	public boolean removeSchemaTemplateType(SchemaTemplateType obj){
		if(obj!=null){
			schemaTemplateTypesMap.remove(obj.getType());
			return schemaTemplateTypes.remove(obj);
		}else {
			return false;
		}
	}
	

	
	public Map<String, ColumnTemplate> getColumnTemplatesMap() {
		return columnTemplatesMap;
	}
	public void setColumnTemplatesMap(Map<String, ColumnTemplate> columnTemplatesMap) {
		this.columnTemplatesMap = columnTemplatesMap;
	}
	public ArrayList<ColumnTemplate> getColumnTemplates() {
		return columnTemplates;
	}
	public void setColumnTemplates(ArrayList<ColumnTemplate> columnTemplates) {
		this.columnTemplates = columnTemplates;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Map<String, SequenceTemplate> getSequenceTemplatesMap() {
		return sequenceTemplatesMap;
	}
	public void setSequenceTemplatesMap(
			Map<String, SequenceTemplate> sequenceTemplatesMap) {
		this.sequenceTemplatesMap = sequenceTemplatesMap;
	}
	public ArrayList<SequenceTemplate> getSequenceTemplates() {
		return sequenceTemplates;
	}
	public void setSequenceTemplates(ArrayList<SequenceTemplate> sequenceTemplates) {
		this.sequenceTemplates = sequenceTemplates;
	}
	public Map<String, SchemaTemplateType> getSchemaTemplateTypesMap() {
		return schemaTemplateTypesMap;
	}
	public void setSchemaTemplateTypesMap(Map<String, SchemaTemplateType> schemaTypesMap) {
		this.schemaTemplateTypesMap = schemaTypesMap;
	}
	public ArrayList<SchemaTemplateType> getSchemaTemplateTypes() {
		return schemaTemplateTypes;
	}
	public void setSchemaTemplateTypes(ArrayList<SchemaTemplateType> schemaTypes) {
		this.schemaTemplateTypes = schemaTypes;
	}
	public String getFilepath() {
		return filepath;
	}
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
}

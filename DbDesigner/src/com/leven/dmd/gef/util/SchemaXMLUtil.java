package com.leven.dmd.gef.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.ObjectInputStream;
import org.eclipse.draw2d.geometry.Rectangle;

import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.model.Table;
import com.thoughtworks.xstream.XStream;

public class SchemaXMLUtil {
	
	
	public static Schema getSchema(File file){
		Schema schema = null;
		try {
			InputStream is = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(is);
			schema = (Schema) ois.readObject();
			ois.close();
			is.close();
//			schema = SchemaXMLUtil.getSchemaFromFile(file);
			if(schema==null) {
				schema = new Schema(file.getName());
				ois.close();
				return schema;
			}
			ois.close();
		} catch (Exception e) {
			schema = new Schema(file.getName());
			return schema;
		}
		return schema;
	}
	/**
	 * 从指定文件中读取出Schema对象
	 * @author leven
	 * 2012-12-19 上午10:52:21
	 * @param file
	 * @return
	 */
	public static Schema getSchemaFromFile(File file){
		System.out.println("ininininininin");
		Schema schema = null;
		XStream xStream = createXStrem();
		FileReader fr = null;
		try{
			fr = new FileReader(file);
			schema = (Schema)xStream.fromXML(fr);
		} catch (Exception e){
			e.printStackTrace();
			return new Schema(file.getName());
		} finally{
			if(fr != null){
				try {
					fr.close();
					fr = null;
				} catch (Exception e) {
					e.printStackTrace();
					return new Schema(file.getName());
				}
			}
		}
		return schema;
	}
	/**
	 * 向指定文件写入Schema对象
	 * @author leven
	 * 2012-12-19 上午10:50:04
	 * @param schema
	 * @param file
	 * @return
	 */
	public static boolean writeSchemaToFile(Schema schema, File file){
		XStream xStream = createXStrem();
		FileWriter writer = null;
		try {
			if(!file.exists()){
				file.createNewFile();
			}
			writer = new FileWriter(file);
			writer.write("<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n"
					+ xStream.toXML(schema));
		} catch (Exception e) {
			return false;
		} finally{
			if(writer!=null){
				try {
					writer.flush();
					writer.close();
					writer = null;
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		return true;
	}
	
	private static XStream createXStrem() {
		XStream xStream = new XStream();
		xStream.alias("schema", Schema.class);
		xStream.useAttributeFor(Schema.class,"name");
		xStream.useAttributeFor(Schema.class,"packageIndex");
		xStream.useAttributeFor(Schema.class,"projectPath");
		
		xStream.aliasField("tables",Schema.class,"tables");
		xStream.aliasField("tablePackages",Schema.class,"tablePackages");
		xStream.aliasField("schemaTemplate",Schema.class,"schemaTemplate");
		
		xStream.alias("table",Table.class);
		xStream.useAttributeFor(Table.class,"name");
		xStream.useAttributeFor(Table.class,"cnName");
		xStream.useAttributeFor(Table.class,"description");
		xStream.aliasField("bounds",Table.class,"bounds");
		xStream.alias("bounds",Rectangle.class);
		xStream.useAttributeFor(Rectangle.class,"x");
		xStream.useAttributeFor(Rectangle.class,"y");
		xStream.useAttributeFor(Rectangle.class,"width");
		xStream.useAttributeFor(Rectangle.class,"height");
		
		return xStream;
	}
}

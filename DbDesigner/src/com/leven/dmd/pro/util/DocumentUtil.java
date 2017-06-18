package com.leven.dmd.pro.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.w3c.dom.Document;

import com.leven.dmd.pro.Messages;


/**
 *
 */

public class DocumentUtil {

	/**
	 * ��Document����д���ļ�
	 * 
	 * @param document
	 *            Document����
	 * @param file
	 *            д����ļ�
	 */
	public static void document2File(Document document, File file) {
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(document);
			oos.flush();
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println(Messages.DocumentUtil_0);
			e.printStackTrace();
		}
	}

	/**
	 * ���ļ���ȡDocument����
	 * 
	 * @param file
	 *            ��ȡ���ļ�
	 * @return Document����
	 */
	public static Document file2Document(File file) {
		ObjectInputStream ois = null;
		Document document = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(file));
			try {
				document = (Document) ois.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			ois.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println(Messages.DocumentUtil_1);
			e.printStackTrace();
		}
		return document;
	}
	
	 

}

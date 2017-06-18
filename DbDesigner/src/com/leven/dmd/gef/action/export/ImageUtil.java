package com.leven.dmd.gef.action.export;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Display;

/**
 * ͼƬ���ɹ���
 * @author leven
 * 2012-8-31 ����06:35:37
 */
public class ImageUtil {
	
	/**
	 * ����figure��ͼƬ
	 * @param figure
	 * @param width
	 * @param height
	 * @return
	 */
	public static final Image getFigureImage(IFigure figure, int width, int height) {
		Image rootImage = new Image(Display.getDefault(), width, height);
		GC gc = new GC(rootImage);
		Graphics graphics = new SWTGraphics(gc);
		figure.paint(graphics);  //ͨ������figure�����Զ���GC,���GC��figure���Ƶ�rootImage
		gc.dispose();
		return rootImage;
	}
	
	/**
	 * ����ImageData��ָ�����ļ���
	 * @param image
	 * @param savePath
	 * @param format IMAGE_PNG,IMAGE_BMP and so on.
	 */
	public static final void saveImage(Image image,String savePath,int format){
		ImageLoader loader = new ImageLoader();
		ImageData data = image.getImageData();
		loader.data = new ImageData[] {data};
		
		loader.save(savePath, format); 
	}

	
}
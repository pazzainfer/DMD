package com.leven.dmd.pro.exp;



import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.Platform;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;

import com.leven.dmd.pro.Messages;

public class ExportImageAction extends Action
{
  private int format;
  private GraphicalViewer viewer;

  public ExportImageAction(GraphicalViewer viewer)
  {
    super(Messages.ExportImageAction_0, GefUiPluginImages.IMG_BMP);
    this.viewer = viewer;
  }

  public void run()
  {
    Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

    FileDialog dialog = new FileDialog(shell, 8196);

    dialog.setFilterNames(StringUtils.split("BMP,JPG", ',')); //$NON-NLS-1$
    dialog.setFilterExtensions(StringUtils.split("*.bmp,*.jpg,*.jpeg", ',')); //$NON-NLS-1$

    String result = dialog.open();
    if(result.toUpperCase().endsWith(".BMP")){ //$NON-NLS-1$
    	format = 0;
    }else{
    	format = 4;
    }
    if (result != null) {
      OutputStream outputStream = null;
      try
      {
        outputStream = new FileOutputStream(result);
        GefUtil.exportImage(this.viewer, outputStream, this.format, false);
      }
      catch (Exception e) {
    	  System.out.println(e.getMessage());
      } finally {
        IOUtils.closeQuietly(outputStream);
      }
    }
  }
  /**
   * 
   */
  public void fileSaveAutoCreateBmpRun(String path){

      OutputStream outputStream = null;
      try
      {
        outputStream = new FileOutputStream(path);
        GefUtil.exportImage(this.viewer, outputStream, 0, false);
      }
      catch (Exception e) {
    	  System.out.println(e.getMessage());
      } finally {
        IOUtils.closeQuietly(outputStream);
      }
  }
  /**
   * 
   */
  public void fileSaveAutoCreateJPGRun(String path){

      OutputStream outputStream = null;
      try
      {
        outputStream = new FileOutputStream(path);
        GefUtil.exportImage(this.viewer, outputStream, 4, false);
      }
      catch (Exception e) {
    	  System.out.println(e.getMessage());
      } finally {
        IOUtils.closeQuietly(outputStream);
      }
  }
  
  /**
	 * ��ԭʼ����
	 * @param viewer
	 */
	 public void buildExportMenu( GraphicalViewer viewer)
	  {
	    String[] t_FileExtensions;
	    String[] t_Titles;
	    int[] t_Formats;
	    ImageDescriptor[] t_Images;
	    String[] t_FileNames;
	    Bundle[] t_Bundles = Platform.getBundles("org.eclipse.gef", "3.2"); //$NON-NLS-1$ //$NON-NLS-2$
	    
	      t_FileNames = new  String[2]; 
	      
	      t_FileNames[0]="BMP"; //$NON-NLS-1$
	      t_FileNames[1]="JPG"; //$NON-NLS-1$
	     

	      t_Titles = new  String[2]; 
	      
	      t_Titles[0]="BMP"; //$NON-NLS-1$
	      t_Titles[1]="JPG"; //$NON-NLS-1$
	      
	      t_FileExtensions = new  String[2]; 
	      
	      t_FileExtensions[0]=".bmp"; //$NON-NLS-1$
	      t_FileExtensions[1]="*.jpg,*.jpeg"; //$NON-NLS-1$
	      
	      
	      
	      t_Formats = new int[2]; 
	      
	      t_Formats[0]=0;
	      t_Formats[1]=4;
	      
	      
	      
	      t_Images = new  ImageDescriptor[2]; 
	      
	      t_Images[0]=GefUiPluginImages.IMG_BMP;
	      t_Images[1]=GefUiPluginImages.IMG_JPG;
	      
	    for (int i = 0; i < t_FileExtensions.length; ++i) {
	      String t_FileExtension = t_FileExtensions[i];
	      String t_FileName = t_FileNames[i];
	      String t_Title = t_Titles[i];
	      int t_Format = t_Formats[i];
	      ImageDescriptor t_Image = t_Images[i];

	   //   ExportImageAction t_Action = new ExportImageAction(t_Title, viewer, t_Image, t_FileName, t_FileExtension, t_Format);
	    //  t_Action.run();
	    }
	  }
	 
  
  
}
package com.leven.dmd.pro.exp;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.leven.dmd.pro.Activator;

public class GefUiPluginImages
{

  public static final ImageDescriptor IMG_BMP;
  public static final ImageDescriptor IMG_JPG;

  static
  {

    String exportPath = "icons/tabledata/database.png";

    
    IMG_BMP = PooledImageDescriptor.getImageDescriptor(createImageDescriptor(exportPath));
    IMG_JPG = PooledImageDescriptor.getImageDescriptor(createImageDescriptor(exportPath));

  }

  private static ImageDescriptor createImageDescriptor(String path)
  {
    return AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, path);
  }
}
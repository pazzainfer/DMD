package com.leven.dmd.pro.exp;



import java.io.OutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Display;

public class GefUtil
{
  private static final Set imageFormats = new HashSet();
  public static int ExportLeftSpanValue=0;
  public static int ExportTopSpanValue=0;
  public static int ExportRightSpanValue=20;
  public static int ExportBottomSpanValue=20;

  static
  {
    imageFormats.add(new Integer(0));
    imageFormats.add(new Integer(1));
    imageFormats.add(new Integer(2));
    imageFormats.add(new Integer(2));
    imageFormats.add(new Integer(3));
    imageFormats.add(new Integer(4));
    imageFormats.add(new Integer(5));
    imageFormats.add(new Integer(6));
  }
  public static void exportFigure(IFigure r_Figure, OutputStream r_OutputStream, int r_Format)
  {
    exportFigures(new IFigure[] { 
      r_Figure }, 
      r_OutputStream, r_Format);
  }

  public static void exportFigures(IFigure[] r_Figures, OutputStream r_OutputStream, int r_Format)
  {
    Dimension t_Size = new Dimension();

    for (int i = 0; i < r_Figures.length; ++i) {
      IFigure t_Figure = r_Figures[i];

      t_Size.width = Math.max(t_Size.width, t_Figure.getSize().width);
      t_Size.height = Math.max(t_Size.height, t_Figure.getSize().height);
    }

    t_Size.width = (t_Size.width + ExportLeftSpanValue + ExportRightSpanValue);
    t_Size.height = (t_Size.height + ExportTopSpanValue + ExportBottomSpanValue);

    Display t_Display = Display.getDefault();
    Image t_Image = null;

    GC t_GraphicContext = null;
    SWTGraphics t_Graphics = null;
    ImageLoader t_ImageLoader = null;
    try
    {
      t_Image = new Image(t_Display, t_Size.width, t_Size.height);

      t_GraphicContext = new GC(t_Image);
      t_Graphics = new SWTGraphics(t_GraphicContext);

      for (int i = 0; i < r_Figures.length; ++i) {
        IFigure t_Figure = r_Figures[i];
        Rectangle rectangel = t_Figure.getBounds();
        int dx = -rectangel.x;
        int dy = -rectangel.y;
        t_Graphics.translate(dx, dy);
        t_Figure.paint(t_Graphics);
      }
      
      ImageData[] id = new ImageData[1];
      id[0]=t_Image.getImageData();

      t_ImageLoader = new ImageLoader();
      t_ImageLoader.data = id;

      t_ImageLoader.logicalScreenHeight = t_Size.width;
      t_ImageLoader.logicalScreenHeight = t_Size.height;

      if (imageFormats.contains(new Integer(r_Format))) {
        t_ImageLoader.save(r_OutputStream, r_Format); 
      }

      t_ImageLoader.save(r_OutputStream, 4);
    }
    finally
    {
      SwtResourceUtil.dispose(t_GraphicContext);
      dispose(t_Graphics);
      SwtResourceUtil.dispose(t_Image);
    }
  }

  public static void dispose(SWTGraphics r_Graphics)
  {
    if (r_Graphics != null)
      r_Graphics.dispose();
  }

  public static void exportImage(GraphicalEditor r_Editor, OutputStream r_OutputStream, int r_Format, boolean r_JustSelected)
  {
    GraphicalViewer t_GraphicalViewer = getGraphicalViewer(r_Editor);
    exportImage(t_GraphicalViewer, r_OutputStream, r_Format, r_JustSelected);
  }

  public static void exportImage(GraphicalViewer r_GraphicalViewer, OutputStream r_OutputStream, int r_Format, boolean r_JustSelected)
  {
    RootEditPart t_RootEditPart = r_GraphicalViewer.getRootEditPart();

    List t_SelectionList = r_GraphicalViewer.getSelectedEditParts();

    if ((!(r_JustSelected)) || (t_SelectionList.isEmpty())) {
      IFigure t_Figure = null;
      if (t_RootEditPart instanceof ScalableFreeformRootEditPart) {
        t_Figure = ((ScalableFreeformRootEditPart)t_RootEditPart).getLayer("Printable Layers");
      }
      else
      {
        t_Figure = (IFigure)t_RootEditPart.getAdapter(IFigure.class);
      }

      if (t_Figure != null)
        exportFigure(t_Figure, r_OutputStream, r_Format);

    }
    else
    {
      IFigure[] t_Figures = new IFigure[t_SelectionList.size()];

      for (int i = 0; i < t_SelectionList.size(); ++i) {
        GraphicalEditPart t_EditPart = (GraphicalEditPart)t_SelectionList.get(i);
        t_Figures[i] = t_EditPart.getFigure();
      }

      exportFigures(t_Figures, r_OutputStream, r_Format);
    }
  }

  public static GraphicalViewer getGraphicalViewer(GraphicalEditor r_Editor)
  {
    if (r_Editor == null)
      return null;

    return ((GraphicalViewer)r_Editor.getAdapter(GraphicalViewer.class));
  }
}
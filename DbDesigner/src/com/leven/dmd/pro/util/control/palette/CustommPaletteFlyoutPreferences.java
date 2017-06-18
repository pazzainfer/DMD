package com.leven.dmd.pro.util.control.palette;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite;

import com.leven.dmd.pro.Activator;
import com.leven.dmd.pro.util.control.palette.CustomPaletteComposite.FlyoutPreferences;
/**
 * �Զ����ɫ���������
 * @author lifeng
 * 2012-8-16 ����12:55:09
 */
public class CustommPaletteFlyoutPreferences implements FlyoutPaletteComposite.FlyoutPreferences, FlyoutPreferences {

	public static final int DEFAULT_PALETTE_WIDTH = 50;

	protected static final String PALETTE_DOCK_LOCATION = "Dock location";
	protected static final String PALETTE_SIZE = "Palette Size";
	protected static final String PALETTE_STATE = "Palette state";

	public int getDockLocation() {
		int location = Activator.getDefault().getPreferenceStore()
				.getInt(PALETTE_DOCK_LOCATION);
		if (location == 0) {
			return PositionConstants.EAST;
		}
		return location;
	}

	public int getPaletteState() {
		return FlyoutPaletteComposite.STATE_COLLAPSED;
	}

	public int getPaletteWidth() {
		int width = Activator.getDefault().getPreferenceStore()
				.getInt(PALETTE_SIZE);
		if (width == 0)
			return DEFAULT_PALETTE_WIDTH;
		return width;
	}

	public void setDockLocation(int location) {
		Activator.getDefault().getPreferenceStore()
				.setValue(PALETTE_DOCK_LOCATION, location);
	}

	public void setPaletteState(int state) {
		Activator.getDefault().getPreferenceStore()
				.setValue(PALETTE_STATE, state);
	}

	public void setPaletteWidth(int width) {
		Activator.getDefault().getPreferenceStore()
				.setValue(PALETTE_SIZE, width);
	}

}
package com.leven.dmd.pro.util;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;

import com.leven.dmd.pro.Activator;

public class ImageHelper {
	private ImageRegistry imageRegistry;

	private static ImageHelper getInstance() {
		return SingltionHolder.instance;
	}
	private static class SingltionHolder {
		static ImageHelper instance = new ImageHelper();
	}

	private ImageHelper() {
		this.imageRegistry = new ImageRegistry();
		declareImages();
	}

	public static ImageRegistry getImageRegistry() {
		return getInstance().imageRegistry;
	}

	private final void declareImages() {
		for(String key : ImageKeys.keys){
			defaultDeclareRegistryImage(key,key);
		}
	}

	private void defaultDeclareRegistryImage(String key, String path) {
		try {
			URL url = FileLocator.find(Platform.getBundle(Activator.PLUGIN_ID),
					new Path(path), null);
			if (url != null) {
				ImageDescriptor image = ImageDescriptor.createFromURL(url);
				this.imageRegistry.put(key, image);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Image getImage(String key) {
		return getImageRegistry().get(key);
	}

	public static ImageDescriptor getImageDescriptor(String key) {
		return getImageRegistry().getDescriptor(key);
	}

	public static ImageDescriptor getImageDescriptorByPath(String path) {
		return Activator.getImageDescriptor(path);
	}
}

package com.leven.dmd.pro.exp;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class ImageRepository {

	private final Map images = new HashMap();
	private String bundleName;
	private static final Map repositories = new HashMap();

	public static synchronized ImageRepository getImageRepository(
			String r_BundleName) {
		ImageRepository t_Repository;
		Object t_Object = repositories.get(r_BundleName);

		if (t_Object == null) {
			t_Repository = new ImageRepository();
			t_Repository.bundleName = r_BundleName;
			repositories.put(r_BundleName, t_Repository);
		} else {
			t_Repository = (ImageRepository) t_Object;
		}

		return t_Repository;
	}

	public static synchronized ImageRepository removeImageRepository(
			String r_BundleName) {
		return ((ImageRepository) repositories.remove(r_BundleName));
	}

	public PooledImageDescriptor getImageDescriptor(String r_ImageName) {
		ImageDescriptor t_ImageDescriptor = (ImageDescriptor) this.images
				.get(r_ImageName);

		if (t_ImageDescriptor == null)
			try {
				t_ImageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin(
						this.bundleName, r_ImageName);

				if (ImageDescriptor.getMissingImageDescriptor().equals(
						t_ImageDescriptor)) {
					return null;
				}

				t_ImageDescriptor = PooledImageDescriptor
						.getImageDescriptor(t_ImageDescriptor);
				this.images.put(r_ImageName, t_ImageDescriptor);
			} catch (Exception localException) {
				return null;
			}

		return ((PooledImageDescriptor) t_ImageDescriptor);
	}

	public void setImageDescriptor(String r_ImageName,
			ImageDescriptor r_ImageDescriptor) {
		if ((r_ImageDescriptor != null) && (r_ImageName != null))
			if (r_ImageDescriptor instanceof PooledImageDescriptor) {
				this.images.put(r_ImageName, r_ImageDescriptor);
			} else
				this.images.put(r_ImageName, PooledImageDescriptor
						.getImageDescriptor(r_ImageDescriptor));
	}

	public String getBundleName() {
		return this.bundleName;
	}

	public static PooledImageDescriptor getImageDescriptor(String r_BundleName,
			String r_ImageName) {
		ImageRepository t_ImageRepository = getImageRepository(r_BundleName);
		return t_ImageRepository.getImageDescriptor(r_ImageName);
	}

	public static Image getImage(String r_BundleName, String r_ImageName) {
		ImageRepository t_ImageRepository = getImageRepository(r_BundleName);
		ImageDescriptor t_ImageDescriptor = t_ImageRepository
				.getImageDescriptor(r_ImageName);
		return t_ImageDescriptor.createImage();
	}
}
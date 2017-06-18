package com.leven.dmd.pro.exp;

import java.util.Hashtable;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;

public class PooledImageDescriptor extends ImageDescriptor {
	private Image image;
	private ImageDescriptor imageDescriptor;
	private static final Hashtable descriptorPools = new Hashtable();
	private static final Hashtable imagePools = new Hashtable();

	public static PooledImageDescriptor getImageDescriptor(
			ImageDescriptor r_ImageDescriptor) {
		if (r_ImageDescriptor == null) {
			return null;
		}

		ImageDescriptor t_RealImageDescriptor = r_ImageDescriptor;
		while (t_RealImageDescriptor instanceof PooledImageDescriptor) {
			t_RealImageDescriptor = ((PooledImageDescriptor) t_RealImageDescriptor).imageDescriptor;
		}

		Object t_Object = descriptorPools.get(t_RealImageDescriptor);
		if (t_Object != null) {
			return ((PooledImageDescriptor) t_Object);
		}

		PooledImageDescriptor t_ImageDescriptor = new PooledImageDescriptor(
				t_RealImageDescriptor);
		descriptorPools.put(t_RealImageDescriptor, t_ImageDescriptor);
		return t_ImageDescriptor;
	}

	public static PooledImageDescriptor getImageDescriptor(Image r_Image) {
		if (r_Image == null) {
			return null;
		}

		Object t_Object = imagePools.get(r_Image);
		if (t_Object != null) {
			return ((PooledImageDescriptor) t_Object);
		}

		ImageDescriptor t_ImageDescriptor = ImageDescriptor
				.createFromImage(r_Image);
		PooledImageDescriptor t_PooledImageDescriptor = new PooledImageDescriptor(
				t_ImageDescriptor);
		t_PooledImageDescriptor.image = r_Image;
		imagePools.put(r_Image, t_PooledImageDescriptor);
		return t_PooledImageDescriptor;
	}

	private PooledImageDescriptor(ImageDescriptor r_ImageDescriptor) {
		this.imageDescriptor = r_ImageDescriptor;
		Assert.isNotNull(r_ImageDescriptor, "");
	}

	public Image createImage(boolean r_ReturnMissingImageOnError,
			Device r_Device) {
		if ((this.image != null) && (!(this.image.isDisposed()))) {
			return this.image;
		}

		this.image = super.createImage(r_ReturnMissingImageOnError, r_Device);
		return this.image;
	}

	public ImageData getImageData() {
		return this.imageDescriptor.getImageData();
	}

	public int hashCode() {
		int result = 1;
		result = 31
				* result
				+ ((this.imageDescriptor == null) ? 0 : this.imageDescriptor
						.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (obj == null)
			return false;

		if (getClass() != obj.getClass())
			return false;

		PooledImageDescriptor other = (PooledImageDescriptor) obj;
		return other.imageDescriptor.equals(this.imageDescriptor);
	}

	public ImageDescriptor getImageDescriptor() {
		return this.imageDescriptor;
	}
}
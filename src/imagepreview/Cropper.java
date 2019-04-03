package imagepreview;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Cropper extends javax.swing.JFrame {

	private static final long serialVersionUID = 3301142609530060479L;

	public void printImagePreview(File file) {	
		try {
			BufferedImage r = crop(resize(file));
			String userHomeFolder = System.getProperty("user.home")+"\\Desktop";
			File output = new File(userHomeFolder, "(new!) "+file.getName());

			if (!output.exists()) {
				output.createNewFile();
			}

			ImageIO.write(r, "png", output);
		} catch (IOException e) {
			System.out.println(e);
		}
	}
	
	public BufferedImage resize(File file) {
		try {
			return getScaledInstance(ImageIO.read(file), 40, 40, true);
		} catch (IOException e) {
			System.out.println(e);
			return null;
		}

	}
	
	public void resizeAndPrint(File file) {
		try {
			File output = new File("resized.png");

			if (!output.exists()) {
				output.createNewFile();
			}

			ImageIO.write(resize(file), "png", output);
		} catch (IOException e) {
			System.out.println(e);
		}
	}
	
	public BufferedImage crop(File file) {
		try {
			int x = 20;
			int y = 20;
			int radius = 20;
			int margin = 5;
			
			BufferedImage bimg = ImageIO.read(file);
			BufferedImage bi = new BufferedImage(2*radius+(2*margin), 2*radius+(2*margin), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = bi.createGraphics();
			g2.setPaint ( new Color (54,57,66) );
			g2.fillRect ( 0, 0, bi.getWidth(), bi.getHeight() );
			g2.translate(bi.getWidth()/2, bi.getHeight()/2);
			Arc2D myarea = new Arc2D.Float(0-radius, 0-radius, 2*radius, 2*radius, 0, -360, Arc2D.OPEN);
			
			AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.CLEAR, 0f);	
			g2.setComposite(composite);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
			g2.setClip(myarea);
			g2.drawImage(bimg.getSubimage(x-radius, y-radius, x+radius, y+radius), -radius, -radius, this);
			
			return bi;
			
			} catch (Exception e) {
				System.out.println(e);
				return null;
			}
	}
	
	public BufferedImage crop(BufferedImage bimg) {
		try {
			int x = 20;
			int y = 20;
			int radius = 20;
			int margin = 5;

			BufferedImage bi = new BufferedImage(2*radius+(2*margin), 2*radius+(2*margin), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = bi.createGraphics();
			g2.setPaint ( new Color (54,57,66) );
			g2.fillRect ( 0, 0, bi.getWidth(), bi.getHeight() );
			g2.translate(bi.getWidth()/2, bi.getHeight()/2);
			
			Arc2D myarea = new Arc2D.Float(0-radius, 0-radius, 2*radius, 2*radius, 0, -360, Arc2D.OPEN);
			
			AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.CLEAR, 0f);
			g2.setComposite(composite);
			g2.setBackground(new Color(54,57,66));
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
			g2.setClip(myarea);
			g2.drawImage(bimg.getSubimage(x-radius, y-radius, x+radius, y+radius), -radius, -radius, this);
			
			return bi;
			
			} catch (Exception e) {
				System.out.println(e);
				return null;
			}
	}
	
	public void cropAndPrint(File file) {
		try {		
			File output = new File("cropped.png");
			
			if (!output.exists()) {
				output.createNewFile();
			}
			
			ImageIO.write(crop(file), "png", output);
			} catch (Exception e) {
				System.out.println(e);
			}
	}
	
	public void cropAndPrint(BufferedImage img) {
		try {		
			File output = new File("cropped.png");
			
			if (!output.exists()) {
				output.createNewFile();
			}
			
			ImageIO.write(crop(img), "png", output);
			} catch (Exception e) {
				System.out.println(e);
			}
	}

	private BufferedImage getScaledInstance(BufferedImage img, int targetWidth, int targetHeight,
			boolean higherQuality) {
		int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB
				: BufferedImage.TYPE_INT_ARGB;
		BufferedImage ret = (BufferedImage) img;
		int w, h;
		if (higherQuality) {
			// Use multi-step technique: start with original size, then
			// scale down in multiple passes with drawImage()
			// until the target size is reached
			w = img.getWidth();
			h = img.getHeight();
		} else {
			// Use one-step technique: scale directly from original
			// size to target size with a single drawImage() call
			w = targetWidth;
			h = targetHeight;
		}

		do {
			if (higherQuality && w > targetWidth) {
				w /= 2.5;
				if (w < targetWidth) {
					w = targetWidth;
				}
			}

			if (higherQuality && h > targetHeight) {
				h /= 2.5;
				if (h < targetHeight) {
					h = targetHeight;
				}
			}

			BufferedImage tmp = new BufferedImage(w, h, type);
			Graphics2D g2 = tmp.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.drawImage(ret, 0, 0, w, h, null);
			g2.dispose();

			ret = tmp;
		} while (w != targetWidth || h != targetHeight);

		return ret;
	}
}

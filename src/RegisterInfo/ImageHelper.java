package RegisterInfo;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import processing.core.PImage;

public class ImageHelper {

	private static File path = new File("");
	
	public PImage button_smart;
	public PImage button_social;
	public PImage button_smart_onMouse;
	public PImage button_social_onMouse;
	public PImage check1;
	public PImage check2;
	public PImage check3;
	public PImage check4;

	public PImage background;

	ImageHelper() {

		button_smart = imageReader("Button_smart.png");
		button_social = imageReader("Button_social.png");
		button_smart_onMouse = imageReader("Button_smart_onMouse.png");
		button_social_onMouse = imageReader("Button_social_onMouse.png");

		check1 = imageReader("check1.png");
		check2 = imageReader("check2.png");
		check3 = imageReader("check3.png");
		check4 = imageReader("check4.png");

		background = imageReader("background.png");

	}

	private PImage imageReader(String fileName) {

		Image img = null;
		try {
			File sourceimage = new File(path.getAbsolutePath() + "\\image\\" + fileName);
			img = ImageIO.read(sourceimage);

		} catch (IOException e) {
		}

		PImage image = new PImage(img);
		return image;
	}
}

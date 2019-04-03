package imagepreview;

import java.io.File;


public class Main {

	public static void main(String[] args) {
		if (args.length!=1) {
			System.out.println("Program requires exactly 1 argument:");
			System.out.println("- Full path to image file");
		} else {
			File file = new File(args[0]);
			System.out.println(args[0]);
			new Cropper().printImagePreview(file);
		}
	}

}

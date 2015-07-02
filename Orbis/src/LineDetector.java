
import static imageProcessors.IPOP.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class LineDetector {

	public static void main(String[] args) {
		BufferedImage image = null;
		try {
			if (args.length > 0)
				image = ImageIO.read(new File(args[0]));
			else
				return;
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		
		find(Integer.parseInt(args[1]), Integer.parseInt(args[2]), image, Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]));
	}
	
	public static void find(int ocs, int ocsTol, int[] image, int w, int h, int colour, int line, int tolerence) {
		int o = occurrences(image, w, h, colour, tolerence, line);
		if ((o <= ocs + ocsTol) && (o >= ocs - ocsTol)) {
			
		}
	}
	
	public static void find(int ocs, int ocsTol, BufferedImage image, int colour, int line, int tolerence) {
		find(ocs, ocsTol, bufferedImageToArray(image), image.getWidth(), image.getHeight(), colour, line, tolerence);
	}

}

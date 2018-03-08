import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Base64;
import java.util.Base64.Decoder;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

public class test
{
    public static void main( String[] args ) throws IOException {	
    	try {

			byte[] imageInByte;
			BufferedImage originalImage = ImageIO.read(new File(
					"res/image.png"));
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(originalImage, "png", baos);
			baos.flush();
			imageInByte = baos.toByteArray();
			String str = DatatypeConverter.printBase64Binary(imageInByte);
			System.out.println(str);
			baos.close();
			
			File f = new File("res/tempImage.txt");
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			bw.write(str);
			bw.close();
			
			byte[] s = DatatypeConverter.parseBase64Binary(str);
			 
			BufferedImage imag=ImageIO.read(new ByteArrayInputStream(s));
			ImageIO.write(imag, "png", new File("res/snap.png"));

	
		    System.out.println("image created");
		    

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
    	
}

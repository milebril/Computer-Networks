import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;

import java.awt.Image;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.FilterWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class test
{
    public static void main( String[] args ) throws IOException {	
    	try {

			byte[] imageInByte;
			BufferedImage originalImage = ImageIO.read(new File(
					"res/black.jpg"));
			//System.out.println(originalImage);
			// convert BufferedImage to byte array
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(originalImage, "jpg", baos);
			baos.flush();
			imageInByte = baos.toByteArray();
			String str = new String(imageInByte, "UTF-8");
			System.out.println(str);
			baos.close();
			
			File f = new File("res/tempImage.txt");
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			bw.write(str);
			bw.close();

			// convert byte array back to BufferedImage
			//InputStream in = new FileInputStream(f);
			//ByteArrayInputStream bais = new ByteArrayInputStream();
			//BufferedImage img = ImageIO.read(new ByteArrayInputStream(data));
//			
//			byte[] data = Files.readAllBytes(Paths.get("res/tempImage.txt"));
//			InputStream in = new ByteArrayInputStream(data);
//			BufferedImage bImageFromConvert = ImageIO.read(in);
//			ImageIO.write(bImageFromConvert, "png", new File("res/ouput.png"));
			
//			ByteArrayInputStream bis = new ByteArrayInputStream(data);
//		    BufferedImage bImage2 = ImageIO.read(bis);
//		    ImageIO.write(bImage2, "png", new File("res/output.png") );
		    System.out.println("image created");
			
//			BufferedImage bImageFromConvert = ImageIO.read(data);
//
//			ImageIO.write(img, "PNG", new File(
//					"res/new-darksouls.png"));
		    
		    byte[] data = Files.readAllBytes(Paths.get("res/tempImage.txt"));
	        ByteArrayInputStream bis = new ByteArrayInputStream(data);
	        Iterator<?> readers = ImageIO.getImageReadersByFormatName("jpg");
	 
	        //ImageIO is a class containing static methods for locating ImageReaders
	        //and ImageWriters, and performing simple encoding and decoding. 
	 
	        ImageReader reader = (ImageReader) readers.next();
	        Object source = bis; 
	        ImageInputStream iis = ImageIO.createImageInputStream(source); 
	        reader.setInput(iis, true);
	        ImageReadParam param = reader.getDefaultReadParam();
	 
	        Image image = reader.read(0, param);
	        //got an image file
	 
	        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
	        //bufferedImage is the RenderedImage to be written
//	 
//	        Graphics2D g2 = bufferedImage.createGraphics();
//	        g2.drawImage(image, null, null);
	 
	        File imageFile = new File("res/newrose2.jpg");
	        ImageIO.write(bufferedImage, "jpg", imageFile);
		    
		    

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
    	
}

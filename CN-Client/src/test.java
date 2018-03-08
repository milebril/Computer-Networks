import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;

import java.awt.Image;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class test
{
    public static void main( String[] args )
    {
        try {
        	File input = new File("res/Internationale Vrouwendag 2018.html");
        	File pic = new File("res/Internationale Vrouwendag 2018.png");
        	BufferedImage image = ImageIO.read(input); 
        	ImageIO.write(image, "PNG", pic);
        	
        } catch (IOException e) {
        	e.printStackTrace();
        }
    }
}

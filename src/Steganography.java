import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.awt.Color;
import java.util.*;


public class Steganography {

	public static void main(String[] args) throws IOException {
		//for jpeg cases
		
		BufferedImage img = null;
		String flag = args[0];
		String imageFileName = args[1];
		String fileName = args[2] + ".txt";		
		
        try {
            img = ImageIO.read(new File(imageFileName));
        } catch (IOException e) {

        }
        
        
        int height = img.getHeight();
        int width = img.getWidth();

        // This prints the image height and width and a specific pixel. 

        System.out.println(imageFileName + " " + (height * width) + " " + 
        height  + "  " +  width);

        if (flag.equalsIgnoreCase("-e")) {
        	encode(img, height, width, fileName, imageFileName);
        }
        else if (flag.equalsIgnoreCase("-d")){
        	decode(img, height, width, fileName);
        }


  
	}
	
	public static void encode(BufferedImage img, int height, int width, String fileName, String imageFileName){
		File input = new File(fileName);
		try{
			FileInputStream fs = new FileInputStream(input);
			int nBytes = fs.available();
			int h = 0, w = 0;
			
			while(nBytes > 0 && h < height) {
				// read in 3 bytes
				byte[] bytes = new byte[3];
				try {
					fs.read(bytes);
					//System.out.println("bytes is: " + new String(bytes));
				} catch (IOException e) {
					e.printStackTrace();
				}				
				
				// convert the bytes to a bitset
				BitSet bits = new BitSet(24);
				bits = bits.valueOf(bytes);
				//System.out.println(bits);		
				
				// iterate through our bitset 3 bits at a time
				for (int i = 0; i < 24 && h < height; i += 3) {
					// if we reach the end of a line of pixels,
					// reset w and go to the next line
					if (w >= width ) {
						
						w = 0;
						++h;
					}				
					if(h < height){
						BitSet rgbVals = bits.get(i, i + 3);
						//System.out.println(rgbVals);
						boolean newRBit = rgbVals.get(0);
						boolean newGBit = rgbVals.get(1);
						boolean newBBit = rgbVals.get(2);
						Color pixel = new Color(img.getRGB(w, h));					
						int rVal, gVal, bVal;
						rVal = pixel.getRed();
						gVal = pixel.getGreen();
						bVal = pixel.getBlue();
						
						// red
						if(newRBit && rVal % 2 == 0) 		//if rVal is currently 0 and needs to b 1
							++rVal; 						//set rVal to 1
						else if(!newRBit && rVal % 2 != 0)	//else if rVal is currently 1 and needs to be 0
							--rVal;							//set rVal to 0					
						
						// green
						if(newGBit && gVal % 2 == 0)		//if gVal is currently 0 and needs to be 1
							++gVal; 						//set gVal to 1
						else if(!newGBit && gVal % 2 != 0)	//else if gVal is currently 1 and needs to be 0
							--gVal;							//set gVal to 0
						
						// blue
						if(newBBit && bVal % 2 == 0) 		//if bVal is currently 0 and needs to be 1
							++bVal; 						//set bVal to 1
						else if(!newBBit && bVal % 2 != 0)	//else if bVal is currently 1 and needs to be 0
							--bVal; 						//set bVal to 0
						
						// create and set our new pixel with the modified bits
						int newVal = (rVal << 16) + (gVal << 8) + bVal;
						img.setRGB(w, h, newVal);
						
						++w;
					}
				}

				nBytes -= 3;
			}
			
			// if the message is truncated, print a message
			if (nBytes > 0)
				System.out.println("The message has been truncated");
			// else, set the null byte when finished
			else {
				for(int i = 0; i < 3; ++i){
					Color nullPixel = new Color(img.getRGB(w, h));
					// if a rgb value ends in a 1, set it a 0
					int rVal = (nullPixel.getRed() % 2 != 0) 	? nullPixel.getRed() - 1 	: nullPixel.getRed();
					int gVal = (nullPixel.getGreen() % 2 != 0) ? nullPixel.getGreen() - 1 : nullPixel.getGreen();
					int bVal = (nullPixel.getBlue() % 2 != 0) 	? nullPixel.getBlue() - 1 	: nullPixel.getBlue();
					
					// create and set our new null pixel
					int newVal = (rVal << 16) + (gVal << 8) + bVal;
					img.setRGB(w + i, h, newVal);
				}
			}
			
			fs.close();
			
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException f) {
			f.printStackTrace();
		}
		
		//write out the new image
		try{
			String[] split = imageFileName.split("\\.");
			File outputfile = new File(split[0] + "-steg." + split[1]);
			ImageIO.write(img,  split[1], outputfile);

		}catch(IOException q){
			q.printStackTrace();
		}
	}
	
	public static void decode(BufferedImage img, int height, int width, String fileName)
			throws IOException {
		int h = 0, w = 0;
		int bitCounter = 0;
		File outfile = new File(fileName + ".txt");
		FileWriter fw = new FileWriter(outfile);
		BitSet outputBits = new BitSet(24);
		//System.out.println("image dimensions: " + img.getWidth() + " " + img.getHeight());

		boolean foundEOF = false;
		
		// iterate through the pixels
		while (h < height && !foundEOF) {
			// get the pixel and its rgb values
	        Color pColor = new Color(img.getRGB(w, h));	
	        boolean rVal = false, gVal = false, bVal = false;
	        if(pColor.getRed() %2 != 0){
	        	rVal = true;
	        }
	        if(pColor.getGreen() %2 != 0){
	        	gVal = true;
	        }
	        if(pColor.getBlue() %2 != 0){
	        	bVal = true;
	        }
			
			// set the bits to the values taken from rgb
	        outputBits.set(bitCounter, rVal);
			outputBits.set(bitCounter + 1, gVal);
			outputBits.set(bitCounter + 2, bVal);
			
			bitCounter += 3;
			++w;
			
			// if we reach the end of a line of pixels,
			// reset w and go to the next line
			if (w >= width) {
				w = 0;
				++h;
			}
			
			
			if (bitCounter >= 24) {
				
				BitSet byte1 = outputBits.get(0, 8);
				BitSet byte2 = outputBits.get(8, 16);
				BitSet byte3 = outputBits.get(16, 24);
				
				if (byte1.isEmpty()){
					foundEOF = true;
				} else if(byte2.isEmpty()) {
					String str = new String(outputBits.get(0, 8).toByteArray(), "UTF-8");
					fw.write(str);
					foundEOF = true;
				} else if (byte3.isEmpty()){
					String str = new String(outputBits.get(0, 16).toByteArray(), "UTF-8");
					fw.write(str);
					foundEOF = true;
				} else {
					bitCounter = 0;
					String str = new String(outputBits.toByteArray(), "UTF-8");
					//System.out.println("string is: " + str);
					fw.write(str);
					//byte[] end = outputBits.toByteArray();
					
					outputBits.clear();
				}
			}
		}
		
		fw.close();
		
	}
	


}

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Scanner;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.awt.Color;
import java.awt.color.*;
import java.util.BitSet;


public class Steganography {

	public static void main(String[] args) throws IOException {
		BufferedImage img = null;
		String flag = args[0];
		String imageFileName = args[1];
		String file = args[2] + ".txt";		
		
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
        	encode(img, height, width, file);
        }
        else if (flag.equalsIgnoreCase("-d")){
        	decode(img, height, width, file);
        }


  
	}
	
	public static void encode(BufferedImage img, int height, int width, String file){
		File input = new File(file);;
		try{
			FileInputStream fs = new FileInputStream(input);
			int nBytes = fs.available();
			int h = 0, w = 0;
			
			while(nBytes > 0 && h < height) {
				// read in 3 bytes
				byte[] bytes = new byte[3];
				try {
					fs.read(bytes);
					System.out.println("bytes is: " + new String(bytes));
				} catch (IOException e) {
					e.printStackTrace();
				}				
				// convert the bytes to a bitset
				BitSet bits = new BitSet(24);
				BitSet inputBits = bits.valueOf(bytes);
				System.out.println(inputBits);		
				// iterate through our bitset 3 bits at a time
				for (int i = 0; i < 24 && h < height; i += 3) {
					if (w >= width) {
						w = 0;
						++h;
					}					
					BitSet rgbVals = inputBits.get(i, i + 2);
					boolean newR = rgbVals.get(0);
					boolean newG = rgbVals.get(1);
					boolean newB = rgbVals.get(2);
					Color pColor = new Color(img.getRGB(w, h));					
					int rVal, gVal, bVal;
					rVal = pColor.getRed();
					gVal = pColor.getGreen();
					bVal = pColor.getBlue();
					System.out.println(rgbVals.get(0) + " " + rgbVals.get(1) + " " + rgbVals.get(3));
					if(newR){	//r value needs to be odd
						if(rVal % 2 == 0){	//if rVal is currently even
							//set rVal to odd
							++rVal;
						}
					}else{	//rVal needs to be even
						if(rVal % 2 != 0){	//if rVal is currently odd
							//set rVal to even
							--rVal;
						}						
					}
					//green
					if(newG){	//r value needs to be odd
						if(gVal % 2 == 0){	//if rVal is currently even
							//set rVal to odd
							++gVal;
						}
					}else{	//rVal needs to be even
						if(gVal % 2 != 0){	//if rVal is currently odd
							//set rVal to even
							--gVal;
						}
					}				
					//blue
					if(newB){	//r value needs to be odd
						if(bVal % 2 == 0){	//if rVal is currently even
							//set rVal to odd
							++bVal;
						}
					}else{	//rVal needs to be even
						if(bVal % 2 != 0){	//if rVal is currently odd
							//set rVal to even
							--bVal;
						}
					}
					//System.out.println(pColor);
					int newVal = (rVal << 16) + (gVal << 8) + bVal;
					//System.out.println("Alterations : [r: " + rVal + ", g: "
							//+ gVal + ", b: " + bVal + "]");
					img.setRGB(w, h, newVal);
					System.out.println(pColor);
					System.out.println(rVal + " " + gVal + " " + bVal);
					++w;
				}

				nBytes -= 3;
			}
		
			img.setRGB(w, h, 0);
			img.setRGB(w + 1, h, 0);
			img.setRGB(w + 2, h, 0);
			
			fs.close();
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException f) {
			f.printStackTrace();
		}
		//write out the new image
		try{
			File outputfile = new File("out-image.png");
			ImageIO.write(img,  "png", outputfile);
		}catch(IOException q){
			q.printStackTrace();
		}
	}
	
	public static void decode(BufferedImage img, int height, int width, String file)
			throws IOException {
		int h = 0, w = 0;
		int bitCounter = 0;
		File outfile = new File("out.txt");
		FileOutputStream fo = null;
		//fo = new FileOutputStream(outfile);
		FileWriter fw = new FileWriter(outfile);
		//byte[] outputBytes = new byte[3];
		BitSet outputBits = new BitSet(24);
		System.out.println("image dimensions: " + img.getWidth() + " " + img.getHeight());
//		try {
//			
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
		
		int rMask = 0x10000;
		int gMask = 0x100;
		int bMask = 0x1;
		boolean foundEOF = false;
		
		// iterate through the pixels
		while (h < height && !foundEOF) {
			//System.out.println("h: " + h + " w: " + w);
			System.out.println("no");
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
//	        if ((pColor.getRed() & rMask) != 0) rVal = true;
//	        if ((pColor.getGreen() & gMask) != 0) gVal = true;
//	        if ((pColor.getBlue() & bMask) != 0) bVal = true;
			System.out.println(rVal + " " + gVal + " " + bVal);
			outputBits.set(24 - bitCounter + 2, rVal);
			outputBits.set(24 - bitCounter + 1, gVal);
			outputBits.set(24 - bitCounter + 0, bVal);
			
			bitCounter += 3;
			++w;
			
			if (w >= width) {
				w = 0;
				++h;
			}
			if (bitCounter >= 24) {
				System.out.println("ok!");
				if (outputBits.isEmpty()){
					System.out.println("whom");
					foundEOF = true;
				}
				else {
					bitCounter = 0;
					String str = new String(outputBits.toByteArray(), "UTF-8");
					//System.out.println("string is: " + str);
					fw.write(str);
					//byte[] end = outputBits.toByteArray();
					
					outputBits.clear();
				}
			}
		}
		
	}
	


}

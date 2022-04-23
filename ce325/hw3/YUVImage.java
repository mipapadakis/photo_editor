/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ce325.hw3;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class YUVImage{
	private int width;
	private int height;
	private YUVPixel array[][];
	
	public YUVImage(int width, int height){
		this.width = width;
		this.height = height;
		array = new YUVPixel[height][width];
		
		for(int i=0; i<height; i++){
			for(int j=0; j<width; j++){
				array[i][j] = new YUVPixel((short)16, (short)128, (short)128);
			}
		}
	}
	
	public YUVImage(YUVImage copyImg){
		width = copyImg.getWidth();
		height = copyImg.getHeight();
		array = new YUVPixel[height][width];
		
		for(int i=0; i<height; i++) {
			for(int j=0; j<width; j++) {
				array[i][j] = new YUVPixel(copyImg.getPixel(i,j));
			}
		}
	}
	
	public YUVImage(RGBImage RGBImg){
		width = RGBImg.getWidth();
		height = RGBImg.getHeight();
		array = new YUVPixel[height][width];
		
		for(int i=0; i<height; i++) {
			for(int j=0; j<width; j++) {
				array[i][j] = new YUVPixel(RGBImg.getPixel(i,j)); //Convert RGB to YUV
			}
		}
	}
	
	public YUVImage(java.io.File file) throws UnsupportedFileFormatException, FileNotFoundException{
		
		try(Scanner sc = new Scanner(file)){
			int row=0, col=0;
			String temp[] = new String[3];
			
			System.out.print("Creating YUV image from file... ");
			temp[0] = sc.next(); //MagicNumber
			temp[1] = sc.next(); //Width
			temp[2] = sc.next(); //Height
			
			if( !temp[0].equals("YUV3")){
				throw new UnsupportedFileFormatException("Incompatible Magic Number!");
			}
			else{
				if( !isInteger(temp[1]) || !isInteger(temp[2]) ){
					System.err.println("The file contains wrong data!");
					System.exit(0);
				}
				width = Integer.parseInt(temp[1]);
				height = Integer.parseInt(temp[2]);

				if(width<=0){
					System.err.println("Can't have zero columns!");
					System.exit(0);
				}
				if(height<=0){
					System.err.println("Can't have zero rows!");
					System.exit(0);
				}
				array = new YUVPixel[height][width];
				while(sc.hasNext()){
					//Get the next three numbers of the file (in string form)
					temp[0] = sc.next(); //Y
					temp[1] = sc.next(); //U
					temp[2] = sc.next(); //V

					if( !isInteger(temp[0]) || !isInteger(temp[1]) || !isInteger(temp[2]) ){
						System.err.println("The file contains wrong data!");
						System.exit(0);
					}

					if(col == width){ //Reached the end of the row!
						col=0;
						row++;
					}
					array[row][col] = new YUVPixel(Short.parseShort(temp[0]), Short.parseShort(temp[1]), Short.parseShort(temp[2]));
					col++;
				}
				System.out.println("Done.");
			}
		}
		catch(NoSuchElementException ex){
			//In case we tried to sc.next while sc.hasNext==0
			throw new UnsupportedFileFormatException("Wrong data!");
		}
	}
	
	public int getWidth(){ return(width); }
	public int getHeight(){ return(height); }
	public YUVPixel getPixel(int row, int col){
		if(row>=height || col>=width || row<0 || col<0 ){
			System.out.println("Wrong dimensions!");
			System.exit(0);
		}
		return(array[row][col]);
	}
	
	public void setWidth(int width){
		this.width = width;
		array = new YUVPixel[height][width];
	}
	public void setHeight(int height){
		this.height = height;
		array = new YUVPixel[height][width];
	}
	public void setPixel(int row, int col, YUVPixel pixel){
		if(row>=height || col>=width || row<0 || col<0 ){
			System.out.println("Wrong dimensions!");
			System.exit(0);
		}
		if(array.length == 0){
			//If we've called super(0,0,0) for initialisation, the array has been created with zero cols and zero rows. Avoid that case:
			array = new YUVPixel[height][width];
		}
		array[row][col] = pixel;
	}
	public void setPixel(int row, int col, RGBPixel pixel){
		setPixel(row, col, new YUVPixel(pixel)); //Convert the RGB pixel to YUV
	}
	
	@Override
	public String toString(){
		StringBuilder yuv = new StringBuilder(String.format("YUV3\n%d %d\n",getWidth(),getHeight()) );
		for(int i=0; i<getHeight(); i++){
			for(int j=0; j<getWidth(); j++){
				yuv.append( String.format("%d %d %d\n",(int) getPixel(i, j).getY(),(int) getPixel(i, j).getU(),(int) getPixel(i, j).getV()) );
			}
		}
		return(yuv.toString());
	}
	
	public void toFile(java.io.File file){
		System.out.print("Creating YUV file...\t");
		try(java.io.DataOutputStream output = new java.io.DataOutputStream(new FileOutputStream(file)) ){
			output.writeBytes(toString());
			output.close();
			System.out.println("Done.");
		}
		catch(IOException ex){
			System.err.println("Could't write to file!");
		}
	}
	
	public void equalize(){
		Histogram hist = new Histogram(this);
		System.out.println("Current histogram:\n" + hist.toString());
		hist.equalize();
		System.out.print("Equalizing image... ");
		for(int i=0; i<getHeight(); i++){
			for(int j=0; j<getWidth(); j++){
				array[i][j].setY( hist.getEqualisedLuminocity(array[i][j].getY()) );
			}
		}
		System.out.println("Done. New histogram:\n" + hist.toString());
	}
	
	//Check if the string contains a number of type Integer
	public static boolean isInteger(String str){
		try{
			Integer.parseInt(str);
			return true;
		}
		catch(NumberFormatException ex){
			return false;
		}
	}
}

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


public class PPMImage extends RGBImage{
	
	public PPMImage(java.io.File file) throws UnsupportedFileFormatException, FileNotFoundException{
		super(0,0,0);
		try(Scanner sc = new Scanner(file)){
			int width, height, colordepth, row=0, col=0;
			String temp[] = new String[3];
			
			System.out.print("Creating PPM image from file... ");
			temp[0] = sc.next();
			//Read the magic Number
			if( !temp[0].equals("P3")){
				throw new UnsupportedFileFormatException("Incompatible Magic Number!");
			}
			width = Integer.parseInt(sc.next());
			height = Integer.parseInt(sc.next());
			colordepth = Integer.parseInt(sc.next());
			
			//The first three numbers (after the Magic Number) are the width, height, and colordepth.
			if(width<=0){
				System.err.println("Can't have zero columns!");
				System.exit(0);
			}
			if(height<=0){
				System.err.println("Can't have zero rows!");
				System.exit(0);
			}
			setWidth(width);
			setHeight(height);
			setColordepth(colordepth);
			
			while(sc.hasNext()){
				//get the next three numbers of the file (in string form)
				temp[0] = sc.next(); //R
				temp[1] = sc.next(); //G
				temp[2] = sc.next(); //B
				
				if( !isInteger(temp[0]) || !isInteger(temp[1]) || !isInteger(temp[2]) ){
					System.err.println("The file contains wrong data!");
					System.exit(0);
				}
				
				//Create each pixel:
				if( Integer.parseInt(temp[0]) > colordepth || Integer.parseInt(temp[1]) > colordepth || Integer.parseInt(temp[2]) > colordepth ){
					System.err.println("Found RGB value higher than colordepth.");
					System.exit(0);
				}
				else if( Integer.parseInt(temp[0]) < 0 || Integer.parseInt(temp[1]) < 0 || Integer.parseInt(temp[2]) < 0){
					System.err.println("Found negative RGB value.");
					System.exit(0);
				}
				else{
					if(col == width){ //Reached the end of the row!
						col=0;
						row++;
					}
					setPixel( row, col, new RGBPixel(Short.parseShort(temp[0]), Short.parseShort(temp[1]), Short.parseShort(temp[2])) );
					col++;
				}
			}
			System.out.println("Done.");
		}
		catch(NoSuchElementException ex){
			//In case we tried to sc.next while sc.hasNext==0
			throw new UnsupportedFileFormatException("Wrong data!");
		}
	}
	
	public PPMImage(RGBImage img){
		super(img.getWidth(), img.getHeight(), img.getColordepth());
		
		for(int i=0; i<getHeight(); i++){
			for(int j=0; j<getWidth(); j++){
				setPixel( i, j, new RGBPixel(img.getPixel(i,j)));
			}
		}
	}
	
	public PPMImage(YUVImage img){
		super(img.getWidth(), img.getHeight(), 255);
		
		for(int i=0; i<getHeight(); i++){
			for(int j=0; j<getWidth(); j++){
				setPixel(i, j, new RGBPixel(img.getPixel(i,j))); //Convert each YUV pixel to RGB pixel
			}
		}
	}
	
	@Override
	public String toString(){
		StringBuilder ppm = new StringBuilder(String.format("P3\n%d %d %d\n",getWidth(),getHeight(),getColordepth()) );
		for(int i=0; i<getHeight(); i++){
			for(int j=0; j<getWidth(); j++){
				ppm.append( String.format("%d %d %d\n",(int) getPixel(i, j).getRed(),(int) getPixel(i, j).getGreen(),(int) getPixel(i, j).getBlue()) );
			}
		}
		return(ppm.toString());
	}
	
	public void toFile(java.io.File file){
		System.out.print("Creating PPM file...\t");
		try(java.io.DataOutputStream output = new java.io.DataOutputStream(new FileOutputStream(file)) ){
			output.writeBytes(toString());
			output.close();
			System.out.println("Done.");
		}
		catch(IOException ex){
			System.err.println("Could't write to file!");
		}
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

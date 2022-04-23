/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ce325.hw3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author minas
 */
public class PPMImageStacker{
	List<PPMImage> list = new LinkedList<>();
	PPMImage stackedImg;
	
	public PPMImageStacker(java.io.File dir) throws UnsupportedFileFormatException, FileNotFoundException{
		System.out.println("Creating list of images...");
		try{
			if(!dir.exists()){
				System.err.printf("[ERROR] Directory %s does not exist!", dir.getName());
			}
			else{
				if(!dir.isDirectory()){
					System.err.printf("[ERROR] %s is not a directory!", dir.getName());
				}
				else{
					java.io.File files[] = dir.listFiles();
					int i=0;
					for (File file : files) {
						System.out.printf("File %d: ", i++);
						list.add(new PPMImage(file));
					}
					System.out.println("List of images created.");
				}
			}
		}
		catch(IOException ex){
			System.err.println("[ERROR] IOException!");
		}
	}
	
	public void stack(){
		int height = list.get(0).getHeight();
		int width = list.get(0).getWidth();
		stackedImg = new PPMImage( new RGBImage(width, height, list.get(0).getColordepth()));
		int rgb[] = new int[3];
		
		System.out.print("Creating stacked image... ");
		for(int i=0; i<height; i++){
			for(int j=0; j<width; j++){
				//Sum of rgb values:
				for (PPMImage img : list) {
					rgb[0]+= img.getPixel(i,j).getRed();
					rgb[1]+= img.getPixel(i,j).getGreen();
					rgb[2]+= img.getPixel(i,j).getBlue();
				}
				//Divide by number of images:
				for(int k = 0; k<3; k++){
					rgb[k] = rgb[k]/list.size();
					if(rgb[k]<0){
						rgb[k] = 0;
					}
					else if(rgb[k]>255){
						rgb[k] = 255;
					}
				}
				//Create the stacked image, one pixel at a time:
				stackedImg.setPixel(i, j, new RGBPixel((short) rgb[0], (short) rgb[1], (short) rgb[2]));
			}
		}
		System.out.println("Done.");
	}
	
	public PPMImage getStackedImage(){
		return stackedImg;
	}
}

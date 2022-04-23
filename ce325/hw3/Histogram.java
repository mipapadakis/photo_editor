/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ce325.hw3;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author minas
 */
public class Histogram {
	private int histogram[];
	private int equalized[]; //contains the equalised luminosity
	private int NumOfPixels; //img.height * img.width
	private int maxY = 0;
	private YUVImage img;
	
	public Histogram(YUVImage img){
		img = new YUVImage(img);
		short Y;
		histogram = new int[236];
		
		System.out.print("Generating Histogram... ");
		for(int i=0; i<img.getHeight(); i++){
			for(int j=0; j<img.getWidth(); j++){
				Y = img.getPixel(i,j).getY();
				histogram[Y]++;
				if(Y>maxY){
					maxY = Y; //Find maximum luminocity
				}
			}
		}
		System.out.println("Done.");
	}
	
	@Override
	public String toString(){
		int max = histogram[0];
		//For normalization:
		for(int i=1; i<histogram.length; i++){
			if(histogram[i]>max){
				max = histogram[i]; //Find the most frequent luminocity
			}
		}
		StringBuilder str = new StringBuilder("");
		for(int i=0; i<histogram.length; i++){
			str.append(Integer.toString(i));
			str.append(": ");
			for(int j=0; j<(int) (80*((double) histogram[i]/(double) max) ); j++){
				str.append("*");
			}
			str.append("\n");
		}
		return(str.toString());
	}
	
	public void toFile(java.io.File file){
		System.out.print("Creating equalised file...\t");
		try(java.io.DataOutputStream output = new java.io.DataOutputStream(new FileOutputStream(file)) ){
			if(histogram!=null){
				output.writeBytes(toString());
				System.out.println("Done.");
			}
			else{
				output.writeBytes("Empty file!");
			}
			output.close();
		}
		catch(IOException ex){
			System.err.println("Could't write to file!");
		}
	}
	
	public void equalize(){
		double p[] = new double[236]; //Possibility
		double sp[] = new double[236]; //Sum of previous Possibilities
		equalized = new int[236];
		NumOfPixels = img.getHeight()*img.getWidth();
		p[0] = (double) histogram[0]/(double) NumOfPixels;
		sp[0] = p[0];
		
		for(int i=1; i<histogram.length; i++){
			p[i] =  (double) histogram[i]/(double) NumOfPixels; //Calculate possibility of every luminocity
			sp[i] = sp[i-1] + p[i]; //Calculate the sum of previous possibilities for each luminocity
		}
		
		//Multiply sp with the maximum luminocity value:
		for(int i=0; i<histogram.length; i++){
			histogram[i]=0;
			equalized[i] = (int) (sp[i]*maxY);
			if(equalized[i]<0){
				equalized[i]=0;
			}
			else if(equalized[i]>235){
				equalized[i]=235;
			}
		}
		//System.out.println("EqualisedLuminocity table created.");
		
		//Replace the Y values of img with the new luminocities:
		for(int i=0; i<img.getHeight(); i++){
			for(int j=0; j<img.getWidth(); j++){
				img.getPixel(i,j).setY( (short) ( equalized[img.getPixel(i,j).getY()] ) ); //Convert img to it's equalized version
			}
		}
		for(int i=0; i<img.getHeight(); i++){
			for(int j=0; j<img.getWidth(); j++){
				histogram[img.getPixel(i,j).getY()]++; //Generate new histogram
			}
		}
	}
	
	public short getEqualisedLuminocity(int luminocity){
		return((short) equalized[luminocity]);
	}
}

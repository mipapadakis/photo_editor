/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ce325.hw3;

/**
 *
 * @author minas
 */
public class RGBPixel {
	private short red, green, blue;
	private int rgb;
	
	public RGBPixel(){
		red = (short) 0;
		green = (short) 0;
		blue = (short) 0;
		setRGB(red, green, blue);
	}
	
	public RGBPixel(short red, short green, short blue){
		this.red = red;
		this.green = green;
		this.blue = blue;
		setRGB(red, green, blue);
	}
	
	public RGBPixel(RGBPixel pixel){
		this.red = pixel.getRed();
		this.green = pixel.getGreen();
		this.blue = pixel.getBlue();
		setRGB(red, green, blue);
	}
	
	public RGBPixel(YUVPixel pixel){
		short C, D, E;
		
		C = (short) (pixel.getY() - 16);
		D = (short) (pixel.getU() - 128);
		E = (short) (pixel.getV() - 128);
		
		red   = clip( (short) ((298*C         + 409*E + 128)>>8) );
		green = clip( (short) ((298*C - 100*D - 208*E + 128)>>8) );
		blue  = clip( (short) ((298*C + 516*D         + 128)>>8) );
		setRGB(red, green, blue);
	}
	
	public short getRed(){ return(red);}
	public short getGreen(){ return(green);}
	public short getBlue(){ return(blue);}
	
	public void setRed(short red){
		this.red = red;
		setRGB(red, green, blue);
	}
	public void setGreen(short green){
		this.green = green;
		setRGB(red, green, blue);
	}
	public void setBlue(short blue){
		this.blue = blue;
		setRGB(red, green, blue);
	}
	
	public int getRGB(){
		return(rgb);
	}
	
	public void setRGB(int value){
		red = (short) ((rgb>>16)&0xFF);
		green = (short) ((rgb>>8)&0xFF);
		blue = (short) (rgb&0xFF);
		rgb = value;
	}
	
	final void setRGB(short red, short green, short blue){
		this.red = red;
		this.green = green;
		this.blue = blue;
		/*rgb = 0;
		rgb = rgb | red<<16;
		rgb = rgb | green<<8;
		rgb = rgb | blue;*/
		rgb = red;
		rgb = (rgb<<8)+green;
		rgb = (rgb<<8)+blue;
	}
	
	@Override
	public String toString(){
		return(String.format("(%s,%s,%s)", Short.toString(red), Short.toString(green), Short.toString(blue)) );
	}
	
	private short clip(short value){
		if(value > 255){
			value = (short) 255;
		}
		else if(value < 0){
			value = (short) 0;
		}
		return(value);
	}
}

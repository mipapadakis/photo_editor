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
public class YUVPixel {
	private short Y, U, V;
	
	public YUVPixel(short Y, short U, short V){
		this.Y = Y;
		this.U = U;
		this.V = V;
	}
	
	public YUVPixel(YUVPixel pixel){
		this.Y = pixel.getY();
		this.U = pixel.getU();
		this.V = pixel.getV();
	}
	
	public YUVPixel(RGBPixel pixel){
		short R, G, B;
		R = pixel.getRed();
		G = pixel.getGreen();
		B = pixel.getBlue();
		Y = (short) ((( 66*R + 129*G +  25*B + 128)>>8)+ 16);
		U = (short) (((-38*R -  74*G + 112*B + 128)>>8)+128);
		V = (short) (((112*R -  94*G -  18*B + 128)>>8)+128);
	}
	
	public short getY(){ return(Y);}
	public short getU(){ return(U);}
	public short getV(){ return(V);}
	
	public void setY(short Y){ this.Y = Y;}
	public void setU(short U){ this.U = U;}
	public void setV(short V){ this.V = V;}
}

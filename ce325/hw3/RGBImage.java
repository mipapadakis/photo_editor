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
public class RGBImage implements Image{
	static int MAX_COLORDEPTH = 255;
	private int width;
	private int height;
	private int colordepth;
	private RGBPixel array[][];
	
	public RGBImage(int width, int height, int colordepth){
		this.width = width;
		this.height = height;
		this.colordepth = colordepth;
		array = new RGBPixel[height][width];
		
		for(int i=0; i<height; i++) {
			for(int j=0; j<width; j++) {
				array[i][j] = new RGBPixel((short)100, (short)100, (short)100);
			}
		}
	}
	
	public RGBImage(RGBImage copyImg){
		width = copyImg.getWidth();
		height = copyImg.getHeight();
		colordepth = copyImg.getColordepth();
		array = new RGBPixel[height][width];
		
		for(int i=0; i<height; i++) {
			for(int j=0; j<width; j++) {
				array[i][j] = new RGBPixel(copyImg.getPixel(i,j));
			}
		}
	}
	
	public RGBImage(YUVImage YUVImg){
		width = YUVImg.getWidth();
		height = YUVImg.getHeight();
		colordepth = (short) 255;
		array = new RGBPixel[height][width];
		
		for(int i=0; i<height; i++) {
			for(int j=0; j<width; j++) {
				array[i][j] = new RGBPixel(YUVImg.getPixel(i,j));
			}
		}
	}
	
	public int getWidth(){ return(width); }
	public int getHeight(){ return(height); }
	public int getColordepth(){ return(colordepth); }
	public RGBPixel getPixel(int row, int col){
		if(row>=height || col>=width || row<0 || col<0 ){
			System.out.println("Wrong dimensions!");
			System.exit(0);
		}
		return(array[row][col]);
	}
	
	public void setWidth(int width){
		this.width = width;
		array = new RGBPixel[height][width];
	}
	public void setHeight(int height){
		this.height = height;
		array = new RGBPixel[height][width];
	}
	public void setColordepth(int colordepth){
		this.colordepth = colordepth;
	}
	public void setPixel(int row, int col, RGBPixel pixel){
		if(row>=height || col>=width || row<0 || col<0 ){
			System.out.println("Wrong dimensions!");
			System.exit(0);
		}
		if(array.length == 0){
			//If we've called super(0,0,0) for initialisation, the array has been created with zero cols and zero rows. Avoid that case:
			array = new RGBPixel[height][width];
		}
		array[row][col] = pixel;
	}
	public void setPixel(int row, int col, YUVPixel pixel){
		setPixel(row, col, new RGBPixel(pixel)); //Convert the YUV pixel to RGB
	}
	
	@Override
	public void grayscale(){
		short temp;
		
		System.out.print("Grayscaling... ");
		for(int i=0; i<getHeight(); i++) {
			for(int j=0; j<getWidth(); j++) {
				temp = (short) (array[i][j].getRed()*0.3 + array[i][j].getGreen()*0.59 + array[i][j].getBlue()*0.11);
				array[i][j].setRed(temp);
				array[i][j].setGreen(temp);
				array[i][j].setBlue(temp);
			}
		}
		System.out.println("Done.");
	}
	
	@Override
	public void doublesize(){
		RGBPixel temp[][] = new RGBPixel[2*height][2*width];
		
		int mode = 0;
		/*  mode = 0 for hw3's algorithm. If mode!=0, use our optimization for doublesizing the image.
		The optimization uses the median color between the pixel's neighbors. Specifically,
		if mode!=0:
			-the temp[2*i][2*j] element is equal to array[i][j].
			-the temp[2*i][2*j + 1] element is the median between array[i][j] and array[i][j+1].
			-the temp[2*i + 1][2*j] element is the median between array[i][j] and array[i+1][j].
			-the temp[2*i + 1][2*j + 1] element is the median between array[i][j], array[i][j+1], array[i+1][j] and array[i+1][j+1].
		The difference between these algorithms is best noticed if you halfsize an image and then doublesize it again.
		*/
		System.out.print("Doublesizing... ");
		
		//Implementation using hw3's instructions:
		if(mode==0){
			for(int i=0; i<height; i++) {
				for(int j=0; j<width; j++) {
					temp[2*i][2*j] = new RGBPixel(array[i][j]);
					temp[2*i+1][2*j] = new RGBPixel(array[i][j]);
					temp[2*i][2*j+1] = new RGBPixel(array[i][j]);
					temp[2*i+1][2*j+1] = new RGBPixel(array[i][j]);
				}
			}
		}
		//Optimization:
		else{
			for(int i=0; i<getHeight(); i++) {
				for(int j=0; j<getWidth(); j++) {
					temp[2*i][2*j] = new RGBPixel(array[i][j]);

					//BELOW
					if(i+1<getHeight()){
						temp[2*i+1][2*j] = new RGBPixel();
						temp[2*i+1][2*j].setRed( (short) ((array[i][j].getRed() + array[i+1][j].getRed())/2) );
						temp[2*i+1][2*j].setGreen( (short) ((array[i][j].getGreen() + array[i+1][j].getGreen())/2) );
						temp[2*i+1][2*j].setBlue( (short) ((array[i][j].getBlue() + array[i+1][j].getBlue())/2) );
					}
					else{ //(Reached edge of photo)
						temp[2*i+1][2*j] = new RGBPixel(array[i][j]);
					}

					//RIGHT
					if(j+1<getWidth()){
						temp[2*i][2*j+1] = new RGBPixel();
						temp[2*i][2*j+1].setRed( (short) ((array[i][j].getRed() + array[i][j+1].getRed())/2) );
						temp[2*i][2*j+1].setGreen( (short) ((array[i][j].getGreen() + array[i][j+1].getGreen())/2) );
						temp[2*i][2*j+1].setBlue( (short) ((array[i][j].getBlue() + array[i][j+1].getBlue())/2) );
					}
					else{ //(Reached edge of photo)
						temp[2*i][2*j+1] = new RGBPixel(array[i][j]);
					}

					//DIAGONAL
					if((j+1<getWidth()) && (i+1<getHeight())){
						//Median between all neighbors:
						temp[2*i+1][2*j+1] = new RGBPixel();
						temp[2*i+1][2*j+1].setRed( (short) ((array[i][j].getRed() + array[i+1][j+1].getRed())/2) );
						temp[2*i+1][2*j+1].setGreen( (short) ((array[i][j].getGreen() + array[i+1][j+1].getGreen())/2) );
						temp[2*i+1][2*j+1].setBlue( (short) ((array[i][j].getBlue() + array[i+1][j+1].getBlue())/2) );
					}
					else{ //(Reached edge of photo)
						temp[2*i+1][2*j+1] = new RGBPixel(array[i][j]);
					}
				}
			}
		}
		array = temp;
		width = array[0].length;
		height = array.length;
		System.out.println("Done.");
	}
	
	@Override
	public void halfsize(){
		RGBPixel temp[][] = new RGBPixel[height/2][width/2];
		
		System.out.print("Halfsizing... ");
		for(int i=0; i<height/2; i++) {
			for(int j=0; j<width/2; j++) {
				temp[i][j] = new RGBPixel();
				temp[i][j].setRed( (short) ((array[2*i][2*j].getRed() + array[2*i+1][2*j].getRed() + array[2*i][2*j+1].getRed() + array[2*i+1][2*j+1].getRed())/4) );
				temp[i][j].setGreen( (short) ((array[2*i][2*j].getGreen() + array[2*i+1][2*j].getGreen() + array[2*i][2*j+1].getGreen() + array[2*i+1][2*j+1].getGreen())/4) );
				temp[i][j].setBlue( (short) ((array[2*i][2*j].getBlue() + array[2*i+1][2*j].getBlue() + array[2*i][2*j+1].getBlue() + array[2*i+1][2*j+1].getBlue())/4) );
			}
		}
		array = temp;
		width = array[0].length;
		height = array.length;
		System.out.println("Done.");
	}
	
	@Override
	public void rotateClockwise(){
		RGBPixel temp[][] = new RGBPixel[width][height];
		
		System.out.print("Rotating... ");
		for(int i=0; i<height; i++) {
			for(int j=0; j<width; j++) {
				temp[j][getHeight() - 1 - i] = new RGBPixel();
				temp[j][getHeight() - 1 - i].setRed(array[i][j].getRed());
				temp[j][getHeight() - 1 - i].setGreen(array[i][j].getGreen());
				temp[j][getHeight() - 1 - i].setBlue(array[i][j].getBlue());
			}
		}
		array = temp;
		width = array[0].length;
		height = array.length;
		System.out.println("Done.");
	}
}

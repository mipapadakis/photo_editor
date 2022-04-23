/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ce325.hw3;

import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.*;

public class PPMFileFilter extends javax.swing.filechooser.FileFilter {
	
	@Override
	public boolean accept(File file) {
	  if(file.isDirectory())
		return true;
	  String name = file.getName();    
	  if(name.length()>4 && name.substring(name.length()-4, name.length()).toLowerCase().equals(".ppm"))
		return true;
	  return false;
	}
  
	@Override
	public String getDescription() { return "PPM File"; }
}

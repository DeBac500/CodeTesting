package robotrader.gui;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class XmlFileFilter extends FileFilter
{
  public boolean accept(File file)
  {
    String ext = getExtension(file);
    
    if (ext.equalsIgnoreCase("XML"))
    {
      return true;
    }
    
    if (file.isDirectory())
    {
      return true;
    }
    
    return false;
  }
  
  public String getDescription()
  {
    return "Xml File";
  }
  
  private String getExtension(File file)
  {
    String ext = "";
    String s = file.getName();
    int i = s.lastIndexOf('.');

    if (i > 0 &&  i < s.length() - 1) 
    {
      ext = s.substring(i+1).toLowerCase();
    }
    return ext;
  }
}

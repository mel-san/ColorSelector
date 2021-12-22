//CS 326 HW8 Color App - Melanie Sanchez
//Application displays the color selected
//and allows for modification to these colors to be saved 

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.io.*; 

public class ColorApp extends JFrame  
{
   //Declaration of Variables
   protected DrawColor drawTest;
   protected JList<String> colorList;
   protected JPanel leftPanel, colorPanel, rightPanel;
   protected JButton buttonSave,buttonReset,redMinus,redPlus,greenMinus,greenPlus,blueMinus,bluePlus;
   protected JTextField redText, greenText,blueText;
   protected ColorObj currentColor, colorOriginal;
   protected ColorObj colorArr[];
   protected int currentIndex;
   public static void main (String argv []) 
   {
      try
      {
          new ColorApp("Color Sampler");
      }
      catch (IOException e)
      {
          System.out.println("File not found");
          System.exit(0);
      }  
   }
   
   public ColorApp(String title) throws IOException
   {
   super(title); // call constructor of base class
   setBounds(300, 300, 500, 300);
   addWindowListener(new WindowDestroyer());

   //Allocation of all variables
   drawTest = new DrawColor();
   drawTest.setBounds(10, 10, 200, 100);

   buttonSave = new JButton("Save");
   buttonReset = new JButton("Reset");
   redMinus = new JButton("-");
   redPlus = new JButton("+");
   greenMinus = new JButton("-");
   greenPlus = new JButton("+");
   blueMinus = new JButton("-");
   bluePlus = new JButton("+");

   //Addin action listeners to the buttons
   buttonSave.addActionListener(new ActionHandler());  
   buttonReset.addActionListener(new ActionHandler());
   redMinus.addActionListener(new ActionHandler());
   redPlus.addActionListener(new ActionHandler());
   greenMinus.addActionListener(new ActionHandler());
   greenPlus.addActionListener(new ActionHandler());
   blueMinus.addActionListener(new ActionHandler());
   bluePlus.addActionListener(new ActionHandler());

   redText = new JTextField("");
   greenText = new JTextField("");
   blueText = new JTextField("");

   //Allocation of the Jpanels
   JPanel leftPanel = new JPanel();
   JPanel rightPanel = new JPanel();
   JPanel colorPanel = new JPanel();

    //Try to open input file with colors "colors".txt
    File infile = new File("colors.txt");
        if( !infile.exists() )
        {
            throw new IOException("File not found");
        }

   
   FileInputStream stream = new FileInputStream("colors.txt");  
   InputStreamReader reader = new InputStreamReader(stream); 
   StreamTokenizer tokens = new StreamTokenizer(reader); 
    
   //Array to hold colorObj
   colorArr = new ColorObj[11];
   //Keeps track of current index
   currentIndex = 0;
   
   //Iterate through each line in the file, adding its colorName, and rgb values to the array

   while (tokens.nextToken() != tokens.TT_EOF) 
   {  
      currentColor = new ColorObj();

      currentColor.colorName = (String)tokens.sval;
      tokens.nextToken();
      currentColor.r = (int)tokens.nval;
      tokens.nextToken();
      currentColor.g = (int)tokens.nval;
      tokens.nextToken();
      currentColor.b = (int)tokens.nval;
      colorArr[currentIndex]=(currentColor);
      currentIndex++;
     
      }
      
      
      stream.close();

      currentIndex--;
    //current Color will keep track of the current color values
    currentColor = new ColorObj(colorArr[currentIndex]);
    //Color Original will keep track of the original value that is from the file or last saved
    colorOriginal= new ColorObj(colorArr[currentIndex]);

    
   
   redText = new JTextField(String.valueOf(currentColor.r));
   greenText = new JTextField(String.valueOf(currentColor.g));
   blueText = new JTextField(String.valueOf(currentColor.b));

  
   //Add each component to its corresponding panel
   colorPanel.add(drawTest);
   leftPanel.add(new JLabel("Red:"));
   leftPanel.add(redText);
   leftPanel.add(redMinus);
   leftPanel.add(redPlus);
   leftPanel.add(new JLabel("Green:"));
   leftPanel.add(greenText);
   leftPanel.add(greenMinus);
   leftPanel.add(greenPlus);
   leftPanel.add(new JLabel("Blue:"));
   leftPanel.add(blueText);
   leftPanel.add(blueMinus);
   leftPanel.add(bluePlus);

   
   leftPanel.add(buttonSave);
   leftPanel.add(buttonReset);
   

   colorList = new JList<String>();
   colorList.addListSelectionListener(new ListHandler()); 

  rightPanel.add(new JScrollPane(colorList));

  //By doing a horizontal split between the color and left panel, this allows for the r,g,b values and sample color to be divided

  JSplitPane splitPaneHorizontal = new JSplitPane(SwingConstants.HORIZONTAL,colorPanel ,leftPanel);
  splitPaneHorizontal.setDividerLocation(120);
  //By doing a vertical spilt between the horizontal and right panel, this allows for the scroll selection to be to the right of the other componenys
  JSplitPane splitPane = new JSplitPane(SwingConstants.VERTICAL, splitPaneHorizontal, rightPanel);
  splitPane.setDividerLocation(255);

  
   getContentPane().add(drawTest);
   getContentPane().add(splitPane);
 
   
   String colors[] = {"Red", "Green", "Blue", "Yellow", "Cyan","Magenta", "Orange", "Pink","Grey","Black","White"};
   colorList.setListData(colors);
   
   setVisible(true);



   }
 
   // Define window adapter                                       
   private class WindowDestroyer extends WindowAdapter 
   {      
      public void windowClosing(WindowEvent e) 
      {    
         try{
         //After program terminates, write updated color information back to the file

            PrintToFile();
         }
         catch (IOException ex)
         {
             System.exit(0);
         }  
      System.exit(0);  
   
      }                                                             
                
      
   public void PrintToFile() throws IOException
   {
      File output = new File("colors.txt");
      if( !output.exists() )
      {
         throw new IOException("File does not exist");
      }
      
      FileOutputStream ostream = new FileOutputStream(output);
      PrintWriter writer = new PrintWriter(ostream);
      //Write each colorname and rgb values back to file
      for (int i = 0; i < 11; i++) 
         {  
            writer.println(colorArr[i].colorName + "\t" + colorArr[i].r + 
            "\t"+ colorArr[i].g + "\t" + colorArr[i].b);  
         } 
      writer.flush();
      ostream.close(); 
   } 

}
   


   private class ListHandler implements ListSelectionListener 
   {        
      public void valueChanged(ListSelectionEvent e)  
      {
         if ( e.getSource() == colorList)
         {
            if ( !e.getValueIsAdjusting() )
            {
               //Set current index equal to the selection made from the list
               currentIndex = colorList.getSelectedIndex();
               //Set currentColor and colorOriginal to the corresponding color object
               currentColor = new ColorObj(colorArr[currentIndex]);
               colorOriginal = new ColorObj(colorArr[currentIndex]);
               //Update text to reflect r,g,b values of color object
               redText.setText(String.valueOf(currentColor.r));
               greenText.setText(String.valueOf(currentColor.g));
               blueText.setText(String.valueOf(currentColor.b));
               drawTest.repaint();
               New_Name("Color Sampler");
            }
         }
   }
   }
   private class ActionHandler implements ActionListener 
   {      
      public void actionPerformed(ActionEvent e)
      {
         if ( e.getSource() == buttonSave)
         {
            //Update name
            New_Name("Color Sampler");
            //Update both array and color Original
            colorArr[currentIndex] = new ColorObj(currentColor);
            colorOriginal = new ColorObj(currentColor);
            //Update text to reflect r,g,b values of color object
            redText.setText(String.valueOf(currentColor.r));
            greenText.setText(String.valueOf(currentColor.g));
            blueText.setText(String.valueOf(currentColor.b));

         }
         else if ( e.getSource() == buttonReset)
         {
            //Update name
            New_Name("Color Sampler");
            //Change current color to the orinal value stored in color Original
            currentColor = new ColorObj(colorOriginal);
            redText.setText(String.valueOf(colorOriginal.r));
            greenText.setText(String.valueOf(colorOriginal.g));
            blueText.setText(String.valueOf(colorOriginal.b));
         }
         //Update r,g,b values based on which button is pressed
         else if (e.getSource() == redMinus)
         {
            New_Name("Color Sampler*");
            currentColor.r = currentColor.r - 5;
            redText.setText(String.valueOf(currentColor.r));
         }
         else if (e.getSource() == redPlus)
         {        
            New_Name("Color Sampler*");
            currentColor.r = currentColor.r + 5;
            redText.setText(String.valueOf(currentColor.r));
         }
         else if (e.getSource() == greenMinus)
         {  
            New_Name("Color Sampler*");
            currentColor.g = currentColor.g - 5;
            greenText.setText(String.valueOf(currentColor.g));

         }

         else if (e.getSource() == greenPlus)
         {  
            New_Name("Color Sampler*");
            currentColor.g = currentColor.g + 5;
            greenText.setText(String.valueOf(currentColor.g));

         }
         else if (e.getSource() == blueMinus)
         {
            New_Name("Color Sampler*");
            currentColor.b = currentColor.b - 5;
            blueText.setText(String.valueOf(currentColor.b));

         }
         else if (e.getSource() == bluePlus)
         {
            New_Name("Color Sampler*");
            currentColor.b = currentColor.b + 5;
            blueText.setText(String.valueOf(currentColor.b));
         }


         drawTest.repaint();

        
      }
   }
                           


   private void New_Name(String name)
   {
      this.setTitle(name);
   }

   //Class that create a colorObj each with a name and r,g,b values
   public class ColorObj
   {
      public int r;
      public int g;
      public int b;
      public String colorName;
      //Intialize r,g,b values to 0 upon creation, and colorName equal to an empty string
      ColorObj()
      {
         r = 0;
         g = 0;
         b = 0;
         colorName = "";
      }
      
      //Copy constructor
      public ColorObj(ColorObj copy)
      {
         this.r = copy.r;
         this.g = copy.g;
         this.b = copy.b;
         this.colorName = copy.colorName;
      }


   }
   class DrawColor extends JComponent
      {
      
      public void paint(Graphics g)
      {
         Dimension d = getSize();
         //Draw a rectangle to the r,g,b values of the current color
         g.setColor(new Color(currentColor.r, currentColor.g, currentColor.b));
         g.fillRect(1, 1, d.width-2, d.height-2);
      }
      }
}  
   

/* http://www.nw-fva.de
   Version 07-11-2008

   (c) 2002 Juergen Nagel, Northwest German Forest Research Station, 
       Gr�tzelstr.2, 37079 G�ttingen, Germany
       E-Mail: Juergen.Nagel@nw-fva.de
 
This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation.

This program is distributed in the hope that it will be useful,
but WITHOUT  WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.
 */
/**
 *
 * @author  sschimpf
 */



package treegross.standsimulation;
import treegross.base.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;

public class TgInternalFrame extends JInternalFrame
{
   
    
    /** Creates a new instance of tginternalframe */
    public TgInternalFrame(JPanel p, String title) {
       super(title);

       //getContentPane().add(p,BorderLayout.CENTER);
       getContentPane().add(p);
       setVisible(true);
       setResizable(true);
       setIconifiable(true);
       setMaximizable(true);
       setClosable(true);
       setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
       pack();
    }
    
}

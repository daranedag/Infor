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
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;



public class TgToolbar extends JToolBar
{
    JButton button;
   
    
    /** Creates a new instance of tgtoolbar */
    public TgToolbar(ActionListener listener, String path, String preferredLanguage) 
    {
       
       Locale currentLocale;
       ResourceBundle messages;
       currentLocale = new Locale(preferredLanguage, "");
       messages = ResourceBundle.getBundle("treegross.standsimulation.MyMenubar",currentLocale);
        button = new JButton(new ImageIcon(path+"//icons//userbaum1.jpg"));
        button.setRolloverEnabled(true);
        button.setToolTipText(messages.getString("Grow"));
        button.setContentAreaFilled( false );
        button.setBorderPainted( false );
        button.setFocusPainted( false );
        button.setActionCommand("Grow"); 
        
        button.addActionListener(listener);
        add(button);
        
        button = new JButton(new ImageIcon (path+"//icons//useraxt1.jpg"));       
        button.setToolTipText(messages.getString("Treatment"));
        button.setContentAreaFilled( false );
        button.setBorderPainted( false );  
        button.setFocusPainted( false );
        button.setActionCommand("Treatment");
        button.addActionListener(listener);
        add(button);
        
/*        button = new JButton(new ImageIcon (path+"//icons//userlineal1.jpg"));
        button.setToolTipText(messages.getString("Sorting"));
        button.setContentAreaFilled( false );
        button.setBorderPainted( false );   
        button.setFocusPainted( false );
        button.setActionCommand("Sorting");
        button.addActionListener(listener);
        add(button);
*/        
        setFloatable(false);

    }
    
}


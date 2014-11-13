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
package treegross.base;

import java.util.EventObject;
/**
 *
 * @author jhansen
 */
public class StandChangeEvent extends EventObject {
    
      private String name;
      private String _action;
      private Object _sender;
    
    /** Creates a new instance of StandChangeEvent */
    public StandChangeEvent(Object source, String newname, String action, Object sender ) {
         super(source);
         name = newname;
         _action = action;
         _sender=sender;
    }
    public String getName(){
            return name;
    }
    
    public String getAction(){
            return _action;
    }
    
    public Object getSender(){
            return _sender;
    }
    
}

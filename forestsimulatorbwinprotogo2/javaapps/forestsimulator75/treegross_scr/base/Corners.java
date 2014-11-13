/* 
* @(#) Corners.java  
*  (c) 2002-2007 Juergen Nagel, Northwest German Research Station, 
*      Grätzelstr.2, 37079 Göttingen, Germany
*      E-Mail: Juergen.Nagel@nw-fva.de
*
*  This program is free software; you can redistribute it and/or
*  modify it under the terms of the GNU General Public License
*  as published by the Free Software Foundation.
*
*  This program is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*  GNU General Public License for more details.
*/

package treegross.base;

/** TreeGrOSS : Stand.java
 *  version 	7.5 2-Oct-2008
 *  author	Juergen Nagel
 * 
 * This class defines the structure of a corner and center points and is used in
 * class Stand for the definition of the stand area and the transformation from
 * polar to xy coordinates.  
 *  
 *  http://www.nw-fva.de/~nagel/treegross/
 *   
 */
public class Corners implements Cloneable{
    /* x,y ccordinate of the stand corner points */    
    public String no, remarks;
    public double x,y,z;  


    public Corners clone(){
        Corners clone= new Corners();
        clone.no= new String(this.no);
        clone.x= new Double(this.x);
        clone.y= new Double(this.y);
        clone.z= new Double(this.z);        
        clone.remarks = new String(this.remarks);
        return clone;
    }
    
}

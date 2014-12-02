/* 
* @(#) TreegrossXML2.java  
*  (c) 2002-2010 Juergen Nagel, Northwest German Research Station,
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
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.ProcessingInstruction;
import org.jdom.output.XMLOutputter;
import org.jdom.input.*;
import org.jdom.DocType;
import java.net.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.*;
import java.util.*;
/** TreeGrOSS : TreegrossXML2.java
 *  version 	7.5 18-Mar-2010
 *  author	Juergen Nagel
 * 
 * This is the 2nd format to define a forest stand by XML. The class can read
 * and write a treegross xml file
 *  
 *  http://www.nw-fva.de/~nagel/treegross/
 *   
 */
public class TreegrossXML2 {
    static Document doc;
    static Element rootElt;
    
    /**
     * Creates a new instance of TreegrossXML2
     */
    public TreegrossXML2() {
    }
    
    /** Creates a Treegross xml-File*/
    public void saveAsXML(Stand st,String filename) {
        NumberFormat f=NumberFormat.getInstance();
        f=NumberFormat.getInstance(new Locale("en","US"));
        f.setMaximumFractionDigits(2);
        f.setMinimumFractionDigits(2);
        Element elt;
        Element elt2;
        Document doc = new Document();
        rootElt = new Element("Bestand");
        ProcessingInstruction pi = new ProcessingInstruction("xml-stylesheet", "type=\"text/xsl\" href=\"treegross.xsl\"");
        doc.addContent(pi);
        doc.setRootElement(rootElt);

        //Bestandesinformation
        rootElt= addString(rootElt, "Id","1");
        rootElt= addString(rootElt, "Identificación",st.standname);
        rootElt= addString(rootElt, "General"," ");
        rootElt= addString(rootElt, "Tamaño de la zona_m2", Double.toString(st.size*10000));
        rootElt= addString(rootElt, "HauptbaumArtCodeStd", Integer.toString(st.sp[0].code));  //Codigo estandar de la especie  principal del arbol
        rootElt= addString(rootElt, "HauptbaumArtCodeLokal", Integer.toString(st.sp[0].code));  // Codigo local de la especie principal del arbol
        rootElt= addString(rootElt, "Año de grabacion", Integer.toString(st.year));
        rootElt= addString(rootElt, "Mes de grabacion", Integer.toString(st.monat));
        rootElt= addString(rootElt, "Origen de los datos",st.datenHerkunft);
        rootElt= addString(rootElt, "Ubicacion",st.standort);
        rootElt= addString(rootElt, "Valor altura_m", Double.toString(st.hochwert_m));
        rootElt= addString(rootElt, "Valor recto_m", Double.toString(st.rechtswert_m));
        rootElt= addString(rootElt, "Altura_uNN_m", Double.toString(st.hoehe_uNN_m));
        rootElt= addString(rootElt, "Exposicion_Gon", Integer.toString(st.exposition_Gon));
        rootElt= addString(rootElt, "Porcentaje pendiente", Double.toString(st.hangneigungProzent));
        rootElt= addString(rootElt, "Area de vegetacion",st.wuchsgebiet);
        rootElt= addString(rootElt, "Distrito Crecimiento",st.wuchsbezirk);
        rootElt= addString(rootElt, "Codigo de ubicacion",st.standortsKennziffer);

        /* Baumarten */;
        for (int i=0;i< st.nspecies;i++){
            elt = new Element("Codigo de especie");
            elt= addString(elt, "Codigo", Integer.toString(st.sp[i].code));
            elt= addString(elt, "Nombre aleman",st.sp[i].spDef.longName);
            elt= addString(elt, "Nombre latin",st.sp[i].spDef.latinName);
            rootElt.addContent(elt);
        }
        /* Add center points */;
        if (st.ncpnt > 0){
        elt = new Element("Vertice");
        elt= addString(elt, "Nr",st.center.no);
        elt= addString(elt, "Coordenada relativa X_m",f.format(st.center.x));
        elt= addString(elt, "Coordenada relativa Y_m",f.format(st.center.y));
        elt= addString(elt, "Coordenada relativa Z_m",f.format(st.center.z));
        rootElt.addContent(elt);
        }

        /* Add corner points */;
        for (int i=0;i< st.ncpnt;i++){
            elt = new Element("Vertice");
            elt= addString(elt, "Nr",st.cpnt[i].no);
            elt= addString(elt, "Coordenada relativa X_m",f.format(st.cpnt[i].x));
            elt= addString(elt, "Corrdenada relativa Y_m",f.format(st.cpnt[i].y));
            elt= addString(elt, "Coordenada relativa Z_m",f.format(st.cpnt[i].z));
            rootElt.addContent(elt);
        }
        /* Add Bäume */;
        for (int i=0;i< st.ntrees;i++){
            //System.out.println("test "+i);
            elt= new Element("Baum");
            elt= addString(elt, "Nr", Integer.toString(i+1));
            elt= addString(elt, "Identificación",st.tr[i].no);
            elt= addString(elt, "BaumartcodeStd","0");  //Codigo estandar de especie de arbol
            elt= addString(elt, "BaumartcodeLokal", Integer.toString(st.tr[i].code));  //Codigo local de especie de arbol
            elt= addString(elt, "Antigüedad_años", Integer.toString(st.tr[i].age));
            elt= addString(elt, "BHD_mR_cm",f.format(st.tr[i].d));
            elt= addString(elt, "Altura_m",f.format(st.tr[i].h));
            elt= addString(elt, "Base de Corona_m",f.format(st.tr[i].cb));
            elt= addString(elt, "Diametro de Corona media_m",f.format(st.tr[i].cw));
            elt= addString(elt, "Indice de Sitio_m",f.format(st.tr[i].si));
            elt= addString(elt, "Coordenada relativa X_m",f.format(st.tr[i].x));
            elt= addString(elt, "Coordenada relativa Y_m",f.format(st.tr[i].y));
            elt= addString(elt, "Coordenada relativa Z_m",f.format(st.tr[i].z));

            boolean lebend=true;
            if (st.tr[i].out > -1) lebend=false;
            elt= addString(elt, "Vida",Boolean.toString(lebend));

            boolean entnommen=false;
            if (st.tr[i].outtype >= 2) entnommen=true;

            elt= addString(elt, "Eliminado", Boolean.toString(entnommen));
            elt= addString(elt, "Mes separacion","3");
            elt= addString(elt, "Año separacion", Integer.toString(st.tr[i].out));

            String grund = "";
            if (st.tr[i].outtype == 1) grund="Mortalidad";
            if (st.tr[i].outtype == 2) grund="Adelgazamiento";
            if (st.tr[i].outtype == 3) grund="Cosecha";
            if (st.tr[i].outtype > 3)  grund="otro";
            elt= addString(elt, "Rechazar razón", grund);

            elt= addString(elt, "Arbol Z", Boolean.toString(st.tr[i].crop));
            elt= addString(elt, "Arbol temporal Z", Boolean.toString(st.tr[i].tempcrop));
            elt= addString(elt, "Arbol Habitat", Boolean.toString(st.tr[i].habitat));
            elt= addString(elt, "Clase Kraftsche","0");
            elt= addString(elt, "Capa", Integer.toString(st.tr[i].layer));
            f.setMaximumFractionDigits(4);
            f.setMinimumFractionDigits(4);
            elt= addString(elt, "Factor de área",f.format(st.tr[i].fac));
            elt= addString(elt, "Volumen_cbm",f.format(st.tr[i].v));
            elt= addString(elt, "Volumen de madera muerta_cbm",f.format(st.tr[i].volumeDeadwood));
            elt= addString(elt, "Observación",st.tr[i].remarks);
            f.setMaximumFractionDigits(2);
            f.setMinimumFractionDigits(2);
            rootElt.addContent(elt);
        }
        /* Abspeichern des doc */
        try {
            File file = new File(filename);
            FileOutputStream result = new FileOutputStream(file);
            XMLOutputter outputter = new XMLOutputter();
            outputter.output(doc,result);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
/**
 * reads a treegross xml-File from URL
 * @param stl TreeGrOSS Stand
 * @param url
 * @return TreeGrOSS Stand
 */
    public treegross.base.Stand readTreegrossStand(treegross.base.Stand stl, URL url){
       Stand st = new Stand();
       st=stl;
       try {
         SAXBuilder builder = new SAXBuilder();
         URLConnection urlcon = url.openConnection();

         Document doc = builder.build(urlcon.getInputStream());
           
         DocType docType = doc.getDocType();
     
         Element bestand =  doc.getRootElement();  
         st.id = bestand.getChild("Id").getText();
         st.addName(bestand.getChild("Identificación").getText());
         st.addsize(Double.parseDouble(bestand.getChild("Tamaño de la zona_m2").getText())/10000.0);
         st.monat = Integer.parseInt(bestand.getChild("Mes de grabacion").getText());
         st.year=Integer.parseInt(bestand.getChild("Año de grabacion").getText());
         st.datenHerkunft = bestand.getChild("Origen de datos").getText();
         st.standort = bestand.getChild("Ubicación").getText();
         st.rechtswert_m = Double.parseDouble(bestand.getChild("Valor derecho_m").getText());
         st.hochwert_m = Double.parseDouble(bestand.getChild("Valor alto_m").getText());
         st.hoehe_uNN_m = Double.parseDouble(bestand.getChild("Altura_uNN_m").getText());
         st.exposition_Gon = Integer.parseInt(bestand.getChild("Exposicion_Gon").getText());
         st.hangneigungProzent = Double.parseDouble(bestand.getChild("Porcentaje de pendiente").getText());
         st.wuchsgebiet = bestand.getChild("Área de vegetación").getText();
         st.wuchsbezirk = bestand.getChild("Distrito de crecimiento").getText();
         st.standortsKennziffer = bestand.getChild("Codigo de ubicación").getText();
         
         st.ncpnt=0;
         st.ntrees=0;
         st.nspecies=0;
         st.center.no="undefined";
         List eckpunkte = bestand.getChildren("Vértice");
         Iterator i = eckpunkte.iterator();
         while (i.hasNext()) {
            Element eckpunkt = (Element) i.next();
            String nrx = eckpunkt.getChild("Nr").getText();
            if (nrx.indexOf("circulo") > -1 || nrx.indexOf("poligono") > -1){
               st.center.no=nrx;    
               st.center.x=Double.parseDouble(eckpunkt.getChild("Coordenada relativa X_m").getText());
               st.center.y=Double.parseDouble(eckpunkt.getChild("Coordenada relativa Y_m").getText());
               st.center.z=Double.parseDouble(eckpunkt.getChild("Coordenada relativa Z_m").getText());
            }
            else {
                st.addcornerpoint(eckpunkt.getChild("Nr").getText(),
                   Double.parseDouble(eckpunkt.getChild("Coordenada relativa X_m").getText()),
                   Double.parseDouble(eckpunkt.getChild("Coordenada relativa Y_m").getText()),
                   Double.parseDouble(eckpunkt.getChild("Coordenada relativa Z_m").getText()));
            }
         } 
//         
         List baeume = bestand.getChildren("Arbol");
         i = baeume.iterator();
         while (i.hasNext()) {
            Element baum = (Element) i.next();
            int out = -1 ;
            //if (Boolean.parseBoolean(baum.getChild("Entnommen").getText())==false) // wenn so dann muss hier flase nuss hier mit true abgeglichen werden
            out = Integer.parseInt(baum.getChild("Año de rechazo").getText());
            int outtype=0;
            String ausGrund = baum.getChild("Razón de rechazo").getText();
            if (ausGrund.contains("Muerte")) outtype=1;
            if (ausGrund.contains("Corte")) outtype=2;
            if (ausGrund.contains("Cosecha")) outtype=3;

            st.addXMLTree (Integer.parseInt(baum.getChild("Codigo local especie de arbol").getText()),
                    baum.getChild("Identificación").getText(),
                    Integer.parseInt(baum.getChild("Antigüedad_años").getText()),
                    out, outtype,
                    Double.parseDouble(baum.getChild("BHD_mR_cm").getText()),
                    Double.parseDouble(baum.getChild("Altura_m").getText()),
                    Double.parseDouble(baum.getChild("Base de corona_m").getText()),
                    Double.parseDouble(baum.getChild("Diametro de la corona media_m").getText()),
                    Double.parseDouble(baum.getChild("Indice de sitio_m").getText()),
                    Double.parseDouble(baum.getChild("Factor de área").getText()),
                    Double.parseDouble(baum.getChild("Coordenada relativa X_m").getText()),
                    Double.parseDouble(baum.getChild("Coordenada relativa Y_m").getText()),
                    Double.parseDouble(baum.getChild("Coordenada relativa Z_m").getText()),
                    Boolean.parseBoolean(baum.getChild("Arbol Z").getText()),
                    Boolean.parseBoolean(baum.getChild("Arbol temporal Z").getText()),
                    Boolean.parseBoolean(baum.getChild("Habitat de arbol").getText()),
                    Integer.parseInt(baum.getChild("Capat").getText()),
                    Double.parseDouble(baum.getChild("VolumenTotholz_cbm").getText()),
                    baum.getChild("Bemerkung").getText()
            );
         }
       }
       catch(Exception e){
           e.printStackTrace();
       }
       return st;
    }
/**
 * Saves the trees and the cornerpoints in separate simple gml Files. The files can be
 * used with the GIS program OpenJump. Use data type FME GML for input to OpenJump
 * @param st TreeGroSS Stand file
 * @param filename  FileName on your hard disk. It gets extended by _trees.gml rsp. _area.gml
 */
        public void saveAsGML(Stand st,String filename) {

        NumberFormat f=NumberFormat.getInstance();
        f=NumberFormat.getInstance(new Locale("en","US"));
        f.setMaximumFractionDigits(2);
        f.setMinimumFractionDigits(2);

        try {
/** all data is writen in File info/treelist.html */
            OutputStream os=new FileOutputStream(filename+"_tress.gml");
	    PrintWriter out= new PrintWriter(new OutputStreamWriter(os));
            out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            out.println("<dataset xmlns=\"http://www.safe.com/xml/schemas/FMEFeatures\" xmlns:fme=\"http://www.safe.com/xml/schemas/FMEFeatures\" "+
                    "xmlns:gml=\"http://www.opengis.net/gml\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "+
                    "xsi:schemaLocation=\"http://www.safe.com/xml/schemas/FMEFeatures FMEFeatures.xsd\"> ");
            out.println("<schemaFeatures>");
            out.println("<gml:featureMember>");
            out.println("<Feature>");
            out.println("<featureType>JCSOutput</featureType>");
            out.println("<property name=\"nr\">text</property>");
            out.println("<property name=\"art\">long</property>");
            out.println("<property name=\"aus\">long</property>");
            out.println("<property name=\"alter\">long</property>");
            out.println("<property name=\"radius\">long</property>");
            out.println("<property name=\"hoehe\">long</property>");
            out.println("<property name=\"kradius\">long</property>");
            out.println("<property name=\"kansatz\">long</property>");
            out.println("<property name=\"c66xy\">long</property>");
            out.println("<property name=\"c66cxy\">long</property>");
            out.println("<property name=\"zbaum\">long</property>");
            out.println("</Feature>");
            out.println("</gml:featureMember>");
            out.println("</schemaFeatures>");

            out.println("<dataFeatures>");

            for(int i=0;i<st.ntrees;i++){ 
               out.println("<gml:featureMember>");
               out.println("<Feature>");
               out.println("<featureType>JCSOutput</featureType>");
               out.println("<property name=\"gml2_coordsys\"></property>");
               out.println("<gml:PointProperty>");
               out.println("<gml:Point>");
               out.println("<gml:coordinates>"+f.format(st.tr[i].x)+","+f.format(st.tr[i].y)+"</gml:coordinates>");
               out.println("</gml:Point>");
               out.println("</gml:PointProperty>");
               out.println("<property name=\"nr\">"+st.tr[i].no+"</property>");
               out.println("<property name=\"art\">"+st.tr[i].code+"</property>");
               out.println("<property name=\"aus\">"+st.tr[i].out+"</property>");
               out.println("<property name=\"alter\">"+st.tr[i].age+"</property>");
               out.println("<property name=\"radius\">"+f.format(st.tr[i].d/200)+"</property>");
               out.println("<property name=\"hoehe\">"+f.format(st.tr[i].h)+"</property>");
               out.println("<property name=\"kradius\">"+f.format(st.tr[i].cw/2)+"</property>");
               out.println("<property name=\"kansatz\">"+f.format(st.tr[i].cb)+"</property>");
               out.println("<property name=\"c66xy\">"+f.format(st.tr[i].c66xy)+"</property>");
               out.println("<property name=\"c66cxy\">"+f.format(st.tr[i].c66cxy)+"</property>");
               out.println("<property name=\"zbaum\">"+st.tr[i].crop+"</property>");
               out.println("</Feature>");
               out.println("</gml:featureMember>");
            }
            out.println("</dataFeatures>");
            out.println("</dataset>");
            out.println();
            out.close();
	}
	catch (Exception e) {	System.out.println(e); }
//
//
        try {
/** all data is writen in File info/treelist.html */
            OutputStream os=new FileOutputStream(filename+"_area.gml");
	    PrintWriter out= new PrintWriter(new OutputStreamWriter(os));
            out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            out.println("<dataset xmlns=\"http://www.safe.com/xml/schemas/FMEFeatures\" xmlns:fme=\"http://www.safe.com/xml/schemas/FMEFeatures\" "+
                    "xmlns:gml=\"http://www.opengis.net/gml\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "+
                    "xsi:schemaLocation=\"http://www.safe.com/xml/schemas/FMEFeatures FMEFeatures.xsd\"> ");
            out.println("<schemaFeatures>");
            out.println("<gml:featureMember>");
            out.println("<Feature>");
            out.println("<featureType>JCSOutput</featureType>");
            out.println("</Feature>");
            out.println("</gml:featureMember>");
            out.println("</schemaFeatures>");

               out.println("<dataFeatures>");
               out.println("<gml:featureMember>");
               out.println("<Feature>");
               out.println("<featureType>JCSOutput</featureType>");
               out.println("<property name=\"gml2_coordsys\"></property>");
               out.println("<gml:PolygonProperty>");
               out.println("<gml:Polygon><gml:outerBoundaryIs><gml:LinearRing>");
               out.println("<gml:coordinates>");
               for(int i=0;i<st.ncpnt;i++)
                      out.println(f.format(st.cpnt[i].x)+","+f.format(st.cpnt[i].y));
               if( st.ncpnt>0) out.println(f.format(st.cpnt[0].x)+","+f.format(st.cpnt[0].y));
               out.println("</gml:coordinates>");

               out.println("</gml:LinearRing></gml:outerBoundaryIs></gml:Polygon>");
               out.println("</gml:PolygonProperty>");
               out.println("</Feature>");
               out.println("</gml:featureMember>");
               out.println("</dataFeatures>");
               out.println("</dataset>");
               out.println();
               out.close();
	}
	catch (Exception e) {	System.out.println(e); }

 
    }


    
    private Element addString(Element elt, String variable, String text){
        Element var = new Element(variable);
        var.addContent(text);  
        elt.addContent(var);
        return elt;
    }

    private Element addString(Element rootElt, String string) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}


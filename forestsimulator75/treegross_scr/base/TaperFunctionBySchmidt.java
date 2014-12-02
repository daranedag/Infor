/* http://www.nw-fva.de
   Version 07-11-2008

   (c) 2002 Juergen Nagel, Northwest German Forest Research Station, 
       Grätzelstr.2, 37079 Göttingen, Germany
       E-Mail: Juergen.Nagel@nw-fva.de
 
This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation.

This program is distributed in the hope that it will be useful,
but WITHOUT  WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.
 */
/** TreeGrOSS : class taper function by Brink calculates volume and diameter
   Version 11-NOV-2004  
For more information see:

SCHMIDT, M. (2001): Prognosemodelle für ausgewählte Holzqualitätsmerkmale wichtiger Baumarten.
Dissertation Univ. Göttingen. S.302 (http:/ /webdoc.sub.gwdg.de/diss/2002/schmidt/index.html)

or
  
NAGEL, J. (1999): Konzeptionelle Überlegungen zum schrittweisen Aufbau eines
waldwachstumskundlichen Simulationssystems für Nordwestdeutschland.
Schriften aus der Forstlichen Fakultät der Universität Göttingen und der
Nieders. Forstl. Versuchsanstalt, Band 128, J.D. Sauerländer's Verlag,
Frankfurt a.M., S.122

or

BWINPro User's Manual http://www.nfv.gwdg/nfvabw01.htm

*/
package treegross.base;
import java. io. Serializable;

 public class TaperFunctionBySchmidt implements Serializable, PlugInTaperFunction
{  double a[];                                    // array for parameter value 
   double b[];
   boolean abReady=false;
   String info="";
   int numberOfFunctions=0;
   boolean hardwood=true;
/*   void initTaperFunction()
     {   a= new double[3];
         b= new double[3];
     }
 */
// load Parameter values, spcode=species code according to Lower Saxony
public void loadParameter(int funNo)
     {  //System.out.println(" Laden der Parameter "+spcode+"  ");
        if (abReady == false) {
           a= new double[3];
           b= new double[3]; 
           abReady = true;
        }
        numberOfFunctions=5;
        if (funNo ==0)  //beech
        { info="beech Schmidt(2001)";hardwood=true;
          a[0]=0.694614;a[1]=0.086273;a[2]=0.135984;}
        if (funNo == 1)    // Oaks 
        { info="oak Schmidt(2001)";hardwood=true;
          a[0]=0.569877;a[1]=0.045065;a[2]=0.245294;}
        if (funNo ==2 ) //spruce
        { info="spruce Schmidt(2001)";hardwood=false;
          a[0]=-0.2232695;a[1]=1.595027;a[2]=-3.154957;b[0]=0.5119944;b[1]=-0.1575136;b[2]=-0.501926;}
        if (funNo ==3 )    //Pines
        { info="pine Schmidt(2001)";hardwood=false;
          a[0]=-1.725752;a[1]=1.331127;a[2]=-0.701594;b[0]=0;b[1]=-0.2141575;b[2]=0.130589;}  
        if (funNo ==4 )   //Douglas fir 
        { info="Douglas fir Schmidt(2001)";hardwood=false;
          a[0]=-0.5828175;a[1]=1.442282;a[2]=-2.18071;b[0]=0.436905;b[1]=-0.200800;b[2]=-0.283568;}
        if (funNo ==5 )   //Abies grandis 2009 
        { info="Abies grandis Schmidt(2009)";hardwood=false;
          a[0]=1.96957;a[1]=1.02877;a[2]=-0.62621;b[0]=1.49214;b[1]=-0.41308;b[2]=0.45226;}
         // System.out.println(" Laden der Parameter ENDE "+a[0]+"  "+b[0]);    
     }    

public double barkreduce(int funNo,double D)
{
  double bark=0.0;
  {
  if (funNo==0) {bark= 2.61029+0.28522*D;}                  //Rotbuche                      Haya
  if (funNo==1) {bark=10.18342+0.68997*D;}                  //Eiche,Stieleiche              Roble, Roble Inglés
  if (funNo==2) {bark= 0.85149+0.60934*D-0.00228*D*D;}      //Baumartengruppe Fichte        Abeto y especies agrupadas
  if (funNo==3) {bark=-2.13785+0.91597*D-0.00375*D*D;}      //Baumartengruppe Kiefer        Pino y especies agrupadas
  if (funNo==4) {bark= 1.59099+0.50146*D;}                   //Baumartengruppe Douglasie    Abeto de douglas y especies agrupadas
  if (funNo==5) {bark= 0.85149+0.60934*D-0.00228*D*D;}      //Baumartengruppe Fichte        Abeto y especies agrupadas
/*  if ((spcode>=500) && (spcode<=511)) {bark= 0.85149+0.60934*D-0.00228*D*D;}      //Baumartengruppe Fichte      Abeto y especies agrupadas
  if ((spcode>=513) && (spcode<=519)) {bark= 0.85149+0.60934*D-0.00228*D*D;}      //Baumartengruppe Fichte        Abeto y especies agrupadas
  if (spcode==512)                   {bark= 0.05167+0.81782*D-0.00968*D*D;}      //Sitkafichte                    Abeto Sitka
  if ((spcode>=520) && (spcode<=550)) {bark= 1.76896+0.59175*D;}                  //Baumartengruppe Tanne         Abeto y especies agrupadas
  if ((spcode>=552) && (spcode<=599)) {bark= 1.76896+0.59175*D;}                  //Baumartengruppe Tanne         Abeto y especies agrupadas
  if (spcode==551)                   {bark=-2.13785+0.91597*D-0.00375*D*D;}
  if ((spcode>=600) && (spcode<=699)) {bark=-2.13785+0.91597*D-0.00375*D*D;}
  if ((spcode>=700) && (spcode<=711)) {bark= 1.59099+0.50146*D;}
  if ((spcode>=713) && (spcode<=730)) {bark= 1.59099+0.50146*D;}
  if ((spcode>=732) && (spcode<=799)) {bark= 1.59099+0.50146*D;}
  if (spcode==712)                    {bark= 5.27169+1.12602*D;}
  if (spcode==731)                    {bark= 3.63666+0.50782*D;}
  if ((spcode>=800) && (spcode<=811)) {bark= 3.58012+1.03147*D;}
  if ((spcode>=813) && (spcode<=899)) {bark= 3.58012+1.03147*D;}
  if (spcode==812)                    {bark=-3.77073+1.29960*D;}
                                                                                 //Ende Nadelholz         Fin madera blanda
  if ((spcode>=110) && (spcode<=111)) {bark=10.18342+0.68997*D;}                  //Eiche,Stieleiche      Roble
  if (spcode==112)                    {bark=14.31589+0.72699*D;}                  //Traubeneiche          Roble albar
  if (spcode==441)                    {bark=14.31589+0.72699*D;}                  //Weide                 Sauce
  if (spcode==113)                    {bark=-3.19581+0.93891*D-0.00596*D*D;}      //Roteiche              Roble rojo
  if (spcode==211)                    {bark= 2.61029+0.28522*D;}                  //Rotbuche              Haya
  if (spcode==221)                    {bark= 7.47159+0.20957*D;}                  //Hainbuche             Carpe
  if (spcode==311)                    {bark=-7.97623+1.40182*D-0.01011*D*D;}      //Esche                 Fresno
  if (spcode==321)                    {bark=-0.62466+0.73312*D-0.00482*D*D;}      //Bergahorn             Sicomoro
  if (spcode==322)                    {bark= 7.43957+0.43244*D;}                  //Spitzahorn            Arce
  if ((spcode>=330) && (spcode<=331)) {bark= 8.26574+0.89505*D;}                  //Bergulme,Ulmen        Olmo
  if ((spcode>=340) && (spcode<=342)) {bark= 1.39565+0.65024*D;}                  //Linden                Tilo
  if ((spcode>=351) && (spcode<=353)) {bark=-2.57381+1.69622*D;}                  //Robinie,Esskastanie,Nuss   Locust, castaño, nogal
  if (spcode==442)                   {bark=-2.57381+1.69622*D;}                  //Rosskastanie            Buckeye, castaño de indias
  if ((spcode>=354) && (spcode<=356)) {bark= 4.05603+0.58080*D;}                  //Kirsche,(Wildapfel,Wildbirne,Speierling,         Cereza                      
  if ((spcode>=358) && (spcode<=359)) {bark= 4.05603+0.58080*D;}                  //...
  if ((spcode>=364) && (spcode<=365)) {bark= 4.05603+0.58080*D;}                  //...
  if ((spcode>=451) && (spcode<=452)) {bark= 4.05603+0.58080*D;}                  // Mehlbeere,Mispel,Wildzwetschge,Eberesche,Spätbl.Traubenkir.        Mostajo, níspero, ciruela silvestre, fresno de montaña, cerezo negro 
  if (spcode==323)                   {bark= 1.63138+0.78958*D;}                  //Feldahorn            Arce silvestre
  if ((spcode>=410) && (spcode<=413)) {bark= 1.63138+0.78958*D;}                  //Birke,Sandbirke,Moorbirke,(Japanische Birke)              Abedul, abedul, abedul pubescente, (abedul japonés)                          
  if (spcode==357)                   {bark=-1.26961+1.21661*D-0.00624*D*D;}      //Elsbeere           Sorbo silvestre 
  if ((spcode>=361) && (spcode<=363)) {bark=-1.26961+1.21661*D-0.00624*D*D;}      //Tulpenbaum,Hickory,Platane        Árbol de tulipán, nogal, sicomoro
  if ((spcode>=432) && (spcode<=434)) {bark=-1.26961+1.21661*D-0.00624*D*D;}      //Pappeln                           Álamos
  if ((spcode>=421) && (spcode<=423)) {bark= 8.05239+0.84910*D;}                  //Erlen                             Aliso    
  if (spcode==431)                   {bark= 9.88855+0.56734*D;}                  //Aspe,                            ???
  if ((spcode>=332) && (spcode<=333)) {bark= 9.88855+0.56734*D;}                  //Flatterulme,Feldulme            Olmo blanco, Olmo
 **/
  }                                                                              //Ende Laubholz        Fin madera dura
  bark=bark/10.0;
  return bark;
} // end barkreduce

/** finds the diameter at a given height */
public double getDiameterEst(int funNo,double dbh,double height,double h,int barkindex,int sortindex)      //Berechnet Schaftradius bei gegebener stemheight h      Radio del eje calculado  dada una altura h del tallo/tronco
                                                                                                  //bei Rindabindex=1 wird die doppelte Rindenstärke vom Durchmesser abgezogen, als Eingangsvariable für die Berechnung der doppelten Rindenstärke wird der abgerundete Schaftdurchmesser benötigt    en Rindabindex = 1 el espesor doble corteza se resta del diámetro, como variable de entrada para el cálculo del espesor de la corteza del diámetro del vástago redondeado doble se requiere                                                                                             
                                                                                                  //bei Forstindex=1 wird der Schaftdurchmesser mit Rinde auf ganze cm abgerundet  en Forstindex=1 se redondeará el diametro del eje con corteza al cm 
{ double u,v,w,i;
  double alpha,beta,Hilfsheight,diameter;  
  int testa0;diameter=0.0;
//  System.out.println(dbh+" "+height+"  "+spcode);   

  loadParameter(funNo);                                                                          //damit h im Procedurkopf [0,1] eingegeben werden kann  para que a altura h entre 0 y 1 pueda entrar
  h=h;
//  System.out.println(dbh+" "+height);   

  if (hardwood==true)
  {                             
    i=a[0]*dbh/2;
    u=i/(1-Math.exp(a[2]*(1.3-height)))+(dbh/2-i)*(1-1/(1-Math.exp(a[1]*(1.3-height))));
    v=(dbh/2-i)*Math.exp(a[1]*1.3)/(1-Math.exp(a[1]*(1.3-height)));
    w=i*Math.exp(-a[2]*height)/(1-Math.exp(a[2]*(1.3-height)));
    if (sortindex==1)
    {
      diameter=((int)(2*(u+v*Math.exp(-a[1]*h)-w*Math.exp(a[2]*h))));
      if (barkindex==1)
      {
        diameter=((int)(2*(u+v*Math.exp(-a[1]*h)-w*Math.exp(a[2]*h))))
        -barkreduce(funNo,((int)(2*(u+v*Math.exp(-a[1]*h)-w*Math.exp(a[2]*h))))*1.0);
      }
    }
    else
    {  
      diameter=2*(u+v*Math.exp(-a[1]*h)-w*Math.exp(a[2]*h));
      if (barkindex==1)
      {
        diameter=(2*(u+v*Math.exp(-a[1]*h)-w*Math.exp(a[2]*h)))
        -barkreduce(funNo,((int)(2*(u+v*Math.exp(-a[1]*h)-w*Math.exp(a[2]*h))))*1.0);
      }
    }
  }
  else //if (hardwood==false)
  {
   // System.out.println(dbh+" else if "+height);   

    testa0=(int)(Math.round(a[0]*10.0));  
   // System.out.println(dbh+" "+testa0);   
                                                                     //über den gerundeten Koeffizienten a0 wird die Baumartengruppe ermittelt   sobre los coeficientes a0 redondeados se determinará el grupo de especies
    if ((testa0==-2) && ((height/dbh)>=0.5585457+1.451182*Math.exp(Math.log(dbh)*(-0.03046118*dbh))))        //9.10.01;Extremfälle für Fichtenkodierte abfangen  Casos extremos de intercepción para codificado abeto
       {Hilfsheight=height;}                                                                     //Überprüfung ob der h/d-wert im Extrapolationsbereich bezüglich eines dbh und Baumarten spezifischen h/d-Wertes liegt       Comprobación si el valor h/d está en la extrapolación con respecto a un dbh y especies especificas con valores h/d
    else if ((testa0==-2) && ((height/dbh)<0.5585457+1.451182*Math.exp(Math.log(dbh)*(-0.03046118*dbh))))    //falls im Extrapolationsbereich wird für die Berechnung der formbeschreibenden Koeffizienten alpha und beta eine Hilfsheight verwendet, die aus dem dbh und dem zugehörigen Grenz h/d-Wert resultiert     Si la extrapolación se utiliza para el calculo de los coeficientes alpha y beta de una altura auxiliar, que resulta de la asociación del dbh y su correspondiente limite del valor h/d.
       {Hilfsheight=(0.5585457+1.451182*Math.exp(Math.log(dbh)*(-0.03046118*dbh)))*dbh;}
    else if ((testa0==-6) && ((height/dbh)>=0.5548092+0.3495206*Math.exp(Math.log(dbh)*(-0.06206942*dbh))))  //9.10.01;Extremfälle für Douglasie abfangen  Casos extremos de abeto Douglas
       {Hilfsheight=height;}
    else if ((testa0==-6) && ((height/dbh)<0.5548092+0.3495206*Math.exp(Math.log(dbh)*(-0.06206942*dbh))))
       {Hilfsheight=(0.5548092+0.3495206*Math.exp(Math.log(dbh)*(-0.06206942*dbh)))*dbh;}
    else if ((testa0==-17) && ((height/dbh)>=0.4276964+0.9883941*Math.exp(Math.log(dbh)*(-0.02910625*dbh)))) //9.10.01;Extremfälle Kiefernkodierte abfangen  Casos extremos pino
       {Hilfsheight=height;}
    else if ((testa0==-17) && ((height/dbh)<0.4276964+0.9883941*Math.exp(Math.log(dbh)*(-0.02910625*dbh))))
       {Hilfsheight=(0.4276964+0.9883941*Math.exp(Math.log(dbh)*(-0.02910625*dbh)))*dbh;}
    else {Hilfsheight=height;}
    alpha=a[0]+a[1]*(1.0/Math.log(Math.exp(Math.log(Hilfsheight)*(1.0/dbh))))+a[2]*(1.0/Math.pow((Hilfsheight/dbh),2.0));
    beta=b[0]+b[1]*(1.0/Math.log(Math.exp(Math.log(Hilfsheight)*(1.0/dbh))))+b[2]*(1.0/Math.pow((Hilfsheight/dbh),2.0));
//    System.out.println(alpha+" alpha u. beta "+beta+" "+Hilfsheight);   
    if (sortindex==1)
    {
      diameter=(int)(2.0*(alpha*(1.0-Math.exp(Math.log(h/height)*3))+beta*Math.log(h/height)));                        //für die Berechnung der Schaftdurchmesser wird selbstverständlich die richtige Höhe verwendet    para el calculo del diametro se utilizará la altura mas obviamente correcta
      if (barkindex==1)
      {
        diameter=(int)((2.0*(alpha*(1.0-Math.exp(Math.log(h/height)*3.0))+beta*Math.log(h/height))))-
        barkreduce(funNo,(1.0*(int)((2.0*(alpha*(1.0-Math.exp(Math.log(h/height)*3.0))+beta*Math.log(h/height))))));
      }
    }
    else
    {
      diameter=2*(alpha*(1-Math.exp(Math.log(h/height)*3))+beta*Math.log(h/height));
      if (barkindex==1)
      {
        diameter=(2*(alpha*(1-Math.exp(Math.log(h/height)*3))+beta*Math.log(h/height)))-
        barkreduce(funNo,(1.0*(int)((2*(alpha*(1-Math.exp(Math.log(h/height)*3))+beta*Math.log(h/height))))));
      }
    }
  }                                                                                               //spcode >=500
return diameter;
}              


/** finds the height for a given diameter */

public double getLengthEst(int funNo, double dbh,double height,double stemd)         //sucht Höhe zu einem vorgegebenen Durchmesser: stemd, 
{
  double u,v,w,i,Durchmesser,testDurchmesser1,testDurchmesser2,testh1,testh11,testh2,alpha,beta,Hilfshoehe,length;
  testh1=0.0;testh11=0.0;
  double h[];
  int j,r,testa0;
  loadParameter(funNo);
  h = new double[1000];
  testDurchmesser1=150;
  double Length=0;                                         //Startabstand zwischen Vorgabe schaftd und imaginärem Schaftdurchmesser}    Distancia inicial entre set schaftd y diametro del eje imaginario
  if (hardwood==true)
  {
    i=a[0]*dbh/2;
    u=i/(1-Math.exp(a[2]*(1.3-height)))+(dbh/2-i)*(1-1/(1-Math.exp(a[1]*(1.3-height))));
    v=(dbh/2-i)*Math.exp(a[1]*1.3)/(1-Math.exp(a[1]*(1.3-height)));
    w=i*Math.exp(-a[2]*height)/(1-Math.exp(a[2]*(1.3-height)));
    for (j=1;j<=500;j++)                                           //Schrittweite ist 10cm, Maximalhöhe, die getestet wird, ist 50 m}       Incremento es de 10 cm, Altura maxima, que será probada es de 50 m
    {
      h[j]=j/10;
      if (h[j]<height) 
      {
        Durchmesser=2*(u+v*Math.exp(-a[1]*h[j])-w*Math.exp(a[2]*h[j]));   //Durchmesser in Stammhöhe h[j] berechnen    Calculo del diametro del tronco a altura h[j]
        testDurchmesser2=Durchmesser-stemd;                     //Vergleich mit Vorgabe stemd     Comparar con stemd por defecto
        if (Math.abs(testDurchmesser2)<=testDurchmesser1)              //wenn sich die Differenz zwischen der Vorgabe und dem berechneten Schaftdurchmesser verringert neuen Vergleichswert festlegen   Cuando la diferencia entre el valor por defecto y el diametro calculado se reduzca, se define un nuevo valor de comparacion
         {
           testDurchmesser1=Math.abs(testDurchmesser2);
           testh1=h[j];                                         //zugehörige Höhe merken, in dieser Schleife wird die richtige Höhe auf dm genau ermittelt  Recuerda la altura asociada, en este bucle se determinará la altura correcta de manera exacta
         }
      }  
    }
    for (r=-9;r<=9;r++)                                            //Plus und Minus 9 cm um den grob ermittelten Wert   Se determina más y menos 9 cm del valor bruto
    {
       testh2=testh1+r/100;
       if (testh2<height)
       {
       Durchmesser=2*(u+v*Math.exp(-a[1]*(testh2))-w*Math.exp(a[2]*(testh2)));
       testDurchmesser2=Durchmesser-stemd;
       if (Math.abs(testDurchmesser2)<=testDurchmesser1 )
          {
           testDurchmesser1=Math.abs(testDurchmesser2);
           testh11=testh2;                                          //zugehörige Höhe merken, in dieser Schleife wird die richtige Höhe auf cm genau ermittelt    Recuerda la altura asociada, en este bucle se determinará la altura correcta de manera exacta
          }
       }
    }
  Length=testh11;
  }                                                               //if spcode <=511
  else // if (hardwood==false)
  {
    testa0=(int)(Math.round(a[0]*10));                                                                          //über den gerundeten Koeffizienten a0 wird die Baumartengruppe ermittelt}
    if ((testa0==-2) && ((height/dbh)>=0.5585457+1.451182*Math.exp(Math.log(dbh)*(-0.03046118*dbh))))         //9.10.01;Extremfälle für Fichtenkodierte abfangen}
      {Hilfshoehe=height;}
    else if ((testa0==-2) &&((height/dbh)<0.5585457+1.451182*Math.exp(Math.log(dbh)*(-0.03046118*dbh))))
      {Hilfshoehe=(0.5585457+1.451182*Math.exp(Math.log(dbh)*(-0.03046118*dbh)))*dbh;}
    else if ((testa0==-6) && ((height/dbh)>=0.5548092+0.3495206*Math.exp(Math.log(dbh)*(-0.06206942*dbh))))   //9.10.01;Extremfälle für Douglasie abfangen}
      {Hilfshoehe=height;}
    else if ((testa0==-6) && ((height/dbh)<0.5548092+0.3495206*Math.exp(Math.log(dbh)*(-0.06206942*dbh))))
      {Hilfshoehe=(0.5548092+0.3495206*Math.exp(Math.log(dbh)*(-0.06206942*dbh)))*dbh;}
    else if ((testa0==-17) && ((height/dbh)>=0.4276964+0.9883941*Math.exp(Math.log(dbh)*(-0.02910625*dbh))))   //9.10.01;Extremfälle Kiefernkodierte abfangen}
      {Hilfshoehe=height;}
    else if ((testa0==-17) && ((height/dbh)<0.4276964+0.9883941*Math.exp(Math.log(dbh)*(-0.02910625*dbh))))
      {Hilfshoehe=(0.4276964+0.9883941*Math.exp(Math.log(dbh)*(-0.02910625*dbh)))*dbh;}
    else Hilfshoehe=height;
    alpha=a[0]+a[1]*(1/Math.log(Math.exp(Math.log(Hilfshoehe)*(1/dbh))))+a[2]*(1/Math.pow((Hilfshoehe/dbh),2.0));
    beta=b[0]+b[1]*(1/Math.log(Math.exp(Math.log(Hilfshoehe)*(1/dbh))))+b[2]*(1/Math.pow((Hilfshoehe/dbh),2.0));
    for (j=1;j<=600;j++)                                                                               //10 cm Schrittweite:  Maximalhöhe, die getestet wird, ist 60 m}   Incremento de 10 cm, altura máxima a probar será de 60 metros
    {
      h[j]=j/10;   
      if (h[j]<height) 
      {
        Durchmesser=2*(alpha*(1-Math.exp(Math.log(h[j]/height)*3))+beta*Math.log(h[j]/height));
        testDurchmesser2=Durchmesser-stemd;
        if (Math.abs(testDurchmesser2)<testDurchmesser1) 
        {
          testDurchmesser1=Math.abs(testDurchmesser2);
          testh1=h[j];
        }
      }
    }
    for (r=-9;r<=9;r++)                                                                                //Plus minus 9 cm um den grob ermittelten Wert, Schrittweite 1 cm  Mas menos 9 cm del valor bruto determinado, Incremento 1 cm.
    {
       testh2=testh1+r/100;
       if (testh2<height) 
       {
         Durchmesser=2*(alpha*(1-Math.exp(Math.log(testh2/height)*3))+beta*Math.log(testh2/height));
         testDurchmesser2=Durchmesser-stemd;
         if (Math.abs(testDurchmesser2)<=testDurchmesser1) 
         {
           testDurchmesser1=Math.abs(testDurchmesser2);
           testh11=testh2;
         }
       }
    } 
  Length=testh11;
  }                                                                                                 // spcode>=500                                                  
  return Length;
}


   /** calculates the taper functionvolume according to the modell of Pain and Boyer 1996 / modell of Riemer et al. 
    species code according to the code of Lower Saxony */

    public double getCumVolume(int funNo, double dbh,double height,double h,int barkindex,int sortindex)                   //bei sortindex=1 wird der Schaftdurchmesser mit Rinde auf ganze cm abgerundet,bei barkindex=1 wird die doppelte Rindenstärke vom Durchmesser abgezogen, als Eingangsvariable für die Berechnung der doppelten Rindenstärke wird der abgerundete Schaftdurchmesser benötigt.
{
 
    double bh=1.3;      
    double Vol,Volsum,Extrapol;                       
    int i;
    double r=0;
    Volsum=0.0;
    Vol=0.0;
    if (hardwood!=true)
    {	
// Calculation in 4 cm pieces, over 0,6 m use function other cone      
      if (h >0.60)                                                                                             //oberhalb von 60 cm Höhe direkt über die Funktion     por encima de 60 cm de altura sobre la funcion
      {                    
          for (i=15;i<=(int)(h*25);i++)                    
          { 
            r=i*4;                                                                                              //Berechnung des Volumens in 4 cm Schritten           Calculo del volumen en pasos de 4 cm                       
            Vol=Math.PI*4/3*(Math.pow
            ((getDiameterEst(funNo,dbh,height,(r/100),barkindex,sortindex)/2),2.0)+
            getDiameterEst(funNo,dbh,height,(r/100),barkindex,sortindex)/2*
            getDiameterEst(funNo,dbh,height,((r+4)/100),barkindex,sortindex)/2+Math.pow(
            (getDiameterEst(funNo,dbh,height,((r+4)/100),barkindex,sortindex)/2),2.0));
            Volsum=Volsum+Vol/1000000;
//            System.out.println("Nadelholz "+i+" "+Vol+" "+Volsum+" "+getDiameterEst(spcode,dbh,height,(r/100),barkindex,sortindex)+
//                  " "+getDiameterEst(spcode,dbh,height,((r+4)/100),barkindex,sortindex)+" "+(r/100) );
          }
//calculation of the last 3cm in 1 cm steps          
          if ((int)((h*25)*4)<((int)(h*100)))                                                                  //bei 4 cm Schritten können an der Baumspitze max. 3 cm fehlen      
          {
            for (i=(int)((h*25)*4);i<=(int)(h*100);i++)
            {
              r=i;  
              Vol=Math.PI*1/3*(Math.pow(                                                                                //Höhe des Kegelstumpfes: 1 cm   Altura del cono truncado
              (getDiameterEst(funNo,dbh,height,(r/100),barkindex,sortindex)/2),2.0)+
              getDiameterEst(funNo,dbh,height,(r/100),barkindex,sortindex)/2*
              getDiameterEst(funNo,dbh,height,((r+1)/100),barkindex,sortindex)/2+Math.pow(
              (getDiameterEst(funNo,dbh,height,((r+1)/100),barkindex,sortindex)/2),2.0));
              Volsum=Volsum+Vol/1000000;
            }
          }
//die Painfunktion erfordert im Bereich unterhalb von 60 cm Stammhöhe eine Extrapolation   la función de dolor requiere un rango por debajo de 60cm de la altura extrapolada del tallo/tronco
          Extrapol=getDiameterEst(funNo,dbh,height,0.6,barkindex,sortindex)-                                      //Berechnung des Stammdurchmessers in 60 cm Höhe    Calculo del diametro del tronco a 60 cm de altura
          getDiameterEst(funNo,dbh,height,1.2,barkindex,sortindex)+                                               //Berechnung des Stammdurchmessers in 120 cm Höhe  Calculo del diametro del tronco a 120 cm de altura
          getDiameterEst(funNo,dbh,height,0.6,barkindex,sortindex);                                               //Die Differenz wird zum Stammdurchmesser in 60 Höhe addiert   La diferencia se sumara del diametro del tronco en 60 altura
          Vol=Math.PI*60/3*(Math.pow(                                                                                   //Höhe des Kegelstumpfes: 60 cm}   Altura del cono truncado
          (getDiameterEst(funNo,dbh,height,0.6,barkindex,sortindex)/2),2.0)+
          getDiameterEst(funNo,dbh,height,0.6,barkindex,sortindex)/2*Extrapol/2+
          Math.pow((Extrapol/2),2.0));
          Volsum=Volsum+Vol/1000000;
      }
      else                                                                                                   //(h<=0.6)
      {
          Extrapol=getDiameterEst(funNo,dbh,height,0.6,barkindex,sortindex)-                                      //s.o.
          getDiameterEst(funNo,dbh,height,1.2,barkindex,sortindex)+
          getDiameterEst(funNo,dbh,height,0.6,barkindex,sortindex);
          Vol=Math.PI*h*100/3*(Math.pow(                                                                                //variable Höhe [0.1,0.6] des Kegelstumpfes je nach Sortiervorgabe   altura variable del cono truncado dependiendo de la especificacion de la especie
          (getDiameterEst(funNo,dbh,height,0.6,barkindex,sortindex)/2),2.0)+
          getDiameterEst(funNo,dbh,height,0.6,barkindex,sortindex)/2*Extrapol/2+
          Math.pow((Extrapol/2),2.0));
          Volsum=Volsum+Vol/1000000;
      }
    }
    else //if (hardwood==false)                                                                                       //broadleafed tree species
    {                             
      for (i=1;i<=(int)(h*25);i++)                                                                                //Berechnung des Volumens in 4 cm Schritten   Calculo del volumen en pasos de 4 cm
      {
          r=i*4;
          Vol=Math.PI*4/3*(Math.pow((getDiameterEst(funNo,dbh,height,(r/100),barkindex,sortindex)/2),2.0)+
          getDiameterEst(funNo,dbh,height,(r/100),barkindex,sortindex)/2*
          getDiameterEst(funNo,dbh,height,((r+4)/100),barkindex,sortindex)/2+Math.pow(
          (getDiameterEst(funNo,dbh,height,((r+4)/100),barkindex,sortindex)/2),2.0));
          Volsum=Volsum+Vol/1000000;
      }
      if (((int)(h*25)*4)<((int)(h*100)))                                                                       //bei 4 cm Schritten können an der Baumspitze max. 3 cm fehlen   A los 4 cm de incremento se puede mirar en la copa del arbol maximos 3 cm faltantes
      {
        for (i=(int)((h*25)*4);i<=(int)(h*100);i++)                                                              //Berechnung der letzten max. 3 cm mit 1cm Schrittweite   Calculo de los ultimos maximos 3 cm con pasos de 1 cm
        {
          Vol=Math.PI*1/3*(Math.pow((getDiameterEst(funNo,dbh,height,(r/100),barkindex,sortindex)/2),2.0)+                         //Höhe des Kegelstumpfes: 1 cm       
          getDiameterEst(funNo,dbh,height,(r/100),barkindex,sortindex)/2*
          getDiameterEst(funNo,dbh,height,((r+1)/100),barkindex,sortindex)/2+Math.pow(
          (getDiameterEst(funNo,dbh,height,((r+1)/100),barkindex,sortindex)/2),2.0));
          Volsum=Volsum+Vol/1000000;
        }
      }
    }                                                                                                          //spcode >=500 or <500: Unterscheidung Nadel- und Laubholz   Distinción de madera blanda y dura
    return Volsum;
}

  public int getNumberOfFunctions(){
       loadParameter(0);
       return numberOfFunctions;
   }
   public String getFunctionName(int funNo){
       loadParameter(funNo);
       return info;
   }    

   public int getFunctionNumber(int speciesCode){
         int funNo=0;
         if (speciesCode < 100) funNo=0;                  //Rotbuche  Haya europea
         if (speciesCode >= 100 && speciesCode <200) funNo=1;               //Eiche,Stieleiche    Roble, roble ingles
         if (speciesCode >= 200 && speciesCode <500) funNo=0;               //Eiche,Stieleiche    Roble, roble ingles
         if (speciesCode >= 500) funNo=2;       //Spruce   Picea
         if (speciesCode >= 700 && speciesCode <800) funNo=3;               //Pine
         if (speciesCode == 611 ) funNo=4;               //Douglas fir
         if (speciesCode == 523 ) funNo=5;               //Abies grandis
         return funNo;
   }
  
//
//                         
}


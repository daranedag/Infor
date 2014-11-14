/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package treegross.treatment;

import treegross.base.Stand;

/**
 *
 * @author nagel
 */
public class HessRied_BZT_Regel {

    public int getWET(Stand st){
        int wet = -1;
// Hauptbaumart festlegen
        int art1 = getArt1(st);
        
// 2. Hauptbaumart festlegen
        int art2 = 0;
        double gmax =-90.0;
        for (int i=0;i<st.nspecies;i++ )
            if (gmax < st.sp[i].gha && st.sp[i].code != art1) {
                art2 = st.sp[i].code;
                gmax = st.sp[i].gha;
        }
// 3. Hauptbaumart festlegen
       int art3 = 0;
        gmax =-90.0;
        for (int i=0;i<st.nspecies;i++ )
            if (gmax < st.sp[i].gha && st.sp[i].code != art1 && st.sp[i].code != art2) {
                art3 = st.sp[i].code;
                gmax = st.sp[i].gha;
        }
// Festlegen der WET

        if (art1 == 110 && art2 == 221) wet = 11;
        if (art1 == 110 && art2 == 211) wet = 12;
        if (art1 == 110 && art2 == 321) wet = 13;
        if (art1 == 110 && art2 == 311) wet = 13;
        if (art1 == 110 && art2 == 411) wet = 14;
        if (art1 == 110 && art2 == 711) wet = 17;
        if (art1 == 113 && art2 == 211) wet = 18;
        if (art1 == 113 && art2 == 221) wet = 19;

        if (art1 == 211) wet = 20;
        if (art1 == 211 && art2 == 321) wet = 23;
        if (art1 == 211 && art2 == 311) wet = 23;
        if (art1 == 211 && art2 == 611) wet = 26;
        if (art1 == 211 && art2 == 521) wet = 29;

        if (art1 == 321) wet = 31;
        if (art1 == 311) wet = 33;
        if (art1 == 311 && art2 == 321) wet = 31;
        if (art1 == 311 && art2 == 421) wet = 34;
        if (art1 == 341) wet = 35;
        if (art1 == 321 && art2 == 441) wet = 35;

        if (art1 == 421) wet = 40;
        if (art1 == 431) wet = 42;
        if (art1 == 351) wet = 43;
        if (art1 == 411) wet = 47;
        if (art1 == 411 && art2 == 421) wet = 44;
        if (art1 == 431) wet = 49;

        if (art1 == 731) wet = 55;

        if (art1 == 611) wet = 62;
        if (art1 == 611 && art2 == 711) wet = 67;
        if (art1 == 611 && art2 == 521) wet = 69;

        if (art1 == 711) wet = 70;
        if (art1 == 711 && art2 == 110) wet = 71;
        if (art1 == 711 && art2 == 211) wet = 72;
        if (art1 == 711 && art2 == 341) wet = 73;
        if (art1 == 711 && art2 == 411) wet = 74;
        if (art1 == 711 && art2 == 611) wet = 76;
        if (art1 == 711 && art2 == 423) wet = 79;

        if (art1 == 521) wet = 92;
        if (art1 == 353) wet = 93;
        return wet;
    }

   public int getArt1(Stand st){
        int art1 = 0;
        double gmax =-90.0;
        for (int i=0;i<st.nspecies;i++ )
            if (gmax < st.sp[i].gha) {
                art1 = st.sp[i].code;
                gmax = st.sp[i].gha;
        }
        return art1;
   }

   public int getArt1byWET(int wet){
         int art = 0;  // Lichtbaumart
         if (wet < 18) art=111;
         if (wet >= 18 && wet <=19) art=113;
         if (wet >= 20 && wet <=29) art=211;
         if (wet >= 31 && wet <=34) art=311;
         if (wet == 35) art=321;
         if (wet == 40) art=421;
         if (wet == 42) art=431;
         if (wet == 43) art=351;
         if (wet == 44) art=411;
         if (wet == 47) art=411;
         if (wet == 49) art=430;
         if (wet == 55) art=731;
         if (wet >= 62 && wet <=69) art=611;
         if (wet >= 70 && wet <=79) art=711;
         if (wet == 92) art=523;
         if (wet == 93) art=353;
         return art;
   }

    public String getMixturebyWET(int wet){
         String mix = "";  // Mixture
        if (wet == 11) mix="110[0.80];221[0.15];411[0.05];";
	if (wet == 12) mix="110[0.75];221[0.15];411[0.10];";
	if (wet == 13) mix="110[0.40];211[0.10];311[0.25];321[0.15];421[0.10];";
	if (wet == 14) mix="110[0.60];411[0.40];";
	if (wet == 17) mix="110[0.50];711[0.40];411[0.10];";
	if (wet == 18) mix="113[0.75];211[0.20];411[0.05];";
	if (wet == 19) mix="113[0.75];221[0.20];411[0.05];";

	if (wet == 20) mix="211[0.90];411[0.10];";
	if (wet == 23) mix="211[0.55];311[25];321[0.10];354[0.05];411[0.05];";
	if (wet == 26) mix="211[0.55];611[0.40];411[0.05];";
	if (wet == 29) mix="211[0.50];521[0.45];411[0.05];";

	if (wet == 31) mix="321[0.36];311[0.34];211[0.20];354[0.10];";
	if (wet == 33) mix="311[0.50];321[0.10];110[0.10];211[0.10];341[0.10];354[0.05];411[0.05];";
	if (wet == 34) mix="311[0.55];421[0.40];411[0.05];";
	if (wet == 35) mix="110[0.15];211[0.15];321[0.30];221[0.10];354[0.10];341[0.20];";

	if (wet == 40) mix="421[0.90];411[0.10];";
	if (wet == 42) mix="431[0.50];411[0.40];110[0.05];421[0.05];";
	if (wet == 43) mix="351[0.90];411[0.10];";
	if (wet == 44) mix="411[0.80];421[0.10];711[0.10];";
	if (wet == 47) mix="411[0.70];711[0.15];110[0.15];";
	if (wet == 49) mix="430[0.90];411[0.10];";

	if (wet == 55) mix="731[0.90];411[0.10];";

	if (wet == 62) mix="611[0.80];211[0.20];";
	if (wet == 67) mix="611[0.60];711[0.30];211[0.10];";
	if (wet == 69) mix="611[0.50];521[0.40];211[0.10];";

	if (wet == 70) mix="711[0.85];110[0.10];411[0.05];";
	if (wet == 71) mix="711[0.70];110[0.20];411[0.10];";
	if (wet == 72) mix="711[0.70];211[0.25];411[0.05];";
	if (wet == 73) mix="711[0.50];341[0.45];411[0.05];";
	if (wet == 74) mix="711[0.80];411[0.20];";
	if (wet == 76) mix="711[0.50];611[0.35];211[0.10];411[0.05];";
	if (wet == 79) mix="711[0.90];411[0.10];";

	if (wet == 92) mix="521[0.50];211[0.45];411[0.05];";
	if (wet == 93) mix="353[0.75];221[0.20];411[0.05];";
         return mix;
   }

   public String getZielstbyWET(int wet){
         String zs = "";  // Zielstärke
        	if (wet == 11) zs="110[70];";
         	if (wet == 12) zs="110[60];";
         	if (wet == 13) zs="110[75];311[60];";
	if (wet == 14) zs="110[50];";
	if (wet == 17) zs="110[50];711[40];";
	if (wet == 18) zs="113[65];";
	if (wet == 19) zs="113[65];";

	if (wet == 20) zs="211[55];";
	if (wet == 23) zs="211[60];321[55];";
	if (wet == 26) zs="211[55];611[70];";
	if (wet == 29) zs="211[55];521[45];";

	if (wet == 31) zs="321[60];";
	if (wet == 33) zs="311[40];";
	if (wet == 34) zs="311[60];421[45];";
	if (wet == 35) zs="321[50];";

	if (wet == 40) zs="421[45];";
	if (wet == 42) zs="431[40];";
	if (wet == 43) zs="351[35];";
	if (wet == 44) zs="411[35];";
	if (wet == 47) zs="411[40];711[40];";
	if (wet == 49) zs="431[40];";

	if (wet == 55) zs="731[40];";

	if (wet == 62) zs="611[60];211[50];";
	if (wet == 67) zs="611[60];711[40];";
	if (wet == 69) zs="611[50];521[45];";

	if (wet == 70) zs="711[40];";
	if (wet == 71) zs="711[45];110[50];";
	if (wet == 72) zs="711[45];211[45];";
	if (wet == 73) zs="711[40];";
	if (wet == 74) zs="711[40];411[40];";
	if (wet == 76) zs="711[45];611[60];";
	if (wet == 79) zs="711[40];";

	if (wet == 92) zs="521[45];";
	if (wet == 93) zs="353[50];";

        return zs;
   }



   public  int selectWETbySite(int naehrstoffziffer, int feuchte, int wetreal){
       int wet = -1;
       int weta[] = {0,0,0,0,0,0,0,0,0,0, };
       double wetp[] = {0,0,0,0,0,0,0,0,0,0, };
//
       if(naehrstoffziffer == 1 && feuchte == 7){
           int wetax[] = {34,13,40,31,49,11,35,0,0,0, };
           double wetpx[] = {18,18,18,18,18,5,5,0,0,0, };
                  weta=wetax; wetp=wetpx;

       }
       if(naehrstoffziffer == 1 && feuchte == 5){
           int wetax[] = {31,13,11,23,93,34,35,49,0,0, };
           double wetpx[] = {18,18,18,18,18,3.3,3.3,3.4,0,0, };
                  weta=wetax; wetp=wetpx;

       }
       if(naehrstoffziffer == 1 && feuchte == 46){
           int wetax[] = {11,0,0,0,0,13,0,0,0,0, };
           double wetpx[] = {90,0,0,0,0,10,0,0,0,0, };
                  weta=wetax; wetp=wetpx;

       }
       if(naehrstoffziffer == 1 && feuchte == 2){
           int wetax[] = {31,23,11,0,0,35,13,12,20,0, };
           double wetpx[] = {30,30,30,0,0,2.5,2.5,2.5,2.5,0, };
                  weta=wetax; wetp=wetpx;

       }
       if(naehrstoffziffer == 1 && feuchte == 1){
           int wetax[] = {31,23,93,0,0,11,13,12,20,0, };
           double wetpx[] = {30,30,30,0,0,2.5,2.5,2.5,2.5,0, };
                  weta=wetax; wetp=wetpx;

       }
       if(naehrstoffziffer == 1 && feuchte == 3){
           int wetax[] = {11,12,33,0,0,35,20,0,0,0, };
           double wetpx[] = {30,30,30,0,0,5,5,0,0,0, };
                  weta=wetax; wetp=wetpx;

       }
       if(naehrstoffziffer == 1 && feuchte == 4){
           int wetax[] = {11,12,35,33,0,73,0,0,0,0, };
           double wetpx[] = {22.5,22.5,22.5,22.5,0,10,0,0,0,0, };
                  weta=wetax; wetp=wetpx;

       }
       if(naehrstoffziffer == 1 && feuchte == 9){
           int wetax[] = {73,71,0,0,0,43,0,0,0,0, };
           double wetpx[] = {45,45,0,0,0,10,0,0,0,0, };
                  weta=wetax; wetp=wetpx;

       }
//
       if(naehrstoffziffer == 2 && feuchte == 7){
           int wetax[] = {11,40,14,0,0,0,0,0,0,0, };
           double wetpx[] = {33.3,33.3,33.4,0,0,0,0,0,0,0, };
                  weta=wetax; wetp=wetpx;

       }
       if(naehrstoffziffer == 2 && feuchte == 5){
           int wetax[] = {11,19,12,0,0,20,40,0,0,0, };
           double wetpx[] = {33.3,33.3,33.4,0,0,5,5,0,0,0, };
                  weta=wetax; wetp=wetpx;

       }
       if(naehrstoffziffer == 2 && feuchte == 46){
           int wetax[] = {11,19,0,0,0,14,12,43,74,0, };
           double wetpx[] = {45,45,0,0,0,2.5,2.5,2.5,2.5,0, };
                  weta=wetax; wetp=wetpx;

       }
       if(naehrstoffziffer == 2 && feuchte == 2){
           int wetax[] = {26,62,12,18,29,11,20,0,0,0, };
           double wetpx[] = {18,18,18,18,18,5,5,0,0,0, };
                  weta=wetax; wetp=wetpx;

       }
       if(naehrstoffziffer == 2 && feuchte == 1){
           int wetax[] = {62,26,11,18,72,12,20,92,0,0, };
           double wetpx[] = {18,18,18,18,18,3.3,3.3,3.4,0,0, };
                  weta=wetax; wetp=wetpx;

       }
       if(naehrstoffziffer == 2 && feuchte == 3){
           int wetax[] = {62,19,11,12,72,26,20,92,0,0, };
           double wetpx[] = {18,18,18,18,18,3.3,3.3,3.4,0,0, };
                  weta=wetax; wetp=wetpx;

       }
       if(naehrstoffziffer == 2 && feuchte == 4){
           int wetax[] = {67,18,11,69,74,76,79,72,55,43, };
           double wetpx[] = {15,15,15,15,15,15,2.5,2.5,2.5,2.5, };
                  weta=wetax; wetp=wetpx;

       }
       if(naehrstoffziffer == 2 && feuchte == 9){
           int wetax[] = {70,71,0,0,0,79,43,0,0,0, };
           double wetpx[] = {45,45,0,0,0,5,5,0,0,0, };
                  weta=wetax; wetp=wetpx;

       }
//
       if(naehrstoffziffer == 3 && feuchte == 7){
           int wetax[] = {44,14,0,0,0,42,40,0,0,0, };
           double wetpx[] = {45,45,0,0,0,5,5,0,0,0, };
                  weta=wetax; wetp=wetpx;

       }
       if(naehrstoffziffer == 3 && feuchte == 5){
           int wetax[] = {76,14,74,42,0,18,74,0,0,0, };
           double wetpx[] = {22.5,22.5,22.5,22.5,0,5,5,0,0,0, };
                  weta=wetax; wetp=wetpx;

       }
       if(naehrstoffziffer == 3 && feuchte == 46){
           int wetax[] = {14,74,0,0,0,47,42,0,0,0, };
           double wetpx[] = {45,45,0,0,0,5,5,0,0,0, };
                  weta=wetax; wetp=wetpx;

       }
       if(naehrstoffziffer == 3 && feuchte == 2){
           int wetax[] = {76,92,70,0,0,18,74,14,0,0, };
           double wetpx[] = {30,30,30,0,0,3.3,3.3,3.4,0,0, };
                  weta=wetax; wetp=wetpx;

       }
       if(naehrstoffziffer == 3 && feuchte == 1){
           int wetax[] = {62,76,18,0,0,72,79,92,0,0, };
           double wetpx[] = {30,30,30,0,0,3.3,3.3,3.4,0,0, };
                  weta=wetax; wetp=wetpx;

       }
       if(naehrstoffziffer == 3 && feuchte == 3){
           int wetax[] = {67,76,74,70,0,47,71,79,92,0, };
           double wetpx[] = {22.5,22.5,22.5,22.5,0,2.5,2.5,2.5,2.5,0, };
                  weta=wetax; wetp=wetpx;

       }
       if(naehrstoffziffer == 3 && feuchte == 4){
           int wetax[] = {74,76,70,0,0,79,47,0,0,0, };
           double wetpx[] = {30,30,30,0,0,5,5,0,0,0 };
                  weta=wetax; wetp=wetpx;

       }
       if(naehrstoffziffer == 3 && feuchte == 9){
           int wetax[] = {70,71,74,0,0,0,0,0,0,0, };
           double wetpx[] = {33.3,33.3,33.4,0,0,0,0,0,0,0, };
           weta=wetax; wetp=wetpx;

       }
// WET bereits in Liste vorhanden
       for (int i=0;i<10;i++) if (weta[i]==wetreal) wet=wetreal;
// WET zufällig zuordnen
       if (wet < 0){
          double sum =0.0;
          double p= 100*Math.random();
          for (int i=0;i<10;i++){
              sum=sum+wetp[i];
              wet=weta[i];
              if (sum > p) break;
          }
       }



       return wet;
   }

}

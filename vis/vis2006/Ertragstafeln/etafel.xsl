<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
<html>
<body>
<h2>Ertragstafel</h2>

<h3><xsl:value-of select="Ertragstafel/Tafelname"/> (<xsl:value-of select="Ertragstafel/LatinName"/>) </h3>
<P><I><xsl:value-of select="Ertragstafel/Autor"/> (<xsl:value-of select="Ertragstafel/Jahr"/>): 
      <xsl:value-of select="Ertragstafel/Quelle"/></I></P>
<P>Region: <xsl:value-of select="Ertragstafel/Region"/></P>
<P>Anmerkungen: <xsl:value-of select="Ertragstafel/Remarks"/></P>
       <table border="1">
          <tr bgcolor="yellow">
	      <td colspan= "8">verbleibend</td>
	      <td colspan= "4">ausscheidend</td>
	      <td colspan= "2">Zuwachs</td>
	      <td colspan= "2"></td>
	   </tr>
          <tr bgcolor="yellow">
	      <td>EKL</td>
	      <td>Alter</td>
	      <td>N</td>
	      <td>Hg</td>
	      <td>H100</td>
	      <td>G</td>
	      <td>Dg</td>
	      <td>V</td>
	      <td>N</td>
	      <td>Hg</td>
	      <td>G</td>
	      <td>V</td>
              <td>iG</td>
	      <td>iV</td>
	      <td>GWL</td>
	      <td>dGZ</td>
	   </tr>
          <tr bgcolor="yellow">
	      <td></td>
	      <td></td>
	      <td>st/ha</td>
	      <td>m</td>
	      <td>m</td>
	      <td>m²/ha</td>
	      <td>cm</td>
	      <td>m³/ha</td>
	      <td>st/ha</td>
	      <td>m</td>
	      <td>m²/ha</td>
	      <td>m³/ha</td>
              <td>m²/ha</td>
	      <td>m³/ha</td>
	      <td>m³/ha</td>
	      <td>m³/ha</td>
	   </tr>
	   <xsl:for-each select="Ertragstafel/Zeile">
	       <tr>							
		 <td><xsl:value-of select="Ertragsklasse"/></td>
		 <td><xsl:value-of select="Alter"/></td>
		 <td><xsl:value-of select="N"/></td>
		 <td><xsl:value-of select="Hg"/></td>
		 <td><xsl:value-of select="H100"/></td>
		 <td><xsl:value-of select="G"/></td>
		 <td><xsl:value-of select="Dg"/></td>
		 <td><xsl:value-of select="V"/></td>
		 <td><xsl:value-of select="N_ausscheidend"/></td>
		 <td><xsl:value-of select="Hg_ausscheidend"/></td>
		 <td><xsl:value-of select="G_ausscheidend"/></td>
		 <td><xsl:value-of select="V_ausscheidend"/></td>
		 <td><xsl:value-of select="iG"/></td>
		 <td><xsl:value-of select="iV"/></td>
		 <td><xsl:value-of select="GWL"/></td>
		 <td><xsl:value-of select="format-number(GWL div Alter,'##.0')"/></td>
		</tr>
	    </xsl:for-each>

	 </table>


</body>
  	</html>
</xsl:template>

</xsl:stylesheet>	
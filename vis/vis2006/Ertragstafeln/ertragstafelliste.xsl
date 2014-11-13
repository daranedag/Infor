<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
<html>
<body>
<h2>Ertragstafeln</h2>

       <table border="1">
          <tr bgcolor="yellow">
	      <td>Tafel</td>
	      <td>Datei</td>
	   </tr>
	   <xsl:for-each select="Ertragstafelliste/Ertragstafel">
	       <tr>							
		 <td><xsl:value-of select="Tafelname"/></td>
		 <td><xsl:value-of select="Datei"/></td>
		</tr>
	    </xsl:for-each>

	 </table>


</body>
  	</html>
</xsl:template>

</xsl:stylesheet>	
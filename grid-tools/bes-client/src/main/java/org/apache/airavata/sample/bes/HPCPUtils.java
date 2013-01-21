package org.apache.airavata.sample.bes;


import javax.xml.namespace.QName;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlCursor.TokenType;
import org.apache.xmlbeans.XmlObject;
import org.ggf.schemas.jsdl.x2005.x11.jsdl.DataStagingType;
import org.ggf.schemas.jsdl.x2005.x11.jsdl.SourceTargetType;

import de.fzj.unicore.wsrflite.xmlbeans.WSUtilities;

import eu.unicore.security.util.Log;

/**
 * Helper for dealing with the HPC file staging profile (see GFD.135)
 * (activity credentials)
 * 
 * @author schuller
 */
public class HPCPUtils {
	
	private HPCPUtils(){}
	
	public final static QName AC_QNAME=new QName("http://schemas.ogf.org/hpcp/2007/11/ac","Credential");
	public final static QName AC_USERNAME=new QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd","Username");
	public final static QName AC_PASSWD=new QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd","Password");
	public final static QName AC_QNAME_2=new QName("http://schemas.ogf.org/hpcp/2007/01/fs","Credential");
	/** 
	 * Extracts username and password from the security credentials as defined in the HPC file staging profile (GFD.135)
	 * <br/><br/>
	   &lt;Credential xmlns="http://schemas.ogf.org/hpcp/2007/01/ac"&gt;<br/>
         &lt;UsernameToken xmlns="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd">&gt;<br/>
          &lt;Username>sc07demo &lt;/Username&gt;<br/>
          &lt;Password>hpcpsc07 &lt;/Password&gt;<br/>
           &lt;/UsernameToken&gt;<br/>
       &lt;/Credential&gt;<br/>
     * <br/>
	 * The first HPCP Activity Credential found in the given source document is used
	 * 
	 * @param source -  an XML document 
	 * @return {@link UsernamePassword} containing the username and password found in the XML
	 */
	public static UsernamePassword extractUsernamePassword(XmlObject source){
		String user=null;
		String passwd=null;
		try{
			XmlObject copy=source.copy();
			XmlObject credential=XmlBeansUtils.extractFirstAnyElement(copy, AC_QNAME);
			if(credential==null){
				//try namespace defined by GFD.135 (but not used in the example)
				credential=XmlBeansUtils.extractFirstAnyElement(copy, AC_QNAME_2);
			}
			if(credential!=null){
				user=getUserName(credential);
				passwd=getPassword(credential);
			}
		}catch(Exception ex){
			Log.logException("Problem extracting data staging credentials.", ex);
		}
		return new UsernamePassword(user, passwd);
	}
	
	/**
	 * extract the username token
	 * @param credentials
	 * @return username or null if not found
	 */
	private static String getUserName(XmlObject credentials){
		XmlCursor c=credentials.newCursor();
		try{
			int t;
				while(true){
					t=c.toNextToken().intValue();
					if(t==TokenType.INT_ENDDOC)return null;
					if(t==TokenType.INT_START){
						if(AC_USERNAME.equals(c.getName())){
							while(c.toNextToken().intValue()!=TokenType.INT_TEXT);
							String res=c.getChars().trim();
							c.dispose();
							return res;
						}
					}
				}
		}catch(Exception e){ }
		finally{
			if(c!=null)c.dispose();
		}
		return null;
	}

	/**
	 * extract the password token
	 * @param credentials
	 * @return password or null if not found
	 */
	private static String getPassword(XmlObject credentials){
		XmlCursor c=credentials.newCursor();
		try{
			int t;
				while(true){
					t=c.toNextToken().intValue();
					if(t==TokenType.INT_ENDDOC)return null;
					if(t==TokenType.INT_START){
						if(AC_PASSWD.equals(c.getName())){
							while(c.toNextToken().intValue()!=TokenType.INT_TEXT);
							String res=c.getChars().trim();
							c.dispose();
							return res;
						}
					}
				}
		}catch(Exception e){ }
		finally{
			if(c!=null)c.dispose();
		}
		return null;
	}
	
	
	public static XmlObject createCredentialsElement(String userName, String password){
		XmlObject newXml = XmlObject.Factory.newInstance();
		
		XmlCursor cursor = newXml.newCursor();
		cursor.toNextToken();
		cursor.beginElement(AC_QNAME);
		cursor.insertElementWithText(AC_USERNAME, userName);
		cursor.insertElementWithText(AC_PASSWD, password);
		cursor.dispose();
		
		return newXml;
	}
	
	
	public static void appendDataStagingWithCredentials(DataStagingType dsType, String userName, String password){
		WSUtilities.append(createCredentialsElement(userName, password), dsType);
	}
	
}

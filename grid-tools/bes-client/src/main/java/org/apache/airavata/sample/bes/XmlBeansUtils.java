package org.apache.airavata.sample.bes;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlCursor.TokenType;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlBeansUtils {

	/**
	 * appends a XmlObject into another, immediately before the end tag<br>
	 * @param what
	 * @param toWhere
	 */
	public static void append(XmlObject what, XmlObject toWhere){
		XmlCursor sourceCurs=what.newCursor();
		sourceCurs.toNextToken();
		XmlCursor targetCurs = toWhere.newCursor();
		targetCurs.toEndDoc();
		targetCurs.toPrevToken();
		sourceCurs.copyXml(targetCurs);
		sourceCurs.dispose();
		targetCurs.dispose();
	}

	/**
	 * returns the QName for the "root element" of the Argument
	 * @param o
	 * @return
	 */
	public static QName getQNameFor(XmlObject o){
		XmlCursor c=o.newCursor();
		try{
			c.toFirstChild();
			return c.getName();
		}finally{
			c.dispose();
		}
	}
	/**
	 * extract xsd:any elements using DOM
	 * 
	 * @param source - the xml fragment
	 * @param q - QName of elements to extract. If null, all children will be returned
	 * @return XmlObject[]
	 * @throws XmlException
	 */
	public static XmlObject[] extractAnyElements(XmlObject source, QName q)throws XmlException{
		NodeList nodes=source.getDomNode().getChildNodes();
		List<XmlObject>results=new ArrayList<XmlObject>();
		for(int i=0;i<nodes.getLength();i++){
			Node n=nodes.item(i);
			if(q!=null){
				if(q.getNamespaceURI().equals(n.getNamespaceURI()) 
						&& q.getLocalPart().equals(n.getLocalName())){
					XmlObject o=XmlObject.Factory.parse(n);
					results.add(o);
				}
			}else{
				XmlObject o=XmlObject.Factory.parse(n);
				results.add(o);
			}
		}
		return results.toArray(new XmlObject[results.size()]);
	}

	public static XmlObject extractFirstAnyElement(XmlObject source, QName q)throws XmlException,IOException{
		XmlCursor c=source.newCursor();
		if(skipToElement(c, q)){
			XmlObject res=XmlObject.Factory.parse(c.newInputStream());
			c.dispose();
			return res;
		}else{
			return null;
		}
	}

	public static XmlObject extractFirstAnyElement(XmlObject source, QName[] q)throws XmlException,IOException{
		XmlCursor c=source.newCursor();
		if(skipToElement(c, q)){
			XmlObject res=XmlObject.Factory.parse(c.newInputStream());
			c.dispose();
			return res;
		}else{
			return null;
		}
	}

	public static Map<String,String>extractAttributes(XmlObject source){
		Map<String,String>result=new HashMap<String,String>();
		XmlCursor curs=source.newCursor();
		while(curs.hasNextToken()){
			TokenType type=curs.toNextToken();
			if(TokenType.END.equals(type))break;
			if(TokenType.ATTR.equals(type)){
				QName q=curs.getName();
				String val=curs.getTextValue();
				result.put(q.getLocalPart(),val);
			}
		}
		return result;
	}

	/**
	 * extract the text content of an XML element 
	 * 
	 * @param source the xml element
	 * 
	 * @return the text content, or "" if element has no content
	 */
	public static String extractElementTextAsString(XmlObject source){
		XmlCursor c=null;
		try{
			c=source.newCursor();
			while(c.hasNextToken()){
				if(c.toNextToken().equals(TokenType.TEXT)){
					return c.getChars();	
				}
			}
			return "";
		}finally{
			try{
				c.dispose();
			}catch(Exception e){}
		}
	}

	/**
	 * fast-forward the cursor up to the element with the given QName
	 *  
	 * @param cursor
	 * @param name
	 * @return false if element does not exist
	 */
	public static boolean skipToElement(XmlCursor cursor, QName name){
		// walk through element tree in prefix order (root first, then children from left to right)
		if(name.equals(cursor.getName())) return true;
		boolean hasMoreChildren=true;
		int i=0;
		while(hasMoreChildren){
			hasMoreChildren = cursor.toChild(i++);
			if(hasMoreChildren){
				boolean foundInChild = skipToElement(cursor, name);
				if(foundInChild) return true;
				cursor.toParent();
			}
		}
		return false;
	}

	/**
	 * fast-forward the cursor up to the element with one of the given QNames
	 *  
	 * @param cursor
	 * @param names - list of acceptable qnames
	 * @return false if element does not exist
	 */
	public static boolean skipToElement(XmlCursor cursor, QName[] names){
		// walk through element tree in prefix order (root first, then children from left to right)
		for(QName name: names){	
			if(name.equals(cursor.getName())) return true;
		}
		boolean hasMoreChildren=true;
		int i=0;
		while(hasMoreChildren){
			hasMoreChildren = cursor.toChild(i++);
			if(hasMoreChildren){
				boolean foundInChild = skipToElement(cursor, names);
				if(foundInChild) return true;
				cursor.toParent();
			}
		}
		return false;
	}

	/**
	 * extract the text of a certain element from an xml document
	 * @param source
	 * @param names
	 * @return text or null if undefined
	 */
	public static String getElementText(XmlObject source, QName[] names){
		try{
			XmlObject o=extractFirstAnyElement(source, names);
			return extractElementTextAsString(o);	
		}
		catch(Exception ex){
			return null;
		}
	}


	/**
	 * extract the text of the specified element
	 * @param source - the source XML document
	 * @param name - the qname of the desired element
	 * @return
	 */
	public static String getElementText(XmlObject source, QName name){
		try{
			XmlObject o=extractFirstAnyElement(source, name);
			if(o==null)return null;
			return extractElementTextAsString(o);	
		}
		catch(Exception ex){
			return null;
		}
	}

	public static String getAttributeText(XmlObject source, QName elementName, QName attributeName){
		try{
			XmlObject o=extractFirstAnyElement(source, elementName);
			if(o==null)return null;
			return getAttributeText(o, attributeName);
		}
		catch(Exception ex){
		}
		return null;
	}

	public static String getAttributeText(XmlObject source, QName attributeName){
		XmlCursor c=source.newCursor();
		try{
			TokenType t=null;
			do{
				t=c.toNextToken();
				if(t.isAttr()){
					if(attributeName.equals(c.getName())){
						return c.getTextValue();
					}
				}
			}while(!TokenType.END.equals(t));
			return null;
		}finally{
			c.dispose();
		}
	}

	/**
	 * extract an array of XML elements identified by their qname from an XML source 
	 * document. 
	 * 
	 * @param source - the xml fragment
	 * @param q - QName of elements to extract
	 * @param asClass - the XMLbeans class of the elements to extract
	 * @return List<T> - a list of XMLBeans objects with the correct runtime type
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T>extractAnyElements(XmlObject source, QName q, Class<? extends XmlObject>asClass){
		List<T>results=new ArrayList<T>();
		XmlCursor cursor=null;
		try{
			if(source != null){
				cursor = source.newCursor();
				skipToElement(cursor, q);
				String ns=q.getNamespaceURI();
				boolean hasMore =true;
				while(hasMore){
					XmlObject next = XmlObject.Factory.parse(cursor.newXMLStreamReader());
					QName name = cursor.getName();
					if( (name!=null &&  (ns.equals(name.getNamespaceURI()) || "*".equals(ns))) 
							&& q.getLocalPart().equals(name.getLocalPart())){
						results.add((T)next);
					}
					hasMore = cursor.toNextSibling();
				}
			}
		}
		catch(XmlException xe){
			//what?
		}
		finally{
			if(cursor!=null)cursor.dispose();
		}
		return results;
	}
}


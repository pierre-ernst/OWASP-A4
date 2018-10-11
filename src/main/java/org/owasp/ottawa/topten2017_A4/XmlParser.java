package org.owasp.ottawa.topten2017_A4;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

/**
 * Abstract XmlParser class
 * 
 * @author Pierre Ernst
 * @date 2018-10-03
 */
public abstract class XmlParser {

	public static final Charset DEFAULT_ENCODING = Charset.forName("UTF-8");

	/**
	 * Implementation-specific parsing of XML input
	 * 
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public abstract String parse(InputStream in) throws Exception;

	public String parse(String xmlInput) throws Exception {
		return parse(xmlInput.getBytes(DEFAULT_ENCODING));
	}

	public String parse(File file) throws Exception {
		return parse(new FileInputStream(file));
	}

	public String parse(byte[] buffer) throws Exception {
		return parse(new ByteArrayInputStream(buffer));
	}

	protected static String toString(Document doc) throws TransformerException {
		StringWriter sw = new StringWriter();
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");

		transformer.transform(new DOMSource(doc), new StreamResult(sw));
		return sw.toString();
	}
}

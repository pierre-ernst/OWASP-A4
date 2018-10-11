package org.owasp.ottawa.topten2017_A4.impl;

import java.io.InputStream;

import javax.xml.parsers.SAXParserFactory;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.DOMWriter;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 
 * @author Pierre Ernst
 * @date 2018-10-11
 */
public class JaxpSaxParser extends JaxpParser {

	protected class JaxpSaxHandler extends DefaultHandler {

		private org.w3c.dom.Document w3cDoc;
		private org.dom4j.Document dom4jDoc;
		private Element current;

		@Override
		public void startDocument() throws SAXException {
			dom4jDoc = DocumentHelper.createDocument();
			current = null;
			w3cDoc = null;
		}

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes)
				throws SAXException {
			if (current == null) {
				current = dom4jDoc.addElement(qName, uri);
			} else {
				current = current.addElement(qName, uri);
			}
			for (int i = 0; i < attributes.getLength(); i++) {
				current.addAttribute(attributes.getQName(i), attributes.getValue(i));
			}
		}

		@Override
		public void characters(char[] value, int start, int length) throws SAXException {
			current.addText(new String(value, start, length));
		}

		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			current = current.getParent();
		}

		@Override
		public void endDocument() throws SAXException {
			try {
				w3cDoc = new DOMWriter().write(dom4jDoc);
			} catch (DocumentException ex) {
				throw new SAXException(ex);
			}
		}
	}

	private SAXParserFactory spf;

	public JaxpSaxParser() {
		spf = SAXParserFactory.newInstance();
	}

	protected JaxpSaxHandler getHandler() {
		return new JaxpSaxHandler();
	}

	@Override
	public void mitigate() throws Exception {
		spf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
	}

	@Override
	public String parse(InputStream in) throws Exception {
		JaxpSaxHandler handler = getHandler();
		spf.newSAXParser().parse(in, handler);
		return toString(handler.w3cDoc);
	}

}

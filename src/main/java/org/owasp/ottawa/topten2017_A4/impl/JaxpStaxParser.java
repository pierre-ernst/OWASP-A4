package org.owasp.ottawa.topten2017_A4.impl;

import java.io.InputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.DOMWriter;

/**
 * 
 * @author Pierre Ernst
 * @date 2018-10-11
 */
public class JaxpStaxParser extends JaxpParser {

	protected XMLInputFactory xmlInputFactory;

	public JaxpStaxParser() {
		xmlInputFactory = new com.sun.xml.internal.stream.XMLInputFactoryImpl();
	}

	@Override
	public void mitigate() throws Exception {
		xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
	}

	@Override
	public String parse(InputStream in) throws Exception {
		XMLStreamReader xsr = xmlInputFactory.createXMLStreamReader(in);

		org.w3c.dom.Document w3cDoc = null;
		org.dom4j.Document dom4jDoc = null;
		Element current = null;

		while (xsr.hasNext()) {
			int eventType = xsr.next();

			switch (eventType) {

			case XMLStreamReader.START_DOCUMENT: {
				dom4jDoc = DocumentHelper.createDocument();
				current = null;
				w3cDoc = null;
				break;
			}

			case XMLStreamReader.START_ELEMENT: {
				// START_DOCUMENT not always parsed for some reasons...
				if (dom4jDoc == null) {
					dom4jDoc = DocumentHelper.createDocument();
				}

				if (current == null) {
					current = dom4jDoc.addElement(xsr.getName().getLocalPart(), xsr.getName().getNamespaceURI());
				} else {
					current = current.addElement(xsr.getName().getLocalPart(), xsr.getName().getNamespaceURI());
				}
				for (int i = 0; i < xsr.getAttributeCount(); i++) {
					current.addAttribute(xsr.getAttributeLocalName(i), xsr.getAttributeValue(i));
				}
				break;
			}

			case XMLStreamReader.CHARACTERS: {
				current.addText(xsr.getText());
				break;
			}

			case XMLStreamReader.END_ELEMENT: {
				current = current.getParent();
				break;
			}

			case XMLStreamReader.END_DOCUMENT: {
				w3cDoc = new DOMWriter().write(dom4jDoc);
				break;
			}
			}

		}
		return toString(w3cDoc);
	}

}

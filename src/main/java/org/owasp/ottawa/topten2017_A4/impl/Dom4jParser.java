package org.owasp.ottawa.topten2017_A4.impl;

import java.io.InputStream;

import org.dom4j.io.DOMWriter;
import org.dom4j.io.SAXReader;
import org.owasp.ottawa.topten2017_A4.XmlParser;

/**
 * Dom4J is immune to XXE even when used with the default config. External
 * entities are simply ignored.
 * 
 * @author Pierre Ernst
 * @date 2018-10-10
 */
public class Dom4jParser extends XmlParser {

	@Override
	public String parse(InputStream in) throws Exception {
		org.dom4j.Document dom4jDoc = new SAXReader().read(in);
		org.w3c.dom.Document w3cDoc = new DOMWriter().write(dom4jDoc);

		return toString(w3cDoc);
	}
}

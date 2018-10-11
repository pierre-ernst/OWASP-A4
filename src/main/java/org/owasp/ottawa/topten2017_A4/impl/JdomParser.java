package org.owasp.ottawa.topten2017_A4.impl;

import java.io.InputStream;

import org.jdom2.input.SAXBuilder;
import org.jdom2.output.DOMOutputter;
import org.owasp.ottawa.topten2017_A4.VulnerableXmlParser;

/**
 * 
 * @author Pierre Ernst
 * @date 2018-10-11
 */
public class JdomParser extends VulnerableXmlParser {

	private SAXBuilder builder;

	public JdomParser() {
		builder = new SAXBuilder();
	}

	@Override
	public void mitigate() throws Exception {
		builder.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
	}

	@Override
	public String parse(InputStream in) throws Exception {
		org.jdom2.Document jdomDoc = builder.build(in);
		org.w3c.dom.Document w3cDoc = new DOMOutputter().output(jdomDoc);
		return toString(w3cDoc);
	}

}

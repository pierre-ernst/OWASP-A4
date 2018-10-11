package org.owasp.ottawa.topten2017_A4.impl;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

/**
 * 
 * @author Pierre Ernst
 * @date 2018-10-11
 */
public class JaxpDomParser extends JaxpParser {

	private DocumentBuilderFactory documentBuilderFactory;

	public JaxpDomParser() {
		documentBuilderFactory = DocumentBuilderFactory.newInstance();
	}

	@Override
	public void mitigate() throws Exception {
		documentBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
	}

	@Override
	public String parse(InputStream in) throws Exception {
		Document doc = documentBuilderFactory.newDocumentBuilder().parse(in);
		return toString(doc);
	}

	/**
	 * Exposed for unit tests with partial mitigation
	 * 
	 * @return
	 */
	public DocumentBuilderFactory getDocumentBuilderFactory() {
		return documentBuilderFactory;
	}

}

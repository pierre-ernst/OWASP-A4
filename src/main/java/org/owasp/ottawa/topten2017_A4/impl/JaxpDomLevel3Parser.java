package org.owasp.ottawa.topten2017_A4.impl;

import java.io.InputStream;

import org.w3c.dom.Document;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSParser;

/**
 * 
 * @author Pierre Ernst
 * @date 2018-10-11
 */
public class JaxpDomLevel3Parser extends JaxpParser {

	private LSParser parser;
	private LSInput input;

	public JaxpDomLevel3Parser()
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, ClassCastException {
		DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();

		DOMImplementationLS impl = (DOMImplementationLS) registry.getDOMImplementation("LS");

		parser = impl.createLSParser(DOMImplementationLS.MODE_SYNCHRONOUS, null);

		input = impl.createLSInput();
	}

	@Override
	public void mitigate() throws Exception {
		parser.getDomConfig().setParameter("http://apache.org/xml/features/disallow-doctype-decl", true);
	}

	@Override
	public String parse(InputStream in) throws Exception {
		input.setByteStream(in);
		Document doc = parser.parse(input);
		return toString(doc);
	}

}

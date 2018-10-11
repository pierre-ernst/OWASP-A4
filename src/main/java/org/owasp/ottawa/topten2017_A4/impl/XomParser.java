package org.owasp.ottawa.topten2017_A4.impl;

import java.io.InputStream;
import java.lang.reflect.Field;

import org.owasp.ottawa.topten2017_A4.VulnerableXmlParser;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.xml.sax.XMLReader;

import nu.xom.Builder;
import nu.xom.converters.DOMConverter;

/**
 * 
 * @author Pierre Ernst
 * @date 2018-10-11
 */
public class XomParser extends VulnerableXmlParser {

	private Builder builder;

	public XomParser() {
		builder = new Builder();
	}

	@Override
	public void mitigate() throws Exception {
		// get the underlying XMLReader member
		Field f = Builder.class.getDeclaredField("parser");
		f.setAccessible(true);
		XMLReader xr = (XMLReader) f.get(builder);

		xr.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
	}

	@Override
	public String parse(InputStream in) throws Exception {
		nu.xom.Document xomDoc = builder.build(in);
		org.w3c.dom.Document w3cDoc = DOMConverter.convert(xomDoc,
				DOMImplementationRegistry.newInstance().getDOMImplementation("XML 1.0"));

		return toString(w3cDoc);
	}

}

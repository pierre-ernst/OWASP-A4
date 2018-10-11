package org.owasp.ottawa.topten2017_A4.impl;

import java.io.IOException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.DefaultHandler2;
import org.xml.sax.ext.EntityResolver2;
import org.xml.sax.ext.LexicalHandler;

/**
 * 
 * @author Pierre Ernst
 * @date 2018-10-11
 */
public class JaxpSax2Parser extends JaxpSaxParser {

	private class JaxpSax2Handler extends JaxpSaxHandler implements LexicalHandler, DeclHandler, EntityResolver2 {

		private DefaultHandler2 delegate;

		private JaxpSax2Handler() {
			delegate = new DefaultHandler2();
		}

		@Override
		public InputSource getExternalSubset(String name, String baseURI) throws SAXException, IOException {
			return delegate.getExternalSubset(name, baseURI);
		}

		@Override
		public InputSource resolveEntity(String name, String publicId, String baseURI, String systemId)
				throws SAXException, IOException {
			return delegate.resolveEntity(name, publicId, baseURI, systemId);
		}

		@Override
		public void attributeDecl(String eName, String aName, String type, String mode, String value)
				throws SAXException {
			delegate.attributeDecl(eName, aName, type, mode, value);
		}

		@Override
		public void elementDecl(String name, String model) throws SAXException {
			delegate.elementDecl(name, model);
		}

		@Override
		public void externalEntityDecl(String name, String publicId, String systemId) throws SAXException {
			delegate.externalEntityDecl(name, publicId, systemId);
		}

		@Override
		public void internalEntityDecl(String name, String value) throws SAXException {
			delegate.internalEntityDecl(name, value);
		}

		@Override
		public void comment(char[] ch, int start, int length) throws SAXException {
			delegate.comment(ch, start, length);
		}

		@Override
		public void endCDATA() throws SAXException {
			delegate.endCDATA();
		}

		@Override
		public void endDTD() throws SAXException {
			delegate.endDTD();
		}

		@Override
		public void endEntity(String name) throws SAXException {
			delegate.endEntity(name);
		}

		@Override
		public void startCDATA() throws SAXException {
			delegate.startCDATA();
		}

		@Override
		public void startDTD(String name, String publicId, String systemId) throws SAXException {
			delegate.startDTD(name, publicId, systemId);
		}

		@Override
		public void startEntity(String name) throws SAXException {
			delegate.startEntity(name);
		}

	}

	@Override
	protected JaxpSaxHandler getHandler() {
		return new JaxpSax2Handler();
	}
}

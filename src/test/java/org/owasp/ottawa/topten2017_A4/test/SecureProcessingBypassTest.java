package org.owasp.ottawa.topten2017_A4.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.owasp.ottawa.topten2017_A4.XmlParser;
import org.owasp.ottawa.topten2017_A4.impl.JaxpDomParser;
import org.xml.sax.SAXParseException;

public class SecureProcessingBypassTest {

	private XmlParser getSoCalledSecureParser() throws ParserConfigurationException {
		JaxpDomParser parser = new JaxpDomParser();
		parser.getDocumentBuilderFactory().setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
		return parser;
	}

	@Test
	public void xmlBomb() {

		try {
			try {
				getSoCalledSecureParser().parse(this.getClass().getResourceAsStream("/xml-bomb.xml"));
				fail("Exception should have been thrown");
			} catch (SAXParseException expected) {
				assertTrue(expected.getMessage().contains("The parser has encountered more than"));
			}
		} catch (Exception ex) {
			ex.printStackTrace(System.err);
			fail(ex.getMessage());
		}
	}

	@Test
	public void quadraticBlowUp() {
		try {

			Thread background = new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						getSoCalledSecureParser().parse(this.getClass().getResourceAsStream("/quadratic-blowup.xml"));
						fail("Thread not interruped");
					} catch (Exception ex) {
						ex.printStackTrace(System.err);
					}
				}
			});

			background.setPriority(Thread.MIN_PRIORITY);
			background.start();
			Thread.sleep(5000L);
			assertTrue("Parsing should take (waaaaay) more than 5s", background.isAlive());
			background.interrupt();

		} catch (Exception ex) {
			ex.printStackTrace(System.err);
			fail(ex.getMessage());
		}
	}
}

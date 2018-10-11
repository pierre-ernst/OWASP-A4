package org.owasp.ottawa.topten2017_A4.test;

import org.owasp.ottawa.topten2017_A4.VulnerableXmlParser;
import org.owasp.ottawa.topten2017_A4.XmlParser;
import org.owasp.ottawa.topten2017_A4.impl.JaxpDomParser;
import org.reflections.Reflections;
import org.w3c.dom.ls.LSException;
import org.xml.sax.SAXParseException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Modifier;
import java.net.InetSocketAddress;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class ParserTest {

	private class XxeHttpHandler implements HttpHandler {

		private String sharedSecret;

		private XxeHttpHandler(String sharedSecret) {
			this.sharedSecret = sharedSecret;
		}

		@Override
		public void handle(HttpExchange x) throws IOException {
			x.sendResponseHeaders(200, sharedSecret.length());
			OutputStream os = x.getResponseBody();
			os.write(sharedSecret.getBytes(XmlParser.DEFAULT_ENCODING));
			os.close();
		}
	}

	private static String generateSecret() {
		SecureRandom random = new SecureRandom();
		byte bytes[] = new byte[20];
		random.nextBytes(bytes);
		return Hex.encodeHexString(bytes);
	}

	private static Set<XmlParser> getAllParserImplementations() throws InstantiationException, IllegalAccessException {
		Set<XmlParser> implementations = new HashSet<>();
		Reflections reflections = new Reflections(XmlParser.class.getPackage().getName() + ".impl");

		for (Class<? extends XmlParser> parserClass : reflections.getSubTypesOf(XmlParser.class)) {
			if (!Modifier.isAbstract(parserClass.getModifiers())) {
				implementations.add(parserClass.newInstance());
			}
		}
		return implementations;
	}

	@Test
	public void legitimateInputTest() {
		try {
			String expected = IOUtils.toString(this.getClass().getResourceAsStream("/bicycle.xml"),
					XmlParser.DEFAULT_ENCODING);

			for (XmlParser parser : getAllParserImplementations()) {
				String parserName = parser.getClass().getSimpleName();

				String parsed = parser.parse(ParserTest.class.getResourceAsStream("/bicycle.xml"));
				assertEquals(parserName, expected, parsed);
			}

		} catch (Exception ex) {
			ex.printStackTrace(System.err);
			fail(ex.getMessage());
		}
	}

	@Test
	public void xxeInputTest() {
		try {

			String secret = generateSecret();

			String expected = IOUtils.toString(this.getClass().getResourceAsStream("/xxe.xml"),
					XmlParser.DEFAULT_ENCODING);
			// remove DOCTYPE
			Pattern regex = Pattern.compile("(<!DOCTYPE[a-zA-Z0-9\"\\s\\[<>!:/\\.]+]>[\r\n]+)", Pattern.MULTILINE);
			Matcher regexMatcher = regex.matcher(expected);
			if (!regexMatcher.find()) {
				throw new RuntimeException("regex coding error");
			}
			expected = regexMatcher.replaceFirst("");

			// replace with expected response from local HTTP listener
			expected = expected.replace("&xxe;", secret);

			HttpServer server = HttpServer.create(new InetSocketAddress(1666), 0);
			server.createContext("/xxe", new XxeHttpHandler(secret));
			server.setExecutor(null);
			server.start();

			for (XmlParser parser : getAllParserImplementations()) {
				if ( parser instanceof VulnerableXmlParser) {
				String parserName = parser.getClass().getSimpleName();

				String parsed = parser.parse(this.getClass().getResourceAsStream("/xxe.xml"));
				assertEquals(parserName, expected, parsed);
				}
			}

			server.stop(0);
			
		} catch (Exception ex) {
			ex.printStackTrace(System.err);
			fail(ex.getMessage());
		}
	}

	@Test
	public void mitigatedXxeTest() {
		try {
			String secret = generateSecret();

			HttpServer server = HttpServer.create(new InetSocketAddress(1666), 0);
			server.createContext("/xxe", new XxeHttpHandler(secret));
			server.setExecutor(null);
			server.start();

			for (XmlParser parser : getAllParserImplementations()) {
				if ( parser instanceof VulnerableXmlParser) {
				String parserName = parser.getClass().getSimpleName();

				((VulnerableXmlParser)parser).mitigate();
				try {
					String parsed = parser.parse(this.getClass().getResourceAsStream("/xxe.xml"));
					assertFalse("XXE triggered for " + parserName, parsed.contains(secret));
				} catch (Exception expected) {
					assertTrue(parserName, expected.getMessage().contains("DOCTYPE is disallowed")
							|| expected.getMessage().contains("was referenced, but not declared")
							|| expected.getMessage().contains("Undeclared general entity"));
				}
			}
			}
			
			server.stop(0);
			
		} catch (Exception ex) {
			ex.printStackTrace(System.err);
			fail(ex.getMessage());
		}
	}

}

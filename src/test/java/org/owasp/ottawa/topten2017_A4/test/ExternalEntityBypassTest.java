package org.owasp.ottawa.topten2017_A4.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.owasp.ottawa.topten2017_A4.XmlParser;
import org.owasp.ottawa.topten2017_A4.impl.JaxpDomParser;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class ExternalEntityBypassTest {

	private class SsrfHttpHandler implements HttpHandler {

		private boolean gotHit = false;
		
		@Override
		public void handle(HttpExchange x) throws IOException {
			gotHit = true;
			String response = "<!ELEMENT pierre (#PCDATA)>";
			x.sendResponseHeaders(200, response.length());
			OutputStream os = x.getResponseBody();
			os.write(response.getBytes(XmlParser.DEFAULT_ENCODING));
			os.close();
		}
	}

	@Test
	public void ssrfTest() {
		try {
			JaxpDomParser parser = new JaxpDomParser();

			/**
			 * These 2 features are not sufficient to prevent SSRF
			 * "http://apache.org/xml/features/nonvalidating/load-external-dtd" should be
			 * set to false, or just disallow doctype altogether
			 */
			parser.getDocumentBuilderFactory().setFeature("http://xml.org/sax/features/external-general-entities",
					false);
			parser.getDocumentBuilderFactory().setFeature("http://xml.org/sax/features/external-parameter-entities",
					false);
			
			SsrfHttpHandler httpHandler = new SsrfHttpHandler();
			HttpServer server = HttpServer.create(new InetSocketAddress(1666), 0);
			server.createContext("/ssrf", httpHandler);
			server.setExecutor(null);
			server.start();

			parser.parse(this.getClass().getResourceAsStream("/simple-ssrf.xml"));
			
			assertTrue(httpHandler.gotHit);

			server.stop(0);
			
		} catch (Exception ex) {
			ex.printStackTrace(System.err);
			fail(ex.getMessage());
		}
	}

}

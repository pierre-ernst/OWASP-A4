package org.owasp.ottawa.topten2017_A4.test;

import static org.junit.Assert.assertEquals;
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

public class ParameterTest {

	private class ParamHttpHandler implements HttpHandler {

		@Override
		public void handle(HttpExchange x) throws IOException {
			String response = getResponse(x.getRequestURI().toString());
			x.sendResponseHeaders(200, response.length());
			OutputStream os = x.getResponseBody();
			os.write(response.getBytes(XmlParser.DEFAULT_ENCODING));
			os.close();
		}

		private String getResponse(String uri) {
			String response = "Unknown URI";
			if (uri.endsWith("/doctype")) {
				response = "<!ENTITY name \"Recumbent %bike;\" >";
			} else if (uri.endsWith("/riders")) {
				response = "1";
			}
			if (uri.endsWith("/wheels")) {
				response = "2";
			}

			return response;
		}
	}

	@Test
	public void indirectRestCallTest() {
		try {
			String expected = IOUtils.toString(this.getClass().getResourceAsStream("/bicycle.xml"),
					XmlParser.DEFAULT_ENCODING);
			expected = expected.replace("Road", "Recumbent");

			HttpServer server = HttpServer.create(new InetSocketAddress(1666), 0);
			server.createContext("/", new ParamHttpHandler());
			server.setExecutor(null);
			server.start();

			XmlParser parser = new JaxpDomParser();
			String parsed = parser.parse(this.getClass().getResourceAsStream("/param.xml"));
			assertEquals(expected, parsed);

			server.stop(0);
		} catch (Exception ex) {
			ex.printStackTrace(System.err);
			fail(ex.getMessage());
		}
	}
}

package org.owasp.ottawa.topten2017_A4;

/**
 * XmlParser which is vulnerable to XXE
 * 
 * @author Pierre Ernst
 * @date 2018-10-11
 */
public abstract class VulnerableXmlParser extends XmlParser {

	/**
	 * Configure the parser to prevent security vulnerabilities when this parser is
	 * used to parse untrusted input
	 * 
	 * @throws Exception
	 */
	public abstract void mitigate() throws Exception;

}

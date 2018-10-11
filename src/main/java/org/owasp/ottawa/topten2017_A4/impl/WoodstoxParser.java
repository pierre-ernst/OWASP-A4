package org.owasp.ottawa.topten2017_A4.impl;

/**
 * 
 * @author Pierre Ernst
 * @date 2018-10-11
 */
public class WoodstoxParser extends JaxpStaxParser {

	public WoodstoxParser() {
		xmlInputFactory = new com.ctc.wstx.stax.WstxInputFactory();
	}
}

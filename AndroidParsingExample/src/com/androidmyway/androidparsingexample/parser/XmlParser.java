package com.androidmyway.androidparsingexample.parser;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlParser {
	private static SAXParser parser = null;
	private static XmlHandler handler = null;

	/**
	 * Returns the parsed XmlElement object
	 * 
	 * @param uri
	 *            Uri of XML document
	 * @return XmlElement parsed XmlElement object
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static XmlElement parse(String uri) throws ParserConfigurationException,
			SAXException, IOException {
		if (parser == null) {
			parser = SAXParserFactory.newInstance().newSAXParser();
		}
		if (handler == null) {
			handler = new XmlHandler();
		}
		
		InputSource source = new InputSource(uri);
		//source.setEncoding("ISO-8859-1");
		source.setEncoding("UTF-8");
		
		parser.parse(source, handler);
		return handler.getOutput();
	}
	
	public static XmlElement parse(InputStream uri) throws ParserConfigurationException,
	SAXException, IOException {
		
		if (parser == null) {
			parser = SAXParserFactory.newInstance().newSAXParser();
		}
		if (handler == null) {
			handler = new XmlHandler();
		}

		InputSource source = new InputSource(uri);
		//source.setEncoding("ISO-8859-1");
		source.setEncoding("UTF-8");

		parser.parse(source, handler);
		return handler.getOutput();
	}
}

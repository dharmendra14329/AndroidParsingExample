package com.androidmyway.androidparsingexample.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

class XmlHandler extends DefaultHandler {

	private XmlElement element;
	private StringBuffer stringVal;
	private XmlElement output = null;

	/**
	 * Returns the output of the parsing
	 * 
	 * @return
	 */
	public XmlElement getOutput() {
		return output;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		// initialize all the fields
		output = null;
		stringVal = null;
		element = null;
	}

	private XmlElement temp = null;
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		
		stringVal = new StringBuffer();
		
		if (element != null) {
			if (element.isEnded()) {
				temp = element.getParent();
			} else {
				temp = element;
			}
		}
		element = new XmlElement(qName, null, attributes, uri);
		if (output == null) {
			output 	= element;
			temp	= element;
			//print(element.getKey() + " created");
		} else {
			temp.addElement(element.getKey(), element);
			element.setParent(temp);
			//print(element.getKey() + " added to " + temp.getKey());
		}
		// Log.d(getClass().getName(), "Element " + element.getKey()
		// + " parsing started!");
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		super.endElement(uri, localName, qName);
		if(element.isEnded())
			element = element.getParent();
		element.setEnded(true);
		element.setValue(stringVal.toString());
		stringVal = new StringBuffer();
		//print(element.getKey() + " ended");

		// Log.d(getClass().getName(), "Element " + element.getKey()
		// + " parsing finished!");
	}

	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		super.characters(ch, start, length);
		stringVal.append(ch, start, length);
	}

	@Override
	public void error(SAXParseException e) throws SAXException {
		super.error(e);
	}
	
	@SuppressWarnings("unused")
	private void print(String text) {
		//System.out.println(text);
		//Log.d("test", text);
	}

}

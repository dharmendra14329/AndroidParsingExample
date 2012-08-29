package com.androidmyway.androidparsingexample.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;

public class XmlElement {

	private String key, value, uri;
	private Map<String, String> attributes;
	//private Map<String, XmlElement> elements;
	private ArrayList<XmlElement> elements;
	private XmlElement parent = null;
	private boolean ended = false;

	public XmlElement(String key, String value, Attributes attrs, String uri) {
		this.key = key;
		this.value = value;
		this.uri = uri;
		attributes = new HashMap<String, String>();
		//elements = new HashMap<String, XmlElement>();
		elements = new ArrayList<XmlElement>();
		if (attrs != null) {
			for (int i = 0; i < attrs.getLength(); i++) {
				attributes.put(attrs.getLocalName(i), attrs.getValue(i));
			}
		}
	}

	public XmlElement(String key, String value) {
		this(key, value, null, null);
	}

	public void setParent(XmlElement parent) {
		this.parent = parent;
	}

	public XmlElement getParent() {
		return parent;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getKey() {
		return key == null ? "" : key;
	}

	public String getValue() {
		if (value == null 
				|| getElements() != null && getElements().size() > 0) {
				StringBuffer buff = new StringBuffer();
//				for (Entry<String, XmlElement>  entry : getElements().entrySet()) {
//					XmlElement e = entry.getValue();
//					eStr = eStr + e.toString();
//				}
				for (XmlElement e : getElements()) {
					buff.append(e.toString());
				}
				return buff.toString();
		}
		return value == null ? "" : value;
	}

	public String getUri() {
		return uri == null ? "" : uri;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void addAttribute(String key, String value) {
		attributes.put(key, value);
	}

	public ArrayList<XmlElement> getElements() {
		return elements;
	}

	public void addElement(String key, XmlElement element) {
		//elements.put(key, element);
		elements.add(element);
	}

	public void setEnded(boolean ended) {
		this.ended = ended;
	}

	public boolean isEnded() {
		return ended;
	}

	public String toString() {
		return "\n<" + getKey() + " "
				+ getAttributes().toString().replace("{", "").replace("}", "")
				+ getUri() + ">" + getValue() + "</" + getKey() + ">";
	}
}

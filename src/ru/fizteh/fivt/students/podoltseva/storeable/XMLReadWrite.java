package ru.fizteh.fivt.students.podoltseva.storeable;

import java.io.StringReader;
import java.io.StringWriter;
import java.text.ParseException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public class XMLReadWrite {

	public static void read(Table table, Storeable dest, String value) throws ParseException {
		if (value == null) {
			throw new ParseException("XMLReadWrite error: Null value in read", 0);
		}
		XMLStreamReader xReader = null;
		try {
			xReader = XMLInputFactory.newInstance().createXMLStreamReader(new StringReader(value));
			if (!xReader.hasNext()) {
				throw new ParseException("XMLReadWrite error: empty value in read", 0);
			}
			if (xReader.next() != XMLStreamConstants.START_ELEMENT 
					|| !xReader.getName().getLocalPart().equals("row")) {
				throw new ParseException("XMLReadWrite error: tag <row> is expected " 
					+ "in the begin of value in read", 0);
			}
			String currentValue;
			for (int i = 0; i < table.getColumnsCount(); ++i) {
				try {
					currentValue = readElement(dest, xReader);
					dest.setColumnAt(i, Magic.convertStringToObject(currentValue, table.getColumnType(i)));
				} catch (XMLStreamException e) {
					throw new ParseException("XMLReadWrite error: some problems with XML in read", 0);
				}
			}
			if (xReader.next() != XMLStreamConstants.END_ELEMENT) {
				throw new ParseException("XMLReadWrite error: tag </row> is expected in read", 0);
			}
		} catch (XMLStreamException e) {
			throw new ParseException("XMLReadWrite error: some problems with XML in read", 0);
		} finally {
			try {
				if (xReader != null) {
					xReader.close();
				}
			} catch (XMLStreamException e) {
				throw new ParseException("XMLReadWrite error: some problems with XML in read", 0);
			}
		}
	}
	
	private static String readElement(Storeable dest, XMLStreamReader xReader) 
			throws XMLStreamException, ParseException {
		if (!xReader.hasNext()) {
			throw new ParseException("XMLReadWrite error: unexpexted end of value in read", 0);
		}
		int node = xReader.next();
		if (node == XMLStreamConstants.END_ELEMENT) {
			throw new ParseException("XMLReadWrite error: not enough columns in read", 0);
		}
		if (node != XMLStreamConstants.START_ELEMENT) {
			if (!xReader.getText().equals("null")) {
				return new String("null");
			} else {
				throw new ParseException("XMLReadWrite error: tag <col> or <null/> is expected in read " + xReader.getName().getLocalPart(), 0); 
			}
		}
		if (xReader.getName().getLocalPart().equals("null")) {
			return new String("null");
		}
		if (!xReader.getName().getLocalPart().equals("col")) {
			throw new ParseException("XMLReadWrite error: tag <col> or <null/> is expected in read " + xReader.getName().getLocalPart(), 0); 
		}
		if (!xReader.hasNext()) {
			throw new ParseException("XMLReadWrite error: unexpexted end of value in read", 0);
		}
		if (xReader.next() != XMLStreamConstants.CHARACTERS) {
			throw new ParseException("XMLReadWrite error: some value is expected in read", 0);
		}
		String returnString = xReader.getText();
		if (xReader.next() != XMLStreamConstants.END_ELEMENT) {
			throw new ParseException("XMLReadWrite error: tag </col> is expected in read", 0);
		}
		return returnString;
	}

	public static String write(Object[] values) {
		try {
			StringWriter stringWriter = new StringWriter();
			XMLStreamWriter xWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(stringWriter);
			xWriter.writeStartElement("row");
			for (int i = 0; i < values.length; ++i) {
				if (values[i] == null) {
					xWriter.writeEmptyElement("null");
				} else {
					xWriter.writeStartElement("col");
					xWriter.writeCharacters(values[i].toString());
					xWriter.writeEndElement();
				}
			}
			xWriter.writeEndElement();
			xWriter.flush();
			xWriter.close();
			return stringWriter.toString();
		} catch (XMLStreamException e) {
			throw new ColumnFormatException("XMLReadWrite error: problems in write");
		}
	}
}

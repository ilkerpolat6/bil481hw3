
package com.mycompany.app;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.mycompany.app.Person;
public class MyHandler extends DefaultHandler {

	// List to hold Employees object
	private List <Person> empList = null;
	private Person emp = null;
	private StringBuilder data = null;

	// getter method for employee list
	public List <Person> getEmpList() {
		return empList;
	}

	boolean bFirstName = false;
	boolean bLastName = false;
	boolean bId = false;

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equalsIgnoreCase("ENTITY")) {
			// create a new Employee and put it in Map
			String id = attributes.getValue("Id");
			// initialize Employee object and set id attribute
			emp = new Person();
			emp.setId(Integer.parseInt(id));
			// initialize list
			if (empList == null)
				empList = new ArrayList<>();
		} else if (qName.equalsIgnoreCase("FIRSTNAME")) {
			// set boolean values for fields, will be used in setting Employee variables
			bFirstName = true;
		} else if (qName.equalsIgnoreCase("LASTNAME")) {
			bLastName = true;
		} 
		// create the data container
		data = new StringBuilder();
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (bFirstName) {
			emp.setFirstname(data.toString());
			bFirstName = false;
		} else if (bLastName) {
			emp.setLastName(data.toString());
			bLastName = false;
		} 
		if (qName.equalsIgnoreCase("ENTITY")) {
			empList.add(emp);
			}
	}

	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
		data.append(new String(ch, start, length));
	}
}
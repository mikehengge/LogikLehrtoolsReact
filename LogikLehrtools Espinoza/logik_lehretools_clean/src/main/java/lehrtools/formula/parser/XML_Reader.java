package lehrtools.formula.parser;

import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
/**
 * Class that reads an xml file describing the input language
 * used for a propositional formula. The LAnguages are stored as nodes.
 *
 */
public class XML_Reader {
	/**
	 * _node_list is the variable used to contain all the read language nodes.
	 */
	NodeList _node_list;
	/**
	 * Constructor for the XML_Reader
	 * Sets up the XML_Reader to red a given file passed a a parameter to the constructor. 
	 * Here the xml document is read and all the nodes representing a LAnguage are storen in 
	 * _node_list
	 * @param path : path to the File containing the languages in xml format.
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public XML_Reader(String path) throws ParserConfigurationException, SAXException, IOException
	{
		//File inputFile = new File(path);
		InputStream inputFile = XML_Reader.class.getResourceAsStream(path);
		DocumentBuilderFactory xml_factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
		dBuilder = xml_factory.newDocumentBuilder();
		Document doc = dBuilder.parse(new InputSource(inputFile));
		doc.getDocumentElement().normalize();
		_node_list = doc.getElementsByTagName("Language");
		
		
		
       
	}
	
	/**
	 * Extracts the information for each language stored in the _node_list.
	 * Then it creates a language and stores it into a list that is returned after all nodes 
	 * where processed.
	 * @return : LinkedList containing the given languages.
	 */
	public LinkedList<Input_Language> get_languages()
	{
		/**
		 * Stores every succesfully created Input_Language class.
		 */
		LinkedList<Input_Language> languages = new LinkedList<>();
		/**
		 * iterates through each node , retrieveing the information needed to 
		 * create an Input_Language class.
		 */
		for (int index = 0; index < _node_list.getLength(); index++)
		{
			Node node = _node_list.item(index);
			if(node.getNodeType() == Node.ELEMENT_NODE)
			{
				Element element = (Element) node;
				String name = element
						      .getElementsByTagName("Name")
						      .item(0)
						      .getTextContent();
				String and = element
					      .getElementsByTagName("AND")
					      .item(0)
					      .getTextContent();
				String or = element
					      .getElementsByTagName("OR")
					      .item(0)
					      .getTextContent();
				String not = element
					      .getElementsByTagName("NOT")
					      .item(0)
					      .getTextContent();
				
				
				String description = element
					      			 .getElementsByTagName("Description")
					      			 .item(0)
					      			 .getTextContent();
				if( !element.getElementsByTagName("DescriptionOR").item(0).getTextContent().isEmpty()) 				
					description += "\n" + element
		      			 			  	  .getElementsByTagName("DescriptionOR")
		      			 			  	  .item(0)
		      			 			  	  .getTextContent();
				if( !element.getElementsByTagName("DescriptionAND").item(0).getTextContent().isEmpty()) 	
					description += "\n" + element
			 			  			  	  .getElementsByTagName("DescriptionAND")
			 			  			  	  .item(0)
			 			  			  	  .getTextContent();
				if( !element.getElementsByTagName("DescriptionNOT").item(0).getTextContent().isEmpty()) 
					description += "\n" + element
			 			  			  	  .getElementsByTagName("DescriptionNOT")
			 			  			  	  .item(0)
			 			  			  	  .getTextContent();
				description += "\n" + element
			 			  			  .getElementsByTagName("DescriptionEx")
			 			  			  .item(0)
			 			  			  .getTextContent();
				String and_as_string = "";
				if(!element.getElementsByTagName("ANDString").item(0).getTextContent().isEmpty())
					and_as_string = element.getElementsByTagName("ANDString").item(0).getTextContent();
				
				languages.add(new Input_Language(name,
												 description,
												 and,
												 or,
												 not,
												 and_as_string
												 ));
			}
		}
		
		return languages;
	}

}

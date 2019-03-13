package lehrtools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.awt.event.*;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import javafx.application.Application;

import javafx.stage.Stage;
import lehrtools.formula.Variable;
import lehrtools.formula.parser.Input_Language;
import lehrtools.formula.parser.XML_Reader;
import lehrtools.model.Resolution;

public class Main {
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		XML_Reader reader;
		//Loading the reader to parse the incoming formula
		reader = new XML_Reader("InputLanguages.xml");
		LinkedList<Input_Language> _languages;
		_languages = reader.get_languages();
		//Select language that is used on incoming formula
		Input_Language language = _languages.get(0);
		
		Resolution resolution;
		//Initiate heuristik
		ArrayList<Variable> heuristik = new ArrayList<Variable>();
		//Case 1: Heuristik given in input
		if (args.length == 2) {
			//Fill heuristik with given Heuristik
			String[] heuristiks = args[1].split(",");
			for (String variable : heuristiks) {
				heuristik.add(new Variable(variable));
			}
			//Start resolution with input 
			resolution = new Resolution(lehrtools.view.Main_Menu_Controller.get_Formula(args[0], "Resolution", language), heuristik);

		}//Case 2: No Heuristik given in input
		else {
			//Start resolution with input
			resolution = new Resolution(lehrtools.view.Main_Menu_Controller.get_Formula(args[0], "Resolution", language));

		}
		//Print result
		if (resolution.proof() == 1)
			System.out.println("End: Result: Formula is satisfiable");
		else
			System.out.println("End: Result: Formula is unsatisfiable");
		System.exit(0);
	}
}

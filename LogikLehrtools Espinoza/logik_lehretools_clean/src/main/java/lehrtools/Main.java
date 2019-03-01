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

//	@Override
//	public void start(Stage primaryStage) {
//		try {
//			BorderPane root = new BorderPane();
//			Parent root = FXMLLoader.load(getClass().getResource("/lehrtools/view/Main_Menu.fxml"));
//			Scene scene = new Scene(root,1024,768);
//			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
//			primaryStage.setScene(scene);
//			primaryStage.initStyle(StageStyle.UNDECORATED);
//			primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/lehrtools/icon.png")));
//			primaryStage.toFront();
//			primaryStage.show();
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//	}

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		// launch(args);
		XML_Reader reader;
		reader = new XML_Reader("InputLanguages.xml");
		LinkedList<Input_Language> _languages;
		_languages = reader.get_languages();
		Input_Language language = _languages.get(0);
		
		Resolution resolution;
		ArrayList<Variable> heuristik = new ArrayList<Variable>();
		
		if (args.length == 2) {
			String[] heuristiks = args[1].split(",");
			for (String variable : heuristiks) {
				heuristik.add(new Variable(variable));
			}
			resolution = new Resolution(lehrtools.view.Main_Menu_Controller.get_Formula(args[0], "Resolution", language), heuristik);

		}
		else {
			resolution = new Resolution(lehrtools.view.Main_Menu_Controller.get_Formula(args[0], "Resolution", language));

		}
		
//		
		
//		heuristik.add(new Variable("x1"));
//		heuristik.add(new Variable("x2"));
//		heuristik.add(new Variable("x3"));

		if (resolution.proof() == 1)
			System.out.println("End: Result: Formula is satisfiable");
		else
			System.out.println("End: Result: Formula is unsatisfiable");
		System.exit(0);
	}

//	
//	private void buttonEventHandler(Event event)
//	{
//		boolean language_error = false;
//		Formula_NF formula = new Formula_NF();
//		String input_language ="";
//		String scene_location="";
//		FXMLLoader loader;
//		Parent parent;
//		//ResolutionController res_controller;
//		try
//		{
//			input_language = ((ToggleButton)_toggle.getSelectedToggle()).getText();
//		
//		}catch(NullPointerException e)
//		{
//			language_error = true;
//		}
//		
//		
//		if((pane_center_textfield.getText().isEmpty()&& _input.isEmpty())|| _selected.getText()=="empty"|| language_error)
//		{
//			error_info_text.setText("");
//			error_info_method.setText("");
//			error_info_language.setText("");
//			if(pane_center_textfield.getText().isEmpty() && _input.isEmpty())
//				error_info_text.setText(" Sie müssen eine Eingabe machen. ");
//			if(_selected.getText()=="empty")
//				error_info_method.setText(" Sie müssen ein Verfahren auswählen. ");
//			if(language_error)
//				error_info_language.setText("Wählen Sie eine Eingabesprache. ");
//			
//			return;
//			
//		} else
//		{
//			error_info_text.setText("");
//			error_info_method.setText("");
//			error_info_language.setText("");
//		}
//		
//		try
//		{
//            if(!pane_center_textfield.getText().isEmpty()) {
//                formula = get_Formula(pane_center_textfield.getText(),
//                                      _selected.getText(),
//                                      get_language(input_language));
//                _input.add(formula);
//                pane_center_textfield.setText("");
//            }
//				
//		} catch(ParserException e)
//		{
//			error_info_text.setText(e.getMessage());
//			return;
//		}
//
//		Formula_NF final_formula = new Formula_NF();
//		for(Formula_NF f : _input) {
//           for(Clause clause : f.get_clauses())
//           {
//               clause.set_prefix("", new ArrayList<>());
//               final_formula.add_clause(clause);
//           }
//        }
//
//		switch(_selected.getText())
//		{
//		case "Resolution" : scene_location = "/lehrtools/view/Resolution_View.fxml";
//							break;
//		case "BD-Resolution" : scene_location = "/lehrtools/view/BD_Resolution_View.fxml";
//							   break;
//		case "DP60"	 : scene_location = "/lehrtools/view/DP60_View.fxml";
//					   break;
//
//		default: return;
//						  
//		}
//		
//		try {
//			loader = new FXMLLoader(getClass().getResource(scene_location));
//			parent =  loader.load();
//
//			
//			switch(_selected.getText())
//			{
//			case "Resolution" : Resolution resolution = new Resolution(final_formula);
//								ResolutionViewModel resolution_view_model= new ResolutionViewModel(resolution);
//								ResolutionController res_controller = loader.getController();
//								res_controller.initialize_components(resolution_view_model);
//								resolution.attach(resolution_view_model);
//								Scene scene = new Scene(parent);
//								Stage app_stage = (Stage) ((Node) event.getSource() ).getScene().getWindow();
//								app_stage.hide();
//								app_stage.setScene(scene);
//								res_controller.set_up_window_state(is_fullscreen ,previous_width, previous_heigth, previous_x_pos, previous_y_pos);
//								
//								app_stage.show();
//								break;
//			case "BD-Resolution" : BDResolution bd_resolution = new BDResolution(final_formula);
//								   BDResolutionViewModel bd_view_model = new BDResolutionViewModel(bd_resolution);
//								   BD_Resolution_Controller bd_res_controller = loader.getController();
//								   bd_res_controller.initialize_components(bd_view_model);
//								   bd_resolution.attach(bd_view_model);
//								   Scene scene_bd = new Scene(parent);
//								   Stage app_stage_bd = (Stage) ((Node) event.getSource() ).getScene().getWindow();
//								   app_stage_bd.hide();
//								   app_stage_bd.setScene(scene_bd);
//								   bd_res_controller.set_up_window_state(is_fullscreen ,previous_width, previous_heigth, previous_x_pos, previous_y_pos);
//								   app_stage_bd.show();
//								   break;
//				case "DP60" : DP60 dp60 = new DP60(final_formula);
//							  DP60ViewModel dp60_view_model = new DP60ViewModel(dp60);
//							  DP60_Controller dp60_controller = loader.getController();
//							  dp60_controller.initialize_components(dp60_view_model);
//							  dp60.attach(dp60_view_model);
//							  Scene scene_dp = new Scene(parent);
//							  Stage app_stage_dp60 = (Stage) ((Node) event.getSource() ).getScene().getWindow();
//							  app_stage_dp60.hide();
//							  app_stage_dp60.setScene(scene_dp);
//							  dp60_controller.set_up_window_state(is_fullscreen ,previous_width, previous_heigth, previous_x_pos, previous_y_pos);
//							  app_stage_dp60.show();
//							  break;
//
//			default: return;
//							  
//			}
//			
//		} catch (IOException e) {
//
//			e.printStackTrace();
//		}
//		
//		
//		
//		
//	}
}

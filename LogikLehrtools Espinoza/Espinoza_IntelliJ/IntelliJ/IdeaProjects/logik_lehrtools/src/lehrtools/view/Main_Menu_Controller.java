package lehrtools.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.xml.parsers.ParserConfigurationException;

import javafx.scene.control.*;
import lehrtools.model.*;
import lehrtools.viewmodel.DP60ViewModel;
import org.xml.sax.SAXException;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lehrtools.formula.Formula;
import lehrtools.formula.parser.Input_Language;
import lehrtools.formula.parser.XML_Reader;
import lehrtools.formula.parser.lexing.Lexer_Clause;
import lehrtools.formula.parser.lexing.Lexer_Formula;
import lehrtools.formula.parser.parsing.ParserException;
import lehrtools.formula.parser.parsing.Parser_Clause;
import lehrtools.formula.parser.parsing.Parser_Gen;
import lehrtools.viewmodel.BDResolutionViewModel;
import lehrtools.viewmodel.ResolutionViewModel;

public class Main_Menu_Controller {
	/**
	 * The root Node of the scene
	 */
	@FXML
	BorderPane root_node;
	/**
	 * The pane at the top of the hierarchy in the bottom node of the root_node.
	 */
	@FXML
	Pane bottom_pane;
	/**
	 * The pane at the top of the hierarchy in the center node of the root_node.
	 */
	@FXML
	Pane pane_center;
	/**
	 * Label that selects the Resolution Procedure.
	 */
	@FXML
	Label label_resolution;
	/**
	 * Label that selects the Backward Dual Resolution procedure.
	 */
	@FXML
	Label label_bd_resolution;
	/**
	 * Label that selects the DP60 procedure
	 */
	@FXML
	Label label_dp60;
	/**
	 *Label in the center Node containg the text "Formeleingabe :"
	 */
	@FXML
	Label pane_center_label;
	/**
	 * TestField used for the input of the formula
	 */
	@FXML
	TextField pane_center_textfield;
	/**
	 * Button that Starts the procedures if all requirements are met.
	 */
	@FXML
	Button start_button;
	/**
	 * Scrollpane that host the Vbox with multiple inputs.
	 */
	@FXML
    ScrollPane formula_scroll_pane;
	/**
	 * Displays formulas in the case of multiple inputs.
	 */
	@FXML
	VBox multiple_inputs_vbox;
	/**
	 * Hbox that hosts the togglebox used to choose the input language.
	 */
	@FXML
	HBox toggle_box;
	/**
	 * Label that displays an error message when no input has been given or when the
	 * parser raised an error.
	 */
	@FXML
	Label error_info_text;
	/**
	 * Label containing an error message when no procedure was selected.
	 */
	@FXML
	Label error_info_method;
	/**
	 * Label containing an error message when no input language was selected.
	 */
	@FXML
	Label error_info_language;
	/**
	 * Displays a description for the selected input language.
	 */
	@FXML
	Label language_info;
	/**
	 * Displays a description for the selected procedure.
	 */
	@FXML
	Label method_info;
	/**
	 * The minimize  control button.
 	 */
	@FXML
	Label minimize;
	/**
	 * The maximioze  control button.
	 */
	@FXML
	Label maximize;
	/**
	 * The fullscreen  control button.
	 */
	@FXML
	Label fullscreen;
	/**
	 * The close  control button.
	 */
	@FXML
	Label close;

	/**
	 * Stores a reference to selected Label on the procedure panel.
	 */
	private Label _selected = new Label("empty");
	/**
	 * Stores the List of available input languages.
	 */
	private LinkedList<Input_Language> _languages;
	/**
	 * Toglegroup containing the buttons to select one of the input languages.
	 */
	private ToggleGroup _toggle;
	/**
	 * Value that signals if the Stage is maximized.
	 */
	private boolean is_fullscreen;
	/**
	 * Stores the width of the Stage before is maximized or is in fullscreen mode.
	 */
	private double previous_width;
	/**
	 * Stores the height of the Stage before is maximized or is in fullscreen mode.
	 */
	private double previous_heigth;
	/**
	 * Stores the x coordinate of the position of the Stage before is maximized or is in fullscreen mode.
	 */
	private double previous_x_pos;
	/**
	 * Stores the y coordinate of the position of the Stage before is maximized or is in fullscreen mode.
	 */
	private double previous_y_pos;
	/**
	 * Stores the position of the x coordinate of the cursor during a drag event.
	 * Is used to calculate the direction and distance the Stage is moved.
	 */
	private double xOffset = 0;
	/**
	 * Stores the position of the y coordinate of the cursor during a drag event.
	 * Is used to calculate the direction and distance the Stage is moved.
	 */
	private double yOffset = 0;
	/**
	 * The formula given as input.
	 */
	private ArrayList<Formula_NF> _input;

	/**
	 * Method automatically called by JavaFX when the Controller is created.
	 */
	public void initialize()
	{
		
		method_info.setText("Wählen Sie ein Verfahren.");
		_toggle = new ToggleGroup();
		initialize_stage();
		initialize_toggle_box();
		initialize_control_buttons();
		initialize_method_label();
        formula_scroll_pane.vvalueProperty().bind(multiple_inputs_vbox.heightProperty());
		is_fullscreen = false;
		start_button.requestFocus();	

		_input = new ArrayList<>();

	}

	/**
	 * Values set when the application switches to this scene.
	 * Refreshes the positions and size the previous scene had.
	 * @param is_fullscreen informs if the scene was maximized or not.
	 * @param previous_width the with of the previous scene.
	 * @param previous_heigth the height ofthe previous scene
	 * @param previous_x_pos the position of the x coordinate of the previous scene
	 * @param previous_y_pos the position of the y coordinate of the previous scene
	 */
	public void set_up_window_state(boolean is_fullscreen,
									double previous_width,
									double previous_heigth,
									double previous_x_pos,
									double previous_y_pos)
	{
		this.is_fullscreen = is_fullscreen;
		this.previous_width = previous_width;
		this.previous_heigth = previous_heigth;
		this.previous_x_pos = previous_x_pos;
		this.previous_y_pos = previous_y_pos;
	}

	/**
	 * Event Handler for the Start button. It will verify if all the
	 * requirements are met to start an procerdure. If the requirements are not met then
	 * the method will display an error message. If all requirements are met the Application will switch to the scene
	 * of the selected procedure.
	 * @param event Event instance.
	 */
	@FXML
	private void buttonEventHandler(Event event)
	{
		boolean language_error = false;
		Formula_NF formula = new Formula_NF();
		String input_language ="";
		String scene_location="";
		FXMLLoader loader;
		Parent parent;
		//ResolutionController res_controller;
		try
		{
			input_language = ((ToggleButton)_toggle.getSelectedToggle()).getText();
		
		}catch(NullPointerException e)
		{
			language_error = true;
		}
		
		
		if((pane_center_textfield.getText().isEmpty()&& _input.isEmpty())|| _selected.getText()=="empty"|| language_error)
		{
			error_info_text.setText("");
			error_info_method.setText("");
			error_info_language.setText("");
			if(pane_center_textfield.getText().isEmpty() && _input.isEmpty())
				error_info_text.setText(" Sie müssen eine Eingabe machen. ");
			if(_selected.getText()=="empty")
				error_info_method.setText(" Sie müssen ein Verfahren auswählen. ");
			if(language_error)
				error_info_language.setText("Wählen Sie eine Eingabesprache. ");
			
			return;
			
		} else
		{
			error_info_text.setText("");
			error_info_method.setText("");
			error_info_language.setText("");
		}
		
		try
		{
            if(!pane_center_textfield.getText().isEmpty()) {
                formula = get_Formula(pane_center_textfield.getText(),
                                      _selected.getText(),
                                      get_language(input_language));
                _input.add(formula);
                pane_center_textfield.setText("");
            }
				
		} catch(ParserException e)
		{
			error_info_text.setText(e.getMessage());
			return;
		}

		Formula_NF final_formula = new Formula_NF();
		for(Formula_NF f : _input) {
           for(Clause clause : f.get_clauses())
           {
               clause.set_prefix("", new ArrayList<>());
               final_formula.add_clause(clause);
           }
        }

		switch(_selected.getText())
		{
		case "Resolution" : scene_location = "/lehrtools/view/Resolution_View.fxml";
							break;
		case "BD-Resolution" : scene_location = "/lehrtools/view/BD_Resolution_View.fxml";
							   break;
		case "DP60"	 : scene_location = "/lehrtools/view/DP60_View.fxml";
					   break;

		default: return;
						  
		}
		
		try {
			loader = new FXMLLoader(getClass().getResource(scene_location));
			parent =  loader.load();

			
			switch(_selected.getText())
			{
			case "Resolution" : Resolution resolution = new Resolution(final_formula);
								ResolutionViewModel resolution_view_model= new ResolutionViewModel(resolution);
								ResolutionController res_controller = loader.getController();
								res_controller.initialize_components(resolution_view_model);
								resolution.attach(resolution_view_model);
								Scene scene = new Scene(parent);
								Stage app_stage = (Stage) ((Node) event.getSource() ).getScene().getWindow();
								app_stage.hide();
								app_stage.setScene(scene);
								res_controller.set_up_window_state(is_fullscreen ,previous_width, previous_heigth, previous_x_pos, previous_y_pos);
								
								app_stage.show();
								break;
			case "BD-Resolution" : BDResolution bd_resolution = new BDResolution(final_formula);
								   BDResolutionViewModel bd_view_model = new BDResolutionViewModel(bd_resolution);
								   BD_Resolution_Controller bd_res_controller = loader.getController();
								   bd_res_controller.initialize_components(bd_view_model);
								   bd_resolution.attach(bd_view_model);
								   Scene scene_bd = new Scene(parent);
								   Stage app_stage_bd = (Stage) ((Node) event.getSource() ).getScene().getWindow();
								   app_stage_bd.hide();
								   app_stage_bd.setScene(scene_bd);
								   bd_res_controller.set_up_window_state(is_fullscreen ,previous_width, previous_heigth, previous_x_pos, previous_y_pos);
								   app_stage_bd.show();
								   break;
				case "DP60" : DP60 dp60 = new DP60(final_formula);
							  DP60ViewModel dp60_view_model = new DP60ViewModel(dp60);
							  DP60_Controller dp60_controller = loader.getController();
							  dp60_controller.initialize_components(dp60_view_model);
							  dp60.attach(dp60_view_model);
							  Scene scene_dp = new Scene(parent);
							  Stage app_stage_dp60 = (Stage) ((Node) event.getSource() ).getScene().getWindow();
							  app_stage_dp60.hide();
							  app_stage_dp60.setScene(scene_dp);
							  dp60_controller.set_up_window_state(is_fullscreen ,previous_width, previous_heigth, previous_x_pos, previous_y_pos);
							  app_stage_dp60.show();
							  break;

			default: return;
							  
			}
			
		} catch (IOException e) {

			e.printStackTrace();
		}
		
		
		
		
	}

	/**
	 * EventHandler that is raised when proedure label is clicked.
	 * It will reference the selected label in teh field _selected and
	 * willhighlight the label.
	 * @param e Event instance
	 */
	@FXML
	private void label_clickedEventHandler(Event e)
	{
		if(_selected.getText()=="")
		{
			_selected = (Label) e.getSource();
			_selected.setId("pane_left_selection_selected");
			set_info_label();
		}else
		{
			_selected.setId("pane_left_selection");
			_selected = (Label) e.getSource();
			_selected.setId("pane_left_selection_selected");
			set_info_label();
		}
		error_info_method.setText("");
		
	}

	/**
	 * Triggered when a key is pressed while the application has focus on the TextField used for input.
	 * If the key pressed is the enter key it will try to parse the given input. If the parser raises an exception
	 * an error message is displayed. If the parsing was successful the for parsed formual will be displayed
	 * undeneath the TextField in the multiple_inputs_vbox. If the key F2 is pressed the last given formula is removed
	 * from the multiple input list.
	 * @param e Event instance.
	 */
	@FXML
	private void key_pressedEventHandler(KeyEvent e)
	{

		if(e.getCode() == KeyCode.ENTER)
		{
            if(pane_center_textfield.getText().isEmpty())
            {
                error_info_text.setText(" Sie müssen eine Eingabe machen. ");
                return;
            }

            if(_selected.getText()=="empty") {
                error_info_method.setText(" Sie müssen ein Verfahren auswählen. ");
                return;
            }

			String input_text = pane_center_textfield.getText();
			String input_language ="";
			try
			{
				input_language = ((ToggleButton)_toggle.getSelectedToggle()).getText();

			}catch(NullPointerException f)
			{
				error_info_language.setText("Wählen Sie eine Eingabesprache. ");
				return;
			}

			try
			{
				Formula_NF formula = get_Formula(input_text,
						                         _selected.getText(),
						                         get_language(input_language));

				_input.add(formula);
				Label new_input = new Label(pane_center_textfield.getText());
				new_input.setId("new_input_label");
				multiple_inputs_vbox.getChildren().add(new_input);
				pane_center_textfield.setText("");
				error_info_text.setText("");

			} catch(ParserException g)
			{
				error_info_text.setText(g.getMessage());
				return;
			}
		}
		else if(e.getCode() == KeyCode.F2)
		{
			if(!_input.isEmpty())
			{
				_input.remove(_input.size()-1);
				multiple_inputs_vbox.getChildren().remove(multiple_inputs_vbox.getChildren().size()-1);
			}
				
		}
	}

	/**
	 * Sets the functionality that enables the Stage to be dragged.
	 */
	private void initialize_stage()
	{
		root_node.setOnMousePressed( new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
		root_node.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	Stage stage = ((Stage)root_node.getScene().getWindow());
				if(is_fullscreen ||stage.isMaximized()) return;
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });
	
	}

	/**
	 * Generates the togglebox with the buttons for the input languages.
	 */
	private void initialize_toggle_box()
	{
		XML_Reader reader;

		try 
		{
			reader = new XML_Reader("InputLanguages.xml");
			_languages = reader.get_languages();
		} catch(ParserConfigurationException e)
		{
			error_info_text.setText("A Parse error happened while reading the xml file");
			return;
		}catch (SAXException e)
		{
			error_info_text.setText("A SAX error happened while reading the xml file");
			return;
		}
		catch(IOException e)
		{
			error_info_text.setText("A IO error happened while reading the xml file");
			return;
		}
		 
		for(Input_Language language : _languages)
		{
			ToggleButton button = new ToggleButton();
			button.setId("toggle_button");
			button.setText(language.name);
			button.setPrefWidth(100);
			button.setToggleGroup(_toggle);
			button.setOnMouseEntered((event) -> {

		        language_info.setText(language.description);

		    });
			button.setOnMouseExited((event) -> {

				set_language_info();

			});
			
		 
			toggle_box.getChildren().add(button);
			
		}
		language_info.setText("Wählen Sie eine Eingabesprache. ");
	}

	/**
	 * Sets an event handler for each procedure label.
	 * This EventHandler will display a description of the procedure
	 * while the cursor hovers over the label.
	 */
	private void initialize_method_label()
	{
		label_resolution.setOnMouseEntered((event) ->{
			method_info.setText("Resolution ist ein Entscheidungsverfahren um festzustellen, ob eine logische Formel erfüllbar ist. \n"
					 + "Dieses ist ein Widerlegungsverfahren. Anstatt die Allgemeingültigkeit direkt zu beweisen, zeigt \n"
					 + "es die Unerfüllbarkeit der Verneinung.");
		}
				);
		label_resolution.setOnMouseExited((event) -> {
			set_info_label();
		}
				);
		
		label_bd_resolution.setOnMouseEntered((event) ->{
			method_info.setText("Minimierungsverfahren durch Berrechnung der Primimplikanten. "
					+ "\n Primimplikanten werden durch Resolution und Absorption berrechent.");
		}
				);
		label_bd_resolution.setOnMouseExited((event) -> {
			set_info_label();
		}
				);
		
		label_dp60.setOnMouseEntered((event) ->{
			method_info.setText("Entscheidungsverfahren um festzustellen, ob eine logische Formel erfüllbar ist.\n "
					+ "Dies wird erreicht durch das Reduzieren der Erfüllbarkeit für eine Formel mit n Variablen auf das \n" +
					"Erfüllbarkeitsproblem für Formeln mit maximal n − 1 Variablen.");
		}
				);
		label_dp60.setOnMouseExited((event) -> {
			set_info_label();
		}
				);
	}

	/**
	 * Sets the EventHandlers for the control buttons.
	 */
	private void initialize_control_buttons()
	{
		close.setOnMouseClicked(e -> ((Stage)root_node.getScene().getWindow()).close());
		minimize.setOnMouseClicked(e -> ((Stage)root_node.getScene().getWindow()).setIconified(true));
		maximize.setOnMouseClicked(e -> maximize_event() );
		fullscreen.setOnMouseClicked(e -> fullscreen_event() );
        previous_width = 1024;
        previous_heigth = 786;

	}

	/**
	 * method called by the EventHandler of te maximize label.
	 */
    @SuppressWarnings("Duplicates")
    private void maximize_event() {
        Stage stage = ((Stage)root_node.getScene().getWindow());
        if(is_fullscreen)
        {
            stage.setWidth(previous_width);
            stage.setHeight(previous_heigth);
            stage.setX(previous_x_pos);
            stage.setY(previous_y_pos);
            is_fullscreen = false;

        }else
        {
            if(!stage.isMaximized())
            {
                previous_x_pos = stage.getX();
                previous_y_pos = stage.getY();
            }


            stage.setMaximized(false);
            Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX(primaryScreenBounds.getMinX());
            stage.setY(primaryScreenBounds.getMinY());
            stage.setWidth(primaryScreenBounds.getWidth());
            stage.setHeight(primaryScreenBounds.getHeight());
            is_fullscreen = true;


        }

    }

    /**
     * Makes the window fit the whole screen
	 * Is called by the EventHandler of the fullscreen label.
     */
	private void fullscreen_event()
	{
		Stage stage = ((Stage)root_node.getScene().getWindow());
        //noinspection Duplicates
        if(stage.isMaximized())
		{
            stage.setMaximized(false);
            stage.setWidth(previous_width);
            stage.setHeight(previous_heigth);
            stage.setX(previous_x_pos);
            stage.setY(previous_y_pos);
			
		}
		else
		{	

			if(is_fullscreen)
			    is_fullscreen = false;
			else
            {
                previous_x_pos = stage.getX();
                previous_y_pos = stage.getY();
            }
            stage.setMaximized(true);
			
			
		}

	}

	/**
	 * Displays a description of a selected input language.
	 */
	private void set_language_info()
	{
		String selected_language="";
		try
		{
		selected_language = ((ToggleButton)_toggle.getSelectedToggle()).getText();
		
		}catch(NullPointerException e)
		{
			language_info.setText("Wählen Sie eine Eingabesprache. ");
		}
		
		
		for(Input_Language language : _languages)
		{
			if(selected_language.equals(language.name))
			{
				language_info.setText(language.description);
                error_info_language.setText("");
				return;
			}
			
			
		}

		
	}

	/**
	 * Displays a description at the bottom of the scene for a selected procedure.
	 */
	private void set_info_label()
	{
		switch(_selected.getText())
		{
		case "Resolution" : method_info.setText("Resolution ist ein Entscheidungsverfahren um festzustellen, ob eine logische Formel erfüllbar ist. \n"
											 + "Dieses ist ein Widerlegungsverfahren. Anstatt die Allgemeingültigkeit direkt zu beweisen, zeigt \n"
											 + "es die Unerfüllbarkeit der Verneinung.");
			break;
		case "BD-Resolution" : method_info.setText("Minimierungsverfahren durch Berrechnung der Primimplikanten. "
							+ "\n Primimplikanten werden durch Resolution und Absorption berrechent.");
			break;
		case "DP60" : method_info.setText("Entscheidungsverfahren um festzustellen, ob eine logische Formel erfüllbar ist.\n "
				+ "Dies wird erreicht durch das Reduzieren der Erfüllbarkeit für eine Formel mit n Variablen auf das \n" +
				"Erfüllbarkeitsproblem für Formeln mit maximal n − 1 Variablen.");
			break;
		default: method_info.setText("Wählen Sie ein Verfahren.");
		}
	}

	/**
	 * Method that retrieves an Input_Language instance on function of a string.
	 * @param selected_language name of the Input_Language innstance.
	 * @return Input_Language instance.
	 */
	private Input_Language get_language(String selected_language)
	{
		
		
		Input_Language selected = new Input_Language("null","null","","","","");
		for(Input_Language language : _languages)
		{
			if(selected_language.equals(language.name))
			{
				
				selected = language;
				break;
			}
			
			
		}
		
		return selected;
		
	}

	/**
	 * Parses given input string into a formula in normalform.
	 * @param given_formula input string representing the formula to be parsed.
	 * @param method the selected procedure.
	 * @param selected_language the selected input language.
	 * @return Formula_NF instance.
	 */
	private Formula_NF get_Formula(String given_formula,String method, Input_Language selected_language)
	{
		{
			if(!selected_language.name.equals( "Klauseln"))
			{
				Lexer_Formula lex = new Lexer_Formula(selected_language.and,selected_language.or,selected_language.neg);
				Parser_Gen parse = new Parser_Gen();
				Formula formula_raw =  parse.parse(lex.tokenize(given_formula));
				
				if(method.equals("Resolution") || method.equals("DP60"))
					return formula_raw.cnf_set_base_representation();
				else if(method.equals("BD-Resolution"))
				{
					Formula_NF dnf_set_base = formula_raw.dnf_set_base_representation();
					Formula_NF new_dnf_set_base = new Formula_NF();
					for(Clause clause : dnf_set_base.get_clauses())
					{
						if(clause.isDegenarate()) continue;
						Clause cnf_clause = new Clause();
						for(Literal literal : clause.getLiterals())
							cnf_clause.addLiteral(literal);
						new_dnf_set_base.add_clause(cnf_clause);
					}
					return new_dnf_set_base;
				}
				else return null;
			}
			else 
			{
				Lexer_Clause lex_c = new Lexer_Clause();
				Parser_Clause parse_c = new Parser_Clause();
				return parse_c.parse(lex_c.tokenize(given_formula)); 
			}
		}
		
	}
	

	
	
}

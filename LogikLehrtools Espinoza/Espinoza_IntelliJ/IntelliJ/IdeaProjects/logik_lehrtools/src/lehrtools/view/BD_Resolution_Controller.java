package lehrtools.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lehrtools.miscellanious.Text_Writer;
import lehrtools.model.state.ModelState;
import lehrtools.view.components.HBox_Factory;
import lehrtools.viewmodel.BDResolutionViewModel;
import lehrtools.viewmodel.ModelEvent;
import lehrtools.viewmodel.information.Line;
import lehrtools.viewmodel.information.Line_Double;

public class BD_Resolution_Controller {
	/**
	 * A reference to the ViewModel
	 */
	private BDResolutionViewModel _view_model;
	/**
	 * List that will by bind itself with the ViewModel.
	 * Will contain information about the steps executed by the algorithm.
	 */
	private ObservableList<Line> _steps;
	/**
	 * Class used to generate the HBox and Vbox for the Steps and calculations to be displayed respectively.
	 */
	private HBox_Factory _hbox_factory;
	/**
	 * Root Node
	 */
	@FXML
	private BorderPane root_node;
	/**
	 * ScrollPane that enables the scrolling of the displayed results in the center node of the root node.
	 * Is a child node or center_Achor_pane.
	 */
	@FXML
	private ScrollPane results_scroll_pane;
	/**
	 * Child Node or results_scroll_pane.
	 * This node will display the result of each step.
	 */
	@FXML
	private VBox vbox_steps;
	/**
	 * Button that triggers a Resolution step.
	 */
	@FXML
	private Button button_resolution;
	/**
	 * Button that triggers the an absorption step
	 */
	@FXML
	private Button button_fw_subsumption;
	/**
	 * Button that triggers the an absorption step
	 */
	@FXML
	private Button button_bw_subsumption;
	/**
	 * Button that will end the algorithm and switch the scene back to the Main Menu
	 */
	@FXML
	private Button button_end;
	/**
	 * Triggers a steb back event. This will revert the calculations done by
	 * the last user input.
	 */
	@FXML
	private Button button_back;
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
	 * Pane that will host the nodes containing the calculations for each step.
	 */
	@FXML
	private AnchorPane additional_info_pane;
	/**
	 * Container that helps display the calculations
	 */
	@FXML
	private VBox additional_info;
	/**
	 * ScrollPane that enables the scrolling of the displayed calculations .
	 */
	@FXML
	private ScrollPane additional_info_scrollpane;
	/**
	 * Box that displays the calculation.
	 */
	@FXML
	private VBox additional_info_vbox;
	/**
	 * A reference to calcualation being "highlighted"
	 */
	@FXML
	private Label calculations_selected;



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
	
	
	public BD_Resolution_Controller()	
	{
		
	}
	/**
	 * Sets up all EventHandlers and bindings.
	 * @param view_model ResolutionViewModel instance.
	 */
	public void  initialize_components(BDResolutionViewModel view_model)
	{
		initialize_root_node();
		initialize_control_buttons();
		initialize_steps();
		_view_model = view_model;
		_hbox_factory = new HBox_Factory();

		Bindings.bindContentBidirectional(_steps, _view_model._lines);
		
		button_resolution.visibleProperty().bind(_view_model._resolution);
		button_fw_subsumption.visibleProperty().bind(_view_model._forward);
		button_bw_subsumption.visibleProperty().bind(_view_model._backward);
		button_end.visibleProperty().bind(_view_model._end);
		
		VBox.setVgrow(results_scroll_pane, Priority.ALWAYS);
		vbox_steps.prefWidthProperty().bind(results_scroll_pane.widthProperty().subtract(15));
		results_scroll_pane.vvalueProperty().bind(vbox_steps.heightProperty());

		VBox.setVgrow(additional_info_scrollpane, Priority.ALWAYS);
		additional_info_vbox.prefWidthProperty().bind(additional_info_scrollpane.widthProperty().subtract(15));
		additional_info_vbox.prefHeightProperty().bind(additional_info_scrollpane.heightProperty().subtract(2));
		additional_info_scrollpane.vvalueProperty().bind(additional_info_vbox.heightProperty());


		calculations_selected.setVisible(true);

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
		Stage stage = ((Stage)root_node.getScene().getWindow());
		if(is_fullscreen || stage.isMaximized() )
			root_node.setRight(additional_info_pane);
	}

	/**
	 * EventHandler for button_resolution.
	 */
	@FXML
	private void resolutionEventHandler()
	{
		_view_model.execute(new ModelEvent(this, ModelState.RESOLUTION,""));
	}

	/**
	 * EventHandler for button_fw_subsumption
	 */
	@FXML
	private void fw_subsumptionEventHandler()
	{
		_view_model.execute(new ModelEvent(this, ModelState.SUBSUMPTION_FW,""));
	}

	/**
	 * EventHandler for button_bw_subsumption.
	 */
	@FXML
	private void bw_subsumptionEventHandler()
	{
		_view_model.execute(new ModelEvent(this, ModelState.SUBSUMPTION_BW,""));
	}
	/**
	 * Event Handler for button_end.
	 */
	@FXML
	private void endEventHandler(Event event)
	{
		Text_Writer.write_to_file(_steps);
		//noinspection Duplicates
		try {
			//Parent resolution_parent = FXMLLoader.load(getClass().getResource("/lehrtools/view/Main_Menu.fxml"));
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/lehrtools/view/Main_Menu.fxml"));
			Parent parent =  loader.load();
			Main_Menu_Controller controller = loader.getController();
			controller.set_up_window_state(is_fullscreen, previous_width, previous_heigth, previous_x_pos, previous_y_pos);
			Scene scene = new Scene(parent);
			Stage app_stage = (Stage) ((Node) event.getSource() ).getScene().getWindow();
			app_stage.hide();
			app_stage.setScene(scene);
			app_stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Event Handler for the Button  button_back
	 */
	@FXML
	private void back_EventHandler()
	{
		_view_model.execute(new ModelEvent(this,ModelState.BACK,""));
	}
	/**
	 * Sets the functionality that enables the Stage to be dragged.
	 */
	private void initialize_root_node()
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
		root_node.setRight(null);
	}
	/**
	 * Creates the EventHandler for StepList.
	 * When triggered this EvenHandler will add or remove content from
	 * the VBox containing te steps and the VBox containing the calculations
	 *
	 */
	private void initialize_steps()
	{
		final List<Line> list = new ArrayList<>();
		_steps =  FXCollections.observableList(list);

		_steps.addListener(new ListChangeListener<Line>() {

			@SuppressWarnings("Duplicates")
			@Override
			public void onChanged(ListChangeListener.Change<? extends Line> change) {
				while(!change.next()) return ;

				if(change.wasAdded())
				{
					vbox_steps.getChildren().clear();
					additional_info_vbox.getChildren().clear();
					//_subsumption_details.clear();
					for(Line line : _steps)
					{
						if(line.label == "Resolution" || line.label == "Start") {
							HBox hbox = _hbox_factory.get_box(line);
							vbox_steps.getChildren().add(hbox);
						}

						if(!((Line_Double) line).calculations.isEmpty())
						{
							additional_info_vbox.getChildren().add(new Label( ((Line_Double) line).formula_name + " (" + line.label + ")" ) );
							VBox vbox = new HBox_Factory().get_resolution_calculations(line, "BD");
							additional_info_vbox.getChildren().add(vbox);
							addEventHandlersCalculations(vbox);
						}

					}

				}
				else if(change.wasRemoved())
				{
					vbox_steps.getChildren().clear();
					additional_info_vbox.getChildren().clear();
				}

			}
		});
	}
	/**
	 * Sets the EventHandlers for the control buttons.
	 */
	@SuppressWarnings("Duplicates")
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
		//If Stage is maximized then return to original size
		if(is_fullscreen)
		{
			stage.setWidth(previous_width);
			stage.setHeight(previous_heigth);
			stage.setX(previous_x_pos);
			stage.setY(previous_y_pos);
			is_fullscreen = false;
			root_node.setRight(null);

		}else
		{
			//if Stage is fullscreen return to original size
			if(!stage.isMaximized())
			{

				previous_x_pos = stage.getX();
				previous_y_pos = stage.getY();
			}

			//maximize Stage
			stage.setMaximized(false);
			Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
			stage.setX(primaryScreenBounds.getMinX());
			stage.setY(primaryScreenBounds.getMinY());
			stage.setWidth(primaryScreenBounds.getWidth());
			stage.setHeight(primaryScreenBounds.getHeight());
			is_fullscreen = true;
			root_node.setRight(additional_info_pane);
		}
			
	}
	/**
	 * Makes the window fit the whole screen
	 * Is called by the EventHandler of the fullscreen label.
	 */
	@SuppressWarnings("Duplicates")
	private void fullscreen_event()
	{
		Stage stage = ((Stage)root_node.getScene().getWindow());
		//If stage is in fullscreen mode return to original size
		if(stage.isMaximized())
		{
			stage.setMaximized(false);
			stage.setWidth(previous_width);
			stage.setHeight(previous_heigth);
			stage.setX(previous_x_pos);
			stage.setY(previous_y_pos);
			root_node.setRight(null);

		}
		else
		{

			if(is_fullscreen)
				is_fullscreen = false;
			else
			{
				previous_x_pos = stage.getX();
				previous_y_pos = stage.getY();
				root_node.setRight(additional_info_pane);
			}
			stage.setMaximized(true);
		}
		
	}
	/**
	 * Adds Eventhanlders to all calculations displayed in the right pane of the GUI.
	 * This EvenHanlders will trigger the "Highlighting" of teh Clauses involved in the calculation.
	 * @param vbox Vbox instance containing the calculations.
	 */
	@SuppressWarnings("Duplicates")
	private void addEventHandlersCalculations(VBox vbox)
	{
		if(vbox.getChildren().size()>0)
		{
			for(int i = 0;i < vbox.getChildren().size();i++)
			{
				FlowPane flowPane = (FlowPane) vbox.getChildren().get(i);
				flowPane.setOnMouseClicked( event -> {
					String text = "";
					for(Node node : flowPane.getChildren())
						text = text+ get_node_text(node);
					_view_model.execute(new ModelEvent(this,ModelState.SHOW_CALCULATION,text));
				});

			}
		}
	}

	/**
	 * Gets the text displayed in a Nodeinstance.
	 * @param node  Node isntance.
	 * @return String
	 */
	@SuppressWarnings("Duplicates")
	private String get_node_text(Node node)
	{
		if(node instanceof HBox)
		{
			Node node1 = ((HBox) node).getChildren().get(0);
			Node node2 = ((HBox) node).getChildren().get(1);
			return ((Label)node1).getText() + ((Label)node2).getText();
		}
		else
			return ((Label) node).getText();
	}




}

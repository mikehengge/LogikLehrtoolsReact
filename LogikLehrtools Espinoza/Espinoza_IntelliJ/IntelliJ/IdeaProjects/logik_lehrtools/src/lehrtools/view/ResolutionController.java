package lehrtools.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lehrtools.miscellanious.Text_Writer;
import lehrtools.model.state.ModelState;
import lehrtools.view.components.HBox_Factory;
import lehrtools.view.components.Tree_Generator;
import lehrtools.viewmodel.ResolutionViewModel;
import lehrtools.viewmodel.information.Line_Composite;
import lehrtools.viewmodel.information.Line_Double;
import lehrtools.viewmodel.information.Line;
import lehrtools.viewmodel.ModelEvent;

public class ResolutionController {
    /**
     * A reference to the ViewModel
     */
	private ResolutionViewModel _view_model;
    /**
     * List that will by bind itself with the ViewModel.
     * Will contain information about the steps executed by the algorithm.
     */
	private ObservableList<Line> _steps_list;
    /**
     * List containing the Variables available for a resolution_over step.
     * Tis list will be bound to ViewModel
     */
	private ObservableList<String> _vars;
    /**
     * Shows or hides the dedcution tree
     */
	private BooleanProperty _binary_tree_visible;
    /**
     * Triggers a refreshes of all steps and calculations displayed.
     */
	private BooleanProperty _redraw;
    /**
     * Root Node
     */
	@FXML
	private BorderPane root_node;
    /**
     * Pane containing all nodes displayed in the center node of _root_node.
     */
	@FXML
    private AnchorPane center_anchor_pane;
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
     * MenuButton that trigger sa Resolution over step.
     */
	@FXML
	private MenuButton menu_resolution_over;
	@FXML
    /**
     * Button that triggers a forward subsumption step.
     */
	private Button button_fw_subsumption;
    /**
     * Button that triggers a backward subsumption step.
     */
	@FXML
	private Button button_bw_subsumption;
	@FXML
    /**
     * Button that will end the algorithm and switch the scene back to the Main Menu
     */
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
     * Label visible when the deduction tree is displayed.
     * When clicked it  removes the deduction tree and shows the calculations.
     */
	@FXML
    private Label calculations;
    /**
     * Displayed when the calculations are displayed. Has no Event handler, its mainly decorative.
     */
	@FXML
    private Label calculations_selected;
    /**
     * Visible when the calculations are displayed.
     * When clicked it removes the calculations and displays the deduction tree.
     */
	@FXML
    private Label deduction_tree;
    /**
     * Visible when the deduction tree is displayed.
     * Has no Event handler, its mainly decorative.
     */
	@FXML
    private Label deduction_tree_selected;
    /**
     * Spans the deduction tre across the whole width of the screen.
     */
	@FXML
    private Label deduction_tree_fullscreen;
    /**
     * resizes the deduction tree back to ist original size.
     */
	@FXML
    private Label deduction_tree_minimize;
    /**
     * Used to display the deduction tree.
     */
    private Canvas binary_tree_view;

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
     * A reference to the VBox displaying the steps for the actual iteration of the
     * algorithm.
     */
    private VBox actual_step_box;

    /**
     * Empty constructor
     */
	public ResolutionController(){	}

    /**
     * Empty initializer
     */
	public void  initialize(){	}

    /**
     * Sets up all EventHandlers and bindings.
     * @param view_model ResolutionViewModel instance.
     */
	public void initialize_components(ResolutionViewModel view_model)
	{
		
		_view_model = view_model;
		initialize_step_list();
		initialize_variable_list();
		initialize_root_node();
		initialize_binary_tree_view();
		initialize_redraw_event();


		
		Bindings.bindContentBidirectional(_steps_list, _view_model._steps);
		Bindings.bindContentBidirectional(_vars, _view_model._vars);
		button_resolution.visibleProperty().bind(_view_model._resolution);
		menu_resolution_over.visibleProperty().bind(_view_model._resolution_over);
		button_fw_subsumption.visibleProperty().bind(_view_model._forward);
		button_bw_subsumption.visibleProperty().bind(_view_model._backward);
		button_end.visibleProperty().bind(_view_model._end);
		_binary_tree_visible.bind(view_model._binary_tree_visible);
		menu_resolution_over.getItems().clear();


		VBox.setVgrow(results_scroll_pane, Priority.ALWAYS);
		vbox_steps.prefWidthProperty().bind(results_scroll_pane.widthProperty().subtract(15));
		results_scroll_pane.vvalueProperty().bind(vbox_steps.heightProperty());


		VBox.setVgrow(additional_info_scrollpane, Priority.ALWAYS);
        additional_info_vbox.prefWidthProperty().bind(additional_info_scrollpane.widthProperty().subtract(15));
		additional_info_vbox.prefHeightProperty().bind(additional_info_scrollpane.heightProperty().subtract(2));
		additional_info_scrollpane.vvalueProperty().bind(additional_info_vbox.heightProperty());
		calculations.setVisible(false  );
		calculations_selected.setVisible(true);
		deduction_tree.setVisible(true);
		deduction_tree_selected.setVisible(false);
		deduction_tree_fullscreen.setVisible(false);
		deduction_tree_minimize.setVisible(false);

		initialize_control_buttons();
		
		
		
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
     * Creates the EventHandler for StepList.
     * When triggered this EvenHandler will add or remove content from
     * the VBox containing te steps and the VBox containing the calculations
     *
     */
    private void initialize_step_list()
    {
        final List<Line> list = new ArrayList<>();
        _steps_list =  FXCollections.observableList(list);

        _steps_list.addListener(new ListChangeListener<Line>() {

            @SuppressWarnings("Duplicates")
            @Override
            public void onChanged(ListChangeListener.Change<? extends Line> change) {
                while(!change.next()) return ;

                if(change.wasAdded())
                {
                    Line line =  _steps_list.get(_steps_list.size()-1);
                    HBox box = new HBox_Factory().get_box(line);


                    if(line instanceof Line_Composite)
                    {
                        actual_step_box = (VBox)box.getChildren().get(1);
                        vbox_steps.getChildren().add(box);
                        addEventhandlers(box,line);
                    }
                    else if(line instanceof Line_Double )
                    {
                        actual_step_box.getChildren().add(box);
                        addEventhandlers(box,line);
                        if(!((Line_Double) line).calculations.isEmpty())
                        {
                            additional_info_vbox.getChildren().add(new Label( ((Line_Double) line).formula_name + " (" + line.label + ")" ) );
                            VBox vbox = new HBox_Factory().get_resolution_calculations(line, "RES");
                            additional_info_vbox.getChildren().add(vbox);
                            addEventHandlersCalculations(vbox);
                        }
                    }
                    else
                        vbox_steps.getChildren().add(box);
                }
                else if(change.wasRemoved())
                {
                    if(deduction_tree_minimize.isVisible())
                    {
                        deduction_tree_fullscreen.setVisible(true);
                        deduction_tree_minimize.setVisible(false);
                        root_node.setCenter(center_anchor_pane);
                        root_node.setRight(additional_info_pane);
                    }
                    vbox_steps.getChildren().clear();
                    additional_info_vbox.getChildren().clear();
                }


            }
        });
    }

    /**
     * EventHandler that updates the Variables offered in teh MenuButton (Resolution Over).
     */
    private void initialize_variable_list()
    {
        final List<String> other_list = new ArrayList<>();
        _vars = FXCollections.observableList(other_list);
        _vars.addListener(new ListChangeListener<String>() {

            @Override
            public void onChanged(ListChangeListener.Change<? extends String> change) {
                if(_vars.isEmpty())
                    menu_resolution_over.getItems().clear();
                else
                {
                    for(String variable : _vars)
                    {
                        MenuItem item = new MenuItem();
                        item.setText(variable);
                        item.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                adittional_info_labelEventHandler();
                                _view_model.execute(new ModelEvent(this,ModelState.RESOLUTION_OVER, item.getText()));
                            }
                        });
                        menu_resolution_over.getItems().add(item);
                    }
                }
            }
        });
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
     * Initializes the Canvas to draw the deduction trees.
     * It initializes and creates an EventHandler for the propoerty _binaary_tree_visible.
     * This EventHandler will show or hide the Canvas containing the deduction tree.
     */
    private void initialize_binary_tree_view()
    {
        binary_tree_view = new Canvas();
        binary_tree_view.setWidth(920);
        binary_tree_view.setHeight(532);
        _binary_tree_visible = new SimpleBooleanProperty(false);
        _binary_tree_visible.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0,
                                Boolean arg1, Boolean arg2) {
                if(arg2)
                {
                    binary_tree_view = new Canvas();
                    Tree_Generator.generate_tree(binary_tree_view, _view_model._selected_clause);
                    additional_info_scrollpane.setContent(binary_tree_view);
                    additional_info_pane.setPrefWidth(800);
                }
                else
                {
                    additional_info_scrollpane.setContent(additional_info_vbox);
                    additional_info_pane.setPrefWidth(500);
                }
            }
        });
    }

    /**
     * Initializes and creates the EventHandler for the property _redraw.
     * This EventHandler will clear all steps and calculations and
     * from the GUI and generate them all again. This EventHandler is triggerd
     * when a calculation was selected.
     */
    private void initialize_redraw_event()
    {
        _redraw = new SimpleBooleanProperty(false);
        _redraw.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue)
                {
                    vbox_steps.getChildren().clear();
                    additional_info_vbox.getChildren().clear();
                    for(Line line :_steps_list)
                    {
                        HBox box = new HBox_Factory().get_box(line);


                        //noinspection Duplicates
                        if(line instanceof Line_Composite)
                        {
                            actual_step_box = (VBox)box.getChildren().get(1);
                            vbox_steps.getChildren().add(box);
                            addEventhandlers(box,line);
                        }
                        else if(line instanceof Line_Double )
                        {
                            actual_step_box.getChildren().add(box);
                            addEventhandlers(box,line);


                            if(!((Line_Double) line).calculations.isEmpty())
                            {
                                additional_info_vbox.getChildren().add(new Label( ((Line_Double) line).formula_name + " (" + line.label + ")" ) );
                                VBox vbox = new HBox_Factory().get_resolution_calculations(line, "RES");
                                additional_info_vbox.getChildren().add(vbox);
                                addEventHandlersCalculations(vbox);
                            }



                        }
                        else
                            vbox_steps.getChildren().add(box);
                    }
                }
            }
        });
        _redraw.bindBidirectional(_view_model._redraw);
    }

    /**
     * Event Handler for the Button button_resolution
     */
	@FXML
	private void resolutionEventHandler()
	{
        adittional_info_labelEventHandler();
		_view_model.execute(new ModelEvent(this, ModelState.RESOLUTION,""));

	}
    /**
     * Event Handler for the Button  button_fw_subsumption
     */
	@FXML
	private void fw_subsumptionEventHandler()
	{
        adittional_info_labelEventHandler();
		_view_model.execute(new ModelEvent(this, ModelState.SUBSUMPTION_FW,""));
	}
    /**
     * Event Handler for the Button  button_bw_subsumption
     */
	@FXML
	private void bw_subsumptionEventHandler()
	{
        adittional_info_labelEventHandler();
		_view_model.execute(new ModelEvent(this, ModelState.SUBSUMPTION_BW,""));
	}
    /**
     * Event Handler for the Button  button_end.
     */
	@SuppressWarnings("Duplicates")
    @FXML
	private void endEventHandler(Event event)
	{
        Text_Writer.write_to_file(_steps_list);
		try {
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
     * Adds EventHandler to adittional_info_label
     */
	@FXML
	private void adittional_info_labelEventHandler()
    {
        if(deduction_tree_minimize.isVisible())
        {
            root_node.setCenter(center_anchor_pane);
            root_node.setRight(additional_info_pane);
        }

        calculations_selected.setVisible(true);
        calculations.setVisible(false);
        deduction_tree.setVisible(true);
        deduction_tree_selected.setVisible(false);
        deduction_tree_fullscreen.setVisible(false);
        deduction_tree_minimize.setVisible(false);

        if(_binary_tree_visible.get())
        {

            _view_model.execute(new ModelEvent(this, ModelState.HIDE_TREE,""));
        }
    }

    /**
     * EventHandler to show the deduction tree,
     */
    @FXML
    private void adittional_info_treeEventHandler() {
	    calculations_selected.setVisible(false);
	    calculations.setVisible(true);
	    deduction_tree.setVisible(false);
	    deduction_tree_selected.setVisible(true);
	    deduction_tree_fullscreen.setVisible(true);
	    deduction_tree_minimize.setVisible(false);
        _view_model.execute(new ModelEvent(this, ModelState.SHOW_TREE, _view_model._selected_clause.toString()));


    }

    /**
     * EventHandler to maximize the size of the deduction tree.
     */
    @FXML
    private void adittional_info_fullscreenEventHandler() {
	    deduction_tree_fullscreen.setVisible(false);
	    deduction_tree_minimize.setVisible(true);
	    root_node.setCenter(null);
	    root_node.setRight(null);
	    root_node.setCenter(additional_info_pane);

    }

    /**
     * EventHandler to hide the deduction tree.
     */
    @FXML
    private void adittional_info_minimizeEventHandler()
    {
        deduction_tree_fullscreen.setVisible(true);
        deduction_tree_minimize.setVisible(false);
        root_node.setCenter(center_anchor_pane);
        root_node.setRight(additional_info_pane);

    }

    /**
     * Adds an EventHandler to every Label displaying a clause. This EventHandler
     * adds teh functionality to display the  deduction tree for the clause.
     * @param box HBox displaying information of step.
     * @param line Line that was used to generate box.
     */
    private void addEventhandlers(HBox box, Line line)
    {

        if(line instanceof Line_Double)
        {
            FlowPane formula = (FlowPane) ((VBox) box.getChildren().get(1)).getChildren().get(1);
            addEventhandlers_helper(formula);
        }
        else
        {
            FlowPane formula = (FlowPane) ((VBox) box.getChildren().get(1)).getChildren().get(0);
            addEventhandlers_helper(formula);
        }
    }

    /**
     * Helper method for addEventHandlers(...).
     * Adds an Eventhandler to every child in the argument formula.
     * @param formula FlowPane instance.
     */
	private void addEventhandlers_helper(FlowPane formula)
    {

        if(formula.getChildren().size() > 2)
        {
            for(int i = 1 ; i< formula.getChildren().size()-1; i++)
            {
                Label label = (Label) formula.getChildren().get(i);
                label.setOnMouseClicked( (event) ->{
                            calculations_selected.setVisible(false);
                            calculations.setVisible(true);
                            deduction_tree.setVisible(false);
                            deduction_tree_selected.setVisible(true);
                            deduction_tree_fullscreen.setVisible(true);
                            deduction_tree_minimize.setVisible(false);
                            _view_model.execute(new ModelEvent(this, ModelState.SHOW_TREE, label.getText()));
                        }
                );
            }
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

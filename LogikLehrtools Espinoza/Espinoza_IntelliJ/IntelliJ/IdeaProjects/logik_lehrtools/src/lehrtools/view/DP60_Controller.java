package lehrtools.view;

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
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lehrtools.miscellanious.Text_Writer;
import lehrtools.model.state.ModelState;
import lehrtools.view.components.HBox_Factory;
import lehrtools.viewmodel.DP60ViewModel;
import lehrtools.viewmodel.ModelEvent;
import lehrtools.viewmodel.information.Line;
import lehrtools.viewmodel.information.Line_Composite;
import lehrtools.viewmodel.information.Line_DP60;
import lehrtools.viewmodel.information.Line_Double;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DP60_Controller
{
    /**
     * A reference to the ViewModel
     */
    private DP60ViewModel _view_model;
    /**
     * Class used to generate the HBox and Vbox for the Steps and calculations to be displayed respectively.
     */
    private HBox_Factory _hbox_factory;
    /**
     * List that will by bind itself with the ViewModel.
     * Will contain information about the steps executed by the algorithm.
     */
    private ObservableList<Line> _steps_list;
    /**
     * List containing all the Clauses available for a Unit Clause step.
     * The elements will be added as MenuItems menu_rule_1_over
     */
    private ObservableList<String> _rule_01_clauses;
    /**
     * List containing all the Pure Literlas available for a Pure Literal step.
     * The elements will be added as MenuItems menu_rule_2_over
     */
    private ObservableList<String> _rule_02_literals;
    /**
     * List containing all the Variables available for a Variable Elimination step.
     * The elements will be added as MenuItems menu_rule_3_over
     */
    private ObservableList<String> _rule_03_variables;
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
     * Button that triggers a Unit Clause Step
     */
    @FXML
    private Button button_rule_1;
    /**
     * Button that triggers a Pure Literal Step
     */
    @FXML
    private Button button_rule_2;
    /**
     * MenuButton containing MenuItems that will trigger
     * a Unit Clause step for a specific unit clause.
     */
    @FXML
    private MenuButton menu_rule_1_over;
    /**
     * MenuButton containing MenuItems that will trigger
     * a Pure Literal step for a specific pure literal.
     */
    @FXML
    private MenuButton menu_rule_2_over;
    /**
     * MenuButton containing MenuItems that will trigger
     * a Variable Elimination  step for a specific variable.
     */
    @FXML
    private MenuButton menu_rule_3_over;
    /**
     * Button that will end the algorithm and switch the scene back to the Main Menu
     */
    @FXML
    private Button button_end;
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
    // Right pane
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
     * Triggers a refreshes of all steps and calculations displayed.
     */

    private BooleanProperty _redraw;


    /**
     * Sets up all EventHandlers and bindings.
     * @param view_model ResolutionViewModel instance.
     */
    public void initialize_components(DP60ViewModel  view_model)
    {
        _view_model = view_model;
        _hbox_factory = new HBox_Factory();

        initialize_step_list();
        initialize_root_node();
        initialize_control_buttons();
        initialize_menu_buttons();
        initialize_calculation_pane();
        initialize_redraw_event();
        Bindings.bindContentBidirectional(_steps_list, _view_model.lines);
        Bindings.bindContentBidirectional(_rule_01_clauses, _view_model.rule_01);
        Bindings.bindContentBidirectional(_rule_02_literals, _view_model.rule_02);
        Bindings.bindContentBidirectional(_rule_03_variables, _view_model.rule_03);

        button_rule_1.visibleProperty().bind(menu_rule_1_over.visibleProperty());
        button_rule_2.visibleProperty().bind(menu_rule_2_over.visibleProperty());
        menu_rule_1_over.visibleProperty().bind(_view_model.rule_1_visible);
        menu_rule_2_over.visibleProperty().bind(_view_model.rule_2_visible);
        menu_rule_3_over.visibleProperty().bind(_view_model.rule_3_visible);
        button_end.visibleProperty().bind(_view_model.end);

        VBox.setVgrow(results_scroll_pane, Priority.ALWAYS);
        vbox_steps.prefWidthProperty().bind(results_scroll_pane.widthProperty().subtract(15));
        results_scroll_pane.vvalueProperty().bind(vbox_steps.heightProperty());


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
    @SuppressWarnings("Duplicates")
    private void initialize_step_list()
    {
        final List<Line> list = new ArrayList<>();
        _steps_list =  FXCollections.observableList(list);

        _steps_list.addListener((ListChangeListener<Line>) change -> {
            if(!change.next()) return ;

            if(change.wasAdded())
            {
                Line line =  _steps_list.get(_steps_list.size()-1);
                HBox box = _hbox_factory.get_box(line);
                vbox_steps.getChildren().add(box);

                if(line instanceof Line_DP60)
                {
                    VBox column2 = (VBox) box.getChildren().get(1);
                    Label calculation_label = (Label) column2.getChildren().get(1);
                    ScrollPane scrollpane = (ScrollPane) column2.getChildren().get(2);
                    column2.getChildren().remove(2);
                    column2.getChildren().remove(1);
                    additional_info_vbox.getChildren().add(calculation_label);
                    VBox calculations = (VBox) scrollpane.getContent();
                    addEventHandlersCalculations(calculations);
                    additional_info_vbox.getChildren().add(calculations);

                }
            }
            else if(change.wasRemoved())
            {
                vbox_steps.getChildren().clear();
                additional_info_vbox.getChildren().clear();
            }

        });
    }
    /**
     * Sets the functionality that enables the Stage to be dragged.
     */
    private void initialize_root_node()
    {
        root_node.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        root_node.setOnMouseDragged(event -> {
            Stage stage = ((Stage)root_node.getScene().getWindow());
            if(is_fullscreen ||stage.isMaximized()) return;
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }

    /**
     * Initializes all MenuButtons. It also initializes _rule_01_clauses,
     * _rule_02_literals and _rule_03_variables adding EventHanlders that update any change to the MenutItems
     * of the MenuButtons menu_rule_1_over , menu_rule_2_over and menu_rule_3_over respectively.
     *
     */
    private void initialize_menu_buttons()
    {
        final List<String> list_1 = new ArrayList<>();
        _rule_01_clauses = FXCollections.observableList(list_1);
        _rule_01_clauses.addListener(new ListChangeListener<>() {

            @Override
            public void onChanged(ListChangeListener.Change<? extends String> change) {
                if (_rule_01_clauses.isEmpty())
                    menu_rule_1_over.getItems().clear();
                else {
                    menu_rule_1_over.getItems().clear();
                    for (String variable : _rule_01_clauses) {
                        MenuItem item = new MenuItem();
                        item.setText(variable);
                        item.setOnAction(new EventHandler<>() {
                            @Override
                            public void handle(ActionEvent event) {
                                _view_model.execute(new ModelEvent(this, ModelState.RULE_01_OVER, item.getText()));
                            }
                        });
                        menu_rule_1_over.getItems().add(item);
                    }
                }


            }
        });

        final List<String> list_2 = new ArrayList<>();
        _rule_02_literals = FXCollections.observableList(list_2);
        _rule_02_literals.addListener(new ListChangeListener<>() {

            @Override
            public void onChanged(ListChangeListener.Change<? extends String> change) {
                if(_rule_02_literals.isEmpty())
                    menu_rule_2_over.getItems().clear();
                else
                {
                    menu_rule_2_over.getItems().clear();
                    for(String variable : _rule_02_literals)
                    {
                        MenuItem item = new MenuItem();
                        item.setText(variable);
                        item.setOnAction(new EventHandler<>() {
                            @Override
                            public void handle(ActionEvent event) {
                                _view_model.execute(new ModelEvent(this,ModelState.RULE_02_OVER, item.getText()));
                            }
                        });
                        menu_rule_2_over.getItems().add(item);
                    }
                }


            }
        });

        final List<String> list_3 = new ArrayList<>();
        _rule_03_variables = FXCollections.observableList(list_3);
        _rule_03_variables.addListener(new ListChangeListener<>() {

            @Override
            public void onChanged(ListChangeListener.Change<? extends String> change) {
                if(_rule_03_variables.isEmpty())
                    menu_rule_3_over.getItems().clear();
                else
                {
                    menu_rule_3_over.getItems().clear();
                    for(String variable : _rule_03_variables)
                    {
                        MenuItem item = new MenuItem();
                        item.setText(variable);
                        item.setOnAction(new EventHandler<>() {
                            @Override
                            public void handle(ActionEvent event) {
                                _view_model.execute(new ModelEvent(this,ModelState.RULE_03, item.getText()));
                            }
                        });
                        menu_rule_3_over.getItems().add(item);
                    }
                }


            }
        });
        menu_rule_1_over.getItems().clear();
        menu_rule_2_over.getItems().clear();
        menu_rule_3_over.getItems().clear();
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
     * Sets the bindings and grow properties for the Right Node of the root_node
     * so theeah component has the optimal size.
     */
    private void initialize_calculation_pane()
    {
        VBox.setVgrow(additional_info_scrollpane, Priority.ALWAYS);
        additional_info_vbox.prefWidthProperty().bind(additional_info_scrollpane.widthProperty().subtract(15));
        additional_info_vbox.prefHeightProperty().bind(additional_info_scrollpane.heightProperty().subtract(2));
        additional_info_scrollpane.vvalueProperty().bind(additional_info_vbox.heightProperty());

        root_node.setRight(null);
    }
    /**
     * Initializes and creates the EventHandler for the property _redraw.
     * This EventHandler will clear all steps and calculations and
     * from the GUI and generate them all again. This EventHandler is triggerd
     * when a calculation was selected.
     */
    @SuppressWarnings("Duplicates")
    private void initialize_redraw_event()
    {
        _redraw = new SimpleBooleanProperty(false);
        _redraw.addListener((observable, oldValue, newValue) -> {
            if(newValue)
            {
                vbox_steps.getChildren().clear();
                additional_info_vbox.getChildren().clear();
                for(Line line :_steps_list)
                {
                    HBox box = _hbox_factory.get_box(line);
                    vbox_steps.getChildren().add(box);

                    if(line instanceof Line_DP60)
                    {
                        VBox column2 = (VBox) box.getChildren().get(1);
                        Label calculation_label = (Label) column2.getChildren().get(1);
                        ScrollPane scrollpane = (ScrollPane) column2.getChildren().get(2);
                        column2.getChildren().remove(2);
                        column2.getChildren().remove(1);
                        additional_info_vbox.getChildren().add(calculation_label);
                        VBox calculations = (VBox) scrollpane.getContent();
                        addEventHandlersCalculations(calculations);
                        additional_info_vbox.getChildren().add(calculations);

                    }
                }
            }
        });
        _redraw.bindBidirectional(_view_model._redraw);
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
     * EventHandler for button_back
     */
    @FXML
    private void back_EventHandler(){_view_model.execute(new ModelEvent(this,ModelState.BACK,""));}
    /**
     * EventHandler for button_rule_1
     */
    @FXML
    private void rule1_EventHandler(){_view_model.execute(new ModelEvent(this,ModelState.RULE_01,""));}
    /**
     * EventHandler for button_rule_2
     */
    @FXML
    private void rule2_EventHandler(){_view_model.execute(new ModelEvent(this,ModelState.RULE_02,""));}
    /**
     * EventHandler for button_rule_3
     */
    @FXML
    private void rule3_EventHandler(){_view_model.execute(new ModelEvent(this,ModelState.RULE_03,""));}
    /**
     * Event Handler for  button_end.
     */
    @FXML
    private void endEventHandler(Event event)
    {
        Text_Writer.write_to_file(_steps_list);
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


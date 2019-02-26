//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package lehrtools.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener.Change;
import javafx.event.ActionEvent;
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
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lehrtools.miscellanious.Text_Writer;
import lehrtools.model.state.ModelState;
import lehrtools.view.components.HBox_Factory;
import lehrtools.viewmodel.DP60ViewModel;
import lehrtools.viewmodel.ModelEvent;
import lehrtools.viewmodel.information.Line;
import lehrtools.viewmodel.information.Line_DP60;

public class DP60_Controller {
    private DP60ViewModel _view_model;
    private HBox_Factory _hbox_factory;
    private ObservableList<Line> _steps_list;
    private ObservableList<String> _rule_01_clauses;
    private ObservableList<String> _rule_02_literals;
    private ObservableList<String> _rule_03_variables;
    @FXML
    private BorderPane root_node;
    @FXML
    private ScrollPane results_scroll_pane;
    @FXML
    private VBox vbox_steps;
    @FXML
    private Button button_rule_1;
    @FXML
    private Button button_rule_2;
    @FXML
    private MenuButton menu_rule_1_over;
    @FXML
    private MenuButton menu_rule_2_over;
    @FXML
    private MenuButton menu_rule_3_over;
    @FXML
    private Button button_end;
    @FXML
    Label minimize;
    @FXML
    Label maximize;
    @FXML
    Label fullscreen;
    @FXML
    Label close;
    @FXML
    private AnchorPane additional_info_pane;
    @FXML
    private VBox additional_info;
    @FXML
    private ScrollPane additional_info_scrollpane;
    @FXML
    private VBox additional_info_vbox;
    private boolean is_fullscreen;
    private double previous_width;
    private double previous_heigth;
    private double previous_x_pos;
    private double previous_y_pos;
    private double xOffset = 0.0D;
    private double yOffset = 0.0D;
    private BooleanProperty _redraw;

    public DP60_Controller() {
    }

    public void initialize_components(DP60ViewModel view_model) {
        this._view_model = view_model;
        this._hbox_factory = new HBox_Factory();
        this.initialize_step_list();
        this.initialize_root_node();
        this.initialize_control_buttons();
        this.initialize_menu_buttons();
        this.initialize_calculation_pane();
        this.initialize_redraw_event();
        Bindings.bindContentBidirectional(this._steps_list, this._view_model.lines);
        Bindings.bindContentBidirectional(this._rule_01_clauses, this._view_model.rule_01);
        Bindings.bindContentBidirectional(this._rule_02_literals, this._view_model.rule_02);
        Bindings.bindContentBidirectional(this._rule_03_variables, this._view_model.rule_03);
        this.button_rule_1.visibleProperty().bind(this.menu_rule_1_over.visibleProperty());
        this.button_rule_2.visibleProperty().bind(this.menu_rule_2_over.visibleProperty());
        this.menu_rule_1_over.visibleProperty().bind(this._view_model.rule_1_visible);
        this.menu_rule_2_over.visibleProperty().bind(this._view_model.rule_2_visible);
        this.menu_rule_3_over.visibleProperty().bind(this._view_model.rule_3_visible);
        this.button_end.visibleProperty().bind(this._view_model.end);
        VBox.setVgrow(this.results_scroll_pane, Priority.ALWAYS);
        this.vbox_steps.prefWidthProperty().bind(this.results_scroll_pane.widthProperty().subtract(15));
        this.results_scroll_pane.vvalueProperty().bind(this.vbox_steps.heightProperty());
    }

    public void set_up_window_state(boolean is_fullscreen, double previous_width, double previous_heigth, double previous_x_pos, double previous_y_pos) {
        this.is_fullscreen = is_fullscreen;
        this.previous_width = previous_width;
        this.previous_heigth = previous_heigth;
        this.previous_x_pos = previous_x_pos;
        this.previous_y_pos = previous_y_pos;
        Stage stage = (Stage)this.root_node.getScene().getWindow();
        if (is_fullscreen || stage.isMaximized()) {
            this.root_node.setRight(this.additional_info_pane);
        }

    }

    private void initialize_step_list() {
        List<Line> list = new ArrayList();
        this._steps_list = FXCollections.observableList(list);
        this._steps_list.addListener((ListChangeListener<Line>)change -> {
            if (change.next()) {
                if (change.wasAdded()) {
                    Line line = (Line)this._steps_list.get(this._steps_list.size() - 1);
                    HBox box = this._hbox_factory.get_box(line);
                    this.vbox_steps.getChildren().add(box);
                    if (line instanceof Line_DP60) {
                        VBox column2 = (VBox)box.getChildren().get(1);
                        Label calculation_label = (Label)column2.getChildren().get(1);
                        ScrollPane scrollpane = (ScrollPane)column2.getChildren().get(2);
                        column2.getChildren().remove(2);
                        column2.getChildren().remove(1);
                        this.additional_info_vbox.getChildren().add(calculation_label);
                        VBox calculations = (VBox)scrollpane.getContent();
                        this.addEventHandlersCalculations(calculations);
                        this.additional_info_vbox.getChildren().add(calculations);
                    }
                } else if (change.wasRemoved()) {
                    this.vbox_steps.getChildren().clear();
                    this.additional_info_vbox.getChildren().clear();
                }

            }
        });
    }

    private void initialize_root_node() {
        this.root_node.setOnMousePressed((event) -> {
            this.xOffset = event.getSceneX();
            this.yOffset = event.getSceneY();
        });
        this.root_node.setOnMouseDragged((event) -> {
            Stage stage = (Stage)this.root_node.getScene().getWindow();
            if (!this.is_fullscreen && !stage.isMaximized()) {
                stage.setX(event.getScreenX() - this.xOffset);
                stage.setY(event.getScreenY() - this.yOffset);
            }
        });
    }

    private void initialize_menu_buttons() {
        List<String> list_1 = new ArrayList();
        this._rule_01_clauses = FXCollections.observableList(list_1);
        this._rule_01_clauses.addListener(new ListChangeListener<Object>() {
            public void onChanged(Change change) {
                if (DP60_Controller.this._rule_01_clauses.isEmpty()) {
                    DP60_Controller.this.menu_rule_1_over.getItems().clear();
                } else {
                    DP60_Controller.this.menu_rule_1_over.getItems().clear();
                    Iterator var2 = DP60_Controller.this._rule_01_clauses.iterator();

                    while(var2.hasNext()) {
                        String variable = (String)var2.next();
                        final MenuItem item = new MenuItem();
                        item.setText(variable);
                        item.setOnAction(new EventHandler<ActionEvent>() {
                            public void handle(ActionEvent event) {
                                DP60_Controller.this._view_model.execute(new ModelEvent(this, ModelState.RULE_01_OVER, item.getText()));
                            }
                        });
                        DP60_Controller.this.menu_rule_1_over.getItems().add(item);
                    }
                }

            }
        });
        List<String> list_2 = new ArrayList();
        this._rule_02_literals = FXCollections.observableList(list_2);
        this._rule_02_literals.addListener(new ListChangeListener<Object>() {
            public void onChanged(Change change) {
                if (DP60_Controller.this._rule_02_literals.isEmpty()) {
                    DP60_Controller.this.menu_rule_2_over.getItems().clear();
                } else {
                    DP60_Controller.this.menu_rule_2_over.getItems().clear();
                    Iterator var2 = DP60_Controller.this._rule_02_literals.iterator();

                    while(var2.hasNext()) {
                        String variable = (String)var2.next();
                        final MenuItem item = new MenuItem();
                        item.setText(variable);
                        item.setOnAction(new EventHandler<ActionEvent>() {
                            public void handle(ActionEvent event) {
                                DP60_Controller.this._view_model.execute(new ModelEvent(this, ModelState.RULE_02_OVER, item.getText()));
                            }
                        });
                        DP60_Controller.this.menu_rule_2_over.getItems().add(item);
                    }
                }

            }
        });
        List<String> list_3 = new ArrayList();
        this._rule_03_variables = FXCollections.observableList(list_3);
        this._rule_03_variables.addListener(new ListChangeListener<Object>() {
            public void onChanged(Change change) {
                if (DP60_Controller.this._rule_03_variables.isEmpty()) {
                    DP60_Controller.this.menu_rule_3_over.getItems().clear();
                } else {
                    DP60_Controller.this.menu_rule_3_over.getItems().clear();
                    Iterator var2 = DP60_Controller.this._rule_03_variables.iterator();

                    while(var2.hasNext()) {
                        String variable = (String)var2.next();
                        final MenuItem item = new MenuItem();
                        item.setText(variable);
                        item.setOnAction(new EventHandler<ActionEvent>() {
                            public void handle(ActionEvent event) {
                                DP60_Controller.this._view_model.execute(new ModelEvent(this, ModelState.RULE_03, item.getText()));
                            }
                        });
                        DP60_Controller.this.menu_rule_3_over.getItems().add(item);
                    }
                }

            }
        });
        this.menu_rule_1_over.getItems().clear();
        this.menu_rule_2_over.getItems().clear();
        this.menu_rule_3_over.getItems().clear();
    }

    private void initialize_control_buttons() {
        this.close.setOnMouseClicked((e) -> {
            ((Stage)this.root_node.getScene().getWindow()).close();
        });
        this.minimize.setOnMouseClicked((e) -> {
            ((Stage)this.root_node.getScene().getWindow()).setIconified(true);
        });
        this.maximize.setOnMouseClicked((e) -> {
            this.maximize_event();
        });
        this.fullscreen.setOnMouseClicked((e) -> {
            this.fullscreen_event();
        });
        this.previous_width = 1024.0D;
        this.previous_heigth = 786.0D;
    }

    private void initialize_calculation_pane() {
        VBox.setVgrow(this.additional_info_scrollpane, Priority.ALWAYS);
        this.additional_info_vbox.prefWidthProperty().bind(this.additional_info_scrollpane.widthProperty().subtract(15));
        this.additional_info_vbox.prefHeightProperty().bind(this.additional_info_scrollpane.heightProperty().subtract(2));
        this.additional_info_scrollpane.vvalueProperty().bind(this.additional_info_vbox.heightProperty());
        this.root_node.setRight((Node)null);
    }

    private void initialize_redraw_event() {
        this._redraw = new SimpleBooleanProperty(false);
        this._redraw.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                this.vbox_steps.getChildren().clear();
                this.additional_info_vbox.getChildren().clear();
                Iterator var4 = this._steps_list.iterator();

                while(var4.hasNext()) {
                    Line line = (Line)var4.next();
                    HBox box = this._hbox_factory.get_box(line);
                    this.vbox_steps.getChildren().add(box);
                    if (line instanceof Line_DP60) {
                        VBox column2 = (VBox)box.getChildren().get(1);
                        Label calculation_label = (Label)column2.getChildren().get(1);
                        ScrollPane scrollpane = (ScrollPane)column2.getChildren().get(2);
                        column2.getChildren().remove(2);
                        column2.getChildren().remove(1);
                        this.additional_info_vbox.getChildren().add(calculation_label);
                        VBox calculations = (VBox)scrollpane.getContent();
                        this.addEventHandlersCalculations(calculations);
                        this.additional_info_vbox.getChildren().add(calculations);
                    }
                }
            }

        });
        this._redraw.bindBidirectional(this._view_model._redraw);
    }

    private void maximize_event() {
        Stage stage = (Stage)this.root_node.getScene().getWindow();
        if (this.is_fullscreen) {
            stage.setWidth(this.previous_width);
            stage.setHeight(this.previous_heigth);
            stage.setX(this.previous_x_pos);
            stage.setY(this.previous_y_pos);
            this.is_fullscreen = false;
            this.root_node.setRight((Node)null);
        } else {
            if (!stage.isMaximized()) {
                this.previous_x_pos = stage.getX();
                this.previous_y_pos = stage.getY();
            }

            stage.setMaximized(false);
            Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX(primaryScreenBounds.getMinX());
            stage.setY(primaryScreenBounds.getMinY());
            stage.setWidth(primaryScreenBounds.getWidth());
            stage.setHeight(primaryScreenBounds.getHeight());
            this.is_fullscreen = true;
            this.root_node.setRight(this.additional_info_pane);
        }

    }

    private void fullscreen_event() {
        Stage stage = (Stage)this.root_node.getScene().getWindow();
        if (stage.isMaximized()) {
            stage.setMaximized(false);
            stage.setWidth(this.previous_width);
            stage.setHeight(this.previous_heigth);
            stage.setX(this.previous_x_pos);
            stage.setY(this.previous_y_pos);
            this.root_node.setRight((Node)null);
        } else {
            if (this.is_fullscreen) {
                this.is_fullscreen = false;
            } else {
                this.previous_x_pos = stage.getX();
                this.previous_y_pos = stage.getY();
                this.root_node.setRight(this.additional_info_pane);
            }

            stage.setMaximized(true);
        }

    }

    @FXML
    private void back_EventHandler() {
        this._view_model.execute(new ModelEvent(this, ModelState.BACK, ""));
    }

    @FXML
    private void rule1_EventHandler() {
        this._view_model.execute(new ModelEvent(this, ModelState.RULE_01, ""));
    }

    @FXML
    private void rule2_EventHandler() {
        this._view_model.execute(new ModelEvent(this, ModelState.RULE_02, ""));
    }

    @FXML
    private void rule3_EventHandler() {
        this._view_model.execute(new ModelEvent(this, ModelState.RULE_03, ""));
    }

    @FXML
    private void endEventHandler(Event event) {
        Text_Writer.write_to_file(this._steps_list);

        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/lehrtools/view/Main_Menu.fxml"));
            Parent parent = (Parent)loader.load();
            Main_Menu_Controller controller = (Main_Menu_Controller)loader.getController();
            controller.set_up_window_state(this.is_fullscreen, this.previous_width, this.previous_heigth, this.previous_x_pos, this.previous_y_pos);
            Scene scene = new Scene(parent);
            Stage app_stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            app_stage.hide();
            app_stage.setScene(scene);
            app_stage.show();
        } catch (IOException var7) {
            var7.printStackTrace();
        }

    }

    private void addEventHandlersCalculations(VBox vbox) {
        if (vbox.getChildren().size() > 0) {
            for(int i = 0; i < vbox.getChildren().size(); ++i) {
                FlowPane flowPane = (FlowPane)vbox.getChildren().get(i);
                flowPane.setOnMouseClicked((event) -> {
                    String text = "";

                    Node node;
                    for(Iterator var4 = flowPane.getChildren().iterator(); var4.hasNext(); text = text + this.get_node_text(node)) {
                        node = (Node)var4.next();
                    }

                    this._view_model.execute(new ModelEvent(this, ModelState.SHOW_CALCULATION, text));
                });
            }
        }

    }

    private String get_node_text(Node node) {
        if (node instanceof HBox) {
            Node node1 = (Node)((HBox)node).getChildren().get(0);
            Node node2 = (Node)((HBox)node).getChildren().get(1);
            return ((Label)node1).getText() + ((Label)node2).getText();
        } else {
            return ((Label)node).getText();
        }
    }
}

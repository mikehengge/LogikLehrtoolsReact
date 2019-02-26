package lehrtools.view.components;

import java.util.ArrayList;

import javafx.beans.property.BooleanProperty;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lehrtools.viewmodel.information.*;
import lehrtools.model.state.S_Sub_Calculation;

/**
 * Class that generates Node elements using a Line instances provided by the
 * ViewModel .
 */
public class HBox_Factory {

	
	
	public HBox_Factory()
	{

		
	}

	/**
	 * Returns an HBox containing the information provided by the Line instance
	 * passed as argument.
	 * Depending on the subclass of Line the argument has a different HBox is generated.
	 * The Resulting HBox displays the result of a step for one of the algorithms and is alwas added to the center
	 * nodeofthe GUI.
	 * @param line  Line instance containg information that needs to be displayed in the GUI.
	 * @return an HBox instance for the GUI
	 */
	public HBox get_box(Line line)
	{
		
		if(line instanceof Line_Composite)
				return get_line_composite_box(line);
		else if(line instanceof Line_BD_Resolution)
				return get_bd_line_box(line);
		else if(line instanceof Line_Double)
				return  get_double_box(line);
		else if(line instanceof  Line_Simple)
				return get_simple_box(line);
		else if(line instanceof Line_DP60)
				return  get_dp60_box(line);
		else
			return get_default_box(line);
	}

	/**
	 * Returns a VBox with formatted content representing the calculations
	 * contained in the Line instance. This method  will only generate content for
	 * calculations of type S_Res_Calculation or S_Sub_Calculation, that are result of a resolution or
	 * subsumption step.
	 * It takes a String procedure  determines the wording used to represent the calculation. For the resolution
	 * the word subsumpiert is used for Backward Dual Resolution the wording absorption is used.
	 * @param line Line instance offering the content to be displayed.
	 * @param procedure String
	 * @return a VBox  dsisplaying a list of all calcualtions contained in the line argument.
	 */
	public VBox get_resolution_calculations(Line line, String procedure)
	{
		Line_Double d_line = (Line_Double) line;
		VBox vbox = new VBox();
		for(ArrayList<String> calculation : d_line.calculations)
		{
			if(calculation.size() == 8)
				formatted_calculations_rule_3(calculation,vbox);
			else {
				if(procedure.equals("RES"))
					calculation.set(3," subsumiert die Klausel ");
				formatted_calculations_subsumption(calculation, vbox);
			}
		}

		return vbox;

	}

	/**
	 * Generates a HBox containing a line and two colums.
	 * @param line Line instance offering the content to be displayed.
	 * @return an HBox instance for the GUI
	 */
	private HBox get_default_box(Line line)
	{
		HBox hbox = new HBox();
		hbox.setId("default_hbox");
		Label label = new Label();
		label.setText(line.label);
		label.setId("default_label");
		Label description = new Label(line.step_description);
		description.setId("default_description");
		description.prefWidthProperty().bind(hbox.widthProperty().subtract(200));
		hbox.getChildren().addAll(label, description);
		return hbox;
	}

	/**
	 * Returns a HBox instance usually used to represent the first entry in the GUI.
	 * Mostly used by the DP60 algorithm.
	 * It contains the formula the algorithm started with.
	 * @param line Line instance offering the content to be displayed.
	 * @return an HBox instance for the GUI
	 */
	private HBox get_simple_box(Line line)
	{
		Line_Simple  s_line = (Line_Simple) line;

		HBox hbox = new HBox();

		Label label = new Label(s_line.label);
		label.setId("dp60_label");

		FlowPane formula_box = new FlowPane();
		formula_box.setId("dp60_flowbox");
		populate_flowpane(formula_box,s_line.clauses, s_line.selected);
		HBox.setHgrow(formula_box, Priority.ALWAYS);

		hbox.getChildren().addAll(label,formula_box);
		return hbox;
	}

	/**
	 * Returns an HBox commonly used for the Resolution  algorithm.
	 * It is used to represent the resolution and subsumption steps.
	 * @param line Line instance offering the content to be displayed.
	 * @return an HBox instance for the GUI
	 */
	private HBox get_double_box(Line line)
	{
		Line_Double d_line = (Line_Double) line;
		HBox hbox = new HBox();
		VBox column_1 = new VBox();
		VBox column_2 = new VBox();
		HBox.setHgrow(column_2, Priority.ALWAYS);

		Label double_label_up = new Label(d_line.label);
		double_label_up.setId("double_label_up");

		Label double_label_down =new Label(d_line.formula_name);
		double_label_down.setId("double_label_down");
		column_1.getChildren().addAll(double_label_up, double_label_down);

		Label double_title = new Label(d_line.step_description);
		double_title.setId("double_title");
		FlowPane flow_box = new FlowPane();
		flow_box.setId("double_flowpane_formula");
		populate_flowpane(flow_box,d_line.clauses, d_line.selected, d_line.active);
		column_2.getChildren().addAll(double_title, flow_box);
		hbox.getChildren().addAll(column_1, column_2);

		double_title.prefWidthProperty().bind(column_2.widthProperty());
		return hbox;
	}
	/**
	 * Returns an HBox commonly used for the Backward DualResolution  algorithm.
	 * It is used to represent the resolution and subsumption(absorption) steps.
	 * @param line Line instance offering the content to be displayed.
	 * @return an HBox instance for the GUI
	 */
	private HBox get_bd_line_box(Line line)
	{

		Line_BD_Resolution bd_line = (Line_BD_Resolution) line;

		HBox hbox = new HBox();
		VBox column_1 = new VBox();
		VBox column_2 = new VBox();
		HBox.setHgrow(column_2, Priority.ALWAYS);



		Label label_up   = new Label( bd_line.label);
		label_up.setId("label_up");
		Label label_down = new Label(bd_line.formula_name);
		label_down.setId("label_down");

		column_1.getChildren().addAll(label_up, label_down);

		FlowPane flow_box = new FlowPane();
		flow_box.setId("composite_flowpane");
		Label open_curved_braces = new Label("{") ;
		open_curved_braces.setId("step_label_active");
		flow_box.getChildren().add(open_curved_braces);
		for(String clause: bd_line.clauses)
		{
			Label l_clause = new Label(clause);
			if(bd_line.active.get(bd_line.clauses.indexOf(clause)).get() )
				l_clause.setId("step_label_active");
			else
				l_clause.setId("step_label_inactive");

			flow_box.getChildren().add(l_clause);
			flow_box.getChildren().add(new Label(","));

		}
		if(!bd_line.clauses.isEmpty())
			flow_box.getChildren().remove(flow_box.getChildren().size()-1);
		Label close_curved_braces = new Label("}") ;
		close_curved_braces.setId("step_label_active");
		flow_box.getChildren().add(close_curved_braces);

		Label sub_description = new Label(bd_line.step_description);
		sub_description.setId("bd_sub_details");
		sub_description.prefWidthProperty().bind(column_2.widthProperty());
		//Vertical Box containing Subsumption steps
		VBox vbox_sub = new VBox();
		//Iterate through Subsumption lines
		for(Integer index : bd_line.index)
		{
			HBox sub_container = new HBox();
			Label sub_index_label = new Label("von R_"+index + " absorbierte clauseln");
			sub_index_label.setId("bd_sub_label");

			ArrayList<S_Sub_Calculation> subsumptions = bd_line.subsumptions.get(bd_line.index.indexOf(index));
			FlowPane flow_box_sub = new FlowPane();
			flow_box_sub.setId("bd_sub_flowpane");

			//iterate through calculations and create labels
			for(S_Sub_Calculation subsumption : subsumptions)
			{
				Label sub_label = new Label(subsumption.toString());
				sub_label.setId("bd_sub_flowpane_label");
				flow_box_sub.getChildren().add(sub_label);


			}
			HBox.setHgrow(flow_box_sub, Priority.ALWAYS);
			sub_container.getChildren().addAll(sub_index_label,flow_box_sub);
			vbox_sub.getChildren().add(sub_container);
		}

		column_2.getChildren().addAll(flow_box,sub_description,vbox_sub);

		hbox.getChildren().addAll(column_1, column_2);

		return hbox;

	}


	/**
	 * Returns an HBox commonly used for the DP60  algorithm.
	 * It is used to represent the results of the Unit Clause , Pure Literal and Variable Elimination steps.
	 * This method includes a Vbox in the HBox it generates.
	 * This VBox contains list with calculations included in the Lineinstance.
	 * @param line Line instance offering the content to be displayed.
	 * @return an HBox instance for the GUI
	 */
	private HBox get_dp60_box(Line line) {
		Line_DP60 dp_line = (Line_DP60) line;
		HBox hbox = new HBox();
		VBox column_1 = new VBox();
		VBox column_2 = new VBox();
		HBox.setHgrow(column_2, Priority.ALWAYS);

		Label name = new Label(dp_line.label);
		name.setId("dp60_label");
        Label step = new Label(dp_line.step);
        step.setId("dp60_step");

		column_1.getChildren().addAll(name,step);

		FlowPane formula_box = new FlowPane();
		formula_box.setId("dp60_flowbox");
		populate_flowpane(formula_box,dp_line.clauses, dp_line.selected);

		Label step_description = new Label(name.getText()+ " : " + step.getText());
		step_description.setId("dp60_sub_details");
		step_description.prefWidthProperty().bind(column_2.widthProperty());

		ScrollPane calculations_scrollpane = new ScrollPane();
		calculations_scrollpane.setId("dp60_scrollpane");
		VBox calculations_vbox = new VBox();
		calculations_vbox.setId("dp60_vbox");
		for(ArrayList<String> calculation : dp_line.calculations_info)
		{
		    if(dp_line.label.equals("Regel 3"))
			    formatted_calculations_rule_3(calculation, calculations_vbox);
		    else
                formatted_calculations_rule_2(calculation, calculations_vbox);

		}
		if(dp_line.calculations_info.isEmpty())
			calculations_vbox.getChildren().add(new Label("Es wurden keine berrechnungen durchgeführt"));
		calculations_scrollpane.setContent(calculations_vbox);
		column_2.getChildren().addAll(formula_box,step_description,calculations_scrollpane);
		if(calculations_vbox.getChildren().size()<6) {
			calculations_scrollpane.setMinHeight(25*calculations_vbox.getChildren().size());
			calculations_scrollpane.setPrefHeight(25*calculations_vbox.getChildren().size());
			calculations_scrollpane.setMaxHeight(25*calculations_vbox.getChildren().size());
		}
		else {
			calculations_scrollpane.setMinHeight(120);
			calculations_scrollpane.setPrefHeight(120);
			calculations_scrollpane.setMaxHeight(120);
		}

		hbox.getChildren().addAll(column_1,column_2);

		return hbox;
	}

	/**
	 * Returns an HBox used exclusively in the REsolution algorithm.
	 * This box is the one containing all the steps of an iteration of
	 * the REsolution algorithm.
	 * @param line Line instance offering the content to be displayed.
	 * @return an HBox instance for the GUI
	 */
	private HBox get_line_composite_box(Line line)
	{
		Line_Composite c_line = (Line_Composite) line;
		HBox hbox = new HBox();

		VBox column_1 = new VBox();
		VBox column_2 = new VBox();
		HBox.setHgrow(column_2, Priority.ALWAYS);

		Label name = new Label(c_line.label);
		name.setWrapText(true);
		name.setId("composite_label");
		Label f_name = new Label(c_line.step_description);
		f_name.setId("composite_label");
		column_1.getChildren().addAll(name, f_name);

		FlowPane formula_box = new FlowPane();
		formula_box.setId("composite_flowpane");

		populate_flowpane(formula_box,c_line.clauses, c_line.clause_selected);

		column_2.getChildren().add(formula_box);


		hbox.getChildren().addAll(column_1, column_2);
		return hbox;


	}

	/**
	 * Formats how the formula is displayed in an Hbox.
	 * @param box The container Node for the formula
	 * @param clauses The string representation of the Clauses in the formula.
	 * @param selected The state of each clause, decides if the clause is displayed in ad
	 *                 "highlighted" state.
	 */
	private void populate_flowpane(FlowPane box , ArrayList<String> clauses, ArrayList<BooleanProperty> selected)
	{

		Label open_curved_braces = new Label("{") ;
		open_curved_braces.setId("step_label_active");
		Label close_curved_braces = new Label("}") ;
		close_curved_braces.setId("step_label_active");
		box.getChildren().add(open_curved_braces);
		for(String clause: clauses)
		{
			Label l_clause = new Label(clause);
			if(selected.get(clauses.indexOf(clause)).get())
				l_clause.setId("step_label_active_selected");
			else
				l_clause.setId("step_label_active");
			box.getChildren().add(l_clause);
			box.getChildren().add(new Label(","));
		}
		if(!clauses.isEmpty())
			box.getChildren().remove(box.getChildren().size()-1);
		box.getChildren().add(close_curved_braces);
	}
	/**
	 * Formats how the formula is displayed in an Hbox.
	 * Used exclusively by get_double_box(Line) method.
	 * @param box The container Node for the formula
	 * @param clauses The string representation of the Clauses in the formula.
	 * @param selected The state of each clause, decides if the clause is displayed in ad
	 *                 "highlighted" state.
	 */
	private void populate_flowpane(FlowPane box , ArrayList<String> clauses,
								   ArrayList<BooleanProperty> selected,
								   ArrayList<BooleanProperty> active)
	{

		Label open_curved_braces = new Label("{") ;
		open_curved_braces.setId("step_label_active");
		Label close_curved_braces = new Label("}") ;
		close_curved_braces.setId("step_label_active");
		box.getChildren().add(open_curved_braces);
		for(String clause: clauses)
		{
			Label l_clause = new Label(clause);
			if(selected.get(clauses.indexOf(clause)).get())
				l_clause.setId("step_label_active_selected");
			else if( active.get(clauses.indexOf(clause)).get())
				l_clause.setId("step_label_active");
			else
				l_clause.setId("step_label_inactive");

			box.getChildren().add(l_clause);
			box.getChildren().add(new Label(","));
		}
		if(!clauses.isEmpty())
			box.getChildren().remove(box.getChildren().size()-1);
		box.getChildren().add(close_curved_braces);
	}

	/**
	 * Formats the string representation for a resolution calculation into a FlowPane.
	 * The representation is delivered partitioned and each part is formatted
	 * accordingly.
	 * @param calculation  string representation of a calculation.
	 * @param vbox VBox  instance  that will host the resulting FlowPane.
	 */
	private void formatted_calculations_rule_3(ArrayList<String> calculation, VBox vbox)
	{
		FlowPane box = new FlowPane();
		box.setId("calculation_label");
		// prefix
		Label label = new Label(calculation.get(0));
        label.setId("calc_prefix");
		HBox clause_box = new HBox();
		clause_box.getChildren().add(label);
		//clause
		label = new Label(calculation.get(1));
		clause_box.getChildren().add(label);
		box.getChildren().add(clause_box);
		//text
		label = new Label(calculation.get(2));
        label.setId("calc_text");
		box.getChildren().add(label);
		//prefix
		label = new Label(calculation.get(3));
        label.setId("calc_prefix");
		clause_box = new HBox();
		clause_box.getChildren().add(label);
		// Clause
		label = new Label(calculation.get(4));
		clause_box.getChildren().add(label);
		box.getChildren().add(clause_box);
		//text
		label = new Label(calculation.get(5));
        label.setId("calc_text");
		box.getChildren().add(label);
		//prefix
		label = new Label(calculation.get(6));
        label.setId("calc_prefix");
		clause_box = new HBox();
		clause_box.getChildren().add(label);
		//clause
		label = new Label(calculation.get(7));
		clause_box.getChildren().add(label);
		box.getChildren().add(clause_box);

		vbox.getChildren().add(box);

	}

	/**
	 * Formats the string representation for a Pure Literal or Unit Clause claculation into a FlowPane.
	 * The representation is delivered partitioned and each part is formatted
	 * accordingly.
	 * @param calculation  string representation of a calculation.
	 * @param vbox VBox  instance  that will host the resulting FlowPane.
	 */
    private void formatted_calculations_rule_2(ArrayList<String> calculation, VBox vbox)
    {
        FlowPane box = new FlowPane();
        box.setId("calculation_label");


        if (calculation.size() == 5 )
        {
            //text
            Label  label = new Label(calculation.get(0));
            label.setId("calc_text");
            box.getChildren().add(label);
            //clause
            label = new Label(calculation.get(1));
            box.getChildren().add(label);
            //text
            label = new Label(calculation.get(2));
            label.setId("calc_text");
            box.getChildren().add(label);
            //prefix
            label = new Label(calculation.get(3));
            label.setId("calc_prefix");
			HBox clause_box = new HBox();
			clause_box.getChildren().add(label);
            //clause
            label = new Label(calculation.get(4));
			clause_box.getChildren().add(label);
            box.getChildren().add(clause_box);

        }
        else
        {
            //text
            Label  label = new Label(calculation.get(0));
            label.setId("calc_text");
            box.getChildren().add(label);
            //literal
            label = new Label(calculation.get(1));
            box.getChildren().add(label);
            //text
            label = new Label(calculation.get(2));
            label.setId("calc_text");
            box.getChildren().add(label);
            //literal
            label = new Label(calculation.get(3));
            box.getChildren().add(label);
            //text
            label = new Label(calculation.get(4));
            box.getChildren().add(label);
            label.setId("calc_text");
            //prefix
            label = new Label(calculation.get(5));
            label.setId("calc_prefix");
			HBox clause_box = new HBox();
			clause_box.getChildren().add(label);
            //clause
            label = new Label(calculation.get(6));
			clause_box.getChildren().add(label);
            box.getChildren().add(clause_box);
        }



        vbox.getChildren().add(box);


    }
	/**
	 * Formats the string representation for a subsumption calculation into a FlowPane.
	 * The representation is delivered partitioned and each part is formatted
	 * accordingly.
	 * @param calculation  string representation of a calculation.
	 * @param vbox VBox  instance  that will host the resulting FlowPane.
	 */
	private void formatted_calculations_subsumption(ArrayList<String> calculation , VBox vbox)
	{

		FlowPane box = new FlowPane();
		box.setId("calculation_label");
		//text
		Label  label = new Label(calculation.get(0));
		label.setId("calc_text");
		box.getChildren().add(label);
		//prefix
		label = new Label(calculation.get(1));
		label.setId("calc_prefix");
		box.getChildren().add(label);
		//clause
		label = new Label(calculation.get(2));
		box.getChildren().add(label);
		//text
		label = new Label(calculation.get(3));
		label.setId("calc_text");
		box.getChildren().add(label);
		//prefix
		label = new Label(calculation.get(4));
		label.setId("calc_prefix");
		box.getChildren().add(label);
		//clause
		label = new Label(calculation.get(5));
		box.getChildren().add(label);

		vbox.getChildren().add(box);
	}


}

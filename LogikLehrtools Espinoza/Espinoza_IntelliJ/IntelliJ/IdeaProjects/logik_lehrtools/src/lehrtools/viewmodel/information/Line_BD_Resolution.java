package lehrtools.viewmodel.information;

import java.util.ArrayList;
import java.util.LinkedList;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import lehrtools.model.state.S_Calculation;
import lehrtools.model.state.S_Sub_Calculation;

/**
 * @deprecated
 */
public class Line_BD_Resolution extends Line_Double {
	
	public ArrayList<BooleanProperty> active;
	public ArrayList<Integer> index ;
	public ArrayList<ArrayList<S_Sub_Calculation>> subsumptions;
	public ArrayList<ArrayList<BooleanProperty>> subsumptions_active;
	
	public Line_BD_Resolution( String label , 
							   String step_description,
							   String formula_name,
							   ArrayList<String> clauses,
							   LinkedList<S_Calculation> resolvents
							   )
	{
		super(label, step_description, formula_name, clauses, resolvents);
		this.index =new ArrayList<>();
		active = new ArrayList<>();
		for(int i = 0; i < clauses.size();i++)
			active.add(new SimpleBooleanProperty(true));
		this.subsumptions =  new ArrayList<>();
		subsumptions_active = new ArrayList<>();
		
	}
							   

}

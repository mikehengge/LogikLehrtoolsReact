package lehrtools.model.state;

import lehrtools.formula.Variable;
import lehrtools.model.Formula_NF;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * Takes a snapshot of the State of all variables relevant to the 
 * Backward Dual Resolution procedure, so that it can be restored to that specific state at a later
 * point in time.
 */
public class BDLog {
	public final LinkedList<Step> steps;
	public final ArrayList<Formula_NF> formula;
	public final Formula_NF resolventen;
	public final Formula_NF subsumed_resolvents;
	public final Formula_NF subsumed_formula;
	public final ModelState state;
	public final boolean fw;
	public final boolean bw;
	public final int index;


	public BDLog(LinkedList<Step> _steps,
				 ArrayList<Formula_NF> _formula,
                 Formula_NF _resolventen,
                 Formula_NF _subsumed_resolvents,
                 Formula_NF _subsumed_formula,
                 ModelState _state,
                 boolean _fw,
                 boolean _bw,
                 int _index
						 )
	{
		steps = (LinkedList<Step>) _steps.clone();
		formula = (ArrayList<Formula_NF>) _formula.clone();
		resolventen = new Formula_NF(_resolventen);
		subsumed_resolvents = new Formula_NF(_subsumed_resolvents);
		subsumed_formula = new Formula_NF(_subsumed_formula);
		state = _state;
		fw = _fw;
		bw = _bw;
		index = _index;
	}
	
	

}

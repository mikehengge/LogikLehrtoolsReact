package lehrtools.model.state;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import lehrtools.formula.Formula;
import lehrtools.formula.Variable;
import lehrtools.model.Formula_NF;
import lehrtools.model.state.ModelState;
import lehrtools.model.state.Step;
/**
 * Takes a snapshot of the State of all variables relevant to the 
 * Resolution procedure, so that it can be restored to that specific state at a later
 * point in time.
 */
public class ResolutionLog {
	public final LinkedList<Step> steps;
	public final int proof;
	public final Formula_NF formula;
	public final Formula_NF resolventen;
	public final Formula_NF resolventen_temp;
	public final Formula_NF subsumed_resolvents;
	public final Formula_NF subsumed_formula;
	public final ModelState state;
	public final Set<Variable> formula_vars;
	public final boolean fw;
	public final boolean bw;
	public final int index;
	
	@SuppressWarnings("unchecked")
	public ResolutionLog(LinkedList<Step> _steps,
						 int _proof,
						 Formula_NF _formula,
						 Formula_NF _resolventen,
						 Formula_NF _resolventen_temp,
						 Formula_NF _subsumed_resolvents,
						 Formula_NF _subsumed_formula,
						 ModelState _state,
						 Set<Variable> _formula_vars,
						 boolean _fw,
						 boolean _bw,
						 int _index
						 )
	{
		steps = (LinkedList<Step>) _steps.clone();
		proof = _proof;
		formula = new Formula_NF(_formula);
		resolventen = new Formula_NF(_resolventen);
		resolventen_temp = new Formula_NF(_resolventen_temp);
		subsumed_resolvents = new Formula_NF(_subsumed_resolvents);
		subsumed_formula = new Formula_NF(_subsumed_formula);
		state = _state;
		formula_vars = (Set<Variable>) ((HashSet<Variable>)_formula_vars).clone();
		fw = _fw;
		bw = _bw;
		index = _index;
	}
	
	

}

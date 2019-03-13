package lehrtools.model;

import java.lang.reflect.Array;
import java.util.*;

import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;

import lehrtools.formula.Variable;
import lehrtools.miscellanious.Text_Writer;
import lehrtools.model.state.*;
import lehrtools.viewmodel.ModelEvent;
import lehrtools.viewmodel.Observer;
import lehrtools.viewmodel.Subject;

/**
 * This class implements the the Resolution algorithm to test if a formula in
 * CNF is satisfiable. It Uses for this the resolution rule and subsumption rule
 * implemented in the Resolution Utility class for this. This class represnts
 * the model in a MVVM pattern, so it interacts with the viewmodel through an
 * observer pattern. This is implement by extending the abstract class subject.
 * Through this the class offers following methods to interact with an observer:
 * attach(observer), execute(Event Object) and getState().
 *
 *
 */
public class Resolution extends Subject {

	/**
	 * List containing all the calculation in order of execution.
	 */
	private LinkedList<Step> _steps = new LinkedList<>();
	/**
	 * at the end of the calculations it will describe if the formula is
	 * satisfiable(1) or unsatisfiable(0), during calculations satisfiability is
	 * undetermined(-1)
	 */
	private int _proof;
	/**
	 * Represent the actual formula the Resolution algorithm is working on.
	 */
	private Formula_NF _formula;
	/**
	 * Represent the actual clause_1 from the actual formula.
	 */
	private Formula_NF _resolventen;

	private Formula_NF _resolventen_temp;
	/**
	 * Represent the actual clause_1 after being subsumed with the actual formula.
	 */
	private Formula_NF _subsumed_resolvents;
	/**
	 * Represent the actual formula after being subsumed with the actual resolvents.
	 */
	private Formula_NF _subsumed_formula;

	/**
	 * Class that contains the information needed by the viewmodel
	 */
	private ModelState _state;
	/**
	 * Only used when a step back is executed. Used to tell the ResolutionViewModel
	 * the state Resolution is in, since the field _state is used to tell the
	 * ResolutionViewModel that step back was executed.
	 */
	private ModelState _back_step_state;
	/**
	 * Contains Vars(Formula) needed for the resolution over a variable
	 */
	private Set<Variable> _formula_vars;
	/**
	 * Flag that states if forward subsumption was already executed.
	 */
	private boolean _fw;
	/**
	 * Flag that states if backward subsumption was already executed.
	 */
	private boolean _bw;

	/**
	 * Variable that keeps count of the number of iterations;
	 */
	private int _index;
	/**
	 * Keeps an ordered list of the states the algorithm went through. Its used to
	 * set the state of the algorithm back to a previous one.
	 */
	private LinkedList<ResolutionLog> _logger;
	
	private ArrayList<Variable> _heuristik;
	private boolean _subsumption_done = false;

	/**
	 * Initializes all variables needed for the calculation. It takes a formula that
	 * is going to be tested for satisfiability. The formula needs to be in a normal
	 * form.
	 *
	 * @param formula : formula to be tested in CFN.
	 */
	public Resolution(Formula_NF formula) {

		_steps.add(new Step_Start(ModelState.START, 0, formula));
		_formula = new Formula_NF(formula);
		_formula_vars = _formula.vars();
		_resolventen = new Formula_NF(formula);
		_resolventen_temp = new Formula_NF();
		_subsumed_resolvents = new Formula_NF();
		_subsumed_formula = new Formula_NF();
		_proof = -1;
		_state = ModelState.RESOLUTION;
		_back_step_state = ModelState.RESOLUTION;
		_fw = false;
		_bw = false;
		_index = 0;
		_logger = new LinkedList<>();
		generate_log();
		
		ArrayList<Variable> heuristik = new ArrayList<Variable>();
		heuristik.addAll(_formula_vars);
		Resolution noHeuristik = new Resolution(_formula, heuristik);
		_proof = noHeuristik._proof;
	}

	/**
	 * Initializes all variables needed for the calculation. It takes a formula that
	 * is going to be tested for satisfiability. The formula needs to be in a normal
	 * form.
	 *
	 * @param formula : formula to be tested in CFN.
	 * @param         heuristik: Heuristik for resolving order
	 */
	public Resolution(Formula_NF formula, ArrayList<Variable> heuristik) {
		_steps.add(new Step_Start(ModelState.START, 0, formula));
		_formula = new Formula_NF(formula);
		_formula_vars = _formula.vars();
		_resolventen = new Formula_NF(formula);
		_resolventen_temp = new Formula_NF();
		_subsumed_resolvents = new Formula_NF();
		_subsumed_formula = new Formula_NF();
		_proof = -1;
		_state = ModelState.RESOLUTION;
		_back_step_state = ModelState.RESOLUTION;
		_fw = false;
		_bw = false;
		_index = 0;
		_logger = new LinkedList<>();
		
		_heuristik = heuristik;
		generate_log();
		ArrayList<Variable> test = (ArrayList<Variable>) _heuristik.clone();
		execute_resolution(test);
	}

	/**
	 * Getter for the LinkedList _steps
	 * 
	 * @return LinkedList &lt;Formula_NF&gt;.
	 */
	public LinkedList<Step> steps() {
		return _steps;
	}

	/**
	 * Getter for the actual state of the resolution
	 * 
	 * @return ModelState _state
	 */
	public ModelState state() {
		return _state;
	}

	/**
	 * Getter for the integer proof
	 * 
	 * @return integer proof
	 */
	public int proof() {
		return _proof;
	}

	/**
	 * Getter for the actual formula the resolution is applied on
	 * 
	 * @return FormulaNF
	 */
	public Formula_NF formula() {
		return _formula;
	}

	/**
	 * Getter for the set of resolvents derived from formula
	 * 
	 * @return Formula_NF
	 */
	public Formula_NF resolventen() {
		return _resolventen;
	}

	/**
	 * Getter for the formula representing the subsumed resolvents the resolution is
	 * applied on
	 * 
	 * @return FormulaNF
	 */
	public Formula_NF subsumed_resolvents() {
		return _subsumed_resolvents;
	}

	/**
	 * Getter for the formula representing the subsumed formula the resolution is
	 * applied on
	 * 
	 * @return FormulaNF
	 */
	public Formula_NF subsumed_formula() {
		return _subsumed_formula;
	}

	/**
	 * Getter for the set of variables of the formula the is being resolved over.
	 * 
	 * @return returns a Set of type Variable.
	 */
	public Set<Variable> formula_vars() {
		return _formula_vars;
	}

	/**
	 * Getter for the execution state of forward_subsumption
	 * 
	 * @return boolean
	 */
	public boolean fw_subsumption() {
		return _fw;
	}

	/**
	 * Getter for the execution state of backward_subsumption
	 * 
	 * @return boolean
	 */
	public boolean bw_subsumption() {
		return _bw;
	}

	/**
	 * Counter for the iterations of the Algorithm
	 * 
	 * @return integer
	 */
	public int index() {
		return _index;
	}

	/**
	 * Main method of the resolution class that manages the execution of the
	 * different steps of the resolution procedure. This method is used by the
	 * viewmodel to pass events triggered by the view. It takes an EventObject, a
	 * standard java class, that has been extended to carry the needed information,
	 * so it will needed to be casted to the expected event. Each event will carry
	 * information about a state which will trigger the corresponding calculations.
	 *
	 * @param event: class carrying information to continue the calculation.
	 */
	public void execute(EventObject event) {
		// execute_resolution(heuristik);
	}

	/**
	 * Calculates all resolvents to the actual formula and stores then in
	 * _resolventen and _steps. If an empty clause is found amongst the resolvents
	 * then the calculations are ended by setting the state to END and the flag for
	 * satisfiability will be set to 1 , signaling the formula is satisfiable. An
	 * update event to the observer will be sent after there each state change.
	 * Should the state of the calculation be inconsistent with the event that
	 * triggered the calculation nothing will be done.
	 */
	private void execute_resolution(ArrayList<Variable> heuristik) {
		if (_state != ModelState.RESOLUTION /* && _state != ModelState.RESOLUTION_OVER */)
			return;

		LinkedList<S_Calculation> res_steps = new LinkedList<>();
		if (heuristik.isEmpty()) {
			for (Variable variable : _formula_vars) {
				_resolventen_temp = _resolventen_temp
						.union_with(ResolutionUtility.resolution_over(_formula, _resolventen, variable, res_steps));
				if (_index != 0)
					_resolventen_temp = _resolventen_temp.union_with(
							ResolutionUtility.resolution_over(_resolventen, _resolventen, variable, res_steps));
			}

			_steps.add(new Step_Resolution(ModelState.RESOLUTION, _index, res_steps, _resolventen_temp, _formula));
			_formula_vars.clear();
			_state = ModelState.SUBSUMPTION;
			_formula = _formula.union_with(_resolventen);
			_formula.remove_duplicate_clauses();
			_resolventen = _resolventen_temp;
			_resolventen.remove_duplicate_clauses();
			_subsumed_resolvents = _resolventen;
			_resolventen_temp = new Formula_NF();

			update();
			if (_resolventen.contains_empty_clause()) {
				_state = ModelState.END;
				_proof = 0;
				_steps.add(new Step_End(_state, _index, _proof, _resolventen.get_empty_clause()));
				update();
				System.out.println(_index + ":End Resolution: empty clause in resolvents");
			}
			if (_resolventen.is_empty()) {
				_state = ModelState.END;
				_proof = 1;
				_steps.add(new Step_End(_state, _index, _proof, null));
				update();
				System.out.println(_index + ":End Resolution: formula has no resolvents");
			}
			System.out.println(_index + ":Non-tautological resolvents: " + _resolventen);

			generate_log();
		} else {
			System.out.println(_index + ":Formula clauses as NNF: " + _formula);
			for (Variable variable : heuristik) {
				_resolventen_temp = _resolventen_temp
						.union_with(ResolutionUtility.resolution_over(_formula, _resolventen, variable, res_steps));
				System.out.println(_index + ":Resolvents with variable "+variable+": " +ResolutionUtility.resolution_over(_formula, _resolventen, variable, res_steps));
				if (_index != 0)
					_resolventen_temp = _resolventen_temp.union_with(
							ResolutionUtility.resolution_over(_resolventen, _resolventen, variable, res_steps));
			}
			System.out.println(_index + ":Non-tautological resolvents: " + _resolventen_temp);


			_steps.add(new Step_Resolution(ModelState.RESOLUTION, _index, res_steps, _resolventen_temp, _formula));
			_formula_vars.clear();
			_state = ModelState.SUBSUMPTION;
			_formula = _formula.union_with(_resolventen);
			_formula.remove_duplicate_clauses();
			_resolventen = _resolventen_temp;
			_resolventen.remove_duplicate_clauses();
			_subsumed_resolvents = _resolventen;
			_resolventen_temp = new Formula_NF();

			update();
			if (_resolventen.contains_empty_clause()) {
				_state = ModelState.END;
				_proof = 0;
				_steps.add(new Step_End(_state, _index, _proof, _resolventen.get_empty_clause()));
				update();
				System.out.println(_index + ":End Resolution: empty clause in resolvents");
			}
			if (_resolventen.is_empty()) {
				_state = ModelState.END;
				_proof = 1;
				_steps.add(new Step_End(_state, _index, _proof, null));
				update();
				System.out.println(_index + ":End Resolution: formula has no resolvents");

			}
			//generate_log();

		}
		if(!_resolventen.is_empty()) execute_forward_subsumption();

	}

	/**
	 * Executes the forward subsumption step. Eliminates all clauses from the field
	 * _resolventen that are subsumed by clauses in the field _formula. If the
	 * resulting formula is empty then the algorithm ends. If the backward
	 * resolution step was executed then prepares the state of teh class for a
	 * resolution step.
	 */
	@SuppressWarnings("Duplicates")
	private void execute_forward_subsumption() {
		_subsumption_done = true;
		if (_fw || _state != ModelState.SUBSUMPTION)
			return;
		LinkedList<S_Calculation> sub_steps = new LinkedList<>();
		_subsumed_resolvents = ResolutionUtility.subsumption(_resolventen, _formula, sub_steps);
		_fw = true;
		_steps.add(new Step_Subsumption(ModelState.SUBSUMPTION_FW, _index, sub_steps, _formula, _resolventen,
				_subsumed_resolvents));
		_resolventen = _subsumed_resolvents;
		System.out.println(_index + ":Forward Subsumption - resolvents after deletion of subsumed clauses: " + _resolventen);
		update();
		if (_subsumed_resolvents.is_empty()) {
			_state = ModelState.END;
			_proof = 1;
			_steps.add(new Step_End(_state, _index, _proof, null));
			update();
			System.out.println(_index + ":End Resolution: formula has no resolvents");
			return;
//		} else if (_bw) {
//			_formula = _subsumed_formula;
//			_formula_vars = _formula.vars();
//			_formula_vars.addAll(_subsumed_resolvents.vars());
//			_state = ModelState.RESOLUTION;
//			_index++;
//			_steps.add(new Step_Start(ModelState.START, _index, _formula.union_with(_resolventen)));
//			_fw = false;
//			_bw = false;
//			update();
		}
		generate_log();
		if (!_resolventen.is_empty()) execute_backward_subsumption();
	}

	/**
	 * Executes the backward subsumption step. Eliminates all clauses from the field
	 * _formula that are subsumed by clauses in the field _resolventen. If the
	 * backward resolution step was executed then prepares the state of teh class
	 * for a resolution step.
	 */
	@SuppressWarnings("Duplicates")
	private void execute_backward_subsumption() {
		if (_bw || _state != ModelState.SUBSUMPTION)
			return;
		LinkedList<S_Calculation> sub_steps = new LinkedList<>();
		_subsumed_formula = ResolutionUtility.subsumption(_formula, _subsumed_resolvents, sub_steps);
		_steps.add(new Step_Subsumption(ModelState.SUBSUMPTION_BW, _index, sub_steps, _resolventen, _formula,
				_subsumed_formula));
		_formula = _subsumed_formula;
		_bw = true;
		update();
		System.out.println(_index + ":Backward Subsumption - resolvents after deletion of subsumed clauses: " + _resolventen);
		if (_fw) {
			_formula_vars = _formula.vars();
			_formula_vars.addAll(_subsumed_resolvents.vars());
			_resolventen = _subsumed_resolvents;
			_state = ModelState.RESOLUTION;
			_index++;
			_steps.add(new Step_Start(ModelState.START, _index, _formula.union_with(_resolventen)));
			_fw = false;
			_bw = false;
			update();
		}
		generate_log();

		if (!_resolventen.is_empty()) execute_resolution(_heuristik);
		else {
			System.out.println(_index + ":End Resolution: formula has no resolvents");
		}
	}

	/**
	 * Sets the state of the algorithm one step back.
	 */
	@SuppressWarnings("unchecked")
	private void execute_step_back() {
		if (_logger.size() == 1)
			return;
		_logger.removeLast();
		ResolutionLog log = _logger.getLast();
		_steps = (LinkedList<Step>) log.steps.clone();
		_proof = log.proof;
		_formula = new Formula_NF(log.formula);
		_resolventen = new Formula_NF(log.resolventen);
		_resolventen_temp = new Formula_NF(log.resolventen_temp);
		_subsumed_resolvents = new Formula_NF(log.subsumed_resolvents);
		_subsumed_formula = new Formula_NF(log.subsumed_formula);
		_formula_vars = (Set<Variable>) ((HashSet<Variable>) log.formula_vars).clone();
		_fw = log.fw;
		_bw = log.bw;
		_index = log.index;
		_state = ModelState.BACK;
		_back_step_state = log.state;
		update();
		_state = log.state;

	}

	@Override
	public ResolutionState getState() {
		return new ResolutionState(_steps, _state, _back_step_state, _formula_vars, _proof, _fw, _bw);
	}

	@Override
	public void attach(Observer observer) {
		_observer = observer;
		update();
	}

	/**
	 * Triggers an update of the actual state of the class to the
	 * ResolutionViewModel.
	 */
	private void update() {
		if (_observer != null)
			_observer.update();
	}

	private void generate_log() {
		ResolutionLog log = new ResolutionLog(_steps, _proof, _formula, _resolventen, _resolventen_temp,
				_subsumed_resolvents, _subsumed_formula, _state, _formula_vars, _fw, _bw, _index);
		_logger.add(log);
	}

}

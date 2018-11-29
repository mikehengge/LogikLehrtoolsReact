package lehrtools.model;

import java.util.*;

import lehrtools.formula.Variable;
import lehrtools.model.state.*;
import lehrtools.viewmodel.ModelEvent;
import lehrtools.viewmodel.Observer;
import lehrtools.viewmodel.Subject;

/**
 * This class implements the the Backward Dual Resolution algorithm to calculate the prime implicants
 * of a formula in DNF.It Uses for this the resolution rule and subsumption rule implemented
 * in the Resolution Utility class for this.
 * This class represnts the model in a MVVM pattern, so it interacts with the viewmodel
 * through an observer pattern. This is implement by extending the abstract class subject.
 * Through this the class offers following methods to interact with an observer: attach(observer),
 * execute(Event Object) and getState().
 *
 *
 */
public class BDResolution extends Subject{
	/**
	 * Store each Step instance resulting of executing a step of the  backward Dual Resolution
	 * algorithm.
	 */
	private LinkedList<Step> _steps;
	/**
	 * Describes the state the algorithm.
	 */
	private ModelState _state;
	/**
	 * Only used when a step back is executed. Used to tell the
	 * BDResolutionViewModel the state BDResolution is in, since the field _state is used
	 * to tell the BDResolutionViewModel that step back was executed.
	 */
	private ModelState _back_step_state;
	/**
	 * Field that ue to determine if the forward subsumption step was already executed.
	 */
	private boolean _fw;
	/**
	 * Field that ue to determine if the backward subsumption step was already executed.
	 */
	private boolean _bw;
	/**
	 * Value representing the iteration number the algorithm.
	 */
	private int _index;

	/**
	 * Represent the actual formula the Resolution algorithm is working on. 
	 */
	private ArrayList<Formula_NF> _formula;
	/**
	 * Represent the actual clause_1 from the actual formula.
	 */
	private Formula_NF _resolventen;
	/**
	 * Represent the actual clause_1 after being subsumed with the actual formula.
	 */
	private Formula_NF _subsumed_resolvents;
	/**
	 * Represent the actual formula after being subsumed with the actual resolvents. 
	 */
	private Formula_NF _subsumed_formula;
	/**
	 * Keeps an ordered list of the states the algorithm went through. Its used to set
	 * the state of the algorithm back to a previous one.
	 */
	private LinkedList<BDLog> _logger;

	/**
	 * Constructor
	 * @param formula formula for which the prime implicants are to be calculated.
	 */
	public BDResolution(Formula_NF formula)
	{
		_steps = new LinkedList<>();
		_state = ModelState.RESOLUTION;
		_back_step_state = ModelState.RESOLUTION;
		_fw = false;
		_bw = false;
		_formula = new ArrayList<>();
		_formula.add(formula);
		_resolventen =new Formula_NF();
		_subsumed_resolvents = new Formula_NF();
		_subsumed_formula = new Formula_NF();
		_index = 0;
		_steps.add(new Step_Resolution(ModelState.START ,0,new LinkedList<>(),_formula.get(0), new Formula_NF()));
		_logger = new LinkedList<>();
		generate_log();

	}

	/**
	 * Getter for the field _steps
	 * @return  LinkedList<Step>
	 */
	public LinkedList<Step> steps(){return _steps;}

	/**
	 * Getter for the field _state
	 * @return ModelState
	 */
	public ModelState state() { return _state;}
	
	/**
	 * Getter for the actual formula the resolution is applied on 
	 * @return FormulaNF 
	 */
	public ArrayList<Formula_NF> formula() {return _formula;}
	/**
	 * Getter for the set of resolvents derived from formula
	 * @return Formula_NF 
	 */
	public Formula_NF resolventen() {return _resolventen;}
	/**
	 * Getter for the formula representing the subsumed resolvents the resolution is applied on 
	 * @return FormulaNF 
	 */
	public Formula_NF subsumed_resolvents() {return _subsumed_resolvents;}
	/**
	 * Getter for the formula representing the subsumed formula the resolution is applied on 
	 * @return FormulaNF 
	 */
	public Formula_NF subsumed_formula() { return _subsumed_formula;}

	/**
	 * Getter for the field _fw
	 * @return boolean
	 */
	public boolean fw_subsumption() { return _fw;}
	/**
	 * Getter for the field _bw
	 * @return boolean
	 */
	public boolean bw_subsumption() {return _bw;}

	/**
	 * Getter for the field _index
	 * @return int
	 */
	public int index() {return _index;}

	/**
	 * Getter for the field _observer
	 * @return Observer
	 */
	public Observer observer() { return _observer;}
	
	@Override
	public void attach(Observer observer) {
		_observer = observer;
		update();
		
	}
	
	@Override
	public ResolutionState getState() {
		return new ResolutionState(_steps,
				 				   _state,
				 				   _back_step_state,
				 				   null,
				 				   0,
				 				   _fw,
				 				   _bw
				 				  );
	}

	@Override
	public void execute(EventObject event) {

		ModelEvent _event = (ModelEvent)event;
		
		if(_state == ModelState.END &&  _event.state()!=ModelState.BACK) return;
		
		switch(_event.state())
		{
			case RESOLUTION : execute_resolution();
							  break;
									
			case SUBSUMPTION_FW :   execute_forward_subsumption();
									break;
				
			case SUBSUMPTION_BW :   execute_backward_subsumption();
									break;
			case BACK: execute_step_back();
						break;
			default:
		}
		
	}

	/**
	 * Executes a resolution step. This steps calculate all new resolvents
	 * by making a call to ResolutionUtiliy.resolution(...).
	 */
	private void execute_resolution()
	{
		if(_state != ModelState.RESOLUTION ) return;
		

		LinkedList<S_Calculation> res_steps = new LinkedList<>();
		for(int i = 0; i< _formula.size(); i++)
			_resolventen = _resolventen.union_with( ResolutionUtility.resolution( _formula.get(i),
																				  _formula.get(_formula.size()-1),
																				  res_steps));
		_resolventen.remove_duplicate_clauses();
		remove_empty_clause();
		_index++;
		_steps.add(new Step_Resolution(ModelState.RESOLUTION,_index,res_steps, _resolventen, merge_formula() ));
		update();
		
		
		if(_resolventen.is_empty())
		{
			_state = ModelState.END;
			update();
		}else
		{
			_state = ModelState.BEFORE_SUBSUMPTION;
			update();
			
			_state = ModelState.SUBSUMPTION;
		}
		generate_log();
	}

	/**
	 * Executes a forward subsumption step. Even if it is called forward subsumption
	 * the operation executed is the absorption rule.
	 */
	@SuppressWarnings("Duplicates")
	private void execute_forward_subsumption()
	{
		if(_fw || _state != ModelState.SUBSUMPTION) return;
		//remove_duplicates();
		LinkedList<S_Calculation> sub_steps = new LinkedList<>();
		_subsumed_resolvents = _resolventen;
		for(int i = 0; i<_formula.size();i++)
			_subsumed_resolvents  =   ResolutionUtility.subsumption(_subsumed_resolvents, _formula.get(i),sub_steps ) ;
		_steps.add(new Step_Subsumption(ModelState.SUBSUMPTION_FW,_index, sub_steps, merge_formula(), _resolventen,_subsumed_resolvents));
		_fw = true;
		update();
		if(_subsumed_resolvents.is_empty())
		{
			_state = ModelState.END;
			update();
		}else if(_bw)
		{

			_state = ModelState.BEFORE_RESOLUTION;
			_fw = false;
			_bw = false;
			_formula.add(_subsumed_resolvents);
			_resolventen = new Formula_NF();
			_subsumed_resolvents = new Formula_NF();
			update();
			_state = ModelState.RESOLUTION;
		}
		generate_log();
	}
	/**
	 * Executes a backward subsumption step. Even if it is called backward subsumption
	 * the operation executed is the absorption rule.
	 */
	private void execute_backward_subsumption()
	{
		if(_bw || _state != ModelState.SUBSUMPTION) return;
		//remove_duplicates();
		LinkedList<S_Calculation> sub_steps = new LinkedList<>();
		Formula_NF formula_before = merge_formula();
		for(int i = 0; i< _formula.size(); i++)
			 _formula.set(i,  ResolutionUtility.subsumption(_formula.get(i), _resolventen,sub_steps ) );
		_steps.add(new Step_Subsumption(ModelState.SUBSUMPTION_BW,_index, sub_steps,_resolventen , formula_before, merge_formula()));
		_bw = true;
		update();
		//noinspection Duplicates
		if(_fw)
		{
			_state = ModelState.BEFORE_RESOLUTION;
			_fw = false;
			_bw = false;
			_formula.add(_subsumed_resolvents);
			_resolventen = new Formula_NF();
			_subsumed_resolvents = new Formula_NF();
			update();
			_state = ModelState.RESOLUTION;
		}
		generate_log();
	}

	/**
	 * Sets the state of the algorithm one step back.
	 */
	@SuppressWarnings("unchecked")
	private void execute_step_back()
	{
		if(_logger.size() == 1) return;
		_logger.removeLast();
		BDLog log = _logger.getLast();
		_steps = (LinkedList<Step>) log.steps.clone();
		_formula = (ArrayList<Formula_NF>) log.formula.clone();
		_resolventen = new Formula_NF(log.resolventen);
		_subsumed_resolvents = new Formula_NF(log.subsumed_resolvents);
		_subsumed_formula = new Formula_NF(log.subsumed_formula);
		_fw = log.fw;
		_bw = log.bw;
		_index = log.index;
		_state =  ModelState.BACK;
		_back_step_state = log.state;
		update();
		_state = log.state;

	}

	/**
	 * If an empty clause is present in _resolventen then it is removed.
	 */
	private void remove_empty_clause()
	{
		if(_resolventen.contains_empty_clause())
			_resolventen.remove_clause(_resolventen.get_empty_clause());
	}

	/**
	 * Triggers an update of the actual state of the class to the
	 * BDResolutionViewModel.
	 */
	private void update()
	{
		if(_observer != null) _observer.update();
	}

	/**
	 * Creates on Fromula_Nf instances formed by all teh elements in
	 * the _formula field.
	 * @return Formula_NF instance of teh union of all elements in _formula.
	 */
	private Formula_NF merge_formula()
	{
		Formula_NF merged_formula = new Formula_NF();
		for(Formula_NF formula : _formula )
			merged_formula = merged_formula.union_with(formula);
		return merged_formula;
	}

	/**
	 * Creats a BDLog instance saving the state of the class and adding it to _logger.
	 */
	private void generate_log()
	{
		BDLog log = new BDLog(	_steps,
								_formula,
								_resolventen,
								_subsumed_resolvents,
								_subsumed_formula,
								_state,
								_fw,
								_bw,
								_index);
		_logger.add(log);

	}
	

}

package lehrtools.viewmodel.information;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import lehrtools.model.Clause;
import lehrtools.model.Formula_NF;
import lehrtools.model.state.*;

/**
 * This class generates the content for the View thats offered by the ViewModel.
 */
public class Line_Factory {
	/**
	 * Generates Line instances for the ResolutionModelView Class.
	 * @param step Step instance with the details of the execution of a step of the Resolution algorithm.
	 * @return Line instances containing the information fo the step the View needs.
	 */
	public Line get_resolution_line(Step step)
	{
		switch(step.type)
		{
			case START : Step_Start start_step = (Step_Start) step;
						 if(start_step.index == 0)
							 return new Line_Composite("Normalform ",
									 				"Res(F)_"+ start_step.index,
									 				formula_to_array(start_step.formula));
						 else
							 return new Line_Composite("Vereinigung" + " Res(F)_"+(start_step.index-1) + "' \u222A R_"+(start_step.index-1)+"'" ,
						 				"Res(F)_"+ start_step.index,
						 				formula_to_array(start_step.formula));
			case RESOLUTION : Step_Resolution res_step = (Step_Resolution) step;
							  return new Line_Double(res_step.type.toString(),
									  			 	 "Alle nicht-tautologischen Resolventen von Res(F)_"+(res_step.index),
									  			 	 "R_"+(res_step.index),
									  			 	 formula_to_array(res_step.resolvents),
									  				 res_step.steps);
			case RESOLUTION_OVER : Step_Resolution_Over res_over_step = (Step_Resolution_Over) step;
								   return new Line_Double(res_over_step.type.toString()+res_over_step.resolved_over,
										   "Alle nicht-tautologischen Resolventen von Res(F)_"+ (res_over_step.index) +" über die Variable "+ res_over_step.resolved_over,
										   "R_"+(res_over_step.index)+"("+res_over_step.resolved_over+")",
										   formula_to_array(res_over_step.resolvents),
										   res_over_step.steps);
			case SUBSUMPTION_FW :
			case SUBSUMPTION_BW : Step_Subsumption sub_step = (Step_Subsumption) step;
								  String description = "";
								  String formula_name = "";
								  if(sub_step.type == ModelState.SUBSUMPTION_FW)
								  {
									  description = "Löschen in R_"+(sub_step.index+1)+" aller durch Res(F)_"+sub_step.index+" subsumierte Klauseln";
									  formula_name = "R_"+(sub_step.index)+"'";
								  }
								  else
								  {
									  description = "Löschen in Res(F)_"+sub_step.index+" aller durch R_"+(sub_step.index+1)+" subsumierte Klauseln";
									  formula_name = "Res(F)_"+sub_step.index+"'";
								  }

									return new Line_Double(sub_step.type.toString(),
											description,
											formula_name,
											formula_to_array(sub_step.subsumed_formula),
											sub_step.steps);

			case END : Step_End end_step = (Step_End) step;
					   if(end_step.proof == 0)
						   return new Line (end_step.type.toString(), "Die Formel ist unerfüllbar.");
					   else 
						   return new Line (end_step.type.toString(), "Die Formel ist erfüllbar.");
			default: return null;
			
		}
		
	}
	/**
	 * Generates Line instances for the BDResolutionModelView Class.
	 * @param step Step instance with the details of the execution of a step of the Backward Dual Resolution algorithm.
	 * @return Line instances containing the information fo the step the View needs.
	 */
	public Line get_bd_resolution_line(Step step)
	{
		switch(step.type)
		{
			case START : Step_Resolution res_start = (Step_Resolution) step;
						 return new Line_Double(res_start.type.toString(),
								"Formeln in Normalform",
								"R_"+(res_start.index),
								formula_to_array(res_start.resolvents),
								res_start.steps);
			case RESOLUTION : Step_Resolution res_step = (Step_Resolution) step;
							return new Line_Double(res_step.type.toString(),
									"Alle nicht-tautologischen Resolventen von R_i, i<"+(res_step.index),
									"R_"+(res_step.index),
									formula_to_array(res_step.resolvents),
									res_step.steps);
			case SUBSUMPTION_FW :
			case SUBSUMPTION_BW : Step_Subsumption sub_step = (Step_Subsumption) step;
				String description = "";
				String formula_name = "";
				if(sub_step.type == ModelState.SUBSUMPTION_FW)
					formula_name = "R_"+(sub_step.index);
				else
					formula_name = "R_i , i<"+sub_step.index;

				return new Line_Double(sub_step.type.toString(),
						description,
						formula_name,
						formula_to_array(sub_step.subsumed_formula),
						sub_step.steps);
			default: return null;

		}
	}

	/**
	 * Generates Line instances for the DP60ModelView Class.
	 * @param step Step instance with the details of the execution of a step of the DP60 algorithm.
	 * @return Line instances containing the information fo the step the View needs.
	 */
	public Line get_dp60_line(Step step)
	{

		switch(step.type)
		{
			case RULE_01:
			case RULE_01_OVER:
			case RULE_02: Step_DP step_dp = (Step_DP) step;
							return new Line_DP60(process_label(step_dp.type),
							process_step(step_dp.type)+step_dp.literals.getFirst(),
							process_label(step_dp.type)+ " : " + process_step(step_dp.type)+step_dp.literals.getFirst(),
							formula_to_array(step_dp.result),
							calculations_dp_to_array(step_dp.calculations)
							);
			case RULE_02_OVER:  Step_DP step_dpx = (Step_DP) step;
								return new Line_DP60(process_label(step_dpx.type),
										process_step(step_dpx.type) + " " + step_dpx.literals.getFirst(),
										process_label(step_dpx.type)+ " : " + process_step(step_dpx.type),
										formula_to_array(step_dpx.result),
										calculations_dp_to_array(step_dpx.calculations)
								);

			case RULE_03: 	Step_DP step_dp1 = (Step_DP) step;
							return new Line_DP60(process_label(step_dp1.type),
									process_step(step_dp1.type) +"über " +step_dp1.literals.getFirst().toString(),
									process_label(step_dp1.type)+ " : " + process_step(step_dp1.type),
									formula_to_array(step_dp1.result),
									calculations_res_to_array(step_dp1.rule_03_calculations)
							);
			case START:  Step_DP step_dp2 = (Step_DP) step;
						return new Line_Simple(	step_dp2.type.toString(),
											   "",
												formula_to_array(step_dp2.original)
												);

			case END: 	Step_End end_step = (Step_End) step;
						if(end_step.proof == 0)
							return new Line (end_step.type.toString(), "Die Formel ist unerfüllbar.");
						else
							return new Line (end_step.type.toString(), "Die Formel ist erfüllbar.");
			default: return null;
		}



	}

	/**
	 * Converts the clauses of a formula into a list of Strings , where each string the repesentation of a clause is.
	 * @param formula Formula to be represented a a list of strings.
	 * @return List of clauses as string.
	 */
	private ArrayList<String> formula_to_array(Formula_NF formula )
	{
		ArrayList<String> clauses = new ArrayList<>();
		Clause[] sorted_clauses = new Clause[formula.get_clauses().size()] ;
		formula.get_clauses().toArray(sorted_clauses);
		Arrays.sort(sorted_clauses);
		for(Clause clause : sorted_clauses)
			clauses.add(clause.toString());
		return clauses;
	}

	/**
	 * Converts a List of Calculation into an List the View can use to display the calculation.
	 * This method only converts calculations resulting of the Unit Clasue and Pure Literal step
	 * fromthe DP60 algorithm.
	 * @param calculations List of S_DP_Calcualtions.
	 * @return ArrayList<ArrayList<String>>
	 */
	private ArrayList<ArrayList<String>> calculations_dp_to_array(LinkedList<S_DP_Calculation> calculations)
	{
		ArrayList<ArrayList<String>>calculations_info = new ArrayList<>();
		for(S_DP_Calculation calculation : calculations)
			calculations_info.add( calculation.toStringList());
		return calculations_info;

	}

	/**
	 * Converts a List of Calculation into an List the View can use to display the calculation.
	 * This method only converts S_Res_Calcualtionand S_Sub_Calculation instances..
	 * @param calculations List of S_Calcualtions.
	 * @return ArrayList<ArrayList<String>>
	 */
	private ArrayList<ArrayList<String>> calculations_res_to_array(LinkedList<S_Calculation> calculations)
	{
		ArrayList<ArrayList<String>> calculations_info = new ArrayList<>();
		for(S_Calculation calculation : calculations)
			calculations_info.add( calculation.toStringList());
		return calculations_info;

	}

	private String process_label(ModelState state)
	{
		switch(state)
		{
			case RULE_01:
			case RULE_01_OVER: return "Regel 1";
			case RULE_02:
			case RULE_02_OVER: return "Regel 2";
			case RULE_03: return "Regel 3";
			default: return "";
		}
	}

	private String process_step(ModelState state)
	{
		switch(state)
		{
			case RULE_01:
			case RULE_01_OVER: return "Unit-Klausel-Eliminierung ";
			case RULE_02:
			case RULE_02_OVER: return "Pure Literal ";
			case RULE_03: return "Variableneliminiation ";
			default: return "";
		}
	}



}

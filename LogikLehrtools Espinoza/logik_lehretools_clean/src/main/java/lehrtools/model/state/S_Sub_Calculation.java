package lehrtools.model.state;

import lehrtools.model.Clause;

import java.util.ArrayList;

/**
 * Contains the Clauses involved ina subsumption or absorption step.
 */
public class S_Sub_Calculation extends S_Calculation{

	/**
	 * clause_2 :the absorber clause.
	 */
	public final Clause clause_2;

	/**
	 * Constructor
	 * @param clause_1 The subsumed or absorbed clause.
	 * @param clause_2 The subsumer or absorber clause.
	 */
	public S_Sub_Calculation(Clause clause_1, Clause clause_2)
	{

		super(clause_1);
		this.clause_2 = clause_2;
		
	}
	
	@Override
	public String toString()
	{
		return clause_2 + " absorbiert " + clause_1;
	}
	@Override
	public ArrayList<String> toStringList()
	{
		ArrayList<String> list = new ArrayList<>();
		list.add("Die Klausel ");            //0
		list.add(clause_2.prefix()+")");         //1
		list.add(clause_2.toStringWP());     //2
		list.add(" absorbiert die Klausel "); //3
		list.add(clause_1.prefix()+")");         //4
		list.add(clause_1.toStringWP());     //5

		return list;

	}

}

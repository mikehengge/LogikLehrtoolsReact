package lehrtools.model.state;

import lehrtools.model.Clause;

import java.util.ArrayList;

/**
 * Contains the Clauses involved in a resolution calculation.
 */
public class S_Res_Calculation extends S_Calculation{
	/**
	 * One of the clashing clauses from which a resolvent was derived.
	 */
	public final Clause parent_1;
	/**
	 * One of the clashing clauses from which a resolvent was derived.
	 */
	public final Clause parent_2;

	/**
	 * Constructor
	 * @param resolvent The resolvent in a resolution calculation.
	 * @param parent_1 A clause from which th resolvent was derived.
	 * @param parent_2 A clause from which th resolvent was derived.
	 */
	public S_Res_Calculation(Clause resolvent, Clause parent_1, Clause parent_2)
	{
		super(resolvent);
		this.parent_1= parent_1;
		this.parent_2 = parent_2;
	}

	@Override
	public String toString()
	{
		return clause_1 + " folgt aus " + parent_1 + " und " + parent_2;
	}
	@Override
	public ArrayList<String> toStringList()
	{
		ArrayList<String> list = new ArrayList<>();
		list.add(clause_1.prefix()+")");       //0
		list.add(clause_1.toStringWP());   //1
		list.add(" folgt aus ");        //2
		list.add(parent_1.prefix()+")");       //3
		list.add(parent_1.toStringWP());   //4
		list.add(" und ");                 //5
		list.add(parent_2.prefix()+")");       //6
		list.add(parent_2.toStringWP());   //7

		return list;

	}


}

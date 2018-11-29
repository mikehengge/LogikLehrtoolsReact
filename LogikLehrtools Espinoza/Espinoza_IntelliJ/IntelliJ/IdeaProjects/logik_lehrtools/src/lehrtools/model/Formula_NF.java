package lehrtools.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import lehrtools.formula.Variable;
/**
 * Set base representation of a propositional formula in normal form.
 */
public class Formula_NF {
	/**
	 * Represents a Clause or a Term depending on the normal form 
	 * the propositional formula is.
	 */
	private Set<Clause> _clauses;
	/**
	 * Constructor that creates an empty Formula_NF
	 */
	public Formula_NF()
	{
		_clauses = new HashSet<Clause>();
	}
	/**
	 * Creates Formula_NF from a given collection of Clauses
	 * @param clauses : clauses describing the Formula_NF
	 */
	public Formula_NF(Collection<Clause> clauses)
	{
		_clauses = new HashSet<Clause>();
		for(Clause c : clauses)
			this.add_clause(c);
	}
	/**
	 * Creates a Formula_NF froma given enumeration fo Clauses
	 * @param clauses : clauses describing the Formula_NF
	 */
	public Formula_NF(Clause... clauses)
	{
		_clauses = new HashSet<Clause>();
		for(Clause c : clauses)
			this.add_clause(c);
	}
	/**
	 * Copy Constructor used to create a deep copy of a given Formula_NF.
	 * @param formula : Formula_NF to be copied.
	 */
	public Formula_NF(Formula_NF formula)
	{
		_clauses = new HashSet<Clause>();
		for(Clause clause : formula.get_clauses())
			_clauses.add(new Clause(clause));
	}
	/**
	 * Getter for the Set of Clauses
	 * @return : Set of Clauses representing the formula.
	 */
	public Set<Clause> get_clauses() {return _clauses;}
	/**
	 * Adds a Clause to the Formula_NF. If the Clause is already contained
	 * then nothing will be added. If the formula already has no parents but a prefix, the prefix will be kept.
	 * @param clause : Clause to be added to the Formula_NF
	 */
	public void add_clause(Clause clause) 
	{ 
		if(clause.parents()==null && clause.prefix()=="") {
			ArrayList<Integer> prefix_list	= new ArrayList<>();
			prefix_list.add(_clauses.size()+1);
			clause.set_prefix("" + (_clauses.size() + 1), prefix_list	);
		}
		_clauses.add(clause);
	}	
	/**
	 * Adds a collection of Clauses.
	 * @param clauses :  collection of Clauses adet to the Formula_NF.
	 */
	public void add_all(Collection<? extends Clause> clauses) 
	{
		for(Clause c : clauses)
			this.add_clause(c);
	}
	/**
	 * Removes a given Clause. If the Clause is not contained ein the formula_NF, then nothing will be done.  
	 * @param clause : Clause to be removed.
	 */
	public void remove_clause(Clause clause) {
        _clauses.remove(clause);
    }
	/**
	 * Merges two Formula_NF into a new one
	 * @param formula : Formula_NF to be merged with.
	 * @return :  new Formula_NF representing the union of both Formula_NF
	 */
	public Formula_NF union_with(Formula_NF formula)
	{
		Formula_NF new_formula  = new Formula_NF(this);

		new_formula.get_clauses().addAll(formula.get_clauses());
		return new_formula;
		
	}
	/**
	 * Method that test if the Fromula_NF contains a unit clause.
	 * @return : true if it contains a unit clause, false otherwise.
	 */
	public boolean contains_unit_clause()
	{
		for(Clause clause : _clauses)
			if(clause.isUnit()) return true;
		return false;
	}
	/**
	 * Test if the Formula_NF is empsty.
	 * @return : true if the Fromula_NF is empt, flase otherwise.
	 */
	public boolean is_empty() {return _clauses.isEmpty();}
	/**
	 * Tests if Fromula_NF contains an empty Clause.
	 * @return : true if it contains an empty clause, false otherwise.
	 */
	public boolean contains_empty_clause()
	{
		for(Clause clause : _clauses)
			if(clause.isEmpty()) return true;
		return false;
	}
	/**
	 * If the Formula_NF contains an empty clause this method will retieve it.
	 * If there is no empty clause a null reference will be returned.
	 * It is recommended to be used after checking with contains_empty_clause()
	 * @return : an empty Clause if one is contained, null reference otherwise
	 */
	public Clause get_empty_clause()
	{
		for(Clause clause : _clauses)
			if(clause.isEmpty()) return clause;
		return null;
	}
	/**
	 * Retrieves a unit Clause if one is present.
	 * If no unit Clause is present then a null reference is returned.
	 * It is recommended to be used after calling contains_unit_clause.
	 * @return
	 */
	public Clause get_unit_clause()
	{
		for(Clause clause : _clauses)
			if(clause.isUnit()) return clause;
		
		return null;
	}
	/**
	 * Returns all variables present in the Formula_NF as a Set.
	 * @return : Set of Variables present in the formula_NF.
	 */
	public Set<Variable> vars()
	{
		Set<Variable> vars = new HashSet<Variable>();
		
		for(Clause clause : _clauses)
			for(Literal literal : clause.getLiterals())
				vars.add(literal.variable());
		return vars;
	}
	/**
	 * Test if a given Clause is present in the Formula_NF.
	 * @param clause : Clause to be searched for.
	 * @return : true if a Clause equal to the given one is found, false otherwise.
	 */
	public boolean contains_clause(Clause clause)
	{
		for(Clause c : _clauses)
			if(clause.equals(c)) return true;
		return false;
	}

	/**
	 * Removes duplicate clauses. Here 2 clauses are considered equal if literal set
	 * of both clauses is equal
	 */
	public void remove_duplicate_clauses()
	{
		HashSet<Clause> new_clause_set = new HashSet<>();
		for(Clause old_clause : _clauses)
		{
			if(!contains_clause_set(old_clause,new_clause_set))
				new_clause_set.add(old_clause);
		}

		_clauses = new_clause_set;
	}

	/**
	 * Helper method remove_duplicate_clauses() method. Test if a given Clause
	 * an equal clause is contained in a given set of Clauses. The notion of equality
	 * is restirct to having the same set of literals.
	 * @param a_clause a Clasue instance
	 * @param set  A set of Clasuses
	 * @return
	 */
	private boolean contains_clause_set(Clause a_clause, Set<Clause> set)
	{
		for(Clause clause : set)
			if(clause.is_equal(a_clause))
				return true;
		return false;
	}

	@Override
	public int hashCode()
	{
		int hash = 1;
		for(Clause clause : _clauses)
			hash = 3*hash+clause.hashCode();
		return hash;
	}
	@Override
	public boolean equals(Object formula)
	{
		if (formula == null)
			return false;
		if (formula == this)
			return true;
		if (!(formula instanceof Formula_NF))
			return false;
		return _clauses.equals(((Formula_NF)formula).get_clauses());
	}
	@Override
	public String toString()
	{
		if(this.is_empty()) return "{}";
		String _string = "{ ";
		for(Clause clause : _clauses)
			_string = _string + clause.toString() + " , ";
		return _string.subSequence(0, _string.length()-2) + "}";
	}

}

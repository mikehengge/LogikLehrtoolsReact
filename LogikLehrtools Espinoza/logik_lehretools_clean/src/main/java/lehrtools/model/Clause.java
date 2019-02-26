package lehrtools.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javafx.util.Pair;

/**
 * Represents a Clause in a set based representation of a propositional formula
*
 */
public class Clause implements  Comparable{
	/**
	 * Set of literals describing the clause.
	 */
	private Set<Literal> _literals;
	/**
	 * Is this Clause deduced through resolution from two other clauses, 
	 * then the are stored as parents.
	 */
	private Pair<Clause,Clause> _parents;
	/**
	 * Its a composition of the prefixes of the parents.
	 * If the Clause has no parents an integer will be assigned.
	 */
	private String _prefix;
	/**
	 * The integers describingthe prefix of teh clause in order of appearance.
	 */
	ArrayList<Integer> _prefix_list;
	/**
	 * Constructs an empty clause.
	 */
	public Clause() 
	{
		_literals = new HashSet<Literal>();
		_prefix = "";
		_prefix_list = new ArrayList<>();
	}


	/**
	 * Constructor for a unit clause
	 * 
	 * @param lit
	 *            the single literal in this clause
	 */
	public Clause(Literal lit) 
	{
		_literals = new HashSet<Literal>();
		_literals.add(lit);
		_prefix = "";
		_prefix_list = new ArrayList<>();
	}
	
	/**
	 * Constructor for a clause with the given collection of literals.
	 * 
	 * @param lits
	 *            literals of the clause
	 */
	public Clause(Collection<Literal> lits) {
		_literals = new HashSet<Literal>(lits);
		_prefix = "";
		_prefix_list = new ArrayList<>();
	}

	/**
	 * Constructor for a clause with the given literals.
	 * 
	 * @param lits
	 *            literals of the clause
	 */
	public Clause(Literal... lits) {
		this._literals = new HashSet<Literal>();
		for (Literal l : lits)
			_literals.add(l);
		_prefix = "";
		_prefix_list = new ArrayList<>();
	}
	/**
	 * Copy Constructor for a clause.Creates a deep 
	 * Copy of the Clause.
	 * 
	 * @param clause
	 *            given clause to be copied      
	 */
	public Clause(Clause clause)
	{
		_literals = new HashSet<Literal>();
		for(Literal literal : clause.getLiterals())
			_literals.add(new Literal(literal));
		if ( clause.parents()!=null) 
			_parents = new Pair<Clause, Clause>(new Clause(clause.parents().getKey()),new Clause(clause.parents().getValue()));
		_prefix = clause.prefix();
		_prefix_list = new ArrayList<>();
		_prefix_list.addAll(clause._prefix_list);
		
	}
	
	/**
	 * Returns the literals of this clause.
	 * 
	 * @return literals of this clause
	 */
	public Set<Literal> getLiterals() {
		return new HashSet<Literal>(_literals);
	}
	/**
	 * Returns the parents of a Clause contained in a 
	 * Pair Class.
	 * @return : A Pair containing both parents of the Clause
	 */
	public Pair<Clause, Clause> parents() { return _parents;}
	/**
	 * Returns the value of the prefix representing the parents of the Clause.
	 * @return : String containing the prefix. 
	 */
	public String prefix()
	{
		//String _string = new StringBuilder().append(_prefix).append("){").toString();
		return  _prefix;
	}

	/**
	 * Sets the value of the prefix representing the parents of the Clause.
	 * This method is used when the Clause has no parents and the prefix is then 
	 * set by the Formula it is contained in.
	 * @param prefix : String containing the prefix.
	 */
	public void set_prefix(String prefix, ArrayList<Integer> prefix_list)
	{
		_prefix = prefix;
		_prefix_list = prefix_list;
	}
	/**
	 * If the clause was created as a result of the resolution over clauses,
	 * then this clauses become the parents of the clause , and are referenced here.
	 * @param parent_1 : parent Clause.
	 * @param parent_2 : parent Clause.
	 */
	public void parents(Clause parent_1 , Clause parent_2)
	{
		_parents = new Pair<Clause, Clause>(parent_1, parent_2);
		_prefix_list.clear();
		if(compare_prefix_list(parent_1._prefix_list, parent_2._prefix_list) == -1) {
			_prefix = "[" + parent_1.prefix() + "," + parent_2.prefix() + "]";
			_prefix_list.addAll(parent_1._prefix_list);
			_prefix_list.addAll(parent_2._prefix_list);
		}
		else {
			_prefix = "[" + parent_2.prefix() + "," + parent_1.prefix() + "]";
			_prefix_list.addAll(parent_2._prefix_list);
			_prefix_list.addAll(parent_1._prefix_list);
		}

	}

	/**
	 * Add a literal to the clause
	 * 
	 * @param lit
	 *            the literal to add
	 */
	public void addLiteral(Literal lit) {
		_literals.add(lit);
	}

	/**
	 * Remove a literal from the clause
	 * 
	 * @param lit
	 *            the literal to remove
	 */
	public void removeLiteral(Literal lit) {
		if(_literals.contains(lit)) _literals.remove(lit);
	}

	/**
	 * Tests if the Clause is a unit clause.
	 * 
	 * @return true if this clause is unit, false else
	 */
	public boolean isUnit() {
		if(_literals.size() == 1) 
			return true;
		else 
			return false;
	}

	/**
	 * Test if the Clause is an empty clause.
	 * 
	 * @return true if this clause is empty, false else
	 */
	public boolean isEmpty() {
		return _literals.isEmpty();
	}

	/**
	 * If this clause is a unit  clause it will returns the
	 * single literal of this clause . Otherwise null will be returned.
	 * It is recomended to be used together with the method isUnit().
	 * 
	 * @return the single literal in this clause in unit case, otherwise null
	 */
	public Literal getUnitLit() {
		if (isUnit())
			return _literals.iterator().next();
		else
			return null;
	}

	/**
	 * Returns true if this clause contains a certain literal lit
	 * 
	 * @param lit
	 *            the literal
	 * @return true if this clause contains lit, false else
	 */
	public boolean contains(Literal lit) {
		return _literals.contains(lit);
	}

	/**
	 * Adds all literals of the given clause to this clause.
	 * 
	 * @param c
	 *            clause to union with
	 */
	public void unionWith(Clause c) {
		_literals.addAll(c.getLiterals());
	}

	private int compare_prefix_list(ArrayList<Integer> prefix_list1,ArrayList<Integer> prefix_list2)
	{
		if(prefix_list1.size() < prefix_list2.size())
			return -1;
		else if(prefix_list1.size() > prefix_list2.size())
			return 1;
		else
		{
			for(int i = 0; i<prefix_list1.size();i++)
				if(prefix_list1.get(i) < prefix_list2.get(i))
					return -1;
				else if(prefix_list1.get(i) > prefix_list2.get(i))
					return 1;

			return 0;
		}
	}

	/**
	 * Implements an equal test based only on the literals contained in the Clausem ignoring the parents.
	 * It will return true if both Clauses contains the same literals.
	 * @param clause Clause this instance is tested against.
	 * @return true if sthe set of literals is equals , false else.
	 */
	public boolean is_equal(Clause clause)
	{
		return _literals.equals(clause._literals) ;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o == this)
			return true;
		if (!(o instanceof Clause))
			return false;
		Clause clause = (Clause) o;

		if( _parents != null && clause.parents() != null)
		{
			if (_parents.getKey().equals(clause._parents.getKey()))
			{
				if(!_parents.getValue().equals(clause._parents.getValue()))
					return false;
			}
			else if (_parents.getKey().equals(clause._parents.getValue()))
			{
				if (!_parents.getValue().equals(clause._parents.getKey()))
					return false;
			}
			else
				return false;

		}
		else if(_parents == null && clause.parents() != null )
			return false;
		else if(_parents != null && clause.parents() == null)
			return false;

		return _literals.equals(clause._literals) ;
	}

	@Override
	public int hashCode() {
		int sum = 0;
		for (Literal lit : _literals)
			sum += lit.hashCode();
		return sum;
	}

	@Override
	public String toString() {
		if(this.isEmpty()) return _prefix +"{}";
		String _string = _prefix + ") {";
		for(Literal lit : _literals)
			_string = _string + lit.toString() +",";
		return _string.subSequence(0, _string.length()-1)+"}";
	}

	/**
	 * Returns a String representation of the Clause without the prefix provided by the parents.
	 * @return a String representation of the Clause without the prefix provided by the parents.
	 */
	public String toStringWP() {
		if(this.isEmpty()) return "{}";
		String _string = "{";
		for(Literal lit : _literals)
			_string = _string + lit.toString() +",";
		return _string.subSequence(0, _string.length()-1)+"}";
	}

	/**
	 * Test is the clasue or term that is represnted is a degenerate clause or term.
	 * @return true if it is degenerate, false if not.
	 */
	public boolean isDegenarate()
	{
		for(Literal literal : _literals)
			if(_literals.contains(literal.negate()))
				return true;
		return false;
	}


	@Override
	public int compareTo(Object o) {
		Clause clause = (Clause) o;
		return compare_prefix_list(_prefix_list,clause._prefix_list);
	}
}

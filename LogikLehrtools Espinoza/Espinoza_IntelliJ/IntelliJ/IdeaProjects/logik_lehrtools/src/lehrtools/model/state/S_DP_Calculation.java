package lehrtools.model.state;

import lehrtools.model.Clause;
import lehrtools.model.Literal;

import java.util.ArrayList;

/**
 * Used to store information of the Clauses involved in a Unit Clause or Pure Literal operation
 * for the DP60 procedure.
 */
public class S_DP_Calculation extends S_Calculation {
    /**
     * If the instance represents the elimination of a clause or a a literal
     * for a Unit Clause step the unit clause is stored here.
     */
    public final Clause unit_clause;
    /**
     * Stores the Clause that results from the elimination fo a literal during a Prue Literal or Unit Clause step.
     */
    public final Clause reduced_clause;
    /**
     * Stores a String specifying what kind of operation was executed. It is used to generate the adequate description.
     * "Rule 01 Clause" : the Elimination of a Clause during a Unit Clause step.
     * "Rule 01 Variable" : the Elimination of a complemtary literal from a Clause
     * during a Unit Clause step.
     * "Rule 02" : the Elimination of a literal during a Pure Literal step.
     */
    public final String type;
    /**
     * Stores the literal used in a Pure Literal step or the literal contained in the unit clause.
     */
    public final Literal used_Literal;

    /**
     * Constructor
     * @param clause The clause on hich the operation is applied on.
     * @param unit_clause The unit clause involved ina a Unit Clause step.
     * @param reduced_clause Clause that results from the elimination fo a literal during a Prue Literal or Unit Clause step.
     * @param type  Specifies what kind of operation was executed.
     * @param literal Literal used in a Pure Literal step or the literal contained in the unit clause.
     */
    public S_DP_Calculation(Clause clause,
                            Clause unit_clause,
                            Clause reduced_clause,
                            String type,
                            Literal literal)
    {
        super(clause);
        this.unit_clause = unit_clause;
        this.reduced_clause = reduced_clause;
        this.type = type;
        used_Literal = literal;
    }

    @Override
    public String toString()
    {
        return clause_1 + " mit " + used_Literal;
    }
    @Override
    public ArrayList<String> toStringList()
    {
        ArrayList<String> list = new ArrayList<>();

        if(type == "Rule 01 Clause")
        {
            list.add("Unit Clause ");            //0 text
            list.add("{"+used_Literal+"}");      //1 clause
            list.add(" entfernt die Klausel ");  //2 text
            list.add(clause_1.prefix()+")");     //3 prefix
            list.add(clause_1.toStringWP());     //4 clause


        }
        else if(type == "Rule 01 Variable")
        {
            list.add("Unit Clause ");                   //0 text
            list.add("{"+used_Literal+"}");             //1 literal
            list.add(" entfernt die Variable ");        //2 text
            list.add(used_Literal.negate().toString()); //3 literal
            list.add(" aus der Klausel ");              //4 text
            list.add(clause_1.prefix() + ")");          //5 prefix
            list.add(clause_1.toStringWP());            //6 formula

        }
        else if(type == "Rule 02")
        {
            list.add("Pure Literal ");           //0
            list.add(used_Literal.toString());   //1
            list.add(" entfernt die Klausel ");  //2
            list.add(clause_1.prefix()+")");     //3
            list.add(clause_1.toStringWP());     //4

        }


        return list;

    }


}

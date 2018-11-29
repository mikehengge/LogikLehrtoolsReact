package lehrtools.model.state;

import lehrtools.model.Clause;

import java.util.ArrayList;

/**
 * A class used as base class for the classes
 * S_DP_Calculation, S_Res_Calculation and S_Sub_Calculation.
 * Contains information about an operation executed during a step
 * of te procedures offered in this application.
 */
public abstract class S_Calculation
{
    /**
     * Clause involved ina operation
     */
    public final Clause clause_1;

    /**
     * Contructor
     * @param clause : a Clause instance involved ina operation.
     */
    public S_Calculation(Clause clause) { clause_1 = clause;}
    @Override
    public abstract String toString();

    /**
     * Creates a String describing the operation it represents and returns it as substrings
     * stored in a an ArrayList. It is used to give different format to each substring
     * while its displayed in the Gui.
     * @return : List of String instances representing a decription of the operation it represents.
     */
    public abstract ArrayList<String> toStringList();
}

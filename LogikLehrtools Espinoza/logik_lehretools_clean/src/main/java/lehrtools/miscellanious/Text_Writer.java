package lehrtools.miscellanious;

import javafx.collections.ObservableList;
import lehrtools.viewmodel.information.*;

import java.util.ArrayList;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

/**
 * Writes the content of every Step in the algorithm to a text file.
 */
public class Text_Writer {
    /**
     * Method that takes a a list containing each step calculated during the algorithm
     * and writes the relevant content into a text file.
     * @param lines List of Steps
     */
    public static void write_to_file(ObservableList<Line> lines)
    {
        PrintWriter print_line;
        FileWriter writer;
        try
        {
            String path = System.getProperty("user.dir") + "\\output.txt";
            writer = new FileWriter(path, false);
            print_line = new PrintWriter(writer);
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return;
        }

        for(Line line : lines)
        {
            if(line instanceof Line_Composite)
                write_composite_box(line,print_line);
            else if(line instanceof Line_Double)
                write_double_box(line,print_line);
            else if(line instanceof Line_Simple)
                write_simple_box(line,print_line);
            else if(line instanceof Line_DP60)
                write_dp60_box(line,print_line);
            else
                write_default_box(line,print_line);
            print_line.println("");
        }



        print_line.close();

    }

    /**
     * Writes the content of a Line_Composite instanceintoa text file.
     * @param line line containing the calculation of one step.
     * @param print_line reference to the instance that eilldo the writing.
     */
    private static void write_composite_box(Line line, PrintWriter print_line)
    {
        Line_Composite line_composite = (Line_Composite) line;
        print_line.println(line_composite.label + " , " + line_composite.step_description);
        print_line.println(formula_to_string(line_composite.clauses));

    }

    /**
     * Writes the content of a Line_Double instanceintoa text file.
     * @param line line containing the calculation of one step.
     * @param print_line reference to the instance that eilldo the writing.
     */
    private static void write_double_box(Line line, PrintWriter print_line)
    {
        Line_Double line_double = (Line_Double) line;
        print_line.println(line_double.label + " , " + line_double.step_description);
        print_line.println(formula_to_string(line_double.clauses));
        print_line.println("Berrechnungen");
        for(ArrayList<String> calculation : line_double.calculations)
            print_line.println(calculation_to_string(calculation));
        if(line_double.calculations.isEmpty())
            print_line.println("Keine Berrechnungen wurden vorgenommen.");
    }
    /**
     * Writes the content of a Line_Simple instanceintoa text file.
     * @param line line containing the calculation of one step.
     * @param print_line reference to the instance that eilldo the writing.
     */
    private static void write_simple_box(Line line, PrintWriter print_line)
    {
        Line_Simple line_simple = (Line_Simple) line;
        print_line.println(line_simple.label);
        print_line.println(formula_to_string(line_simple.clauses));
    }
    /**
     * Writes the content of a Line_DP60 instanceintoa text file.
     * @param line line containing the calculation of one step.
     * @param print_line reference to the instance that eilldo the writing.
     */
    private static void write_dp60_box(Line line, PrintWriter print_line)
    {
        Line_DP60 line_dp = (Line_DP60) line;
        print_line.println(line_dp.label + " , " + line_dp.step);
        print_line.println(formula_to_string(line_dp.clauses));
        print_line.println("Berrechnungen");
        for(ArrayList<String> calculation : line_dp.calculations_info)
            print_line.println(calculation_to_string(calculation));
        if(line_dp.calculations_info.isEmpty())
            print_line.println("Keine Berrechnungen wurden vorgenommen.");

    }
    /**
     * Writes the content of a Line instanceintoa text file.
     * @param line line containing the calculation of one step.
     * @param print_line reference to the instance that eilldo the writing.
     */
    private static void write_default_box(Line line, PrintWriter print_line)
    {
        print_line.println(line.label);
        print_line.println(line.step_description);
    }

    /**
     * Merges a List of Strings into one string. The calculation is partitioned in substrings for better formatting
     * and are put toghether again before writing.
     * @param calculation_list list of string representing a calculation
     * @return merge string
     */
    private static String calculation_to_string(ArrayList<String> calculation_list)
    {
        String calculation ="";
        for(String sub_string :calculation_list)
            calculation = calculation + sub_string;
        return calculation;
    }

    /**
     * A formula is partitioned into its clauses,here its reassembled into a formula again.
     * @param clauses list of clauses representing aformula
     * @return formula in set based representation.
     */
    private static String formula_to_string( ArrayList<String> clauses)
    {
        String formula = "{ ";
        for(String clause: clauses)
        {
            formula = formula + clause;
            formula = formula + " , ";
        }
        if(!clauses.isEmpty())
            formula = (String) formula.subSequence(0,formula.length()-3);
        formula = formula + " }";
        return formula;
    }
}

package lehrtools.view.components;



import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Pair;
import lehrtools.model.Clause;

/**
 * Draws a deduction tree for a Clause into a Canvas.
 */
public class Tree_Generator {
	/**
	 * Starts the process of drawing the deduction tree of clause into th canvas.
	 * It uses the height of the tree to dertemine the maximal height the canvas instance needs
	 * to be able to display the tree.
	 * @param canvas Canvas instances that will display teh deduction tree.
	 * @param clause Clause for which the deduction tree is requested.
	 */
	public static void generate_tree(Canvas canvas, Clause clause)
	{
		int total_height =  tree_height(clause)*90 +30;
		canvas.setHeight(total_height);
		draw_tree(canvas,clause);
		
	}

	/**
	 * Calculates the height for the tree. The height
	 * is represented by the amount of nodes of the longest branch.
	 * @param clause clause at the root of the tree.
	 * @return height of the tree.
	 */
	private static int tree_height(Clause clause)
	{
		return tree_heigth_rek(clause,-1);
	}

	/**
	 * Helper class to calculate the height of a tree using recursion.
	 * @param clause clause at the root of the tree or subtree.
	 * @param actual_height the height at which the clause is position in the tree.
	 * @return the height of the tree.
	 */
	private static int tree_heigth_rek(Clause clause, int actual_height)
	{
		if(clause.parents()==null) return actual_height+1;
		Pair<Clause,Clause> children = clause.parents();
		int left_branch_height = tree_heigth_rek(children.getKey(),actual_height+1);
		int rigth_branch_height = tree_heigth_rek(children.getValue(),actual_height+1);
		
		if(left_branch_height > rigth_branch_height)
			return left_branch_height;
		else
			return rigth_branch_height;
	}

	/**
	 * Starts the draving of the deduction tree onthe canvas.
	 * @param canvas canvas tha will display the deduction tree
	 * @param clause Clause for which the deduction tree is requested.
	 */
	private static void draw_tree(Canvas canvas, Clause clause)
	{
		draw_tree(canvas,clause,0,20);
	}

	/**
	 * Helper Class that draws the deduction tree. This method will draw the the tree
	 * starting by the leafs of the tree, which are the clauses with out parent. This way the derived clauses can be
	 * position optimal. Also it drawn from left to right , which allows the resize the canvas in case more space is needed.
	 * The deduction tree is traverse recursively .
	 * @param canvas canvas tha will display the deduction tree
	 * @param clause Clause for which the deduction tree is requested.
	 * @param node_depth represents the height of the node to be drawn.
	 * @param rigth_boundary the actual width of the tree.
	 * @return an instance of Draw_Result containing the position, size of child node and teh actual width of the tree.
	 */
	private static Draw_Result draw_tree(Canvas canvas, Clause clause, int node_depth, double rigth_boundary)
	{
		GraphicsContext gc = canvas.getGraphicsContext2D();
		if(clause.parents() == null)
		{
			
			double size = clause.toString().length()*9;
			double heigth = (int) (canvas.getHeight()- node_depth*90);
			canvas.setWidth(rigth_boundary+size+20+40);
			
			gc.setFill(Color.web("#00527A"));
			gc.setStroke(Color.WHITE);
			gc.fillRect(rigth_boundary, heigth -30, size+40, 30);
			gc.setFill(Color.WHITE);
			gc.setFont(Font.font("Helvetica", FontWeight.BOLD, 20));
			gc.fillText(clause.toString(), rigth_boundary+20, heigth-10);
			
			return new Draw_Result(rigth_boundary,size+40,rigth_boundary+size+20+40);
			
		}
		else
		{
			Draw_Result left_node_results = draw_tree(canvas,clause.parents().getKey(),node_depth+1, rigth_boundary);
			Draw_Result rigth_node_results = draw_tree(canvas,clause.parents().getValue(),node_depth+1, left_node_results.rigth_boundary );
			double new_rigth_boundary = left_node_results.rigth_boundary > rigth_node_results.rigth_boundary 
														? left_node_results.rigth_boundary : rigth_node_results.rigth_boundary;
			double size = clause.toString().length()*9;
			double heigth = canvas.getHeight()- node_depth*90;
			double node_pos = left_node_results.h_position +(rigth_node_results.h_position + rigth_node_results.size - left_node_results.h_position - size)/2;
			
			gc.setFill(Color.web("#00527A"));
			gc.setStroke(Color.WHITE);
			gc.fillRect(node_pos-20, heigth -30, size+40, 30);
			gc.setLineWidth(3);
			gc.strokeLine(left_node_results.h_position +(rigth_node_results.h_position + rigth_node_results.size - left_node_results.h_position)/2, 
						  heigth-30, 
						  left_node_results.h_position + left_node_results.size/2, 
						  canvas.getHeight()- (node_depth + 1)*90);
			gc.strokeLine(left_node_results.h_position +(rigth_node_results.h_position + rigth_node_results.size - left_node_results.h_position)/2, 
					  	  heigth-30, 
					  	  rigth_node_results.h_position + rigth_node_results.size/2, 
					  	  canvas.getHeight()- (node_depth + 1)*90);
			gc.setFill(Color.WHITE);
			gc.setFont(Font.font("Helvetica", FontWeight.BOLD, 20));
			gc.fillText(clause.toString(), node_pos, heigth-10);
			return new Draw_Result(node_pos-20,size+40,new_rigth_boundary);
		}
		
	}

	/**
	 * Stores the position, size  of a node.
	 * Also the width of the tree after a node was drawn.
	 */
	private static class Draw_Result
	{
		final double h_position;
		final double size;
		final double rigth_boundary;
		
		public Draw_Result(double h_position, double size, double rigth_boundary)
		{
			this.h_position = h_position;
			this.size = size;
			this.rigth_boundary = rigth_boundary;
		}
	}
}

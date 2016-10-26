/**
 * 
 */
package example;

import java.util.HashMap;

import parser.SimpleCBaseVisitor;
import parser.SimpleCParser.IfStmtContext;

/**
 * @author parallels
 *
 */
public class IfVisitor extends SimpleCBaseVisitor<Void>{
	private HashMap<String, Integer> Var;
	
	public IfVisitor (HashMap<String, Integer> Var){
		this.setVar(Var);
		
	}

	
	@Override
	public Void visitIfStmt (IfStmtContext ctx){
		
		
		System.out.print(ctx.getText());
		return null;
	}
	
	
	
	/**
	 * @return the var 
	 */
	public HashMap<String, Integer> getVar() {
		return Var;
	}

	/**
	 * @param var the var to set
	 */
	public void setVar(HashMap<String, Integer> var) {
		Var = var;
	}

}

/**
 * 
 */
package example;

import java.util.HashMap;
import java.util.Map;

import parser.SimpleCBaseVisitor;
import parser.SimpleCParser.ProcedureDeclContext;
import parser.SimpleCParser.VarDeclContext;

/**
 * @author parallels
 *
 */
public class globalVarVisitor extends SimpleCBaseVisitor<Void> {
	private HashMap<String, Integer> Var;
	private StringBuilder GlobalSMTResult;

	public globalVarVisitor(HashMap<String,Integer> Var) {
		this.setVar(Var);
		GlobalSMTResult = new StringBuilder();
	}

	/* A boolean variable to check whether in the procedure */
	private boolean InProcedure = false;

	/* Check whether in the procedure declaration */
	@Override
	public Void visitProcedureDecl(ProcedureDeclContext ctx) {
		InProcedure = true;
		super.visitProcedureDecl(ctx);
		InProcedure = false;
		return null;
	}

	@Override
	public Void visitVarDecl(VarDeclContext ctx) {
		/* if not in the procedure, this is a global variable */
		if (!InProcedure) {
			String VarName = ctx.getChild(1).getText();
			// initialize the notation of one variable as 0
			Var.put(VarName, 0);
			VarName = VarName + "0";
			GlobalSMTResult.append(GlobalSMTGenerate(VarName).toString());
		}
		return null;
	}

	public StringBuilder GlobalSMTGenerate(String VarName) {
		StringBuilder GlobalSMT = new StringBuilder();
		String SMT = "(declare-fun " + VarName + " () Int)" + "\n";
		return GlobalSMT.append(SMT);
	}

	public String getResult() {
		return GlobalSMTResult.toString();
	}
	
	/* *********** *
	 * get and set *
	 * *********** */
	public HashMap<String, Integer> getVar() {
		return Var;
	}

	public void setVar(HashMap<String, Integer> var) {
		Var = var;
	}

}

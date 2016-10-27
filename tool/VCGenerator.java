package tool;
import parser.SimpleCParser.ProcedureDeclContext;
import example.assertStmtVisitor;
import parser.SimpleCParser.ProgramContext;
import example.FormalParameterVisitor;
import example.IfVisitor;
import example.declVisitor;
import example.globalVarVisitor;
import example.havocStmtVisitor;
import example.assignStmtVisitor;


import java.util.*;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;

public class VCGenerator {
	
	private static HashMap<String, Integer> Var= new HashMap<String,Integer>();
	
	
	private ProcedureDeclContext proc;
	private declVisitor declVis; 
	private assignStmtVisitor assignStmtVis;
	private globalVarVisitor gvv;
	private FormalParameterVisitor fpv;
	private assertStmtVisitor assertStmtVis;
	private IfVisitor ifVisitor;
	private ProgramContext progc;
	private StringBuilder result;
	private static String GlobalRes;
	//private havocStmtVisitor havocStmtVis;
	
	public VCGenerator(ProgramContext progc,ProcedureDeclContext proc) {
		this.proc = proc;
		this.progc = progc;
		// TODO: You will probably find it useful to add more fields and constructor arguments
		declVis = new declVisitor(Var);
		gvv = new globalVarVisitor(Var);
		result = new StringBuilder("(set-logic QF_NIA)\n");
	}
	
	public VCGenerator(ProgramContext progc) {
		this.progc = progc;
		// TODO: You will probably find it useful to add more fields and constructor arguments
		gvv = new globalVarVisitor(Var);
		result = new StringBuilder("(set-logic QF_NIA)\n");
	}
	public VCGenerator(ProcedureDeclContext proc) {
		this.proc = proc;
		fpv = new FormalParameterVisitor(Var);
		// TODO: You will probably find it useful to add more fields and constructor arguments
		declVis = new declVisitor(Var);
		assignStmtVis = new assignStmtVisitor(Var);
		Var = assignStmtVis.getMap();
		assertStmtVis = new assertStmtVisitor(Var);
		ifVisitor = new IfVisitor(Var);
		//havocStmtVis = new havocStmtVisitor(Var);
		result = new StringBuilder("(set-logic QF_NIA)\n");
		
	}
	public StringBuilder generateVC() {
			declVis.visit(proc);
			result.append(GlobalRes);
			fpv.visit(proc);
			assignStmtVis.visit(proc);
			assertStmtVis.visit(proc);
			//havocStmtVis.visit(proc);
			ifVisitor.visit(proc);
			result.append(fpv.getFormalParameterResult().toString());
			for(int i = 0 ; i<declVis.getIDArray().size() ; i++){
				String s = "(declare-fun " + declVis.getIDArray().get(i) +" () Int)" + "\n";
				result.append(s);
			}
			result.append(assignStmtVis.getdecla());
			//result.append(havocStmtVis.getdecla());
			result.append(assignStmtVis.getassignStmt());
			String assertexpr  = null;
			for(int i = 0 ; i < assertStmtVis.getassertExpr().size() ; i++){
				if(assertexpr == null)
					assertexpr =  assertStmtVis.getassertExpr().get(i);
				else
				assertexpr = assertexpr + " " + assertStmtVis.getassertExpr().get(i);
			}
//			System.out.println(assertStmtVis.getassertExpr().size());
			result.append("(assert (not (and " + assertexpr + ")))");
		
			// TODO: generate the meat of the VC
			result.append("\n(check-sat)\n");
			
			//Test function
//			System.out.println(Var.get("i"));
			//
			return result;
	}
	
	public void getGlobalResult(){
		gvv.visit(progc);
		String GlobalResult = gvv.getResult();
		GlobalRes = GlobalResult;
	}
	
}



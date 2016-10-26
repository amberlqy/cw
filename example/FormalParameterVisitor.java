package example;

import java.util.List;
import java.util.HashMap;

import parser.SimpleCBaseVisitor;
import parser.SimpleCParser.FormalParamContext;
import parser.SimpleCParser.ProcedureDeclContext;
import parser.SimpleCParser.VarDeclContext;

public class FormalParameterVisitor extends SimpleCBaseVisitor<Void>{
	private HashMap<String, Integer> Var;
	private StringBuilder FormalParameterResult;

	public FormalParameterVisitor (HashMap<String,Integer> Var){
		this.setVar(Var);
		setFormalParameterResult(new StringBuilder());
	}
	
	@Override
	public Void visitProcedureDecl(ProcedureDeclContext ctx) 
	{
		for(FormalParamContext fp:ctx.formals){
			String fpName = fp.ID().getText();
			Var.put(fpName, 0);
			fpName =fpName+"0";
			FormalParameterResult.append(FormalParameterSMTGenerate(fpName).toString());
		}
		return  null;
	}
	
	
	
	public StringBuilder FormalParameterSMTGenerate(String ParName) {
		StringBuilder FPSMT = new StringBuilder();
		String SMT = "(declare-fun " + ParName + " () Int)" + "\n";
		return FPSMT.append(SMT);
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

	public StringBuilder getFormalParameterResult() {
		return FormalParameterResult;
	}

	public void setFormalParameterResult(StringBuilder formalParameterResult) {
		FormalParameterResult = formalParameterResult;
	}
	
	
	
	
}

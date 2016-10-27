package example;

import java.util.HashMap;
import parser.SimpleCBaseVisitor;
import parser.SimpleCParser;

public class havocStmtVisitor extends SimpleCBaseVisitor<Void>{
	private HashMap<String, Integer> var;
	private String declaration;
	
	public havocStmtVisitor(HashMap<String, Integer> var) {
		this.var = var;
	}
	
	public String getLastIndexId(String id){
		String m;
		if(var.containsKey(id)){
			int i = var.get(id);
			m = id + String.valueOf(i);
		}
		else
			m = id + "0";
		return m;
	}
	
	public void increaseVarIndex(String id){
		if(var.containsKey(id)){
			var.put(id, var.get(id)+1);
		}
		else{
			var.put(id, 1);
		}
		if(declaration == null){
			declaration = "(declare-fun " + getLastIndexId(id) + " () Int)" + "\n";
		}
		else{
			declaration = declaration + "(declare-fun " + getLastIndexId(id) + " () Int)" + "\n";
		}
	}
	
	@Override
	public Void visitHavocStmt(SimpleCParser.HavocStmtContext ctx){
		increaseVarIndex(ctx.ID().getText());
		return null;
	}
	
	public String getdecla(){
		return declaration;
	}

}

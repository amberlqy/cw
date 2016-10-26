package example;

import java.util.*;

import parser.SimpleCParser.VarDeclContext;
import parser.SimpleCBaseVisitor;

public class declVisitor extends SimpleCBaseVisitor<Void> {

	private ArrayList<String> IDArray = new ArrayList<>();
	private HashMap <String, Integer> Var; 
	
	public declVisitor(HashMap<String, Integer> Var) {
		this.setVar(Var);
		// TODO Auto-generated constructor stub
	}
	@Override
	public Void visitVarDecl(VarDeclContext ctx) {
		String varName = ctx.ID().getText();
		Var.put(varName, 0);
		String s = ctx.ID().getText() + "0";
		// System.out.println(s);
		IDArray.add(s);
		return null;
	}

	public ArrayList getIDArray() {
		return IDArray;
	}
	public HashMap <String, Integer> getVar() {
		return Var;
	}
	public void setVar(HashMap <String, Integer> var) {
		Var = var;
	}

}

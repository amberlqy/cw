package example;

import java.util.ArrayList;
import java.util.HashMap;


import example.assignStmtVisitor;
import parser.SimpleCBaseVisitor;
import parser.SimpleCParser.AssertStmtContext;

public class assertStmtVisitor extends SimpleCBaseVisitor<Void>{
	private ArrayList<String> preSeq = new ArrayList<>();
	private HashMap<String, Integer> var;
	private String num = "0123456789"; 
	private ArrayList<String> assertExpr = new ArrayList<>();
	private assignStmtVisitor assignStmtVisitor = new assignStmtVisitor(var) ;
	
	public assertStmtVisitor (HashMap<String, Integer> var) {
		this.var = var;
	}
	
	 @Override
	 public Void  visitAssertStmt(AssertStmtContext ctx){
		 assignStmtVisitor.midToPre(ctx.expr().getText(),preSeq);
		 assertExpr.add(setAssertStmt());
		 return null;
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
	 
	 public String setAssertStmt(){
		 String assertStmtOne = null;
		for(int i =0 ; i < preSeq.size();i++){
			String s = preSeq.get(i);
			if(var.containsKey(s)){
				s = getLastIndexId(s);
			}
			if(num.contains(s)){
				for(int j = i + 1;j < preSeq.size() && num.contains(preSeq.get(j));j++){
					s = s + preSeq.get(j);
					i = j;
				}
			}
			if(s.equals("||"))
				s = "or";
			if(s.equals("&&"))
				s = "and";
			if(s.equals("!="))
				s = "not (=";
			if(s.equals("!"))
				s = "not";
			if(s.equals("=="))
				s = "=";
			if(assertStmtOne == null)
				assertStmtOne = s ;
			else
				assertStmtOne = assertStmtOne +  " " + s;
		}
		assertStmtOne = "(" + assertStmtOne + ")";
		return assertStmtOne;
	 }
	
public HashMap<String, Integer> getVar() {
		return var;
	}

	public void setVar(HashMap<String, Integer> var) {
		this.var = var;
	}
	
public ArrayList<String> getassertExpr(){
	return assertExpr;
}
}

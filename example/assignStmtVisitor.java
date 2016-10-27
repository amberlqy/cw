package example;

import java.util.*;
import parser.SimpleCParser;
import parser.SimpleCParser.VarDeclContext;
import parser.SimpleCBaseVisitor;
import example.declVisitor;

public class assignStmtVisitor extends SimpleCBaseVisitor<Void> {

	private ArrayList<String> preSeq = new ArrayList<>();
	private String assignStmt;
	private HashMap<String, Integer> var = new HashMap<String, Integer>();
	private String declaration;
	// private ArrayList<String> varList = new ArrayList<>();
	private String num = "0123456789";

	public assignStmtVisitor(HashMap<String, Integer> var) {
		this.var = var;
	}

	// @Override
	// public Void visitVarDecl(VarDeclContext ctx){
	// String s = ctx.ID().getText();
	// //System.out.println(s);
	// varList.add(s);
	// return null;
	//
	// }
	public String getassignStmt() {
		return assignStmt;
	}

	@Override
	public Void visitAssignStmt(SimpleCParser.AssignStmtContext ctx) {
		String left = ctx.ID().getText();
		String right = ctx.expr().getText();
		midToPre(right, preSeq);
		if (assignStmt == null) {
			assignStmt = setAssignStmt(var, left) + "\n";
		} else
			assignStmt = assignStmt + setAssignStmt(var, left) + "\n";
		return null;
	}

	public String getdecla() {
		return declaration;
	}

	public ArrayList getPreSeq() {
		return preSeq;
	}

	public void increaseVarIndex(String id) {
		if (var.containsKey(id)) {
			var.put(id, var.get(id) + 1);
		} else {
			var.put(id, 1);
		}
		if (declaration == null) {
			declaration = "(declare-fun " + getLastIndexId(id) + " () Int)" + "\n";
		} else {
			declaration = declaration + "(declare-fun " + getLastIndexId(id) + " () Int)" + "\n";
		}
	}

	public String getLastIndexId(String id) {
		String m;
		if (var.containsKey(id)) {
			int i = var.get(id);
			m = id + String.valueOf(i);
		} else
			m = id + "0";
		return m;
	}

	public String setAssignStmt(HashMap<String, Integer> var, String id) {
		String assignStmtOne = null;// one row
		for (int i = 0; i < preSeq.size(); i++) {
			String subpre = preSeq.get(i);
			for (int k = 0; k < subpre.length(); k++) {
				String s = String.valueOf(subpre.charAt(k));
				if (var.containsKey(s)) {
					s = getLastIndexId(s);
				}
				if (num.contains(s)) {
					for (int j = k + 1; j < subpre.length() && num.contains(String.valueOf(subpre.charAt(j))); j++) {
						s = s + String.valueOf(subpre.charAt(j));
						k = j;
					}
				}
				if (s.equals(":"))
					s = "ite";
				if (s.equals("?"))
					continue;
				if (s.equals("/"))
					s = "div";
				if (assignStmtOne == null)
					assignStmtOne = s;
				else
					assignStmtOne = assignStmtOne + " " + s;
			} // end of for k
		} // end of i
		increaseVarIndex(id);
		if (!assignStmtOne.contains(" ")) {
			assignStmtOne = "(assert (= " + getLastIndexId(id) + " " + assignStmtOne + "))";
		} else
			assignStmtOne = "(assert (= " + getLastIndexId(id) + " (" + assignStmtOne + ")))";
		return assignStmtOne;
	}

	public void midToPre(String midSeq, ArrayList<String> preSeq) {
		preSeq.clear();
		Stack<String> op = new Stack<>();
		Stack<String> result = new Stack<>();
		// String midSeq = ctx.expr().getText();
		for (int i = midSeq.length() - 1; i >= 0; i--) {
			String s = String.valueOf(midSeq.charAt(i));
			String next = null;
			if (i > 0) {
				next = String.valueOf(midSeq.charAt(i - 1));
			}
			switch (s) {
			case ")":
				op.push(s);
				result.push(s);
				// System.out.println(op.peek());
				break;
			case "(":
				if (op.peek().equals("/")) {
					String chushu = result.pop();
					String beichushu = result.pop();
					s = ":?(=" + beichushu + "0)" + chushu + "(/" + chushu + beichushu + ")";
					result.push(s);
					op.pop();
				} else
					result.push(op.pop());
				// result.push("(");
				String subpre = "(";
				while (!result.peek().equals(")")) {
					subpre = subpre + result.pop();
				}
				subpre = subpre + ")";
				result.push(subpre);
				op.pop();
				break;
			case "/":
			case "*":
			case "%":
				op.push(s);
				break;
			case "+":
			case "-":
				while (!op.isEmpty() && (op.peek().equals("/") || op.peek().equals("*") || op.peek().equals("%"))) {
					result.push(op.pop());
				}
				op.push(s);
				break;
			case ">":
			case "<":
				if (next.equals(">") || next.equals("<")) {
					// System.out.println("?");
					i--;
					s = next + s;
					while (!op.isEmpty() && (op.peek().equals("/") || op.peek().equals("*") || op.peek().equals("%")
							|| op.peek().equals("+") || op.peek().equals("-"))) {
						result.push(op.pop());
					}
				} else {
					while (!op.isEmpty() && (op.peek() == "/" || op.peek() == "*" || op.peek() == "%"
							|| op.peek() == "+" || op.peek() == "-" || op.peek() == ">>" || op.peek() == "<<")) {
						result.push(op.pop());
					}
				}
				op.push(s);
				break;
			case "=":
				i--;
				s = next + s;
				if (next == ">" || next == "<") { // >=,<=
					while (!op.isEmpty() && (op.peek() == "/" || op.peek() == "*" || op.peek() == "%"
							|| op.peek() == "+" || op.peek() == "-" || op.peek() == ">>" || op.peek() == "<<")) {
						result.push(op.pop());
					}
				} else { // !=,==
					while (!op.isEmpty() && (op.peek() == "/" || op.peek() == "*" || op.peek() == "%"
							|| op.peek() == "+" || op.peek() == "-" || op.peek() == ">>" || op.peek() == "<<"
							|| op.peek() == "<" || op.peek() == ">" || op.peek() == "<=" || op.peek() == ">=")) {
						result.push(op.pop());
					}
				}
				op.push(s);
				break;
			case "&":
			case "|":
			case "^":
				if (next.equals("|") || next.equals("&")) {
					i--;
					s = next + s;
				}
				while (!op.isEmpty() && (op.peek() == "/" || op.peek() == "*" || op.peek() == "%" || op.peek() == "+"
						|| op.peek() == "-" || op.peek() == ">>" || op.peek() == "<<" || op.peek() == "<"
						|| op.peek() == ">" || op.peek() == "<=" || op.peek() == ">=" || op.peek() == "=="
						|| op.peek() == "!=")) {
					result.push(op.pop());
				}
				op.push(s);
				break;
			case "?":
			case ":":
				while (!op.isEmpty() && (op.peek() == "/" || op.peek() == "*" || op.peek() == "%" || op.peek() == "+"
						|| op.peek() == "-" || op.peek() == ">>" || op.peek() == "<<" || op.peek() == "<"
						|| op.peek() == ">" || op.peek() == "<=" || op.peek() == ">=" || op.peek() == "=="
						|| op.peek() == "!=" || op.peek() == "&" || op.peek() == "|" || op.peek() == "^"
						|| op.peek() == "&&" || op.peek() == "||")) {
					result.push(op.pop());
				}
				op.push(s);
				break;
			default:
				result.push(s);
			}// end of switch
		} // end of for
		while (!op.isEmpty()) {
			if (op.peek().equals("/")) {
				String chushu = result.pop();
				String beichushu = result.pop();
				String s = ":?(=" + beichushu + "0)" + chushu + "(/" + chushu + beichushu + ")";
				result.push(s);
				op.pop();
			} else
				result.push(op.pop());
		}
		while (!result.isEmpty()) {
			if(num.contains(result.peek())) {
				String n = result.pop();
				while(!result.isEmpty() && num.contains(result.peek())){
					n = n + result.pop();
				}
				
				preSeq.add(n);
			}
//			else if(Character.isLetter(result.peek().charAt(0))) {
//				String n = result.pop();
//				while(!result.isEmpty() && Character.isLetter(result.peek().charAt(0))){
//					n = n + result.pop();
//				}
//				System.out.println(n);
//				preSeq.add(n);
//			}
			else
				preSeq.add(result.pop());
		}
		// return pre;
	}

	public HashMap<String, Integer> getMap() {
		return var;
	}
}

package com.paracamplus.tilp1.parser.ilpml;

import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.paracamplus.ilp1.interfaces.IASTblock;
import com.paracamplus.ilp1.interfaces.IASTexpression;
import com.paracamplus.tilp1.interfaces.IASTfactory;
import com.paracamplus.tilp1.typer.interfaces.IType;
import com.paracamplus.ilp1.interfaces.IASTvariable;

import antlr4.TypedILPMLgrammar1Listener;
import antlr4.TypedILPMLgrammar1Parser.ExprContext;
import antlr4.TypedILPMLgrammar1Parser.AlternativeContext;
import antlr4.TypedILPMLgrammar1Parser.BinaryContext;
import antlr4.TypedILPMLgrammar1Parser.BindingContext;
import antlr4.TypedILPMLgrammar1Parser.ConstFalseContext;
import antlr4.TypedILPMLgrammar1Parser.ConstFloatContext;
import antlr4.TypedILPMLgrammar1Parser.ConstIntegerContext;
import antlr4.TypedILPMLgrammar1Parser.ConstStringContext;
import antlr4.TypedILPMLgrammar1Parser.ConstTrueContext;
import antlr4.TypedILPMLgrammar1Parser.FuntionTypeContext;
import antlr4.TypedILPMLgrammar1Parser.InvocationContext;
import antlr4.TypedILPMLgrammar1Parser.ProgContext;
import antlr4.TypedILPMLgrammar1Parser.SequenceContext;
import antlr4.TypedILPMLgrammar1Parser.TypeContext;
import antlr4.TypedILPMLgrammar1Parser.Type_Context;
import antlr4.TypedILPMLgrammar1Parser.UnaryContext;
import antlr4.TypedILPMLgrammar1Parser.VariableContext;

public class ILPMLListener implements TypedILPMLgrammar1Listener {
	protected IASTfactory factory;

	public ILPMLListener(IASTfactory factory) {
		this.factory = factory;
	}

	@Override
	public void exitFuntionType(FuntionTypeContext ctx) {
		IType[] argsType = new IType[ctx.typeParam.size()];
		int index = 0;
		for (Type_Context typeC : ctx.typeParam) {
			argsType[index] = typeC.node;
			index++;
		}
		ctx.node = factory.newFunctionType(argsType, ctx.exitType.node);
	}

	@Override
	public void exitType(TypeContext ctx) {
		ctx.node = factory.newType(ctx.type.getText());
	}

	@Override
	public void exitBinding(BindingContext ctx) {
		ctx.node = factory.newBlock(
				toBindings(ctx.vars, ctx.vals, ctx.types/*
														 * modification tilp1
														 */), ctx.body.node);
	}

	protected IASTblock.IASTbinding[] toBindings(List<Token> vars,
			List<ExprContext> exprs, List<Type_Context> types) {
		if (vars == null)
			return new IASTblock.IASTbinding[0];
		/* par construction, vars et ctxs ont la même taille */
		assert (vars.size() == exprs.size());
		IASTblock.IASTbinding[] r = new IASTblock.IASTbinding[exprs.size()];
		int pos = 0;
		for (Token v : vars) {
			r[pos] = factory.newBinding(
					factory.newTypedVariable(v.getText(), types.get(pos).node/*
																		 * modification
																		 * tilp1
																		 */),
					exprs.get(pos).node);
			pos++;
		}
		return r;
	}

	/*
	 * OLD ilp1
	 */

	@Override
	public void exitVariable(VariableContext ctx) {
		ctx.node = factory.newVariable(ctx.getText());
	}

	@Override
	public void exitInvocation(InvocationContext ctx) {
		ctx.node = factory.newInvocation(ctx.fun.node, toExpressions(ctx.args));
	}

	@Override
	public void exitConstFloat(ConstFloatContext ctx) {
		ctx.node = factory.newFloatConstant(ctx.floatConst.getText());
	}

	@Override
	public void exitConstInteger(ConstIntegerContext ctx) {
		ctx.node = factory.newIntegerConstant(ctx.intConst.getText());
	}

	@Override
	public void exitAlternative(AlternativeContext ctx) {
		ctx.node = factory.newAlternative(ctx.condition.node,
				ctx.consequence.node, (ctx.alternant == null ? null
						: ctx.alternant.node));
	}

	@Override
	public void exitSequence(SequenceContext ctx) {
		ctx.node = factory.newSequence(toExpressions(ctx.exprs));
	}

	@Override
	public void exitConstFalse(ConstFalseContext ctx) {
		ctx.node = factory.newBooleanConstant("false");
	}

	@Override
	public void exitProg(ProgContext ctx) {
		IASTexpression e = factory.newSequence(toExpressions(ctx.exprs));
		ctx.node = factory.newProgram(e);
	}

	@Override
	public void exitUnary(UnaryContext ctx) {
		ctx.node = factory.newUnaryOperation(
				factory.newOperator(ctx.op.getText()), ctx.arg.node);
	}

	@Override
	public void exitConstTrue(ConstTrueContext ctx) {
		ctx.node = factory.newBooleanConstant("true");
	}

	@Override
	public void exitConstString(ConstStringContext ctx) {
		/*
		 * On enlève le " initial et final, et on interprète les codes
		 * d'échapement \n, \r, \t, \"
		 */
		String s = ctx.getText();
		StringBuilder buf = new StringBuilder();
		for (int i = 1; i < s.length() - 1; i++) {
			if (s.charAt(i) == '\\' && i < s.length() - 2) {
				switch (s.charAt(i + 1)) {
				case 'n':
					buf.append('\n');
					i++;
					break;
				case 'r':
					buf.append('\r');
					i++;
					break;
				case 't':
					buf.append('\t');
					i++;
					break;
				case '"':
					buf.append('\"');
					i++;
					break;
				default:
					buf.append(s.charAt(i));
				}
			} else
				buf.append(s.charAt(i));
		}
		ctx.node = factory.newStringConstant(buf.toString());
	}

	@Override
	public void exitBinary(BinaryContext ctx) {
		ctx.node = factory.newBinaryOperation(
				factory.newOperator(ctx.op.getText()), ctx.arg1.node,
				ctx.arg2.node);
	}

	/* Utilitaires de conversion ANTLR vers AST */

	protected IASTexpression[] toExpressions(List<ExprContext> ctxs) {
		if (ctxs == null)
			return new IASTexpression[0];
		IASTexpression[] r = new IASTexpression[ctxs.size()];
		int pos = 0;
		for (ExprContext e : ctxs) {
			r[pos++] = e.node;
		}
		return r;
	}

	protected IASTvariable[] toVariables(List<Token> vars, boolean addSelf) {
		if (vars == null)
			return new IASTvariable[0];
		IASTvariable[] r = new IASTvariable[vars.size() + (addSelf ? 1 : 0)];
		int pos = 0;
		if (addSelf) {
			// Les déclarations de méthodes ont une variable "self" implicite
			r[pos++] = factory.newVariable("self");
		}
		for (Token v : vars) {
			r[pos++] = factory.newVariable(v.getText());
		}
		return r;
	}

	protected String[] toStrings(List<Token> vars) {
		if (vars == null)
			return new String[0];
		String[] r = new String[vars.size()];
		int pos = 0;
		for (Token v : vars) {
			r[pos++] = v.getText();
		}
		return r;
	}

	@Override
	public void enterEveryRule(ParserRuleContext arg0) {
	}

	@Override
	public void exitEveryRule(ParserRuleContext arg0) {
	}

	@Override
	public void visitErrorNode(ErrorNode arg0) {
	}

	@Override
	public void visitTerminal(TerminalNode arg0) {
	}

	@Override
	public void enterConstInteger(ConstIntegerContext ctx) {
	}

	@Override
	public void enterProg(ProgContext ctx) {
	}

	@Override
	public void enterConstFloat(ConstFloatContext ctx) {
	}

	@Override
	public void enterVariable(VariableContext ctx) {
	}

	@Override
	public void enterBinary(BinaryContext ctx) {
	}

	@Override
	public void enterAlternative(AlternativeContext ctx) {
	}

	@Override
	public void enterConstFalse(ConstFalseContext ctx) {
	}

	@Override
	public void enterSequence(SequenceContext ctx) {
	}

	@Override
	public void enterConstTrue(ConstTrueContext ctx) {
	}

	@Override
	public void enterBinding(BindingContext ctx) {
	}

	@Override
	public void enterConstString(ConstStringContext ctx) {
	}

	@Override
	public void enterUnary(UnaryContext ctx) {
	}

	@Override
	public void enterInvocation(InvocationContext ctx) {
	}

	@Override
	public void enterFuntionType(FuntionTypeContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enterType(TypeContext ctx) {
		// TODO Auto-generated method stub

	}

}

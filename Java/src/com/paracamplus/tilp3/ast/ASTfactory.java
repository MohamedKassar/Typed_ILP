package com.paracamplus.tilp3.ast;

import com.paracamplus.ilp1.interfaces.IASTexpression;
import com.paracamplus.ilp1.interfaces.IASTvariable;
import com.paracamplus.ilp2.interfaces.IASTfunctionDefinition;
import com.paracamplus.ilp3.ast.ASTcodefinitions;
import com.paracamplus.ilp3.ast.ASTprogram;
import com.paracamplus.ilp3.interfaces.IASTlambda;
import com.paracamplus.ilp3.interfaces.IASTnamedLambda;
import com.paracamplus.ilp3.interfaces.IASTprogram;
import com.paracamplus.tilp1.interfaces.IASTtypedVariable;
import com.paracamplus.tilp1.typer.interfaces.IFunctionType;
import com.paracamplus.tilp3.interfaces.IASTfactory;
import com.paracamplus.tilp3.interfaces.IASTtypedLambda;
import com.paracamplus.tilp3.interfaces.IASTtypedNamedLambda;

public class ASTfactory extends com.paracamplus.tilp2.ast.ASTfactory implements
		IASTfactory {

	@Override
	public IASTprogram newProgram(IASTfunctionDefinition[] functions,
			IASTexpression expression) {
		return new ASTprogram(functions, expression);
	}

	@Override
	public IASTexpression newCodefinitions(IASTnamedLambda[] functions,
			IASTexpression body) {
		return new ASTcodefinitions(functions, body);
	}

	@Override
	public IASTtypedLambda newTypedLambda(IASTtypedVariable[] variables,
			IASTexpression body, IFunctionType type) {
		return new ASTtypedLambda(variables, body, type);
	}

	@Override
	public IASTtypedNamedLambda newTypedNamedLambda(
			IASTtypedVariable functionVariable, IASTtypedVariable[] variables,
			IASTexpression body, IFunctionType type) {
		return new ASTtypedNamedLambda(functionVariable, variables, body, type);
	}

	@Deprecated
	@Override
	public IASTexpression newTry(IASTexpression body, IASTlambda catcher,
			IASTexpression finallyer) {
		throw new RuntimeException("Must not be called.");
	}

	@Deprecated
	@Override
	public IASTlambda newLambda(IASTvariable[] variables, IASTexpression body) {
		throw new RuntimeException("Must not be called.");
	}

	@Deprecated
	@Override
	public IASTnamedLambda newNamedLambda(IASTvariable functionVariable,
			IASTvariable[] variables, IASTexpression body) {
		throw new RuntimeException("Must not be called.");
	}
}

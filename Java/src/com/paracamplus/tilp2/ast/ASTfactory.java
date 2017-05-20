package com.paracamplus.tilp2.ast;

import com.paracamplus.ilp1.interfaces.IASTexpression;
import com.paracamplus.ilp1.interfaces.IASTvariable;
import com.paracamplus.ilp2.ast.ASTassignment;
import com.paracamplus.ilp2.ast.ASTloop;
import com.paracamplus.ilp2.ast.ASTprogram;
import com.paracamplus.ilp2.interfaces.IASTfunctionDefinition;
import com.paracamplus.ilp2.interfaces.IASTprogram;
import com.paracamplus.tilp1.typer.interfaces.IType;
import com.paracamplus.tilp2.interfaces.IASTfactory;
import com.paracamplus.tilp2.interfaces.IASTtypedFunctionDefinition;
import com.paracamplus.tilp2.interfaces.IASTtypedVariableDefinition;

public class ASTfactory extends com.paracamplus.tilp1.ast.ASTfactory implements
		IASTfactory {

	@Override
	public IASTprogram newProgram(IASTfunctionDefinition[] functions,
			IASTexpression expression) {
		return new ASTprogram(functions, expression);
	}

	@Override
	public IASTexpression newLoop(IASTexpression condition, IASTexpression body) {
		return new ASTloop(condition, body);
	}

	@Override
	public IASTexpression newAssignment(IASTvariable variable,
			IASTexpression value) {
		return new ASTassignment(variable, value);
	}

	@Override
	public IASTtypedFunctionDefinition newTypedFunctionDefinition(
			IASTvariable functionVariable, IASTvariable[] variables,
			IASTexpression body) {
		return new ASTtypedFunctionDefinition(functionVariable, variables,
				body);
	}

	@Override
	public IASTtypedVariableDefinition newTypedVariableDefinition(
			IASTvariable variable, IASTexpression expression, IType type) {
		return new ASTtypedvariableDefinition(variable, expression, type);
	}

	@Deprecated
	@Override
	public IASTfunctionDefinition newFunctionDefinition(
			IASTvariable functionVariable, IASTvariable[] variables,
			IASTexpression body) {
		throw new RuntimeException("must not be called");
	}
}

package com.paracamplus.tilp3.ast;

import com.paracamplus.ilp1.interfaces.IASTexpression;
import com.paracamplus.ilp1.interfaces.IASTvariable;
import com.paracamplus.tilp1.interfaces.IASTtypedVariable;
import com.paracamplus.tilp1.typer.interfaces.IFunctionType;
import com.paracamplus.tilp3.interfaces.IASTtypedNamedLambda;

public class ASTtypedNamedLambda extends ASTtypedLambda implements
		IASTtypedNamedLambda {

	private final IASTtypedVariable functionVariable;

	public ASTtypedNamedLambda(IASTtypedVariable functionVariable,
			IASTvariable[] variables, IASTexpression body, IFunctionType type) {
		super(variables, body, type);
		this.functionVariable = functionVariable;
	}

	@Override
	public IASTvariable getFunctionVariable() {
		return functionVariable;
	}

}

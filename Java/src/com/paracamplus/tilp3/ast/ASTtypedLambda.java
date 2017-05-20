package com.paracamplus.tilp3.ast;

import com.paracamplus.ilp1.interfaces.IASTexpression;
import com.paracamplus.ilp1.interfaces.IASTvariable;
import com.paracamplus.ilp3.ast.ASTlambda;
import com.paracamplus.tilp1.typer.interfaces.IFunctionType;
import com.paracamplus.tilp3.interfaces.IASTtypedLambda;

public class ASTtypedLambda extends ASTlambda implements IASTtypedLambda{

	private final IFunctionType type;
	
	public ASTtypedLambda(IASTvariable[] variables, IASTexpression body, IFunctionType type) {
		super(variables, body);
		this.type = type;
	}

	@Override
	public IFunctionType getType() {
		return type;
	}

}

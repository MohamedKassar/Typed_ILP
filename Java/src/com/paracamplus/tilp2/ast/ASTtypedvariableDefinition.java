package com.paracamplus.tilp2.ast;

import com.paracamplus.ilp1.interfaces.IASTexpression;
import com.paracamplus.ilp1.interfaces.IASTvariable;
import com.paracamplus.ilp2.ast.ASTassignment;
import com.paracamplus.tilp1.typer.interfaces.IType;
import com.paracamplus.tilp2.interfaces.IASTtypedVariableDefinition;

public class ASTtypedvariableDefinition extends ASTassignment implements IASTtypedVariableDefinition{

	private final IType type;
	public ASTtypedvariableDefinition(IASTvariable variable,
			IASTexpression expression, IType type) {
		super(variable, expression);
		this.type = type;
		
	}

	@Override
	public IType getType() {
		return type;
	}

}

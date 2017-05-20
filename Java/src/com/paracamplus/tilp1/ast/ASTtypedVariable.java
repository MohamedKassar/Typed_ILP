package com.paracamplus.tilp1.ast;

import com.paracamplus.tilp1.interfaces.IASTtypedVariable;
import com.paracamplus.tilp1.typer.interfaces.IType;


public class ASTtypedVariable extends com.paracamplus.ilp1.ast.ASTvariable implements IASTtypedVariable{

	private IType type;
	
	public ASTtypedVariable(String name, IType type) {
		super(name);
		this.type = type;
	}
	
	@Override
	public IType getType() {
		return type;
	}

}

package com.paracamplus.tilp2.ast;

import com.paracamplus.ilp1.interfaces.IASTexpression;
import com.paracamplus.ilp1.interfaces.IASTvariable;
import com.paracamplus.ilp2.ast.ASTfunctionDefinition;
import com.paracamplus.tilp1.interfaces.IASTtypedVariable;
import com.paracamplus.tilp1.typer.interfaces.IFunctionType;
import com.paracamplus.tilp2.interfaces.IASTtypedFunctionDefinition;

public class ASTtypedFunctionDefinition extends ASTfunctionDefinition implements
		IASTtypedFunctionDefinition {

	public ASTtypedFunctionDefinition(IASTvariable functionVariable,
			IASTvariable[] variables, IASTexpression body) {
		super(functionVariable, variables, body);
	}

	@Override
	public IFunctionType getType() {
		if(!(getFunctionVariable() instanceof IASTtypedVariable))
			throw new RuntimeException("Not typed function !");
		
		if(!(((IASTtypedVariable) getFunctionVariable()).getType() instanceof IFunctionType))
			throw new RuntimeException("Not function type!");
		
		return (IFunctionType)((IASTtypedVariable) getFunctionVariable()).getType();
	}

}

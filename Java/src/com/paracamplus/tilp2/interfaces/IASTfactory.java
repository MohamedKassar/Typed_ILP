package com.paracamplus.tilp2.interfaces;

import com.paracamplus.ilp1.interfaces.IASTexpression;
import com.paracamplus.ilp1.interfaces.IASTvariable;
import com.paracamplus.tilp1.typer.interfaces.IType;

public interface IASTfactory extends
		com.paracamplus.tilp1.interfaces.IASTfactory, com.paracamplus.ilp2.interfaces.IASTfactory{
	
	
	IASTtypedFunctionDefinition newTypedFunctionDefinition(
			IASTvariable functionVariable, IASTvariable[] variables,
			IASTexpression body);

	IASTtypedVariableDefinition newTypedVariableDefinition(IASTvariable variable,
			IASTexpression expression, IType type);
}

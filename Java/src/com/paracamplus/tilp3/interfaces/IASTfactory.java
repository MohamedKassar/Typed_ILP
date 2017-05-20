package com.paracamplus.tilp3.interfaces;

import com.paracamplus.ilp1.interfaces.IASTexpression;
import com.paracamplus.tilp1.interfaces.IASTtypedVariable;
import com.paracamplus.tilp1.typer.interfaces.IFunctionType;

public interface IASTfactory extends
		com.paracamplus.tilp2.interfaces.IASTfactory,
		com.paracamplus.ilp3.interfaces.IASTfactory {
	IASTtypedLambda newTypedLambda(IASTtypedVariable[] variables,
			IASTexpression body, IFunctionType type);

	IASTtypedNamedLambda newTypedNamedLambda(
			IASTtypedVariable functionVariable, IASTtypedVariable[] variables,
			IASTexpression body, IFunctionType type);
}

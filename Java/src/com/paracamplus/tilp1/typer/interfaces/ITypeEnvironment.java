package com.paracamplus.tilp1.typer.interfaces;

import com.paracamplus.ilp1.interfaces.IASTvariable;
import com.paracamplus.ilp1.interfaces.IEnvironment;
import com.paracamplus.tilp1.typer.exceptions.TypeCheckerException;

public interface ITypeEnvironment extends
		IEnvironment<IASTvariable, IType, TypeCheckerException> {
	
    @Override
    ITypeEnvironment extend(IASTvariable variable, IType value);
    
    @Override
    ITypeEnvironment getNext() throws TypeCheckerException;
}

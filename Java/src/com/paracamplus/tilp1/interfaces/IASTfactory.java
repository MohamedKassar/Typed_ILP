package com.paracamplus.tilp1.interfaces;

import com.paracamplus.tilp1.typer.interfaces.IType;

public interface IASTfactory extends com.paracamplus.ilp1.interfaces.IASTfactory{
	IType newType(String type);
	IType newFunctionType(IType[] argsType, IType exitType);
	IASTtypedVariable newTypedVariable(String name, IType type);
}

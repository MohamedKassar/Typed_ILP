package com.paracamplus.tilp2.interfaces;

import com.paracamplus.ilp2.interfaces.IASTfunctionDefinition;
import com.paracamplus.tilp1.typer.interfaces.IFunctionType;

public interface IASTtypedFunctionDefinition extends IASTfunctionDefinition{
	IFunctionType getType();
}

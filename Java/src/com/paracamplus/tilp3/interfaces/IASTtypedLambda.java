package com.paracamplus.tilp3.interfaces;

import com.paracamplus.ilp3.interfaces.IASTlambda;
import com.paracamplus.tilp1.typer.interfaces.IFunctionType;

public interface IASTtypedLambda extends IASTlambda {
	IFunctionType getType();
}

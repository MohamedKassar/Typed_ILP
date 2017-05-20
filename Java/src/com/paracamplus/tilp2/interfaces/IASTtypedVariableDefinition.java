package com.paracamplus.tilp2.interfaces;

import com.paracamplus.ilp2.interfaces.IASTassignment;
import com.paracamplus.tilp1.typer.interfaces.IType;

public interface IASTtypedVariableDefinition extends IASTassignment{
	IType getType();
}

package com.paracamplus.tilp1.typer.interfaces;

import com.paracamplus.tilp1.typer.exceptions.TypeCheckerException;

public interface IFunctionType extends IType{
	IType[] getDefaultArgsTypes();
	IType getExitType();
	boolean argsAcceptType(IType[] args) throws TypeCheckerException;
}

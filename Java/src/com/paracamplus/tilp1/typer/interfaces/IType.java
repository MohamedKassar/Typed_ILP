package com.paracamplus.tilp1.typer.interfaces;

import com.paracamplus.tilp1.typer.exceptions.TypeCheckerException;

public interface IType {
	public String toString();

	/*
	 * The order is very important type1.isOfType(type2) can be != to
	 * type2.isOfType(type1)
	 */
	boolean isOfType(IType type) throws TypeCheckerException;
}

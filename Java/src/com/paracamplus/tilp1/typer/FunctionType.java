package com.paracamplus.tilp1.typer;

import com.paracamplus.tilp1.typer.exceptions.TypeCheckerException;
import com.paracamplus.tilp1.typer.interfaces.IFunctionType;
import com.paracamplus.tilp1.typer.interfaces.IType;

public class FunctionType implements IFunctionType {

	private final IType exitType;
	private final IType[][] argsTypes;

	/*
	 * default type must be put in the first position
	 */
	public FunctionType(IType exitType, IType[]... argsTypes) {
		this.exitType = exitType;
		this.argsTypes = argsTypes;
	}

	@Override
	public IType[] getDefaultArgsTypes() {
		return argsTypes[0];
	}

	@Override
	public IType getExitType() {
		return exitType;
	}

	@Override
	public String toString() {
		String temp = "(";
		for (int i = 0; i < argsTypes[0].length; i++) {
			temp += argsTypes[0][i].toString();
			if (i < argsTypes[0].length - 1) {
				temp += " * ";
			}
		}
		temp += ")->" + exitType.toString();
		return temp;
	}

	@Override
	public boolean argsAcceptType(IType[] args) throws TypeCheckerException {
		boolean accepted;
		for (int i = 0; i < argsTypes.length; i++) {
			if (argsTypes[i].length == args.length) {
				accepted = true;
				for (int j = 0; j < args.length; j++) {
					if (!args[j].isOfType(argsTypes[i][j])) {
						accepted = false;
					}
				}
				if (accepted)
					return true;
			}
		}
		if (argsTypes[0].length != args.length)
			throw new TypeCheckerException("Illegal args number.");

		return false;
	}

	@Override
	public boolean isOfType(IType type) throws TypeCheckerException {
		if (type == this)
			return true;
		if (type instanceof FunctionType) {

			/*
			 * we suppose that type have just one args possibility
			 */
			boolean valid = false;
			for (IType[] aT : ((FunctionType) type).argsTypes) {
				if (argsAcceptType(aT)) {
					valid = true;
					break;
				}
			}

			if (valid && exitType.isOfType(((FunctionType) type).exitType))
				return true;
			
		}
		return false;
	}

}

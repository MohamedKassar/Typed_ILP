package com.paracamplus.tilp1.typer;

import com.paracamplus.ilp1.interfaces.IASTvariable;
import com.paracamplus.tilp1.typer.exceptions.TypeCheckerException;
import com.paracamplus.tilp1.typer.interfaces.IType;
import com.paracamplus.tilp1.typer.interfaces.ITypeEnvironment;

public class TypeEnvironment implements ITypeEnvironment {

	private final IASTvariable variable;
	private final IType type;
	private final ITypeEnvironment next;

	public TypeEnvironment(IASTvariable variable, IType type,
			ITypeEnvironment next) {

		this.variable = variable;
		this.type = type;
		this.next = next;
	}

	@Override
	public boolean isPresent(IASTvariable key) {
		if (key.getName().equals(getKey().getName())) {
			return true;
		} else {
			return getNext().isPresent(key);
		}
	}

	@Override
	public IASTvariable getKey() {
		return variable;
	}

	@Override
	public IType getValue(IASTvariable key) throws TypeCheckerException {
		if (key.getName().equals(getKey().getName())) {
			return type;
		} else {
			return getNext().getValue(key);
		}
	}

	@Override
	public void update(IASTvariable key, IType value)
			throws TypeCheckerException {
		throw new TypeCheckerException("Never reached.");
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public ITypeEnvironment extend(IASTvariable variable, IType value) {
		return new TypeEnvironment(variable, value, this);
	}

	@Override
	public ITypeEnvironment getNext() {
		return next;
	}

	@Override
	public String toString() {
		return "(" + variable.getName() + ", " + type + ")"
				+ ((next == null) ? "" : ", " + next.toString());
	}

}

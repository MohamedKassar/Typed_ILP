package com.paracamplus.tilp1.typer;

import com.paracamplus.ilp1.interfaces.IASTvariable;
import com.paracamplus.tilp1.typer.exceptions.TypeCheckerException;
import com.paracamplus.tilp1.typer.interfaces.IType;
import com.paracamplus.tilp1.typer.interfaces.ITypeEnvironment;

public class EmptyTypeEnvironment implements ITypeEnvironment{

	@Override
	public boolean isPresent(IASTvariable key) {
		return false;
	}

	@Override
	public IASTvariable getKey() throws TypeCheckerException {
		throw new TypeCheckerException("Really empty environment");
	}

	@Override
	public IType getValue(IASTvariable key) throws TypeCheckerException {
		throw new TypeCheckerException("Variable " + key.getName() + " is not typed yet!");
	}

	@Override
	public void update(IASTvariable key, IType value) throws TypeCheckerException {
		throw new RuntimeException("Update must not be called.");		
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public ITypeEnvironment extend(IASTvariable variable, IType value) {
		return new TypeEnvironment(variable, value, this);
	}

	@Override
	public ITypeEnvironment getNext() throws TypeCheckerException {
		 throw new TypeCheckerException("Completely empty environment");
	}

}

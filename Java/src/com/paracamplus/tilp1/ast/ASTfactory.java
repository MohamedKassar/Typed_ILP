package com.paracamplus.tilp1.ast;

import com.paracamplus.tilp1.interfaces.IASTfactory;
import com.paracamplus.tilp1.interfaces.IASTtypedVariable;
import com.paracamplus.tilp1.typer.FunctionType;
import com.paracamplus.tilp1.typer.PrimitiveType;
import com.paracamplus.tilp1.typer.interfaces.IType;

public class ASTfactory extends com.paracamplus.ilp1.ast.ASTfactory implements
		IASTfactory {

	@Override
	public IType newType(String type) {
		if (type.equals("unit")) {
			return PrimitiveType.UNIT;
		}
		if (type.equals("float")) {
			return PrimitiveType.FLOAT;
		}
		if (type.equals("int")) {
			return PrimitiveType.INT;
		}
		if (type.equals("string")) {
			return PrimitiveType.STRING;
		}
		if (type.equals("bool")) {
			return PrimitiveType.BOOL;
		}
		throw new RuntimeException("Must not be raised.");
	}

	@Override
	public IType newFunctionType(IType[] argsType, IType exitType) {
		return new FunctionType(exitType, argsType);
	}

	@Override
	public IASTtypedVariable newTypedVariable(String name, IType type) {
		return new ASTtypedVariable(name, type);
	}

}

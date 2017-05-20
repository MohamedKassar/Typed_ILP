package com.paracamplus.tilp1.trash;

import java.io.Writer;

import com.paracamplus.ilp1.interpreter.primitive.Newline;
import com.paracamplus.tilp1.typer.FunctionType;
import com.paracamplus.tilp1.typer.PrimitiveType;
import com.paracamplus.tilp1.typer.interfaces.IType;

public class TypedNewline extends Newline implements ITypedPrimitive {

	private final FunctionType type;

	public TypedNewline(Writer out) {
		super(out);
		type = new FunctionType(PrimitiveType.UNIT, new IType[] {});
	}

	@Override
	public FunctionType getType() {
		return type;
	}

}

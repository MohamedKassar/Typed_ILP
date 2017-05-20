package com.paracamplus.tilp1.trash;

import java.io.Writer;

import com.paracamplus.ilp1.interpreter.primitive.Print;
import com.paracamplus.tilp1.typer.FunctionType;
import com.paracamplus.tilp1.typer.PrimitiveType;
import com.paracamplus.tilp1.typer.interfaces.IType;

public class TypedPrint extends Print implements ITypedPrimitive {

	private final FunctionType type;

	public TypedPrint(Writer out) {
		super(out);
		type = new FunctionType(PrimitiveType.UNIT,
				new IType[] { PrimitiveType.STRING },
				new IType[] { PrimitiveType.INT },
				new IType[] { PrimitiveType.BOOL },
				new IType[] { PrimitiveType.FLOAT });
	}

	@Override
	public FunctionType getType() {
		return type;
	}

}

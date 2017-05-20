package com.paracamplus.tilp1.typer;

import java.io.Writer;

import com.paracamplus.ilp1.interpreter.interfaces.IGlobalVariableEnvironment;
import com.paracamplus.tilp1.typer.interfaces.IType;

public class GlobalVariableStuff {
	public static void fillGlobalVariables(IGlobalVariableEnvironment env,
			Writer out) {
		env.addGlobalVariableValue("pi", PrimitiveType.FLOAT);
//		env.addGlobalVariableValue(new TypedPrint(out));
		env.addGlobalVariableValue("print", new FunctionType(
				PrimitiveType.UNIT, new IType[] { PrimitiveType.STRING },
				new IType[] { PrimitiveType.INT },
				new IType[] { PrimitiveType.BOOL },
				new IType[] { PrimitiveType.FLOAT }));
		env.addGlobalVariableValue("newline", new FunctionType(PrimitiveType.UNIT, new IType[] {}));

//		env.addGlobalVariableValue(new TypedNewline(out));
	}
}

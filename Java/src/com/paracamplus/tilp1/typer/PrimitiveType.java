package com.paracamplus.tilp1.typer;

import com.paracamplus.tilp1.typer.exceptions.TypeCheckerException;
import com.paracamplus.tilp1.typer.interfaces.IPrimitiveType;
import com.paracamplus.tilp1.typer.interfaces.IType;

public class PrimitiveType implements IPrimitiveType{
	
	public static final IType UNIT = new PrimitiveType("unit");
	public static final IType FLOAT = new PrimitiveType("float");
	public static final IType INT = new PrimitiveType("int");
	public static final IType BOOL = new PrimitiveType("bool");
	public static final IType STRING = new PrimitiveType("string");
	
	private final String type;
	
	public PrimitiveType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return type;
	}

	@Override
	public boolean isOfType(IType type) throws TypeCheckerException {
		if(type == this)
			return true;
		
		if(type instanceof PrimitiveType){
			if(((PrimitiveType) type).type.equals(this.type))
				return true;
			
			if(this.type.equals("int") && type.isOfType(FLOAT))
				return true;
		}
		
		return false;
	}
	
}

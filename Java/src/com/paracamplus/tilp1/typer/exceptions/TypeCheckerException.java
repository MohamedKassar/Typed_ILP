package com.paracamplus.tilp1.typer.exceptions;

@SuppressWarnings("serial")
public class TypeCheckerException extends Exception {
	public TypeCheckerException(String msg) {
		super(msg);
	}

	public TypeCheckerException(String operator, String argType, String expected) {
		super("\"" + operator + "\""
				+ " operator is indefined for the argument type : " + argType
				+ ", expected type is : " + expected + ".");
	}

	public TypeCheckerException(String operator, String argType) {
		super("\"" + operator + "\""
				+ " operator is indefined for the argument type : " + argType
				+ ".");
	}

	public TypeCheckerException(Exception e) {
		super(e);
	}
}

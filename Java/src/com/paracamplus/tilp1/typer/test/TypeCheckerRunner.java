package com.paracamplus.tilp1.typer.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import com.paracamplus.ilp1.interfaces.IASTprogram;
import com.paracamplus.ilp1.parser.ParseException;
import com.paracamplus.ilp1.parser.Parser;
import com.paracamplus.tilp1.parser.ilpml.ILPMLParser;
import com.paracamplus.tilp1.typer.TypeChecker;
import com.paracamplus.tilp1.typer.exceptions.TypeCheckerException;
import com.paracamplus.ilp1.tools.FileTool;

public class TypeCheckerRunner {

	protected Parser parser;

	public TypeCheckerRunner() {
		parser = new Parser();
	}

	/*
	 * public void setXMLParser(IXMLParser p) { parser.setXMLParser(p); }
	 */

	public void setILPMLParser(ILPMLParser p) {
		parser.setILPMLParser(p);
	}

	protected TypeChecker typeChecker;

	public void setTypeCheker(TypeChecker typeChecker) {
		this.typeChecker = typeChecker;
	}

	public void runTypeChaker(IASTprogram program) throws TypeCheckerException {
		if (typeChecker == null) {
			throw new TypeCheckerException("typeChecker not set");
		}
		try {
			result = typeChecker.checkTyping(program).toString();
			System.out.println("Type: " + result);
		} catch (TypeCheckerException e) {
			result = e.getMessage();
			System.out.println("Typing error: " + result);
		}

	}

	public void testFile(File file) throws ParseException, IOException,
			TypeCheckerException {
		System.err.println("Testing " + file.getAbsolutePath() + " ...");
		assertTrue(file.exists());
		IASTprogram program = parser.parse(file);
		runTypeChaker(program);
	}

	protected String result;

	public String getResult() {
		return result;
	}

	public void checkType(File file) throws IOException {
		String expectedResult = readExpectedType(file);
		String type = result;
		assertEquals("Comparing results", expectedResult, type.toString()
				.replaceAll(" ", ""));
	}

	public String readExpectedType(File file) throws IOException {
		File resultFile = FileTool.changeSuffix(file, "type");
		assertTrue(file.exists());
		return FileTool.slurpFile(resultFile).trim().replaceAll(" ", "");
	}

}

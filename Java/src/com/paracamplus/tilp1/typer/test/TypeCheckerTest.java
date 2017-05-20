package com.paracamplus.tilp1.typer.test;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.paracamplus.tilp1.ast.ASTfactory;
import com.paracamplus.tilp1.interfaces.IASTfactory;
import com.paracamplus.ilp1.interpreter.GlobalVariableEnvironment;
import com.paracamplus.tilp1.typer.GlobalVariableStuff;
import com.paracamplus.tilp1.typer.TypeChecker;
import com.paracamplus.tilp1.typer.exceptions.TypeCheckerException;
import com.paracamplus.ilp1.interpreter.interfaces.IGlobalVariableEnvironment;
import com.paracamplus.ilp1.interpreter.test.InterpreterRunner;
import com.paracamplus.ilp1.parser.ParseException;
import com.paracamplus.tilp1.parser.ilpml.ILPMLParser;

@RunWith(Parameterized.class)
public class TypeCheckerTest {

	protected static String[] samplesDirName = { "SamplesTILP1" };
	protected static String pattern = "tilp_[0123456789]*";
	protected File file;

	public TypeCheckerTest(File file) {
		this.file = file;
	}

	public void configureRunner(TypeCheckerRunner run)
			throws TypeCheckerException {
		// configuration du parseur
		IASTfactory factory = new ASTfactory();
		run.setILPMLParser(new ILPMLParser(factory));

		// configuration de l'interprète
		IGlobalVariableEnvironment gve = new GlobalVariableEnvironment();
		GlobalVariableStuff.fillGlobalVariables(gve, null);
		TypeChecker typeChecker = new TypeChecker(gve);
		run.setTypeCheker(typeChecker);
	}

	@Test
	public void processFile() throws ParseException, IOException,
			TypeCheckerException {
		TypeCheckerRunner run = new TypeCheckerRunner();
		configureRunner(run);
		run.testFile(file);
		run.checkType(file);
	}

	@Parameters(name = "{0}")
	public static Collection<File[]> data() throws Exception {
		return InterpreterRunner.getFileList(samplesDirName, pattern);
	}

}

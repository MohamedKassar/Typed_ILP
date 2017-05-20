package com.paracamplus.tilp3.typer.test;

import java.io.File;
import java.util.Collection;

import org.junit.runners.Parameterized.Parameters;

import com.paracamplus.ilp1.interpreter.GlobalVariableEnvironment;
import com.paracamplus.ilp1.interpreter.interfaces.IGlobalVariableEnvironment;
import com.paracamplus.ilp1.interpreter.test.InterpreterRunner;
import com.paracamplus.tilp1.typer.GlobalVariableStuff;
import com.paracamplus.tilp1.typer.exceptions.TypeCheckerException;
import com.paracamplus.tilp1.typer.test.TypeCheckerRunner;
import com.paracamplus.tilp3.ast.ASTfactory;
import com.paracamplus.tilp3.interfaces.IASTfactory;
import com.paracamplus.tilp3.parser.ilpml.ILPMLParser;
import com.paracamplus.tilp3.typer.TypeChecker;

public class TypeCheckerTest extends
		com.paracamplus.tilp2.typer.test.TypeCheckerTest {

	protected static String[] samplesDirName = {"xxxx" };//"SamplesTILP3", "SamplesTILP2", "SamplesTILP1" };

	public TypeCheckerTest(File file) {
		super(file);
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

	@Parameters(name = "{0}")
	public static Collection<File[]> data() throws Exception {
		return InterpreterRunner.getFileList(samplesDirName, pattern);
	}

}

package com.paracamplus.tilp2.typer;

import com.paracamplus.ilp2.interfaces.IASTprogram;
import com.paracamplus.ilp1.interfaces.IASTvariable;
import com.paracamplus.ilp1.interpreter.interfaces.IGlobalVariableEnvironment;
import com.paracamplus.ilp2.interfaces.IASTassignment;
import com.paracamplus.ilp2.interfaces.IASTfunctionDefinition;
import com.paracamplus.ilp2.interfaces.IASTloop;
import com.paracamplus.ilp2.interfaces.IASTvisitor;
import com.paracamplus.tilp1.interfaces.IASTtypedVariable;
import com.paracamplus.tilp1.typer.EmptyTypeEnvironment;
import com.paracamplus.tilp1.typer.PrimitiveType;
import com.paracamplus.tilp1.typer.exceptions.TypeCheckerException;
import com.paracamplus.tilp1.typer.interfaces.IType;
import com.paracamplus.tilp1.typer.interfaces.ITypeEnvironment;
import com.paracamplus.tilp2.interfaces.IASTtypedFunctionDefinition;
import com.paracamplus.tilp2.interfaces.IASTtypedVariableDefinition;

public class TypeChecker extends com.paracamplus.tilp1.typer.TypeChecker
		implements IASTvisitor<IType, ITypeEnvironment, TypeCheckerException> {

	public TypeChecker(IGlobalVariableEnvironment globalVariableEnvironment) {
		super(globalVariableEnvironment);
	}

	public IType checkTyping(IASTprogram program) throws TypeCheckerException {//TODO : check me
		IASTfunctionDefinition[] funDefs = program.getFunctionDefinitions();
		for (int i = 0; i < funDefs.length; i++) {
			if (!(funDefs[i] instanceof IASTtypedFunctionDefinition))
				throw new TypeCheckerException("Function \""
						+ funDefs[i].getName() + "\" is not typed.");
			IASTtypedFunctionDefinition temp = (IASTtypedFunctionDefinition) funDefs[i];
			globalVariableEnvironment.addGlobalVariableValue(temp.getName(),
					temp.getType());
		}
		for (int i = 0; i < funDefs.length; i++) {
			ITypeEnvironment funEnv = new EmptyTypeEnvironment();
			for (IASTvariable var : funDefs[i].getVariables()) {
				if (!(var instanceof IASTtypedVariable)) {
					throw new TypeCheckerException("In function \""
							+ funDefs[i].getName() + "\" variable \""
							+ var.getName() + "\" is not typed.");
				}
				IASTtypedVariable tVar = (IASTtypedVariable) var;
				funEnv = funEnv.extend(tVar, tVar.getType());
			}
			IType funRealExitType = funDefs[i].getBody().accept(this, funEnv);
			IASTtypedFunctionDefinition temp = (IASTtypedFunctionDefinition) funDefs[i];
			if (!funRealExitType.isOfType(temp.getType().getExitType()))
				throw new TypeCheckerException("In function \""
						+ funDefs[i].getName() + "\" exit type is \""
						+ funRealExitType + "\" but expected type was \""
						+ temp.getType().getExitType() + "\".");

		}
		ITypeEnvironment env = new EmptyTypeEnvironment();
		return program.getBody().accept(this, env);
	}

	@Override
	public IType checkTyping(com.paracamplus.ilp1.interfaces.IASTprogram program)
			throws TypeCheckerException {
		return this.checkTyping((IASTprogram) program);
	}

	@Override
	public IType visit(IASTassignment iast, ITypeEnvironment env)
			throws TypeCheckerException {
		IType varType, exprType;

		if (iast instanceof IASTtypedVariableDefinition) {
			// TODO to check : j'accepte la multi definition une variable
			IASTtypedVariableDefinition temp = (IASTtypedVariableDefinition) iast;
			exprType = temp.getExpression().accept(this, env);
			varType = temp.getType();
		} else {// iast instanceof IASTassignment
			varType = iast.getVariable().accept(this, env);
			exprType = iast.getExpression().accept(this, env);
		}
		if (!exprType.isOfType(varType))
			throw new TypeCheckerException(
					"Assignement error, expression type is\"" + exprType
							+ "\" but expected type was \"" + varType + "\".");

		if (iast instanceof IASTtypedVariableDefinition)
			// TODO to check : j'accepte la multi definition une variable
			globalVariableEnvironment.addGlobalVariableValue(iast.getVariable()
					.getName(), varType);

		return PrimitiveType.UNIT;
	}

	@Override
	public IType visit(IASTloop iast, ITypeEnvironment env)
			throws TypeCheckerException {
		IType conditionType = iast.getCondition().accept(this, env);
		if (!conditionType.isOfType(PrimitiveType.BOOL)) {
			throw new TypeCheckerException("Loop condition not boolean.");
		}
		IType loopType = iast.getBody().accept(this, env);
		if (!loopType.isOfType(PrimitiveType.UNIT))
			throw new TypeCheckerException("Loop must be unit.");
		return PrimitiveType.UNIT;
	}

}

package com.paracamplus.tilp3.typer;

import com.paracamplus.ilp1.interfaces.IASTvariable;
import com.paracamplus.ilp1.interpreter.interfaces.IGlobalVariableEnvironment;
import com.paracamplus.ilp3.interfaces.IASTcodefinitions;
import com.paracamplus.ilp3.interfaces.IASTlambda;
import com.paracamplus.ilp3.interfaces.IASTnamedLambda;
import com.paracamplus.ilp3.interfaces.IASTtry;
import com.paracamplus.ilp3.interfaces.IASTvisitor;
import com.paracamplus.tilp1.interfaces.IASTtypedVariable;
import com.paracamplus.tilp1.typer.PrimitiveType;
import com.paracamplus.tilp1.typer.exceptions.TypeCheckerException;
import com.paracamplus.tilp1.typer.interfaces.IType;
import com.paracamplus.tilp1.typer.interfaces.ITypeEnvironment;
import com.paracamplus.tilp3.interfaces.IASTtypedLambda;
import com.paracamplus.tilp3.interfaces.IASTtypedNamedLambda;

public class TypeChecker extends com.paracamplus.tilp2.typer.TypeChecker
		implements IASTvisitor<IType, ITypeEnvironment, TypeCheckerException> {

	public TypeChecker(IGlobalVariableEnvironment globalVariableEnvironment) {
		super(globalVariableEnvironment);
	}

	@Override
	public IType visit(IASTcodefinitions iast, ITypeEnvironment env)
			throws TypeCheckerException {
		IASTnamedLambda[] funcs = iast.getFunctions();
		for (IASTnamedLambda iasTnamedLambda : funcs) {
			if (!(iasTnamedLambda instanceof IASTtypedNamedLambda))
				throw new TypeCheckerException("Local function must be typed.");
			IASTtypedNamedLambda temp = (IASTtypedNamedLambda) iasTnamedLambda;
			env = env.extend(temp.getFunctionVariable(), temp.getType());
		}
		for (IASTnamedLambda iasTnamedLambda : funcs) {
			iasTnamedLambda.accept(this, env);
		}
		return iast.getBody().accept(this, env);
	}

	@Override
	public IType visit(IASTlambda iast, ITypeEnvironment env)
			throws TypeCheckerException {

		for (IASTvariable var : iast.getVariables()) {
			if (!(var instanceof IASTtypedVariable)) {
				throw new TypeCheckerException("In lambda, variable \""
						+ var.getName() + "\" is not typed.");
			}

			IASTtypedVariable tVar = (IASTtypedVariable) var;
			env = env.extend(tVar, tVar.getType());
		}
		IType funRealExitType = iast.getBody().accept(this, env);
		IASTtypedLambda temp = (IASTtypedLambda) iast;

		if (!funRealExitType.isOfType(temp.getType().getExitType())) {
			String msg;
			if (temp instanceof IASTnamedLambda) {
				msg = "In local function \""
						+ ((IASTnamedLambda) temp).getFunctionVariable()
								.getName() + "\" exit type is \""
						+ funRealExitType + "\" but expected type was \""
						+ temp.getType().getExitType() + "\".";
			} else {
				msg = "In lambda, exit type is \"" + funRealExitType
						+ "\" but expected type was \""
						+ temp.getType().getExitType() + "\".";
			}
			throw new TypeCheckerException(msg);
		}
		if (temp instanceof IASTnamedLambda)
			return PrimitiveType.UNIT;
		
		return temp.getType();
	}

	@Deprecated
	@Override
	public IType visit(IASTtry iast, ITypeEnvironment env)
			throws TypeCheckerException {
		throw new RuntimeException("Must not be called.");
	}

}

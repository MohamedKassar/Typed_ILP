package com.paracamplus.tilp1.typer;


import com.paracamplus.ilp1.interfaces.IASTalternative;
import com.paracamplus.ilp1.interfaces.IASTbinaryOperation;
import com.paracamplus.ilp1.interfaces.IASTblock;
import com.paracamplus.ilp1.interfaces.IASTboolean;
import com.paracamplus.ilp1.interfaces.IASTexpression;
import com.paracamplus.ilp1.interfaces.IASTfloat;
import com.paracamplus.ilp1.interfaces.IASTinteger;
import com.paracamplus.ilp1.interfaces.IASTinvocation;
import com.paracamplus.ilp1.interfaces.IASToperator;
import com.paracamplus.ilp1.interfaces.IASTprogram;
import com.paracamplus.ilp1.interfaces.IASTsequence;
import com.paracamplus.ilp1.interfaces.IASTstring;
import com.paracamplus.ilp1.interfaces.IASTunaryOperation;
import com.paracamplus.ilp1.interfaces.IASTvariable;
import com.paracamplus.ilp1.interfaces.IASTvisitor;
import com.paracamplus.ilp1.interpreter.interfaces.IGlobalVariableEnvironment;
import com.paracamplus.tilp1.interfaces.IASTtypedVariable;
import com.paracamplus.tilp1.trash.ITypedPrimitive;
import com.paracamplus.tilp1.typer.EmptyTypeEnvironment;
import com.paracamplus.tilp1.typer.exceptions.TypeCheckerException;
import com.paracamplus.tilp1.typer.interfaces.IType;
import com.paracamplus.tilp1.typer.interfaces.ITypeEnvironment;

public class TypeChecker implements
		IASTvisitor<IType, ITypeEnvironment, TypeCheckerException> {

	protected IGlobalVariableEnvironment globalVariableEnvironment;

	public TypeChecker(IGlobalVariableEnvironment globalVariableEnvironment) {
		this.globalVariableEnvironment = globalVariableEnvironment;
	}

	public IType checkTyping(IASTprogram program) throws TypeCheckerException {
		ITypeEnvironment env = new EmptyTypeEnvironment();
		return program.getBody().accept(this, env);
	}

	@Override
	public IType visit(IASTalternative iast, ITypeEnvironment env)
			throws TypeCheckerException {
		IType conditionType = iast.getCondition().accept(this, env);
		if (!conditionType.isOfType(PrimitiveType.BOOL)) {
			throw new TypeCheckerException("Alternative condition not boolean.");
		}
		IType alternantType = (iast.getAlternant() == null) ? null : iast
				.getAlternant().accept(this, env);
		IType consequenceType = (iast.getConsequence() == null) ? null : iast
				.getConsequence().accept(this, env);

		if (alternantType == null) {
			if (consequenceType.isOfType(PrimitiveType.UNIT))
				return PrimitiveType.UNIT;
			throw new TypeCheckerException(
					"In alternative without else block, consequence must be unit.");
		}

		if (alternantType != consequenceType)
			throw new TypeCheckerException("Alternative type error.");

		return alternantType;

	}

	@Override
	public IType visit(IASTbinaryOperation iast, ITypeEnvironment env)
			throws TypeCheckerException {
		IType leftOperandType = iast.getLeftOperand().accept(this, env);
		IType rightOperandType = iast.getRightOperand().accept(this, env);
		String operator = iast.getOperator().getName();
		if (leftOperandType.isOfType(PrimitiveType.UNIT)
				|| rightOperandType.isOfType(PrimitiveType.UNIT)) {
			throw new TypeCheckerException("Operation \"" + operator
					+ "\" impossible for unit type.");
		}
		if (operator.equals("&") || operator.equals("|")
				|| operator.equals("^")) {
			if (!leftOperandType.isOfType(PrimitiveType.BOOL)
					|| !rightOperandType.isOfType(PrimitiveType.BOOL)) {
				throw new TypeCheckerException(operator, leftOperandType
						+ " and " + rightOperandType, "boolean");
			}

			return PrimitiveType.BOOL;
		}
		if (operator.equals("==") || operator.equals("!=")
				|| operator.equals(">=") || operator.equals(">")
				|| operator.equals("<=") || operator.equals("<")) {
			//!(float*float, int*float, float*int, int*int)
			if(!(leftOperandType.isOfType(PrimitiveType.FLOAT) && rightOperandType.isOfType(PrimitiveType.FLOAT))){
//			if ((!leftOperandType.isOfType(PrimitiveType.FLOAT) && !leftOperandType.isOfType(PrimitiveType.INT))TODO
//					|| (!rightOperandType.isOfType(PrimitiveType.FLOAT) && !rightOperandType.isOfType(PrimitiveType.INT))) {
				throw new TypeCheckerException(operator, "not numeric",
						"int or float");
			}
			return PrimitiveType.BOOL;
		}

		if (operator.equals("+")) {
			if (leftOperandType.isOfType(PrimitiveType.STRING)
					|| rightOperandType.isOfType( PrimitiveType.STRING)) {
				return PrimitiveType.STRING;
			}

			if (leftOperandType.isOfType(PrimitiveType.INT)
					&& rightOperandType.isOfType(PrimitiveType.INT)) {
				return PrimitiveType.INT;
			}
			
			//float*float, int*float, float*int (note: int*int already treated)
			if(leftOperandType.isOfType(PrimitiveType.FLOAT) && rightOperandType.isOfType(PrimitiveType.FLOAT)){
//			if ((leftOperandType.isOfType(PrimitiveType.INT) || leftOperandType.isOfType(PrimitiveType.FLOAT))
//					&& (rightOperandType.isOfType(PrimitiveType.INT) || rightOperandType.isOfType(PrimitiveType.FLOAT))) {
				return PrimitiveType.FLOAT;
			}

			throw new TypeCheckerException("+", leftOperandType + " and "
					+ rightOperandType);
		}

		if (operator.equals("%")) {

			if (leftOperandType.isOfType(PrimitiveType.INT)
					&& rightOperandType.isOfType(PrimitiveType.INT)) {
				return PrimitiveType.INT;
			}

			throw new TypeCheckerException("%", leftOperandType + " and "
					+ rightOperandType, "int args");
		}

		if (operator.equals("-") || operator.equals("/")
				|| operator.equals("*")) {
			if (leftOperandType.isOfType(PrimitiveType.INT)
					&& rightOperandType.isOfType(PrimitiveType.INT)) {
				return PrimitiveType.INT;
			}
			
			//float*float, int*float, float*int (note: int*int already treated)
			if(leftOperandType.isOfType(PrimitiveType.FLOAT) && rightOperandType.isOfType(PrimitiveType.FLOAT)){
//			if ((leftOperandType.isOfType(PrimitiveType.INT) || leftOperandType.isOfType(PrimitiveType.FLOAT))
//					&& (rightOperandType.isOfType(PrimitiveType.INT) || rightOperandType.isOfType( PrimitiveType.FLOAT))) {
				return PrimitiveType.FLOAT;
			}

			throw new TypeCheckerException(operator, leftOperandType + " and "
					+ rightOperandType, "numeric args");
		}

		// never reached
		throw new TypeCheckerException("Must not be raised.");
	}

	@Override
	public IType visit(IASTblock iast, ITypeEnvironment env)
			throws TypeCheckerException {
		IASTblock.IASTbinding[] bindings = iast.getBindings();
		ITypeEnvironment oldEnv = env;
		for (int i = 0; i < bindings.length; i++) {
			IType initType = bindings[i].getInitialisation().accept(this,
					oldEnv);

			if (!(bindings[i].getVariable() instanceof IASTtypedVariable)) {
				// never reached
				throw new TypeCheckerException("Variable : "
						+ bindings[i].getVariable().getName()
						+ ", must be typed !");
			}

			IASTtypedVariable variable = (IASTtypedVariable) bindings[i]
					.getVariable();

			if (!initType.isOfType(variable.getType())) {
					throw new TypeCheckerException(
							"Binding not compatible type.");
			}
			//TODO to check : j'accept la multi definition une variable
			env = env.extend(variable, variable.getType());
		}
		IType bodyType = iast.getBody().accept(this, env);
		return bodyType;
	}

	@Override
	public IType visit(IASTinvocation iast, ITypeEnvironment env)
			throws TypeCheckerException {
		//TODO modified code to be checked
		IType type = iast.getFunction().accept(this, env);
		if (!(type instanceof FunctionType)) {
			throw new TypeCheckerException("Can not invoc a variable.");
		}
		FunctionType funcType = (FunctionType) type;

		IASTexpression[] args = iast.getArguments();
		IType[] argsTypes = new IType[args.length];

		for (int i = 0; i < args.length; i++) {
			argsTypes[i] = args[i].accept(this, env);
		}
		if (!funcType.argsAcceptType(argsTypes)) {
			throw new TypeCheckerException("Incompatible args type.");
		}
		return funcType.getExitType();
	}

	@Override
	public IType visit(IASToperator iast, ITypeEnvironment env)
			throws TypeCheckerException {
		throw new TypeCheckerException("Already processed via Operation");
	}

	@Override
	public IType visit(IASTsequence iast, ITypeEnvironment env)
			throws TypeCheckerException {
		IASTexpression[] exprs = iast.getExpressions();
		IType lastExprType = null;
		for (int i = 0; i < exprs.length; i++) {
			lastExprType = iast.getExpressions()[i].accept(this, env);
		}
		return lastExprType;
	}

	@Override
	public IType visit(IASTunaryOperation iast, ITypeEnvironment env)
			throws TypeCheckerException {
		IType operandType = iast.getOperand().accept(this, env);
		String operator = iast.getOperator().getName();
		if (operator.equals("!")) {
			if (!operandType.isOfType(PrimitiveType.BOOL)) {
				throw new TypeCheckerException(operator,
						operandType.toString(), "boolean");
			}
		} else if (operator.equals("-")) {
			if (!operandType.isOfType(PrimitiveType.FLOAT)
					/*&& !operandType.isOfType(PrimitiveType.INT)*/) {
				throw new TypeCheckerException(operator,
						operandType.toString(), "int or float");
			}
		} else {
			// never reached
			throw new TypeCheckerException("Uknown unary operator.");
		}
		return operandType;
	}

	@Override
	public IType visit(IASTvariable iast, ITypeEnvironment env)
			throws TypeCheckerException {
		if (iast instanceof IASTtypedVariable) {
			throw new TypeCheckerException(
					"IASTtypdVariable must not be visited.");
		}
		IType type;
		try {
			type = env.getValue(iast);
		} catch (TypeCheckerException e) {
			// TODO : check
			Object obj = globalVariableEnvironment.getGlobalVariableValue(iast
					.getName());
			if (obj == null) {
				throw new TypeCheckerException("Variable " + iast.getName()
						+ " not declared.");
			}
			if (!(obj instanceof ITypedPrimitive)) {
				if (obj instanceof IType) {
					return (IType) obj;
				}
				throw new TypeCheckerException("Global variable"
						+ iast.getName() + "must be typed.");
			} else {
				ITypedPrimitive primitive = (ITypedPrimitive) obj;
				type = primitive.getType();

			}

		}
		return type;
	}

	@Override
	public IType visit(IASTboolean iast, ITypeEnvironment env)
			throws TypeCheckerException {
		return PrimitiveType.BOOL;
	}

	@Override
	public IType visit(IASTfloat iast, ITypeEnvironment env)
			throws TypeCheckerException {
		return PrimitiveType.FLOAT;
	}

	@Override
	public IType visit(IASTinteger iast, ITypeEnvironment env)
			throws TypeCheckerException {
		return PrimitiveType.INT;
	}

	@Override
	public IType visit(IASTstring iast, ITypeEnvironment env)
			throws TypeCheckerException {
		return PrimitiveType.STRING;
	}

}

package com.paracamplus.tilp1.trash;

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
import com.paracamplus.tilp1.interfaces.IASTfactory;
import com.paracamplus.tilp1.interfaces.IASTtypedVariable;
import com.paracamplus.tilp1.typer.EmptyTypeEnvironment;
import com.paracamplus.tilp1.typer.exceptions.TypeCheckerException;
import com.paracamplus.tilp1.typer.interfaces.IType;
import com.paracamplus.tilp1.typer.interfaces.ITypeEnvironment;

public class AstTyper implements
		IASTvisitor<IASTexpression, ITypeEnvironment, TypeCheckerException> {

	private final IASTfactory factory;

	public AstTyper(IASTfactory factory, IASTexpression prgram) {
		this.factory = factory;
	}

	public IASTprogram type(IASTprogram prgram)
			throws TypeCheckerException {
		ITypeEnvironment env = new EmptyTypeEnvironment();
		return factory.newProgram(prgram.getBody().accept(this, env));
	}

	@Override
	public IASTexpression visit(IASTalternative iast, ITypeEnvironment env)
			throws TypeCheckerException {
		IASTexpression alternant = (iast.getAlternant() == null) ? null : iast
				.getAlternant().accept(this, env);
		IASTexpression condition = (iast.getCondition() == null) ? null : iast
				.getCondition().accept(this, env);
		IASTexpression consequence = (iast.getConsequence() == null) ? null
				: iast.getConsequence().accept(this, env);
		return factory.newAlternative(condition, consequence, alternant);
	}

	@Override
	public IASTexpression visit(IASTbinaryOperation iast, ITypeEnvironment env)
			throws TypeCheckerException {
		IASTexpression leftOperand = iast.getLeftOperand().accept(this, env);
		IASTexpression rightOperand = iast.getRightOperand().accept(this, env);
		return factory.newBinaryOperation(iast.getOperator(), leftOperand,
				rightOperand);
	}

	@Override
	public IASTexpression visit(IASTblock iast, ITypeEnvironment env)
			throws TypeCheckerException {
		IASTblock.IASTbinding[] bindings = iast.getBindings();
		IASTblock.IASTbinding[] newBindings = new IASTblock.IASTbinding[bindings.length];
		ITypeEnvironment oldEnv = env;

		for (int i = 0; i < newBindings.length; i++) {
			IASTexpression init = bindings[i].getInitialisation().accept(this,
					oldEnv);
			if (!(bindings[i].getVariable() instanceof IASTtypedVariable)) {
				// never reached
				throw new TypeCheckerException("Variable : "
						+ bindings[i].getVariable().getName()
						+ ", must be typed !");
			}
			IASTtypedVariable variable = (IASTtypedVariable) bindings[i]
					.getVariable();
			// useless
			variable = factory.newTypedVariable(variable.getName(),
					variable.getType());
			env = env.extend(variable, variable.getType());
			newBindings[i] = factory.newBinding(variable, init);
		}

		IASTexpression body = iast.getBody().accept(this, env);
		return factory.newBlock(newBindings, body);
	}

	@Override
	public IASTexpression visit(IASTboolean iast, ITypeEnvironment env)
			throws TypeCheckerException {
		return iast;
	}

	@Override
	public IASTexpression visit(IASTfloat iast, ITypeEnvironment env)
			throws TypeCheckerException {
		return iast;
	}

	@Override
	public IASTexpression visit(IASTinteger iast, ITypeEnvironment env)
			throws TypeCheckerException {
		return iast;
	}

	@Override
	public IASTexpression visit(IASTinvocation iast, ITypeEnvironment env)
			throws TypeCheckerException {

		IASTexpression[] args = iast.getArguments();
		IASTexpression[] newArgs = new IASTexpression[args.length];
		for (int i = 0; i < newArgs.length; i++) {
			newArgs[i] = args[i].accept(this, env);
		}
		IASTexpression func = iast.getFunction().accept(this, env);

		return factory.newInvocation(func, args);
	}

	@Override
	public IASTexpression visit(IASToperator iast, ITypeEnvironment env)
			throws TypeCheckerException {
		throw new TypeCheckerException("Already processed via Operation");
	}

	@Override
	public IASTexpression visit(IASTsequence iast, ITypeEnvironment env)
			throws TypeCheckerException {
		IASTexpression[] exprs = iast.getExpressions();
		IASTexpression[] newExprs = new IASTexpression[exprs.length];
		for (int i = 0; i < newExprs.length; i++) {
			newExprs[i] = exprs[i].accept(this, env);
		}
		return factory.newSequence(newExprs);
	}

	@Override
	public IASTexpression visit(IASTstring iast, ITypeEnvironment env)
			throws TypeCheckerException {
		return iast;
	}

	@Override
	public IASTexpression visit(IASTunaryOperation iast, ITypeEnvironment env)
			throws TypeCheckerException {
		IASTexpression operand = iast.getOperand().accept(this, env);
		return factory.newUnaryOperation(iast.getOperator(), operand);
	}

	@Override
	public IASTexpression visit(IASTvariable iast, ITypeEnvironment env)
			throws TypeCheckerException {
		if (iast instanceof IASTtypedVariable) {
			throw new TypeCheckerException("IASTtypdVariable must not be visited.");
		}
		IType type = env.getValue(iast);
		return factory.newTypedVariable(iast.getName(), type);
	}
}

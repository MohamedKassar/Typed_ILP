package com.paracamplus.tilp1.parser.ilpml;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import antlr4.TypedILPMLgrammar1Lexer;
import antlr4.TypedILPMLgrammar1Parser;

import com.paracamplus.ilp1.interfaces.IASTprogram;
import com.paracamplus.ilp1.parser.ParseException;
import com.paracamplus.tilp1.parser.ilpml.ILPMLListener;
import com.paracamplus.tilp1.interfaces.IASTfactory;

public class ILPMLParser extends com.paracamplus.ilp1.parser.ilpml.ILPMLParser {

	public ILPMLParser(IASTfactory factory) {
		super(factory);
	}

	public IASTprogram getProgram() throws ParseException {
		try {
			ANTLRInputStream in = new ANTLRInputStream(input.getText());
			// flux de caractères -> analyseur lexical
			TypedILPMLgrammar1Lexer lexer = new TypedILPMLgrammar1Lexer(in);
			// analyseur lexical -> flux de tokens
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			// flux tokens -> analyseur syntaxique
			TypedILPMLgrammar1Parser parser = new TypedILPMLgrammar1Parser(
					tokens);
			// démarage de l'analyse syntaxique
			TypedILPMLgrammar1Parser.ProgContext tree = parser.prog();
			// parcours de l'arbre syntaxique et appels du Listener
			ParseTreeWalker walker = new ParseTreeWalker();
			ILPMLListener extractor = new ILPMLListener((IASTfactory) factory);
			walker.walk(extractor, tree);
			return tree.node;
		} catch (Exception e) {
			throw new ParseException(e);
		}
	}
}

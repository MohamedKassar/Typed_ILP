package com.paracamplus.tilp3.parser.ilpml;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import antlr4.TypedILPMLgrammar3Lexer;
import antlr4.TypedILPMLgrammar3Parser;

import com.paracamplus.ilp1.interfaces.IASTprogram;
import com.paracamplus.ilp1.parser.ParseException;
import com.paracamplus.tilp3.interfaces.IASTfactory;
import com.paracamplus.tilp3.parser.ilpml.ILPMLListener;

public class ILPMLParser extends com.paracamplus.tilp2.parser.ilpml.ILPMLParser {
	public ILPMLParser(IASTfactory factory) {
		super(factory);
	}

	public IASTprogram getProgram() throws ParseException {
		try {
			ANTLRInputStream in = new ANTLRInputStream(input.getText());
			// flux de caractères -> analyseur lexical
			TypedILPMLgrammar3Lexer lexer = new TypedILPMLgrammar3Lexer(in);
			// analyseur lexical -> flux de tokens
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			// flux tokens -> analyseur syntaxique
			TypedILPMLgrammar3Parser parser = new TypedILPMLgrammar3Parser(
					tokens);
			// démarage de l'analyse syntaxique
			TypedILPMLgrammar3Parser.ProgContext tree = parser.prog();
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

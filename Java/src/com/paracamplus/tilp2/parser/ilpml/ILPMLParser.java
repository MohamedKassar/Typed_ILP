package com.paracamplus.tilp2.parser.ilpml;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import antlr4.TypedILPMLgrammar2Lexer;
import antlr4.TypedILPMLgrammar2Parser;

import com.paracamplus.ilp1.interfaces.IASTprogram;
import com.paracamplus.ilp1.parser.ParseException;
import com.paracamplus.tilp2.interfaces.IASTfactory;
import com.paracamplus.tilp2.parser.ilpml.ILPMLListener;

public class ILPMLParser extends com.paracamplus.tilp1.parser.ilpml.ILPMLParser{

	public ILPMLParser(IASTfactory factory) {
		super(factory);
	}
	
	public IASTprogram getProgram() throws ParseException {
		try {
			ANTLRInputStream in = new ANTLRInputStream(input.getText());
			// flux de caractères -> analyseur lexical
			TypedILPMLgrammar2Lexer lexer = new TypedILPMLgrammar2Lexer(in);
			// analyseur lexical -> flux de tokens
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			// flux tokens -> analyseur syntaxique
			TypedILPMLgrammar2Parser parser = new TypedILPMLgrammar2Parser(
					tokens);
			// démarage de l'analyse syntaxique
			TypedILPMLgrammar2Parser.ProgContext tree = parser.prog();
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

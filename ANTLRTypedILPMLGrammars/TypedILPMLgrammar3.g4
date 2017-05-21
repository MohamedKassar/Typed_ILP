grammar TypedILPMLgrammar3;

// Import de la grammaire à enrichir
import TypedILPMLgrammar1;

// Redéfinition des programmes
prog returns [com.paracamplus.ilp2.interfaces.IASTprogram node] 
    : (defs+=globalFunDef ';'?)*  (exprs+=expr ';'?) * EOF
    ;

// Fonction globale
globalFunDef returns [com.paracamplus.ilp2.interfaces.IASTfunctionDefinition node]
    : 'function' name=IDENT '(' (vars+=IDENT ':' types+= type_)? (',' vars+=IDENT ':' types+= type_)* ')' ':' exitType = type_
        body=expr
    ;
    // fonction locale nommée
localFunDef returns [com.paracamplus.ilp3.interfaces.IASTnamedLambda node]
    : 'function' name=IDENT '(' (vars+=IDENT ':' types+=type_)? (',' vars+=IDENT ':' types+=type_)* ')' ':' exitType = type_
        body=expr
    ;
 
// Expressions enrichies
expr returns [com.paracamplus.ilp1.interfaces.IASTexpression node]

// séquence d'instructions
    : '(' exprs+=expr (';'? exprs+=expr)* ';'? ')' # Sequence

// invocation
    | fun=expr '(' args+=expr? (',' args+=expr)* ')' # Invocation

// opérations
    | op=('-' | '!') arg=expr # Unary
    | arg1=expr op=('*' | '/' | '%') arg2=expr # Binary
    | arg1=expr op=('+' | '-') arg2=expr # Binary
    | arg1=expr op=('<' | '<=' | '>' | '>=') arg2=expr # Binary
    | arg1=expr op=('==' | '!=') arg2=expr # Binary
    | arg1=expr op='&' arg2=expr # Binary
    | arg1=expr op=('|' | '^') arg2=expr # Binary
    | 'true' # ConstTrue
    | 'false' # ConstFalse
    | intConst=INT # ConstInteger
    | floatConst=FLOAT # ConstFloat
    | stringConst=STRING # ConstString
    | var=IDENT # Variable
    | 'let' vars+=IDENT ':' types+=type_ '=' vals+=expr ('and' vars+=IDENT ':' types+=type_ '=' vals+=expr)* 
      'in' body=expr # Binding
    | 'if' condition=expr 'then' consequence=expr 
        ('else' alternant=expr)? # Alternative
    | var=IDENT (':' type = type_)? '=' val=expr # VariableAssign
    | 'while' condition=expr 'do' body=expr # Loop
    
// ajouts TILP3
// fonctions anonymes
    | 'lambda' '(' (vars+=IDENT ':' types+=type_)? (',' vars+=IDENT ':' types+=type_)* ')' ':' exitType = type_
      body=expr # Lambda
    
// fonctions locales
    | defs+=localFunDef ('and' defs+=localFunDef)* 'in' body=expr 
    # Codefinitions       
    ;

type_ returns [com.paracamplus.tilp1.interfaces.IType node]
    :type = 'unit' #type
    |type = 'bool' #type
    |type = 'int' #type
    |type ='float' #type
    |type = 'string' #type
    |'(' ((typeParam += type_ '*')* typeParam += type_)? ')' '->' exitType = type_  #funtionType
    ;




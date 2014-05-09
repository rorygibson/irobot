grammar robots;

//
// Lexer rules (start with uppercase letter)
//

Comment
  :  '#' ~( '\r' | '\n' )*
  ;

Identifier : IdentifierChar+ ;

Glob: '*' ;

Hash : '#';

WS : ( ' '
  | '\t'
  | '\n'
  | '\r'
  ) -> channel(HIDDEN) ;

fragment
IdentifierChar
  : [0-9a-zA-Z]
  | '/'
  | '~'
  | '-' 
  | '_'
  | '&'
  | '?'
  | '.'
  | '..'
  ;


//
// Parser rules (start with lowercase letter)
//

records : Comment* record* Comment* ;

record : Comment* agent Comment* rule*;

agent : 'User-Agent:' (Identifier | Glob)  Comment*;

rule : (allow | disallow | extension) Comment* ;

disallow : 'Disallow' ':' Identifier Comment*;

allow : 'Allow' ':' Identifier Comment*;

extension : Identifier ':' Identifier Comment*;




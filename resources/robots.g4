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
  | ':'
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

UserAgent : [Uu] [Ss] [Ee] [Rr] '-' [Aa] [Gg] [Ee] [Nn] [Tt] ':' ;

//
// Parser rules (start with lowercase letter)
//

records : Comment* record* Comment* ;

record : Comment* agent Comment* (allow | disallow | extension)* Comment*;

agent : UserAgent (Identifier | Glob)  Comment*;

disallow : 'Disallow' Identifier Comment*;

allow : 'Allow' Identifier Comment*;

extension : Identifier Identifier Comment*;




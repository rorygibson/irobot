grammar robots;

Comment
  :  '#' ~( '\r' | '\n' )*
  ;

Identifier : ([a-z] | [A-Z])+ ;

Path : ('*' | '/')+ ;

Hash : '#';

WS : ( ' '
  | '\t'
  | '\n'
  | '\r'
  ) -> channel(HIDDEN) ;

records : Comment* record* Comment* ;

record : Comment* agent Comment* rule*;

agent : 'User-Agent:' Identifier Comment*;

rule : (allow | disallow | extension) Comment* ;

disallow : 'Disallow' ':' Path Comment*;

allow : 'Allow' ':' Path Comment*;

extension : Identifier ':' Identifier Comment*;




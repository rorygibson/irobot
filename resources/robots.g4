// grammar test;
// rule  : 'hello' ID ;
// ID : ([a-z] | [A-Z])+;
// WS : [ \t\r\n]+ -> skip ;



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

record : Comment* agentline Comment* ruleline*;

agentline : 'User-Agent:' Identifier Comment*;

ruleline : (allowexpr | disallowexpr | extline) Comment* ;

disallowexpr : 'Disallow' ':' Path Comment*;

allowexpr : 'Allow' ':' Path Comment*;

extline : Identifier ':' Identifier Comment*;




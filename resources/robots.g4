// grammar test;
// rule  : 'hello' ID ;
// ID : ([a-z] | [A-Z])+;
// WS : [ \t\r\n]+ -> skip ;



grammar robots;

Comment
  :  '#' ~( '\r' | '\n' )*
  ;

Agent : ([a-z] | [A-Z])+ ;

Path : ('*' | '/')+ ;

Hash : '#';

WS : ( ' '
  | '\t'
  | '\n'
  | '\r'
  ) -> channel(HIDDEN) ;


records : record* ;

record : agentline ruleline*;

agentline : 'User-Agent:' Agent Comment*;

ruleline : (allowexpr | disallowexpr) Comment* ;

disallowexpr : 'Disallow' ':' Path Comment*;

allowexpr : 'Allow' ':' Path Comment*;




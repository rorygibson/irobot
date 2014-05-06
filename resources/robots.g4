// grammar test;
// rule  : 'hello' ID ;
// ID : ([a-z] | [A-Z])+;
// WS : [ \t\r\n]+ -> skip ;



grammar robots;

records : record* ;

record : agentline WS* ruleline* WS*;

agentline : 'User-Agent:' AGENT WS* ;

AGENT : ([a-z] | [A-Z])+ ;

ruleline : (allowexpr | disallowexpr) WS* ;

disallowexpr : 'Disallow' ':' PATH WS*;

allowexpr : 'Allow' ':' PATH WS*;

PATH : ('*' | '/')+ ;

WS : ( ' '
  | '\t'
  | '\n'
  | '\r'
  ) -> channel(HIDDEN) ;

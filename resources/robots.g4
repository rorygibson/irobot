grammar robots;

//
// Lexer rules (start with uppercase letter)
//

Comment
  :  '#' ~( '\r' | '\n' )*
  -> skip
  ;

WS : ( ' '
  | '\t'
  | '\n'
  | '\r'
  ) -> channel(HIDDEN) ;

ID : (ValidChar)+ ;

URL : 'http://' (ValidChar)* ;

fragment
ValidChar
  : ( [0-9a-zA-Z]
  | '/'
  | '~'
  | '-' 
  | '_'
  | '&'
  | '?'
  | '.'
  | '*'
  | '..'
  | '='
  )+  
  ;

UserAgent : 
  [Uu] [Ss] [Ee] [Rr] '-' [Aa] [Gg] [Ee] [Nn] [Tt] ':' 
  ;


//
// Parser rules (start with lowercase letter)
//

records : (record | Comment)* ;

record : agent (Comment | allow | disallow | sitemap | extension)* ;

agent : UserAgent ID Comment*;

disallow : 'Disallow:' ID Comment*;

allow : 'Allow:' ID Comment*;

sitemap : 'Sitemap:' (URL | ID) Comment*;

extension : ID ':' ID Comment*;



grammar robots;

//
// Lexer rules (start with uppercase letter)
//

Comment
  :  '#' ~( '\r' | '\n' )*
  -> skip
  ;

Identifier : IdentifierChar+ ;

Hash : '#';

WS : ( ' '
  | '\t'
  | '\n'
  | '\r'
  ) -> channel(HIDDEN) ;

SEP : ':' -> skip;

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
  | '*'
  | '..'
  | '='
  ;

UserAgent : 
  [Uu] [Ss] [Ee] [Rr] '-' [Aa] [Gg] [Ee] [Nn] [Tt] 
  SEP 
  ;

Disallow 
  : 'Disallow' 
  SEP 
  ;

Allow 
  : 'Allow' 
  SEP 
  ;

Sitemap 
  : 'Sitemap' 
  SEP 
  ;

//
// Parser rules (start with lowercase letter)
//

records : (record | Comment)* ;

record : agent (Comment | allow | disallow | sitemap | extension)* ;

agent : UserAgent Identifier Comment*;

disallow : Disallow Identifier Comment*;

allow : Allow Identifier Comment*;

sitemap : Sitemap Identifier Comment*;

extension : Identifier Identifier Comment*;




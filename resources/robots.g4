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

fragment
ValidChar
  : ( [a-z]
  | [A-Z]
  | '/'
  | '~'
  | '-' 
  | '_'
  | '&'
  | '?'
  | '.'
  | '*'
  | ','
  | '$'
  | '%'
  | '..'
  | '@'
  | '+'
  | '('
  | ')'
  | '!'
  | '"'
  | '\''
  | '|'
  | ';'
  | '#'
  | '=' )+  
  ;

// TODO :

Digit
  : ('0'..'9')
  ;

UserAgent 
  : [Uu] [Ss] [Ee] [Rr] '-' [Aa] [Gg] [Ee] [Nn] [Tt] ':' 
  ;

Sitemap 
  : [Ss] [Ii] [Tt] [Ee] [Mm] [Aa] [Pp] ':'
  ;

Disallow 
  : [Dd] [Ii] [Ss] [Aa] [Ll] [Ll] [Oo] [Ww] ':' 
  ;

Allow 
  : [Aa] [Ll] [Ll] [Oo] [Ww] ':' 
  ;

ID 
  : (ValidChar | Digit)+ 
  ;

URL 
  : 'http://' (ValidChar | Digit)* 
  ;

CrawlDelay 
  : [Cc] [Rr] [Aa] [Ww] [Ll] '-' [Dd] [Ee] [Ll] [Aa] [Yy] ':'
  ;


//
// Parser rules (start with lowercase letter)
//

records : (record | Comment)* ;

record : sitemap | (agent (Comment | allow | disallow | sitemap | crawldelay | extension)*) ;

agent : UserAgent ID Comment*;

disallow : Disallow ID Comment*;

allow : Allow ID Comment*;

sitemap : Sitemap (URL | ID) Comment*;

crawldelay : CrawlDelay ID Comment*;

extension : ID ':' ID Comment*;



/*
 * Copyright (c) 2010 by Bart Kiers
 * 
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 * 
 * Project      : Liqp; a Liquid Template grammar/parser
 * Developed by : Bart Kiers, bart@big-o.nl
 */
grammar Liquid;



tokens {
  ASSIGNMENT,
  ATTRIBUTES,
  BLOCK,
  CAPTURE,
  CASE,
  COMMENT,
  CYCLE, 
  ELSE,
  FILTERS,
  FILTER,
  FOR_ARRAY,
  FOR_BLOCK,
  FOR_RANGE,
  GROUP,
  HASH,
  IF,
  INDEX,
  ELSIF,
  INCLUDE,
  LOOKUP,
  OUTPUT,
  PARAMS,
  PLAIN,
  RAW,
  TABLE,
  UNLESS,
  WHEN,
  WITH,
  NO_SPACE,
  CUSTOM_TAG,
  CUSTOM_TAG_BLOCK
}


/* parser rules */
parse
 : block EOF
   //(t=. {System.out.printf("\%-20s '\%s'\n", tokenNames[$t.type], $t.text);})* EOF
 ;

block
 : (atom)*
 ;

atom
 : tag
 | output
 | assignment
 | text
 ;

tag
 : custom_tag
 | raw_tag
 | comment_tag
 | if_tag
 | unless_tag
 | case_tag
 | cycle_tag
 | for_tag
 | table_tag
 | capture_tag
 | include_tag
 | break_tag
 | continue_tag
 ;

custom_tag
 : (TagStart Id (expr (Comma expr)*)? TagEnd )
   (custom_tag_block)?
 ;

custom_tag_block
 : (atom)* TagStart EndId TagEnd
 ;

raw_tag
 : TagStart RawStart TagEnd raw_body TagStart RawEnd TagEnd 
 ;

raw_body
 : other_than_tag_start 
 ;

comment_tag
 : TagStart CommentStart TagEnd comment_body TagStart CommentEnd TagEnd 
 ;

comment_body
 : other_than_tag_start 
 ;

other_than_tag_start
 : ~(TagStart | OutStart)+
 ;

if_tag
 : TagStart IfStart expr TagEnd block elsif_tag* else_tag? TagStart IfEnd TagEnd
 ;

elsif_tag
 : TagStart Elsif expr TagEnd block 
 ;

else_tag
 : TagStart Else TagEnd block 
 ;

unless_tag
 : TagStart UnlessStart expr TagEnd block else_tag? TagStart UnlessEnd TagEnd 
 ;

case_tag
 : TagStart CaseStart expr TagEnd Other? when_tag+ else_tag? TagStart CaseEnd TagEnd 
 ;

when_tag
 : TagStart When term ((Or | Comma) term)* TagEnd block 
 ;

cycle_tag
 : TagStart Cycle cycle_group expr (Comma expr)* TagEnd 
 ;

cycle_group
 : (expr Col)? 
 ;

for_tag
 : for_array
 | for_range    
 ;

for_array
 : TagStart ForStart Id In lookup attribute* TagEnd
   for_block
   TagStart ForEnd TagEnd
 ;

for_range
 : TagStart ForStart Id In OPar expr DotDot expr CPar attribute* TagEnd
   block
   TagStart ForEnd TagEnd
 ;

for_block
 : a=block (TagStart Else TagEnd b=block)? 
 ;

attribute
 : Id Col expr 
 ;

table_tag
 : TagStart TableStart Id In lookup attribute* TagEnd block TagStart TableEnd TagEnd 
 ;

capture_tag
 : TagStart CaptureStart ( Id TagEnd block TagStart CaptureEnd TagEnd  
                         | Str TagEnd block TagStart CaptureEnd TagEnd 
                         )
 ;

include_tag
 : TagStart Include a=Str (With b=Str)? TagEnd 
 ;

break_tag
 : TagStart Break TagEnd 
 ;

continue_tag
 : TagStart Continue TagEnd 
 ;

output
 : OutStart expr filter* OutEnd 
 ;

filter
 : Pipe Id params? 
 ;

params
 : Col expr (Comma expr)* 
 ;

assignment
 : TagStart Assign Id EqSign expr filter? TagEnd 
 ;

expr
 : or_expr
 ;

or_expr
 : and_expr (Or and_expr)*
 ;

and_expr
 : contains_expr (And contains_expr)*
 ;

contains_expr
 : eq_expr (Contains eq_expr)?
 ;

eq_expr
 : rel_expr ((Eq | NEq) rel_expr)*
 ;

rel_expr
 : term ((LtEq | Lt | GtEq | Gt) term)?
 ;

term
 : DoubleNum
 | LongNum
 | Str
 | True
 | False
 | Nil
 | NoSpace+       
 | lookup
 | Empty
 | OPar expr CPar 
 ;

lookup : Id;

id
 : Id
 | Continue 
 ;

id2
 : id
 | Empty 
 ;

index
 : Dot id2      
 | OBr expr CBr 
 ;

text
 : ~(OutStart | OutEnd | TagStart | TagEnd)+
 ;

/* lexer rules */
OutStart : '{{';
OutEnd   : '}}';
TagStart : '{%';
TagEnd   : '%}';

Str : (SStr | DStr);

DotDot    : '..';
Dot       : '.';
NEq       : '!=' | '<>';
Eq        :  '==';
EqSign    :  '=';
GtEq      :  '>=';
Gt        :  '>';
LtEq      :  '<=';
Lt        :  '<';
Minus     :  '-';
Pipe      :  '|';
Col       :  ':';
Comma     :  ',';
OPar      :  '(';
CPar      :  ')';
OBr       :  '[';
CBr       :  ']';
QMark     :  '?';

CommentStart : 'comment';
CommentEnd : 'endcomment';
RawStart : 'raw';
RawEnd : 'endraw';
IfStart : 'if';
IfEnd : 'endif';
Elsif : 'elsif';
UnlessStart : 'unless';
UnlessEnd : 'endunless';
Else : 'else';
Contains : 'contains';
CaseStart : 'case';
CaseEnd : 'endcase';
When : 'when';
Cycle : 'cycle';
ForStart : 'for';
ForEnd : 'endfor';
In : 'in';
And : 'and';
Or : 'or';
TableStart : 'tablerow';
TableEnd : 'endtablerow';
Assign : 'assign';
True : 'true';
False : 'false';
Nil : 'nil';
Include : 'include';
With : 'with';
CaptureStart : 'capture';
CaptureEnd : 'endcapture';
EndId : 'end';
Break : 'break';
Continue : 'continue';
Empty : 'empty';
//Other : .+?;

DoubleNum :  '-'? Digit+  '.' Digit* ;
LongNum   :  '-'? Digit+;
WS        :  [ \t\r\n] -> skip;

Id : (Letter | '_') (Letter | '_' | '-' | Digit)*;

/* fragment rules */
fragment Letter : 'a'..'z' | 'A'..'Z';
fragment Digit  : '0'..'9';
fragment SStr   : '\'' ~'\''* '\'' ;
fragment DStr   : '"' ~'"'* '"'    ;

NoSpace
 : ~(' ' | '\t' | '\r' | '\n')
 ;



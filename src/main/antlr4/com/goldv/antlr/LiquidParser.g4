parser grammar LiquidParser;

options { tokenVocab=LiquidLexer; }

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
 : (OPEN_TAG Id (expr (Comma expr)*)? CLOSE_TAG )
   (custom_tag_block)?
 ;

custom_tag_block
 : (atom)* OPEN_TAG EndId CLOSE_TAG
 ;

raw_tag
 : OPEN_TAG RawStart CLOSE_TAG raw_body OPEN_TAG RawEnd CLOSE_TAG
 ;

raw_body
 : other_than_tag_start
 ;

comment_tag
 : OPEN_TAG CommentStart CLOSE_TAG comment_body OPEN_TAG CommentEnd CLOSE_TAG
 ;

comment_body
 : other_than_tag_start
 ;

other_than_tag_start
 : ~(OPEN_TAG | OPEN_OUTPUT)+
 ;

if_tag
 : OPEN_TAG IfStart expr CLOSE_TAG block elsif_tag* else_tag? OPEN_TAG IfEnd CLOSE_TAG
 ;

elsif_tag
 : OPEN_TAG Elsif expr CLOSE_TAG block
 ;

else_tag
 : OPEN_TAG Else CLOSE_TAG block
 ;

unless_tag
 : OPEN_TAG UnlessStart expr CLOSE_TAG block else_tag? OPEN_TAG UnlessEnd CLOSE_TAG
 ;

case_tag
 : OPEN_TAG CaseStart expr CLOSE_TAG TEXT? when_tag+ else_tag? OPEN_TAG CaseEnd CLOSE_TAG
 ;

when_tag
 : OPEN_TAG When term ((Or | Comma) term)* CLOSE_TAG block
 ;

cycle_tag
 : OPEN_TAG Cycle cycle_group expr (Comma expr)* CLOSE_TAG
 ;

cycle_group
 : (expr Col)?
 ;

for_tag
 : for_array
 | for_range
 ;

for_array
 : OPEN_TAG ForStart Id In lookup attribute* CLOSE_TAG
   for_block
   OPEN_TAG ForEnd CLOSE_TAG
 ;

for_range
 : OPEN_TAG ForStart Id In OPar expr DotDot expr CPar attribute* CLOSE_TAG
   block
   OPEN_TAG ForEnd CLOSE_TAG
 ;

for_block
 : a=block (OPEN_TAG Else CLOSE_TAG b=block)?
 ;

attribute
 : Id Col expr
 ;

table_tag
 : OPEN_TAG TableStart Id In lookup attribute* CLOSE_TAG block OPEN_TAG TableEnd CLOSE_TAG
 ;

capture_tag
 : OPEN_TAG CaptureStart ( Id CLOSE_TAG block OPEN_TAG CaptureEnd CLOSE_TAG
                         | Str CLOSE_TAG block OPEN_TAG CaptureEnd CLOSE_TAG
                         )
 ;

include_tag
 : OPEN_TAG Include a=Str (With b=Str)? CLOSE_TAG
 ;

break_tag
 : OPEN_TAG Break CLOSE_TAG
 ;

continue_tag
 : OPEN_TAG Continue CLOSE_TAG
 ;

output
 : OPEN_OUTPUT lookup filter* CLOSE_OUTPUT
 ;

filter
 : Pipe Id params?
 ;

params
 : Col expr (Comma expr)*
 ;

assignment
 : OPEN_TAG Assign Id EqSign expr filter? CLOSE_TAG
 ;

expr
 : or_expr
 ;

or_expr
 : and_expr (Or and_expr)*
 ;

and_expr
 : rel_expr (And rel_expr)*
 ;

contains_expr
 : rel_expr (Contains rel_expr)?
 ;

rel_expr
 : term ((LtEq | Lt | GtEq | Gt | Eq | NEq) term)?
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
 : ~(OPEN_OUTPUT | CLOSE_OUTPUT | OPEN_TAG | CLOSE_TAG)+
 ;
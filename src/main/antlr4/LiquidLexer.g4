lexer grammar LiquidLexer;

OPEN_TAG : '{%' -> pushMode(INSIDE_TAG);
OPEN_OUTPUT : '{{' -> pushMode(INSIDE_OUTPUT);

TEXT : .+? ;



// -------------------------- Everything inside a tag -------------------------
mode INSIDE_TAG;

CLOSE_TAG : '%}' -> popMode;

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

// -------------------------- Everything inside an output -------------------------
mode INSIDE_OUTPUT;

CLOSE_OUTPUT : '}}' -> popMode;


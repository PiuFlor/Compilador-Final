//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 2 "gramatica.y"
package compilador;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

//#line 27 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short ID=257;
public final static short IF=258;
public final static short THEN=259;
public final static short ELSE=260;
public final static short BEGIN=261;
public final static short END=262;
public final static short END_IF=263;
public final static short OUTF=264;
public final static short TYPEDEF=265;
public final static short FUN=266;
public final static short RET=267;
public final static short WHILE=268;
public final static short PAIR=269;
public final static short GOTO=270;
public final static short ASIG=271;
public final static short MAYOR_IGUAL=272;
public final static short MENOR_IGUAL=273;
public final static short DIST=274;
public final static short INTEGER=275;
public final static short DOUBLE=276;
public final static short CTE=277;
public final static short CADENA=278;
public final static short ETIQUETA=279;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    1,    1,    1,    1,    2,    3,    3,
    4,    4,    4,    5,    5,    5,    5,    5,    5,    5,
    5,    8,   11,   11,   11,   11,   11,   11,   11,   14,
   14,   14,   15,   15,   15,   16,   16,   16,   16,   16,
   16,   16,   16,   18,   13,   13,   13,   12,   12,    6,
    6,    6,    6,    6,    6,    6,    6,    6,    6,   19,
   19,   19,   24,   24,   24,   24,   20,   20,   20,   20,
   20,   20,   27,   27,   27,   27,   27,   28,   28,   28,
   26,   26,   26,   26,   29,   29,   29,   29,   29,   29,
   21,   21,   21,   21,   21,   21,    9,    9,    9,   30,
   30,   22,   22,   22,   10,   10,   10,   10,   10,   23,
   32,   32,    7,   25,   17,   17,   17,   17,   17,   17,
   17,   33,   33,   33,   33,   34,   34,   31,   31,
};
final static short yylen[] = {                            2,
    1,    4,    4,    4,    4,    4,    2,    1,    2,    1,
    1,    1,    1,    2,    2,    2,    2,    2,    2,    2,
    1,    2,    9,    3,    6,    7,    8,    9,    8,    2,
    2,    2,    2,    2,    1,    5,    2,    3,    5,    3,
    4,    4,    5,    4,    1,    3,    3,    1,    1,    2,
    2,    2,    2,    2,    2,    2,    2,    2,    2,    3,
    3,    3,    1,    1,    3,    4,    9,    7,    5,    7,
    7,    9,    1,    3,    3,    3,    3,    2,    1,    1,
    3,    3,    3,    2,    1,    1,    1,    1,    1,    1,
    4,    4,    4,    3,    4,    4,    5,    2,    4,    5,
    1,    5,    3,    5,    6,    6,    4,    6,    6,    1,
    2,    2,    1,    4,    3,    3,    3,    3,    3,    3,
    1,    3,    3,    1,    1,    1,    1,    1,    2,
};
final static short yydefred[] = {                         0,
    8,    0,    0,    1,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   48,   49,  113,    0,   10,   11,   12,
   13,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  110,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   89,   90,   86,  128,    0,    0,
   85,   87,   88,    0,  125,    0,    0,  127,    0,  124,
    0,    0,    0,    0,    0,    0,    0,    0,  112,  111,
    0,    9,   18,   14,   19,   15,   20,   16,   17,    0,
    0,   22,    0,   55,   50,   56,   51,   57,   52,   58,
   53,   59,   54,    0,    0,    0,    0,    0,   46,    0,
   47,    0,    0,    0,  129,    0,   84,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   73,  103,    6,
    0,   24,    0,    0,    0,    0,    0,   64,    0,    3,
    0,    2,    5,    0,  114,    0,    0,    0,    0,  118,
    0,  120,    0,    0,    0,    0,   83,  126,  122,  123,
   96,   95,   91,   93,   92,    0,   99,    0,    0,    0,
  107,    0,    0,    0,    0,   80,   79,    0,    0,    0,
    0,    0,    0,    0,    0,  104,   44,    0,   69,    0,
  101,    0,   97,    0,    0,   32,   31,   30,    0,  102,
   76,   78,   75,    0,   74,    0,    0,    0,    0,    0,
   35,    0,    0,    0,    0,    0,    0,  106,    0,  109,
  108,  105,    0,    0,    0,    0,    0,    0,    0,   33,
    0,   25,    0,    0,   66,   71,   70,    0,   68,    0,
    0,    0,    0,    0,   40,    0,   38,   26,    0,    0,
    0,    0,    0,   27,   29,    0,   41,    0,   42,    0,
    0,   72,   67,  100,   43,   39,   36,   23,   28,
};
final static short yydgoto[] = {                          3,
    4,    5,  209,   18,   19,   20,   21,   22,   23,   24,
   25,   26,   27,  174,  210,  211,   54,   55,   28,   29,
   30,   31,   32,  137,   33,   56,  129,  178,   57,  193,
   58,   34,   59,   60,
};
final static short yysindex[] = {                      -218,
    0,  462,    0,    0,  360,  -14,  -36,  148,  -39,  -72,
 -212,  154, -215,    0,    0,    0,  387,    0,    0,    0,
    0,  -55,  -45,  -13,   46, -168, -195,   21,   32,   44,
   45,   49, -162,    0,  -14,  403,  430,  249,    4, -144,
 -153, -127,  137,  113,    0,    0,    0,    0, -132,  249,
    0,    0,    0,   82,    0,  -97,   66,    0,  134,    0,
  125,  -22,  136,  -94,   -1,  166,  249,  478,    0,    0,
  -43,    0,    0,    0,    0,    0,    0,    0,    0,  -37,
  -70,    0,  138,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  160,  -11,  446,   11,  181,    0,  158,
    0,  -27,  -27,   66,    0,  286,    0,  162,  167,  214,
  478,   25,   -5,   -5,   76,  137,  -25,  114,  159,   43,
 -107, -105,  115, -101,  347,  217,  327,    0,    0,    0,
 -101,    0,  -38,  137,  -16,  244,   70,    0,   70,    0,
  -14,    0,    0,  478,    0,  134,  134,  467,  -41,    0,
  134,    0,  134,  137,  244,  144,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  340,    0, -102,  370,  374,
    0,  189,  173,  397,  478,    0,    0,  487,  -34,  496,
  407,  117,   65,   66,   66,    0,    0,  511,    0,  203,
    0,  -40,    0,  207,  198,    0,    0,    0,  205,    0,
    0,    0,    0,  -14,    0,  211,   22,   66,  133,  216,
    0,  223,   17,  244,   47,  177,  -65,    0,  436,    0,
    0,    0,  117,  117,  137,  220,   40,  484,  -14,    0,
  228,    0,  225, -101,    0,    0,    0,  526,    0,  -40,
  227,  236,  499,   41,    0,  234,    0,    0,  117,  459,
  193,  241,  382,    0,    0,  452,    0,   69,    0,  251,
  258,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,  246,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  101,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  518,    0,    0,    0,   89,    0,
    0,    0,    0,  -32,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  -10,    0,
    0,    0,   77,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   91,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   93,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   97,   88,  -12,  105,    0,  108,    0,
  519,    0,    0,    0,    0,   12,   34,    0,    0,    0,
   56,    0,   78,  275,  290,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  303,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   10,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  262,    0,    0,    0,  266,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,    0,  156,  369,  287,  426,  507,    0,    0,    0,
    0,  451,  139,  -71, -186,  321,  405,    0,    0,    0,
    0,    0,    0,  441,  -58,  122,   15,  414,  501,    0,
 -175,    0,  393,  265,
};
final static int YYTABLESIZE=796;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        126,
   62,  183,  131,   74,   49,   38,   40,   40,  126,  126,
  126,  126,  126,   76,  126,  163,  219,   49,  118,   63,
  192,  121,   49,  104,  138,   38,  126,  126,  126,  126,
  121,   63,  121,  121,  121,  138,  241,  242,    1,   49,
   69,   65,    2,  117,   66,   78,   63,   40,  121,  121,
  121,  121,  117,   65,  117,  117,  117,  233,  122,  181,
  234,  226,  260,   16,  253,  119,   49,  108,   65,  109,
  117,  117,  117,  117,  119,   83,  119,  119,  119,   85,
  246,  258,  108,  108,  109,  109,   41,  115,   80,  108,
   87,  109,  119,  119,  119,  119,  115,   81,  115,  115,
  115,  185,   89,   91,   79,  212,   41,   93,   94,  116,
   49,  213,   39,  184,  115,  115,  115,  115,  116,  126,
  116,  116,  116,  100,  108,  156,  109,  267,  101,  126,
  126,  126,  126,   68,  126,   98,  116,  116,  116,  116,
   21,   53,   51,   52,  105,   42,  126,   45,  167,   45,
  169,   94,  104,  191,  172,   61,  208,   17,  186,   98,
   37,  111,  250,   60,   82,  115,   62,   14,   15,   14,
   15,  106,  208,   14,   15,  113,  121,   42,   99,  102,
  114,  103,   49,   63,   64,  132,  133,   50,  125,  200,
  237,   96,   49,   67,  238,  120,   65,  239,   49,  165,
   73,  108,  217,  109,   49,  124,   49,   53,   51,   52,
   75,   49,  130,   53,   51,   52,   38,  188,   42,   39,
   39,  144,  182,  126,  126,  126,  126,  203,  126,   44,
  162,  126,   38,  116,   44,  126,   48,  126,   61,  126,
  126,  126,   77,   63,  140,  121,  121,  121,  121,   48,
  121,  158,  252,  121,   48,  117,   38,  121,   49,  121,
   39,  121,  121,  121,   49,   65,  143,  117,  117,  117,
  117,   48,  117,   14,   15,  117,   84,  225,   44,  117,
  157,  117,  145,  117,  117,  117,  108,   86,  109,  119,
  119,  119,  119,   49,  119,  245,  257,  119,   48,   88,
   90,  119,  235,  119,   92,  119,  119,  119,   53,   51,
   52,  115,  115,  115,  115,   82,  115,   14,   15,  115,
  172,   43,   44,  115,  266,  115,  149,  115,  115,  115,
   81,  161,   98,  116,  116,  116,  116,  107,  116,   14,
   15,  116,   48,  126,   45,  116,   45,  116,   94,  116,
  116,  116,   61,   45,   46,   47,   21,   21,   21,   45,
   60,   77,   21,   62,   21,   21,   21,   21,   21,  164,
   21,  171,    6,    7,    8,   21,   21,  159,  160,   21,
    9,   10,   11,  207,   12,   72,   13,  175,  229,    7,
    8,   14,   15,  134,  135,   16,    9,   10,   11,  207,
   12,  190,   13,   43,   44,   72,  189,   14,   15,   43,
   44,   16,  176,  176,   48,   43,  135,  150,   44,   45,
   46,   47,  152,   44,   48,   45,   46,   47,  197,  198,
   48,  194,    6,    7,    8,  195,   48,  199,   48,  236,
    9,   10,   11,   48,   12,  196,   13,  206,    6,    7,
    8,   14,   15,  221,  222,  262,    9,   10,   11,  218,
   12,  112,   13,  220,   72,  223,  119,   14,   15,  154,
   44,  224,    6,    7,    8,  243,   44,  231,  232,  240,
    9,   10,   11,  248,   12,  249,   13,  136,  254,  259,
   48,   14,   15,  128,  146,  147,   48,  255,  136,  261,
  151,  153,  176,  263,   43,   44,  264,  187,  148,  108,
  265,  109,  268,  269,  155,  123,   45,    7,    4,   70,
   45,   46,   47,   37,  247,   48,  108,   34,  109,  230,
   82,   82,   82,   82,  139,   82,  128,  176,   82,  256,
  180,  102,   82,  103,   82,   81,   81,   81,   81,    0,
   81,  177,  177,   81,  110,    0,    0,   81,   77,   81,
    0,    0,   77,    0,    0,   77,    0,    0,    0,  128,
  166,  168,  170,    0,  173,    0,    0,   72,    0,    0,
    0,  173,  179,    7,    8,    0,    0,    0,  214,  215,
    9,   10,   11,    0,   12,    0,   13,    0,    0,    0,
  128,   14,   15,  202,    0,  202,    0,    0,    0,    0,
    0,  227,  228,  128,    0,   35,    7,    8,    0,    0,
   36,    0,    0,    9,   10,   11,    0,   12,    0,   13,
  244,    0,    0,  173,   14,   15,    0,    0,   16,    0,
    0,  177,    6,    7,    8,    0,    0,    0,   71,    0,
    9,   10,   11,    0,   12,    0,   13,    0,    6,    7,
    8,   14,   15,  128,   95,   16,    9,   10,   11,    0,
   12,    0,   13,    0,    0,    0,  177,   14,   15,    0,
    0,   16,    0,    0,  173,    6,    7,    8,    0,    0,
    0,   97,    0,    9,   10,   11,    0,   12,    0,   13,
    0,  141,    7,    8,   14,   15,    0,  142,   16,    9,
   10,   11,    0,   12,    0,   13,    0,    6,    7,    8,
   14,   15,    0,    0,   16,    9,   10,   11,    0,   12,
    0,   13,    0,  126,    7,    8,   14,   15,  127,    0,
   16,    9,    6,    7,    8,   12,    0,   13,  201,    0,
    9,  204,    7,    8,   12,    0,   13,  205,    0,    9,
    0,    0,    0,   12,    0,   13,  216,    7,    8,    0,
    0,  127,    0,    0,    9,    0,    0,    0,   12,    0,
   13,  251,    7,    8,    0,    0,  127,    0,    0,    9,
    0,    0,    0,   12,    0,   13,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         32,
   40,   40,   40,   59,   45,   40,   44,   44,   41,   42,
   43,   44,   45,   59,   47,   41,  192,   45,   41,   32,
  123,   32,   45,   40,   83,   40,   59,   60,   61,   62,
   41,   44,   43,   44,   45,   94,  223,  224,  257,   45,
  256,   32,  261,   32,  257,   59,   59,   44,   59,   60,
   61,   62,   41,   44,   43,   44,   45,   41,   60,  131,
   44,   40,  249,  279,  240,   32,   45,   43,   59,   45,
   59,   60,   61,   62,   41,  271,   43,   44,   45,   59,
   41,   41,   43,   43,   45,   45,  123,   32,  257,   43,
   59,   45,   59,   60,   61,   62,   41,  266,   43,   44,
   45,   32,   59,   59,   59,   41,  123,   59,  271,   32,
   45,  183,  257,   44,   59,   60,   61,   62,   41,   32,
   43,   44,   45,  277,   43,  111,   45,   59,  256,   42,
   43,   44,   45,   12,   47,   59,   59,   60,   61,   62,
   40,   60,   61,   62,  277,    7,   59,   59,  256,   59,
  256,   59,   40,  256,  256,   59,   40,    2,  144,   38,
    5,  259,  234,   59,   26,   41,   59,  275,  276,  275,
  276,   50,   40,  275,  276,   42,  271,   39,   40,   43,
   47,   45,   45,  256,  257,  256,  257,   40,   67,  175,
  256,   36,   45,   40,  260,   60,  269,  263,   45,   41,
  256,   43,  188,   45,   45,   40,   45,   60,   61,   62,
  256,   45,  256,   60,   61,   62,   40,  259,   80,  257,
  257,   41,  261,  256,  257,  258,  259,  262,  261,  257,
  256,  264,   40,  256,  257,  268,  277,  270,  278,  272,
  273,  274,  256,  256,  256,  256,  257,  258,  259,  277,
  261,  257,  238,  264,  277,  278,   40,  268,   45,  270,
  257,  272,  273,  274,   45,  256,  256,  256,  257,  258,
  259,  277,  261,  275,  276,  264,  256,  256,  257,  268,
  256,  270,  125,  272,  273,  274,   43,  256,   45,  256,
  257,  258,  259,   45,  261,  256,  256,  264,  277,  256,
  256,  268,  256,  270,  256,  272,  273,  274,   60,   61,
   62,  256,  257,  258,  259,   41,  261,  275,  276,  264,
  256,  256,  257,  268,  256,  270,   41,  272,  273,  274,
   41,  256,  256,  256,  257,  258,  259,  256,  261,  275,
  276,  264,  277,  256,  256,  268,  256,  270,  256,  272,
  273,  274,  256,  272,  273,  274,  256,  257,  258,  271,
  256,   59,  262,  256,  264,  265,  266,  267,  268,  256,
  270,  257,  256,  257,  258,  275,  276,  113,  114,  279,
  264,  265,  266,  267,  268,   17,  270,   41,  256,  257,
  258,  275,  276,  256,  257,  279,  264,  265,  266,  267,
  268,   62,  270,  256,  257,   37,  263,  275,  276,  256,
  257,  279,  126,  127,  277,  256,  257,  256,  257,  272,
  273,  274,  256,  257,  277,  272,  273,  274,  256,  257,
  277,   62,  256,  257,  258,   62,  277,   41,  277,  263,
  264,  265,  266,  277,  268,  257,  270,   41,  256,  257,
  258,  275,  276,  256,  257,  263,  264,  265,  266,  257,
  268,   57,  270,  257,   96,  261,   62,  275,  276,  256,
  257,  261,  256,  257,  258,  256,  257,  262,  256,   44,
  264,  265,  266,  256,  268,  261,  270,   83,  262,  256,
  277,  275,  276,   68,  102,  103,  277,  262,   94,   41,
  108,  109,  216,  263,  256,  257,  125,   41,  104,   43,
   59,   45,  262,  256,  110,   65,  271,    0,    0,   13,
  272,  273,  274,  262,   41,  277,   43,  262,   45,  209,
  256,  257,  258,  259,   94,  261,  111,  251,  264,   41,
  127,   43,  268,   45,  270,  256,  257,  258,  259,   -1,
  261,  126,  127,  264,   54,   -1,   -1,  268,  256,  270,
   -1,   -1,  260,   -1,   -1,  263,   -1,   -1,   -1,  144,
  120,  121,  122,   -1,  124,   -1,   -1,  209,   -1,   -1,
   -1,  131,  256,  257,  258,   -1,   -1,   -1,  184,  185,
  264,  265,  266,   -1,  268,   -1,  270,   -1,   -1,   -1,
  175,  275,  276,  178,   -1,  180,   -1,   -1,   -1,   -1,
   -1,  207,  208,  188,   -1,  256,  257,  258,   -1,   -1,
  261,   -1,   -1,  264,  265,  266,   -1,  268,   -1,  270,
  226,   -1,   -1,  183,  275,  276,   -1,   -1,  279,   -1,
   -1,  216,  256,  257,  258,   -1,   -1,   -1,  262,   -1,
  264,  265,  266,   -1,  268,   -1,  270,   -1,  256,  257,
  258,  275,  276,  238,  262,  279,  264,  265,  266,   -1,
  268,   -1,  270,   -1,   -1,   -1,  251,  275,  276,   -1,
   -1,  279,   -1,   -1,  234,  256,  257,  258,   -1,   -1,
   -1,  262,   -1,  264,  265,  266,   -1,  268,   -1,  270,
   -1,  256,  257,  258,  275,  276,   -1,  262,  279,  264,
  265,  266,   -1,  268,   -1,  270,   -1,  256,  257,  258,
  275,  276,   -1,   -1,  279,  264,  265,  266,   -1,  268,
   -1,  270,   -1,  256,  257,  258,  275,  276,  261,   -1,
  279,  264,  256,  257,  258,  268,   -1,  270,  262,   -1,
  264,  256,  257,  258,  268,   -1,  270,  262,   -1,  264,
   -1,   -1,   -1,  268,   -1,  270,  256,  257,  258,   -1,
   -1,  261,   -1,   -1,  264,   -1,   -1,   -1,  268,   -1,
  270,  256,  257,  258,   -1,   -1,  261,   -1,   -1,  264,
   -1,   -1,   -1,  268,   -1,  270,
};
}
final static short YYFINAL=3;
final static short YYMAXTOKEN=279;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,"' '",null,null,null,null,null,null,null,"'('","')'","'*'","'+'",
"','","'-'",null,"'/'",null,null,null,null,null,null,null,null,null,null,null,
"';'","'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,"ID","IF","THEN","ELSE","BEGIN","END",
"END_IF","OUTF","TYPEDEF","FUN","RET","WHILE","PAIR","GOTO","ASIG",
"MAYOR_IGUAL","MENOR_IGUAL","DIST","INTEGER","DOUBLE","CTE","CADENA","ETIQUETA",
};
final static String yyrule[] = {
"$accept : program",
"program : programa",
"programa : nombre_programa BEGIN bloque_sentencias_programa END",
"programa : nombre_programa BEGIN END error",
"programa : nombre_programa BEGIN bloque_sentencias_programa error",
"programa : nombre_programa bloque_sentencias_programa END error",
"programa : BEGIN bloque_sentencias_programa END error",
"programa : nombre_programa error",
"nombre_programa : ID",
"bloque_sentencias_programa : bloque_sentencias_programa sentencia_programa",
"bloque_sentencias_programa : sentencia_programa",
"sentencia_programa : sentencias_declarativas",
"sentencia_programa : sentencias_ejecutables",
"sentencia_programa : etiqueta",
"sentencias_declarativas : declaracion_variable ';'",
"sentencias_declarativas : declaracion_tipo_subrango ';'",
"sentencias_declarativas : declaracion_tipo_pair ';'",
"sentencias_declarativas : declaracion_funcion ';'",
"sentencias_declarativas : declaracion_variable error",
"sentencias_declarativas : declaracion_tipo_subrango error",
"sentencias_declarativas : declaracion_tipo_pair error",
"sentencias_declarativas : declaracion_funcion",
"declaracion_variable : tipo list_var",
"declaracion_funcion : tipo FUN ID '(' parametro ')' BEGIN cuerpo_funcion END",
"declaracion_funcion : tipo FUN error",
"declaracion_funcion : tipo FUN ID '(' ')' error",
"declaracion_funcion : tipo FUN ID BEGIN cuerpo_funcion END error",
"declaracion_funcion : FUN ID '(' parametro ')' BEGIN cuerpo_funcion END",
"declaracion_funcion : tipo FUN ID '(' parametro ',' parametro ')' error",
"declaracion_funcion : tipo ID '(' parametro ')' BEGIN cuerpo_funcion END",
"parametro : tipo ID",
"parametro : tipo error",
"parametro : error ID",
"cuerpo_funcion : bloque_sentencias_programa sentencia_retorno",
"cuerpo_funcion : bloque_sentencias_programa error",
"cuerpo_funcion : sentencia_retorno",
"sentencia_retorno : RET '(' expr_aritmetic ')' ';'",
"sentencia_retorno : RET error",
"sentencia_retorno : '(' expr_aritmetic ')'",
"sentencia_retorno : RET '(' expr_aritmetic ')' error",
"sentencia_retorno : RET expr_aritmetic error",
"sentencia_retorno : RET '(' expr_aritmetic error",
"sentencia_retorno : RET expr_aritmetic ')' error",
"sentencia_retorno : RET '(' error ')' ';'",
"invocacion_funcion : ID '(' expr_aritmetic ')'",
"list_var : ID",
"list_var : ID ',' list_var",
"list_var : ID list_var error",
"tipo : INTEGER",
"tipo : DOUBLE",
"sentencias_ejecutables : sentencia_asignacion ';'",
"sentencias_ejecutables : sentencia_seleccion ';'",
"sentencias_ejecutables : sentencia_out ';'",
"sentencias_ejecutables : sentencia_while ';'",
"sentencias_ejecutables : sentencia_goto ';'",
"sentencias_ejecutables : sentencia_asignacion error",
"sentencias_ejecutables : sentencia_seleccion error",
"sentencias_ejecutables : sentencia_out error",
"sentencias_ejecutables : sentencia_while error",
"sentencias_ejecutables : sentencia_goto error",
"sentencia_asignacion : list_var ASIG list_expresion",
"sentencia_asignacion : list_var ASIG error",
"sentencia_asignacion : var_pair ASIG list_expresion",
"list_expresion : expr_aritmetic",
"list_expresion : var_pair",
"list_expresion : list_expresion ',' expr_aritmetic",
"list_expresion : list_expresion ' ' expr_aritmetic error",
"sentencia_seleccion : IF '(' condicion ')' THEN bloque_sentencias_del ELSE bloque_sentencias_del END_IF",
"sentencia_seleccion : IF '(' condicion ')' THEN bloque_sentencias_del END_IF",
"sentencia_seleccion : IF condicion THEN bloque_sentencias_del END_IF",
"sentencia_seleccion : IF '(' condicion ')' THEN bloque_sentencias_del error",
"sentencia_seleccion : IF '(' condicion ')' THEN error END_IF",
"sentencia_seleccion : IF '(' condicion ')' THEN bloque_sentencias_del ELSE error END_IF",
"bloque_sentencias_del : sentencias_ejecutables",
"bloque_sentencias_del : BEGIN bloque_ejecutable END",
"bloque_sentencias_del : BEGIN error END",
"bloque_sentencias_del : error bloque_ejecutable END",
"bloque_sentencias_del : BEGIN bloque_ejecutable error",
"bloque_ejecutable : bloque_ejecutable sentencias_ejecutables",
"bloque_ejecutable : sentencias_ejecutables",
"bloque_ejecutable : sentencias_declarativas",
"condicion : expr_aritmetic comparador expr_aritmetic",
"condicion : expr_aritmetic comparador error",
"condicion : comparador expr_aritmetic error",
"condicion : expr_aritmetic error",
"comparador : '='",
"comparador : DIST",
"comparador : '>'",
"comparador : '<'",
"comparador : MAYOR_IGUAL",
"comparador : MENOR_IGUAL",
"sentencia_out : OUTF '(' CADENA ')'",
"sentencia_out : OUTF '(' expr_aritmetic ')'",
"sentencia_out : OUTF '(' ')' error",
"sentencia_out : OUTF '(' error",
"sentencia_out : OUTF '(' CADENA error",
"sentencia_out : OUTF CADENA ')' error",
"declaracion_tipo_subrango : TYPEDEF ID ASIG tipo rango",
"declaracion_tipo_subrango : TYPEDEF error",
"declaracion_tipo_subrango : TYPEDEF ID ASIG error",
"rango : '{' const ',' const '}'",
"rango : error",
"sentencia_while : WHILE '(' condicion ')' bloque_sentencias_del",
"sentencia_while : WHILE condicion bloque_sentencias_del",
"sentencia_while : error '(' condicion ')' bloque_sentencias_del",
"declaracion_tipo_pair : TYPEDEF PAIR '<' tipo '>' ID",
"declaracion_tipo_pair : TYPEDEF error '<' tipo '>' ID",
"declaracion_tipo_pair : TYPEDEF PAIR tipo ID",
"declaracion_tipo_pair : TYPEDEF PAIR '<' tipo '>' error",
"declaracion_tipo_pair : TYPEDEF PAIR '<' error '>' ID",
"sentencia_goto : llamado_etiqueta",
"llamado_etiqueta : GOTO etiqueta",
"llamado_etiqueta : GOTO error",
"etiqueta : ETIQUETA",
"var_pair : ID '{' CTE '}'",
"expr_aritmetic : expr_aritmetic '+' termino",
"expr_aritmetic : expr_aritmetic '-' termino",
"expr_aritmetic : error '+' termino",
"expr_aritmetic : expr_aritmetic '+' error",
"expr_aritmetic : error '-' termino",
"expr_aritmetic : expr_aritmetic '-' error",
"expr_aritmetic : termino",
"termino : termino '*' factor",
"termino : termino '/' factor",
"termino : factor",
"termino : invocacion_funcion",
"factor : ID",
"factor : const",
"const : CTE",
"const : '-' CTE",
};

//#line 247 "gramatica.y"

//Funciones
public static ArrayList<String> errores = new ArrayList<String>();
public static  List<String> warnings = new ArrayList<>();
public static ArrayList<String> erroreslex = new ArrayList<String>();
public static  List<String> warningslex = new ArrayList<>();

int yylex() throws IOException {

        int identificador_token = 0;
        Reader lector = compilador.AnalizadorLexico.lector;
        compilador.AnalizadorLexico.estado_actual = 0;
        while (! EOF(lector)) {
          char caracter = nextChar(lector);
          identificador_token = AnalizadorLexico.proximoEstado(lector, caracter);
          if (identificador_token != ConstantesCompilador.EN_LECTURA) {
            yylval = new ParserVal(AnalizadorLexico.getTokenActual());
            AnalizadorLexico.token_actual.delete(0, AnalizadorLexico.token_actual.length());
            return identificador_token;
          }
        }
        return identificador_token;
}

 public static char nextChar(Reader reader) throws IOException {
        reader.mark(1);
        char next_char = (char) reader.read();
        reader.reset();
        return next_char;
      }

      
      private boolean EOF(Reader lector) throws IOException {
        lector.mark(1);
        int value = lector.read();
        lector.reset();
        return value == -1;
      }

public static void agregarError(int tipo, String clase, String error)
      {
        if ( tipo == ConstantesCompilador.ERROR && clase == ConstantesCompilador.SINTACTICO)
          errores.add("Linea: "+ AnalizadorLexico.getLineaActual()+ " (ERROR) " + clase + ": "+ error);
        else
          warnings.add("Linea: "+AnalizadorLexico.getLineaActual()+ " (WARNING) "+ clase + ": "+ error);

      }
     
public static void agregarErrorLex(int tipo, String clase, String error)
      {
        if ( tipo == ConstantesCompilador.ERROR && clase == ConstantesCompilador.LEXICO)
          erroreslex.add("Linea: "+ AnalizadorLexico.getLineaActual()+ " (ERROR) " + clase + ": "+ error);
        else
          warningslex.add("Linea: "+AnalizadorLexico.getLineaActual()+ " (WARNING) "+ clase + ": "+ error);

      }

public static void imprimirErrores()
{   System.out.println("Errores Lexicos: ");
    if ( erroreslex.size() > 0)
        {
            System.out.println("No se ha podido compilar el programa debido a los siguientes errores: ");
            for (String error : erroreslex)
                System.err.println(error);
        } else {
            System.out.println("Se ha terminado de compilar correctamente.");
        }
            if ( warningslex.size() > 0)
        {
            for (String Warning : warningslex)
                System.out.println(Warning);
        }
     

    System.out.println("Errores Sintacticos: ");
    if ( errores.size() > 0)
        {
            System.out.println("No se ha podido compilar el programa debido a los siguientes errores: ");
            for (String error : errores)
                System.err.println(error);
        } else {
            System.out.println("Se ha terminado de compilar correctamente.");
        }
            if ( warnings.size() > 0)
        {
            for (String Warning : warnings)
                System.out.println(Warning);
        }
}


public static void ConstanteNegativa(String lexema){

    if(lexema.contains("x")){
      String sinPrefijo = lexema.startsWith("-0x") 
                            ? lexema.substring(3) 
                            : lexema.startsWith("0x") 
                            ? lexema.substring(2) 
                            : lexema;
       lexema = sinPrefijo;
        if (Integer.parseInt(lexema,16) > ConstantesCompilador.MAX_INT_POSITIVO+1) {
             Parser.agregarErrorLex(ConstantesCompilador.WARNING, ConstantesCompilador.LEXICO, "Constante Hexadecimal negativa entera fuera del rango permitido " );
             lexema = "0x" + Integer.toHexString(ConstantesCompilador.MAX_INT_POSITIVO+1).toUpperCase();
             
             
             
           }
      }else{
     if(! lexema.contains(".")) {
         if (Integer.parseInt(lexema) > ConstantesCompilador.MAX_INT_POSITIVO+1) {
             Parser.agregarErrorLex(ConstantesCompilador.WARNING, ConstantesCompilador.LEXICO, "Constante negativa entera fuera del rango permitido " );
             lexema = String.valueOf(ConstantesCompilador.MAX_INT_POSITIVO+1);
             
           }
     } }
     
     TablaDeSimbolos.obtenerSimbolo(lexema).setLexema("-" + lexema);
 }

public static void ConstantePositiva(String lexema){
    if(lexema.contains("x")){
      String sinPrefijo = lexema.startsWith("-0x") 
                            ? lexema.substring(3) 
                            : lexema.startsWith("0x") 
                            ? lexema.substring(2) 
                            : lexema;
       lexema = sinPrefijo;
       if (Integer.parseInt(lexema,16) > ConstantesCompilador.MAX_INT_POSITIVO) {
            Parser.agregarErrorLex(ConstantesCompilador.WARNING, ConstantesCompilador.LEXICO, "Constante Hexadecimal positiva entera fuera del rango permitido " );
            lexema = "0x" + Integer.toHexString(ConstantesCompilador.MAX_INT_POSITIVO+1).toUpperCase();
            TablaDeSimbolos.obtenerSimbolo(lexema).setLexema("0x7FFF");
          }
      }else{
    if(! lexema.contains(".")) {
        if (Integer.parseInt(lexema) > ConstantesCompilador.MAX_INT_POSITIVO) {
            Parser.agregarErrorLex(ConstantesCompilador.WARNING, ConstantesCompilador.LEXICO, "Constante positiva entera fuera del rango permitido " );
            lexema = String.valueOf(ConstantesCompilador.MAX_INT_POSITIVO+1);
            TablaDeSimbolos.obtenerSimbolo(lexema).setLexema(String.valueOf(ConstantesCompilador.MAX_INT_POSITIVO));
          }
      }}
   
}


void yyerror(String error) {
    // funcion utilizada para imprimir errores que produce yacc
    System.out.println("Yacc reporto un error: " + error);
}
//#line 727 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse() throws IOException
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 2:
//#line 29 "gramatica.y"
{System.out.println("Linea "+ AnalizadorLexico.getLineaActual() + ", Se reconocio el programa " +  val_peek(3).sval);}
break;
case 3:
//#line 30 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Se esperaban sentencias de ejecucion");}
break;
case 4:
//#line 31 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de delimitador de programa: END");}
break;
case 5:
//#line 32 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de delimitador de programa: BEGIN");}
break;
case 6:
//#line 33 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR,ConstantesCompilador.SINTACTICO, "Se esperaba un nombre de programa");}
break;
case 7:
//#line 34 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Error sintactico al compilar no permite terminar de leer el programa de forma correcta");}
break;
case 18:
//#line 54 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR,ConstantesCompilador.SINTACTICO, "Se esperaba un ; luego de la declaracion de variable");}
break;
case 19:
//#line 55 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR,ConstantesCompilador.SINTACTICO, "Se esperaba un ; luego de la declaracion de sub-tipo");}
break;
case 20:
//#line 56 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR,ConstantesCompilador.SINTACTICO, "Se esperaba un ; luego de la declaracion del tipo pair");}
break;
case 21:
//#line 57 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR,ConstantesCompilador.SINTACTICO, "Se esperaba un ; luego de la declaracion de la funcion");}
break;
case 22:
//#line 60 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio una declaracion de variable");}
break;
case 23:
//#line 64 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio la declaracion de funcion con su valor de retorno");}
break;
case 24:
//#line 65 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de nombre en la función");}
break;
case 25:
//#line 66 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta parametro");}
break;
case 26:
//#line 67 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta parametro");}
break;
case 27:
//#line 68 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta tipo");}
break;
case 28:
//#line 69 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "error en la cantidad de parametros");}
break;
case 29:
//#line 70 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta palabra reservada FUN");}
break;
case 30:
//#line 73 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio parametro");}
break;
case 31:
//#line 74 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Se espera un identificador luego del tipo");}
break;
case 32:
//#line 75 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de tipo de parámetro formal en declaración de función"); }
break;
case 34:
//#line 80 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de sentencia RET en la función");}
break;
case 36:
//#line 84 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio una sentencia de retorno");}
break;
case 37:
//#line 85 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Debe especificar el valor a retornar");}
break;
case 38:
//#line 86 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta RET");}
break;
case 39:
//#line 87 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta el ';' despues del Retorno");}
break;
case 40:
//#line 88 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta parentesis");}
break;
case 41:
//#line 89 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta parentesis para cerrar");}
break;
case 42:
//#line 90 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta parentesis para abrir");}
break;
case 43:
//#line 91 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Retorno vacio");}
break;
case 44:
//#line 94 "gramatica.y"
{ System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio el llamado a la funcion " + val_peek(3).sval );}
break;
case 47:
//#line 99 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de ',' en la declaracion de variables");}
break;
case 55:
//#line 112 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Error en la sentencia asignacion falta ';' ");}
break;
case 56:
//#line 113 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Error en la sentencia seleccion falta ';' ");}
break;
case 57:
//#line 114 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Error en la sentencia out falta ';' ");}
break;
case 58:
//#line 115 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Error en la sentencia while falta ';' ");}
break;
case 59:
//#line 116 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Error en la sentencia goto falta ';' ");}
break;
case 60:
//#line 120 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio la asignacion ");}
break;
case 61:
//#line 121 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Error en la expresion de la asignacion");}
break;
case 62:
//#line 122 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio la asignacion ");}
break;
case 65:
//#line 127 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio lista multiple");}
break;
case 66:
//#line 128 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Error, Falta ','");}
break;
case 67:
//#line 133 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio una sentencia de control IF");}
break;
case 68:
//#line 134 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio una sentencia de control IF");}
break;
case 69:
//#line 135 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de paréntesis en condición de selección"); }
break;
case 70:
//#line 136 "gramatica.y"
{  agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de END_IF"); }
break;
case 71:
//#line 137 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de contenido en bloque THEN"); }
break;
case 72:
//#line 138 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de contenido en bloque ELSE"); }
break;
case 75:
//#line 143 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de contenido en bloque"); }
break;
case 76:
//#line 144 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta el BEGIN"); }
break;
case 77:
//#line 145 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta el END"); }
break;
case 80:
//#line 150 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "No se permiten sentencias declarativas"); }
break;
case 82:
//#line 154 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de expresion aritmetica en comparación"); }
break;
case 83:
//#line 155 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de expresion aritmetica en comparación"); }
break;
case 84:
//#line 156 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de comparador en expresion"); }
break;
case 85:
//#line 159 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio la condicion de comparacion por igual");}
break;
case 86:
//#line 160 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio la condicion de comparacion por distinto");}
break;
case 87:
//#line 161 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio la condicion de comparacion por mayor");}
break;
case 88:
//#line 162 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio la condicion de comparacion por menor");}
break;
case 89:
//#line 163 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio la condicion de comparacion por mayor igual");}
break;
case 90:
//#line 164 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio la condicion de comparacion por menor igual");}
break;
case 91:
//#line 168 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio una sentencia OUTF");}
break;
case 92:
//#line 169 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio una sentencia OUTF");}
break;
case 93:
//#line 170 "gramatica.y"
{  agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta parámetro en sentencia OUTF"); }
break;
case 94:
//#line 171 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Parámetro incorrecto en sentencia OUTF");}
break;
case 95:
//#line 172 "gramatica.y"
{  agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta el parentesis de cierre"); }
break;
case 96:
//#line 173 "gramatica.y"
{  agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta el parentesis de apertura"); }
break;
case 97:
//#line 178 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio una declaracion tipo_subrango");}
break;
case 98:
//#line 179 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta nombre del tipo definido"); }
break;
case 99:
//#line 180 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta tipo base"); }
break;
case 101:
//#line 184 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de {} en el rango");}
break;
case 102:
//#line 188 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio una sentencia  WHILE");}
break;
case 103:
//#line 189 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de parentesis en la iteración"); }
break;
case 104:
//#line 190 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta WHILE"); }
break;
case 105:
//#line 195 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio PAIR");}
break;
case 106:
//#line 196 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta PAIR"); }
break;
case 107:
//#line 197 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta '<>' en la declaración de PAIR"); }
break;
case 108:
//#line 198 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta identificador al final de la declaración"); }
break;
case 109:
//#line 199 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta tipo en la declaración de PAIR"); }
break;
case 110:
//#line 203 "gramatica.y"
{System.out.println("Linea "+ AnalizadorLexico.getLineaActual() + ", Se reconocio una sentencia GOTO");}
break;
case 111:
//#line 206 "gramatica.y"
{System.out.println("Linea "+ AnalizadorLexico.getLineaActual() + ", Se reconocio una sentencia GOTO");}
break;
case 112:
//#line 207 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta etiqueta en la sentencia GOTO"); }
break;
case 113:
//#line 210 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio la etiqueta " + val_peek(0).sval);}
break;
case 117:
//#line 219 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de operando en expresión.");}
break;
case 118:
//#line 220 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de operando en expresión.");}
break;
case 119:
//#line 221 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de operando en expresión." );}
break;
case 120:
//#line 222 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de operando en expresión.");}
break;
case 128:
//#line 236 "gramatica.y"
{ System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio " + val_peek(0).sval + " CTE positiva");
     ConstantePositiva(val_peek(0).sval);
     yyval.sval = val_peek(0).sval;}
break;
case 129:
//#line 239 "gramatica.y"
{ System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio " + val_peek(1).sval + val_peek(0).sval + " CTE negativa");
     ConstanteNegativa(val_peek(0).sval);
     yyval.sval = ('-' + val_peek(0).sval);}
break;
//#line 1236 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 * @throws IOException 
 */
public void run() throws IOException
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################

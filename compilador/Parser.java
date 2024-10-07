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
    5,    8,   11,   11,   11,   11,   11,   11,   14,   14,
   14,   15,   15,   15,   16,   16,   16,   16,   16,   16,
   16,   18,   13,   13,   13,   12,   12,    6,    6,    6,
    6,    6,    6,    6,    6,    6,    6,   19,   19,   19,
   24,   24,   24,   24,   20,   20,   20,   20,   20,   20,
   27,   27,   27,   27,   28,   28,   28,   26,   26,   26,
   26,   29,   29,   29,   29,   29,   29,   21,   21,   21,
   21,   21,   21,    9,    9,    9,   30,   30,   22,   22,
   22,   10,   10,   10,   10,   10,   23,   31,   31,    7,
   25,   17,   17,   17,   17,   17,   17,   17,   32,   32,
   32,   32,   33,   33,   34,   34,
};
final static short yylen[] = {                            2,
    1,    4,    4,    4,    4,    4,    2,    1,    2,    1,
    1,    1,    1,    2,    2,    2,    2,    2,    2,    2,
    1,    2,    9,    3,    6,    7,    9,    9,    2,    2,
    2,    2,    2,    1,    5,    2,    3,    5,    3,    4,
    4,    4,    1,    3,    3,    1,    1,    2,    2,    2,
    2,    2,    2,    2,    2,    2,    2,    3,    3,    3,
    1,    1,    3,    4,    9,    7,    5,    7,    7,    9,
    3,    3,    3,    3,    2,    1,    1,    3,    3,    3,
    2,    1,    1,    1,    1,    1,    1,    4,    4,    4,
    3,    4,    4,    5,    2,    4,    5,    1,    5,    3,
    5,    6,    6,    4,    6,    6,    1,    2,    2,    1,
    4,    3,    3,    3,    3,    3,    3,    1,    3,    3,
    1,    1,    1,    1,    1,    2,
};
final static short yydefred[] = {                         0,
    8,    0,    0,    1,    0,    0,    0,    0,    0,    0,
    0,    0,   46,   47,  110,    0,   10,   11,   12,   13,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  107,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   86,   87,   83,  125,    0,    0,
   82,   84,   85,    0,  122,    0,    0,    0,  121,  124,
    0,    0,    0,    0,    0,    0,    0,  109,  108,    0,
    9,   18,   14,   19,   15,   20,   16,   17,    0,   22,
    0,   53,   48,   54,   49,   55,   50,   56,   51,   57,
   52,    0,    0,    0,    0,    0,    0,   44,    0,   45,
    0,    0,    0,  126,    0,   81,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  100,    6,   24,    0,    0,
    0,    0,    0,   62,    0,    3,    0,    2,    5,    0,
    0,  111,    0,    0,    0,    0,  115,    0,  117,    0,
    0,    0,    0,   80,  123,  119,  120,   93,   92,   88,
   90,   89,    0,   96,    0,    0,    0,  104,    0,   77,
   76,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  101,   42,    0,   67,    0,   98,    0,   94,    0,
    0,   99,    0,   73,   75,   72,    0,   71,    0,    0,
    0,    0,   34,    0,    0,    0,    0,   31,   30,   29,
    0,    0,    0,  103,    0,  106,  105,  102,    0,    0,
    0,    0,    0,   32,    0,   25,    0,    0,   64,    0,
   69,   68,    0,   66,    0,    0,   39,    0,   37,   26,
    0,    0,    0,    0,    0,    0,   40,    0,   41,    0,
    0,   27,   70,   65,   97,   38,   35,   23,   28,
};
final static short yydgoto[] = {                          3,
    4,    5,  201,   17,   18,   19,   20,   21,   22,   23,
   24,   25,   26,  181,  202,  203,   54,   55,   27,   28,
   29,   30,   31,  133,   32,   56,  126,  172,   57,  189,
   33,   58,   59,   60,
};
final static short yysindex[] = {                      -122,
    0,  315,    0,    0,  201,  -36,   -5,  107,  -39, -108,
  113, -223,    0,    0,    0,  162,    0,    0,    0,    0,
  -37,  -34,   -6,   11,  -52, -217,   26,   28,   34,   45,
   56, -194,    0,  -36,  242,  275, -136,  119,  -42,  -70,
  -79,  -28,  114,  161,    0,    0,    0,    0,  -26,  119,
    0,    0,    0,   41,    0,  -68,  -31,  157,    0,    0,
  194,  -24,  196,   14,  -55,  119, -215,    0,    0,   27,
    0,    0,    0,    0,    0,    0,    0,    0,  -85,    0,
  -13,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -31,   36,  291,   38,  230,  266,    0,  198,    0,
   81,   81,  -31,    0,  288,    0,   18,   31,   49, -215,
   91,   88,   88,   75,  114,  -33,  110,  216,  -59, -110,
  -86,   80,  336,  369,  384,    0,    0,    0,  -16,  114,
  -14,  166,   93,    0,  166,    0,  -36,    0,    0,  -63,
 -215,    0,  157,  157,  399,  136,    0,  157,    0,  157,
  114,  166,  147,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  359,    0, -100,  371,  373,    0, -215,    0,
    0,  -62,   15,  217,   76,  -38,  -31,  -31,  144,  156,
  398,    0,    0, -101,    0,  186,    0,  170,    0,  192,
  172,    0,  414,    0,    0,    0,  414,    0,   43,  -31,
   92,  202,    0,  207,  381,  166,   98,    0,    0,    0,
  206,  339,  151,    0,  424,    0,    0,    0,  114,  -31,
   86,  407,  -36,    0,  214,    0,  221,  -63,    0,   76,
    0,    0,  -73,    0,  209,   97,    0,  222,    0,    0,
   76,  442,  226,  354,  227,  364,    0,   65,    0,  229,
  228,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,  223,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   60,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  492,    0,    0,    0,    0,  117,    0,
    0,    0,    0,  -32,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  -25,    0,    0,
    0,    0,  118,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  126,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  127,
   63,   13,  133,    0,  138,    0,  497,    0,    0,    0,
    0,    0,   -1,    6,    0,    0,    0,   30,    0,   37,
    7,  141,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -53,    0,    0,    0,
    0,    0,    0,    0,    0,   20,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  239,    0,
    0,    0,  240,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,    0,  149,  362,  284,  281,  491,    0,    0,    0,
    0,  296,  105,  -65, -113,  304,  342,    0,    0,    0,
    0,    0,    0,    0,  427,  112,  262,  386,  455,    0,
    0,   12,  348,    0,
};
final static int YYTABLESIZE=660;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        123,
   62,   40,  204,   38,  121,   74,  118,  160,  123,  123,
  123,  123,  123,   49,  123,  118,  117,  118,  118,  118,
   49,   73,  188,  176,   75,  103,  123,  123,  123,  123,
  114,   49,   68,  118,  118,  118,  118,  116,   40,  114,
  124,  114,  114,  114,   61,  125,  116,   79,  116,  116,
  116,   63,   77,   81,   38,   15,   61,  114,  114,  114,
  114,  112,   49,   63,  116,  116,  116,  116,  113,   78,
  112,   61,  112,  112,  112,   49,   92,  113,   63,  113,
  113,  113,  220,  107,   83,  108,   85,   49,  112,  112,
  112,  112,   87,   49,  123,  113,  113,  113,  113,   21,
   53,   51,   52,   89,  123,  123,  123,  123,   41,  123,
  205,   42,  143,  144,   91,  200,  243,   41,  148,  150,
   96,  123,   67,  257,  178,   49,  238,  250,  107,   80,
  108,  200,   49,  107,    1,  108,  177,  248,    2,  107,
  107,  108,  108,   42,   98,  164,   50,   63,   64,   97,
   16,   49,   66,   36,  212,  187,  101,   49,  102,  125,
   65,  105,  242,   49,   13,   14,   53,   51,   52,  166,
  128,  129,   53,   51,   52,   43,   95,  123,   53,   51,
   52,   78,  244,   94,   91,   59,   39,  125,   13,   14,
  110,   58,  179,  193,    7,    8,   60,   99,  112,  194,
  103,    9,   74,  113,   39,   11,   74,   12,  107,   74,
  108,   13,   14,   79,   39,   13,   14,  179,   72,   13,
   14,   74,  159,  123,   43,   44,  123,  100,  123,   37,
  118,  115,   44,  118,  114,  118,   13,   14,   61,  123,
  123,  123,  130,  131,  175,   48,  118,  118,  118,   76,
  104,   39,   48,  116,  114,  119,  162,  114,  107,  114,
  108,  116,   79,   48,  116,   79,  116,   79,   61,  140,
  114,  114,  114,  147,   44,   63,  196,  116,  116,  116,
   37,   82,  127,   84,  120,  112,  149,   44,  112,   86,
  112,  136,  113,  139,   48,  113,  106,  113,  219,   44,
   88,  112,  112,  112,  151,   44,  141,   48,  113,  113,
  113,   90,   45,   46,   47,   21,   21,   21,  123,   48,
  256,   21,  142,   21,   21,   48,   21,   21,  146,   21,
  158,    6,    7,    8,   21,   21,  168,   44,   21,    9,
   10,  237,  199,   11,  155,   12,  154,  223,    7,    8,
   13,   14,  247,  229,   15,    9,   10,   48,  199,   11,
  122,   12,   43,   44,   48,  161,   13,   14,   43,   44,
   15,  153,   43,   95,   43,   44,  169,   71,   45,   46,
   47,   91,   59,   48,   45,   46,   47,   43,   58,   48,
   45,   46,   47,   60,  184,   48,   78,   71,  111,   78,
  208,   78,  182,  118,  171,  171,  232,  170,  170,  185,
  233,  209,  210,  234,  163,  165,  167,    6,    7,    8,
  186,  227,  132,   70,  228,    9,   10,  217,  218,   11,
  192,   12,  190,  135,  191,  180,   13,   14,  211,  183,
   15,  107,  214,  108,  145,  213,  215,  239,  216,  107,
  152,  108,  195,   38,  195,   71,   34,    7,    8,  156,
  157,   35,  226,  225,    9,   10,  230,  235,   11,  240,
   12,  180,  197,    7,    8,   13,   14,  249,  198,   15,
    9,  241,  251,  259,   11,  246,   12,  252,  255,  254,
  258,    7,  171,   43,  245,  170,    4,    6,    7,    8,
   36,   33,   69,   93,  224,    9,   10,  134,  109,   11,
  174,   12,    0,    0,    0,    0,   13,   14,  206,  207,
   15,    0,    0,  180,  171,    0,    0,  170,    0,    0,
    6,    7,    8,    0,    0,    0,   95,    0,    9,   10,
  221,  222,   11,    0,   12,    0,  137,    7,    8,   13,
   14,    0,  138,   15,    9,   10,    0,    0,   11,    0,
   12,  236,   71,    0,    0,   13,   14,    0,    0,   15,
    6,    7,    8,    0,    0,    0,    0,    0,    9,   10,
    0,    0,   11,    0,   12,    0,    0,    0,    0,   13,
   14,    0,    0,   15,    6,    7,    8,    0,    0,    0,
    0,  231,    9,   10,    0,    0,   11,    0,   12,    6,
    7,    8,    0,   13,   14,    0,  253,    9,   10,    0,
    0,   11,    0,   12,    6,    7,    8,    0,   13,   14,
    0,    0,    9,   10,    0,    0,   11,    0,   12,  173,
    7,    8,    0,   13,   14,    0,    0,    9,   10,    0,
    0,   11,    0,   12,    0,    0,    0,    0,   13,   14,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         32,
   40,   44,   41,   40,   60,   59,   32,   41,   41,   42,
   43,   44,   45,   45,   47,   41,   41,   43,   44,   45,
   45,   59,  123,   40,   59,   40,   59,   60,   61,   62,
   32,   45,  256,   59,   60,   61,   62,   32,   44,   41,
  256,   43,   44,   45,   32,  261,   41,   41,   43,   44,
   45,   32,   59,  271,   40,  279,   44,   59,   60,   61,
   62,   32,   45,   44,   59,   60,   61,   62,   32,   59,
   41,   59,   43,   44,   45,   45,  271,   41,   59,   43,
   44,   45,   40,   43,   59,   45,   59,   45,   59,   60,
   61,   62,   59,   45,   32,   59,   60,   61,   62,   40,
   60,   61,   62,   59,   42,   43,   44,   45,  123,   47,
  176,    7,  101,  102,   59,   40,  230,  123,  107,  108,
  257,   59,   11,   59,   32,   45,   41,  241,   43,   25,
   45,   40,   45,   43,  257,   45,   44,   41,  261,   43,
   43,   45,   45,   39,   40,  256,   40,  256,  257,   38,
    2,   45,   40,    5,  256,  256,   43,   45,   45,  261,
  269,   50,  228,   45,  275,  276,   60,   61,   62,  256,
  256,  257,   60,   61,   62,   59,   59,   66,   60,   61,
   62,   41,  256,   35,   59,   59,  257,  261,  275,  276,
  259,   59,  256,  256,  257,  258,   59,  277,   42,  262,
   40,  264,  256,   47,  257,  268,  260,  270,   43,  263,
   45,  275,  276,  266,  257,  275,  276,  256,  256,  275,
  276,  256,  256,  256,  256,  257,  259,  256,  261,  266,
  256,  256,  257,  259,   41,  261,  275,  276,  278,  272,
  273,  274,  256,  257,  261,  277,  272,  273,  274,  256,
  277,  257,  277,  278,  256,   60,   41,  259,   43,  261,
   45,  256,  256,  277,  259,  259,  261,  261,  256,   40,
  272,  273,  274,  256,  257,  256,  262,  272,  273,  274,
  266,  256,  256,  256,  271,  256,  256,  257,  259,  256,
  261,  256,  256,  256,  277,  259,  256,  261,  256,  257,
  256,  272,  273,  274,  256,  257,   41,  277,  272,  273,
  274,  256,  272,  273,  274,  256,  257,  258,  256,  277,
  256,  262,  125,  264,  265,  277,  267,  268,   41,  270,
  256,  256,  257,  258,  275,  276,  257,  257,  279,  264,
  265,  256,  267,  268,  257,  270,  256,  256,  257,  258,
  275,  276,  256,  256,  279,  264,  265,  277,  267,  268,
   65,  270,  256,  257,  277,  256,  275,  276,  256,  257,
  279,  110,  256,  256,  256,  257,   41,   16,  272,  273,
  274,  256,  256,  277,  272,  273,  274,  271,  256,  277,
  272,  273,  274,  256,  259,  277,  256,   36,   57,  259,
  257,  261,  141,   62,  124,  125,  256,  124,  125,  263,
  260,  256,  257,  263,  119,  120,  121,  256,  257,  258,
   62,   41,   81,  262,   44,  264,  265,  256,  257,  268,
  169,  270,   62,   92,   62,  140,  275,  276,   41,   41,
  279,   43,  257,   45,  103,  184,  277,   41,  257,   43,
  109,   45,  172,   40,  174,   94,  256,  257,  258,  112,
  113,  261,  256,  262,  264,  265,  261,   44,  268,  256,
  270,  176,  256,  257,  258,  275,  276,  256,  262,  279,
  264,  261,   41,  256,  268,  277,  270,  262,  125,  263,
  262,    0,  212,  271,  233,  212,    0,  256,  257,  258,
  262,  262,   12,  262,  201,  264,  265,   81,   54,  268,
  125,  270,   -1,   -1,   -1,   -1,  275,  276,  177,  178,
  279,   -1,   -1,  228,  244,   -1,   -1,  244,   -1,   -1,
  256,  257,  258,   -1,   -1,   -1,  262,   -1,  264,  265,
  199,  200,  268,   -1,  270,   -1,  256,  257,  258,  275,
  276,   -1,  262,  279,  264,  265,   -1,   -1,  268,   -1,
  270,  220,  201,   -1,   -1,  275,  276,   -1,   -1,  279,
  256,  257,  258,   -1,   -1,   -1,   -1,   -1,  264,  265,
   -1,   -1,  268,   -1,  270,   -1,   -1,   -1,   -1,  275,
  276,   -1,   -1,  279,  256,  257,  258,   -1,   -1,   -1,
   -1,  263,  264,  265,   -1,   -1,  268,   -1,  270,  256,
  257,  258,   -1,  275,  276,   -1,  263,  264,  265,   -1,
   -1,  268,   -1,  270,  256,  257,  258,   -1,  275,  276,
   -1,   -1,  264,  265,   -1,   -1,  268,   -1,  270,  256,
  257,  258,   -1,  275,  276,   -1,   -1,  264,  265,   -1,
   -1,  268,   -1,  270,   -1,   -1,   -1,   -1,  275,  276,
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
"declaracion_funcion : error FUN ID '(' parametro ')' BEGIN cuerpo_funcion END",
"declaracion_funcion : tipo FUN ID '(' parametro ',' parametro ')' error",
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
"sentencia_asignacion : var_pair ASIG expr_aritmetic",
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
"rango : '{' CTE ',' CTE '}'",
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

//#line 243 "gramatica.y"

//Funciones
public static ArrayList<String> errores = new ArrayList<String>();
public static  List<String> warnings = new ArrayList<>();

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
        if ( tipo == ConstantesCompilador.ERROR)
          errores.add("Linea: "+ AnalizadorLexico.getLineaActual()+ " (ERROR) " + clase + ": "+ error);
        else
          warnings.add("Linea: "+AnalizadorLexico.getLineaActual()+ " (WARNING) "+ clase + ": "+ error);

      }

public static void imprimirErrores()
{
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

     if(! lexema.contains(".")) {
         if (Integer.parseInt(lexema) > ConstantesCompilador.MAX_INT_POSITIVO+1) {
             Parser.agregarError(ConstantesCompilador.WARNING, ConstantesCompilador.LEXICO, "Constante entera fuera del rango permitido " );
             lexema = String.valueOf(ConstantesCompilador.MAX_INT_POSITIVO+1);

           }
     }
     TablaDeSimbolos.agregarSimbolo('-' + lexema, ConstantesCompilador.CONSTANTE);
 }

public static void ConstantePositiva(String lexema){
    if(! lexema.contains(".")) {
        if (Integer.parseInt(lexema) > ConstantesCompilador.MAX_INT_POSITIVO) {
            Parser.agregarError(ConstantesCompilador.WARNING, ConstantesCompilador.LEXICO, "Constante entera fuera del rango permitido " );
            lexema = String.valueOf(ConstantesCompilador.MAX_INT_POSITIVO);
          }
      }
}


void yyerror(String error) {
    // funcion utilizada para imprimir errores que produce yacc
    System.out.println("Yacc reporto un error: " + error);
}
//#line 635 "Parser.java"
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
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de delimitador de programa");}
break;
case 5:
//#line 32 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de delimitador de programa");}
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
//#line 72 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio parametro");}
break;
case 30:
//#line 73 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Se espera un identificador luego del tipo");}
break;
case 31:
//#line 74 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de tipo de parámetro formal en declaración de función"); }
break;
case 33:
//#line 79 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de sentencia RET en la función");}
break;
case 35:
//#line 83 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio una sentencia de retorno");}
break;
case 36:
//#line 84 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Debe especificar el valor a retornar");}
break;
case 37:
//#line 85 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta RET");}
break;
case 38:
//#line 86 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta el ';' despues del Retorno");}
break;
case 39:
//#line 87 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta parentesis");}
break;
case 40:
//#line 88 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta parentesis para cerrar");}
break;
case 41:
//#line 89 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta parentesis para abrir");}
break;
case 42:
//#line 92 "gramatica.y"
{ System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio el llamado a la funcion " + val_peek(3).sval );}
break;
case 45:
//#line 97 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de ',' en la declaracion de variables");}
break;
case 53:
//#line 110 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Error en la sentencia asignacion falta ';' ");}
break;
case 54:
//#line 111 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Error en la sentencia seleccion falta ';' ");}
break;
case 55:
//#line 112 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Error en la sentencia out falta ';' ");}
break;
case 56:
//#line 113 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Error en la sentencia while falta ';' ");}
break;
case 57:
//#line 114 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Error en la sentencia goto falta ';' ");}
break;
case 58:
//#line 118 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio la asignacion ");}
break;
case 59:
//#line 119 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Error en la expresion de la asignacion");}
break;
case 63:
//#line 125 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio lista multiple");}
break;
case 64:
//#line 126 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Error, Falta ','");}
break;
case 65:
//#line 131 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio una sentencia de control IF");}
break;
case 66:
//#line 132 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio una sentencia de control IF");}
break;
case 67:
//#line 133 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de paréntesis en condición de selección"); }
break;
case 68:
//#line 134 "gramatica.y"
{  agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de END_IF"); }
break;
case 69:
//#line 135 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de contenido en bloque THEN"); }
break;
case 70:
//#line 136 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de contenido en bloque ELSE"); }
break;
case 72:
//#line 140 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de contenido en bloque"); }
break;
case 73:
//#line 141 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta el BEGIN"); }
break;
case 74:
//#line 142 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta el END"); }
break;
case 77:
//#line 147 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "No se permiten sentencias declarativas"); }
break;
case 79:
//#line 151 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de expresion aritmetica en comparación"); }
break;
case 80:
//#line 152 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de expresion aritmetica en comparación"); }
break;
case 81:
//#line 153 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de comparador en expresion"); }
break;
case 82:
//#line 156 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio la condicion de comparacion por igual");}
break;
case 83:
//#line 157 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio la condicion de comparacion por distinto");}
break;
case 84:
//#line 158 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio la condicion de comparacion por mayor");}
break;
case 85:
//#line 159 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio la condicion de comparacion por menor");}
break;
case 86:
//#line 160 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio la condicion de comparacion por mayor igual");}
break;
case 87:
//#line 161 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio la condicion de comparacion por menor igual");}
break;
case 88:
//#line 165 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio una sentencia OUTF");}
break;
case 89:
//#line 166 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio una sentencia OUTF");}
break;
case 90:
//#line 167 "gramatica.y"
{  agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta parámetro en sentencia OUTF"); }
break;
case 91:
//#line 168 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Parámetro incorrecto en sentencia OUTF");}
break;
case 92:
//#line 169 "gramatica.y"
{  agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta el parentesis de cierre"); }
break;
case 93:
//#line 170 "gramatica.y"
{  agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta el parentesis de apertura"); }
break;
case 94:
//#line 175 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio una declaracion tipo_subrango");}
break;
case 95:
//#line 176 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta nombre del tipo definido"); }
break;
case 96:
//#line 177 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta tipo base"); }
break;
case 98:
//#line 181 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de {} en el rango");}
break;
case 99:
//#line 185 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio una sentencia  WHILE");}
break;
case 100:
//#line 186 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de parentesis en la iteración"); }
break;
case 101:
//#line 187 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta WHILE"); }
break;
case 102:
//#line 192 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio PAIR");}
break;
case 103:
//#line 193 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta PAIR"); }
break;
case 104:
//#line 194 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta '<>' en la declaración de PAIR"); }
break;
case 105:
//#line 195 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta identificador al final de la declaración"); }
break;
case 106:
//#line 196 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta tipo en la declaración de PAIR"); }
break;
case 107:
//#line 200 "gramatica.y"
{System.out.println("Linea "+ AnalizadorLexico.getLineaActual() + ", Se reconocio una sentencia GOTO");}
break;
case 108:
//#line 203 "gramatica.y"
{System.out.println("Linea "+ AnalizadorLexico.getLineaActual() + ", Se reconocio una sentencia GOTO");}
break;
case 109:
//#line 204 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta etiqueta en la sentencia GOTO"); }
break;
case 110:
//#line 207 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio la etiqueta " + val_peek(0).sval);}
break;
case 114:
//#line 215 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de operando en expresión.");}
break;
case 115:
//#line 216 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de operando en expresión.");}
break;
case 116:
//#line 217 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de operando en expresión." );}
break;
case 117:
//#line 218 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de operando en expresión.");}
break;
case 125:
//#line 232 "gramatica.y"
{ System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio " + val_peek(0).sval + " CTE positiva");
     ConstantePositiva(val_peek(0).sval);
     yyval.sval = val_peek(0).sval;}
break;
case 126:
//#line 235 "gramatica.y"
{ System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio " + val_peek(1).sval + val_peek(0).sval + " CTE negativa");
     ConstanteNegativa(val_peek(0).sval);
     yyval.sval = ('-' + val_peek(0).sval);}
break;
//#line 1132 "Parser.java"
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
 */
public void run()
{
  try {
    yyparse();
  } catch (IOException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
  }
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

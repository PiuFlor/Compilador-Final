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

import GeneracionCodigoIntermedio.ArbolSintactico;
import GeneracionCodigoIntermedio.NodoComun;
import GeneracionCodigoIntermedio.NodoControl;
import GeneracionCodigoIntermedio.NodoHoja;
import GeneracionCodigoIntermedio.NodoMultipleAsignacion;

import java.util.Collections;

//#line 35 "Parser.java"




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
    5,    5,    8,   12,   12,   12,   12,   15,   15,   15,
   15,   16,   16,   16,   17,   17,   17,   17,   18,   18,
   18,   18,   18,   18,   18,   18,   20,   14,   14,   14,
   13,   13,    6,    6,    6,    6,    6,    6,    6,    6,
    6,    6,   21,   21,   21,   26,   26,   26,   26,   22,
   22,   22,   22,   22,   22,   29,   29,   29,   29,   29,
   30,   30,   30,   28,   28,   28,   28,   31,   31,   31,
   31,   31,   31,   23,   23,   23,   23,   23,   23,   10,
   10,   10,   32,   32,   24,   24,   24,   11,   11,   11,
   11,   11,   25,   34,   34,    7,   27,    9,   19,   19,
   19,   19,   19,   19,   19,   35,   35,   35,   35,   36,
   36,   33,   33,
};
final static short yylen[] = {                            2,
    1,    4,    4,    4,    4,    4,    2,    1,    2,    1,
    1,    1,    1,    2,    2,    2,    2,    2,    2,    2,
    2,    1,    2,    7,    4,    5,    7,    3,    3,    2,
    2,    2,    2,    2,    2,    3,    2,    1,    5,    2,
    3,    5,    3,    4,    4,    5,    4,    1,    3,    3,
    1,    1,    2,    2,    2,    2,    2,    2,    2,    2,
    2,    2,    3,    3,    3,    1,    1,    3,    4,    9,
    7,    5,    7,    7,    9,    1,    3,    3,    3,    3,
    2,    1,    1,    3,    3,    3,    2,    1,    1,    1,
    1,    1,    1,    4,    4,    4,    3,    4,    4,    5,
    2,    4,    5,    1,    5,    3,    5,    6,    6,    4,
    6,    6,    1,    2,    2,    1,    4,    2,    3,    3,
    3,    3,    3,    3,    1,    3,    3,    1,    1,    1,
    1,    1,    2,
};
final static short yydefred[] = {                         0,
    8,    0,    0,    1,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   51,   52,  116,    0,   10,   11,   12,
   13,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  113,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   92,   93,   89,  132,
    0,    0,   88,   90,   91,    0,  129,    0,    0,  131,
    0,  128,    0,    0,    0,    0,    0,   30,    0,    0,
  115,  114,    0,    9,   19,   14,   15,   20,   16,   21,
   17,   18,    0,    0,   23,    0,    0,    0,   58,   53,
   59,   54,   60,   55,   61,   56,   62,   57,    0,    0,
    0,    0,    0,    0,   49,    0,   50,    0,    0,    0,
  133,    0,   87,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   76,  106,    6,   29,   28,    0,    0,
    0,    0,   67,    0,    0,    0,    0,   38,    0,    0,
    0,    0,    0,    3,    0,    2,    5,    0,  117,    0,
    0,    0,    0,  122,    0,  124,    0,    0,    0,    0,
   86,  130,  126,  127,   99,   98,   94,   96,   95,    0,
  102,    0,    0,    0,  110,    0,   83,   82,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   34,   25,   33,   32,    0,    0,  107,   47,    0,   72,
    0,  104,    0,  100,    0,    0,  105,   79,   81,   78,
    0,   77,    0,    0,    0,    0,   43,    0,   41,    0,
   26,    0,    0,    0,    0,  109,    0,  112,  111,  108,
   69,    0,   44,    0,   45,    0,    0,   74,   73,    0,
   71,    0,   46,   42,   39,   24,   27,    0,    0,    0,
   75,   70,  103,
};
final static short yydgoto[] = {                          3,
    4,    5,  146,   18,   19,   20,   21,   22,   23,   24,
   25,   26,   27,   28,   29,  152,  147,  148,   56,   57,
   30,   31,   32,   33,   34,  142,   35,   58,  135,  189,
   59,  214,   60,   36,   61,   62,
};
final static short yysindex[] = {                      -108,
    0,  446,    0,    0,  -89,  -37,  -36,  148,  -39, -143,
 -240,  154, -215,    0,    0,    0,  330,    0,    0,    0,
    0,  -52,  -20,  -45,  -13,    6, -112, -189,  -16,   11,
   21,   33,   44,   45, -173,    0,  -37,  365,  398,  249,
    4, -128, -116,  -91,   -7,  131,    0,    0,    0,    0,
  -99,  249,    0,    0,    0,   82,    0,  -79,   66,    0,
  113,    0,  144,  -22,  129,  -68,  -56,    0,  249,  356,
    0,    0,  -50,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    4,  150,    0,  138,  117,   65,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  160,  -43,
  414,   -3,  181,  -91,    0,  158,    0,  -27,  -27,   66,
    0,  234,    0,  162,  167,  214,  356,   41,   -5,   -5,
   31,   -7,  -25,   63,  155,  103,  -74,  190,   70,  291,
  217,  -36,  462,    0,    0,    0,    0,    0,   -7,  -14,
   15,   80,    0,   22,   66,  133,   87,    0,  107,  106,
  157,  151,   80,    0,  -37,    0,    0,  356,    0,  113,
  113,  471,  111,    0,  113,    0,  113,   -7,   15,  109,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  324,
    0,  -98,  326,  340,    0,  356,    0,    0,  232,  -34,
  381,   66,   66,   -7,  220,   18,  536,  -37,  446,  176,
    0,    0,    0,    0,  175,  204,    0,    0,  478,    0,
  191,    0,  -40,    0,  198,  173,    0,    0,    0,    0,
  -37,    0,   15,   62,  556,   40,    0,  182,    0,  446,
    0,  117,  421,  177,  -97,    0,  423,    0,    0,    0,
    0,  413,    0,   49,    0,  216,  239,    0,    0,  493,
    0,  -40,    0,    0,    0,    0,    0,  193,  245,  386,
    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,  242,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  101,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  520,    0,    0,    0,
   32,    0,    0,  465,    0,  -32,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -10,    0,    0,    0,   69,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -38,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   77,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  242,    0,    0,    0,    0,    0,    0,   91,   88,
  -12,   97,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  105,    0,  529,    0,    0,    0,    0,   12,
   34,    0,    0,    0,   56,    0,   78,  275,  290,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  430,    0,    0,    0,  273,  276,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  301,    0,   10,    0,    0,    0,    0,    0,    0,  279,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,    0,  146,   51,  -88,  384,  524,    0,    0,    0,
    0,    0,  440,  457,    0,  338,  318,  407,  466,    0,
    0,    0,    0,    0,    0,  460,    3,  122,  369,  429,
  500,    0, -176,    0,  395,  399,
};
final static int YYTABLESIZE=763;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        130,
   64,   31,   40,  128,   51,   40,   76,   42,  130,  130,
  130,  130,  130,   79,  130,  177,   68,   51,  124,   66,
   48,  125,   51,   88,  213,  110,  130,  130,  130,  130,
  125,   66,  125,  125,  125,  108,  237,  109,   77,   51,
   71,   68,  187,  121,  187,   81,   66,   42,  125,  125,
  125,  125,  121,   68,  121,  121,  121,  114,  228,  115,
  114,  195,  115,   16,   82,  123,   51,   74,   68,   90,
  121,  121,  121,  121,  123,  260,  123,  123,  123,   92,
  244,   86,  114,  114,  115,  115,   43,  119,  143,   74,
   48,   94,  123,  123,  123,  123,  119,   99,  119,  119,
  119,  143,   96,   98,  114,  150,  115,  255,   43,  120,
   51,  193,   65,   66,  119,  119,  119,  119,  120,  130,
  120,  120,  120,  192,  114,   67,  115,  101,   41,  130,
  130,  130,  130,   70,  130,   97,  120,  120,  120,  120,
   22,   55,   53,   54,   83,  187,  130,   17,    1,   64,
   39,   74,    2,   84,  119,   63,  145,  212,  249,  120,
  106,  103,  250,   65,  107,  251,   37,    7,    8,  187,
  110,   38,  145,  112,    9,   10,   11,  111,   12,  117,
   13,  181,   51,  101,  121,   14,   15,   52,  126,   16,
  130,  205,   51,   69,  206,  179,   74,  114,   51,  115,
   14,   15,  127,   75,   51,  136,   51,   55,   53,   54,
   78,   51,  154,   55,   53,   54,   40,   48,   14,   15,
   41,  158,   31,  130,  130,  130,  130,  220,  130,   46,
  176,  130,   40,  122,   46,  130,   50,  130,   63,  130,
  130,  130,   80,   66,   87,  125,  125,  125,  125,   50,
  125,  172,  157,  125,   50,  123,   40,  125,   51,  125,
   41,  125,  125,  125,   51,   68,   89,  121,  121,  121,
  121,   50,  121,  227,  163,  121,   91,  194,   46,  121,
   74,  121,  159,  121,  121,  121,  175,   48,   93,  123,
  123,  123,  123,   51,  123,  243,  171,  123,   50,   95,
   97,  123,   48,  123,  254,  123,  123,  123,   55,   53,
   54,  119,  119,  119,  119,   85,  119,  241,  178,  119,
  149,   45,   46,  119,  101,  119,  185,  119,  119,  119,
   84,  186,   97,  120,  120,  120,  120,  113,  120,   14,
   15,  120,   50,  130,  230,  120,   64,  120,  200,  120,
  120,  120,   63,   47,   48,   49,   22,   22,   22,   80,
   65,  202,   22,  201,   22,   22,   22,   22,   22,  209,
   22,  210,    6,    7,    8,   22,   22,   14,   15,   22,
    9,   10,   11,  144,   12,  211,   13,  215,  198,    7,
    8,   14,   15,  139,  140,   16,    9,   10,   11,  144,
   12,  216,   13,   45,   46,  137,  138,   14,   15,   45,
   46,   16,  203,  204,   50,   45,  140,  164,   46,   47,
   48,   49,  166,   46,   50,   47,   48,   49,  239,  240,
   50,  231,    6,    7,    8,  232,   50,  245,   50,  248,
    9,   10,   11,   50,   12,  183,   13,  236,    6,    7,
    8,   14,   15,  134,  238,  261,    9,   10,   11,  149,
   12,  247,   13,   44,   14,   15,  252,   14,   15,  168,
   46,  253,    6,    7,    8,  225,   46,  256,   14,   15,
    9,   10,   11,   85,   12,  170,   13,    6,  132,    8,
   50,   14,   15,  218,  257,    9,   50,  104,  105,   12,
  134,   13,  160,  161,   45,   46,  129,  262,  165,  167,
  263,  208,   48,  114,  188,  115,  188,  173,  174,    7,
   47,   48,   49,  118,  118,   50,  207,  151,    4,  125,
   85,   85,   85,   85,   37,   85,   72,   35,   85,  104,
   36,  134,   85,  233,   85,   84,   84,   84,   84,  246,
   84,  141,  199,   84,  217,  116,   80,   84,  153,   84,
   80,  191,    0,   80,  141,  180,  182,  184,    0,  134,
    0,    0,  219,    0,  219,  162,  229,  235,  114,    0,
  115,  169,    0,    0,    0,    6,    7,    8,  104,    0,
    0,   73,  134,    9,   10,   11,  242,   12,  108,   13,
  109,    0,    0,    0,   14,   15,    0,    0,   16,  196,
  197,  131,  132,    8,    0,    0,  133,  188,  259,    9,
    6,    7,    8,   12,    0,   13,  100,    0,    9,   10,
   11,    0,   12,  134,   13,    0,  221,  132,    8,   14,
   15,  188,  222,   16,    9,  151,    0,    0,   12,    0,
   13,    0,    0,    6,    7,    8,    0,  223,  224,  102,
  226,    9,   10,   11,    0,   12,    0,   13,    0,  155,
    7,    8,   14,   15,    0,  156,   16,    9,   10,   11,
    0,   12,    0,   13,    0,   40,   40,   40,   14,   15,
    0,   40,   16,   40,   40,   40,    0,   40,    0,   40,
    0,    6,    7,    8,   40,   40,    0,    0,   40,    9,
   10,   11,    0,   12,    0,   13,    0,  190,    7,    8,
   14,   15,    0,    0,   16,    9,   10,   11,    0,   12,
    0,   13,    0,  234,  132,    8,   14,   15,  133,    0,
    0,    9,    0,    0,    0,   12,    0,   13,  258,  132,
    8,    0,    0,  133,    0,    0,    9,    0,    0,    0,
   12,    0,   13,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         32,
   40,   40,   40,   60,   45,   40,   59,   44,   41,   42,
   43,   44,   45,   59,   47,   41,  257,   45,   41,   32,
   59,   32,   45,   40,  123,   40,   59,   60,   61,   62,
   41,   44,   43,   44,   45,   43,  213,   45,   59,   45,
  256,   32,  131,   32,  133,   59,   59,   44,   59,   60,
   61,   62,   41,   44,   43,   44,   45,   43,   41,   45,
   43,   40,   45,  279,   59,   32,   45,   17,   59,   59,
   59,   60,   61,   62,   41,  252,   43,   44,   45,   59,
   41,  271,   43,   43,   45,   45,  123,   32,   86,   39,
   59,   59,   59,   60,   61,   62,   41,  271,   43,   44,
   45,   99,   59,   59,   43,   41,   45,   59,  123,   32,
   45,   32,  256,  257,   59,   60,   61,   62,   41,   32,
   43,   44,   45,   44,   43,  269,   45,   59,  257,   42,
   43,   44,   45,   12,   47,   59,   59,   60,   61,   62,
   40,   60,   61,   62,  257,  234,   59,    2,  257,   59,
    5,  101,  261,  266,   42,   59,   40,  256,  256,   47,
  277,   40,  260,   59,  256,  263,  256,  257,  258,  258,
   40,  261,   40,   52,  264,  265,  266,  277,  268,  259,
  270,  256,   45,   38,   41,  275,  276,   40,   60,  279,
   69,   41,   45,   40,   44,   41,  146,   43,   45,   45,
  275,  276,  271,  256,   45,  256,   45,   60,   61,   62,
  256,   45,  256,   60,   61,   62,   40,  256,  275,  276,
  257,   41,  261,  256,  257,  258,  259,  262,  261,  257,
  256,  264,   40,  256,  257,  268,  277,  270,  278,  272,
  273,  274,  256,  256,  261,  256,  257,  258,  259,  277,
  261,  257,  256,  264,  277,  278,   40,  268,   45,  270,
  257,  272,  273,  274,   45,  256,  256,  256,  257,  258,
  259,  277,  261,  256,   41,  264,  256,  256,  257,  268,
  230,  270,  125,  272,  273,  274,  256,  256,  256,  256,
  257,  258,  259,   45,  261,  256,  256,  264,  277,  256,
  256,  268,  271,  270,  256,  272,  273,  274,   60,   61,
   62,  256,  257,  258,  259,   41,  261,  256,  256,  264,
  256,  256,  257,  268,  256,  270,  257,  272,  273,  274,
   41,   41,  256,  256,  257,  258,  259,  256,  261,  275,
  276,  264,  277,  256,  199,  268,  256,  270,  262,  272,
  273,  274,  256,  272,  273,  274,  256,  257,  258,   59,
  256,  256,  262,  257,  264,  265,  266,  267,  268,  259,
  270,  263,  256,  257,  258,  275,  276,  275,  276,  279,
  264,  265,  266,  267,  268,   62,  270,   62,  256,  257,
  258,  275,  276,  256,  257,  279,  264,  265,  266,  267,
  268,   62,  270,  256,  257,  256,  257,  275,  276,  256,
  257,  279,  256,  257,  277,  256,  257,  256,  257,  272,
  273,  274,  256,  257,  277,  272,  273,  274,  256,  257,
  277,  256,  256,  257,  258,  261,  277,  256,  277,  263,
  264,  265,  266,  277,  268,  256,  270,  257,  256,  257,
  258,  275,  276,   70,  257,  263,  264,  265,  266,  256,
  268,   41,  270,    7,  275,  276,   44,  275,  276,  256,
  257,   59,  256,  257,  258,  256,  257,  262,  275,  276,
  264,  265,  266,   27,  268,  117,  270,  256,  257,  258,
  277,  275,  276,  262,  256,  264,  277,   41,   42,  268,
  117,  270,  108,  109,  256,  257,   67,  263,  114,  115,
  125,   41,  271,   43,  131,   45,  133,  119,  120,    0,
  272,  273,  274,   59,   59,  277,  158,   88,    0,   64,
  256,  257,  258,  259,  262,  261,   13,  262,  264,   83,
  262,  158,  268,  206,  270,  256,  257,  258,  259,  232,
  261,   86,  146,  264,  186,   56,  256,  268,   99,  270,
  260,  133,   -1,  263,   99,  126,  127,  128,   -1,  186,
   -1,   -1,  189,   -1,  191,  110,   41,  209,   43,   -1,
   45,  116,   -1,   -1,   -1,  256,  257,  258,  132,   -1,
   -1,  262,  209,  264,  265,  266,   41,  268,   43,  270,
   45,   -1,   -1,   -1,  275,  276,   -1,   -1,  279,  144,
  145,  256,  257,  258,   -1,   -1,  261,  234,  250,  264,
  256,  257,  258,  268,   -1,  270,  262,   -1,  264,  265,
  266,   -1,  268,  250,  270,   -1,  256,  257,  258,  275,
  276,  258,  262,  279,  264,  206,   -1,   -1,  268,   -1,
  270,   -1,   -1,  256,  257,  258,   -1,  192,  193,  262,
  195,  264,  265,  266,   -1,  268,   -1,  270,   -1,  256,
  257,  258,  275,  276,   -1,  262,  279,  264,  265,  266,
   -1,  268,   -1,  270,   -1,  256,  257,  258,  275,  276,
   -1,  262,  279,  264,  265,  266,   -1,  268,   -1,  270,
   -1,  256,  257,  258,  275,  276,   -1,   -1,  279,  264,
  265,  266,   -1,  268,   -1,  270,   -1,  256,  257,  258,
  275,  276,   -1,   -1,  279,  264,  265,  266,   -1,  268,
   -1,  270,   -1,  256,  257,  258,  275,  276,  261,   -1,
   -1,  264,   -1,   -1,   -1,  268,   -1,  270,  256,  257,
  258,   -1,   -1,  261,   -1,   -1,  264,   -1,   -1,   -1,
  268,   -1,  270,
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
"sentencias_declarativas : declaracion_especial ';'",
"sentencias_declarativas : declaracion_tipo_subrango ';'",
"sentencias_declarativas : declaracion_tipo_pair ';'",
"sentencias_declarativas : declaracion_funcion ';'",
"sentencias_declarativas : declaracion_variable error",
"sentencias_declarativas : declaracion_tipo_subrango error",
"sentencias_declarativas : declaracion_tipo_pair error",
"sentencias_declarativas : declaracion_funcion",
"declaracion_variable : tipo list_var",
"declaracion_funcion : encabezado_fun '(' parametro ')' BEGIN cuerpo_funcion END",
"declaracion_funcion : encabezado_fun '(' ')' error",
"declaracion_funcion : encabezado_fun BEGIN cuerpo_funcion END error",
"declaracion_funcion : encabezado_fun '(' parametro ',' parametro ')' error",
"encabezado_fun : tipo FUN ID",
"encabezado_fun : tipo FUN error",
"encabezado_fun : FUN ID",
"encabezado_fun : tipo ID",
"parametro : tipo ID",
"parametro : tipo error",
"parametro : error ID",
"cuerpo_funcion : bloque_sentencias_programa sentencia_retorno",
"cuerpo_funcion : bloque_sentencias_programa sentencia_retorno bloque_sentencias_programa",
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
"declaracion_especial : ID list_var",
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

//#line 977 "gramatica.y"

//Funciones
public static ArrayList<String> errores = new ArrayList<String>();
public static  List<String> warnings = new ArrayList<>();
public static ArrayList<String> erroreslex = new ArrayList<String>();
public static  List<String> warningslex = new ArrayList<>();

public static ArrayList<String> erroresSemanticos = new ArrayList<String>();
public static  List<String> warningsSemanticos = new ArrayList<>();

private static ArrayList<String> lista_identificadores = new ArrayList<String>();
private static ArrayList<ArbolSintactico> lista_expresiones = new ArrayList<ArbolSintactico>();
private static ArrayList<String> lista_pair = new ArrayList<String>();
private static ArrayList<String> lista_sub = new ArrayList<String>();
private static ArrayList<String> lista_etiquetas = new ArrayList<String>();

private static ArrayList<NodoControl> lista_func = new ArrayList<NodoControl>();

public static ArrayList<String> lista_id_func = new ArrayList<String>();

private static NodoControl raiz;
private static NodoComun ptrRaizPrograma;

private static NodoComun ptrRaizFuncion;
private static NodoComun aux_raiz_func;

private static String ambitoActual = "global";

private static int rango1 = 0;
private static int rango2 = 0;

public static boolean tieneErrores = false;
private static final NodoComun nodoError = new NodoComun("ERROR", null,null);

public static String auxTipoAsig = "";
public static String auxTipoComp = "";

public static boolean chequearTipos(Simbolo s1){
    if(s1.getId() != -1){
        if(!auxTipoComp.equals("")){
            //es  una asignacion
            if(s1.getTipo().equals(auxTipoComp) || s1.getTipoParametro().equals(auxTipoComp)){
                return true;
            }else{
                String err = "Linea: " + AnalizadorLexico.getLineaActual() + ". Error Semantico: Variable " + s1.getLexema() + " con tipo incorrecto";
                erroresSemanticos.add(err);
                return false; 
            }
        }else{
            //es una expr_aritmetic suelta, por ejemplo en una COMPARACION
            if (s1.getTipo().equals("INTEGER") || s1.getTipo().equals("DOUBLE")){
                      auxTipoComp = s1.getTipo();
            }else{
                     auxTipoComp = s1.getTipoParametro();
            }
            return true;
        }
    }else{
        return true;
    }
}

/*public static boolean chequearTipos(Simbolo s1){
    return true;
}*/

public static void generarArbol(ArbolSintactico arbl){
    if(raiz == null){
        //Si la raiz no existe la creo
        NodoComun sentencia = new NodoComun("SentenciaArbol",null,null);
        NodoControl program = new NodoControl("Programa", sentencia);
        raiz = program;
        ptrRaizPrograma = sentencia;
    }
    if(ptrRaizPrograma.getHijoIzq() == null){
        //Si el nodo izquierdo es null, agrego la nueva sentencia ahi
        ptrRaizPrograma.setHijoIzq(arbl);

    }else{
        //si el nodo izq ya esta ocupado, creo un nuevo NodoControl "Sentencia" y añado la sentencia nueva en el nodo izq
        NodoComun nuevo = new NodoComun("SentenciaArbol", arbl,null);
        ptrRaizPrograma.setHijoDer(nuevo);
        ptrRaizPrograma = nuevo; //seteo puntero;
    }
}

public static void generarArbolFunc(ArbolSintactico arbl){
    if(aux_raiz_func == null){
        //Si la raiz no existe la creo
        NodoComun sentencia = new NodoComun("SentenciaArbolFun",null,null);
        aux_raiz_func = sentencia;
        ptrRaizFuncion = sentencia;
    }
    if(ptrRaizFuncion.getHijoIzq() == null){
        //Si el nodo izquierdo es null, agrego la nueva sentencia ahi
        ptrRaizFuncion.setHijoIzq(arbl);
    }else{
        //si el nodo izq ya esta ocupado, creo un nuevo NodoControl "Sentencia" y añado la sentencia nueva en el nodo izq
        NodoComun nuevo = new NodoComun("SentenciaArbolFun", arbl,null);
        ptrRaizFuncion.setHijoDer(nuevo);
        ptrRaizFuncion = nuevo; //seteo puntero;
    }
}




public static void buscarErroresEnNodo(ArbolSintactico arb){
    if(!tieneErrores){
        if(arb == nodoError){
            tieneErrores = true;
        }if(arb.getDer() != null) {
            buscarErroresEnNodo(arb.getDer());
        }
        if(arb.getIzq() != null) {
            buscarErroresEnNodo(arb.getIzq());
        }
    }
}

public static void agregarMetodoLista(NodoControl raiz){
    lista_func.add(raiz);
}

public static void agregarArbol(String nombreFunc){
    NodoControl func = new NodoControl(nombreFunc,aux_raiz_func); 
    aux_raiz_func = null;
    ptrRaizFuncion = null;
    lista_func.add(func);
}

public static ArrayList<NodoControl> get_arboles(){
    ArrayList<NodoControl> aux = new ArrayList<NodoControl>();
    for(NodoControl n : lista_func){
        aux.add(n);
    }
    //lista_func.clear();
    return aux;
}


public static NodoControl getRaiz(){
	return raiz;
}


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
            if ( warningsSemanticos.size() > 0)
        {
            for (String Warning : warnings)
                System.out.println(Warning);
        }

        System.out.println("Errores Semanticos: ");
    if ( erroresSemanticos.size() > 0)
        {
            System.out.println("No se ha podido compilar el programa debido a los siguientes errores: ");
            for (String error : erroresSemanticos)
                System.err.println(error);
        } else {
            System.out.println("Se ha terminado de compilar correctamente.");
        }
      if ( warningsSemanticos.size() > 0)
        {
            for (String Warning : warningsSemanticos)
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
             TablaDeSimbolos.obtenerSimbolo(lexema).setLexema("-" + lexema);
    }}
    
    Simbolo s = TablaDeSimbolos.obtenerSimbolo(lexema);
        String tipo = s.getTipo();
        TablaDeSimbolos.agregarSimbolo('-'+lexema, ConstantesCompilador.CONSTANTE);
        TablaDeSimbolos.obtenerSimbolo('-'+lexema).setUso("constante");
        TablaDeSimbolos.obtenerSimbolo('-'+lexema).setTipo(tipo);


   /*    if(!lexema.contains(".")){

        TablaDeSimbolos.agregarSimbolo('-'+lexema, ConstantesCompilador.CONSTANTE);
        TablaDeSimbolos.obtenerSimbolo('-'+lexema).setUso("constante");
        TablaDeSimbolos.obtenerSimbolo('-'+lexema).setTipo("INTEGER");

        //Constantes.tokens.put(lexemanegativo, Constantes.CTE);
    }else{
        TablaDeSimbolos.agregarSimbolo('-'+lexema, ConstantesCompilador.CONSTANTE);
        TablaDeSimbolos.obtenerSimbolo('-'+lexema).setUso("constante");
        TablaDeSimbolos.obtenerSimbolo('-'+lexema).setTipo("DOUBLE");
     //   Constantes.tokens.put(lexemanegativo, Constantes.CTE);

    } */
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
          }else

          if (Integer.parseInt(lexema) > ConstantesCompilador.MAX_INT_POSITIVO-1) {
            Parser.agregarError(ConstantesCompilador.WARNING, ConstantesCompilador.LEXICO, "El numero es demasiado grande. Se lo ha reemplazado por el mas cercano posible: " + ConstantesCompilador.MAX_INT_POSITIVO);
            lexema = String.valueOf(ConstantesCompilador.MAX_INT_POSITIVO-1);
          }
 
}
 }





void yyerror(String error) {
    // funcion utilizada para imprimir errores que produce yacc
    System.out.println("Yacc reporto un error: " + error);
}
//#line 899 "Parser.java"
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
//#line 36 "gramatica.y"
{/*System.out.println("Linea "+ AnalizadorLexico.getLineaActual() + ", Se reconocio el programa " +  $1.sval);*/
                               if (!lista_etiquetas.isEmpty()){
                                  String err = "Linea " + AnalizadorLexico.getLineaActual() + ". Error Semantico: No existe la etiqueta";
                                  erroresSemanticos.add(err);
                               }
                               if (val_peek(1).obj instanceof ArbolSintactico) {
                                    ArbolSintactico arbAux = (ArbolSintactico) val_peek(1).obj;
                                    buscarErroresEnNodo(arbAux);
                                    generarArbol(arbAux);
                                }}
break;
case 3:
//#line 46 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Se esperaban sentencias de ejecucion");}
break;
case 4:
//#line 47 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de delimitador de programa: END");}
break;
case 5:
//#line 48 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de delimitador de programa: BEGIN");}
break;
case 6:
//#line 49 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR,ConstantesCompilador.SINTACTICO, "Se esperaba un nombre de programa");}
break;
case 7:
//#line 50 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Error sintactico al compilar no permite terminar de leer el programa de forma correcta");}
break;
case 9:
//#line 56 "gramatica.y"
{ 
                      if (val_peek(1).obj instanceof ArbolSintactico && val_peek(0).obj instanceof ArbolSintactico ){
                           ArbolSintactico Cuerpo_sen1 = (ArbolSintactico) val_peek(0).obj; 
                           ArbolSintactico Cuerpo_bloqueI = (ArbolSintactico) val_peek(1).obj;
                           yyval=new ParserVal(new NodoComun("SentenciaProg",Cuerpo_bloqueI , Cuerpo_sen1));
                     }else
                        if (val_peek(1).obj instanceof ArbolSintactico){
                           ArbolSintactico Cuerpo_bloqueI = (ArbolSintactico) val_peek(1).obj;
                           yyval=val_peek(1);
                       }else
                          if (val_peek(0).obj instanceof ArbolSintactico){
                           ArbolSintactico Cuerpo_bloqueI = (ArbolSintactico) val_peek(0).obj;
                           yyval=val_peek(0);}
           }
break;
case 10:
//#line 70 "gramatica.y"
{yyval = val_peek(0);}
break;
case 12:
//#line 75 "gramatica.y"
{ if (val_peek(0).obj instanceof ArbolSintactico) {
                                    ArbolSintactico arbAux = (ArbolSintactico) val_peek(0).obj;
                                    
                                    yyval=new ParserVal(arbAux);
                                }}
break;
case 13:
//#line 80 "gramatica.y"
{ if (lista_etiquetas.contains(val_peek(0).sval)){
                       lista_etiquetas.remove(val_peek(0).sval);
                       ArbolSintactico arbAux = (ArbolSintactico) val_peek(0);
                       NodoControl nodo= new NodoControl("ETIQUETA_SALTO",arbAux);
                       yyval=new ParserVal(nodo);
               }else{
                  String err = "Linea " + AnalizadorLexico.getLineaActual() + ". Error Semantico: No existe el salto";
                  erroresSemanticos.add(err);
                  yyval = new ParserVal( nodoError);
               }         
   }
break;
case 19:
//#line 98 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR,ConstantesCompilador.SINTACTICO, "Se esperaba un ; luego de la declaracion de variable");}
break;
case 20:
//#line 99 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR,ConstantesCompilador.SINTACTICO, "Se esperaba un ; luego de la declaracion de sub-tipo");}
break;
case 21:
//#line 100 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR,ConstantesCompilador.SINTACTICO, "Se esperaba un ; luego de la declaracion del tipo pair");}
break;
case 22:
//#line 101 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR,ConstantesCompilador.SINTACTICO, "Se esperaba un ; luego de la declaracion de la funcion");}
break;
case 23:
//#line 104 "gramatica.y"
{/*System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio una declaracion de variable");*/
                                                    for(String s : lista_identificadores){
                                                       if(!TablaDeSimbolos.existeSimboloAmbitoActual(s + ":"+ambitoActual)){
                                                            Simbolo sim = TablaDeSimbolos.obtenerSimbolo(s);
                                                            sim.setTipo(val_peek(1).sval);
                                                            sim.setLexema(s+":"+ambitoActual);
                                                            sim.setAmbito(ambitoActual);                                                                                                                 
                                                            sim.setUso("identificador");
                                                            sim.setValorAsignado(true);      
                                                        }else{
                                                            String err = "Linea " + AnalizadorLexico.getLineaActual() + ". Error Semantico: Variable re declarada en el mismo ambito";
                                                            erroresSemanticos.add(err);
                                                        }
                                                    }
                                                    lista_identificadores.clear();
                                                }
break;
case 24:
//#line 123 "gramatica.y"
{/*System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio la declaracion de funcion con su valor de retorno");                                              */
                       String ambitoInicial = ambitoActual;
                       if (val_peek(1).obj instanceof ArbolSintactico) {
                                    ArbolSintactico arbAux = (ArbolSintactico) val_peek(1).obj;
                                    buscarErroresEnNodo(arbAux);
                                    NodoComun arb = new NodoComun("Cuerpo_Fun",(ArbolSintactico)val_peek(6),(ArbolSintactico)  val_peek(1).obj);
                                    NodoControl arbol= new NodoControl("Funcion",arb);
                                    agregarMetodoLista(arbol);
                                    generarArbolFunc((ArbolSintactico) arb);
                                    
                                  /*  arbAux.recorrerArbol("-");*/
                                } 
                          int indice = ambitoActual.lastIndexOf(':');
if (indice != -1) {
    ambitoActual = ambitoActual.substring(0, indice);
} else {
    /* Manejo seguro si no hay ':' en ambitoActual*/
    ambitoActual = ambitoInicial; /* O un valor predeterminado para el ámbito global*/
}
              
                                      
}
break;
case 25:
//#line 146 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta parametro");}
break;
case 26:
//#line 147 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta parametro");}
break;
case 27:
//#line 148 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "error en la cantidad de parametros");}
break;
case 28:
//#line 151 "gramatica.y"
{
                      if(!TablaDeSimbolos.existeSimboloAmbitoActual(val_peek(0).sval + ":"+ambitoActual)){
                        Simbolo simboloFuncion = TablaDeSimbolos.obtenerSimbolo(val_peek(0).sval);
                        simboloFuncion.setTipo(val_peek(2).sval);
                        simboloFuncion.setUso("funcion");
                        simboloFuncion.setLexema(val_peek(0).sval+":"+ambitoActual);                        
                        simboloFuncion.setAmbito(ambitoActual);
                        ambitoActual = ambitoActual + ":" + val_peek(0).sval ;                       
                        lista_id_func.add(val_peek(0).sval);
                        /*agregarArbol($3.sval);*/
                        yyval = new NodoHoja(val_peek(0).sval);
                      }else{
                          String err = "Linea " + AnalizadorLexico.getLineaActual() + ". Error Semantico: Identificador funcion re declarado en el mismo ambito";
                          erroresSemanticos.add(err);
                          yyval = nodoError;
                           }  
                        }
break;
case 29:
//#line 168 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de nombre en la función"); yyval = nodoError;}
break;
case 30:
//#line 169 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta tipo"); yyval = nodoError;}
break;
case 31:
//#line 170 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta palabra reservada FUN"); yyval = nodoError;}
break;
case 32:
//#line 173 "gramatica.y"
{/*System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio parametro");*/
                        Simbolo simboloFuncion = TablaDeSimbolos.obtenerSimbolo(val_peek(0).sval);
                        simboloFuncion.setTipo(val_peek(1).sval);
                        simboloFuncion.setLexema(val_peek(0).sval+":"+ambitoActual);
                        simboloFuncion.setUso("Parametro");
                        simboloFuncion.setAmbito(ambitoActual);
}
break;
case 33:
//#line 180 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Se espera un identificador luego del tipo");}
break;
case 34:
//#line 181 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de tipo de parámetro formal en declaración de función"); }
break;
case 35:
//#line 185 "gramatica.y"
{ ArbolSintactico Cuerpo_ret = (ArbolSintactico) val_peek(0).obj; 
                           ArbolSintactico Cuerpo_fun = (ArbolSintactico) val_peek(1).obj;
                          
                           NodoComun cuerpo_completo = new NodoComun("Sentencias_fun",Cuerpo_fun , Cuerpo_ret);
                           yyval=new ParserVal(cuerpo_completo);
           }
break;
case 36:
//#line 191 "gramatica.y"
{
                          ArbolSintactico Cuerpo_ret = (ArbolSintactico) val_peek(1).obj; 
                           ArbolSintactico Cuerpo_fun1 = (ArbolSintactico) val_peek(2).obj;
                          ArbolSintactico Cuerpo_fun2 = (ArbolSintactico) val_peek(0).obj;
                          NodoComun cuerpo = new NodoComun("Sentencias_fun",Cuerpo_fun1 , Cuerpo_ret);
                          NodoComun cuerpo_completo = new NodoComun("Sentencias_fun",cuerpo , Cuerpo_fun2);
                          yyval=new ParserVal(cuerpo_completo);
    }
break;
case 37:
//#line 199 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de sentencia RET en la función"); yyval =new ParserVal(nodoError);}
break;
case 38:
//#line 200 "gramatica.y"
{yyval = val_peek(0);}
break;
case 39:
//#line 203 "gramatica.y"
{
                           /* System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio una sentencia de retorno");*/
                                ArbolSintactico arb = (ArbolSintactico) val_peek(2); 
                               yyval = new ParserVal(new NodoComun(val_peek(4).sval, arb,null));  }
break;
case 40:
//#line 208 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Debe especificar el valor a retornar");yyval =new ParserVal(nodoError);}
break;
case 41:
//#line 209 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta RET");yyval =new ParserVal(nodoError);}
break;
case 42:
//#line 210 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta el ';' despues del Retorno");yyval =new ParserVal(nodoError);}
break;
case 43:
//#line 211 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta parentesis");yyval =new ParserVal(nodoError);}
break;
case 44:
//#line 212 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta parentesis para cerrar");yyval =new ParserVal(nodoError);}
break;
case 45:
//#line 213 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta parentesis para abrir");yyval =new ParserVal(nodoError);}
break;
case 46:
//#line 214 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Retorno vacio");}
break;
case 47:
//#line 217 "gramatica.y"
{/* System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio el llamado a la funcion " + $1.sval );*/
         /* System.out.println("invocacion func" + $1.sval + (TablaDeSimbolos.existeSimboloAmbitoActual($1.sval +":"+ ambitoActual)));*/
          if (lista_id_func.contains(val_peek(3).sval) && (TablaDeSimbolos.existeSimboloAmbitoActual(val_peek(3).sval +":"+ ambitoActual)) ){    
           Simbolo s = TablaDeSimbolos.obtenerParametro(ambitoActual+":"+val_peek(3).sval);        
           ArbolSintactico exprArbol = (ArbolSintactico)val_peek(1);
           Simbolo simbol = TablaDeSimbolos.obtenerSimbolo( exprArbol.getLex()+":"+ ambitoActual );
           String tipoExp;
           if (simbol != null && simbol.getId() != -1){
              tipoExp = TablaDeSimbolos.obtenerSimbolo( exprArbol.getLex()+":"+ ambitoActual ).getTipo();  
              /*System.out.println("tipoExp" + exprArbol.getLex()+":"+ ambitoActual + "TIPO" + tipoExp ); */
           }else{
                          
                          tipoExp = TablaDeSimbolos.obtenerSimbolo( exprArbol.getLex()).getTipo();  
                         /* System.out.println("tipoExp" + exprArbol.getLex()+":"+ ambitoActual + "TIPO" + tipoExp ); */
               
           }          
           if(s.getTipo().equals(tipoExp)){                 
             NodoHoja id_func = new NodoHoja(val_peek(3).sval);
             ArbolSintactico arb2 = (ArbolSintactico) val_peek(1);             
             yyval = new NodoComun("ejecucion funcion", id_func, arb2);  
             }else{
                String err = "Linea " + AnalizadorLexico.getLineaActual() + ". Error Semantico:Tipo Parametro formal distinto Parametro Real " ;
              erroresSemanticos.add(err);
              yyval = (NodoComun) nodoError;
              }
         }else{
          if(lista_id_func.contains(val_peek(3).sval)){
              String err = "Linea " + AnalizadorLexico.getLineaActual() + ". Error Semantico: funcion fuera de ambito" ;
              erroresSemanticos.add(err);
              yyval =  nodoError;
          }else{
              String err = "Linea " + AnalizadorLexico.getLineaActual() + ". Error Semantico: funcion no declarada" ;
              erroresSemanticos.add(err);
              yyval =  nodoError;
           }
          yyval =  nodoError;
        } }
break;
case 48:
//#line 255 "gramatica.y"
{lista_identificadores.add(val_peek(0).sval); 
                yyval = (ArbolSintactico) new NodoHoja(val_peek(0).sval);
                }
break;
case 49:
//#line 258 "gramatica.y"
{lista_identificadores.add(val_peek(2).sval); 
                yyval = (ArbolSintactico) new NodoHoja(val_peek(2).sval);
                }
break;
case 50:
//#line 261 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de ',' en la declaracion de variables");}
break;
case 51:
//#line 264 "gramatica.y"
{yyval = val_peek(0);}
break;
case 52:
//#line 265 "gramatica.y"
{yyval = val_peek(0);}
break;
case 53:
//#line 269 "gramatica.y"
{yyval = val_peek(1); }
break;
case 54:
//#line 270 "gramatica.y"
{yyval = val_peek(1); }
break;
case 55:
//#line 271 "gramatica.y"
{yyval = val_peek(1); }
break;
case 58:
//#line 274 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Error en la sentencia asignacion falta ';' "); 
		       yyval = (ArbolSintactico) nodoError;}
break;
case 59:
//#line 276 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Error en la sentencia seleccion falta ';' ");
		      yyval = (ArbolSintactico) nodoError;
                                                }
break;
case 60:
//#line 279 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Error en la sentencia out falta ';' ");
		      yyval = (ArbolSintactico) nodoError;
                                                }
break;
case 61:
//#line 282 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Error en la sentencia while falta ';' ");
              yyval = (ArbolSintactico) nodoError;
                                                }
break;
case 62:
//#line 285 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Error en la sentencia goto falta ';' ");
              yyval = (ArbolSintactico) nodoError;
                                                }
break;
case 63:
//#line 291 "gramatica.y"
{ /*System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ", Se reconoció la asignación múltiple."); */
       List<ArbolSintactico> arbolesAsignacion = new ArrayList<>(); 
       int numVars = lista_identificadores.size(); 
       int numExprs = lista_expresiones.size(); 
       /* Supone que list_expresion devuelve una lista de ArbolSintactico  */
       /* Asegurar que hay al menos tantas expresiones como variables */
       int minSize = Math.min(numVars, numExprs); 
       Collections.reverse(lista_identificadores);    
       /* Ajusta el orden si es necesario */
       for (int i = 0; i < minSize; i++) {           
         String varLexema = lista_identificadores.get(i) + ":" + ambitoActual;
         /*System.out.println("dato" + varLexema);*/
         Simbolo simbol;
        if(!TablaDeSimbolos.existeSimboloAmbitoActual(val_peek(2).sval +":"+ambitoActual)) {
           simbol = TablaDeSimbolos.obtenerSimbolodentrodelAmbito(varLexema);
        }else{
          simbol = TablaDeSimbolos.obtenerSimbolo(varLexema); 
        }
         
         if (simbol != null && simbol.getId() != -1) { 
          /* Obtener el tipo de la variable y el tipo de la expresión */
          String tipoVar = simbol.getTipo();
           String[] sub = tipoVar.split(":"); 
           String tip = sub[0];
          if( lista_sub.contains(tip)){
               tipoVar = simbol.getTipoParametro();
          }  
          ArbolSintactico exprArbol = lista_expresiones.get(i); 
          String tipoExpr ; 
          if(exprArbol.getLex().equals("IndicePair")){
                tipoExpr = TablaDeSimbolos.obtenerSimbolo(exprArbol.getIzq().getLex()).getTipoParametro();                
               
            }else{
                 Simbolo a = TablaDeSimbolos.obtenerSimbolo(exprArbol.getLex() +":" + ambitoActual);
                 if (a.getUso().equals("identificador") || a.getUso().equals("PAIR")){
                         tipoExpr = TablaDeSimbolos.obtenerSimbolo(exprArbol.getLex()+":" + ambitoActual ).getTipo(); 
                 }else{
                     if (exprArbol.getLex().equals("ejecucion funcion")){
                       tipoExpr = TablaDeSimbolos.obtenerSimbolo(exprArbol.getIzq().getLex()+":" + ambitoActual ).getTipo();
                     }else{
                       tipoExpr = TablaDeSimbolos.obtenerSimbolo(exprArbol.getLex() ).getTipo(); 
                     }
                 }
            }
           if (exprArbol.getLex().equals("+") || 
                             exprArbol.getLex().equals("-") || 
                            exprArbol.getLex().equals("*") || 
                            exprArbol.getLex().equals("/")){
                           tipoExpr = TablaDeSimbolos.obtenerSimbolo(exprArbol.getIzq().getLex()).getTipo();
                            
                            }
        /*  System.out.println("tipo izq:" + tipoVar + " " + "tipo der:" + tipoExpr); */
          if (tipoVar.equals(tipoExpr)) { 
         /*  System.out.println("tipo var" + tipoVar);*/
            NodoHoja hoja = new NodoHoja(simbol.getLexema()); 
            arbolesAsignacion.add(new NodoComun(":=", hoja, exprArbol)); 
          } else { 
            if (tipoVar.equals("DOUBLE") && !lista_pair.contains(tipoExpr)) { 
              Simbolo exp = TablaDeSimbolos.obtenerSimbolo(exprArbol.getLex()); 
              /*exp.setTipo("DOUBLE"); */
              NodoHoja hoja = new NodoHoja(simbol.getLexema()); 
              arbolesAsignacion.add(new NodoComun(":=", hoja, new NodoControl("CONVERSION", exprArbol))); 
              /*System.out.println("cambio de tipo"); */
              } else {
                 if(lista_pair.contains(tipoExpr) || lista_sub.contains(tipoExpr)){ 
        						 String Subtipo = TablaDeSimbolos.obtenerSimbolo(exprArbol.getLex()).getTipoParametro(); 
        						 if (tipoVar.equals(Subtipo)) { 
           						 NodoHoja hoja = new NodoHoja(simbol.getLexema()); 
           						 arbolesAsignacion.add(new NodoComun(":=", hoja, exprArbol)); }
           						 else{
           						 erroresSemanticos.add("Linea: " + AnalizadorLexico.getLineaActual() + ". Error Semántico: Incompatibilidad de tipo en la asignación de " + lista_identificadores.get(i) + " := " + exprArbol.getLex());
                  arbolesAsignacion.add(nodoError); 
                  yyval = new ParserVal(nodoError);
           						 }
        		}else{				
                    erroresSemanticos.add("Linea: " + AnalizadorLexico.getLineaActual() + ". Error Semántico: Incompatibilidad de tipo en la asignación de " + lista_identificadores.get(i) + " := " + exprArbol.getLex());
                    arbolesAsignacion.add(nodoError); 
                    yyval = new ParserVal(nodoError);
                } 
                } 
            } } else {
                   erroresSemanticos.add("Linea: " + AnalizadorLexico.getLineaActual() + ". Error Semántico: La variable " + varLexema + " no existe.");
                   yyval = new ParserVal(nodoError);
                    } } 

        if (numVars > numExprs) {
            for (int i = numExprs; i < numVars; i++) {
                String varLexema = lista_identificadores.get(i) + ":" + ambitoActual;
                Simbolo simbol = TablaDeSimbolos.obtenerSimbolo(varLexema);
                if (simbol != null && simbol.getId() != -1) {
                  if (simbol.getTipo().equals("INTEGER")||simbol.getTipoParametro().equals("INTEGER")){
                    NodoHoja hoja = new NodoHoja(simbol.getLexema());
                    NodoHoja cero = new NodoHoja("0");
                    TablaDeSimbolos.agregarSimbolo(String.valueOf(0), ConstantesCompilador.CONSTANTE);
                    TablaDeSimbolos.obtenerSimbolo(String.valueOf(0)).setTipo("INTEGER");
                    TablaDeSimbolos.obtenerSimbolo(String.valueOf(0)).setUso("constante");
                    arbolesAsignacion.add(new NodoComun(":=", hoja, cero));
                  }else{
                    NodoHoja hoja = new NodoHoja(simbol.getLexema());
                    NodoHoja cero = new NodoHoja("0.0");
                    TablaDeSimbolos.agregarSimbolo(String.valueOf(0.0), ConstantesCompilador.CONSTANTE);
                    TablaDeSimbolos.obtenerSimbolo(String.valueOf(0.0)).setTipo("DOUBLE");
                    TablaDeSimbolos.obtenerSimbolo(String.valueOf(0.0)).setUso("constante");
                    arbolesAsignacion.add(new NodoComun(":=", hoja, cero));  
                  }
                   /* System.out.println("Warning: Variable " + varLexema + " asignada a 0.");*/
                     warningsSemanticos.add("Linea: " + AnalizadorLexico.getLineaActual() + ". Warning Semántico: Variable " + varLexema + " asignada a 0");
                   
                }
            }
        } else if (numExprs > numVars) {
           /* System.out.println("Warning: Se descartaron expresiones adicionales.");*/
            warningsSemanticos.add("Linea: " + AnalizadorLexico.getLineaActual() + ". Warning Semántico: Se descartaron expresiones adicionales.");
        }

        /* Construir el nodo para la asignación múltiple*/
        yyval = new ParserVal(new NodoMultipleAsignacion("AsignacionM",arbolesAsignacion));
        lista_identificadores.clear();
        lista_expresiones.clear(); 
    }
break;
case 64:
//#line 412 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Error en la expresion de la asignacion");}
break;
case 65:
//#line 413 "gramatica.y"
{/*System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio la asignacion ");*/
        List<ArbolSintactico> arbolesAsignacion = new ArrayList<>();
        
        int numVars = lista_identificadores.size();
        int numExprs = lista_expresiones.size(); 
        int minSize = Math.min(numVars, numExprs);
        Collections.reverse(lista_identificadores);
        for (int i = 0; i < minSize; i++) {
            String varLexema = lista_identificadores.get(i) + ":" + ambitoActual;
            Simbolo simbol = TablaDeSimbolos.obtenerSimbolo(varLexema);
            
            if (simbol != null && simbol.getId() != -1) {
                /* Obtener el tipo de la variable y el tipo de la expresión*/
                String tipoVar = simbol.getTipo();
                String tipoVarSub = simbol.getTipoParametro();
                ArbolSintactico exprArbol = lista_expresiones.get(i);
                String tipoExpr = TablaDeSimbolos.obtenerSimbolo(exprArbol.getLex()).getTipo();
                if (tipoVar.equals(tipoExpr) || tipoVarSub.equals(tipoExpr)) {
                    /* Si los tipos coinciden, se agrega el nodo de asignación al árbol*/
                    NodoHoja hoja = new NodoHoja(simbol.getLexema());
                    arbolesAsignacion.add(new NodoComun(":=",(ArbolSintactico) val_peek(2) , exprArbol));
                } else {
                    /* Si los tipos no coinciden, registra un error semántico*/
                    if(tipoVarSub.equals("DOUBLE")){
                        Simbolo exp = TablaDeSimbolos.obtenerSimbolo(exprArbol.getLex());
                       /* exp.setTipo("DOUBLE");*/
                        NodoHoja hoja = new NodoHoja(simbol.getLexema());
                        arbolesAsignacion.add(new NodoComun(":=", hoja, new NodoControl("CONVERSION", exprArbol)));
                        /*System.out.println("cambio de tipo");*/
                    }else{
                    erroresSemanticos.add("Linea: " + AnalizadorLexico.getLineaActual() + 
                                          ". Error Semántico: Incompatibilidad de tipo en la asignación de " + 
                                          lista_identificadores.get(i) + " := " + exprArbol.getLex());
                    arbolesAsignacion.add(nodoError);  /* Agrega un nodo de error en el árbol*/
                    yyval = new ParserVal(nodoError);
                }}
            } else {
                erroresSemanticos.add("Linea: " + AnalizadorLexico.getLineaActual() + 
                                      ". Error Semántico: La variable " + varLexema + " no existe.");
                                      yyval = new ParserVal(nodoError);
            }
        }

        /* Agregar nodos de valor cero o descartar sobrantes con warning*/
        if (numVars > numExprs) {
            for (int i = numExprs; i < numVars; i++) {
                String varLexema = lista_identificadores.get(i) + ":" + ambitoActual;
                Simbolo simbol = TablaDeSimbolos.obtenerSimbolo(varLexema);
                if (simbol != null && simbol.getId() != -1) {
                    NodoHoja hoja = new NodoHoja(simbol.getLexema());
                    NodoHoja cero = new NodoHoja("0");
                    arbolesAsignacion.add(new NodoComun(":=", hoja, cero));
                   /* System.out.println("Warning: Variable " + varLexema + " asignada a 0.");*/
                    warningsSemanticos.add("Linea: " + AnalizadorLexico.getLineaActual() + ". Warning Semántico: Variable " + varLexema + " asignada a 0.");
                }
            }
        } else if (numExprs > numVars) {
           /* System.out.println("Warning: Se descartaron expresiones adicionales.");*/
            warningsSemanticos.add("Linea: " + AnalizadorLexico.getLineaActual() + ". Warning Semántico: Se descartaron expresiones adicionales.");
        }

        /* Construir el nodo para la asignación múltiple*/
        yyval = new ParserVal(new NodoMultipleAsignacion("AsignacionM",arbolesAsignacion));
        lista_identificadores.clear();
        lista_expresiones.clear(); 
    }
break;
case 66:
//#line 481 "gramatica.y"
{
                lista_expresiones.add((ArbolSintactico) val_peek(0)); 
                yyval = val_peek(0);                
                }
break;
case 67:
//#line 485 "gramatica.y"
{
    lista_identificadores.remove(lista_identificadores.size() - 1);
    lista_expresiones.add((ArbolSintactico) val_peek(0));
             yyval = val_peek(0);}
break;
case 68:
//#line 489 "gramatica.y"
{/*System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio lista multiple");*/
	            lista_expresiones.add((ArbolSintactico)val_peek(0)); 
                yyval = val_peek(0);
                }
break;
case 69:
//#line 493 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Error, Falta ','");
          yyval = (ArbolSintactico) nodoError;}
break;
case 70:
//#line 499 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio una sentencia de control IF");
                                    if((ArbolSintactico)val_peek(6) != nodoError){
                                        ArbolSintactico nodoThen = (ArbolSintactico) val_peek(3).obj;
                                        NodoControl then = new NodoControl("then",nodoThen); /*Bloque del THEN*/
                                        ArbolSintactico nodoElse = (ArbolSintactico) val_peek(1).obj;
                                        NodoControl _else = new NodoControl("else", nodoElse); /*Bloque del ELSE*/
                                        NodoComun cuerpo = new NodoComun("cuerpo", then, _else);
                                        ArbolSintactico nodocond = (ArbolSintactico) val_peek(6);
                                        NodoControl condicion = new NodoControl("Condicion",nodocond);
                                      /*  $$ = (ArbolSintactico) new NodoComun($1.sval, condicion , cuerpo); */
                                        yyval = new ParserVal(new NodoComun(val_peek(8).sval, condicion , cuerpo));
                                    }else{
                                        yyval = val_peek(6);
                                    }                                                                                                          
                                }
break;
case 71:
//#line 515 "gramatica.y"
{/*System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio una sentencia de control IF");*/

                                    if((ArbolSintactico)val_peek(4) != nodoError){          
                                        ArbolSintactico nodoThen = (ArbolSintactico) val_peek(1).obj; 
                                        ArbolSintactico nodoCon = (ArbolSintactico) val_peek(4);                                                         
                                        NodoControl then = new NodoControl("then",nodoThen);
                                        NodoControl condicion = new NodoControl("Condicion", nodoCon);
                                        yyval = new ParserVal(new NodoComun(val_peek(6).sval, condicion , then));                                                      
                                    }else{
                                        yyval = val_peek(4);
                                    }
                                }
break;
case 72:
//#line 528 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de paréntesis en condición de selección");   yyval = (ArbolSintactico) nodoError; }
break;
case 73:
//#line 529 "gramatica.y"
{  agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de END_IF");   yyval = (ArbolSintactico) nodoError; }
break;
case 74:
//#line 530 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de contenido en bloque THEN");   yyval = (ArbolSintactico) nodoError;}
break;
case 75:
//#line 531 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de contenido en bloque ELSE");  yyval = (ArbolSintactico) nodoError; }
break;
case 76:
//#line 534 "gramatica.y"
{ yyval=val_peek(0);}
break;
case 77:
//#line 535 "gramatica.y"
{ yyval = val_peek(1);}
break;
case 78:
//#line 536 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de contenido en bloque");yyval = new ParserVal(nodoError); }
break;
case 79:
//#line 537 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta el BEGIN"); yyval = new ParserVal(nodoError);}
break;
case 80:
//#line 538 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta el END"); yyval = new ParserVal(nodoError);}
break;
case 81:
//#line 541 "gramatica.y"
{ ArbolSintactico Cuerpo_bloqueD = (ArbolSintactico) val_peek(0).obj; 
                           ArbolSintactico Cuerpo_bloqueI = (ArbolSintactico) val_peek(1).obj;
                           yyval=new ParserVal(new NodoComun("Sentencia_Dentro_IF",Cuerpo_bloqueI , Cuerpo_bloqueD));
           }
break;
case 82:
//#line 545 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 83:
//#line 546 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "No se permiten sentencias declarativas");yyval = new ParserVal(nodoError); }
break;
case 84:
//#line 549 "gramatica.y"
{
    ArbolSintactico arbIzq = (ArbolSintactico) val_peek(2);
    ArbolSintactico arbDer = (ArbolSintactico) val_peek(0);
    Simbolo simbolo1;
    Simbolo simbolo2;
    if (!arbIzq.getLex().equals("+") && 
                            ! arbIzq.getLex().equals("-") && 
                            !arbIzq.getLex().equals("*") &&  
                           ! arbIzq.getLex().equals("/")){
    simbolo1 = TablaDeSimbolos.obtenerSimbolo(arbIzq.getLex());
    }else{
        simbolo1 = TablaDeSimbolos.obtenerSimbolo(arbIzq.getIzq().getLex());
    }
    if(!arbDer.getLex().equals("+") && 
                            ! arbDer.getLex().equals("-") && 
                            !arbDer.getLex().equals("*") &&  
                           ! arbDer.getLex().equals("/") ){
      simbolo2 = TablaDeSimbolos.obtenerSimbolo(arbDer.getLex());
    }else{
       simbolo2 = TablaDeSimbolos.obtenerSimbolo(arbDer.getIzq().getLex());
      }

        if (arbIzq != nodoError && arbDer != nodoError) {
            if (simbolo1.getId() != -1 && simbolo2.getId() != -1) {
                if (chequearTipos(simbolo1) && chequearTipos(simbolo2)) {
                    yyval = new NodoComun(val_peek(1).sval, arbIzq, arbDer); /* Se agrega el comparador al árbol*/
                } else {
                    agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Error Semantico: El tipo de la comparación es distinto.");
                    yyval = nodoError;
                }
            } else {
                agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Lado izquierdo o derecho de la comparación incorrecto.");
                yyval = nodoError;
            }
        } else {
            agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de expresión aritmética en comparación.");
            yyval = nodoError;
        }
    }
break;
case 85:
//#line 588 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de expresion aritmetica en comparación"); 
    yyval = nodoError;}
break;
case 86:
//#line 590 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de expresion aritmetica en comparación"); 
    yyval = nodoError;}
break;
case 87:
//#line 593 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de comparador en expresion"); 
    yyval = nodoError;}
break;
case 88:
//#line 598 "gramatica.y"
{/*System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio la condicion de comparacion por igual");*/
                            yyval.sval = "=";}
break;
case 89:
//#line 600 "gramatica.y"
{/*System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio la condicion de comparacion por distinto");*/
                          yyval.sval = "DIST";}
break;
case 90:
//#line 602 "gramatica.y"
{/*System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio la condicion de comparacion por mayor");*/
                         yyval.sval = ">";}
break;
case 91:
//#line 604 "gramatica.y"
{/*System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio la condicion de comparacion por menor"); */
          					yyval.sval = "<";}
break;
case 92:
//#line 606 "gramatica.y"
{/*System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio la condicion de comparacion por mayor igual");*/
          					 yyval.sval = "MAYOR_IGUAL";}
break;
case 93:
//#line 608 "gramatica.y"
{/*System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio la condicion de comparacion por menor igual");*/
          					 yyval.sval = "MENOR_IGUAL";}
break;
case 94:
//#line 613 "gramatica.y"
{/*System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio una sentencia OUTF");*/
                                        Simbolo CAD = TablaDeSimbolos.obtenerSimbolo(val_peek(1).sval);
                                        CAD.setUso("cadena");
                                        NodoHoja cadena = new NodoHoja(val_peek(1).sval);                                        
                                        NodoControl bloque = new NodoControl("OUTF",(ArbolSintactico) cadena); 
                                      
                                        yyval = new ParserVal(bloque);
}
break;
case 95:
//#line 621 "gramatica.y"
{/* {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio una sentencia OUTF");*/
       
                                        ArbolSintactico cadena = (ArbolSintactico)val_peek(1);                                        
                                        NodoControl bloque = new NodoControl("OUTF",cadena); 
                                      
                                        yyval = new ParserVal(bloque);
      }
break;
case 96:
//#line 628 "gramatica.y"
{  agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta parámetro en sentencia OUTF");yyval = new ParserVal(nodoError); }
break;
case 97:
//#line 629 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Parámetro incorrecto en sentencia OUTF");yyval = new ParserVal(nodoError);}
break;
case 98:
//#line 630 "gramatica.y"
{  agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta el parentesis de cierre"); yyval = new ParserVal(nodoError);}
break;
case 99:
//#line 631 "gramatica.y"
{  agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta el parentesis de apertura");yyval = new ParserVal(nodoError); }
break;
case 100:
//#line 636 "gramatica.y"
{/*System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio una declaracion tipo_subrango");*/
                      if(!TablaDeSimbolos.existeSimboloAmbitoActual(val_peek(3).sval + ":"+ambitoActual)){
                        Simbolo simboloFuncion = TablaDeSimbolos.obtenerSimbolo(val_peek(3).sval);
                        simboloFuncion.setTipo(val_peek(1).sval);
                        simboloFuncion.setUso("SUBTIPO");
                        simboloFuncion.setLexema(val_peek(3).sval+":"+ambitoActual);                        
                        simboloFuncion.setAmbito(ambitoActual);  
                        simboloFuncion.setRango1(rango1);
                        simboloFuncion.setRango2(rango2);                    
                        lista_sub.add(val_peek(3).sval);
                      }else{
                          String err = "Linea " + AnalizadorLexico.getLineaActual() + ". Error Semantico: Identificador re declarado en el mismo ambito";
                          erroresSemanticos.add(err);
                           }  
}
break;
case 101:
//#line 651 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta nombre del tipo definido"); }
break;
case 102:
//#line 652 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta tipo base"); }
break;
case 103:
//#line 655 "gramatica.y"
{
        NodoHoja a = (NodoHoja) val_peek(3) ;
        rango1 = Integer.parseInt(a.getLex()) ;
        a = (NodoHoja) val_peek(1) ;
        rango2 = Integer.parseInt(a.getLex());
        if (rango1 >= rango2){
         String err = "Linea " + AnalizadorLexico.getLineaActual() + ". Error Semantico: LimiteInf mayor que LimiteSup";
         erroresSemanticos.add(err);
        }
       }
break;
case 104:
//#line 665 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de {} en el rango");}
break;
case 105:
//#line 669 "gramatica.y"
{/*System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio una sentencia  WHILE");*/
                   if((ArbolSintactico)val_peek(2) != nodoError){
                                        ArbolSintactico nodobloque = (ArbolSintactico) val_peek(0).obj;
                                        NodoControl bloque = new NodoControl("sentencia",nodobloque); /*Bloque del THEN*/
                                        NodoComun cuerpo = new NodoComun("cuerpo", bloque,null);
                                        ArbolSintactico nodocond = (ArbolSintactico) val_peek(2);
                                        NodoControl condicion = new NodoControl("Condicion",nodocond);
                                      
                                        yyval = new ParserVal(new NodoComun(val_peek(4).sval, condicion , cuerpo));
                                    }else{
                                        yyval = val_peek(2);
                                    }         
                  }
break;
case 106:
//#line 682 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de parentesis en la iteración");yyval = new ParserVal(nodoError); }
break;
case 107:
//#line 683 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta WHILE");yyval = new ParserVal(nodoError); }
break;
case 108:
//#line 688 "gramatica.y"
{/*System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio PAIR");*/
                      if(!TablaDeSimbolos.existeSimboloAmbitoActual(val_peek(0).sval + ":"+ambitoActual)){
                        Simbolo simboloFuncion = TablaDeSimbolos.obtenerSimbolo(val_peek(0).sval);
                        simboloFuncion.setTipo(val_peek(2).sval);
                        simboloFuncion.setUso("PAIR");
                        simboloFuncion.setLexema(val_peek(0).sval+":"+ambitoActual);                        
                        simboloFuncion.setAmbito(ambitoActual);                                          
                        lista_pair.add(val_peek(0).sval);
                      }else{
                          String err = "Linea " + AnalizadorLexico.getLineaActual() + ". Error Semantico: Identificador re declarado en el mismo ambito";
                          erroresSemanticos.add(err);
                           }  
}
break;
case 109:
//#line 701 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta PAIR"); }
break;
case 110:
//#line 702 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta '<>' en la declaración de PAIR"); }
break;
case 111:
//#line 703 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta identificador al final de la declaración"); }
break;
case 112:
//#line 704 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta tipo en la declaración de PAIR"); }
break;
case 114:
//#line 711 "gramatica.y"
{/*System.out.println("Linea "+ AnalizadorLexico.getLineaActual() + ", Se reconocio una sentencia GOTO");                                                                    */
                                      lista_etiquetas.add(val_peek(0).sval);
                                      NodoHoja etiqueta = new NodoHoja(val_peek(0).sval);                                        
                                      NodoControl bloque = new NodoControl(val_peek(1).sval,(ArbolSintactico) val_peek(0)); 
                                      
                                        yyval = new ParserVal(bloque);}
break;
case 115:
//#line 717 "gramatica.y"
{ agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta etiqueta en la sentencia GOTO");yyval = new ParserVal(nodoError); }
break;
case 116:
//#line 720 "gramatica.y"
{/*System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio la etiqueta " + $1.sval);*/
                     NodoHoja etiqueta = new NodoHoja(val_peek(0).sval);
                    yyval=etiqueta;
                }
break;
case 117:
//#line 726 "gramatica.y"
{
    String indice1 = "1";
    String indice2 = "2";
    if (indice1.equals(val_peek(1).sval) || indice2.equals(val_peek(1).sval)){
         lista_identificadores.add(val_peek(3).sval);
        
         NodoHoja izq = new NodoHoja(val_peek(3).sval + ":"+ ambitoActual);
         NodoHoja Der = new NodoHoja(val_peek(1).sval);
         yyval = new NodoComun("IndicePair",izq,Der);
       /*  $$ = new NodoHoja($1.sval);*/
    }else{
         String err = "Linea " + AnalizadorLexico.getLineaActual() + ". Error Semantico: Indice Pair fuera de rango ";
         erroresSemanticos.add(err);
         yyval = nodoError;
     }
}
break;
case 118:
//#line 744 "gramatica.y"
{/*System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio una declaracion de variable");*/
                                                   if (lista_pair.contains(val_peek(1).sval) || lista_sub.contains(val_peek(1).sval)){                                                   
                                                    for(String s : lista_identificadores){
                                                       if(!TablaDeSimbolos.existeSimboloAmbitoActual(s + ":"+ambitoActual)){
                                                            Simbolo sim = TablaDeSimbolos.obtenerSimbolo(s);
                                                            sim.setTipo(val_peek(1).sval+ ":"+ ambitoActual);
                                                            sim.setLexema(s+":"+ambitoActual);
                                                            sim.setAmbito(ambitoActual);                                                                                                                 
                                                            sim.setUso("identificador");
                                                            sim.setValorAsignado(true); 
                                                            Simbolo simEspecial = TablaDeSimbolos.obtenerSimbolo(val_peek(1).sval + ":"+ ambitoActual);  
                                                            sim.setTipoParametro(simEspecial.getTipo()); 
                                                            sim.setRango1(simEspecial.getRango1());
                                                            sim.setRango2(simEspecial.getRango2());
                                                           
                                                        }else{
                                                            String err = "Linea " + AnalizadorLexico.getLineaActual() + ". Error Semantico: Variable re declarada en el mismo ambito";
                                                            erroresSemanticos.add(err);
                                                        }
                                                     }
                                                    }else{
                                                            String err = "Linea " + AnalizadorLexico.getLineaActual() + ". Error Semantico: Tipo no declarado ";
                                                            erroresSemanticos.add(err);
                                                    }
                                                    lista_identificadores.clear();
                                                }
break;
case 119:
//#line 774 "gramatica.y"
{
    ArbolSintactico arbIzq = (ArbolSintactico) val_peek(2);
    ArbolSintactico arbDer = (ArbolSintactico) val_peek(0);
    
    Simbolo simboloIzq = TablaDeSimbolos.obtenerSimbolo(arbIzq.getLex());
    Simbolo simboloDer = TablaDeSimbolos.obtenerSimbolo(arbDer.getLex());
    /*System.out.println("simbolo izq:" + simboloIzq.getUso());*/
    /*System.out.println("simbolo der:" + simboloDer.getUso());*/
    if(simboloIzq.getUso().equals("constante") && simboloDer.getUso().equals("constante")){
       if(simboloIzq.getTipo().equals("INTEGER") && simboloDer.getTipo().equals("INTEGER")){
              yyval = new NodoComun("+", arbIzq, arbDer);
       }else{
          if(simboloIzq.getTipo().equals("DOUBLE") && simboloDer.getTipo().equals("DOUBLE")){
             yyval = new NodoComun("+", arbIzq, arbDer);
          }else{
              if(simboloIzq.getTipo().equals("DOUBLE") && simboloDer.getTipo().equals("INTEGER")){
                      yyval = new NodoComun("+", arbIzq, new NodoControl("CONVERSION", arbDer));
              }else{
                     yyval = new NodoComun("+",new NodoControl("CONVERSION", arbIzq),arbDer);
              }
          }
      }     
     }else{ simboloIzq = TablaDeSimbolos.obtenerSimbolo(arbIzq.getLex()+":"+ambitoActual);
            simboloDer = TablaDeSimbolos.obtenerSimbolo(arbDer.getLex()+":"+ambitoActual);
         if (simboloIzq.getTipo().equals("INTEGER") && simboloDer.getTipo().equals("DOUBLE")) {
                    /*simboloIzq.setTipo("DOUBLE");*/
                    yyval = new NodoComun("+",new NodoControl("CONVERSION", arbIzq) , arbDer);
           } else if (simboloIzq.getTipo().equals("DOUBLE") && simboloDer.getTipo().equals("INTEGER")) {
                 /*simboloDer.setTipo("DOUBLE");*/
                 yyval = new NodoComun("+", arbIzq,new NodoControl("CONVERSION", arbDer));
           } else if (simboloIzq.getTipo().equals(simboloDer.getTipo())) {
               yyval = new NodoComun("+", arbIzq, arbDer);
       }}
}
break;
case 120:
//#line 808 "gramatica.y"
{
ArbolSintactico arbIzq = (ArbolSintactico) val_peek(2);
    ArbolSintactico arbDer = (ArbolSintactico) val_peek(0);
    
    Simbolo simboloIzq = TablaDeSimbolos.obtenerSimbolo(arbIzq.getLex());
    Simbolo simboloDer = TablaDeSimbolos.obtenerSimbolo(arbDer.getLex());
    if(simboloIzq.getUso().equals("constante") && simboloDer.getUso().equals("constante")){
       if(simboloIzq.getTipo().equals("INTEGER") && simboloDer.getTipo().equals("INTEGER")){
              yyval = new NodoComun("-", arbIzq, arbDer);
       }else{
          if(simboloIzq.getTipo().equals("DOUBLE") && simboloDer.getTipo().equals("DOUBLE")){
             yyval = new NodoComun("-", arbIzq, arbDer);
          }else{
              if(simboloIzq.getTipo().equals("DOUBLE") && simboloDer.getTipo().equals("INTEGER")){
                      yyval = new NodoComun("-", arbIzq, new NodoControl("CONVERSION", arbDer));
              }else{
                     yyval = new NodoComun("-",new NodoControl("CONVERSION", arbIzq),arbDer);
              }
          }
      }      
     }else{ simboloIzq = TablaDeSimbolos.obtenerSimbolo(arbIzq.getLex()+":"+ambitoActual);
            simboloDer = TablaDeSimbolos.obtenerSimbolo(arbDer.getLex()+":"+ambitoActual);
     
         if (simboloIzq.getTipo().equals("INTEGER") && simboloDer.getTipo().equals("DOUBLE")) {
                    /*System.out.println("EXP_ARIT:se modifico tipo lado izq");*/
                   /* simboloIzq.setTipo("DOUBLE");*/
                    yyval = new NodoComun("-", new NodoControl("CONVERSION", arbIzq), arbDer);
           } else if (simboloIzq.getTipo().equals("DOUBLE") && simboloDer.getTipo().equals("INTEGER")) {
              /* System.out.println("EXP_ARIT:se modifico tipo lado der");*/
                 simboloDer.setTipo("DOUBLE");
                 yyval = new NodoComun("-", arbIzq, new NodoControl("CONVERSION", arbDer));
           } else if (simboloIzq.getTipo().equals(simboloDer.getTipo())) {
               /*System.out.println("EXP_ARIT:NO SE MODIFICO NINGUN TIPO");*/
               yyval = new NodoComun("-", arbIzq, arbDer);
       }}

}
break;
case 121:
//#line 845 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de operando en expresión.");yyval = nodoError;}
break;
case 122:
//#line 846 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de operando en expresión.");yyval = nodoError;}
break;
case 123:
//#line 847 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de operando en expresión." );yyval = nodoError;}
break;
case 124:
//#line 848 "gramatica.y"
{agregarError(ConstantesCompilador.ERROR, ConstantesCompilador.SINTACTICO, "Falta de operando en expresión.");yyval = nodoError;}
break;
case 125:
//#line 849 "gramatica.y"
{ yyval = val_peek(0);}
break;
case 126:
//#line 852 "gramatica.y"
{
   ArbolSintactico arbIzq = (ArbolSintactico) val_peek(2);
    ArbolSintactico arbDer = (ArbolSintactico) val_peek(0);
    
    Simbolo simboloIzq = TablaDeSimbolos.obtenerSimbolo(arbIzq.getLex());
    Simbolo simboloDer = TablaDeSimbolos.obtenerSimbolo(arbDer.getLex());
    /*System.out.println("simbolo izq:" + simboloIzq.getUso());*/
    /*System.out.println("simbolo der:" + simboloDer.getUso());*/
    if(simboloIzq.getUso().equals("constante") && simboloDer.getUso().equals("constante")){
       if(simboloIzq.getTipo().equals("INTEGER") && simboloDer.getTipo().equals("INTEGER")){
              yyval = new NodoComun("*", arbIzq, arbDer);
       }else{
          if(simboloIzq.getTipo().equals("DOUBLE") && simboloDer.getTipo().equals("DOUBLE")){
             yyval = new NodoComun("*", arbIzq, arbDer);
          }else{
              if(simboloIzq.getTipo().equals("DOUBLE") && simboloDer.getTipo().equals("INTEGER")){
                      yyval = new NodoComun("*", arbIzq, new NodoControl("CONVERSION", arbDer));
              }else{
                     yyval = new NodoComun("*",new NodoControl("CONVERSION", arbIzq),arbDer);
              }
          }
      }      
     }else{ simboloIzq = TablaDeSimbolos.obtenerSimbolo(arbIzq.getLex()+":"+ambitoActual);
            simboloDer = TablaDeSimbolos.obtenerSimbolo(arbDer.getLex()+":"+ambitoActual);
     
         if (simboloIzq.getTipo().equals("INTEGER") && simboloDer.getTipo().equals("DOUBLE")) {
                    /*System.out.println("EXP_ARIT:se modifico tipo lado izq");*/
                   /* simboloIzq.setTipo("DOUBLE");*/
                    yyval = new NodoComun("*", new NodoControl("CONVERSION", arbIzq), arbDer);
           } else if (simboloIzq.getTipo().equals("DOUBLE") && simboloDer.getTipo().equals("INTEGER")) {
                    /*System.out.println("EXP_ARIT:se modifico tipo lado der");*/
                    /*simboloDer.setTipo("DOUBLE");*/
                 yyval = new NodoComun("*", arbIzq, new NodoControl("CONVERSION", arbDer));
           } else if (simboloIzq.getTipo().equals(simboloDer.getTipo())) {
                    /*System.out.println("EXP_ARIT:NO SE MODIFICO NINGUN TIPO");*/
                    yyval = new NodoComun("*", arbIzq, arbDer);
       }}
    

}
break;
case 127:
//#line 893 "gramatica.y"
{
    ArbolSintactico arbIzq = (ArbolSintactico) val_peek(2);
    ArbolSintactico arbDer = (ArbolSintactico) val_peek(0);
    
    Simbolo simboloIzq = TablaDeSimbolos.obtenerSimbolo(arbIzq.getLex());
    Simbolo simboloDer = TablaDeSimbolos.obtenerSimbolo(arbDer.getLex());
   /* System.out.println("simbolo izq:" + simboloIzq.getUso());*/
    /*System.out.println("simbolo der:" + simboloDer.getUso());*/
    if(simboloIzq.getUso().equals("constante") && simboloDer.getUso().equals("constante")){
       if(simboloIzq.getTipo().equals("INTEGER") && simboloDer.getTipo().equals("INTEGER")){
              yyval = new NodoComun("/", arbIzq, arbDer);
       }else{
          if(simboloIzq.getTipo().equals("DOUBLE") && simboloDer.getTipo().equals("DOUBLE")){
             yyval = new NodoComun("/", arbIzq, arbDer);
          }else{
              if(simboloIzq.getTipo().equals("DOUBLE") && simboloDer.getTipo().equals("INTEGER")){
                      yyval = new NodoComun("/", arbIzq, new NodoControl("CONVERSION", arbDer));
              }else{
                     yyval = new NodoComun("/",new NodoControl("CONVERSION", arbIzq),arbDer);
              }
          }
      }      
     }else{ simboloIzq = TablaDeSimbolos.obtenerSimbolo(arbIzq.getLex()+":"+ambitoActual);
            simboloDer = TablaDeSimbolos.obtenerSimbolo(arbDer.getLex()+":"+ambitoActual);
     
         if (simboloIzq.getTipo().equals("INTEGER") && simboloDer.getTipo().equals("DOUBLE")) {
                    /*System.out.println("EXP_ARIT:se modifico tipo lado izq");*/
                   /* simboloIzq.setTipo("DOUBLE");*/
                    yyval = new NodoComun("/", new NodoControl("CONVERSION", arbIzq), arbDer);
           } else if (simboloIzq.getTipo().equals("DOUBLE") && simboloDer.getTipo().equals("INTEGER")) {
               /*System.out.println("EXP_ARIT:se modifico tipo lado der");*/
               /*  simboloDer.setTipo("DOUBLE");*/
                 yyval = new NodoComun("/", arbIzq, new NodoControl("CONVERSION", arbDer));
           } else if (simboloIzq.getTipo().equals(simboloDer.getTipo())) {
               /*System.out.println("EXP_ARIT:NO SE MODIFICO NINGUN TIPO");*/
               yyval = new NodoComun("/", arbIzq, arbDer);
       }}
    

}
break;
case 128:
//#line 933 "gramatica.y"
{ yyval = val_peek(0);}
break;
case 129:
//#line 934 "gramatica.y"
{yyval =  val_peek(0);}
break;
case 130:
//#line 937 "gramatica.y"
{  if(TablaDeSimbolos.existeSimboloAmbitoActual(val_peek(0).sval +":"+ambitoActual)) {             
                yyval = (ArbolSintactico) new NodoHoja(val_peek(0).sval+":"+ambitoActual);
                }else {
                   Simbolo simbol = TablaDeSimbolos.obtenerSimbolodentrodelAmbito(val_peek(0).sval+":"+ambitoActual);
                   if (TablaDeSimbolos.existeSimboloAmbitoActual(simbol.getLexema())){
                     yyval = (ArbolSintactico) new NodoHoja(simbol.getLexema());
                   }else{
                   String err = "Linea " + AnalizadorLexico.getLineaActual() + ". Error Semantico: Variable no declarada en el ambito:" + ambitoActual ;
                   erroresSemanticos.add(err);
                   yyval = (ArbolSintactico) nodoError;
                   }
        }
                }
break;
case 131:
//#line 950 "gramatica.y"
{ yyval = val_peek(0);}
break;
case 132:
//#line 953 "gramatica.y"
{ /*System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio " + $1.sval + " CTE positiva");*/
  /* if (($1.sval).contains(".")){ }*/
     if (!val_peek(0).sval.contains(String.valueOf(".")))
        ConstantePositiva(val_peek(0).sval);
     yyval = (ArbolSintactico) new NodoHoja(String.valueOf(val_peek(0).sval)); /* padre ------- $1*/
            if(auxTipoAsig.equals("")){
                /*Esto me sirve para resolver la comparacion del parametro de una funcion*/
                auxTipoAsig = TablaDeSimbolos.obtenerSimboloSinAmbito(val_peek(0).sval).getTipo();
            } }
break;
case 133:
//#line 962 "gramatica.y"
{ /*System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio " + $1.sval + $2.sval + " CTE negativa");*/
   /* if (($2.sval).contains(".")){$2.sval = String.valueOf(PasarAFloatJava($2.sval));}*/
     if (!val_peek(1).sval.contains(String.valueOf(".")))
     ConstanteNegativa(val_peek(0).sval);
     yyval = (ArbolSintactico) new NodoHoja("-" + String.valueOf(val_peek(0).sval)); /* padre ------- $1*/
        if(auxTipoAsig.equals("")){
            auxTipoAsig = TablaDeSimbolos.obtenerSimboloSinAmbito(val_peek(1).sval).getTipo();
        }}
break;
//#line 2245 "Parser.java"
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

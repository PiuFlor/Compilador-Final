.386 
.model flat, stdcall 
option casemap :none 
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\user32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib 
.data 
$errorDivisionPorCero db " Error Assembler:No se puede realizar la division por cero" , 0 
$errorOverflowSumaDouble db " Error Assembler: overflow en la suma de flotantes ", 0 
$errorOverRangoSubtipoMenor db " Error Assembler: valor menor a rango minimo ", 0 
$errorOverRangoSubtipoMayor db " Error Assembler: valor mayor a rango superior ", 0 
$constMaximoDouble dq 1.7976931348623157E308 
$auxIntCompIzq dw ? 
$auxIntCompDer dw ? 
$auxDoubleCompIzq dq ? 
$auxDoubleCompDer dq ? 
$x$global$f1 dw ? 
$x$global$f1$f2 dw ? 
$1 dw 1
ENTRO_A_F1 db "[ENTRO A F1]" , 0 
ENTRO_A_F2 db "[ENTRO A F2]" , 0 
$a$global dw ? 
$3 dw 3
@aux0 dw ? 
.code
f2: 
MOV AX , $1
MOV $x$global$f1$f2, AX
invoke MessageBox, NULL, addr ENTRO_A_F2, addr ENTRO_A_F2, MB_OK 
MOV AX , $x$global$f1$f2 
ret  
f1: 
MOV AX , $1
MOV $x$global$f1, AX
invoke MessageBox, NULL, addr ENTRO_A_F1, addr ENTRO_A_F1, MB_OK 
MOV AX ,$x$global$f1
MOV @aux0 , AX 
PUSH  @aux0
CALL f2 
MOV AX , @aux0
MOV $x$global$f1, AX
MOV AX , $x$global$f1 
ret  
main:
MOV AX ,$3
MOV @aux0 , AX 
PUSH  @aux0
CALL f1 
MOV AX , @aux0
MOV $a$global, AX
invoke ExitProcess, 0 
errorDivisionPorCero: 
invoke MessageBox, NULL, addr $errorDivisionPorCero , addr $errorDivisionPorCero , MB_OK 
invoke ExitProcess, 0 
errorOverflowSumaDouble: 
invoke MessageBox, NULL, addr $errorOverflowSumaDouble , addr $errorOverflowSumaDouble , MB_OK 
invoke ExitProcess, 0 
errorOverRangoSubtipoMenor: 
invoke MessageBox, NULL, addr $errorOverRangoSubtipoMenor , addr $errorOverRangoSubtipoMenor , MB_OK 
invoke ExitProcess, 0 
errorOverRangoSubtipoMayor: 
invoke MessageBox, NULL, addr $errorOverRangoSubtipoMayor , addr $errorOverRangoSubtipoMayor , MB_OK 
invoke ExitProcess, 0 
end main
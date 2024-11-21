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
$b$global dw ? 
$15 dw 15
$10 dw 10
$5 dw 5
ANTES_DEL_GOTO db "[ANTES DEL GOTO]" , 0 
DESPUES_DEL_GOTO db "[DESPUES DEL GOTO]" , 0 
DESPUES_DEL_SALTO db "[DESPUES DEL SALTO]" , 0 
.code
main:
MOV AX , $15
MOV $b$global, AX
label1: 
MOV AX , $10
MOV $auxIntCompIzq , AX 
MOV AX , $b$global
MOV $auxIntCompDer , AX 
MOV AX , $auxIntCompIzq 
CMP AX , $auxIntCompDer 
JAE label2
MOV AX , $5
MOV $b$global, AX
invoke MessageBox, NULL, addr ANTES_DEL_GOTO, addr ANTES_DEL_GOTO, MB_OK 
JMP afuera@ 
invoke MessageBox, NULL, addr DESPUES_DEL_GOTO, addr DESPUES_DEL_GOTO, MB_OK 
JMP label1 
label2: 
afuera@: 
invoke MessageBox, NULL, addr DESPUES_DEL_SALTO, addr DESPUES_DEL_SALTO, MB_OK 
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
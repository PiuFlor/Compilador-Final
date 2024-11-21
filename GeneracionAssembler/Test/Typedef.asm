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
$1 dw 1
$$1 dw -1
$10 dw 10
$n$global dw ? 
$a$global dw ? 
DENTRO__IF db "[DENTRO  IF]" , 0 
DENTRO__ELSE db "[DENTRO  ELSE]" , 0 
.code
main:
MOV AX , $1
MOV $a$global, AX
MOV AX , $a$global
MOV BX ,-1
CMP AX , BX
JL errorOverRangoSubtipoMenor 
MOV BX ,10
CMP AX , BX
JG errorOverRangoSubtipoMayor 
MOV $n$global, AX
MOV AX , $a$global
MOV $auxIntCompIzq , AX 
MOV AX , $n$global
MOV $auxIntCompDer , AX 
MOV AX , $auxIntCompIzq 
CMP AX , $auxIntCompDer 
JNE label1
invoke MessageBox, NULL, addr DENTRO__IF, addr DENTRO__IF, MB_OK 
JMP label2
label1: 
invoke MessageBox, NULL, addr DENTRO__ELSE, addr DENTRO__ELSE, MB_OK 
label2: 
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
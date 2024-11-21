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
$p1$global dw 0, 0 
$p2$global dw 0, 0 
$1 dw 1
$34 dw 34
DENTROIF db "[DENTRO  IF]" , 0 
DENTROELSE db "[DENTRO  ELSE]" , 0 
.code
main:
MOV AX , $34
MOV [$p1$global], AX
MOV AX , $p2$global
MOV $p1$global, AX
MOV AX , $p1$global
MOV $auxIntCompIzq , AX 
MOV AX , $p2$global
MOV $auxIntCompDer , AX 
MOV AX , $auxIntCompIzq 
CMP AX , $auxIntCompDer 
JNE label1
invoke MessageBox, NULL, addr DENTROIF, addr DENTROIF, MB_OK 
JMP label2
label1: 
invoke MessageBox, NULL, addr DENTROELSE, addr DENTROELSE, MB_OK 
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
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
auxCadena db 50 dup(?) 
msg db 'Resultado: %d' , 0  
msg2 db 'Resultado: %d.%02d' , 0 
buffer db 64 dup (?) 
$partEnt dd ? 
$partDec dd ? 
fpu_cw WORD ? 
factor dq 100.0 
$p1$global dw 0, 0 
$p2$global dw 0, 0 
$1 dw 1
$34 dw 34
$aux$global dw ? 
bien1 db "[bien1]" , 0 
$2 dw 2
$a$global dq ? 
bien2 db "[bien2]" , 0 
.code
main:
MOV AX , $34
MOV [$p1$global], AX
MOV AX , [$p1$global]
MOV $aux$global, AX
MOV AX , $aux$global
MOV $auxIntCompIzq , AX 
MOV AX , $34
MOV $auxIntCompDer , AX 
MOV AX , $auxIntCompIzq 
CMP AX , $auxIntCompDer 
JNE label1
invoke MessageBox, NULL, addr bien1, addr bien1, MB_OK 
label1: 
MOV AX , $2
MOV [$p1$global+4], AX
MOV AX , $p1$global
MOV $p2$global, AX
MOV AX , [$p2$global]
MOV $aux$global, AX
MOV AX , $aux$global
MOV $auxIntCompIzq , AX 
MOV AX , $34
MOV $auxIntCompDer , AX 
MOV AX , $auxIntCompIzq 
CMP AX , $auxIntCompDer 
JNE label2
invoke MessageBox, NULL, addr bien2, addr bien2, MB_OK 
label2: 
FSTP $a$global
FILD $p1$global
FSTP $a$global
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
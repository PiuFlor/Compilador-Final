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
$x$global$f1 dw ? 
ENTRO_A_F1 db "[ENTRO A F1]" , 0 
$3 dw 3
$a$global dw ? 
.code
f1: 
PUSH EBP
MOV EBP, ESP 
MOV AX , [EBP+8]
MOV $x$global$f1, AX
invoke MessageBox, NULL, addr ENTRO_A_F1, addr ENTRO_A_F1, MB_OK 
MOV AX, $x$global$f1
POP EBP
ret
MOV AX , $3
MOV $x$global$f1, AX
main:
MOV AX ,$3
PUSH  AX
CALL f1 
ADD ESP, 4
MOV $a$global, AX
MOV AX, $a$global 
invoke wsprintf, addr auxCadena, addr msg , AX 
invoke MessageBox, NULL, addr auxCadena, addr auxCadena, MB_OK 
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
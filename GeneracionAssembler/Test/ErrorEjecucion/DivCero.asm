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
$a$global dw ? 
$7 dw 7
$0 dw 0
$b$global dq ? 
$7_0 dq 7.0
$0_0 dq 0.0
@aux1 dw ? 
@aux2 dq ? 
.code
main:
MOV AX , $7
MOV $a$global, AX
CMP $0, 0 
JE errorDivisionPorCero
MOV AX , $a$global
XOR DX,DX 
MOV BX , $0 
DIV BX 
MOV @aux1 , AX 
MOV AX , @aux1
MOV $a$global, AX
FLD $7_0
FSTP $b$global
FLD $0_0 
FTST 
FNSTSW AX 
SAHF 
JE errorDivisionPorCero 
FLD $b$global 
FDIV $0_0
FSTP @aux2
FLD @aux2
FSTP $b$global
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
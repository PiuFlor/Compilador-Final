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
$x$global dw ? 
$a$global dq ? 
$42 dw 42
$3_14 dq 3.14
El_valor_de_x_es_ db "[El valor de x es ]" , 0 
$5 dw 5
El_valor_de_a_es_ db "[El valor de a es ]" , 0 
$3_0 dq 3.0
@aux1 dw ? 
@aux2 dq ? 
.code
main:
MOV AX , $42
MOV $x$global, AX
FLD $3_14
FSTP $a$global
invoke MessageBox, NULL, addr El_valor_de_x_es_, addr El_valor_de_x_es_, MB_OK 
MOV AX , $x$global
ADD AX , $5
MOV @aux1 , AX 
MOV AX, @aux1 
invoke wsprintf, addr auxCadena, "%d", AX 
invoke MessageBox, NULL, addr auxCadena, addr auxCadena, MB_OK 
invoke MessageBox, NULL, addr El_valor_de_a_es_, addr El_valor_de_a_es_, MB_OK 
FLD $a$global
FADD $3_0
FSTP @aux2
FLD @aux2 
FLD $constMaximoDouble 
FCOM 
FNSTSW AX 
SAHF 
JG errorOverflowSumaDouble 
FLD @aux2 
invoke wsprintf, addr auxCadena ,"%f",@aux2 
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
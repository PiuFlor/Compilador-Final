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
$a$global dw ? 
$b$global dw ? 
$c$global dw ? 
$d$global dq ? 
$1 dw 1
$2 dw 2
$3 dw 3
$245_3 dq 245.3
$3_0 dq 3.0
a_mayor_igual_2 db "[a mayor igual 2]" , 0 
$35 dw 35
$50 dw 50
a_menor_50 db "[a menor 50]" , 0 
@aux1 dw ? 
.code
main:
MOV AX , $1
ADD AX , $1
MOV @aux1 , AX 
MOV AX , @aux1
MOV $a$global, AX
MOV AX , $2
MOV $b$global, AX
FLD $245_3
FSTP $d$global
FLD $d$global
FSTP $auxDoubleCompIzq 
FLD $3_0
FSTP $auxDoubleCompDer 
FLD $auxDoubleCompIzq 
FCOM $auxDoubleCompDer 
FNSTSW AX 
SAHF 
JNGE label1
invoke MessageBox, NULL, addr a_mayor_igual_2, addr a_mayor_igual_2, MB_OK 
label1: 
MOV AX , $35
MOV $a$global, AX
MOV AX , $a$global
MOV $auxIntCompIzq , AX 
MOV AX , $50
MOV $auxIntCompDer , AX 
MOV AX , $auxIntCompIzq 
CMP AX , $auxIntCompDer 
JAE label2
invoke MessageBox, NULL, addr a_menor_50, addr a_menor_50, MB_OK 
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
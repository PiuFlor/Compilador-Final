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
$b$global dw ? 
$5 dw 5
$7 dw 7
$8 dw 8
a_mayor_a_b db "[a mayor a b]" , 0 
a__menor_o_igual_a_b db "[a  menor o igual a b]" , 0 
.code
main:
MOV AX , $5
MOV $a$global, AX
MOV AX , $5
MOV $b$global, AX
label1: 
MOV AX , $a$global
MOV $auxIntCompIzq , AX 
MOV AX , $b$global
MOV $auxIntCompDer , AX 
MOV AX , $auxIntCompIzq 
CMP AX , $auxIntCompDer 
JNE label2
MOV AX , $7
MOV $a$global, AX
label3: 
MOV AX , $a$global
MOV $auxIntCompIzq , AX 
MOV AX , $b$global
MOV $auxIntCompDer , AX 
MOV AX , $auxIntCompIzq 
CMP AX , $auxIntCompDer 
JLE label4
MOV AX , $8
MOV $b$global, AX
invoke MessageBox, NULL, addr a_mayor_a_b, addr a_mayor_a_b, MB_OK 
JMP label3 
label4: 
invoke MessageBox, NULL, addr a__menor_o_igual_a_b, addr a__menor_o_igual_a_b, MB_OK 
JMP label1 
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
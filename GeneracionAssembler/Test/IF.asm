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
$errorOverflowMultEntero db " Error Assembler: overflow en producto de enteros ", 0
$errorOverflowSumaFlotantes db " Error Assembler: overflow en la suma de flotantes ", 0
$constMaximoFloat dd 3.40282347E+38
$auxIntCompIzq dw ?
$auxIntCompDer dw ?
$auxDoubleCompIzq dq ?
$auxDoubleCompDer dq ?
$a$global dw ?
$b$global dw ?
$6 dw 6
$5 dw 5
_A_es_menor_a_B_ db "[ A es menor a B ]" , 0
_A_es_mayor_a_B_ db "[ A es mayor a B ]" , 0
_A_es_igual_a_B_ db "[ A es igual a B ]" , 0
.code
main:
MOV AX , $6
MOV $a$global, AX
MOV AX , $5
MOV $b$global, AX
MOV AX , $a$global
MOV $auxIntCompIzq , AX
MOV AX , $b$global
MOV $auxIntCompDer , AX
MOV AX , $auxIntCompIzq
CMP AX , $auxIntCompDer
JAE label1
MOV AX , $b$global
MOV $a$global, AX
invoke MessageBox, NULL, addr _A_es_menor_a_B_, addr _A_es_menor_a_B_, MB_OK
JMP label2
label1:
MOV AX , $a$global
MOV $b$global, AX
invoke MessageBox, NULL, addr _A_es_mayor_a_B_, addr _A_es_mayor_a_B_, MB_OK
label2:
MOV AX , $a$global
MOV $auxIntCompIzq , AX
MOV AX , $b$global
MOV $auxIntCompDer , AX
MOV AX , $auxIntCompIzq
CMP AX , $auxIntCompDer
JNE label3
invoke MessageBox, NULL, addr _A_es_igual_a_B_, addr _A_es_igual_a_B_, MB_OK
label3:
invoke ExitProcess, 0
errorDivisionPorCero:
invoke MessageBox, NULL, addr $errorDivisionPorCero , addr $errorDivisionPorCero , MB_OK
invoke ExitProcess, 0
errorOverflowMultEntero:
invoke MessageBox, NULL, addr $errorOverflowMultEntero , addr $errorOverflowMultEntero , MB_OK
invoke ExitProcess, 0
errorOverflowSumaFlotantes:
invoke MessageBox, NULL, addr $errorOverflowSumaFlotantes , addr $errorOverflowSumaFlotantes , MB_OK
invoke ExitProcess, 0
end main
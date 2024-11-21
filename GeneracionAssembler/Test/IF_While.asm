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
$6 dw 6
$5 dw 5
_A_es_menor_a_B_ db "[ A es menor a B ]" , 0 
_A_es_mayor_a_B_ db "[ A es mayor a B ]" , 0 
_A_es_igual_a_B_ db "[ A es igual a B ]" , 0 
$10 dw 10
_ENTRO_al_IF db "[ ENTRO al IF]" , 0 
_ENTRO_EN_EL_WHILE db "[ ENTRO EN EL WHILE]" , 0 
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
MOV AX , $10
MOV $a$global, AX
label4: 
MOV AX , $a$global
MOV $auxIntCompIzq , AX 
MOV AX , $b$global
MOV $auxIntCompDer , AX 
MOV AX , $auxIntCompIzq 
CMP AX , $auxIntCompDer 
JE label5
MOV AX , $b$global
MOV $a$global, AX
MOV AX , $a$global
MOV $auxIntCompIzq , AX 
MOV AX , $b$global
MOV $auxIntCompDer , AX 
MOV AX , $auxIntCompIzq 
CMP AX , $auxIntCompDer 
JNE label6
invoke MessageBox, NULL, addr _ENTRO_al_IF, addr _ENTRO_al_IF, MB_OK 
label6: 
invoke MessageBox, NULL, addr _ENTRO_EN_EL_WHILE, addr _ENTRO_EN_EL_WHILE, MB_OK 
JMP label4 
label5: 
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
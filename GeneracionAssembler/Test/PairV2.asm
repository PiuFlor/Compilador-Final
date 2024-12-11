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
_variables_iguales db "[ variables iguales]" , 0 
variables_distintas db "[variables distintas]" , 0 
$0 dw 0
arreglos_iguales db "[arreglos iguales]" , 0 
arreglos_distintos db "[arreglos distintos]" , 0 
.code
main:
MOV AX , $34
MOV [$p1$global], AX
MOV AX , [$p1$global]
MOV [$p2$global], AX
MOV AX , [$p1$global]
MOV $auxIntCompIzq , AX 
MOV AX , [$p2$global]
MOV $auxIntCompDer , AX 
MOV AX , $auxIntCompIzq 
CMP AX , $auxIntCompDer 
JNE label1
invoke MessageBox, NULL, addr _variables_iguales, addr _variables_iguales, MB_OK 
MOV AX, [$p1$global] 
invoke wsprintf, addr auxCadena, addr msg , AX 
invoke MessageBox, NULL, addr auxCadena, addr auxCadena, MB_OK 
MOV AX, [$p2$global] 
invoke wsprintf, addr auxCadena, addr msg , AX 
invoke MessageBox, NULL, addr auxCadena, addr auxCadena, MB_OK 
JMP label2
label1: 
invoke MessageBox, NULL, addr variables_distintas, addr variables_distintas, MB_OK 
label2: 
MOV AX , $p2$global
MOV $p1$global, AX
MOV AX , $0
MOV $p2$global, AX
MOV AX , $0
MOV $p1$global, AX
MOV AX , $0
MOV $p2$global, AX
MOV AX , $0
MOV $p1$global, AX
MOV AX , $p1$global
MOV $auxIntCompIzq , AX 
MOV AX , $p2$global
MOV $auxIntCompDer , AX 
MOV AX , $auxIntCompIzq 
CMP AX , $auxIntCompDer 
JNE label3
invoke MessageBox, NULL, addr arreglos_iguales, addr arreglos_iguales, MB_OK 
JMP label4
label3: 
invoke MessageBox, NULL, addr arreglos_distintos, addr arreglos_distintos, MB_OK 
label4: 
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
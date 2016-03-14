package edu.yildiz.emu8086.derleyici.asm.komut;

/**
 *
 * Tokenizer'da kullanılmak üzere hangi assembly komutlarının tanındığını 
 * listeleyen enum.
 * 
 * @author MMC
 */
public enum Komutlar
{
    //Binary/Decimal Arithmetic
        ADC, ADD, INC, DEC, SBB, SUB, IMUL, MUL, SAL, SHL,
        DIV, IDIV, SAR, SHR, CBW, CWD, NEG,
        
    //Comparison
        CMP, TEST,
    
    //Control Transfer
        JA, JAE, 
        JB, JBE, JNB, JNBE,
        JC, JCXZ, JNC,
        JE, JNE,
        JG, JGE, JNG, JNGE,
        JL, JLE, JNL, JNLE,
        JMP, JNA,
        JNO, JNS, JNZ, JO, JS, JZ,
        LOOP, LOOPE, LOOPNE, LOOPNZ, LOOPZ,
        
    //Data Movement
        MOV, XCHG,
    
    //Flag
        CLC, CMC, RCL, RCR, STC,
    
    //Logical
        AND, NOT, OR, XOR, ROL, ROR,
          
    //Processor Control
        HLT, NOP,          
}

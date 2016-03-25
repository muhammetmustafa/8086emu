
package board;

import edu.yildiz.emu8086.board.eu.ALU;
import edu.yildiz.emu8086.board.eu.AluIslemi;
import edu.yildiz.emu8086.tipler.FlagRegister;
import edu.yildiz.emu8086.tipler.VeriUzunlugu;
import edu.yildiz.emu8086.tipler.Word;
import edu.yildiz.emu8086.util.Donusum;
import org.junit.Test;

/**
 *
 * @author MMC
 */
public class AluTest 
{
    @Test
    public void aluTest()
    {
        FlagRegister flagRegister = new FlagRegister();
        ALU alu = new ALU(flagRegister);
        
        alu.setSolOperand((byte)0x43);
        
        alu.islem(AluIslemi.Tur.NEG, VeriUzunlugu.Byte_8);
        
        System.out.println(Donusum.decimalToHexadecimal((byte)alu.getAluYazmac()));
        System.out.println(flagRegister);
    }
}

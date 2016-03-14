
package edu.yildiz.emu8086.derleyici.asm.komutlar.datamovement;

import edu.yildiz.emu8086.derleyici.asm.komut.OperandsizKomut;
import edu.yildiz.emu8086.derleyici.asm.operand.ByteRegisterOperand;
import edu.yildiz.emu8086.tipler.Yazmac;

/**
 *  LAHF copies the low byte of the Processor Status Flags (FLAGS) register to AH.
 * 
 * @author MMC
 */
public class LAHF extends OperandsizKomut
{
    public LAHF()
    {
        super.veriTransferIslemiEkle(new ByteRegisterOperand(Yazmac.AH), new ByteRegisterOperand(Yazmac.FLAGL));
    }
}


package edu.yildiz.emu8086.tipler;

/**
 *
 * @author MMC
 */
public class MemoryAddress 
{
    private int indis;
    private Memory.Bank bank;
    
    private Word segment;
    private Word offset;
    
    public MemoryAddress(Word segmentAdresi, Word offsetAdresi)
    {
        this.adreseGit(segmentAdresi, offsetAdresi);
    }

    @Override
    public String toString()
    {
        return this.segment + ":" + this.offset;
    }
    
    private int indisBul(Word segment, Word offset)
    {
        return (segment.getDegerInt() << 4) + offset.getDegerInt();
    }
    
    public final void adreseGit(Word segment, Word offset)
    {
        this.segment = segment;
        this.offset = offset;
        
        this.indis = this.indisBul(segment, offset);
        this.bank = offset.bitGet(0) ? Memory.Bank.HIGH : Memory.Bank.LOW;
    }
    
    /**
     * Bu adresi şu anki adresden bir sonrakini gösterecek şekilde ilerletir.
     * @return 
     */
    public MemoryAddress sonrakiAdres()
    {
        return new MemoryAddress(segment, offset.topla((byte)1));
    }
    
    public int getIndis()
    {
        return indis;
    }

    public Memory.Bank getBank()
    {
        return bank;
    }

    public Word getSegment()
    {
        return segment;
    }

    public Word getOffset()
    {
        return offset;
    }
}

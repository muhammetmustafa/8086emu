
package edu.yildiz.emu8086.tipler;

import static edu.yildiz.emu8086.tipler.FlagRegister.Tur.AUXILIARY;
import static edu.yildiz.emu8086.tipler.FlagRegister.Tur.CARRY;
import static edu.yildiz.emu8086.tipler.FlagRegister.Tur.DIRECTION;
import static edu.yildiz.emu8086.tipler.FlagRegister.Tur.INTERRUPT_ENABLE;
import static edu.yildiz.emu8086.tipler.FlagRegister.Tur.OVERFLOW;
import static edu.yildiz.emu8086.tipler.FlagRegister.Tur.PARITY;
import static edu.yildiz.emu8086.tipler.FlagRegister.Tur.SIGN;
import static edu.yildiz.emu8086.tipler.FlagRegister.Tur.TRACE;
import static edu.yildiz.emu8086.tipler.FlagRegister.Tur.ZERO;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 *
 * @author MMC
 */
public class FlagRegister 
{
    public enum Tur
    {
        CARRY (0),
        PARITY (2),
        AUXILIARY (4),
        ZERO (6),
        SIGN (7),
        TRACE (8),
        INTERRUPT_ENABLE (9),
        DIRECTION (10),
        OVERFLOW (11);
        
        private final byte bitSirasi;

        private Tur(int bitSirasi)
        {
            this.bitSirasi = (byte)bitSirasi;
        }
        
        public String kisaHali()
        {
            switch (this)
            {
                case CARRY:
                    return "CF";
                case PARITY:
                    return "PF";
                case AUXILIARY:
                    return "AF";
                case ZERO:
                    return "ZF";
                case SIGN:
                    return "SF";
                case TRACE:
                    return "TF";
                case INTERRUPT_ENABLE:
                    return "IF";
                case DIRECTION:
                    return "DF";
                case OVERFLOW:
                    return "OF";
                default:
                    return "";
            }
        }
        
        public byte getBitSirasi()
        {
            return bitSirasi;
        }
    }
    
    private final PropertyChangeSupport olayYonetici  = new PropertyChangeSupport(this);
    private final Word flags;

    public FlagRegister()
    {
        this.flags = new Word();
    }
    
    public final void reset()
    {
        clearAuxiliaryCarryFlag();
        clearCarryFlag();
        clearDirectionFlag();
        clearInterruptEnableFlag();
        clearOverflowFlag();
        clearParityFlag();
        clearSignFlag();
        clearTraceFlag();
        clearZeroFlag();
    }
    
    public Word getFlags()
    {
        return flags;
    }

    public boolean getFlag(Tur tur)
    {
        return this.flags.bitGet(tur.getBitSirasi());
    }
    
    public void setFlag(Tur tur, boolean deger)
    {
        this.flags.bitSet(tur.getBitSirasi(), deger);
    }
    
    @Override
    public String toString()
    {
        return this.flags.toStringBinary();
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        this.olayYonetici.addPropertyChangeListener(listener);
    }
    
    public boolean getCarryFlag()
    {
        return this.flags.bitGet(CARRY.getBitSirasi());
    }
    public void setCarryFlag()
    {
        this.flags.bitSet(CARRY.getBitSirasi(), true);
        
        this.olayYonetici.firePropertyChange(CARRY.kisaHali(), null, true);
    }
    public void setCarryFlag(boolean deger)
    {
        this.flags.bitSet(CARRY.getBitSirasi(), deger);
        
        this.olayYonetici.firePropertyChange(CARRY.kisaHali(), null, deger);
    }
    public void clearCarryFlag()
    {
        this.flags.bitSet(CARRY.getBitSirasi(), false);
        
        this.olayYonetici.firePropertyChange(CARRY.kisaHali(), null, false);
    }
    
    public boolean getOverflowFlag()
    {
        return this.flags.bitGet(OVERFLOW.getBitSirasi());
    }
    public void setOverflowFlag()
    {
        this.flags.bitSet(OVERFLOW.getBitSirasi(), true);
        
        this.olayYonetici.firePropertyChange(OVERFLOW.kisaHali(), null, true);
    }
    public void setOverflowFlag(boolean deger)
    {
        this.flags.bitSet(OVERFLOW.getBitSirasi(), deger);
        
        this.olayYonetici.firePropertyChange(OVERFLOW.kisaHali(), null, deger);
    }
    public void clearOverflowFlag()
    {
        this.flags.bitSet(OVERFLOW.getBitSirasi(), false);
        
        this.olayYonetici.firePropertyChange(OVERFLOW.kisaHali(), null, false);
    }
    
    public boolean getZeroFlag()
    {
        return this.flags.bitGet(ZERO.getBitSirasi());
    }
    public void setZeroFlag()
    {
        this.flags.bitSet(ZERO.getBitSirasi(), true);
        
        this.olayYonetici.firePropertyChange(ZERO.kisaHali(), null, true);
    }
    public void setZeroFlag(boolean deger)
    {
        this.flags.bitSet(ZERO.getBitSirasi(), deger);
        
        this.olayYonetici.firePropertyChange(ZERO.kisaHali(), null, deger);
    }
    public void clearZeroFlag()
    {
        this.flags.bitSet(ZERO.getBitSirasi(), false);
        
        this.olayYonetici.firePropertyChange(ZERO.kisaHali(), null, false);
    }
    
    public boolean getDirectionFlag()
    {
        return this.flags.bitGet(DIRECTION.getBitSirasi());
    }
    public void setDirectionFlag()
    {
        this.flags.bitSet(DIRECTION.getBitSirasi(), true);
        
        this.olayYonetici.firePropertyChange(DIRECTION.kisaHali(), null, true);
    }
    public void setDirectionFlag(boolean deger)
    {
        this.flags.bitSet(DIRECTION.getBitSirasi(), deger);
        
        this.olayYonetici.firePropertyChange(DIRECTION.kisaHali(), null, deger);
    }
    public void clearDirectionFlag()
    {
        this.flags.bitSet(DIRECTION.getBitSirasi(), false);
        
        this.olayYonetici.firePropertyChange(DIRECTION.kisaHali(), null, false);
    }
    
    public boolean getInterruptEnableFlag()
    {
        return this.flags.bitGet(INTERRUPT_ENABLE.getBitSirasi());
    }
    public void setInterruptEnableFlag()
    {
        this.flags.bitSet(INTERRUPT_ENABLE.getBitSirasi(), true);
        
        this.olayYonetici.firePropertyChange(INTERRUPT_ENABLE.kisaHali(), null, true);
    }
    public void setInterruptEnableFlag(boolean deger)
    {
        this.flags.bitSet(INTERRUPT_ENABLE.getBitSirasi(), deger);
        
        this.olayYonetici.firePropertyChange(INTERRUPT_ENABLE.kisaHali(), null, deger);
    }
    public void clearInterruptEnableFlag()
    {
        this.flags.bitSet(INTERRUPT_ENABLE.getBitSirasi(), false);
        
        this.olayYonetici.firePropertyChange(INTERRUPT_ENABLE.kisaHali(), null, false);
    }
    
    public boolean getTraceFlag()
    {
        return this.flags.bitGet(TRACE.getBitSirasi());
    }
    public void setTraceFlag()
    {
        this.flags.bitSet(TRACE.getBitSirasi(), true);
        
        this.olayYonetici.firePropertyChange(TRACE.kisaHali(), null, true);
    }
    public void setTraceFlag(boolean deger)
    {
        this.flags.bitSet(TRACE.getBitSirasi(), deger);
        
        this.olayYonetici.firePropertyChange(TRACE.kisaHali(), null, deger);
    }
    public void clearTraceFlag()
    {
        this.flags.bitSet(TRACE.getBitSirasi(), false);
        
        this.olayYonetici.firePropertyChange(TRACE.kisaHali(), null, false);
    }
    
    public boolean getSignFlag()
    {
        return this.flags.bitGet(SIGN.getBitSirasi());
    }
    public void setSignFlag()
    {
        this.flags.bitSet(SIGN.getBitSirasi(), true);
        
        this.olayYonetici.firePropertyChange(SIGN.kisaHali(), null, true);
    }
    public void setSignFlag(boolean deger)
    {
        this.flags.bitSet(SIGN.getBitSirasi(), deger);
        
        this.olayYonetici.firePropertyChange(SIGN.kisaHali(), null, deger);
    }
    public void clearSignFlag()
    {
        this.flags.bitSet(SIGN.getBitSirasi(), false);
        
        this.olayYonetici.firePropertyChange(SIGN.kisaHali(), null, false);
    }
    
    public boolean getAuxiliaryCarryFlag()
    {
        return this.flags.bitGet(AUXILIARY.getBitSirasi());
    }
    public void setAuxiliaryCarryFlag()
    {
        this.flags.bitSet(AUXILIARY.getBitSirasi(), true);
    
        this.olayYonetici.firePropertyChange(AUXILIARY.kisaHali(), null, true);
    }
    public void setAuxiliaryCarryFlag(boolean deger)
    {
        this.flags.bitSet(AUXILIARY.getBitSirasi(), deger);
    
        this.olayYonetici.firePropertyChange(AUXILIARY.kisaHali(), null, deger);
    }
    public void clearAuxiliaryCarryFlag()
    {
        this.flags.bitSet(AUXILIARY.getBitSirasi(), false);
        
        this.olayYonetici.firePropertyChange(AUXILIARY.kisaHali(), null, false);
    }
    
    public boolean getParityFlag()
    {
        return this.flags.bitGet(PARITY.getBitSirasi());
    }
    public void setParityFlag()
    {
        this.flags.bitSet(PARITY.getBitSirasi(), true);
        
        this.olayYonetici.firePropertyChange(PARITY.kisaHali(), null, true);
    }
    public void setParityFlag(boolean deger)
    {
        this.flags.bitSet(PARITY.getBitSirasi(), deger);
        
        this.olayYonetici.firePropertyChange(PARITY.kisaHali(), null, deger);
    }
    public void clearParityFlag()
    {
        this.flags.bitSet(PARITY.getBitSirasi(), false);
        
        this.olayYonetici.firePropertyChange(PARITY.kisaHali(), null, false);
    }
}

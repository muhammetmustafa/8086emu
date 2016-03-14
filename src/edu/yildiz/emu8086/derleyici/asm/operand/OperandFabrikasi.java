
package edu.yildiz.emu8086.derleyici.asm.operand;

import edu.yildiz.emu8086.derleyici.ParserException;
import edu.yildiz.emu8086.derleyici.Token;
import edu.yildiz.emu8086.derleyici.TokenTipi;
import edu.yildiz.emu8086.tipler.Yazmac;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author MMC
 */
public class OperandFabrikasi 
{

    public OperandFabrikasi()
    {
    }
    
    public static Operand yeniOperand(List<Token> tokens) throws ParserException
    {
        if (tokens.size() == 1)
        {
            return tekTokenAnaliz(tokens.get(0));
        }
        else if (tokens.size() > 1)
        {
            return cokluTokenAnaliz(tokens);
        }
        else
        {
            throw new ParserException("Operand oluşturacak token toplanmamış!");
        }
    }
    
    private static Operand tekTokenAnaliz(Token t) throws ParserException
    {
        try
        {
            switch (t.type)
            {
                case LITERAL: 
                    return ImmediateOperand.tesbit(t.text);
                case YAZMAC: 
                    return RegisterOperand.tesbit(t);
                case IDENTIFIER:
                    return new EtiketOperand(t.text);
                default:
                    throw new ParserException(String.format("'%s' parametresi hatalı", t.text), t.line, t.col);
            }
        }
        catch (ParserException e)
        {
            throw e;
        }
        catch (IllegalArgumentException e)
        {
            throw new ParserException(e.getMessage());
        }
    }
    
    private static Operand cokluTokenAnaliz(List<Token> tokenlar) throws ParserException 
    {
        Token t0 = tokenlar.get(0);
        
        if (t0.type == TokenTipi.ARTI || t0.type == TokenTipi.EKSI)
        {
            String isaret = t0.type.getKarakter();
            
            if (tokenlar.get(1).type == TokenTipi.LITERAL)
            {
                return ImmediateOperand.tesbit(isaret + tokenlar.get(1).text);
            }
            else
            {
                throw new ParserException("+ veya - den sonra literal gelmelid");
            }
        }
        
        MemoryOperandOtomat moo = new MemoryOperandOtomat(tokenlar);
        moo.calistir();
        
        return new MemoryOperand(moo.getSegmentOverride(), moo.getOffset(), moo.getDisplacement());
    }
    
    private static class MemoryOperandOtomat
    {
        private final List<Token> tokenlar;
        private int iToken = 0;
        
        private Yazmac segmentOverride = null;
        private ImmediateOperand displacement = null;
        private boolean displacementNegate = false;
        private Yazmac[] offset = null;
        
        public MemoryOperandOtomat(List<Token> tokenlar)
        {
            this.tokenlar = tokenlar;
        }

        public Yazmac getSegmentOverride()
        {
            return segmentOverride;
        }

        public Yazmac[] getOffset()
        {
            return offset;
        }

        public ImmediateOperand getDisplacement()
        {
            if (displacementNegate)
                displacement.ikininKomplementi();
            
            return displacement;
        }
        
        public void calistir() throws ParserException
        {
            this.q0();
            
            //Girdi kabul edilirse buraya kadar exception fırlatılmadan gelinmiş demektir.
        }
        
        private void q0() throws ParserException
        {
            this.indisKontrol();
            
            Token t = this.tokenlar.get(iToken);
            switch(t.type)
            {
                case YAZMAC:
                    if (Yazmac.segmentYazmaciMi(t.text))
                    {
                        segmentOverride = Yazmac.valueOf(t.text.toUpperCase(Locale.ROOT));

                        iToken++;
                        this.q1();
                    }
                    else
                    {
                        this.hata(t);
                    }
                    break;
                case KOSELI_PARANTEZ_AC:
                    iToken++;
                    this.q3();
                    break;
                default:
                    this.hata(t);
            }
        }
        
        private void q1() throws ParserException
        {
            this.indisKontrol();
            
            Token t = this.tokenlar.get(iToken);
            switch(t.type)
            {
                case IKI_NOKTA_UST_USTE:
                    iToken++;
                    this.q2();
                    break;
                default:
                    this.hata(t);
            }
        }
        
        private void q2() throws ParserException
        {
            this.indisKontrol();
            
            Token t = this.tokenlar.get(iToken);
            switch(t.type)
            {
                case LITERAL:
                    displacement = ImmediateOperand.tesbit(t.text);
                    iToken++;
                    this.q5();
                    break;
                case KOSELI_PARANTEZ_AC:
                    iToken++;
                    this.q3();
                    break;
                default:
                    this.hata(t);
            }
        }
        
        private void q3() throws ParserException
        {
            this.indisKontrol();
            
            Token t = this.tokenlar.get(iToken);
            switch(t.type)
            {
                case YAZMAC:
                    
                    //Yazmaç offset yazmacı mı = bp, bx, si, di
                    if (!Yazmac.offsetYazmaciMi(t.text))
                        this.hata(t);
                    
                    if (offset == null)
                        this.offset = new Yazmac[2];
                    
                    Yazmac yazmac = Yazmac.valueOf(t.text.toUpperCase(Locale.ROOT));
                    
                    if (this.offset[0] == null)
                    {
                        this.offset[0] = yazmac;
                    }
                    else
                    {
                        //Şimdi de offset'te bp + bx, si + di varsa hata olacağından
                        //bunu kontrol edelim. Bu kontrolü burada yapmamızın amacı
                        //parsing sırasında hangi token'da hata oluştuğunu gösterilebilmemizdendir.
                        switch(this.offset[0])
                        {
                            case BP:
                                if (yazmac == Yazmac.BX)
                                    this.hata(t);
                                break;
                            case BX:
                                if (yazmac == Yazmac.BP)
                                    this.hata(t);
                                break;
                            case SI:
                                if (yazmac == Yazmac.DI)
                                    this.hata(t);
                                break;
                            case DI:
                                if (yazmac == Yazmac.SI)
                                    this.hata(t);
                                break;
                            default:
                                this.hata(t);
                        }
                        
                        this.offset[1] = yazmac;
                    }
                    
                    iToken++;
                    this.q4();
                    break;
                case LITERAL:
                    displacement = ImmediateOperand.tesbit(t.text);
                    iToken++;
                    this.q4();
                    break;
                default:
                    this.hata(t);
            }
        }
        
        private void q4() throws ParserException
        {
            this.indisKontrol();
            
            Token t = this.tokenlar.get(iToken);
            switch(t.type)
            {
                case EKSI:
                    this.displacementNegate = true;
                case ARTI:
                    iToken++;
                    this.q3();
                    break;
                case KOSELI_PARANTEZ_KAPA:
                    iToken++;
                    this.q5();
                    break;
                default:
                    this.hata(t);
            }
        }
        
        //Kabul durumu
        private void q5() throws ParserException
        {
            if (iToken <= this.tokenlar.size() - 1)
                throw new ParserException("Operand hatası!");
        }
        
        private void indisKontrol() throws ParserException
        {
            if (iToken >= this.tokenlar.size())
                throw new ParserException("Operand hatası!");
        }
        
        private void hata(Token token) throws ParserException
        {
            throw new ParserException(String.format("'%s' civarında hata. Satır = %d, Sütun = %d", token.text, token.line, token.col), token.line, token.col);
        }
    }
}

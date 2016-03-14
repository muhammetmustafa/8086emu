package edu.yildiz.emu8086.derleyici;

import edu.yildiz.emu8086.derleyici.asm.komut.Komutlar;
import static edu.yildiz.emu8086.derleyici.TokenTipi.*;
import edu.yildiz.emu8086.tipler.Yazmac;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author MMC
 */
public class Tokenizer
{
    private static final Pattern rgxLiteralHex = Pattern.compile("[0-9][0-9ABCDEFabcdef]+(H|h)");
    private static final Pattern rgxLiteralDec = Pattern.compile("[0-9]+");
    private static final Pattern rgxYorum = Pattern.compile(";.*");
    private static final Pattern rgxIdentifier = Pattern.compile("[a-zA-Z][a-zA-Z0-9_]*");
    
    private static final HashMap<String, TokenTipi> keywords = new HashMap<>();

    static
    {
        for (Komutlar k : Komutlar.values())
            keywords.put(k.toString(), KOMUT);
        Yazmac.parserGuvenliYazmaclar().stream().forEach((y) ->
        {
            keywords.put(y.toString(), YAZMAC);
        });
    }

    private final ArrayList<Token> tokens;
    private String input;
    private int line;
    private int col;

    public Tokenizer(String input) throws ParserException
    {
        this.tokens = new ArrayList<>();
        this.input = input;
        this.line = 1;
        this.col = 1;
        
        this.tokenize();
    }

    public ArrayList<Token> getTokens()
    {
        return tokens;
    }
    
    private void tokenize() throws ParserException
    {
        skipWhitespace();
        
        while (!input.isEmpty())
        {
            boolean ok
                = tryTok(",", VIRGUL)
                || tryTok("\n", SATIR_SONU)
                || tryTok("[", KOSELI_PARANTEZ_AC) 
                || tryTok("]", KOSELI_PARANTEZ_KAPA)
                || tryTok(":", IKI_NOKTA_UST_USTE)
                || tryTok("+", ARTI) 
                || tryTok("-", EKSI)
                
                || tryRegex(rgxLiteralHex, LITERAL)
                || tryRegex(rgxLiteralDec, LITERAL)
                || tryRegex(rgxYorum, YORUM)
                || tryKeywordOrIdentifier()
                ;
            
            if (!ok)
            {
                throw new ParserException(
                    String.format("%d. satir ve %d. sütündaki karakter veya karakter dizesi tanınamadı", line, col)
                , line, col);
            }

            skipWhitespace();
        }
        
        //Çok işimize yarayacak dosya sonu belirtecini tokenların sonuna ekleyelim.
        this.tokens.add(new Token(EOF, "EOF", -1,-1));
    }

    private void skipWhitespace()
    {
        int i = 0;
        while (i < input.length() &&  Character.isWhitespace(input.charAt(i)))
        {
            i++;
        }
        consumeInput(i);
    }

    private boolean tryTok(String expected, TokenTipi ty)
    {
        if (input.startsWith(expected))
        {
            tokens.add(new Token(ty, expected, line, col));
            consumeInput(expected.length());
            return true;
        } 
        else
        {
            return false;
        }
    }

    private boolean tryRegex(Pattern p, TokenTipi ty)
    {
        Matcher m = p.matcher(input);
        if (m.lookingAt())
        {
            tokens.add(new Token(ty, m.group(), line, col));
            consumeInput(m.end());
            return true;
        } 
        else
        {
            return false;
        }
    }

    private boolean tryKeywordOrIdentifier()
    {
        if (tryRegex(rgxIdentifier, IDENTIFIER))
        {
            Token tok = tokens.get(tokens.size() - 1);
            TokenTipi kwType = keywords.get(tok.text.toUpperCase(Locale.ROOT));
            if (kwType != null)
            {
                tok = new Token(kwType, tok.text, tok.line, tok.col);
                tokens.set(tokens.size() - 1, tok);
            }
            return true;
        } 
        else
        {
            return false;
        }
    }

    private void consumeInput(int amount)
    {
        for (int i = 0; i < amount; ++i)
        {
            char c = input.charAt(i);
            if (c == '\n')
            {
                line++;
                col = 1;
            } 
            else if (c == '\r')
            {
                // Ignore
            } 
            else
            {
                col++;
            }
        }
        
        input = input.substring(amount);
    }
}

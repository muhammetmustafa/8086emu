
package edu.yildiz.emu8086.derleyici;

/**
 *
 * @author MMC
 */
public class Token implements Comparable<Token>
{
    public TokenTipi type;
    public final String text;
    public final int line;
    public final int col;
    public final int endCol;
    
    public Token(TokenTipi type, String text, int line, int col) {
        this.type = type;
        this.text = text;
        this.line = line;
        this.col = col;
        this.endCol = col + text.length();
    }
    
    @Override
    public int hashCode() {
        return type.hashCode() + text.hashCode() + line + col;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Token) {
            Token that = (Token)obj;
            return
                    this.type.equals(that.type) &&
                    this.text.equals(that.text) &&
                    this.line == that.line &&
                    this.col == that.col;
        } else {
            return false;
        }
    }

    @Override
    public String toString()
    {
        return "[" + this.type + " , \"" + this.text + "\"]";
    }

    @Override
    public int compareTo(Token o)
    {
        return this.line-o.line == 0 ? this.col - o.col : this.line - o.line;
    }
    
    
}

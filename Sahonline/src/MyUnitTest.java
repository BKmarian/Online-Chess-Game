import org.junit.Test;
import static org.junit.Assert.*;

public class MyUnitTest {

    @Test
    public void testConcatenate() {
    	ChessServer chess = new ChessServer();
    	ChessServer.Game myUnit = chess.new Game();
        String matrix[][] = { 
        		{ "wtura", "wcal", "wnebun", "wrege", "wregina", "wnebun", "wcal", "wtura" },
				{ "wpion", "wpion", "wpion", "wpion", "wpion", "wpion", "wpion", "wpion" },
				{ "0", "0", "0", "0", "0", "0", "0", "0" }, { "0", "0", "0", "0", "0", "0", "0", "0" },
				{ "0", "0", "0", "0", "0", "0", "0", "0" }, { "0", "0", "0", "0", "0", "0", "0", "0" },
				{ "bpion", "bpion", "bpion", "bpion", "bpion", "bpion", "bpion", "bpion" },
				{ "btura", "bcal", "bnebun", "brege", "bregina", "bnebun", "bcal", "btura" } };
        String matrix2[][] = {
        		{ "0", "0", "wrege", "0", "0", "0", "0", "0" }, { "0", "0", "0", "0", "0", "0", "0", "0" },
        		{ "0", "0", "0", "0", "0", "0", "0", "0" }, { "0", "0", "0", "0", "0", "0", "0", "0" },
        		{ "brege", "wregina", "wtura", "0", "0", "0", "0", "0" }, { "0", "0", "0", "0", "0", "0", "0", "0" },
        		{ "0", "0", "0", "0", "0", "0", "0", "0" }, { "0", "0", "0", "0", "0", "0", "0", "0" }};
        
        String matrix3[][] = {
        		{ "0", "0", "wrege", "0", "0", "0", "0", "0" }, { "0", "0", "0", "0", "0", "0", "0", "0" },
        		{ "0", "0", "0", "0", "0", "0", "0", "0" }, { "0", "0", "0", "0", "0", "0", "0", "0" },
        		{ "brege", "0", "wtura", "0", "0", "0", "0", "0" }, { "0", "0", "0", "0", "0", "0", "0", "0" },
        		{ "0", "0", "0", "0", "0", "0", "0", "0" }, { "0", "0", "0", "0", "0", "0", "0", "0" }};
        
        assertEquals("onetwo", "salam");

    }
}
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * Created by vince on 22/04/15.
 */
public class LiqudTest {

    public static void main(String... args) throws Exception{
        ANTLRInputStream afs = new ANTLRInputStream(ClassLoader.getSystemResourceAsStream("test.liquid"));

        LiquidLexer ll = new LiquidLexer(afs);

        CommonTokenStream cts = new CommonTokenStream(ll);

        LiquidParser parser = new LiquidParser(cts);

        ParseTree tree = parser.block();

        System.out.println(tree.toStringTree(parser));
    }


}

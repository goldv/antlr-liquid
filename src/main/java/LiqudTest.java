import com.goldv.context.Context;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import com.goldv.visitor.DefaultLiquidParserListener;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import com.goldv.antlr.*;

/**
 * Created by vince on 22/04/15.
 */
public class LiqudTest {

    public static void main(String... args) throws Exception{
        ANTLRInputStream afs = new ANTLRInputStream(ClassLoader.getSystemResourceAsStream("test.liquid"));

        LiquidLexer ll = new LiquidLexer(afs);

        CommonTokenStream cts = new CommonTokenStream(ll);

        LiquidParser parser = new LiquidParser(cts);

        ParseTree tree = parser.parse();

        List<Integer> items = new LinkedList<>();
        items.add(1);
        items.add(31);
        items.add(32);

        Map<String, Object> scope = new HashMap<>();
        scope.put("item", "blah blah");
        scope.put("array", items);

        DefaultLiquidParserListener dlf = new DefaultLiquidParserListener(new Context(scope));

        System.out.println(dlf.visit(tree) );

        System.out.println(tree.toStringTree(parser));
    }


}

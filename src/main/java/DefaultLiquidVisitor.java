import org.antlr.v4.runtime.misc.NotNull;

/**
 * Created by vince on 24/04/15.
 */
public class DefaultLiquidVisitor extends LiquidParserBaseVisitor<String>{

    public String visitText(@NotNull LiquidParser.TextContext ctx) {
        return ctx.getText();
    }

    protected String aggregateResult(String aggregate, String nextResult) {
        if(aggregate != null && nextResult != null)
            return aggregate + nextResult;
        if(aggregate != null)
            return aggregate;
        if(nextResult != null)
            return nextResult;
        else
            return null;
    }


}

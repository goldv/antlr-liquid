import org.antlr.v4.runtime.misc.NotNull;

import java.util.List;
import java.util.Map;

/**
 * Created by vince on 26/04/15.
 */
public class DefaultLiquidParserListener extends LiquidParserBaseVisitor<String>{

    private final Map<String, Object> scope;

    public DefaultLiquidParserListener(Map<String, Object> scope){
        this.scope = scope;
    }

    public String visitText(@NotNull LiquidParser.TextContext ctx) {
        return ctx.getText();
    }

    public String visitOutput(@NotNull LiquidParser.OutputContext ctx) {
        // evaluate the expression
        String result = visit(ctx.expr());
        return result;
    }



    public String visitLookup(@NotNull LiquidParser.LookupContext ctx) {
        if(scope.get(ctx.getText()) == null){
            return "";
        } else {
            return scope.get(ctx.getText()).toString();
        }
    }

    public String visitFor_array(@NotNull LiquidParser.For_arrayContext ctx) {
        Object col = scope.get( ctx.lookup().getText() );

        String childScopeId = ctx.Id().getText();

        Object currentChildScope = scope.get(childScopeId);

        if(col != null){
            if(col instanceof List){
                List<Object> list = (List<Object>)col;

                StringBuilder sb = new StringBuilder();

                for( Object child : list){
                    scope.put(childScopeId, child);
                    sb.append( visit(ctx.for_block()) );

                }

                // reset the origin child scope.
                scope.put(childScopeId, currentChildScope);

                return sb.toString();
            }
        }

        return "";
    }

    public String visitIf_tag(@NotNull LiquidParser.If_tagContext ctx) {
        ExpressionVisitor ev = new ExpressionVisitor(scope);
        if( ev.visitExpr(ctx.expr()) ){
            return visit( ctx.block() );
        } else {
            return "";
        }
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


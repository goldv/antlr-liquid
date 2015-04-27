package com.goldv.visitor;

import com.goldv.antlr.LiquidParser;
import com.goldv.antlr.LiquidParserBaseVisitor;
import com.goldv.context.ChildContext;
import com.goldv.context.Context;
import org.antlr.v4.runtime.misc.NotNull;
import java.util.Collection;

/**
 * Created by vince on 26/04/15.
 */
public class DefaultLiquidParserListener extends LiquidParserBaseVisitor<String> {

    private final Context context;

    public DefaultLiquidParserListener(Context scope){
        this.context = scope;
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
        String value = context.getAsString(ctx.getText());

        if(value == null) return "";
        else return value;
    }

    public String visitFor_array(@NotNull LiquidParser.For_arrayContext ctx) {
        Collection<Object> col = context.getAsCollection(ctx.lookup().getText());

        if(col != null){
            String childContextId = ctx.Id().getText();
            StringBuilder sb = new StringBuilder();

            for( Object child : col){
                ChildContext cs = context.createChild();
                cs.add(childContextId, child);
                sb.append(visit(ctx.for_block()));
                cs.delete();
            }
            return sb.toString();
        }
        return "";
    }

    public String visitIf_tag(@NotNull LiquidParser.If_tagContext ctx) {
        ExpressionVisitor ev = new ExpressionVisitor(context);
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


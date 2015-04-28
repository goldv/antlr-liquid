package com.goldv.visitor;

import com.goldv.antlr.LiquidParser;
import com.goldv.antlr.LiquidParserBaseVisitor;
import com.goldv.context.ChildContext;
import com.goldv.context.Context;
import org.antlr.v4.runtime.misc.NotNull;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;

/**
 * Created by vince on 26/04/15.
 */
public class TemplateRenderer extends LiquidParserBaseVisitor<Writer> {

    private final Context context;
    private final Writer writer;

    public TemplateRenderer(Context scope, Writer writer){
        this.context = scope;
        this.writer = writer;
    }

    private Writer write(CharSequence str){
        try {
            return writer.append(str);
        } catch(IOException e){
            throw new RuntimeException("Unable to render", e);
        }
    }

    public Writer visitText(@NotNull LiquidParser.TextContext ctx) {
        return write(ctx.getText());
    }

    public Writer visitOutput(@NotNull LiquidParser.OutputContext ctx) {
        // evaluate the expression
        return visit(ctx.expr());
    }

    public Writer visitLookup(@NotNull LiquidParser.LookupContext ctx) {
        String value = context.getAsString(ctx.getText());

        if(value == null) return write("");
        else return write(value);
    }

    public Writer visitFor_array(@NotNull LiquidParser.For_arrayContext ctx) {
        Collection<Object> col = context.getAsCollection(ctx.lookup().getText());

        if(col != null){
            String childContextId = ctx.Id().getText();

            for( Object child : col){
                ChildContext cs = context.createChild();
                cs.add(childContextId, child);
                visit(ctx.for_block());
                cs.delete();
            }
        }
        return writer;
    }

    public Writer visitIf_tag(@NotNull LiquidParser.If_tagContext ctx) {
        BooleanExpressionEvaluator ev = new BooleanExpressionEvaluator(context);
        if( ev.visitExpr(ctx.expr()) ){
            return visit( ctx.block() );
        } else {
            return write("");
        }
    }

    protected Writer aggregateResult(Writer aggregate, Writer nextResult) {
        return writer;
    }
}


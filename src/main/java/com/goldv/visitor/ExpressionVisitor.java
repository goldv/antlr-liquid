package com.goldv.visitor;

import com.goldv.context.Context;
import org.antlr.v4.runtime.misc.NotNull;
import com.goldv.antlr.*;

/**
 * Created by vince on 26/04/15.
 */
public class ExpressionVisitor extends LiquidParserBaseVisitor<Boolean> {

    enum Operation{EQ, NEQ, GTE, GT, LTE, LT}

    private final Context context;

    public ExpressionVisitor(Context context){
        this.context = context;
    }

    public Boolean visitRel_expr(@NotNull LiquidParser.Rel_exprContext ctx) {

        LiquidParser.TermContext firstTerm = ctx.term(0);
        if(firstTerm.True() != null) return true;
        else if(firstTerm.False() != null) return false;
        else{
            LiquidParser.TermContext secondTerm = ctx.term(1);

            Double term2 =doubleFromTerm(secondTerm);
            Double term1 = doubleFromTerm(firstTerm);

            return performRelOperation(ctx, term1, term2);

        }
    }

    public Boolean visitAnd_expr(@NotNull LiquidParser.And_exprContext ctx) {
        for(LiquidParser.Rel_exprContext expr : ctx.rel_expr() ){
            if(!visit(expr)) return false;
        }
        return true;
    }

    public Boolean visitOr_expr(@NotNull LiquidParser.Or_exprContext ctx) {
        for( LiquidParser.And_exprContext expr : ctx.and_expr() ){
            if(visit(expr)) return true;
        }

        return false;
    }

    public Boolean visitExpr(@NotNull LiquidParser.ExprContext ctx) {
        return visit(ctx.or_expr());
    }

    private Boolean performRelOperation(LiquidParser.Rel_exprContext ctx, Double term1, Double term2){
        switch(deriveOperationFromExpr(ctx)){
            case GT: return term1 > term2;
            case GTE: return term1 >= term2;
            case LT: return term1 < term2;
            case LTE: return term1 <= term2;
            case EQ: return term1 == term2;
            case NEQ: return term1 != term2;
            default: throw new RuntimeException("Unknown operation type " + deriveOperationFromExpr(ctx));
        }
    }

    private Double doubleFromTerm(LiquidParser.TermContext term){
        if(term.DoubleNum() != null){
            return Double.parseDouble(term.DoubleNum().getText());
        } else if( term.LongNum() != null){
            return Double.parseDouble(term.LongNum().getText());
        } else if( term.lookup() != null){
            String lookup = term.lookup().getText();
            return context.getAsNumber(lookup);
        }

        return Double.NaN;
    }

    private Operation deriveOperationFromExpr(LiquidParser.Rel_exprContext ctx){
        if(ctx.Gt() != null) return Operation.GT;
        else if( ctx.GtEq() != null) return Operation.GTE;
        else if( ctx.Lt() != null) return Operation.LT;
        else if(ctx.LtEq() != null) return Operation.LTE;
        else throw new RuntimeException("Unknown operation type for expression");
    }

//    protected Boolean aggregateResult(Boolean aggregate, Boolean nextResult) {
//        System.out.println(aggregate + " " + nextResult);
//
//        return false;
//    }
}

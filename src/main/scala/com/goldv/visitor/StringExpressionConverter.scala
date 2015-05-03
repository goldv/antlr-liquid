package com.goldv.visitor

import com.goldv.antlr.{LiquidParser, LiquidParserBaseVisitor}
import scala.collection.JavaConversions._

/**
 * Created by vince on 03/05/15.
 */
class StringExpressionConverter extends LiquidParserBaseVisitor[String]{

  import Operation._

  override def visitRel_expr(ctx: LiquidParser.Rel_exprContext) = {
    val firstTerm: LiquidParser.TermContext = ctx.term(0)
    if (firstTerm.True != null) "true"
    else if (firstTerm.False != null) "false"
    else {
      val secondTerm: LiquidParser.TermContext = ctx.term(1)
      val term2 = termString(secondTerm)
      val term1 = termString(firstTerm)
      performRelOperation(ctx, term1, term2)
    }
  }


  override def visitAnd_expr(ctx: LiquidParser.And_exprContext) = ctx.rel_expr.map(visit).mkString(" && ")
  override def visitOr_expr(ctx: LiquidParser.Or_exprContext) = ctx.and_expr().map(visit).mkString(" || ")
  override def visitExpr(ctx: LiquidParser.ExprContext) = visit(ctx.or_expr)

  def performRelOperation(ctx: LiquidParser.Rel_exprContext, term1: String, term2: String) = {
    deriveOperationFromExpr(ctx) match {
      case GT => s"$term1 > $term2"
      case GTE => s"$term1 >= $term2"
      case LT => s"$term1 < $term2"
      case LTE => s"$term1 <= $term2"
      case EQ => s"$term1 == $term2"
      case NEQ => s"$term1 != $term2"
      case _ => throw new RuntimeException("Unknown operation type " + deriveOperationFromExpr(ctx))
    }
  }


  def termString(term: LiquidParser.TermContext) = {
    if (term.DoubleNum != null) {
      term.DoubleNum.getText
    }
    else if (term.LongNum != null) {
      term.LongNum.getText
    }
    else if (term.lookup != null) {
      term.lookup.getText
    } else ""

  }


  def deriveOperationFromExpr(ctx: LiquidParser.Rel_exprContext) = {
    if (ctx.Gt != null) Operation.GT
    else if (ctx.GtEq != null) Operation.GTE
    else if (ctx.Lt != null) Operation.LT
    else if (ctx.LtEq != null) Operation.LTE
    else throw new RuntimeException("Unknown operation type for expression")
  }
}

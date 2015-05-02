package com.goldv.visitor

import com.goldv.antlr.{LiquidParser, LiquidParserBaseVisitor}
import com.goldv.context.Context
import scala.collection.JavaConversions._

/**
 * Created by vince on 02/05/15.
 */

object Operation extends Enumeration {
  type Operation = Value
  val EQ, NEQ, GTE, GT, LTE, LT = Value
}

class BooleanExpressionEvaluator(context: Context) extends LiquidParserBaseVisitor[Boolean]{

  import Operation._

  override def visitRel_expr(ctx: LiquidParser.Rel_exprContext) = {
    val firstTerm: LiquidParser.TermContext = ctx.term(0)
    if (firstTerm.True != null) true
    else if (firstTerm.False != null) false
    else {
      val secondTerm: LiquidParser.TermContext = ctx.term(1)
      val term2 = doubleFromTerm(secondTerm)
      val term1 = doubleFromTerm(firstTerm)
      performRelOperation(ctx, term1, term2)
    }
  }

  override def visitAnd_expr(ctx: LiquidParser.And_exprContext) = ctx.rel_expr.exists(e => !visit(e))
  override def visitOr_expr(ctx: LiquidParser.Or_exprContext) = ctx.and_expr().exists(visit)
  override def visitExpr(ctx: LiquidParser.ExprContext) = visit(ctx.or_expr)

  def performRelOperation(ctx: LiquidParser.Rel_exprContext, term1: Double, term2: Double) = {
    deriveOperationFromExpr(ctx) match {
      case GT => term1 > term2
      case GTE => term1 >= term2
      case LT => term1 < term2
      case LTE => term1 <= term2
      case EQ => term1 == term2
      case NEQ => term1 != term2
      case _ => throw new RuntimeException("Unknown operation type " + deriveOperationFromExpr(ctx))
    }
  }

  def doubleFromTerm(term: LiquidParser.TermContext) = {
    if (term.DoubleNum != null) {
      term.DoubleNum.getText.toDouble
    }
    else if (term.LongNum != null) {
      term.LongNum.getText.toDouble
    }
    else if (term.lookup != null) {
      val lookup: String = term.lookup.getText
      context.getDouble(lookup).getOrElse(Double.NaN)
    }

    Double.NaN
  }

  def deriveOperationFromExpr(ctx: LiquidParser.Rel_exprContext) = {
    if (ctx.Gt != null) Operation.GT
    else if (ctx.GtEq != null) Operation.GTE
    else if (ctx.Lt != null) Operation.LT
    else if (ctx.LtEq != null) Operation.LTE
    else throw new RuntimeException("Unknown operation type for expression")
  }

}

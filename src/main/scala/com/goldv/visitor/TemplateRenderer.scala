package com.goldv.visitor

import java.io.Writer

import com.goldv.antlr.{LiquidParser, LiquidParserBaseVisitor}
import com.goldv.context.Context
import com.goldv.template.{TemplateLoader, Template }

/**
 * Created by vince on 01/05/15.
 */
class TemplateRenderer(context: Context, loader: TemplateLoader, writer: Writer) extends LiquidParserBaseVisitor[Writer]{

  override def visitText(ctx: LiquidParser.TextContext) = writer.append(ctx.getText())

  override def visitOutput(ctx: LiquidParser.OutputContext) = visit(ctx.lookup())

  override def visitLookup(ctx: LiquidParser.LookupContext) = {
    context.getString(ctx.getText()).foreach( t => writer.append(t))
    writer
  }

  override def visitInclude_tag(ctx: LiquidParser.Include_tagContext) = {
    loader.forName(ctx.Str(0).getText.replace("\"", "")).foreach( t => t.render(this))
    writer
  }

  override def visitFor_array(ctx: LiquidParser.For_arrayContext) = {
    context.getList(ctx.lookup.getText).foreach{ col =>
      val childContextId = ctx.Id.getText

      for (child <- col) {
        val cs = context.createChild
        cs.put(childContextId, child)
        visit(ctx.for_block)
        context.deleteChild
      }
    }

    writer
  }


  override def visitIf_tag(ctx: LiquidParser.If_tagContext) = {
    val ev = new BooleanExpressionEvaluator(context)
    if (ev.visitExpr(ctx.expr)) {
      visit(ctx.block)
    }
    else {
      writer
    }
  }

}

package com.goldv.visitor

import java.io.Writer

import com.goldv.antlr.{LiquidParser, LiquidParserBaseVisitor}
import com.goldv.context.Context
import com.goldv.template.TemplateLoader

/**
 * Created by vince on 02/05/15.
 */
class AjaxRenderer(context: Context, loader: TemplateLoader, writer: Writer) extends LiquidParserBaseVisitor[Writer]{

  override def visitText(ctx: LiquidParser.TextContext) = writer.append(ctx.getText())

  override def visitOutput(ctx: LiquidParser.OutputContext) = writer.append(txt.outputTag(ctx.lookup().getText()).toString())

  override def visitInclude_tag(ctx: LiquidParser.Include_tagContext) = {
    loader.forName(ctx.Str(0).getText.replace("\"", "")).foreach( t => t.render(this))
    writer
  }

  override def visitIf_tag(ctx: LiquidParser.If_tagContext) = {
    val ev = new StringExpressionConverter
    writer.append(txt.ifTagOpen(ev.visitExpr(ctx.expr)).toString())
    visit(ctx.block)
    writer.append(txt.ifTagClose().toString())
  }

  override def visitFor_array(ctx: LiquidParser.For_arrayContext) = {
    writer.append( txt.forTagOpen(ctx.Id.getText, ctx.lookup.getText).toString() )
    visit(ctx.for_block)
    writer.append( txt.forTagClose().toString() )
  }


}

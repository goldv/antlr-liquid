package com.goldv.template

import java.io.{Writer, InputStream}

import com.goldv.antlr.{LiquidParser, LiquidLexer}
import com.goldv.context.Context
import com.goldv.visitor.TemplateRenderer
import org.antlr.v4.runtime.tree.ParseTree
import org.antlr.v4.runtime.{CommonTokenStream, ANTLRInputStream}

/**
 * Created by vince on 02/05/15.
 */
class Template(input: InputStream, loader: TemplateLoader) {

  val tree: ParseTree  = parse(input)

  def render(context: Context, writer: Writer) = {
    val dlf = new TemplateRenderer(context, loader, writer);
    dlf.visit(tree);
  }

  def parse(input: InputStream) = {
    val afs = new ANTLRInputStream(input)
    val ll = new LiquidLexer(afs)
    val cts = new CommonTokenStream(ll)
    val parser = new LiquidParser(cts)

    parser.parse()
  }

}

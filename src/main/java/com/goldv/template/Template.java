package com.goldv.template;

import com.goldv.antlr.LiquidLexer;
import com.goldv.antlr.LiquidParser;
import com.goldv.context.Context;
import com.goldv.visitor.TemplateRenderer;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.Map;

/**
 * Created by goldv on 28/04/2015.
 */
public class Template {

  private final ParseTree tree;
  private final TemplateLoader loader;

  public Template(InputStream input, TemplateLoader loader) throws IOException {
    this.tree = parse(input);
    this.loader = loader;
  }

  public void render(Map<String, Object> context, Writer writer){
    TemplateRenderer dlf = new TemplateRenderer(new Context(context), writer, loader);
    dlf.visit(tree);
  }

  private ParseTree parse(InputStream input) throws IOException {
    ANTLRInputStream afs = new ANTLRInputStream(input);
    LiquidLexer ll = new LiquidLexer(afs);
    CommonTokenStream cts = new CommonTokenStream(ll);
    LiquidParser parser = new LiquidParser(cts);
    return parser.parse();
  }
}

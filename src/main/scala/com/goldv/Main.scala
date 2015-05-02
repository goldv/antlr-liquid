package com.goldv

import java.io.StringWriter

import com.goldv.context.Context
import com.goldv.io.DefaultTemplateLoader

/**
 * Created by vince on 02/05/15.
 */
object Main extends App{
  val items = 2 :: 31 :: 32 :: Nil

  val scope= Map("item" -> "Item List", "array" -> items)
  val sw = new StringWriter
  val loader = new DefaultTemplateLoader("src/main/resources")
  loader.intitialize

  loader.forName("test").foreach{ t =>

    println("rendering template")
    t.render(new Context(scope), sw)
  }

  //loader.forName("partial/header").render(scope,sw);

  println(sw.toString)

}

package com.goldv.io

import java.io.{Writer, File, FileInputStream}


import com.goldv.template.{Template, TemplateLoader}
import org.apache.commons.io.FileUtils
import org.apache.commons.io.filefilter.{SuffixFileFilter, FileFilterUtils}

import scala.collection.JavaConversions._

/**
 * Created by vince on 02/05/15.
 */
class DefaultTemplateLoader(directoryName: String) extends TemplateLoader{

  val baseDir = new File(directoryName)
  if (!baseDir.isDirectory) throw new IllegalArgumentException(directoryName + " is not a directory")

  var cache: Map[String, Template] = Map.empty[String, Template]


  def forName(name: String): Option[Template] = cache.get(name)

  def intitialize = {
    val templateFiles = FileUtils.listFiles(baseDir, new SuffixFileFilter(".liquid"), FileFilterUtils.trueFileFilter)

    for (templateFile <- templateFiles) {
      val templateName = stripSuffix(baseDir, templateFile)
      val fi= new FileInputStream(templateFile)
      val tmpl = new Template(fi, this)

      cache = cache.updated(templateName, tmpl)
    }
  }

  def stripSuffix(baseDir: File, file: File) = {
    val name = file.getPath
    val dotIdx = name.lastIndexOf(".")
    name.substring(0, dotIdx).substring(baseDir.getPath.length + 1).replace("\\", "/")
  }

}

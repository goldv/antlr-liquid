package com.goldv.io;

import com.goldv.template.Template;
import com.goldv.template.TemplateLoader;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by goldv on 29/04/2015.
 */
public class DirectoryBasedTemplateCache implements TemplateLoader {

  private final File baseDir;
  private final Map<String, Template> cache = new HashMap<>();

  public DirectoryBasedTemplateCache(String directoryName){
    this.baseDir = new File(directoryName);
    if(!baseDir.isDirectory()) throw new IllegalArgumentException(directoryName + " is not a directory");
  }

  public void intitialize() throws IOException {
    Collection<File> templateFiles = FileUtils.listFiles(baseDir, FileFilterUtils.trueFileFilter(), FileFilterUtils.trueFileFilter());
    for(File templateFile : templateFiles){
      String templateName = stripSuffix(baseDir, templateFile);
      FileInputStream fi = new FileInputStream(templateFile);

      Template tmpl = new Template(fi, this);
      cache.put(templateName, tmpl);
    }
  }

  private static String stripSuffix(File baseDir, File file) throws IOException{
    String name = file.getPath();
    int dotIdx = name.lastIndexOf(".");
    return name.substring(0, dotIdx).substring(baseDir.getPath().length() + 1).replace("\\","/");
  }

  @Override
  public Template forName(String name) {
    return cache.get(name);
  }
}

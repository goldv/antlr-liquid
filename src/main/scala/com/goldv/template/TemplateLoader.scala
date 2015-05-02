package com.goldv.template

/**
 * Created by vince on 02/05/15.
 */
trait TemplateLoader {

  def forName(name: String): Option[Template]
}

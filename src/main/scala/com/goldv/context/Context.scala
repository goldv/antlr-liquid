package com.goldv.context



/**
 * Created by vince on 01/05/15.
 */
class Context(var context: Map[String, Any] = Map.empty[String, Any]) {

  var childContexts: List[Context] = Nil

  def createChild = {
    val ctx = Context()
    childContexts = ctx :: childContexts
    ctx
  }

  def deleteChild = {
    childContexts = childContexts.dropRight(1)
  }

  def put(field: String, value: Any) = context = context.updated(field, value)

  def getDouble(field: String) = getValue(field).flatMap{
    case n : Int => Some( n.doubleValue() )
    case n : Double => Some( n.doubleValue() )
    case n : Long => Some( n.doubleValue() )
    case n : Short => Some( n.doubleValue() )
    case n : Byte => Some( n.doubleValue() )
    case _ => None
  }

  def getBoolean(field: String) = getValue(field).flatMap{
    case b: Boolean => Some(b)
    case _ => None
  }

  def getString(field: String) = getValue(field).map(_.toString)

  def getList(field: String) = getValue(field).flatMap{
    case l: Seq[_] => Some(l.toList)
    case _ => None
  }

  def getValue(field: String): Option[Any] = getValueFromChildContext(field) match{
    case Some(childValue) => Some(childValue)
    case None => getValueFromContext(field, context)
  }

  def getValueFromChildContext(field: String): Option[Any] = childContexts.find(ctx => ctx.getValueFromContext(field, ctx.context).isDefined )
    .flatMap( ctx => ctx.getValueFromContext(field, ctx.context))

  def getValueFromContext(field: String, searchScope: Map[String, Any]): Option[Any] = {

    val delimIdx = field.indexOf(".")
    if(delimIdx < 0){
      searchScope.get(field)
    } else {
      val nextField = field.substring(0, delimIdx);

      searchScope.get(nextField).flatMap{
        case m:Map[_,_] => getValueFromContext(field.substring(delimIdx + 1), m.asInstanceOf[Map[String, Any]])
        case _ => None
      }
    }
  }

}

object Context{
  def apply() = new Context()
}

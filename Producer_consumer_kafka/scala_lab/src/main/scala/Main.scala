import java.util.{Calendar, Date}
import scala.io.Source

object Main extends App {

  val r = scala.util.Random
  var filename = "violation_list.csv"
  var data = Source.fromFile(filename).getLines.toList


  def latitude(): String ={
    (40.493.toFloat+r.nextFloat()%0.430.toFloat).toString()
  }
  def longitude(): String ={
    "-"+(73.685.toFloat+r.nextFloat()%0.59.toFloat).toString()
  }
  def time(): String ={
    Calendar.getInstance().getTime().toString()
  }
  def id(): String ={
    r.nextInt(1000).toString()
  }
  def battery(): String ={
    r.nextFloat().toString()
  }
  def plate(): String ={
    r.alphanumeric.filter(_.isLetter).take(3).mkString("").toUpperCase()+r.alphanumeric.filter(_.isDigit).take(4).mkString("")
  }
  def violation_code(): String ={
    read_csv_line(r.nextInt(100),data)
  }
  def regular_message(): String ={
    latitude+","+longitude+","+time+","+id+","+battery+"\n"
  }
  def violation_message(): String ={
    latitude+","+longitude+","+time+","+id+","+battery+","+violation_code+"\n"
  }
  def read_csv_line[A](n: Int, ls: List[A]): A = (n, ls) match {
    case (0, h :: _   ) => h
    case (n, _ :: tail) => read_csv_line(n - 1, tail)
    case (_, Nil      ) => throw new NoSuchElementException
  }

  println(regular_message)
  println(violation_message)
}
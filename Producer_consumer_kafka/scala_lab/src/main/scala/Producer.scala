object Producer extends App {

  import org.apache.log4j.BasicConfigurator
  BasicConfigurator.configure()
  import java.util.Properties
  import org.apache.kafka.clients.producer._

  import java.util.{Calendar, Date}
  import scala.io.Source


  val  props = new Properties()
  props.put("bootstrap.servers", "localhost:9092")
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

  val producer = new KafkaProducer[String, String](props)
  val TOPIC="rtran"

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

  def select_record(x: Int, prob: Int):Unit = x match {
    case x if x < prob => send_violation
    case x if x > prob-1 => send_regular
  }

  def send_violation(): Unit = {
    val record = new ProducerRecord(TOPIC, "key", violation_message)
    producer.send(record)
  }

  def send_regular(): Unit = {
    val record = new ProducerRecord(TOPIC, "key", regular_message)
    producer.send(record)
  }

  def send_messages(): Unit = {
    Thread.sleep(r.nextInt(3000))
    select_record(r.nextInt(100), 20)
    send_messages
  }

  send_messages

  producer.close()
}
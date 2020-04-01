object Main extends App {

  import java.util.{Calendar, Date}
  import scala.io.Source

  import org.apache.log4j.BasicConfigurator
  BasicConfigurator.configure()
  import java.util.Properties
  import org.apache.kafka.clients.producer._

  val  props = new Properties()
  props.put("bootstrap.servers", "localhost:9092")
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

  val producer = new KafkaProducer[String, String](props)
  val TOPIC=args(0)

  val r = scala.util.Random
  var filename = "csv_files/"+args(1)
  var data = Source.fromFile(filename).getLines

  def read_csv_line(d: String): Unit = {
    for (line <- data) {
      val record = new ProducerRecord(TOPIC, "key", line)
      producer.send(record)
    }
  }

  read_csv_line(data.next())
  //println(args(0))
  //println(args(1))
}
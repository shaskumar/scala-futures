import com.github.tototoshi.csv._
import java.io._

object CsvReader extends App {

  def readCsv() = {
    val readerFile = CSVReader.open(new File("sample.csv"))
    println(readerFile.allWithHeaders())
    val content = """eins,zwei,drei
                    |11,12,13
                    |21,22,23
                    |""".stripMargin

    val readerStringReader = CSVReader.open(new StringReader(content))
    println(readerStringReader.allWithHeaders())
  }
  readCsv()
}

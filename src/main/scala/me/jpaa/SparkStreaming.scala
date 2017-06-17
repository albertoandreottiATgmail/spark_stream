package me.jpaa;


import org.apache.spark.sql.streaming.ProcessingTime
import org.apache.spark.sql.{SQLContext, SparkSession}
import java.io.File

/**
 * Created by jose on 21/10/16.
 */


object SparkStreaming {

  def main(args: Array[String]) {

      val spark = SparkSession.builder().
        appName("spark_streaming").
        master("local[2]").
        getOrCreate()

      import spark.implicits._

      val fullPath = this.getClass.getResource("/people1.json").getPath
      val resourcesPath = (new File(fullPath)).getParent

      // read the first file to get the schema
      val firstFile = spark.read.json(s"$fullPath")

      val rawRecords = spark.readStream
        .schema(firstFile.schema)
        .json(s"$resourcesPath/*.json")

      val ageEvents = rawRecords
        .select($"name",  - $"age" + 2017 as "birthyear")

      val streamingQuery = ageEvents
        .writeStream
        // check for files every 2s
        .trigger(ProcessingTime("2 seconds"))
        // write in the console
        .format("console")
        .option("path", s"$resourcesPath/birthyears")
        .start()

      // Wait 2 minutes
      streamingQuery.awaitTermination(120000)
  }
}


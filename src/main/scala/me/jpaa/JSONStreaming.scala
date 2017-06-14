package me.jpaa;


import org.apache.spark.sql.streaming.ProcessingTime
import org.apache.spark.sql.{SQLContext, SparkSession}

/**
 * Created by jose on 21/10/16.
 */


object JSONStreaming {

  def main(args: Array[String]) {

      val spark = SparkSession.builder().
        appName("spark_streaming").
        master("local[2]").
        getOrCreate()

      import spark.implicits._

      val filename = "/home/jose/spark_stream"

      val firstFile = spark.read.json(s"$filename/*.json")

      val rawRecords = spark.readStream
        .schema(firstFile.schema)
        .json(s"$filename/*.json")

      val ageEvents = rawRecords
        .select($"name",  - $"age" + 2017 as "birthyear")

      val streamingETLQuery = ageEvents
        .writeStream
        // check for files every 2s
        .trigger(ProcessingTime("2 seconds"))
        .format("console") // write as JSON
        .option("path", s"$filename/birthyears")
        .start()

      // Wait 1 minute
      streamingETLQuery.awaitTermination(60000)


  }
}


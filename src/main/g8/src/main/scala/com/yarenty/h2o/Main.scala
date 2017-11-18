package com.yarenty.h2o

import org.apache.spark.SparkContext
import org.apache.spark.h2o.H2OContext
import org.apache.spark.sql.SparkSession
import water.support.{SparkContextSupport, SparklingWaterApp}
import water.util.Log

/**
  * Created by yarenty on 15/06/2017.
  */
abstract class Example {
  def flow()
}

object Main extends SparklingWaterApp with SparkContextSupport {

  // Prepare environment
  implicit val sc = new SparkContext(configure("H2O Sparkling Water App"))
  // SQL support
  implicit val sqlContext = SparkSession.builder().getOrCreate().sqlContext
  // Start H2O services
  implicit val h2oContext = H2OContext.getOrCreate(sc)


  def main(args: Array[String]): Unit = {

    println("Hello World!")
    Log.info("Hello World - using H2O logger")

    //    h2oContext.openFlow()

    ProstateKMeansExample.flow()

    //get data from mercedes kaggle competition
    // MercedesDeepLearningExample.flow()

    // Shutdown Spark cluster and H2O
    //    shutdown()
  }

}
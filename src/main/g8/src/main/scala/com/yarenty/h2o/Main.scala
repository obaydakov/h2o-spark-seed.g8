package com.yarenty.h2o

import hex.kmeans.{KMeans, KMeansModel}
import hex.kmeans.KMeansModel.KMeansParameters
import org.apache.spark.SparkContext
import org.apache.spark.h2o.H2OContext
import org.apache.spark.sql.{SQLContext, SparkSession}
import water.AutoBuffer
import water.fvec.H2OFrame
import water.support.{H2OFrameSupport, SparkContextSupport, SparklingWaterApp}
import water.util.Log

/**
  * Created by yarenty on 15/06/2017.
  */
object Main extends SparklingWaterApp with SparkContextSupport {

  // Prepare environment
  implicit val sc = new SparkContext(configure("H2O Sparkling Water App"))
  // SQL support
  implicit val sqlContext = SparkSession.builder().getOrCreate().sqlContext
  // Start H2O services
  implicit val h2oContext = H2OContext.getOrCreate(sc)


  def flow(): KMeansModel = {

    // Add a file to be available for cluster mode
    addFiles(sc, absPath("src/main/resources/prostate.csv"))

    // Run H2O cluster inside Spark cluster
    import h2oContext.implicits._

    // We do not need to wait for H2O cloud since it will be launched by backend

    // Load raw data
    val parse = ProstateParse
    val rawdata = sc.textFile(enforceLocalSparkFile("prostate.csv"), 2)
    // Parse data into plain RDD[Prostate]
    val table = rawdata.map(_.split(",")).map(line => parse(line))

    // Convert to SQL type RDD
    //    val sqlContext = SparkSession.builder().getOrCreate().sqlContext
    import sqlContext.implicits._ // import implicit conversions
    table.toDF.createOrReplaceTempView("prostate_table")

    // Invoke query on data; select a subsample
    val query = "SELECT * FROM prostate_table WHERE CAPSULE=1"
    val result = sqlContext.sql(query) // Using a registered context and tables

    // Build a KMeans model, setting model parameters via a Properties
    val model = runKmeans(result)
    println(model)
    model
  }

  private def runKmeans[T](trainDataFrame: H2OFrame): KMeansModel = {
    val params = new KMeansParameters
    params._train = trainDataFrame._key
    params._k = 3
    // Create a builder
    val job = new KMeans(params)
    // Launch a job and wait for the end.
    val kmm = job.trainModel.get
    // Print the JSON model
    println(new String(kmm._output.writeJSON(new AutoBuffer()).buf()))
    // Return a model
    
    kmm
  }


  /** Prostate schema definition. */
  case class Prostate(ID: Option[Long],
                      CAPSULE: Option[Int],
                      AGE: Option[Int],
                      RACE: Option[Int],
                      DPROS: Option[Int],
                      DCAPS: Option[Int],
                      PSA: Option[Float],
                      VOL: Option[Float],
                      GLEASON: Option[Int]) {
    def isWrongRow(): Boolean = (0 until productArity).map(idx => productElement(idx)).forall(e => e == None)
  }

  /** A dummy csv parser for prostate dataset. */
  object ProstateParse extends Serializable {
    val EMPTY = Prostate(None, None, None, None, None, None, None, None, None)

    def apply(row: Array[String]): Prostate = {
      import water.support.ParseSupport._
      if (row.length < 9) EMPTY
      else Prostate(long(row(0)), int(row(1)), int(row(2)), int(row(3)), int(row(4)), int(row(5)), float(row(6)), float(row(7)), int(row(8)))
    }
  }


  def main(args: Array[String]): Unit = {

    println("Hello World!")
    Log.info("Hello World - using H2O logger")

    h2oContext.openFlow()

    val model = flow()
    
    // Shutdown Spark cluster and H2O
//    shutdown()
  }

}
package com.yarenty.h2o

import java.net.URI

import com.yarenty.h2o.Main.h2oContext
import hex.deeplearning.DeepLearningModel.DeepLearningParameters
import hex.deeplearning.{DeepLearning, DeepLearningModel}
import org.apache.spark.h2o.H2OContext
import water.fvec.H2OFrame
import water.support.H2OFrameSupport

/**
  * Created by yarenty on 20/06/17.
  */
object MercedesDeepLearningExample extends Example {


  def flow(): Unit = {

    val datadir = "/opt/data/mercedes"
    val trainFile = datadir + "/train.csv"
    val testFile = datadir + "/test.csv"
    // Run H2O cluster inside Spark cluster

    // We do not need to wait for H2O cloud since it will be launched by backend
    val input = new H2OFrame(getSimpleCSVParser, new URI(trainFile))
    val test = new H2OFrame(getSimpleCSVParser, new URI(testFile))

    val processedNames = input.names.drop(2) // in this data only first 2 columns are not categorical
    println(processedNames mkString ",")

    //to make sure that they are enums (difefrent versions of default parsers in different releases h2o behave... differently ;-)
    input.colToEnum(processedNames)
    test.colToEnum(processedNames)


    val (train, valid) = split(input) // this is split 0.9/0.1

    val model = dlModel(train, valid)
    println(model)

    val prediction = model.score(test)

    saveCSV(prediction, datadir + "/out.csv")
  }


  private def split(in: H2OFrame)(implicit h2oContext: H2OContext): (H2OFrame, H2OFrame) = {
    import h2oContext.implicits._

    val keys = Array[String]("train.hex", "test.hex")
    val ratios = Array[Double](0.9, 0.1)

    val frs = H2OFrameSupport.splitFrame(in, keys, ratios)
    (frs(0), frs(1))
  }


  private def dlModel(train: H2OFrame, valid: H2OFrame): DeepLearningModel = {
    val params = new DeepLearningParameters()
    params._train = train.key
    params._valid = valid.key
    params._response_column = "y"
    //    params._ignored_columns = Array("ID")

    val dl = new DeepLearning(params)
    dl.trainModel.get

  }

}

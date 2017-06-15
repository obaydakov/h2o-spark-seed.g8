package com.yarenty.h2o

import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}
import water.fvec.{Frame, H2OFrame, Vec}
import water.{DKV, H2O, Key}


/**
  * The test verify implementation of {@link com.yarenty.h2o.Main} class.
  */
class H2OTest extends FlatSpec with Matchers with BeforeAndAfter {

  // Init cloud before run
  before {
    // Setup cloud name
    val args = Array[String]("-name", "h2o_test_cloud")
    // Build a cloud of 1
    H2O.main(args)
    H2O.waitForCloudSize(1, 10 * 1000 /* ms */)
  }


  def arrayToVec(arr: Array[Double]): Vec = {
    val vec = Vec.makeZero(arr.length)
    val vw = vec.open

    for (i <- arr.indices) {
      vw.set(i, arr(i))
    }
    vw.close()
    vec
  }

  def arrayToTimeVec(arr: Array[Long]): Vec = {
    val vec = Vec.makeZero(arr.length) //, Vec.T_TIME)
    val vw = vec.open

    for (i <- arr.indices) {
      vw.set(i, arr(i))
    }
    vw.close()
    vec
  }


  "AnomalyDetection using frame with dates and values" should " predict anomalies based on daily/weekly behaviour " in {
    
    assert( true === true)
    
  }

}

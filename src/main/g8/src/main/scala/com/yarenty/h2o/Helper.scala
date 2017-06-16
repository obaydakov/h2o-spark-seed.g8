package com.yarenty.h2o

import org.apache.spark.h2o.H2OContext
import water.fvec.{H2OFrame, Vec}
import water.support.H2OFrameSupport

/**
  * Created by yarenty on 16/06/2017.
  */
object Helper {

  def split(in: H2OFrame)(implicit h2oContext: H2OContext): (H2OFrame, H2OFrame) = {
    import h2oContext.implicits._

    val keys = Array[String]("train.hex", "test.hex")
    val ratios = Array[Double](0.8, 0.2)

    val frs = H2OFrameSupport.splitFrame(in, keys, ratios)
    (frs(0), frs(1))
  }


  def vecToArray(v: Vec): Array[Double] = {
    val arr = Array.ofDim[Double](v.length.toInt)
    for (i <- 0 until v.length.toInt) {
      arr(i) = v.at(i)
    }
    arr
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
    val vec = Vec.makeZero(arr.length, Vec.T_TIME)
    val vw = vec.open

    for (i <- arr.indices) {
      vw.set(i, arr(i))
    }
    vw.close()
    vec
  }

}

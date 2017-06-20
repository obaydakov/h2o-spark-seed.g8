package com.yarenty.h2o

import java.io.{ByteArrayInputStream, File, InputStream, PrintWriter}
import java.nio.charset.StandardCharsets

import org.apache.commons.io.FilenameUtils
import org.apache.spark.h2o.H2OContext
import water.fvec.{Frame, H2OFrame, Vec}
import water.support.H2OFrameSupport
import water.util.Log

import scala.reflect.io.Directory

/**
  * Created by yarenty on 16/06/2017.
  */
object Helper {

  def saveCSV(f: Frame, fileName: String): Unit = {
    Log.debug("CSV export::" + fileName)
    val csv = f.toCSV(true, false)
    val csv_writer = new PrintWriter(new File(fileName))
    while (csv.available() > 0) {
      csv_writer.write(csv.read.toChar)
    }
    csv_writer.close()
  }


  def createOutputDirectory(fileName: String, force: Boolean = false): Boolean = {
    val dir = FilenameUtils.getFullPathNoEndSeparator(fileName)
    Log.debug(s"Create output directory: $dir")
    val out = Directory(dir)
    out.createDirectory(force = force)
    if (force && !out.exists) {
      Log.err(s"Could not create output directory: $dir")
      System.exit(-1)
    }
    out.exists
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

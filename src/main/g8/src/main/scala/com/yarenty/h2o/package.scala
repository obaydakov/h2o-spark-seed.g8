package com.yarenty

import java.io.{ByteArrayInputStream, File, InputStream, PrintWriter}
import java.nio.charset.StandardCharsets

import org.apache.commons.io.FilenameUtils

import scala.reflect.io.Directory

/**
  * Created by yarenty on 16/06/2017.
  */
package object h2o {


  def getSimpleCSVParser: ParseSetup = {
    val p = new ParseSetup()
    p.setParseType(DefaultParserProviders.CSV_INFO)
    p.setSeparator(44)
    p.setSingleQuotes(false)
    p.setCheckHeader(1)
    p
  }

  def saveCSV(f: Frame, fileName: String): Unit = {
    Log.debug("CSV export::" + fileName)
    val csv = f.toCSV(true, false)
    val csv_writer = new PrintWriter(new File(fileName))
    while (csv.available() > 0) {
      csv_writer.write(csv.read.toChar)
    }
    csv_writer.close()
  }



  def saveString(f: String, fileName: String, force: Boolean = true): Unit = {
    createOutputDirectory(fileName, force)
    val stream: InputStream = new ByteArrayInputStream(f.getBytes(StandardCharsets.UTF_8))
    val string_writer = new PrintWriter(new File(fileName))
    while (stream.available() > 0) {
      string_writer.write(stream.read.toChar)
    }
    string_writer.close()
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


  def split(in: H2OFrame, ratio: Double): (Frame, Frame) = {

    val keys = Array[String]("train.hex", "test.hex")
    val ratios = Array[Double](ratio)

    val frs = split(in, keys, ratios)
    (frs(0), frs(1))
  }


  def split[T <: Frame](fr: T, keys: Seq[String], ratios: Seq[Double]): Array[Frame] = {
    val ks = keys.map(Key.make[Frame](_)).toArray
    val splitter = new FrameSplitter(fr, ratios.toArray, ks, null)
    water.H2O.submitTask(splitter)
    // return results
    splitter.getResult
  }




}

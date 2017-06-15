package com.yarenty.h2o

import org.apache.spark.SparkContext
import org.apache.spark.h2o.H2OContext
import org.apache.spark.sql.{SQLContext, SparkSession}
import water.fvec.{Frame, H2OFrame, Vec}
import water.support.{H2OFrameSupport, SparkContextSupport, SparklingWaterApp}

/**
  * Created by yarenty on 15/06/2017.
  */
object Main extends SparkContextSupport {

  // Prepare environment
  implicit val sc = new SparkContext(configure("H2O Hyper-parameter AI search"))
  // SQL support
  implicit val sqlContext = SparkSession.builder().getOrCreate().sqlContext
  // Start H2O services
  implicit val h2oContext = H2OContext.getOrCreate(sc)

  

  def main(args: Array[String]): Unit = {



/*
    val datadir = "/opt/data/mercedes"

    val input = new H2OFrame(getParser, new java.net.URI(s"${datadir}/train.csv"))
    val names = input._names.drop(2) //ID,y
    println(names.mkString(";"))
    input.colToEnum(names)
    val (train, valid) = app.split(input, Array("train_input.hex","valid_input.hex"))
    val test = new H2OFrame(getParser, new java.net.URI(s"${datadir}/test.csv"))
    test.colToEnum(names)
    
    val out = GBMModelUnderTest.calculate(params, train,valid)
    
    /   val hp = app.initialRound( train,valid)
     val hp = app.prevPredict()

    val vecs:Array[Vec] = for (h <- hp) yield {
      val v = Vec.makeZero(h.length)
      for (i <- h.indices) {
        v.set(i,h(i))
      }
      v
    }
    

    
    val outFrame = new H2OFrame(new Frame(Array("alpha", "lambda", "beta", "acc"), vecs))
    val (train, test) = app.split(outFrame)




    val pVecs:Array[Vec] = for (h <-  app.toPredict()) yield {
      val v = Vec.makeZero(h.length)
      for (i <- h.indices) {
        v.set(i,h(i))
      }
      v
    }
    val predMe = new H2OFrame(new Frame(Array("alpha", "lambda", "beta", "acc"), pVecs))
    
    
    val out = HyperAIFinder.calculate(train, test, predMe)

    println(out.maxs().mkString(";"))
    
    val ids = app.getIds(out)

    val params = GBMHyperParams.getParams()
    
    println(" predicted best  output:")
    
    var pred = Vector[Double]()
    var real = Vector[Double]()

    for (i <-ids) {
      
      
      val a = pVecs(0).at(i)
      val l = pVecs(1).at(i)
      val b = pVecs(2).at(i)
      val p = out.at(i)
      
      println(s"$i => a=$a; l=$l; b=$b; predOut=$p;")


      params._alpha = Array(a)
      params._lambda = Array(l)
      params._beta_epsilon = b

      val fout = GLMModelUnderTest.calculate(params)
      println(s"$i => a=$a; l=$l; b=$b; predOut=$p; REAL!! = $fout ")
      pred = pred :+ p
      real = real :+ fout
    }

    val initial = outFrame.vec("acc").maxs()
    
    println("SUMMARY:")
    val _p = new GLMParameters()
    _p._family = GLMParameters.Family.binomial
    _p._alpha = Array(1.0) 
    _p._lambda = Array(0.0)
    
    val blind = GLMModelUnderTest.calculate(_p)
    println("SUMMARY:")
    
    println("            blind guess=" + blind)
    println("     initial max values=" + initial.mkString(";"))
    println(" predicted by inception=" + pred.mkString(";"))
    println("      real after retest=" + real.mkString(";"))

*/

  }


//  def split(in: H2OFrame): (H2OFrame, H2OFrame) = {
//    val keys = Array[String]("trainHyper.hex", "testHyper.hex")
//    split(in,keys)
//  }
//
//
//  def split(in: H2OFrame, keys:Array[String]): (H2OFrame, H2OFrame) = {
//    import h2oContext.implicits._
//
//
//    val ratios = Array[Double](0.8, 0.2)
//    val frs = splitFrame(in, keys, ratios)
//    val (train, test) = (frs(0), frs(1))
//    (train, test)
//  }
//


  def getIds(vec: Vec): Array[Int] = {
    val min = vec.maxs().min
    vecToArray(vec).zipWithIndex.filter(_._1 >= min).map(_._2)
  }


  def vecToArray(v: Vec): Array[Double] = {
    val arr = Array.ofDim[Double](v.length.toInt)
    for (i <- 0 until v.length.toInt) {
      arr(i) = v.at(i)
    }
    arr
  }

  

}
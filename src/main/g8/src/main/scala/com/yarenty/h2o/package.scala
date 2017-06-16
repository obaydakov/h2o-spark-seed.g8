package com.yarenty

import water.parser.{DefaultParserProviders, ParseSetup}

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
  
  
}

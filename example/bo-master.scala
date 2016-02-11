/*
 * Copyright 2015 bigobject.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bigobject.spark

import scala.collection.Map

import org.apache.spark.{Logging, SparkContext}
import org.apache.spark.sql.{DataFrame, SQLContext}

/* BORDD singleton
 * . accumulate multiple BO nodes.
*/
object BOMaster extends Logging {

  def sql(
    sc: SparkContext,
    url: String,
    sqlString: String,
    sqlCtx: SQLContext = null) : DataFrame = {
    var sqlSc = sqlCtx
    if (sqlCtx == null)
      sqlSc = new SQLContext(sc)

    val urls = url.split(",")

    var allDF = null.asInstanceOf[DataFrame]
    for (u <- urls) {
	  val opt = Map(
	    "url" -> s"jdbc:mysql://$u",
		"driver" -> "com.mysql.jdbc.Driver",
		"dbtable" -> s"($sqlString)")
      val dfBO = sqlSc.read.format("jdbc").options(opt).load()
      if (allDF == null)
        allDF = dfBO
      else
        allDF = allDF.unionAll(dfBO)
    }
	allDF
  }

}
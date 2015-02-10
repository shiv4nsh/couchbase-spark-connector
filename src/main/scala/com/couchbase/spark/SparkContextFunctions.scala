/**
 * Copyright (C) 2015 Couchbase, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALING
 * IN THE SOFTWARE.
 */
package com.couchbase.spark

import com.couchbase.client.java.document.{RawJsonDocument, Document, JsonDocument}
import com.couchbase.spark.connection.CouchbaseConfig
import com.couchbase.spark.rdd.DocumentRDD
import org.apache.spark.SparkContext

import scala.reflect.ClassTag

class SparkContextFunctions(@transient val sc: SparkContext) extends Serializable {

  def couchbaseGet[D <: Document[_]: ClassTag](id: String): DocumentRDD[D] = {
    couchbaseGet(Seq(id)).asInstanceOf[DocumentRDD[D]]
  }

  def couchbaseGet[D <: Document[_]](ids: Seq[String])(implicit ct: ClassTag[D]) = {
    ct match {
      case ClassTag.Nothing => new DocumentRDD[JsonDocument](sc, new CouchbaseConfig(sc), ids, 1)
      case _ => new DocumentRDD[D](sc, new CouchbaseConfig(sc), ids, 1)
    }
  }

}
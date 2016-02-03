# Spark-bo

BO, a.k.a [Bigobject](http://www.bigobject.io/).

"BigObject Analytics is an analytic database. It is designed to help users easily gain actionable insights from their data and build data-driven applications."

"BigObject Analytics delivers an analytic framework that unmask the intelligence out of your data."

## How to collaborate with multiple BO nodes?
BOMaster is a singleton to accumulate and co-work with multiple BO nodes. You’ve to build your code with bo-master.scala. Besides, you may want to change the package name in bo-master.scala to make it same as your package.

### How to use?
To work with BO, you can use BOMaster object, it provides a function to issue a SQL statement. Here is the object definition:

```scala
object BOMaster extends Logging {

  /**
   * Returns accumulated DataFrame from one or more BO nodes
   * @param sc Spark context
   * @param url BO IP/hostname. If there are multiple BO nodes, add comma in between, e.g., “192.168.1.100,192.168.1.200”
   * @param sqlString SQL statement
   * @param sqlCtx SQL context (optional)
   */
  def sql(
    sc: SparkContext,
    url: String,
    sqlString: String,
    sqlCtx: SQLContext = null) : DataFrame

}
```

Here is an example to find product's brands from sales table.

```scala
val url = “192.168.1.100,192.168.1.200”
val stm = “FIND Product.brand FROM sales”
val dfFindBrand = BOMaster.sql(sc, url, stm)
```

### How to run?
BO supports [MySQL](https://www.mysql.com) protocol. To run above code, you’ve to download [MySQL jdbc driver](https://dev.mysql.com/downloads/connector/j/).

Run your code in spark-shell:

```
$ bin/spark-shell --jars <mysql jdbc driver>.jar
```

Submit your code to Spark cluster:

```
$ bin/spark-submit --<your class> --master spark://127.0.0.1:7077 --jars <mysql jdbc driver>.jar <your package>.jar
```

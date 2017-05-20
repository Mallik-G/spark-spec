package com.github.mrpowers.spark.spec.sql

import java.sql.Timestamp
import java.sql.Date

import com.github.mrpowers.spark.fast.tests.DataFrameComparer
import com.github.mrpowers.spark.models._
import com.github.mrpowers.spark.spec.SparkSessionTestWrapper
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.{StructType, _}
import org.scalatest._
import com.github.mrpowers.spark.daria.sql.SparkSessionExt._

class FunctionsSpec
    extends FunSpec
    with SparkSessionTestWrapper
    with DataFrameComparer {

  import spark.implicits._

  describe("#abs") {

    it("calculates the absolute value") {

      val sourceDF = spark.createDF(
        List(
          (1),
          (-8),
          (-5),
          (null)
        ), List(
          ("num1", IntegerType, true)
        )
      )

      val actualDF = sourceDF.withColumn("num1abs", abs(col("num1")))

      val expectedDF = spark.createDF(
        List(
          (1, 1),
          (-8, 8),
          (-5, 5),
          (null, null)
        ), List(
          ("num1", IntegerType, true),
          ("num1abs", IntegerType, true)
        )
      )

      assertSmallDataFrameEquality(actualDF, expectedDF)

    }

  }

  describe("#acos") {

    it("calculates the cosine inverse of the given value") {

      val sourceDF = spark.createDF(
        List(
          (-1.0),
          (-0.5),
          (0.5),
          (1.0)
        ), List(
          ("num1", DoubleType, false)
        )
      )

      val actualDF = sourceDF.withColumn("acos_value", acos(col("num1")))

      val expectedDF = spark.createDF(
        List(
          (-1.0, 3.141592653589793),
          (-0.5, 2.0943951023931957),
          (0.5, 1.0471975511965979),
          (1.0, 0.0)
        ), List(
          ("num1", DoubleType, false),
          ("acos_value", DoubleType, true)
        )
      )

      assertSmallDataFrameEquality(actualDF, expectedDF)

    }

  }

  describe("#add_months") {

    it("returns the date that is numMonths after startDate") {

      val sourceDF = spark.createDF(
        List(
          ("1", Timestamp.valueOf("2016-01-01 00:00:00")),
          ("2", Timestamp.valueOf("2016-12-01 00:00:00"))
        ), List(
          ("person_id", StringType, true),
          ("birth_date", TimestampType, true)
        )
      )

      val actualDF = sourceDF.withColumn(
        "future_date",
        add_months(col("birth_date"), 2)
      )

      val expectedDF = Seq(
        ("1", "2016-01-01 00:00:00", "2016-03-01"),
        ("2", "2016-12-01 00:00:00", "2017-02-01")
      ).toDF("person_id", "birth_date", "future_date")
        .withColumn("birth_date", col("birth_date").cast("timestamp"))
        .withColumn("future_date", col("future_date").cast("date"))

      assertSmallDataFrameEquality(actualDF, expectedDF)

    }

  }

  describe("#approx_count_distinct") {
    pending
  }

  describe("#array_contains") {
    pending
  }

  describe("#array") {
    pending
  }

  describe("#asc_nulls_first") {

    it("sorts a DataFrame with null values first") {

      val sourceDF = spark.createDF(
        List(null, 1, -8, null, -5),
        List(("num1", IntegerType, true))
      )

      val actualDF = sourceDF.sort(asc_nulls_first("num1"))

      val expectedDF = spark.createDF(
        List(null, null, -8, -5, 1),
        List(("num1", IntegerType, true))
      )

      assertSmallDataFrameEquality(actualDF, expectedDF)

    }

  }

  describe("#asc_nulls_last") {

    it("sorts a DataFrame with null values last") {

      val sourceDF = spark.createDF(
        List(
          (null),
          (1),
          (-8),
          (null),
          (-5)
        ), List(
          ("num1", IntegerType, true)
        )
      )

      val actualDF = sourceDF.sort(asc_nulls_last("num1"))

      val expectedDF = spark.createDF(
        List(
          (-8),
          (-5),
          (1),
          (null),
          (null)
        ), List(
          ("num1", IntegerType, true)
        )
      )

      assertSmallDataFrameEquality(actualDF, expectedDF)

    }

  }

  describe("#asc") {

    it("sorts a DataFrame in ascending order") {

      val sourceDF = spark.createDF(
        List(
          (1),
          (-8),
          (-5)
        ), List(
          ("num1", IntegerType, true)
        )
      )

      val actualDF = sourceDF.sort(asc("num1"))

      val expectedDF = spark.createDF(
        List(
          (-8),
          (-5),
          (1)
        ), List(
          ("num1", IntegerType, true)
        )
      )

      assertSmallDataFrameEquality(actualDF, expectedDF)

    }

  }

  describe("#ascii") {

    it("Computes the numeric value of the first character of the string column, and returns the result as an int column") {

      val sourceDF = spark.createDF(
        List(
          ("A"),
          ("AB"),
          ("B"),
          ("C"),
          ("1"),
          ("2"),
          ("3")
        ), List(
          ("Chr", StringType, true)
        )
      )

      val actualDF = sourceDF.withColumn("ASCII", ascii(col("Chr")))

      val expectedDF = spark.createDF(
        List(
          ("A", 65),
          ("AB", 65),
          ("B", 66),
          ("C", 67),
          ("1", 49),
          ("2", 50),
          ("3", 51)
        ), List(
          ("Chr", StringType, true),
          ("ASCII", IntegerType, true)
        )
      )

      assertSmallDataFrameEquality(actualDF, expectedDF)

    }

  }

  describe("#asin") {

    it("Computes the sine inverse of the given column; the returned angle is in the range -pi/2 through pi/2.") {

      val sourceDF = spark.createDF(
        List(
          (1.0),
          (0.2),
          (0.5)
        ), List(
          ("num1", DoubleType, true)
        )
      )

      val actualDF = sourceDF.withColumn("asin", asin("num1"))

      val expectedDF = spark.createDF(
        List(
          (1.0, 1.5707963267948966),
          (0.2, 0.2013579207903308),
          (0.5, 0.5235987755982989)
        ), List(
          ("num1", DoubleType, true),
          ("asin", DoubleType, true)
        )
      )

      assertSmallDataFrameEquality(actualDF, expectedDF)

    }

  }

  describe("#atan") {

    it("Computes the tangent inverse of the given column") {

      val sourceDF = spark.createDF(
        List(
          (1),
          (5),
          (8)
        ), List(
          ("num1", IntegerType, true)
        )
      )

      val actualDF = sourceDF.withColumn("atan", atan("num1"))

      val expectedDF = spark.createDF(
        List(
          (1, 0.7853981633974483),
          (5, 1.373400766945016),
          (8, 1.446441332248135)
        ), List(
          ("num1", IntegerType, true),
          ("atan", DoubleType, true)
        )
      )

      assertSmallDataFrameEquality(actualDF, expectedDF)

    }

  }

  describe("#atan2") {
    pending
  }

  describe("#avg") {

    it("Computes the average(an average is the sum of a list of numbers divided by the number of numbers in the list) of a column skipping the null values") {

      val sourceDF = spark.createDF(
        List(
          (7.793357934),
          (167.7902098),
          (-26.26209048),
          (113.8381503),
          (18.01957295),
          (-7.266169154),
          (10.20120724),
          (-658.5405405),
          (-6.702617801),
          (35.99217221),
          (0.0),
          (null)
        ), List(
          ("Double", DoubleType, true)
        )
      )

      val expectedDF = spark.createDF(
        List(
          (-31.37606795463637)
        ), List(
          ("average", DoubleType, true)
        )
      )

      val actualDF = sourceDF.agg(avg("Double").as("average"))

      assertSmallDataFrameEquality(actualDF, expectedDF)
    }

  }

  describe("#base64") {

    it("Computes the BASE64 encoding of a binary column and returns it as a string column") {

      val sourceDF = spark.createDF(
        List(
          ("01010100")
        ), List(
          ("bin1", StringType, true)
        )
      ).withColumn("bin1", col("bin1").cast("binary"))

      val actualDF = sourceDF.withColumn("base64", base64(col("bin1")))

      val expectedDF = spark.createDF(
        List(
          ("01010100", "MDEwMTAxMDA=")
        ), List(
          ("bin1", StringType, true),
          ("base64", StringType, true)
        )
      ).withColumn("bin1", col("bin1").cast("binary"))

      assertSmallDataFrameEquality(actualDF, expectedDF)

    }

  }

  describe("#bin") {
    pending
  }

  describe("#bitwiseNOT") {
    pending
  }

  describe("#broadcast") {
    pending
  }

  describe("#bround") {

    it("returns the value of the column e rounded to 0 decimal places with HALF_EVEN round mode") {

      val sourceDF = spark.createDF(
        List(
          (8.1),
          (64.8),
          (3.5),
          (-27.0),
          (null)
        ), List(
          ("num1", DoubleType, true)
        )
      )

      val actualDF = sourceDF.withColumn("brounded_num", bround(col("num1")))

      val expectedDF = spark.createDF(
        List(
          (8.1, 8.0),
          (64.8, 65.0),
          (3.5, 4.0),
          (-27.0, -27.0),
          (null, null)
        ), List(
          ("num1", DoubleType, true),
          ("brounded_num", DoubleType, true)
        )
      )

      assertSmallDataFrameEquality(actualDF, expectedDF)

    }

    pending
  }

  describe("#callUDF") {
    pending
  }

  describe("#cbrt") {

    it("computes the cube-root of the given value") {

      val sourceDF = Seq(
        (8),
        (64),
        (-27)
      ).toDF("num1")

      val actualDF = sourceDF.withColumn("cube_root", cbrt(col("num1")))

      val expectedDF = spark.createDF(
        List(
          (8, 2.0),
          (64, 4.0),
          (-27, -3.0)
        ), List(
          ("num1", IntegerType, false),
          ("cube_root", DoubleType, true)
        )
      )

      assertSmallDataFrameEquality(actualDF, expectedDF)

    }

  }

  describe("#ceil") {

    it("rounds the number up to the nearest integer") {

      val numbersDF = Seq(
        (1.5),
        (-8.1),
        (5.9)
      ).toDF("num1")

      val actualDF = numbersDF.withColumn("upper", ceil(col("num1")))

      val expectedDF = spark.createDF(
        List(
          (1.5, 2L),
          (-8.1, -8L),
          (5.9, 6L)
        ), List(
          ("num1", DoubleType, false),
          ("upper", LongType, true)
        )
      )

      assertSmallDataFrameEquality(actualDF, expectedDF)

    }

  }

  describe("#coalesce") {

    it("returns the first column that is not null, or null if all inputs are null.") {

      val wordsDF = Seq(
        ("banh", "mi"),
        ("pho", "ga"),
        (null, "cheese"),
        ("pizza", null),
        (null, null)
      ).toDF("word1", "word2")

      val actualDF = wordsDF.withColumn(
        "yummy",
        coalesce(
          col("word1"),
          col("word2")
        )
      )

      val expectedDF = Seq(
        ("banh", "mi", "banh"),
        ("pho", "ga", "pho"),
        (null, "cheese", "cheese"),
        ("pizza", null, "pizza"),
        (null, null, null)
      ).toDF("word1", "word2", "yummy")

      assertSmallDataFrameEquality(actualDF, expectedDF)

    }

  }

  describe("#col") {
    pending
  }

  describe("#collect_list") {
    pending
  }

  describe("#collect_set") {
    pending
  }

  describe("#column") {
    pending
  }

  describe("#concat_ws") {

    it("concatenates multiple input string columns with separator") {

      val wordsDF = Seq(
        ("banh", "mi"),
        ("pho", "ga"),
        (null, "cheese"),
        ("pizza", null),
        (null, null)
      ).toDF("word1", "word2")

      val actualDF = wordsDF.withColumn(
        "yummy",
        concat_ws(
          "_",
          col("word1"),
          col("word2")
        )
      )

      val expectedDF = spark.createDF(
        List(
          ("banh", "mi", "banh_mi"),
          ("pho", "ga", "pho_ga"),
          (null, "cheese", "cheese"), // null column will be omitted
          ("pizza", null, "pizza"), // null column will be omitted
          (null, null, "") // all null columns give ""
        ), List(
          ("word1", StringType, true),
          ("word2", StringType, true),
          ("yummy", StringType, false)
        )
      )

      assertSmallDataFrameEquality(actualDF, expectedDF)

    }

  }

  describe("#concat") {

    it("concatenates multiple input string columns together into a single string column") {

      val wordsDF = Seq(
        ("banh", "mi"),
        ("pho", "ga"),
        (null, "cheese"),
        ("pizza", null),
        (null, null)
      ).toDF("word1", "word2")

      val actualDF = wordsDF.withColumn(
        "yummy",
        concat(
          col("word1"),
          col("word2")
        )
      )

      val expectedDF = Seq(
        ("banh", "mi", "banhmi"),
        ("pho", "ga", "phoga"),
        (null, "cheese", null),
        ("pizza", null, null),
        (null, null, null)
      ).toDF("word1", "word2", "yummy")

      assertSmallDataFrameEquality(actualDF, expectedDF)

    }

  }

  describe("#conv") {
    pending
  }

  describe("#corr") {
    pending
  }

  describe("#cos") {

    it("calculates the cosine of the given value") {

      val sourceDF = spark.createDF(
        List(
          (1),
          (2),
          (3)
        ), List(
          ("num1", IntegerType, true)
        )
      )

      val actualDF = sourceDF.withColumn("i_am_scared", cos("num1"))

      val expectedDF = spark.createDF(
        List(
          (1, 0.5403023058681398),
          (2, -0.4161468365471424),
          (3, -0.9899924966004454)
        ), List(
          ("num1", IntegerType, true),
          ("i_am_scared", DoubleType, true)
        )
      )

      assertSmallDataFrameEquality(actualDF, expectedDF)

    }

  }

  describe("#cosh") {
    pending
  }

  describe("#count") {
    it("Returns the number of rows in the DataFrame.") {

      val expectedCount = 5
      val sourceDF = Seq(
        ("Spotlight", 2015),
        ("Birdman", 2014),
        ("12 Years a Slave", 2013),
        ("Argo", 2012),
        ("Argo", 2012)
      ).toDF("movie", "year")

      val rowCount = sourceDF.count;

      assert(rowCount, expectedCount)

    }
  }

  describe("#countDistinct") {

    it("aggregate function: returns the number of distinct items in a group") {

      val sourceDF = Seq(
        ("A", 1),
        ("B", 1),
        ("A", 2),
        ("A", 2),
        ("B", 3),
        ("A", 3)
      ).toDF("id", "foo")

      val actualDF = sourceDF.groupBy($"id").agg(countDistinct($"foo") as "distinctCountFoo").orderBy($"id")

      val expectedDF = spark.createDF(
        List(
          ("A", 3L),
          ("B", 2L)
        ), List(
          ("id", StringType, true),
          ("distinctCountFoo", LongType, false)
        )
      )

      assertSmallDataFrameEquality(actualDF, expectedDF)

    }

  }

  describe("#covar_pop") {
    pending
  }

  describe("#covar_samp") {
    pending
  }

  describe("#crc32") {
    pending
  }

  describe("#cume_dist") {
    pending
  }

  describe("#current_date") {
    pending
  }

  describe("#current_timestamp") {
    pending
  }

  describe("#date_add") {

    it("returns the date that is days days after start") {

      val sourceDF = Seq(
        ("1", "2016-01-01 00:00:00"),
        ("2", "2016-12-01 00:00:00")
      ).toDF("person_id", "birth_date")
        .withColumn("birth_date", col("birth_date").cast("timestamp"))

      val actualDF = sourceDF.withColumn(
        "future_date",
        date_add(col("birth_date"), 4)
      )

      val expectedDF = Seq(
        ("1", "2016-01-01 00:00:00", "2016-01-05"),
        ("2", "2016-12-01 00:00:00", "2016-12-05")
      ).toDF("person_id", "birth_date", "future_date")
        .withColumn("birth_date", col("birth_date").cast("timestamp"))
        .withColumn("future_date", col("future_date").cast("date"))

      assertSmallDataFrameEquality(actualDF, expectedDF)

    }

  }

  describe("#date_format") {
    pending
  }

  describe("#date_sub") {
    pending
  }

  describe("#datediff") {
    pending
  }

  describe("#dayofmonth") {
    pending
  }

  describe("#dayofyear") {
    pending
  }

  describe("#decode") {
    pending
  }

  describe("#degrees") {
    pending
  }

  describe("#dense_rank") {
    pending
  }

  describe("#desc_nulls_first") {

    it("Returns a sort expression based on the descending order of the column, and null values appear before non-null values.") {

      val sourceDF = spark.createDF(
        List(
          (null),
          (1),
          (-8),
          (null),
          (-5)
        ), List(
          ("num1", IntegerType, true)
        )
      )

      val actualDF = sourceDF.sort(desc_nulls_first("num1"))

      val expectedDF = spark.createDF(
        List(
          (null),
          (null),
          (1),
          (-5),
          (-8)
        ), List(
          ("num1", IntegerType, true)
        )
      )

      assertSmallDataFrameEquality(actualDF, expectedDF)

    }

  }

  describe("#desc_nulls_last") {

    it("Returns a sort expression based on the descending order of the column, and null values appear after non-null values.") {

      val sourceDF = spark.createDF(
        List(
          (null),
          (1),
          (7),
          (null),
          (-5)
        ), List(
          ("num1", IntegerType, true)
        )
      )

      val actualDF = sourceDF.sort(desc_nulls_last("num1"))

      val expectedDF = spark.createDF(
        List(
          (7),
          (1),
          (-5),
          (null),
          (null)
        ), List(
          ("num1", IntegerType, true)
        )
      )

      assertSmallDataFrameEquality(actualDF, expectedDF)

    }

  }

  describe("#desc") {

    it("sorts a column in descending order") {

      val sourceDF = spark.createDF(
        List(
          (1),
          (-8),
          (-5)
        ), List(
          ("num1", IntegerType, true)
        )
      )

      val actualDF = sourceDF.sort(desc("num1"))

      val expectedDF = spark.createDF(
        List(
          (1),
          (-5),
          (-8)
        ), List(
          ("num1", IntegerType, true)
        )
      )

      assertSmallDataFrameEquality(actualDF, expectedDF)
    }

  }

  describe("#encode") {
    pending
  }

  describe("#exp") {
    pending
  }

  describe("#explode") {

    it("returns a new DataFrame where each row has been expanded to zero or more rows by the provided function") {

      val df = Seq(
        ("A", Seq("a", "b", "c"), Seq(1, 2, 3)),
        ("B", Seq("a", "b", "c"), Seq(5, 6, 7))
      ).toDF("id", "class", "num")

      val actualDF = df.select(
        df("id"),
        explode(df("class")).alias("class"),
        df("num")
      )

      val expectedDF = Seq(
        ("A", "a", Seq(1, 2, 3)),
        ("A", "b", Seq(1, 2, 3)),
        ("A", "c", Seq(1, 2, 3)),
        ("B", "a", Seq(5, 6, 7)),
        ("B", "b", Seq(5, 6, 7)),
        ("B", "c", Seq(5, 6, 7))
      ).toDF("id", "class", "num")

      assertSmallDataFrameEquality(actualDF, expectedDF)

    }

  }

  describe("#expm1") {
    pending
  }

  describe("#expr") {
    pending
  }

  describe("#factorial") {

    it("calculates the product of an integer and all the integers below") {

      val sourceDF = spark.createDF(
        List(
          (0),
          (1),
          (2),
          (3),
          (4),
          (5),
          (6)
        ), List(
          ("number", IntegerType, false)
        )
      )

      val actualDF = sourceDF.withColumn("result", factorial(col("number")))

      val expectedDF = spark.createDF(
        List(
          (0, 1L),
          (1, 1L),
          (2, 2L),
          (3, 6L),
          (4, 24L),
          (5, 120L),
          (6, 720L)
        ), List(
          ("number", IntegerType, false),
          ("result", LongType, true)
        )
      )

      assertSmallDataFrameEquality(actualDF, expectedDF)

    }

  }

  describe("#first") {
    pending
  }

  describe("#floor") {
    pending
  }

  describe("#format_number") {
    pending
  }

  describe("#format_string") {
    pending
  }

  describe("#from_json") {
    pending
  }

  describe("#from_unixtime") {
    pending
  }

  describe("#get_json_object") {
    pending
  }

  describe("#greatest") {
    pending
  }

  describe("#grouping_id") {
    pending
  }

  describe("#grouping") {
    pending
  }

  describe("#hash") {
    pending
  }

  describe("#hex") {
    pending
  }

  describe("#hour") {
    pending
  }

  describe("#hypot") {
    pending
  }

  describe("#initcap") {

    it("converts the first letter of each word to upper case, returns a new column") {

      val wordsDF = Seq(
        ("bat man"),
        ("cat woman"),
        ("spider man")
      ).toDF("no_upper_words")

      val actualDF = wordsDF.withColumn("first_upper", initcap(col("no_upper_words")))

      val expectedDF = Seq(
        ("bat man", "Bat Man"),
        ("cat woman", "Cat Woman"),
        ("spider man", "Spider Man")
      ).toDF("no_upper_words", "first_upper")

      assertSmallDataFrameEquality(actualDF, expectedDF)

    }

  }

  describe("#input_file_name") {
    pending
  }

  describe("#instr") {
    pending
  }

  describe("#isnan") {
    pending
  }

  describe("#isnull") {

    it("checks column values for null") {

      val wordsDF = Seq(
        (null),
        ("hello"),
        (null),
        (null),
        ("football")
      ).toDF("word")

      val actualDF = wordsDF.withColumn("nullCheck", isnull(col("word")))

      val expectedDF = Seq(
        (null, true),
        ("hello", false),
        (null, true),
        (null, true),
        ("football", false)
      ).toDF("word", "nullCheck")

      assertSmallDataFrameEquality(actualDF, expectedDF)
    }

  }

  describe("#json_tuple") {
    pending
  }

  describe("#kurtosis") {
    pending
  }

  describe("#lag") {
    pending
  }

  describe("#last_day") {
    pending
  }

  describe("#last") {
    pending
  }

  describe("#lead") {
    pending
  }

  describe("#least") {
    pending
  }

  describe("#length") {

    it("returns the length of the column") {

      val expectedDF = spark.createDF(
        List(
          ("banh", 4),
          ("delilah", 7),
          (null, null)
        ), List(
          ("word", StringType, true),
          ("length", IntegerType, true)
        )
      )

      val wordsDF = Seq(
        ("banh"),
        ("delilah"),
        (null)
      ).toDF("word")

      val actualDF = wordsDF.withColumn("length", length(col("word")))

      assertSmallDataFrameEquality(actualDF, expectedDF)

    }

  }

  describe("#levenshtein") {
    pending
  }

  describe("#lit") {
    pending
  }

  describe("#locate") {

    it("returns index of first occurrence of search string") {

      val sourceDF = spark.createDF(
        List(
          ("Spider-man"),
          ("Batman")
        ), List(
          ("word", StringType, true)
        )
      )

      val actualDF = sourceDF.withColumn("short_word", locate("man", col("word")))

      val expectedDF = spark.createDF(
        List(
          ("Spider-man", 8),
          ("Batman", 4)
        ), List(
          ("word", StringType, true),
          ("short_word", IntegerType, true)
        )
      )

      assertSmallDataFrameEquality(actualDF, expectedDF)

    }

  }

  describe("#log") {
    pending
  }

  describe("#log10") {
    pending
  }

  describe("#log1p") {
    pending
  }

  describe("#log2") {
    pending
  }

  describe("#lower") {

    it("converts a string to lower case") {

      val wordsDF = Seq(
        ("Batman"),
        ("CATWOMAN"),
        ("pikachu")
      ).toDF("word")

      val actualDF = wordsDF.withColumn("lower_word", lower(col("word")))

      val expectedDF = Seq(
        ("Batman", "batman"),
        ("CATWOMAN", "catwoman"),
        ("pikachu", "pikachu")
      ).toDF("word", "lower_word")

      assertSmallDataFrameEquality(actualDF, expectedDF)

    }

  }

  describe("#lpad") {
    pending
  }

  describe("#ltrim") {
    pending
  }

  describe("#map") {
    pending
  }

  describe("#max") {
    pending
  }

  describe("#md5") {
    pending
  }

  describe("#mean") {
    pending
  }

  describe("#min") {
    pending
  }

  describe("#minute") {

    it("extracts the minute from a timestamp") {

      val sourceDF = spark.createDF(
        List(
          ("1", Timestamp.valueOf("2016-01-01 00:10:00")),
          ("2", Timestamp.valueOf("1970-12-01 00:06:00"))
        ), List(
          ("person_id", StringType, true),
          ("birth_date", TimestampType, true)
        )
      )

      val actualDF = sourceDF.withColumn("birth_minute", minute(col("birth_date")))

      val expectedDF = spark.createDF(
        List(
          ("1", Timestamp.valueOf("2016-01-01 00:10:00"), 10),
          ("2", Timestamp.valueOf("1970-12-01 00:06:00"), 6)
        ), List(
          ("person_id", StringType, true),
          ("birth_date", TimestampType, true),
          ("birth_minute", IntegerType, true)
        )
      )

      assertSmallDataFrameEquality(actualDF, expectedDF)

    }

  }

  describe("#monotonically_increasing_id") {
    pending
  }

  describe("#month") {
    pending
  }

  describe("#months_between") {
    pending
  }

  describe("#nanv1") {
    pending
  }

  describe("#negate") {
    pending
  }

  describe("#next_day") {
    pending
  }

  describe("#not") {
    pending
  }

  describe("#ntile") {
    pending
  }

  describe("#percent_rank") {
    pending
  }

  describe("#pmod") {
    pending
  }

  describe("#least") {
    pending
  }

  describe("#posexplode") {
    pending
  }

  describe("#pow") {

    it("returns the value of the first argument raised to the power of the second argument") {

      val numsDF = Seq(
        (2),
        (3),
        (1)
      ).toDF("num")

      val actualDF = numsDF.withColumn("power", pow(col("num"), 3))

      val expectedDF = spark.createDF(
        List(
          (2, 8.0),
          (3, 27.0),
          (1, 1.0)
        ), List(
          ("num", IntegerType, false),
          ("power", DoubleType, false)
        )
      )

      assertSmallDataFrameEquality(actualDF, expectedDF)

    }

  }

  describe("#quarter") {
    pending
  }

  describe("#radians") {
    pending
  }

  describe("#rand") {
    pending
  }

  describe("#randn") {
    pending
  }

  describe("#rank") {
    pending
  }

  describe("#regexp_extract") {
    pending
  }

  describe("#regexp_replace") {
    pending
  }

  describe("#repeat") {
    pending
  }

  describe("#reverse") {
    pending
  }

  describe("#rint") {
    pending
  }

  describe("#round") {
    pending
  }

  describe("#row_number") {
    pending
  }

  describe("#rpad") {

    it("Right-padded with pad to a length of len") {

      val wordsDF = Seq(
        ("banh"),
        ("delilah"),
        (null),
        ("c")
      ).toDF("word1")

      val actualDF = wordsDF.withColumn("rpad_column", rpad(col("word1"), 5, "x"))

      val expectedDF = Seq(
        ("banh", "banhx"),
        ("delilah", "delil"),
        (null, null),
        ("c", "cxxxx")
      ).toDF("word1", "rpad_column")

      assertSmallDataFrameEquality(actualDF, expectedDF)

    }

  }

  describe("#rtrim") {

    it("trims the spaces from right end for the specified string value") {

      val wordsDF = Seq(
        ("nice   "),
        ("cat"),
        (null),
        ("  person         ")
      ).toDF("word1")

      val actualDF = wordsDF.withColumn(
        "rtrim_column", rtrim(col("word1"))
      )

      val expectedDF = Seq(
        ("nice   ", "nice"),
        ("cat", "cat"),
        (null, null),
        ("  person         ", "  person")
      ).toDF("word1", "rtrim_column")

      assertSmallDataFrameEquality(actualDF, expectedDF)

    }

  }

  describe("#second") {
    pending
  }

  describe("#sha1") {
    pending
  }

  describe("#sha2") {
    pending
  }

  describe("#shiftLeft") {
    pending
  }

  describe("#shiftRight") {
    pending
  }

  describe("#shiftRightUnsigned") {
    pending
  }

  describe("#signum") {
    pending
  }

  describe("#sin") {
    pending
  }

  describe("#sinh") {
    pending
  }

  describe("#size") {
    pending
  }

  describe("#skewness") {
    pending
  }

  describe("#sort_array") {
    pending
  }

  describe("#soundex") {
    pending
  }

  describe("#spark_partition_id") {
    pending
  }

  describe("#split") {
    pending
  }

  describe("#sqrt") {

    it("Computes the square root of the specified float value") {

      val numsDF = Seq(
        (49),
        (144),
        (89)
      ).toDF("num1")

      val sqrtDF = numsDF.withColumn("sqrt_num", sqrt(col("num1")))

      val expectedDF = spark.createDF(
        List(
          (49, 7.0),
          (144, 12.0),
          (89, 9.433981132056603)
        ), List(
          ("num1", IntegerType, false),
          ("sqrt_num", DoubleType, true)
        )
      )

      assertSmallDataFrameEquality(sqrtDF, expectedDF)

    }

  }

  describe("#stddev_pop") {
    pending
  }

  describe("#stddev_samp") {
    pending
  }

  describe("#stddev") {
    pending
  }

  describe("#struct") {
    pending
  }

  describe("#substring_index") {
    pending
  }

  describe("#substring") {

    it("Slices a string, starts at position pos of length len.") {

      val wordsDF = Seq(
        ("Batman"),
        ("CATWOMAN"),
        ("pikachu")
      ).toDF("word")

      val actualDF = wordsDF.withColumn("substring_word", substring(col("word"), 0, 3))

      val expectedDF = Seq(
        ("Batman", "Bat"),
        ("CATWOMAN", "CAT"),
        ("pikachu", "pik")
      ).toDF("word", "substring_word")

      assertDataFrameEquality(actualDF, expectedDF)

    }

  }

  describe("#sum") {
    describe("(when column has only null values)") {
      it("returns null") {
        val sourceDF = Seq(SumNumericInput(None), SumNumericInput(None)).toDF
        val actualDF = sourceDF.agg(sum("colA") as "sum")
        val expectedDF = Seq(SumNumericOutput(None)).toDF

        assertSmallDataFrameEquality(actualDF, expectedDF)
      }
    }
    describe("(when column has at-least one non null value)") {
      it("returns the sum of a real numbers") {
        val sourceDF = Seq(SumNumericInput(Some(200.50)), SumNumericInput(Some(-10.0)), SumNumericInput(Some(10)), SumNumericInput(Some(-30)), SumNumericInput(None)).toDF
        val actualDF = sourceDF.agg(sum("colA") as "sum")
        val expectedDF = Seq(SumNumericOutput(Some(170.5))).toDF

        assertSmallDataFrameEquality(actualDF, expectedDF)
      }
    }
  }

  describe("#sumDistinct") {
    pending
  }

  describe("#tan") {
    pending
  }

  describe("#tanh") {
    pending
  }

  describe("#to_date") {
    pending
  }

  describe("#to_json") {
    pending
  }

  describe("#to_utc_timestamp") {
    pending
  }

  describe("#translate") {
    pending
  }

  describe("#trim") {

    it("converts a string to lower case") {

      val wordsDF = Seq(
        ("bat  "),
        ("  cat")
      ).toDF("word")

      val actualDF = wordsDF.withColumn("short_word", trim(col("word")))

      val expectedDF = Seq(
        ("bat  ", "bat"),
        ("  cat", "cat")
      ).toDF("word", "short_word")

      assertSmallDataFrameEquality(actualDF, expectedDF)

    }

  }

  describe("#trunc") {
    pending
  }

  describe("#udf") {
    pending
  }

  describe("#unbase64") {
    pending
  }

  describe("#unhex") {
    pending
  }

  describe("#unix_timestamp") {
    pending
  }

  describe("#upper") {

    it("converts a string to upper case") {

      val wordsDF = Seq(
        ("BatmaN"),
        ("boO"),
        ("piKachu")
      ).toDF("word")

      val actualDF = wordsDF.withColumn("upper_word", upper(col("word")))

      val expectedDF = Seq(
        ("BatmaN", "BATMAN"),
        ("boO", "BOO"),
        ("piKachu", "PIKACHU")
      ).toDF("word", "upper_word")

      assertSmallDataFrameEquality(actualDF, expectedDF)

    }

  }

  describe("#var_pop") {
    pending
  }

  describe("#var_samp") {
    pending
  }

  describe("#variance") {
    pending
  }

  describe("#weekofyear") {
    pending
  }

  describe("#when") {
    pending
  }

  describe("#window") {
    pending
  }

  describe("#year") {

    it("extracts the year from a timestamp") {

      val sourceDF = spark.createDF(
        List(
          ("1", Timestamp.valueOf("2016-01-01 00:00:00")),
          ("2", Timestamp.valueOf("1970-12-01 00:00:00"))
        ), List(
          ("person_id", StringType, true),
          ("birth_date", TimestampType, true)
        )
      )

      val actualDF = sourceDF.withColumn("birth_year", year(col("birth_date")))

      val expectedDF = spark.createDF(
        List(
          ("1", Timestamp.valueOf("2016-01-01 00:00:00"), 2016),
          ("2", Timestamp.valueOf("1970-12-01 00:00:00"), 1970)
        ), List(
          ("person_id", StringType, true),
          ("birth_date", TimestampType, true),
          ("birth_year", IntegerType, true)
        )
      )

      assertSmallDataFrameEquality(actualDF, expectedDF)

    }

    it("extracts the year from a date") {

      val sourceDF = spark.createDF(
        List(
          ("1", Date.valueOf("2016-01-01")),
          ("2", Date.valueOf("1970-12-01"))
        ), List(
          ("person_id", StringType, true),
          ("birth_date", DateType, true)
        )
      )

      val actualDF = sourceDF.withColumn("birth_year", year(col("birth_date")))

      val expectedDF = spark.createDF(
        List(
          ("1", Date.valueOf("2016-01-01"), 2016),
          ("2", Date.valueOf("1970-12-01"), 1970)
        ), List(
          ("person_id", StringType, true),
          ("birth_date", DateType, true),
          ("birth_year", IntegerType, true)
        )
      )

      assertSmallDataFrameEquality(actualDF, expectedDF)

    }

  }

}


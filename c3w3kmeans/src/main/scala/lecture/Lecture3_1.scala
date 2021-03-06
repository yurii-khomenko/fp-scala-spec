package lecture

import org.scalameter.{Key, Warmer, config}

object Lecture3_1 extends App {

//  def initArraySeq(xs: Array[Int])(v: Int): Unit =
//    for (i <- xs.indices) xs(i) = v

  def initArraySeq(xs: Array[Int])(v: Int): Unit = { // imperative, but more fast
    var i = 0
    while (i < xs.length) {
      xs(i) = v
      i += 1
    }
  }

  def initArrayPar(xs: Array[Int])(v: Int): Unit =
    for (i <- xs.indices.par) xs(i) = v

  val standardConfig = config(
    Key.exec.minWarmupRuns -> 20,
    Key.exec.maxWarmupRuns -> 50,
    Key.exec.benchRuns -> 10,
    Key.verbose -> true
  ) withWarmer new Warmer.Default

  val size = 100000000
  val array = Array.ofDim[Int](size)

  val cores = Runtime.getRuntime.availableProcessors()
  println(s"this machine has $cores cores")

  val seqtime = standardConfig measure {
    initArraySeq(array)(5)
  }
  println(s"sequential time: $seqtime ms")

  val parTaskTime = standardConfig measure {
    initArrayPar(array)(7)
  }
  println(s"parallel time: $parTaskTime ms")
  println(f"speedup by parallel: ${seqtime / parTaskTime}%.2f")
}
package fpmax.after

import scala.util.Try
import Program._

object App1 {
  def parseInt(s: String): Option[Int] = Try(s.toInt).toOption

  def finish[F[_], A](a: => A)(implicit F: Program[F]): F[A] = F.finish(a)
  def putStrLn[F[_]: Console](line: ConsoleOut): F[Unit] =
    Console[F].putStrLn(line)
  def getStrLn[F[_]: Console]: F[String] = Console[F].getStrLn

  def nextInt[F[_]](upper: Int)(implicit F: Random[F]): F[Int] =
    Random[F].nextInt(upper)

  def checkContinue[F[_]: Program: Console](name: String): F[Boolean] =
    for {
      _ <- putStrLn(ConsoleOut.DoYouWantToContinue(name))
      input <- getStrLn.map(_.toLowerCase)
      cont <- input match {
        case "y" => finish(true)
        case "n" => finish(false)
        case _   => checkContinue(name)
      }
    } yield cont

  def printResults[F[_]: Console](input: String,
                                  num: Int,
                                  name: String): F[Unit] =
    parseInt(input).fold(
      putStrLn(ConsoleOut.ThisIsNotValid(name))
    )(
      guess =>
        if (guess == num) putStrLn(ConsoleOut.YouGuessedRight(name))
        else
          putStrLn(ConsoleOut.YouGuessedWrong(name, num)))

  def gameLoop[F[_]: Program: Random: Console](name: String): F[Unit] =
    for {
      num <- nextInt(5).map(_ + 1)
      _ <- putStrLn(ConsoleOut.PleaseGuess(name))
      input <- getStrLn
      _ <- printResults(input, num, name)
      cont <- checkContinue(name)
      _ <- if (cont) gameLoop(name) else finish(())
    } yield ()

  def main[F[_]: Program: Random: Console]: F[Unit] =
    for {
      _ <- putStrLn(ConsoleOut.WhatIsYourName)
      name <- getStrLn
      _ <- putStrLn(ConsoleOut.WelcomeToGame(name))
      _ <- gameLoop(name)
    } yield ()

  def mainIO: IO[Unit] = main[IO]

  def mainTestIO: TestIO[Unit] = main[TestIO]

  val TestExample =
    TestData(input = "Tester" :: "1" :: "n" :: Nil,
             output = Nil,
             nums = 0 :: Nil)

  def runTest = mainTestIO.eval(TestExample).showResults

  def main(args: Array[String]): Unit = {
    println(runTest)
  }
}

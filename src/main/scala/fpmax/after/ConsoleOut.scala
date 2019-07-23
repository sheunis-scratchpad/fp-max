package fpmax.after

sealed trait ConsoleOut {
  def en: String
}

object ConsoleOut {
  case class YouGuessedRight(name: String) extends ConsoleOut {
    def en = "You guessed right, " + name + "!"
  }
  case class YouGuessedWrong(name: String, num: Int) extends ConsoleOut {
    def en = "You guessed wrong, " + name + "! The number was: " + num
  }
  case class DoYouWantToContinue(name: String) extends ConsoleOut {
    def en = "Do you want to continue, " + name + "?"
  }
  case class PleaseGuess(name: String) extends ConsoleOut {
    def en = "Dear " + name + ", please guess a number from 1 to 5:"
  }
  case class ThisIsNotValid(name: String) extends ConsoleOut {
    def en = "That is not a valid selection, " + name + "!"
  }
  case object WhatIsYourName extends ConsoleOut {
    def en = "What is your name?"
  }
  case class WelcomeToGame(name: String) extends ConsoleOut {
    def en = "Hello, " + name + ", welcome to the game!"
  }
}

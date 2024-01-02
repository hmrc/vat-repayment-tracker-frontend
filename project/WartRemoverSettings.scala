import sbt.Compile
import sbt.Keys.compile
import wartremover.Wart._
import wartremover.WartRemover.autoImport.{wartremoverErrors, wartremoverWarnings}

object  WartRemoverSettings {

  lazy val wartRemoverWarning = {
    val warningWarts = Seq(
      JavaSerializable,
      AsInstanceOf,
      IsInstanceOf
    )
    Compile / compile / wartremoverWarnings ++= warningWarts
  }
  lazy val wartRemoverError = {
    // Error
    val errorWarts = Seq(
      ArrayEquals,
      AnyVal,
      EitherProjectionPartial,
      Enumeration,
      ExplicitImplicitTypes,
      FinalVal,
      JavaConversions,
      JavaSerializable,
      LeakingSealed,
      MutableDataStructures,
      OptionPartial,
      Recursion,
      Return,
      TryPartial,
      Var,
      While
    )

    Compile / compile / wartremoverErrors ++= errorWarts
  }
}

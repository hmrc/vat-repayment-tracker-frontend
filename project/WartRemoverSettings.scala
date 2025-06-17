import sbt.{Def, *}
import sbt.Keys.compile
import wartremover.Wart
import wartremover.Wart.*
import wartremover.WartRemover.autoImport.{wartremoverErrors, wartremoverWarnings}

object  WartRemoverSettings {

  lazy val wartRemoverWarning: Def.Setting[Seq[Wart]] = {
    val warningWarts = Seq(
      JavaSerializable,
      AsInstanceOf,
      IsInstanceOf
    )
    Compile / compile / wartremoverWarnings ++= warningWarts
  }
  lazy val wartRemoverError: Def.Setting[Seq[Wart]] = {
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

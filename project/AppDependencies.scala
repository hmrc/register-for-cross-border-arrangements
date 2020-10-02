import sbt._

object AppDependencies {
  import play.core.PlayVersion

  val compile = Seq(
    play.sbt.PlayImport.ws,
    "org.reactivemongo" %% "play2-reactivemongo"           % "0.18.6-play27",
    "uk.gov.hmrc"       %% "domain"                        % "5.10.0-play-27",
    "uk.gov.hmrc"       %% "play-conditional-form-mapping" % "1.3.0-play-26",
    "uk.gov.hmrc"       %% "bootstrap-health-play-27"      % "2.25.0",
    "uk.gov.hmrc"       %% "bootstrap-backend-play-27"     % "2.25.0"
  )

  val test = Seq(
    "org.scalatest"               %% "scalatest"             % "3.0.8",
    "org.scalatestplus.play"      %% "scalatestplus-play"    % "4.0.3",
    "org.pegdown"                 %  "pegdown"               % "1.6.0",
    "org.jsoup"                   %  "jsoup"                 % "1.12.1",
    "com.typesafe.play"           %% "play-test"             % PlayVersion.current,
    "org.mockito"                 %  "mockito-all"           % "1.10.19",
    "com.github.tomakehurst"      %  "wiremock-standalone"   % "2.25.0",
    "org.scalacheck"              %% "scalacheck"            % "1.14.3",
    "wolfendale"                  %% "scalacheck-gen-regexp" % "0.1.2"
  ).map(_ % Test)

  def apply(): Seq[ModuleID] = compile ++ test
}

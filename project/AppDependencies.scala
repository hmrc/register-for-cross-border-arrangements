import sbt._

object AppDependencies {
  import play.core.PlayVersion

  val compile = Seq(
    play.sbt.PlayImport.ws,
    "uk.gov.hmrc"       %% "domain"                        % "8.1.0-play-28",
    "uk.gov.hmrc"       %% "play-conditional-form-mapping" % "1.12.0-play-28",
    "uk.gov.hmrc"       %% "bootstrap-backend-play-28"     % "7.10.0"
  )

  val test = Seq(
    "org.scalatest"               %% "scalatest"                % "3.2.14",
    "org.scalatestplus"           %% "scalacheck-1-17"          % "3.2.14.0",
    "org.scalatestplus.play"      %% "scalatestplus-play"       % "5.1.0",
    "org.pegdown"                 %  "pegdown"                  % "1.6.0",
    "org.jsoup"                   %  "jsoup"                    % "1.15.3",
    "com.typesafe.play"           %% "play-test"                % PlayVersion.current,
    "org.mockito"                 %%  "mockito-scala"            % "1.17.12",
    "com.github.tomakehurst"      %  "wiremock-standalone"      % "2.27.2",
    "org.scalatestplus"           %% "scalatestplus-scalacheck" % "3.1.0.0-RC2",
    "wolfendale"                  %% "scalacheck-gen-regexp"    % "0.1.2",
    "com.vladsch.flexmark"        %  "flexmark-all"             % "0.64.0"
  ).map(_ % Test)

  def apply(): Seq[ModuleID] = compile ++ test
}

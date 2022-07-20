import sbt._

object AppDependencies {
  import play.core.PlayVersion

  val compile = Seq(
    play.sbt.PlayImport.ws,
    "uk.gov.hmrc"       %% "domain"                        % "6.2.0-play-28",
    "uk.gov.hmrc"       %% "play-conditional-form-mapping" % "1.9.0-play-28",
    "uk.gov.hmrc"       %% "bootstrap-health-play-28"      % "5.4.0",
    "uk.gov.hmrc"       %% "bootstrap-backend-play-28"     % "5.24.0"
  )

  val test = Seq(
    "org.scalatest"               %% "scalatest"                % "3.1.0",
    "org.scalatestplus.play"      %% "scalatestplus-play"       % "5.1.0",
    "org.pegdown"                 %  "pegdown"                  % "1.6.0",
    "org.jsoup"                   %  "jsoup"                    % "1.12.1",
    "com.typesafe.play"           %% "play-test"                % PlayVersion.current,
    "org.mockito"                 %%  "mockito-scala"            % "1.16.34",
    "com.github.tomakehurst"      %  "wiremock-standalone"      % "2.27.0",
    "org.scalatestplus"           %% "scalatestplus-scalacheck" % "3.1.0.0-RC2",
    "wolfendale"                  %% "scalacheck-gen-regexp"    % "0.1.2",
    "com.vladsch.flexmark"        %  "flexmark-all"             % "0.35.10"
  ).map(_ % Test)

  def apply(): Seq[ModuleID] = compile ++ test
}

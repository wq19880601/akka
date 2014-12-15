import NativePackagerKeys._

packageArchetype.akka_application

name := """hello-kernel"""

mainClass in Compile := Some("sample.kernel.hello.HelloKernel")

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-kernel" % "2.3.7-bin-rp-15v01p01",
  "com.typesafe.akka" %% "akka-actor" % "2.3.7-bin-rp-15v01p01"
)
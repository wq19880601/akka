import akka._
import com.typesafe.tools.mima.plugin.MimaKeys
import spray.boilerplate.BoilerplatePlugin._

AkkaBuild.defaultSettings
AkkaBuild.experimentalSettings
Formatting.formatSettings
OSGi.stream
Dependencies.stream
MimaKeys.previousArtifacts := akkaStreamAndHttpPreviousArtifacts("akka-stream").value
Boilerplate.settings

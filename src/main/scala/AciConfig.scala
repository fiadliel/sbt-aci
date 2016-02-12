package com.gilt.sbt

object AciConfig {
  def makeConfig(name: String, executableScriptName: String, dependencies: Seq[String]): String = {
    s"""
{
  "acKind": "ImageManifest",
  "acVersion": "0.7.4",
  "name": "$name",
  "app": {
    "exec": [
      "bin/${executableScriptName}"
    ],
    "user": "0",
    "group": "0",
    "workingDirectory": "/"
  },
  ${dependenciesString(dependencies)}
}
"""
  }

  def dependencyString(dependency: String): String =
    s"""
       |{
       |  "imageName": "${dependency}"
       |}
   """.stripMargin

  val dependencyStart =
    """
      |"dependencies": [
    """.stripMargin

  def dependenciesString(dependencies: Seq[String]): String =
    dependencies.map(dependencyString).mkString(dependencyStart, ",\n", "]")

}
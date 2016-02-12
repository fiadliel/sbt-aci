package com.gilt.sbt

import com.typesafe.sbt.packager.Keys._
import com.typesafe.sbt.packager.universal.Archives
import com.typesafe.sbt.packager.universal.UniversalPlugin.autoImport._
import com.typesafe.sbt.packager.archetypes.JavaAppPackaging
import sbt._
import sbt.Keys._

object Aci extends AutoPlugin {

  val Aci = config("aci") extend Universal

  object autoImport {
    val aciDependencies = settingKey[Seq[String]]("ACI Dependencies")
    val aciManifest = taskKey[File]("ACI Manifest")
  }

  import autoImport._

  override val requires = JavaAppPackaging

  override val projectSettings = inConfig(Aci)(Seq(
    name := s"${organization.value}/${normalizedName.value}",
    target := target.value / "aci",
    mappings := (renameDests((mappings in Universal).value, "rootfs") ++ Seq(aciManifest.value -> "manifest")),
    packageBin :=
      Archives.makeTarball(Archives.gzip, ".aci")(target.value, normalizedName.value, mappings.value, None),
    aciDependencies := Seq("registry-1.docker.io/fiadliel/java8-jre:latest"),
    aciManifest := {
      val manifestFile = target.value / "manifest"
      IO.write(manifestFile, AciConfig.makeConfig(name.value, executableScriptName.value, aciDependencies.value))
      manifestFile
    }
  ))

  def renameDest(originalPath: String, dest: String) =
    "%s/%s" format (dest, originalPath)

  def renameDests(from: Seq[(File, String)], dest: String) =
    for {
      (f, path) <- from
    } yield (f, renameDest(path, dest))
}

package se.vlovgr.example

import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.collection.MinSize
import eu.timepit.refined.string.MatchesRegex

package object config {
  type ApiKey = String Refined MatchesRegex[W.`"[a-zA-Z0-9]{25,40}"`.T]

  type DatabasePassword = String Refined MinSize[W.`20`.T]
}

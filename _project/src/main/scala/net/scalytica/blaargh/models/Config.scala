/**
 * Copyright(c) 2016 Knut Petter Meen, all rights reserved.
 */
package net.scalytica.blaargh.models

import org.scalajs.dom.ext.Ajax
import upickle.default._

import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

case class Author(
  name: String,
  email: String
)

case class Owner(
  name: String,
  avatar: String,
  bio: String,
  email: String,
  disqusShortname: String,
  twitter: String,
  googleAnalytics: String,
  github: String,
  linkedin: String
)

object Owner {
  val empty = Owner(
    name = "",
    avatar = "",
    bio = "",
    email = "",
    disqusShortname = "",
    twitter = "",
    googleAnalytics = "",
    github = "",
    linkedin = ""
  )
}

case class Mailto(
  from: String,
  to: String,
  subject: String,
  url: String,
  acceptCharset: String,
  resultUrl: String
)

object Mailto {
  val empty = Mailto(
    from = "",
    to = "",
    subject = "",
    url = "",
    acceptCharset = "",
    resultUrl = ""
  )
}

case class Config(
  siteTitle: String,
  authors: Seq[Author],
  owner: Owner,
  mailto: Mailto
)

object Config {

  val empty = Config(
    siteTitle = "",
    authors = Seq.empty,
    owner = Owner.empty,
    mailto = Mailto.empty
  )

  def load(): Future[Config] =
    Ajax.get(
      url = "config/config.json"
    ).map { xhr =>
      xhr.status match {
        case ok: Int if ok == 200 => read[Config](xhr.responseText)
        case _ => empty
      }
    }

}

/**
  * Copyright(c) 2016 Knut Petter Meen, all rights reserved.
  */
package net.scalytica.blaargh.pages

import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.vdom.prefix_<^._
import net.scalytica.blaargh.styles.BlaarghBootstrapCSS

import scalacss.ScalaCssReact._

object ThanksPage {

  val component = ReactComponentB[Unit]("Thanks")
    .stateless
    .render { _ =>
      <.div(BlaarghBootstrapCSS.container,
        <.div(BlaarghBootstrapCSS.container,
          <.h1("Thanks"),
          <.p("The message has been received")
        )
      )
    }
    .build

  def apply() = component()
}

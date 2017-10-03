/**
  * Copyright(c) 2017 Jarl André Hübenthal, all rights reserved.
  */
package net.scalytica.blaargh.pages

import japgolly.scalajs.react.{BackendScope, ReactComponentB}
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._

object ContactPage {

  class Backend($: BackendScope[Unit, Unit]) {

    def render() = {
      <.div(
        <.h3("Contact me")
      )
    }
  }

  private val component = ReactComponentB[Unit]("About")
    .renderBackend[Backend]
    .build

  def apply() = component()

}
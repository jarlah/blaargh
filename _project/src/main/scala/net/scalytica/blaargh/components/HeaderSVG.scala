/**
 * Copyright(c) 2016 Knut Petter Meen, all rights reserved.
 */
package net.scalytica.blaargh.components

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.svg.prefix_<^._
import net.scalytica.blaargh.models.Config

import scala.scalajs.niocharset.StandardCharsets
import scalacss.Defaults._
import scalacss.ScalaCssReact._

object HeaderSVG {

  object Styles extends StyleSheet.Inline {

    import dsl._

    val svg = style(
      width(100.%%),
      height(5.em),
      unsafeChild("text")(
        svgTextAnchor := "middle"
      ),
      unsafeChild("#alpha")(
        svgFill := rgb(190, 190, 190)
      ),
      unsafeChild("#title")(
        letterSpacing :=! "-2px",
        fontSize(6.em),
        fontWeight :=! "800"
      ),
      unsafeChild("#subtitle")(
        letterSpacing(6.px),
        fontSize(1.2.em),
        textTransform.uppercase
      ),
      unsafeChild("#base")(
        svgFill := "black",
        mask := "url(#mask)"
      )
    )
  }

  val component = ReactComponentB[Config]("HeaderSVG")
    .render { $ =>
      // Calculate binary values for site owner's initials
      val subTitle = $.props.owner.name.filter(_.isUpper).getBytes(StandardCharsets.UTF_8).map(b => Integer.toBinaryString(b: Int))
      <.svg(Styles.svg,
        <.defs(
          <.maskTag(^.id := "mask", ^.x := "0", ^.y := "0", ^.width := "100%", ^.height := "100%",
            <.rect(^.id := "alpha", ^.x := "0", ^.y := "0", ^.width := "100%", ^.height := "100%")
          )
        ),
        <.rect(^.id := "base", ^.x := "0", ^.y := "0", ^.width := "100%", ^.height := "100%")
      )
    }
    .build

  def apply(conf: Config) = component(conf)

}

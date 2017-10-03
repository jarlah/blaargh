/**
  * Copyright(c) 2017 Jarl André Hübenthal, all rights reserved.
  */
package net.scalytica.blaargh.pages


import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import net.scalytica.blaargh.models.Config
import net.scalytica.blaargh.styles.BlaarghBootstrapCSS

import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scalacss.ScalaCssReact._

object ContactPage {

  case class Props(siteConf: Future[Config])

  case class State(
    conf: Config,
    name: Option[String],
    email: Option[String],
    message: Option[String]
  )

  class Backend($: BackendScope[Props, State]) {

    def init: Callback = {
      $.props.map(p =>
        Callback.future[Unit] {
          for {
            config <- p.siteConf
          } yield {
            $.modState(_.copy(conf = config))
          }
        }.runNow()
      )
    }

    def onChangeName(e: ReactKeyboardEventI): Callback = {
      val newValue = e.target.value
      $.modState(_.copy(name = Some(newValue)))
    }

    def validateName(name: String): Boolean = name.length >= 3

    def onChangeEmail(e: ReactKeyboardEventI): Callback = {
      val newValue = e.target.value
      $.modState(_.copy(email = Some(newValue)))
    }

    private val emailRegex = """^[a-zA-Z0-9\.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$""".r
    def validateEmail(email: String): Boolean = email match{
      case null                                           => false
      case e if e.trim.isEmpty                            => false
      case e if emailRegex.findFirstMatchIn(e).isDefined  => true
      case _                                              => false
    }

    def onChangeMessage(e: ReactKeyboardEventI): Callback = {
      val newValue = e.target.value
      $.modState(_.copy(message = Some(newValue)))
    }

    def validateMessage(message: String): Boolean = message.length >= 10

    def onSubmit(valid: Boolean) = (event: ReactEvent) => {
      if (!valid) {
        event.preventDefault()
      }
      Callback.empty
    }

    def render(state: State) = {
      val isNameValid = state.name.exists(validateName)
      val isEmailValid = state.email.exists(validateEmail)
      val isMessageValid = state.message.exists(validateMessage)
      val isFormValid = isNameValid && isEmailValid && isMessageValid
      <.div(BlaarghBootstrapCSS.container,
        <.div(BlaarghBootstrapCSS.container,
          <.h1("Contact Me"),
          <.form(
            ^.onSubmit ==> onSubmit(isFormValid),
            ^.className := "form-horizontal",
            ^.action := state.conf.mailto.url,
            ^.acceptCharset := state.conf.mailto.acceptCharset,
            <.input(^.tpe := "hidden", ^.name := "_from", ^.value := state.conf.mailto.from),
            <.input(^.tpe := "hidden", ^.name := "_to", ^.value := state.conf.mailto.to),
            <.input(^.tpe := "hidden", ^.name := "_subject", ^.value := state.conf.mailto.subject),
            <.input(^.tpe := "hidden", ^.name := "_resulturl", ^.value := state.conf.mailto.resultUrl),
            <.div(^.className := s"form-group ${if(!isNameValid) "has-error" else ""}",
              <.label(^.className := "control-label col-sm-2", ^.htmlFor := "name", "Name"),
              <.div(^.className := "col-sm-10",
                <.input(
                  ^.className := "form-control",
                  ^.id := "name",
                  ^.name := "name",
                  ^.onChange ==> onChangeName,
                  ^.value := state.name.getOrElse("")
                )
              ),
              if (!isNameValid)
                <.span(
                  ^.className := "col-sm-offset-2 col-sm-10 form-control-static help-block",
                  "Name must be 3 or more characters"
                )
              else EmptyTag
            ),
            <.div(^.className := s"form-group ${if(!isEmailValid) "has-error" else ""}",
              <.label(^.className := "control-label col-sm-2", ^.htmlFor := "email", "Email"),
              <.div(^.className := "col-sm-10",
                <.input(
                  ^.className := "form-control",
                  ^.id := "email",
                  ^.name := "email",
                  ^.tpe := "email",
                  ^.onChange ==> onChangeEmail,
                  ^.value := state.email.getOrElse("")
                )
              ),
              if (!isEmailValid)
                <.span(
                  ^.className := "col-sm-offset-2 col-sm-10 form-control-static help-block",
                  "Enter your email"
                )
              else EmptyTag
            ),
            <.div(^.className := s"form-group ${if(!isMessageValid) "has-error" else ""}",
              <.label(^.className := "control-label col-sm-2", ^.htmlFor := "message", "Message"),
              <.div(^.className := "col-sm-10",
                <.textarea(
                  ^.className := "form-control",
                  ^.id := "message",
                  ^.name := "message",
                  ^.onChange ==> onChangeMessage,
                  ^.value := state.message.getOrElse(""),
                  ^.rows := 10
                )
              ),
              if (!isMessageValid)
                <.span(
                  ^.className := "col-sm-offset-2 col-sm-10 form-control-static help-block",
                  "Message must be 10 or more characters"
                )
              else EmptyTag
            ),
            <.div(^.className := "form-group",
              <.div(^.className := "col-sm-offset-2 col-sm-10",
                <.button(
                  ^.className := "btn btn-default",
                  ^.tpe := "submit",
                  ^.disabled := !isFormValid,
                  ^.onClick ==> onSubmit(isFormValid),
                  "Send"
                )
              )
            )
          )
        )
      )
    }
  }

  private val component = ReactComponentB[Props]("Contact")
    .initialState_P(p => State(Config.empty, None, None, None))
    .renderBackend[Backend]
    .componentWillMount(_.backend.init)
    .build

  def apply(config: Future[Config]) = component(Props(config))

}
/**
 * Copyright (C) 2009-2016 Typesafe Inc. <http://www.typesafe.com>
 */
package docs.http.scaladsl

import akka.actor.ActorSystem
import org.scalatest.{ Matchers, WordSpec }

class WebSocketClientExampleSpec extends WordSpec with Matchers {

  "single-WebSocket-request-example" in {
    pending // compile-time only test
    //#single-websocket-request
    import akka.Done
    import akka.http.scaladsl.Http
    import akka.stream.ActorMaterializer
    import akka.stream.scaladsl._
    import akka.http.scaladsl.model._
    import akka.http.scaladsl.model.ws._

    import scala.concurrent.Future

    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()
    import system.dispatcher

    // future done is the materialized value of Sink.foreach,
    // emitted when the stream completes
    val flow: Flow[Message, Message, Future[Done]] = Flow.fromSinkAndSourceMat(
      Sink.foreach[Message] {
        case message: TextMessage.Strict =>
          println(message.text)
      },
      // send this as a message over the WebSocket
      Source.single(TextMessage("hello world!")))(Keep.left)

    // second value NotUsed in this case
    val (upgradeResponse: Future[WebsocketUpgradeResponse], closed: Future[Done]) =
      Http().singleWebsocketRequest(WebsocketRequest("ws://echo.websocket.org"), flow)

    // just like a regular http request we can get 404 NotFound etc.
    // that will be available from upgrade.response
    val connected = upgradeResponse.flatMap { upgrade =>
      if (upgrade.response.status == StatusCodes.OK) {
        Future.successful(Done)
      } else {
        throw new RuntimeException(s"Connection failed: ${upgrade.response.status}")
      }
    }

    // in a real application you would not side effect here
    connected.onComplete(println)
    closed.foreach(_ => println("closed"))

    //#single-websocket-request
  }

  "webSocketClient-flow-example" in {
    pending // compile-time only test

    //#websocket-client-flow
    import akka.Done
    import akka.http.scaladsl.Http
    import akka.stream.ActorMaterializer
    import akka.stream.scaladsl._
    import akka.http.scaladsl.model._
    import akka.http.scaladsl.model.ws._

    import scala.concurrent.Future

    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()
    import system.dispatcher

    // future done is the materialized value of Sink.foreach,
    // emitted when the stream completes
    val incoming: Sink[Message, Future[Done]] =
      Sink.foreach[Message] {
        case message: TextMessage.Strict =>
          println(message.text)
      }

    // send this as a message over the WebSocket
    val outgoing = Source.single(TextMessage("hello world!"))

    // flow to use (note: not re-usable!)
    val webSocketFlow = Http().websocketClientFlow(WebsocketRequest("ws://echo.websocket.org"))

    val (upgradeResponse: Future[WebsocketUpgradeResponse], closed: Future[Done]) =
      outgoing
        .via(webSocketFlow)
        .toMat(incoming)(Keep.both)
        .run()

    // just like a regular http request we can get 404 NotFound etc.
    // that will be available from upgrade.response
    val connected = upgradeResponse.flatMap { upgrade =>
      if (upgrade.response.status == StatusCodes.OK) {
        Future.successful(Done)
      } else {
        throw new RuntimeException(s"Connection failed: ${upgrade.response.status}")
      }
    }

    // in a real application you would not side effect here
    connected.onComplete(println)
    closed.foreach(_ => println("closed"))

    //#websocket-client-flow
  }

}

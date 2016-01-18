.. _client-side-websocket-support:

Client-Side WebSocket Support
=============================

Client side WebSocket support is available through ``Http().singleWebSocketRequest()`` ,
``Http().webSocketClientFlow()`` and ``webSocketClientLayer``. As a WebSocket consists of two flows of
messages of which either might happen first, rather than the request and response of a regular HTTP request,
WebSocket requests always works with ``Flow[Message, Message, Mat]` where the messages will pass through.

A WebSocket request starts with a regular HTTP request which contains an ``Upgrade`` header, so in
addition to the flow of messages there also is an initial response from the server, this is modelled
with ``WebSocketUpgradeResponse``.


Message
-------
Messages sent and received over a WebSocket can be either ``TextMessage``s or ``BinaryMessage``s and each
of those has two subtypes ``Strict`` or ``Streaming``. Most commonly messages will probably be ``Strict``.
``Streaming`` will be used when the WebSocket server sends multi fragment messages (first fragment not marked
as final fragment as described in `rfc 6455 section 5.2`__).

When ``Streaming`` the data is provided as a ``Source[ByteString, NotUsed]`` for binary and
``Source[String, NotUsed]`` for text messages.

__ https://tools.ietf.org/html/rfc6455#section-5.2


TODO use cases for each of the options below, why choose either?


singleWebSocketRequest
----------------------
``singleWebSocketRequest`` takes a `WebSocketRequest` and a flow it will connect to the source and
sink of the WebSocket connection, it will trigger the request right away and returns a tuple containing the
`Future[WebsocketUpgradeResponse]` and the materialized value from the provided flow.

The future will succeed when the WebSocket connection has been established or fail if the connection fails.

Simple example sending a message and printing any incoming message:

.. includecode:: ../../code/docs/http/scaladsl/WebSocketClientExampleSpec.scala
   :include: single-websocket-request


webSocketClientFlow
-------------------
``webSocketClientFlow`` takes a request, and returns a ``Flow[Message, Message, Future[WebSocketUpgradeResponse]]``.
The materialized value will complete once stream has been run and the WebSocket connection succeeded or failed.

Simple example sending a message and printing any incoming message:


.. includecode:: ../../code/docs/http/scaladsl/WebSocketClientExampleSpec.scala
   :include: websocket-client-flow


webSocketClientLayer
--------------------
``webSocketClientLayer`` is a more advanced API that returns a ``WebSocketClientLayer`` allowing for layering with
Bidi-something... TODO



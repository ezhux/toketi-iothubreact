// Copyright (c) Microsoft. All rights reserved.

package com.microsoft.azure.iot.iothubreact

import akka.stream.stage.{GraphStage, GraphStageLogic, InHandler, OutHandler}
import akka.stream.{Attributes, FlowShape, Inlet, Outlet}

private[iothubreact] class StreamManager[A]
  extends GraphStage[FlowShape[A, A]] with Logger {

  private[this] val in          = Inlet[A]("StreamCanceller.Flow.in")
  private[this] val out         = Outlet[A]("StreamCanceller.Flow.out")
  private[this] var closeSignal = false

  override val shape = FlowShape.of(in, out)

  def close(): Unit = closeSignal = true

  override def createLogic(attr: Attributes): GraphStageLogic = {
    new GraphStageLogic(shape) {

      setHandler(in, new InHandler {
        override def onPush(): Unit = push(out, grab(in))
      })

      setHandler(out, new OutHandler {
        override def onPull(): Unit = {
          if (closeSignal) {
            log.info("Tadas: cancelling stream")
            cancel(in)
          } else {
            pull(in)
          }
        }

        override def onDownstreamFinish(): Unit = {
          log.info("Tadas: onDownstreamFinish")
          cancel(in)
          super.onDownstreamFinish()
        }

      })
    }
  }
}

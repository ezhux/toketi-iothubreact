// Copyright (c) Microsoft. All rights reserved.

package com.microsoft.azure.iot.iothubreact.checkpointing.backends

import com.microsoft.azure.iot.iothubreact.checkpointing.{CPConfiguration, ICPConfiguration}

trait CheckpointBackend {

  def checkpointNamespace(implicit config: ICPConfiguration = new CPConfiguration): String = config.storageNamespace

  /** Read the offset of the last record processed for the given partition
    *
    * @param partition IoT hub partition number
    *
    * @return Offset of the last record (already) processed
    */
  def readOffset(partition: Int): String

  /** Store the offset for the given IoT hub partition
    *
    * @param partition IoT hub partition number
    * @param offset    IoT hub partition offset
    */
  def writeOffset(partition: Int, offset: String): Unit
}

// THIS FILE IS GENERATED! DO NOT EDIT IT MANUALLY!
@file:Suppress("PackageDirectoryMismatch", "unused", "FunctionName", "RedundantModalityModifier",
    "UNCHECKED_CAST", "JoinDeclarationAndAssignment", "USELESS_CAST",
    "RemoveRedundantQualifierName", "NOTHING_TO_INLINE", "NON_FINAL_MEMBER_IN_OBJECT",
    "RedundantVisibilityModifier", "RedundantUnitReturnType", "MemberVisibilityCanBePrivate")

package godot

import godot.`annotation`.GodotBaseType
import godot.core.PackedInt64Array
import godot.core.VariantArray
import godot.core.VariantType.ARRAY
import godot.core.VariantType.DOUBLE
import godot.core.VariantType.NIL
import godot.core.VariantType.PACKED_INT_64_ARRAY
import godot.core.memory.TransferContext
import kotlin.Any
import kotlin.Boolean
import kotlin.Double
import kotlin.Float
import kotlin.Int
import kotlin.Suppress

@GodotBaseType
public open class OggPacketSequence : Resource() {
  public var packetData: VariantArray<VariantArray<Any?>>
    get() {
      TransferContext.writeArguments()
      TransferContext.callMethod(rawPtr, ENGINEMETHOD_ENGINECLASS_OGGPACKETSEQUENCE_GET_PACKET_DATA,
          ARRAY)
      return (TransferContext.readReturnValue(ARRAY, false) as VariantArray<VariantArray<Any?>>)
    }
    set(`value`) {
      TransferContext.writeArguments(ARRAY to value)
      TransferContext.callMethod(rawPtr, ENGINEMETHOD_ENGINECLASS_OGGPACKETSEQUENCE_SET_PACKET_DATA,
          NIL)
    }

  public var granulePositions: PackedInt64Array
    get() {
      TransferContext.writeArguments()
      TransferContext.callMethod(rawPtr,
          ENGINEMETHOD_ENGINECLASS_OGGPACKETSEQUENCE_GET_PACKET_GRANULE_POSITIONS,
          PACKED_INT_64_ARRAY)
      return (TransferContext.readReturnValue(PACKED_INT_64_ARRAY, false) as PackedInt64Array)
    }
    set(`value`) {
      TransferContext.writeArguments(PACKED_INT_64_ARRAY to value)
      TransferContext.callMethod(rawPtr,
          ENGINEMETHOD_ENGINECLASS_OGGPACKETSEQUENCE_SET_PACKET_GRANULE_POSITIONS, NIL)
    }

  public var samplingRate: Float
    get() {
      TransferContext.writeArguments()
      TransferContext.callMethod(rawPtr,
          ENGINEMETHOD_ENGINECLASS_OGGPACKETSEQUENCE_GET_SAMPLING_RATE, DOUBLE)
      return (TransferContext.readReturnValue(DOUBLE, false) as Double).toFloat()
    }
    set(`value`) {
      TransferContext.writeArguments(DOUBLE to value.toDouble())
      TransferContext.callMethod(rawPtr,
          ENGINEMETHOD_ENGINECLASS_OGGPACKETSEQUENCE_SET_SAMPLING_RATE, NIL)
    }

  public override fun new(scriptIndex: Int): Boolean {
    callConstructor(ENGINECLASS_OGGPACKETSEQUENCE, scriptIndex)
    return true
  }

  public fun getLength(): Float {
    TransferContext.writeArguments()
    TransferContext.callMethod(rawPtr, ENGINEMETHOD_ENGINECLASS_OGGPACKETSEQUENCE_GET_LENGTH,
        DOUBLE)
    return (TransferContext.readReturnValue(DOUBLE, false) as Double).toFloat()
  }

  public companion object
}
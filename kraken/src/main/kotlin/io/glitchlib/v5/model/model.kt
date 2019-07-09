package io.glitchlib.v5.model

import io.glitchlib.GlitchException

class MaxUploadSizeExceededException(val maxUploadSize: Long) :
        GlitchException("Max uploaded size exceeded. Max file size : $maxUploadSize Bytes")
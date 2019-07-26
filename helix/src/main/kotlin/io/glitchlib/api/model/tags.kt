package io.glitchlib.api.model

import com.google.gson.annotations.SerializedName
import io.glitchlib.model.IDObject
import java.util.Locale
import java.util.UUID

data class Tag(
    @SerializedName("tag_id")
    override val id: UUID,
    val isAuto: Boolean,
    val localizationNames: Localized,
    val localizationDescriptions: Localized
) : IDObject<UUID> {

    fun getName(locale: Locale = Locale.forLanguageTag("en-us")) = localizationNames.getValue(locale)
    fun getDescription(locale: Locale = Locale.forLanguageTag("en-us")) = localizationNames.getValue(locale)

    fun getName(locale: String = "en-us") = getName(Locale.forLanguageTag(locale))
    fun getDescription(locale: String = "en-us") = getDescription(Locale.forLanguageTag(locale))
}

typealias Localized = Map<Locale, String>
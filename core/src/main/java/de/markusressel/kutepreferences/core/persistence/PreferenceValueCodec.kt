package de.markusressel.kutepreferences.core.persistence

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonParser
import com.google.gson.JsonPrimitive
import de.markusressel.kutepreferences.core.preference.number.range.RangePersistenceModel

/**
 * Converts preference values that a store cannot represent natively to and from JSON.
 *
 * The JSON format is part of the persisted data of apps, so it must stay compatible.
 */
internal object PreferenceValueCodec {

    private const val TAG = "KutePreferences"
    private const val RANGE_MIN = "min"
    private const val RANGE_MAX = "max"

    private val gson = Gson()

    fun encode(value: Any): String = when (value) {
        is Set<*> -> gson.toJson(value.toList())
        is RangePersistenceModel<*> -> JsonObject().apply {
            addProperty(RANGE_MIN, value.min)
            addProperty(RANGE_MAX, value.max)
        }.toString()

        else -> gson.toJson(value, value.javaClass)
    }

    /**
     * @param key the preference key, only used for logging. The value is not logged, since it might be sensitive.
     * @return the decoded value, or [defaultValue] if [json] cannot be decoded into the type of [defaultValue]
     */
    fun <T : Any> decodeOrDefault(key: String, json: String, defaultValue: T): T {
        return try {
            decode(json, defaultValue)
        } catch (ex: JsonParseException) {
            logDecodeFailure(key, ex)
            defaultValue
        } catch (ex: IllegalStateException) {
            logDecodeFailure(key, ex)
            defaultValue
        } catch (ex: UnsupportedOperationException) {
            logDecodeFailure(key, ex)
            defaultValue
        } catch (ex: NumberFormatException) {
            logDecodeFailure(key, ex)
            defaultValue
        }
    }

    private fun logDecodeFailure(key: String, ex: Exception) {
        Log.w(TAG, "Could not decode the value of preference '$key', using its default", ex)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> decode(json: String, defaultValue: T): T {
        val decoded: Any? = when (defaultValue) {
            is Set<*> -> decodeSet(JsonParser.parseString(json), defaultValue)
            is RangePersistenceModel<*> -> decodeRange(JsonParser.parseString(json), defaultValue)
            else -> gson.fromJson(json, defaultValue.javaClass)
        }
        return (decoded ?: defaultValue) as T
    }

    private fun decodeSet(json: JsonElement, defaultValue: Set<*>): Set<Any> {
        val sample = defaultValue.firstOrNull()
        return json.asJsonArray.mapTo(LinkedHashSet()) { element ->
            when (sample) {
                null -> decodeWithoutSample(element)
                else -> gson.fromJson(element, sample.javaClass)
            }
        }
    }

    /**
     * An empty default value does not reveal the element type of a set, so the JSON type is used.
     */
    private fun decodeWithoutSample(element: JsonElement): Any = when {
        element.isJsonPrimitive -> decodePrimitive(element.asJsonPrimitive)
        else -> element.toString()
    }

    private fun decodePrimitive(primitive: JsonPrimitive): Any = when {
        primitive.isBoolean -> primitive.asBoolean
        primitive.isNumber -> primitive.asDouble
        else -> primitive.asString
    }

    private fun decodeRange(json: JsonElement, defaultValue: RangePersistenceModel<*>): RangePersistenceModel<*> {
        val range = json.asJsonObject
        return RangePersistenceModel(
            min = decodeNumber(range.get(RANGE_MIN), defaultValue.min),
            max = decodeNumber(range.get(RANGE_MAX), defaultValue.max),
        )
    }

    private fun decodeNumber(json: JsonElement, sample: Number): Number = when (sample) {
        is Float -> json.asFloat
        is Double -> json.asDouble
        is Int -> json.asInt
        is Long -> json.asLong
        is Short -> json.asShort
        is Byte -> json.asByte
        else -> json.asNumber
    }

}

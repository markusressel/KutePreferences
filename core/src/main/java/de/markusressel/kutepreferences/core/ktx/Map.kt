package de.markusressel.kutepreferences.library.ktx

import java.util.*


/**
 * Map.forEach as an extension function (to be able to use it on API Levels < 24
 */
public fun <K : Any, V : Any> Map<K, V>.forEachCompat(action: ((k: K, v: V) -> Unit)) {
    for (entry in entries) {
        try {
            action(entry.key, entry.value)
        } catch (ise: IllegalStateException) {
            // this usually means the entry is no longer in the map.
            throw ConcurrentModificationException(ise)
        }
    }
}
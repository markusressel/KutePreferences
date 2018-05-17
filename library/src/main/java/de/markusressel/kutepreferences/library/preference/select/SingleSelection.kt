package de.markusressel.kutepreferences.library.preference.select

import java.io.Serializable

data class SingleSelection<Type : Serializable>(val selectedValue: Type) : Serializable
package de.markusressel.kutepreferences.core.tree

import de.markusressel.kutepreferences.core.KutePreferenceListItem

class Node(val parent: Node?,
           val item: KutePreferenceListItem?,
           val children: MutableList<Node> = mutableListOf())
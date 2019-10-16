package com.mygdx.game.ext

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Array
import com.mygdx.game.base.BaseActor

// Extension functions

operator fun Stage.plusAssign(actor: BaseActor) {
    addActor(actor)
}

operator fun <T> Array<in T>.plusAssign(element: T) {
    this.add(element)
}

operator fun <T> Array<in T>.plusAssign(elements: Iterable<T>) {
    for (element in elements)
       this += element
}

operator fun <T> MutableCollection<T>.plusAssign(elements: Iterable<T>) {
    for (element in elements)
        this += element
}

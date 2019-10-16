package com.mygdx.game.entities

import com.badlogic.gdx.scenes.scene2d.Action
import com.mygdx.game.base.BaseActor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions

class Shield(x: Float, y: Float, s: Stage ) : BaseActor(x, y, s) {

    init {
        loadTexture("shields.png")

        val pulse : Action = Actions.sequence(
                Actions.scaleTo(1.05f, 1.05f, 1f),
                Actions.scaleTo(0.95f,0.95f, 1f)
        )

        addAction(Actions.forever(pulse))
    }
}
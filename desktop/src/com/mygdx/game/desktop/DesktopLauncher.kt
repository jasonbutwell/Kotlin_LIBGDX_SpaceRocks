package com.mygdx.game.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.mygdx.game.SpaceGame
import com.mygdx.game.config.Settings

fun main(arg: Array<String>) {
    val config = LwjglApplicationConfiguration()

    config.width = Settings.screenWidth
    config.height = Settings.screenHeight
    config.title = Settings.title

    LwjglApplication(SpaceGame(), config)
}

package com.mygdx.game.base

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.*
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector
import java.lang.Class.*
import kotlin.math.PI
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Group
import com.mygdx.game.ext.plusAssign

open class BaseActor(x: Float, y: Float, stage: Stage, scale : Float = 1f) : Group() {

    private lateinit var animation: Animation<TextureRegion>
    private lateinit var boundaryPolygon: Polygon

    private var elapsedTime = 0f
    private var animationPaused = false

    private var velocityVec = Vector2(0F, 0F)
    private var accelerationVec = Vector2(0f, 0f)

    var acceleration = 0F
    var maxSpeed = 1000f
    var deceleration = 0f

    init {
        setScale(scale)
        setPosition(x, y)
        stage += this

        //setDebug(true)
    }

    fun wrapAroundWorld() {

        if (x + width < 0)
            x = worldBounds.width   // Object goes outside left side

        if( x > worldBounds.width)  // Object goes outside right side
            x = -width

        if (y + height < 0)
            y = worldBounds.height  // Object goes below bottom

        if (y > worldBounds.height) // Object goes above top
            y = -height
    }

    fun boundToWorld() {

        // check left edge
        if (x < 0) x = 0f

        // check right edge
        if (x + width > worldBounds.width) x = worldBounds.width - width

        // check bottom edge
        if (y < 0) y = 0f

        // check top edge
        if (y + height > worldBounds.height) y = worldBounds.height - height
    }

    fun centerAtPosition(x: Float, y: Float) {
        setPosition(x - width / 2, y - height / 2)
    }

    fun centerAtActor(other: BaseActor) {
        centerAtPosition(other.x + other.width / 2,other.y + other.height / 2)
    }

    fun setOpacity(opacity: Float) {
        this.color.a = opacity
    }

    fun overlaps(other: BaseActor): Boolean {
        val poly1: Polygon = getBoundaryPolygon()
        val poly2: Polygon = other.getBoundaryPolygon()

        // Initial test to improve performance
        if (!poly1.boundingRectangle.overlaps(poly2.boundingRectangle)) return false

        return Intersector.overlapConvexPolygons(poly1, poly2)
    }

    fun preventOverlap(other: BaseActor): Vector2? {
        val poly1 = getBoundaryPolygon()
        val poly2 = other.getBoundaryPolygon()

        // initial test to improve performance
        if (!poly1.boundingRectangle.overlaps(poly2.boundingRectangle))
            return null

        val mtv = MinimumTranslationVector()

        if (!Intersector.overlapConvexPolygons(poly1, poly2, mtv))
            return null

        moveBy(mtv.normal.x * mtv.depth, mtv.normal.y * mtv.depth)

        return mtv.normal
    }

    private fun setBoundaryRectangle() {
        val w = width
        val h = height
        val vertices = floatArrayOf(0f, 0f, w, 0f, w, h, 0f, h)
        boundaryPolygon = Polygon(vertices)
    }

    private fun getBoundaryPolygon(): Polygon {

        boundaryPolygon.setPosition(x, y)
        boundaryPolygon.setOrigin(originX, originY)
        boundaryPolygon.rotation = rotation
        boundaryPolygon.setScale(scaleX, scaleY)

        return boundaryPolygon
    }

    fun setBoundaryPolygon(numSides: Int) {

        val w = width
        val h = height

        val vertices = FloatArray(2 * numSides)

        for (i in 0 until numSides) {
            val angle = i * (2 * PI) / numSides
            vertices[2 * i] = w / 2 * MathUtils.cos(angle.toFloat()) + w / 2             // x-coordinate
            vertices[2 * i + 1] = h / 2 * MathUtils.sin(angle.toFloat()) + h / 2         // y-coordinate
        }

        boundaryPolygon = Polygon(vertices)
    }

    override fun act(dt: Float) {
        super.act(dt)

        if (!animationPaused)
            elapsedTime += dt
    }

    fun setMotionAngle(angle: Float) {
        velocityVec.setAngle(angle)
    }

    fun setSpeed(speed: Float) {
        if (velocityVec.len() == 0f)
            velocityVec.set(speed, 0f)
        else
            velocityVec.setLength(speed)
    }

    fun getSpeed() = velocityVec.len()
    fun getMotionAngle() = velocityVec.angle()
    fun isMoving() = getSpeed() > 0

    fun applyPhysics(dt: Float) {

        // apply acceleration
        velocityVec.add(accelerationVec.x * dt, accelerationVec.y * dt)

        var speed = getSpeed()

        // decrease speed (decelerate) when not accelerating
        if (accelerationVec.len() == 0f)
            speed -= deceleration * dt

        // keep speed within set bounds
        speed = MathUtils.clamp(speed, 0f, maxSpeed)

        // update velocity
        setSpeed(speed)

        // apply velocity
        moveBy(velocityVec.x * dt, velocityVec.y * dt)

        // reset acceleration
        accelerationVec.set(0f, 0f)
    }

    fun accelerateAtAngle(angle: Float) {
        accelerationVec.add(Vector2(acceleration, 0f).setAngle((angle)))
    }

    fun accelerateForward() {
        accelerateAtAngle(rotation)
    }

    private fun loadAnimationFromFiles(filenames: Array<String>, frameDuration: Float, loop: Boolean): Animation<TextureRegion> {

        val textureArray: Array<TextureRegion> = Array()

        filenames.forEach {
            val texture = Texture(Gdx.files.internal(it))
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
            textureArray += TextureRegion(texture)
        }

        val anim: Animation<TextureRegion> = Animation(frameDuration, textureArray)

        if (loop)
            anim.setPlayMode(Animation.PlayMode.LOOP)
        else
            anim.setPlayMode(Animation.PlayMode.NORMAL)

        setAnimation(anim)

        return anim
    }

    fun loadAnimationFromSheet(fileName: String, rows: Int, cols: Int, frameDuration: Float, loop: Boolean): Animation<TextureRegion> {

        val textureArray = Array<TextureRegion>()
        val texture = Texture(Gdx.files.internal(fileName), true)

        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)

        val temp = TextureRegion.split(texture, texture.width / cols, texture.height / rows)

        for (r in 0 until rows)
            for (c in 0 until cols)
                textureArray += temp[r][c]

        val anim: Animation<TextureRegion> = Animation(frameDuration, textureArray)

        if (loop)
            anim.setPlayMode(Animation.PlayMode.LOOP)
        else
            anim.setPlayMode(Animation.PlayMode.NORMAL)

        setAnimation(anim)

        return anim
    }

    private fun setAnimation(anim: Animation<TextureRegion>) {

        animation = anim

        val texture: TextureRegion = anim.getKeyFrame(0f)
        val width = texture.regionWidth
        val height = texture.regionHeight

        setSize(width.toFloat(), height.toFloat())
        setOrigin((width / 2).toFloat(), (height / 2).toFloat())

        setBoundaryRectangle()
    }

    fun loadTexture(fileName: String): Animation<TextureRegion> {

        val fileNames = Array<String>(1)
        fileNames += fileName

        return loadAnimationFromFiles(fileNames, 1f, true)
    }

    fun isAnimationFinished() = animation.isAnimationFinished(elapsedTime)

    fun setAnimationPaused(pauseState: Boolean) {
        animationPaused = pauseState
    }

    override fun drawDebug(shapes: ShapeRenderer) {
        super.drawDebug(shapes)

        shapes.setColor(Color.RED)
        shapes.polygon(boundaryPolygon.transformedVertices)
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {

        val c: Color = color

        batch?.setColor(c.r, c.g, c.b, c.a)

        if (isVisible)
            batch?.draw(animation.getKeyFrame(elapsedTime),
                    x, y, originX, originY,
                    width, height, scaleX, scaleY, rotation)

        super.draw(batch, parentAlpha)
    }

    fun alignCamera() {
        val cam = stage.camera
        val v = stage.viewport

        // center camera on actor
        cam.position.set(this.x + originX, this.y + originY, 0f)

        // bound camera to layout
        cam.position.x = MathUtils.clamp(cam.position.x,
                cam.viewportWidth / 2, worldBounds.width - cam.viewportWidth / 2)

        cam.position.y = MathUtils.clamp(cam.position.y,
                cam.viewportHeight / 2, worldBounds.height - cam.viewportHeight / 2)

        cam.update()
    }

    companion object {

        lateinit var worldBounds: Rectangle

        fun getList(stage: Stage, className: String): Array<BaseActor> {

            val subPackage = ".base"
            val packageName = "entities"

            val list = Array<BaseActor>()
            val fullClassName = ("${BaseActor::class.java.`package`.name}.${packageName}.${className}")
                    .toString()
                    .replace(subPackage,"")

            lateinit var theClass: Class<*>

            try {
                theClass = forName(fullClassName)
            } catch (error: Exception) {
                error.printStackTrace()
            }

            stage.actors.forEach {
                if (theClass.isInstance(it))
                    list += it as BaseActor
            }

            return list
        }

        fun count(stage: Stage, className: String) = getList(stage, className).size

        fun setWorldBounds(ba: BaseActor) {
            setWorldBounds(ba.width, ba.height)
        }

        fun setWorldBounds(width: Float, height: Float) {
            worldBounds = Rectangle(0f, 0f, width, height)
        }
    }
}

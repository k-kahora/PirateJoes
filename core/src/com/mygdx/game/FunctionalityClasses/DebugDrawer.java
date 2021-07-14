package com.mygdx.game.FunctionalityClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ShortArray;


public class DebugDrawer {

    private static PolygonSpriteBatch pgsb = new PolygonSpriteBatch();
    private static PolygonSprite polySprite;



    public static void initPolygon(Polygon polygon) {

        Pixmap pix;
        Texture texture;
        TextureRegion textureRegion;
        EarClippingTriangulator triangulator;



        pix = new Pixmap(1,1, Pixmap.Format.RGBA8888);
        pix.setColor(1, 0, 0, 1);
        pix.fill();
        texture = new Texture(pix);
        textureRegion = new TextureRegion(texture);

        triangulator= new EarClippingTriangulator();

        ShortArray triangles = triangulator.computeTriangles(polygon.getVertices());
        PolygonRegion region = new PolygonRegion(textureRegion, polygon.getVertices(), triangles.toArray());
        polySprite = new PolygonSprite(region);

    }


    private static ShapeRenderer debugRenderer = new ShapeRenderer();

    public static void DrawDebugLine(Vector2 start, Vector2 end, int lineWidth, Color color, Matrix4 projectionMatrix)
    {
        Gdx.gl.glLineWidth(lineWidth);
        debugRenderer.setProjectionMatrix(projectionMatrix);
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);

        debugRenderer.setColor(color);
        debugRenderer.line(start, end);
        debugRenderer.end();
        Gdx.gl.glLineWidth(1);
    }
    public static void DrawDebugRectangle(float x, float y, float width, float height, Color color, Matrix4 projectionMatrix) {

        debugRenderer.setProjectionMatrix(projectionMatrix);
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        debugRenderer.setColor(color);
        debugRenderer.rect(x,y,width,height);
        debugRenderer.end();
        Gdx.gl.glLineWidth(1);

    }

    public static void DrawDebugCircle(Vector2 start, float radius, int lineWidth, Color color, Matrix4 projectionMatrix)
    {
        Gdx.gl.glLineWidth(lineWidth);
        debugRenderer.setProjectionMatrix(projectionMatrix);
        debugRenderer.begin(ShapeRenderer.ShapeType.Filled);
        debugRenderer.setColor(color);
        debugRenderer.circle(start.x, start.y, radius);
        debugRenderer.end();
        Gdx.gl.glLineWidth(1);
    }



    public static void DrawDebugPolygon(Polygon polygon, int lineWidth, Color color, Matrix4 projectionMatrix)
    {

        initPolygon(polygon);


/*

        pgsb.setProjectionMatrix(projectionMatrix);
        pgsb.begin();
        polySprite.draw(pgsb);
        pgsb.end();



        */

        Gdx.gl.glLineWidth(lineWidth);
        debugRenderer.setProjectionMatrix(projectionMatrix);
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        debugRenderer.setColor(color);



        debugRenderer.polygon(polygon.getVertices());
        debugRenderer.end();
        Gdx.gl.glLineWidth(1);
    }
}

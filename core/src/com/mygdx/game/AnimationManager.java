package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;

public class AnimationManager {

    private final ArrayList<TextureRegion> frames = new ArrayList<TextureRegion>();



    private int maxFrames;

    private int frameCount = 0;


    private int startingFrame;

    private String region;

    private float animationCycle;
    private float animationTime;





    /*

        animationCycle is the time between each frame
        maxFrames is how many frames
        startingFrame is [BLANK]
        region is the string to be modiefied with an int
        textureAtlas is the .atlas file for the animation

     */
    public AnimationManager(float animationCycle, int maxFrames, int startingFrame, String region, TextureAtlas textureAtlas) {

        this.startingFrame = startingFrame;
        this.region = region;
        this.maxFrames = maxFrames;


        // this populates frames with each texture region
        for (int i = 0; i < maxFrames; ++i) {

            frames.add(textureAtlas.findRegion(region + (i + startingFrame)));

        }

        this.animationCycle = animationCycle / maxFrames;


    }

    public void update(float rate) {

        animationTime += rate;

        if (animationTime >= animationCycle) {
            animationTime = 0;
            frameCount ++;
        }

        frameCount = frameCount > maxFrames - startingFrame? 0 : frameCount;





    }

    public TextureRegion getFrame() {

        return frames.get(frameCount);   // so it sets the index at 0 we subtract startingFrame

    }

    public ArrayList<TextureRegion> getRegion() {
        return frames;
    }




}

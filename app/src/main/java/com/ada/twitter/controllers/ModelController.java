package com.ada.twitter.controllers;

/**
 * Created by ada on 11/5/16.
 */
public class ModelController {

    private static ModelController ourInstance = new ModelController();

    public static ModelController getInstance() {
        return ourInstance;
    }

    private ModelController() {
    }


}

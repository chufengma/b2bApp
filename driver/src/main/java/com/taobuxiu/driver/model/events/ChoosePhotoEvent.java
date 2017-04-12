package com.taobuxiu.driver.model.events;

/**
 * Created by dev on 2017/3/6.
 */

public class ChoosePhotoEvent extends BaseStatusEvent {
    public String photoFile;
    public String id;

    public ChoosePhotoEvent(String id, String photoFile, int status) {
        this.photoFile = photoFile;
        this.id = id;
        this.status = status;
    }
}

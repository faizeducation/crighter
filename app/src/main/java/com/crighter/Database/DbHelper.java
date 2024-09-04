package com.crighter.Database;

/**
 * Created by gold on 10/2/2018.
 */

public class DbHelper {

    String id;
    String status;//0 mean uploaded 1 mean not uploaded
    String userid;
    String audiopath;
    String image1;
    String image2;
    String image3;
    String image4;
    String image5;
    String lat;
    String lng;

    public void setId(String id) {
        this.id = id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setAudiopath(String audiopath) {
        this.audiopath = audiopath;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public void setImage4(String image4) {
        this.image4 = image4;
    }

    public void setImage5(String image5) {
        this.image5 = image5;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }



    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getUserid() {
        return userid;
    }

    public String getAudiopath() {
        return audiopath;
    }

    public String getImage1() {
        return image1;
    }

    public String getImage2() {
        return image2;
    }

    public String getImage3() {
        return image3;
    }

    public String getImage4() {
        return image4;
    }

    public String getImage5() {
        return image5;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }


}

package com.example.mobile_shopping;

public class Users {

    String _key;
    String _name;
    String _image;
    String _status;
    String _thumb_image;

    public Users () {

    }

    public Users(String _key, String _name, String _image, String _status, String _thumb_image) {
        this._key = _key;
        this._name = _name;
        this._image = _image;
        this._status = _status;
        this._thumb_image = _thumb_image;
    }

    public String get_key() {
        return _key;
    }

    public void set_key(String _key) {
        this._key = _key;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_image() {
        return _image;
    }

    public void set_image(String _image) {
        this._image = _image;
    }

    public String get_status() {
        return _status;
    }

    public void set_status(String _status) {
        this._status = _status;
    }

    public String get_thumb_image() {
        return _thumb_image;
    }

    public void set_thumb_image(String _thumb_image) {
        this._thumb_image = _thumb_image;
    }
}

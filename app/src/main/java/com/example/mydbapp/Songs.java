package com.example.mydbapp;

public class Songs {
    private int _id;
    private String _name;

    private String _author;
    private int _duration;
    private String _albumName;

    public Songs(){}

    public Songs(int id, String name, String author, int duration, String albumName) {
        this._id = id;
        this._name = name;
        this._author = author;
        this._duration = duration;
        this._albumName = albumName;
    }

    public Songs(String name, String author, int duration, String albumName) {
        this._name = name;
        this._author = author;
        this._duration = duration;
        this._albumName = albumName;
    }

    public int getID() {
        return _id;
    }

    public void setID(int id) {
        this._id = id;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public String getAuthor() {
        return _author;
    }

    public void setAuthor(String author) {
        this._author = author;
    }

    public int getDuration() {
        return _duration;
    }

    public void setDuration(int duration) {
        this._duration = duration;
    }

    public String getAlbumName() {
        return _albumName;
    }

    public void setAlbumName(String albumName) {
        this._albumName = albumName;
    }
}

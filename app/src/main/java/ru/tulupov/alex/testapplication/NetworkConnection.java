package ru.tulupov.alex.testapplication;

interface NetworkConnection {

    String getHtmlText(String url);
    boolean checkUrl (String url);
}

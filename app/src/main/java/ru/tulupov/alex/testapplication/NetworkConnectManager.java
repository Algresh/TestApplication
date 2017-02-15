package ru.tulupov.alex.testapplication;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetworkConnectManager implements NetworkConnection {

    @Override
    public boolean checkUrl(String url) {
        if (url.contains("http://") || url.contains("https://")) {
            return true;
        }
        return false;
    }

    @Override
    public String getHtmlText(String url) {
        try {
            return downloadByUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    private String downloadByUrl (String strUrl) throws IOException {
        InputStream inputStream = null;
        String textHtml = null;

        try {
            URL url = new URL(strUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                inputStream = connection.getInputStream();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                int data;
                while ((data = inputStream.read()) != -1) {
                    byteArrayOutputStream.write(data);
                }

                byte[] result = byteArrayOutputStream.toByteArray();
                byteArrayOutputStream.close();
                textHtml = new String(result);

                inputStream.close();
            }

            return textHtml;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return textHtml;
    }
}

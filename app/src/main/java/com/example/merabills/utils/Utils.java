package com.example.merabills.utils;

import android.app.Activity;
import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

/**
 * Created by Jeetesh Surana.
 */
public class Utils {
    public static void saveTextToFile(Activity activity,String filename, String text) {
        try {
            FileOutputStream outputStream = activity.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(text.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String loadTextFromFile(Activity activity,String filename) {
        try {
            FileInputStream inputStream = activity.openFileInput(filename);
            InputStreamReader streamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();
            streamReader.close();
            inputStream.close();
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

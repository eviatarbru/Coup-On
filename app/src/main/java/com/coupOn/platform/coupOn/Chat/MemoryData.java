package com.coupOn.platform.coupOn.Chat;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class MemoryData
{
    public static String getLastMsg(Context context, String chatId)
    {
        String data = "0";
        try
        {
            FileInputStream fis = context.openFileInput(chatId + ".txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = bufferedReader.readLine()) != null)
            {
                sb.append(line);
            }
            data = sb.toString();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return data;
    }

    public static String saveLastMsgTS(String data, String chatKey, Context context)
    {
        try
        {
            FileOutputStream fileOutputStream = context.openFileOutput(chatKey + ".txt", Context.MODE_PRIVATE);
            fileOutputStream.write(data.getBytes());
            fileOutputStream.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return data;
    }

    public static String getData(Context context)
    {
        String data = "";
        try
        {
            FileInputStream fis = context.openFileInput("datata.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = bufferedReader.readLine()) != null)
            {
                sb.append(line);
            }
            data = sb.toString();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return data;
    }

    public static String saveData(String data, Context context)
    {
        try
        {
            FileOutputStream fileOutputStream = context.openFileOutput("datata.txt", Context.MODE_PRIVATE);
            fileOutputStream.write(data.getBytes());
            fileOutputStream.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return data;
    }
}

package com.example.mzdoes.subtrack;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by per6 on 11/30/17.
 */

public class Utility {

    public static void saveList(Context context, String key, ArrayList<Subscription> subscriptionArrayList) throws IOException {
        String tempFile = null;
        for (Subscription sub : subscriptionArrayList) {
            FileOutputStream fos = context.openFileOutput (key, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream (fos);
            oos.writeObject (subscriptionArrayList);
            oos.close ();
            fos.close ();
        }
    }

    public static ArrayList<Subscription> readList(Context context, String key) throws IOException, ClassNotFoundException {
        FileInputStream fis = context.openFileInput (key);
        ObjectInputStream ois = new ObjectInputStream (fis);
        ArrayList<Subscription> subscriptionArrayList = (ArrayList<Subscription>) ois.readObject();
        return subscriptionArrayList;
    }

    public static int getInt(String s) {
        Scanner scanner = new Scanner(s);
        return scanner.nextInt();
    }
}

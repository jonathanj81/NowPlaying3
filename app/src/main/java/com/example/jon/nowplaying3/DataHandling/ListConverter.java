package com.example.jon.nowplaying3.DataHandling;

import android.arch.persistence.room.TypeConverter;

import java.util.Arrays;
import java.util.List;

public class ListConverter {

    @TypeConverter
    public static List<String> toList(String spaceDelimited) {
        return Arrays.asList(spaceDelimited.split("\\s+"));
    }

    @TypeConverter
    public static String toStorableString(List<String> list) {
        StringBuilder temp = new StringBuilder();
        for (String item : list){
            temp.append(item).append(" ");
        }
        return temp.toString();
    }
}

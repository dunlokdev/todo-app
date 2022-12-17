package com.example.todoapp.Model;

import com.example.todoapp.R;

import java.util.ArrayList;
import java.util.List;

public class SpinnerItem {
    private String name;
    private int image;

    public SpinnerItem(String name, int image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public static List<SpinnerItem> TypeOptions() {
        List<SpinnerItem> result = new ArrayList<>();

        result.add(new SpinnerItem("All", R.drawable.folder_all));
        result.add(new SpinnerItem("Personal", R.drawable.black_folder));
        result.add(new SpinnerItem("Work", R.drawable.brown_folder));
        result.add(new SpinnerItem("Playing", R.drawable.white_folder));
        result.add(new SpinnerItem("Home", R.drawable.dark_blue_folder));
        result.add(new SpinnerItem("Technical", R.drawable.green_folder));
        result.add(new SpinnerItem("Studies", R.drawable.pink_folder));
        result.add(new SpinnerItem("School", R.drawable.red_folder));
        result.add(new SpinnerItem("Market", R.drawable.teal_folder));
        result.add(new SpinnerItem("Others", R.drawable.yellow_folder));

        return result;
    }

    public static List<SpinnerItem> StatusOptions() {
        List<SpinnerItem> result = new ArrayList<>();

        result.add(new SpinnerItem("All", R.drawable.folder_all));
        result.add(new SpinnerItem("Working", R.drawable.loading));
        result.add(new SpinnerItem("Success", R.drawable.success));

        return result;
    }
}

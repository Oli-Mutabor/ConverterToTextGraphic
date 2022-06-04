package ru.netology.graphics.image;

public class ColorSchema implements TextColorSchema {
    private final char[] textSymbols = new char[]{'#', '$', '@', '%', '*', '+', '-', '\''};

    @Override
    public char convert(int color) {
        return textSymbols[color / 32];
    }
}

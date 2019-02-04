package com.simorgh.cyclecalendar.util;

import android.graphics.Rect;
import android.view.View;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import androidx.recyclerview.widget.RecyclerView;

public class MyItemDecoration extends RecyclerView.ItemDecoration {

    @Override
    public void getItemOffsets(@NotNull Rect outRect, @NotNull View view, @NotNull RecyclerView parent, @NotNull RecyclerView.State state) {
        // only for the last one
        if (parent.getChildAdapterPosition(view) == Objects.requireNonNull(parent.getAdapter()).getItemCount() - 1) {
            outRect.bottom = 200;
        }
    }
}
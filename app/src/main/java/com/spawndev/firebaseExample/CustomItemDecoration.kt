package com.spawndev.firebaseExample

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CustomItemDecoration(private val verticalSpacing: Int, private val horizontalSpacing: Int) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.top = verticalSpacing
        outRect.bottom = verticalSpacing
        outRect.left = horizontalSpacing
        outRect.right = horizontalSpacing
    }
}
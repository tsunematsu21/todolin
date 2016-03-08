package com.tunesworks.todolin

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.util.AttributeSet
import android.view.View

class BottomBarBehavior(context: Context, attrs: AttributeSet): CoordinatorLayout.Behavior<View>() {
    override fun layoutDependsOn(parent: CoordinatorLayout?, child: View?, dependency: View?): Boolean {
        return dependency is Snackbar.SnackbarLayout
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout?, child: View?, dependency: View?): Boolean {
        if (dependency != null) {
            val translationY = Math.min(0f, dependency.translationY - dependency.height);
            child?.translationY = translationY
        }

        return true
    }
}

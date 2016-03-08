package com.tunesworks.todolin

import android.support.design.widget.Snackbar

abstract class SnackbarCallback: Snackbar.Callback() {
    var isShown = false

    open fun onShown() {}
    open fun onDismissed() {}

    final override fun onShown(snackbar: Snackbar?) {
        isShown = true
        onShown()
    }

    final override fun onDismissed(snackbar: Snackbar?, event: Int) {
        if (isShown) {
            isShown = false
            if (Snackbar.Callback.DISMISS_EVENT_ACTION != event) onDismissed()
        }
    }
}
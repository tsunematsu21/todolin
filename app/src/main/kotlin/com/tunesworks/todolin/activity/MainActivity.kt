package com.tunesworks.todolin.activity

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.RecognizerIntent
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.Snackbar
import android.support.v4.view.ViewPager
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import com.tunesworks.todolin.R
import com.tunesworks.todolin.ToDolin
import com.tunesworks.todolin.event.ToDoEvent
import com.tunesworks.todolin.fragment.PagerAdapter
import com.tunesworks.todolin.model.ToDo
import com.tunesworks.todolin.value.ItemColor
import com.tunesworks.todolin.value.primary
import com.tunesworks.todolin.value.primaryDark
import com.tunesworks.todolin.value.primaryLight
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.properties.Delegates

class MainActivity : BaseActivity() {
    val REQUEST_CODE = 0

    var bottomSheetBehavior: BottomSheetBehavior<View> by Delegates.notNull<BottomSheetBehavior<View>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var oldSlideOffset = 0f
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet as View).apply {
            setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    if (slideOffset > 0.8) {
                        if (oldSlideOffset > slideOffset) fab.show()
                        else fab.hide()

                    } else if (slideOffset < -0.7) {
                        if (oldSlideOffset < slideOffset) fab.show()
                        else fab.hide()
                    }
                    oldSlideOffset = slideOffset
                }
                override fun onStateChanged(bottomSheet: View, newState: Int) {}
            })
        }
        bottomSheet.setOnClickListener {}


        view_pager.apply {
            adapter = PagerAdapter(supportFragmentManager)

            fab.setOnLongClickListener {
                try {
                    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                        putExtra(RecognizerIntent.EXTRA_PROMPT, "Please Speech")
                    }
                    startActivityForResult(intent, REQUEST_CODE)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(this@MainActivity, "Error: Activity Not Found!", Toast.LENGTH_SHORT).show()
                }
                true
            }
            fab.setOnClickListener {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }

            var oldItemColor = ItemColor.DEFAULT
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {}
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
                override fun onPageSelected(position: Int) {
                    val newItemColor = ItemColor.values()[position]

                    // Animate toolbar background color
                    ValueAnimator.ofObject(ArgbEvaluator(), oldItemColor.primary, newItemColor.primary).apply {
                        addUpdateListener { appbar.setBackgroundColor(it.animatedValue as Int) }
                        duration = 500
                        interpolator = DecelerateInterpolator()
                        start()
                    }

                    // Animate status bar color
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ValueAnimator.ofObject(ArgbEvaluator(), oldItemColor.primaryDark, newItemColor.primaryDark).apply {
                            addUpdateListener { window.statusBarColor = it.animatedValue as Int }
                            duration = 500
                            interpolator = DecelerateInterpolator()
                            start()
                        }
                    }

                    // Save color
                    oldItemColor = newItemColor
                }
            })
        }

        tabs.setupWithViewPager(view_pager)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if ((REQUEST_CODE == requestCode) && (RESULT_OK == resultCode)) {
            val results = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (results != null) {
                Realm.getDefaultInstance().use { realm ->
                    val todo = ToDo(title = results.first())
                    realm.executeTransaction({
                        it.copyToRealm(todo)
                    }, object : Realm.Transaction.Callback() {
                        override fun onSuccess() {
                            super.onSuccess()
                            ToDolin.bus.post(ToDoEvent.Create(todo))
                            Snackbar.make(fab, "Create new ToDo", Toast.LENGTH_SHORT)
                        }

                        override fun onError(e: Exception?) {
                            super.onError(e)
                            Snackbar.make(fab, "Error!!!", Toast.LENGTH_SHORT)
                        }
                    })
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}

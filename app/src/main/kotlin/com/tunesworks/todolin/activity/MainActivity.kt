package com.tunesworks.todolin.activity

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.RecognizerIntent
import android.support.v4.view.ViewPager
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import com.tunesworks.todolin.R
import com.tunesworks.todolin.fragment.PagerAdapter
import com.tunesworks.todolin.value.ItemColor
import com.tunesworks.todolin.value.primary
import com.tunesworks.todolin.value.primaryDark
import com.tunesworks.todolin.value.primaryLight
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    val REQUEST_CODE = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        view_pager.apply {
            adapter = PagerAdapter(supportFragmentManager)

            fab.setOnClickListener {
                try {
                    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                        putExtra(RecognizerIntent.EXTRA_PROMPT, "Please Speech")
                    }
                    startActivityForResult(intent, REQUEST_CODE)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(this@MainActivity, "Error: Activity Not Found!", Toast.LENGTH_SHORT).show()
                }
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
                Toast.makeText(this, results.first(), Toast.LENGTH_LONG).show()
                //createToDo(results.first())
                //results.forEach { Log.d(this@MainActivity.javaClass.name, "Recognizer Result: $it") }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}

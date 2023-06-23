package com.demo.splashscreenapisample

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    private lateinit var splashScreen: SplashScreen
    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashScreen = installSplashScreen()
        setContentView(R.layout.activity_main)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        // 1. Uncomment the line below if you want to use custom exit animation.
        useCustomExitAnimation()

        // 2. Uncomment the line below if you want to keep splash screen on-screen for longer period.
        keepSplashScreenFor5Seconds()

        // 3. Uncomment the line below if you want to keep splash screen on-screen indefinitely.
        //keepSplashScreenIndefinitely()
    }

    /**
     * Use customize exit animation for splash screen.
     */
    private fun useCustomExitAnimation() {
        /*splashScreen.setOnExitAnimationListener { splashScreenViewProvider ->
            val splashScreenView = splashScreenViewProvider.view
            val slideUp = ObjectAnimator.ofFloat(
                splashScreenView,
                View.TRANSLATION_Y,
                0f,
                -splashScreenView.height.toFloat(),
            )
            slideUp.interpolator = BounceInterpolator()
            slideUp.duration = 1500L
            slideUp.doOnEnd {
                splashScreenViewProvider.remove()
            }
            slideUp.start()
        }*/

        splashScreen.setOnExitAnimationListener { splashScreenViewProvider ->
            val splashScreenView = splashScreenViewProvider.view
            // custom animation.
            ObjectAnimator.ofFloat(
                splashScreenView,
                View.TRANSLATION_X,
                0f,
                -splashScreenView.width.toFloat()
            ).apply {
                duration = 1000
                // Call SplashScreenView.remove at the end of your custom animation.
                doOnEnd {
                    splashScreenViewProvider.remove()
                }
            }.also {
                // Run your animation.
                it.start()
            }
        }

    }

    /**
     * Keep splash screen on-screen for longer period. This is useful if you need to load data when
     * splash screen is appearing.
     */
    private fun keepSplashScreenFor5Seconds() {
        val content = findViewById<View>(android.R.id.content)

        /*content.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                Thread.sleep(5000)
                content.viewTreeObserver.removeOnPreDrawListener(this)
                return true
            }
        })*/

        content.viewTreeObserver.addOnPreDrawListener(object :
            ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean =
                when {
                    mainViewModel.mockDataLoading() -> {
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    }
                    else -> false
                }
        })
    }

    /**
     * Keep splash screen on-screen indefinitely. This is useful if you're using a custom Activity
     * for routing.
     */
    private fun keepSplashScreenIndefinitely() {
        splashScreen.setKeepOnScreenCondition { true }
    }
}
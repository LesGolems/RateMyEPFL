package com.github.sdp.ratemyepfl.dependencyinjection

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.core.util.Preconditions
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import com.github.sdp.ratemyepfl.activity.HiltTestActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi

object HiltUtils {

    /**
     * Launch a fragment in an hilt container (i.e., the HiltTestActivity)
     * This is mandatory for an AndroidEntryPoint fragment (that is, injected
     * by Hilt) since the activity provided by any other fragment launcher
     * is not annotated with it (and Hilt thus can't inject)
     *
     * Ex:
     *      launchFragmentInHiltContainer<MyFragment> { fragment ->
     *          // Works as a fragment.apply()
     *      }
     *      // The view is set. You can call any view matcher.
     *      onView(withId(myId)).perform(click())
     *
     */
    @ExperimentalCoroutinesApi
    inline fun <reified T : Fragment> launchFragmentInHiltContainer(
        fragmentArgs: Bundle? = null,
        themeResId: Int = androidx.fragment.testing.R.style.FragmentScenarioEmptyFragmentActivityTheme,
        fragmentFactory: FragmentFactory? = null,
        crossinline action: T.() -> Unit = {}
    ) {
        val mainActivityIntent = Intent.makeMainActivity(
            ComponentName(
                ApplicationProvider.getApplicationContext(),
                HiltTestActivity::class.java
            )
        ).putExtra(
            "androidx.fragment.app.testing.FragmentScenario.EmptyFragmentActivity.THEME_EXTRAS_BUNDLE_KEY",
            themeResId
        )

        ActivityScenario.launch<HiltTestActivity>(mainActivityIntent).onActivity { activity ->
            fragmentFactory?.let {
                activity.supportFragmentManager.fragmentFactory = it
            }
            val fragment = activity.supportFragmentManager.fragmentFactory.instantiate(
                Preconditions.checkNotNull(T::class.java.classLoader),
                T::class.java.name
            )
            fragment.arguments = fragmentArgs

            activity.supportFragmentManager.beginTransaction()
                .add(android.R.id.content, fragment, "")
                .commitNow()

            (fragment as T).action()
        }

    }
}
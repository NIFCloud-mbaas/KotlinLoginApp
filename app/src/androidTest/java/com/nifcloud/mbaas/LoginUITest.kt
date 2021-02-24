package com.nifcloud.mbaas

import androidx.test.espresso.Espresso
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.nifcloud.user.MainActivity
import com.nifcloud.user.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginUITest {
    var ivLogo: ViewInteraction? = null
    var edtName: ViewInteraction? = null
    var edtPass: ViewInteraction? = null
    var btnLogin: ViewInteraction? = null
    var tvLinkSignup: ViewInteraction? = null

    @get:Rule
    val activityRule = ActivityScenarioRule(
        MainActivity::class.java
    )

    @Before
    fun setup() {
        ivLogo = Espresso.onView(withId(R.id.iv_logo))
        edtName = Espresso.onView(withId(R.id.input_name))
        edtPass = Espresso.onView(withId(R.id.input_password))
        btnLogin = Espresso.onView(withId(R.id.btn_login))
        tvLinkSignup = Espresso.onView(withId(R.id.link_signup))
    }

    @Test
    fun initialScreen() {
        ivLogo!!.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        edtName!!.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        edtPass!!.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        btnLogin!!.check(ViewAssertions.matches(ViewMatchers.withText("Login")))
        tvLinkSignup!!.check(ViewAssertions.matches(ViewMatchers.withText("No account yet? Create one")))
    }

    @Test
    fun validate_user_name() {
        edtName!!.perform(ViewActions.typeText(""))
        edtPass!!.perform(ViewActions.typeText("123456"), ViewActions.closeSoftKeyboard())
        btnLogin!!.perform(ViewActions.scrollTo()).perform(ViewActions.click())
        edtName!!.check(ViewAssertions.matches(ViewMatchers.hasErrorText("enter username")))
    }

    @Test
    fun validate_empty_pass() {
        edtName!!.perform(ViewActions.typeText("Hoge"))
        edtPass!!.perform(ViewActions.typeText(""), ViewActions.closeSoftKeyboard())
        btnLogin!!.perform(ViewActions.scrollTo()).perform(ViewActions.click())
        edtPass!!.check(ViewAssertions.matches(ViewMatchers.hasErrorText("between 4 and 10 alphanumeric characters")))
    }

    @Test
    fun validate_less_pass_length() {
        edtName!!.perform(ViewActions.typeText("Hoge"))
        edtPass!!.perform(ViewActions.typeText("123"), ViewActions.closeSoftKeyboard())
        btnLogin!!.perform(ViewActions.scrollTo()).perform(ViewActions.click())
        edtPass!!.check(ViewAssertions.matches(ViewMatchers.hasErrorText("between 4 and 10 alphanumeric characters")))
    }

    @Test
    fun validate_over_pass_length() {
        edtName!!.perform(ViewActions.typeText("Hoge"))
        edtPass!!.perform(ViewActions.typeText("12345678901"), ViewActions.closeSoftKeyboard())
        btnLogin!!.perform(ViewActions.scrollTo()).perform(ViewActions.click())
        edtPass!!.check(ViewAssertions.matches(ViewMatchers.hasErrorText("between 4 and 10 alphanumeric characters")))
    }

    @Test
    fun openSignup() {
        tvLinkSignup!!.perform(ViewActions.scrollTo()).perform(ViewActions.click())
        Espresso.onView(withId(R.id.btn_signup))
            .check(ViewAssertions.matches(ViewMatchers.withText("Create Account")))
    }

    @Test
    @Throws(InterruptedException::class)
    fun doLogin() {
        edtName!!.perform(ViewActions.typeText("Hoge"))
        edtPass!!.perform(ViewActions.typeText("123456"), ViewActions.closeSoftKeyboard())
        btnLogin!!.perform(ViewActions.scrollTo()).perform(ViewActions.click())
        var timeout = 0
        while (timeout < 10000) {
            timeout += try {
                Espresso.onView(ViewMatchers.withText("You are logged in!"))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                break
            } catch (e: Exception) {
                Thread.sleep(1000L)
                1000
            }
        }
    }
}
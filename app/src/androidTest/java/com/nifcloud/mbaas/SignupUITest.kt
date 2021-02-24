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
class SignupUITest {

    var ivLogo: ViewInteraction? = null
    var edtName: ViewInteraction? = null
    var edtPass: ViewInteraction? = null
    var btnSignup: ViewInteraction? = null
    var tvLinkLogin: ViewInteraction? = null

    @get:Rule
    val activityRule = ActivityScenarioRule(
        MainActivity::class.java
    )

    @Before
    fun setup() {
        Espresso.onView(withId(R.id.link_signup)).perform(ViewActions.click())
        ivLogo = Espresso.onView(withId(R.id.iv_logo))
        edtName = Espresso.onView(withId(R.id.input_name))
        edtPass = Espresso.onView(withId(R.id.input_password))
        btnSignup = Espresso.onView(withId(R.id.btn_signup))
        tvLinkLogin = Espresso.onView(withId(R.id.link_login))
    }

    @Test
    fun initialScreen() {
        ivLogo!!.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        edtName!!.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        edtPass!!.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        btnSignup!!.check(ViewAssertions.matches(ViewMatchers.withText("Create Account")))
        tvLinkLogin!!.check(ViewAssertions.matches(ViewMatchers.withText("Already a member? Login")))
    }

    @Test
    fun validate_empty_user_name() {
        edtName!!.perform(ViewActions.typeText(""))
        edtPass!!.perform(ViewActions.typeText("123456"), ViewActions.closeSoftKeyboard())
        btnSignup!!.perform(ViewActions.scrollTo()).perform(ViewActions.click())
        edtName!!.check(ViewAssertions.matches(ViewMatchers.hasErrorText("at least 3 characters")))
    }

    @Test
    fun validate_less_user_name_length() {
        edtName!!.perform(ViewActions.typeText("ho"))
        edtPass!!.perform(ViewActions.typeText("123456"), ViewActions.closeSoftKeyboard())
        btnSignup!!.perform(ViewActions.scrollTo()).perform(ViewActions.click())
        edtName!!.check(ViewAssertions.matches(ViewMatchers.hasErrorText("at least 3 characters")))
    }

    @Test
    fun validate_empty_pass() {
        edtName!!.perform(ViewActions.typeText("hoge"))
        edtPass!!.perform(ViewActions.typeText(""), ViewActions.closeSoftKeyboard())
        btnSignup!!.perform(ViewActions.scrollTo()).perform(ViewActions.click())
        edtPass!!.check(ViewAssertions.matches(ViewMatchers.hasErrorText("between 4 and 10 alphanumeric characters")))
    }

    @Test
    fun validate_less_pass_length() {
        edtName!!.perform(ViewActions.typeText("hoge"))
        edtPass!!.perform(ViewActions.typeText("123"), ViewActions.closeSoftKeyboard())
        btnSignup!!.perform(ViewActions.scrollTo()).perform(ViewActions.click())
        edtPass!!.check(ViewAssertions.matches(ViewMatchers.hasErrorText("between 4 and 10 alphanumeric characters")))
    }

    @Test
    fun validate_over_pass_length() {
        edtName!!.perform(ViewActions.typeText("hoge"))
        edtPass!!.perform(ViewActions.typeText("12345678901"), ViewActions.closeSoftKeyboard())
        btnSignup!!.perform(ViewActions.scrollTo()).perform(ViewActions.click())
        edtPass!!.check(ViewAssertions.matches(ViewMatchers.hasErrorText("between 4 and 10 alphanumeric characters")))
    }

    @Test
    fun openLogin() {
        tvLinkLogin!!.perform(ViewActions.scrollTo()).perform(ViewActions.click())
        Espresso.onView(withId(R.id.btn_login))
            .check(ViewAssertions.matches(ViewMatchers.withText("Login")))
    }

    @Test
    @Throws(InterruptedException::class)
    fun doLogin() {
        edtName!!.perform(ViewActions.typeText("Hoge"))
        edtPass!!.perform(ViewActions.typeText("123456"), ViewActions.closeSoftKeyboard())
        btnSignup!!.perform(ViewActions.click())
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
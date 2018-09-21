package com.nifcloud.user

import android.app.Activity
import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import com.nifcloud.mbaas.core.NCMBUser

class SignupActivity : AppCompatActivity() {

    @BindView(R.id.input_name)
    lateinit var _nameText: EditText
    @BindView(R.id.input_password)
    lateinit var _passwordText: EditText
    @BindView(R.id.btn_signup)
    lateinit var _signupButton: Button
    @BindView(R.id.link_login)
    lateinit var _loginLink: TextView

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        ButterKnife.bind(this)

        _signupButton.setOnClickListener { signup() }

        _loginLink.setOnClickListener {
            // Finish the registration screen and return to the Login activity
            setResult(Activity.RESULT_CANCELED, intent)
            finish()
        }
    }

    fun signup() {
        Log.d(TAG, "Signup")

        if (!validate()) {
            onSignupFailed()
            return
        }

        _signupButton!!.isEnabled = false

        val progressDialog = ProgressDialog(this@SignupActivity,
                R.style.AppTheme_Dark_Dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Creating Account...")
        progressDialog.show()

        val name = _nameText!!.text.toString()
        val password = _passwordText!!.text.toString()

        // TODO: Implement your own signup logic here.
        //NCMBUserのインスタンスを作成
        val user = NCMBUser()
        //ユーザ名を設定
        user.userName = name
        //パスワードを設定
        user.setPassword(password)
        //設定したユーザ名とパスワードで会員登録を行う
        user.signUpInBackground { e ->
            if (e != null) {
                //会員登録時にエラーが発生した場合の処理
                onSignupFailed()
                progressDialog.dismiss()
            } else {
                android.os.Handler().postDelayed(
                        {
                            // On complete call either onSignupSuccess or onSignupFailed
                            // depending on success
                            onSignupSuccess()
                            // onSignupFailed();
                            progressDialog.dismiss()
                        }, 3000)
            }
        }
    }


    fun onSignupSuccess() {
        _signupButton!!.isEnabled = true
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    fun onSignupFailed() {
        Toast.makeText(baseContext, "Login failed", Toast.LENGTH_LONG).show()

        _signupButton!!.isEnabled = true
    }

    fun validate(): Boolean {
        var valid = true

        val name = _nameText!!.text.toString()
        val password = _passwordText!!.text.toString()

        if (name.isEmpty() || name.length < 3) {
            _nameText!!.error = "at least 3 characters"
            valid = false
        } else {
            _nameText!!.error = null
        }

        if (password.isEmpty() || password.length < 4 || password.length > 10) {
            _passwordText!!.error = "between 4 and 10 alphanumeric characters"
            valid = false
        } else {
            _passwordText!!.error = null
        }

        return valid
    }

    companion object {
        private val TAG = "SignupActivity"
    }
}
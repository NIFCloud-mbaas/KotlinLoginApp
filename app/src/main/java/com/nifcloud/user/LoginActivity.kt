package com.nifcloud.user

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import butterknife.BindView
import butterknife.ButterKnife
import com.nifcloud.mbaas.core.NCMBException
import com.nifcloud.mbaas.core.NCMBUser

class LoginActivity : AppCompatActivity() {

    @BindView(R.id.input_name)
    lateinit var _nameText: EditText
    @BindView(R.id.input_password)
    lateinit var _passwordText: EditText
    @BindView(R.id.btn_login)
    lateinit var _loginButton: Button
    @BindView(R.id.link_signup)
    lateinit var _signupLink: TextView

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        ButterKnife.bind(this)

        _loginButton.setOnClickListener { login() }


        _signupLink?.setOnClickListener {
            // Start the Signup activity
            val intent = Intent(applicationContext, SignupActivity::class.java)
            startActivityForResult(intent, REQUEST_SIGNUP)
        }
    }

    fun login() {
        Log.d(TAG, "Login")

        if (!validate()) {
            onLoginFailed()
            return
        }

        _loginButton?.isEnabled = false

        val progressDialog = ProgressDialog(this@LoginActivity,
                R.style.AppTheme_Dark_Dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Authenticating...")
        progressDialog.show()

        val name = _nameText?.text.toString()
        val password = _passwordText?.text.toString()

        // TODO: Implement your own authentication logic here.
        //ユーザ名とパスワードを指定してログインを実行
        try {
            NCMBUser.loginInBackground(name, password) { user, e ->
                if (e != null) {
                    //エラー時の処理
                    onLoginFailed()
                    progressDialog.dismiss()
                } else {
                    android.os.Handler().postDelayed(
                            {
                                // On complete call either onLoginSuccess or onLoginFailed
                                onLoginSuccess()
                                // onLoginFailed();
                                progressDialog.dismiss()
                            }, 3000)
                }
            }
        } catch (e: NCMBException) {
            e.printStackTrace()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == Activity.RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish()
            }
        }
    }

    override fun onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true)
    }

    fun onLoginSuccess() {
        _loginButton?.isEnabled = true
        finish()
    }

    fun onLoginFailed() {
        Toast.makeText(baseContext, "Login failed", Toast.LENGTH_LONG).show()

        _loginButton?.isEnabled = true
    }

    fun validate(): Boolean {
        var valid = true

        val name = _nameText?.text.toString()
        val password = _passwordText?.text.toString()

        if (name.isEmpty()) {
            _nameText?.error = "enter username"
            valid = false
        } else {
            _nameText?.error = null
        }

        if (password.isEmpty() || password.length < 4 || password.length > 10) {
            _passwordText?.error = "between 4 and 10 alphanumeric characters"
            valid = false
        } else {
            _passwordText?.error = null
        }

        return valid
    }

    companion object {
        private val TAG = "LoginActivity"
        private val REQUEST_SIGNUP = 0
    }
}

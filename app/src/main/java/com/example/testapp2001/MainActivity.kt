package com.example.testapp2001

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.testapp2001.RetrofitClient

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.properties.Delegates


class MainActivity : AppCompatActivity() {
    private var mEmailView: AutoCompleteTextView? = null
    private var mPasswordView: EditText? = null
    private var mEmailLoginButton: Button? = null
    private var mJoinButton: TextView? = null
    private var mProgressView: ProgressBar? = null
    private var service: ServiceApi? = null
    var loginactivity :MainActivity? =null
    companion object{
        lateinit var maincontext : MainActivity
        var Useruid by Delegates.notNull<Int>()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        maincontext=this
        loginactivity=this
        setContentView(R.layout.activity_main)
        mEmailView =findViewById<View>(R.id.email_edittext) as AutoCompleteTextView
        mPasswordView = findViewById<View>(R.id.password_edittext) as EditText
        mEmailLoginButton =
            findViewById<View>(R.id.signIn_button) as Button
        mJoinButton = findViewById<View>(R.id.register_button) as TextView
        mProgressView = findViewById<View>(R.id.login_progress) as ProgressBar
        val retrofit = RetrofitClient.client
        service = retrofit.create(ServiceApi::class.java);

        maincontext=this@MainActivity

        mEmailLoginButton!!.setOnClickListener {
            attemptLogin()
        }
        mJoinButton!!.setOnClickListener {
            val intent = Intent(applicationContext, JoinActivity::class.java)
            startActivity(intent)
        }
    }

    private fun attemptLogin() {
        mEmailView!!.error = null
        mPasswordView!!.error = null
        val email = mEmailView!!.text.toString()
        val password = mPasswordView!!.text.toString()
        var cancel = false
        var focusView: View? = null

        // 패스워드의 유효성 검사
        if (password.isEmpty()) {
            mEmailView!!.error = "비밀번호를 입력해주세요."
            focusView = mEmailView
            cancel = true
        } else if (!isPasswordValid(password)) {
            mPasswordView!!.error = "6자 이상의 비밀번호를 입력해주세요."
            focusView = mPasswordView
            cancel = true
        }

        // 이메일의 유효성 검사
        if (email.isEmpty()) {
            mEmailView!!.error = "이메일을 입력해주세요."
            focusView = mEmailView
            cancel = true
        } else if (!isEmailValid(email)) {
            mEmailView!!.error = "@를 포함한 유효한 이메일을 입력해주세요."
            focusView = mEmailView
            cancel = true
        }
        if (cancel) {
            focusView!!.requestFocus()
        } else {
            Log.d("debug","$email, $password 로그인 시도")
            startLogin(LoginData(email, password))
            showProgress(true)
        }
    }

    private fun startLogin(data: LoginData) {
        service!!.userLogin(data)!!.enqueue(object : Callback<LoginResponse?> {
            override fun onResponse(
                call: Call<LoginResponse?>,
                response: Response<LoginResponse?>
            ) {
                val result = response.body()
                showProgress(false)
                if (result != null) {
                    Toast.makeText(MainActivity.maincontext, "${result.message}", Toast.LENGTH_SHORT).show()
                    Log.d("debug","${result.message}")

                    if (result.code == 200){
                        MainActivity.Useruid=result.userId
                        Log.d("debub","${result.userId}")
                        val intent= Intent(loginactivity,FriendsActivity::class.java)
                        startActivity(intent)
                    }
                }



            }

            override fun onFailure(
                call: Call<LoginResponse?>,
                t: Throwable
            ) {
                Toast.makeText(MainActivity.maincontext, "로그인 에러 발생", Toast.LENGTH_SHORT).show()

                Log.e("로그인 에러 발생", t.message!!)
                showProgress(false)
            }
        })
    }

    private fun isEmailValid(email: String): Boolean {
        return email.contains("@")
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 6
    }

    private fun showProgress(show: Boolean) {
        mProgressView!!.visibility = if (show) View.VISIBLE else View.GONE
    }
}
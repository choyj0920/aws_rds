package com.example.testapp2001;

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.example.testapp2001.JoinData
import com.example.testapp2001.JoinResponse
import com.example.testapp2001.RetrofitClient
import com.example.testapp2001.ServiceApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.android.synthetic.main.activity_join.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class JoinActivity : AppCompatActivity() {
    private var mEmailView: AutoCompleteTextView? = null
    private var mPasswordView: EditText? = null
    private var mNameView: EditText? = null
    private var mJoinButton: Button? = null
    private var mProgressView: ProgressBar? = null
    private var service: ServiceApi? = null
    companion object{
        lateinit var joincontext : Context
    }
    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_join)

        mEmailView = findViewById(R.id.email);
        mPasswordView =  findViewById(R.id.password)
        mNameView = findViewById(R.id.name)
        mJoinButton = findViewById(R.id.register)
        mProgressView = findViewById(R.id.join_progress)

        joincontext=this

        // ec2 가져오기
        val retrofit = RetrofitClient.client
        service = retrofit.create(ServiceApi::class.java);

        mJoinButton!!.setOnClickListener { attemptJoin() }
    }

    private fun attemptJoin() {
        mNameView!!.error = null
        mEmailView!!.error = null
        mPasswordView!!.error = null
        val name = mNameView!!.text.toString()
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

        // 이름의 유효성 검사
        if (name.isEmpty()) {
            mNameView!!.error = "이름을 입력해주세요."
            focusView = mNameView
            cancel = true
        }
        if (cancel) {
            focusView!!.requestFocus()
        } else {
            startJoin(JoinData(name, email, password))
            showProgress(true)
        }
    }

    private fun startJoin(data: JoinData) {
        service!!.userJoin(data)!!.enqueue(object : Callback<JoinResponse?> {
            override fun onResponse(
                call: Call<JoinResponse?>,
                response: Response<JoinResponse?>
            ) {
                val result = response.body()
                Toast.makeText(JoinActivity.joincontext, result!!.message, Toast.LENGTH_SHORT).show()
                showProgress(false)
                if (result.code == 200) {
                    finish()
                }
            }

            override fun onFailure(
                call: Call<JoinResponse?>,
                t: Throwable
            ) {
                Toast.makeText(JoinActivity.joincontext, "회원가입 에러 발생", Toast.LENGTH_SHORT).show()
                Log.e("회원가입 에러 발생", t.message!!)
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
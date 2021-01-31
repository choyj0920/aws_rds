package com.example.testapp2001

import android.content.Intent
import android.graphics.Bitmap
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URL
import java.util.*

class FriendsActivity : AppCompatActivity() {
    lateinit var rv_friend : RecyclerView
    lateinit var chatroomid : String
    lateinit var friendsactivity:FriendsActivity
    private var service: ServiceApi? = null

    var profileList=arrayListOf<Profiles>()
    companion object{
    }
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friendlist)

        val retrofit = RetrofitClient.client
        service = retrofit.create(ServiceApi::class.java);

        chatroomid=intent.getStringExtra("chatroomid").toString()
        friendsactivity=this
        loadData()
        rv_friend = findViewById((R.id.rv_profile))as RecyclerView
        var layoutManager=
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL,true)
        layoutManager.stackFromEnd=true  //역순
        rv_friend.layoutManager=layoutManager
        //rvprofile.adapter = ProfileAdapter(requireContext(), profileList)
        rv_friend.adapter=ProfileAdapter(friendsactivity,profileList)



    }


    fun loadData()  { // 채팅방 메시지 값 로드 함수
        profileList=arrayListOf<Profiles>()
        service!!.findFriend(UserData(MainActivity.Useruid))!!.enqueue(object : Callback<FriendlistResponse?> {
            override fun onResponse(
                    call: Call<FriendlistResponse?>,
                    response: Response<FriendlistResponse?>
            ) {
                val result = response.body()

                if (result != null) {
                    // Toast.makeText(MainActivity.maincontext, "${result.message}", Toast.LENGTH_SHORT).show()
                    Log.d("debug","${result.message}")

                    if (result.code == 200){
                        result.userlist as Array<friendrelation>
                        for (i in result.userlist ){
                            if (i.user1uid==MainActivity.Useruid){
                                profileList.add(Profiles(i.user2name,i.user2uid))
                                Log.d("debug","${i.user2name}, ${i.user2uid}")
                            }
                            else{
                                profileList.add(Profiles(i.user1name,i.user1uid))
                                Log.d("debug","${i.user1name}, ${i.user1uid}")

                            }

                        }
                        rv_friend.adapter=ProfileAdapter(friendsactivity,profileList)

                    }
                }
            }

            override fun onFailure(
                    call: Call<FriendlistResponse?>,
                    t: Throwable
            ) {
                Toast.makeText(MainActivity.maincontext, "로그인 에러 발생", Toast.LENGTH_SHORT).show()

                Log.e("로그인 에러 발생", t.message!!)

            }
        })


    }

}
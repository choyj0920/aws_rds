package com.example.testapp2001

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.net.URL


class ProfileAdapter(var context : Context, val profileList: ArrayList<Profiles>) : RecyclerView.Adapter<ProfileAdapter.CustomViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): ProfileAdapter.CustomViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.friends_item,parent,false)
        return CustomViewHolder(view) // inflater -> 부착
    }

    override fun getItemCount(): Int {
        return profileList.size
    }
    // 
    override fun onBindViewHolder(holder: ProfileAdapter.CustomViewHolder, position: Int) {

        holder.name.text = profileList.get(position).name
        holder.age.text = profileList.get(position).uid.toString()



    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name =itemView.findViewById<TextView>(R.id.board_name)         // 이름
        val age =itemView.findViewById<TextView>(R.id.board_age)           // 나이

    }
}


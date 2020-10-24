package com.example.kotlindemo3

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class MovieCustomTagAdapter(var mCtx: Context, var resources: Int, var items: List<Model>) :
    ArrayAdapter<Model>(mCtx, resources, items) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(resources, null)

        val movieImage = view.findViewById<ImageView>(R.id.image)

        var mItem: Model = items[position]
        Picasso.with(this.context).load(mItem.movieImageUrl).into(movieImage)
        return view
    }
}
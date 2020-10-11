package com.example.synechron_test.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.synechron_test.R
import com.example.synechron_test.model.PetsItem
import kotlinx.android.synthetic.main.item_layout.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL


class PetsAdapter(val context: Context, val listener: OnViewClickListener) :
    RecyclerView.Adapter<PetsAdapter.PetsViewHolder>() {

    var list: ArrayList<PetsItem> = arrayListOf()

    class PetsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: PetsItem) {

            itemView.tv_title.text = data.title
        }
    }

    interface OnViewClickListener {
        fun onViewClick(content_url: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetsViewHolder {
        val rootView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return PetsViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: PetsViewHolder, position: Int) {
        holder.bind(list[position])
        CoroutineScope(Dispatchers.IO).launch {
            val bmp: Bitmap = loadImage(list[position].image_url)
            withContext(Dispatchers.Main) {
                holder.itemView.iv_picture.setImageBitmap(bmp)
            }
        }
        holder.itemView.item_layout_root_id.setOnClickListener {

            listener.onViewClick(list[position].content_url)

        }
    }
    private fun loadImage(imageirl: String): Bitmap {
        val url = URL(imageirl)
        val bmp: Bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
        return bmp
    }

    override fun getItemCount(): Int = list.size

    fun setData(newlist: ArrayList<PetsItem>) {
        list = newlist
        notifyDataSetChanged()
    }

}
package com.example.updateviewmodel.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.updateviewmodel.model.SongSearch
import com.example.updateviewmodel.databinding.ItemSongBinding

class SongAdapter(
    var listSong: MutableList<SongSearch>,
    val inter: ISongAdapter
) : RecyclerView.Adapter<SongAdapter.SongHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongHolder {
        return SongHolder(
            ItemSongBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = listSong.size

    override fun onBindViewHolder(holder: SongHolder, position: Int) {
        holder.binding.itemData = listSong[position]
        holder.binding.root.setOnClickListener {
            inter.onItemClick(position)
        }
    }

    fun setData(values: MutableList<SongSearch>) {
        listSong = values
        notifyDataSetChanged()
    }

    interface ISongAdapter {
        fun onItemClick(position: Int)
    }

    class SongHolder(val binding: ItemSongBinding) : RecyclerView.ViewHolder(binding.root)
}
package com.example.updateviewmodel.repository

import com.example.updateviewmodel.model.SongLink
import com.example.updateviewmodel.model.SongSearch
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface SongRepository {
    @GET("/api/searchSong")
    fun searchSong(
        @Query(value = "songName")
        name:String
    ) :Observable<MutableList<SongSearch>>

    @GET("/api/linkMusic")
    fun getLinkMusic(
        @Query(value = "linkSong")
        linkSong:String
    ) :Observable<SongLink>
}
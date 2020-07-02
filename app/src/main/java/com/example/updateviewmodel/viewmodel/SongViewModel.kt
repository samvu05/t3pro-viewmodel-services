package com.example.updateviewmodel.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.updateviewmodel.model.SongSearch
import com.example.updateviewmodel.repository.SongRepository
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SongViewModel {
    //notification den view chi su dung isLoading
    val isLoading = ObservableBoolean(false)
    private val repository: SongRepository
    val songs = MutableLiveData<MutableList<SongSearch>>()
    val finishGetLinkSong = MutableLiveData<SongSearch>()


    init {
        repository = Retrofit.Builder()
            .baseUrl("https://songserver.herokuapp.com")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(
                RxJava2CallAdapterFactory.create()
            ).build()
            .create(SongRepository::class.java)

    }

    fun searchSong(name: String): Disposable {
        isLoading.set(true)
        return repository.searchSong(name)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                //thanh cong
                isLoading.set(false)
                songs.value = it
            }, {
                //that bai
                isLoading.set(false)
            })
    }

    fun getFullLinkSong(linkSong: String): Disposable {
        isLoading.set(true)
        return repository.getLinkMusic(linkSong)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                //thanh cong
                isLoading.set(false)
                for (item in songs.value!!){
                    if (item.linkSong!!.equals(linkSong)){
                        item.linkMusic = it.link
                        finishGetLinkSong.value=item
                    }
                }
            }, {
                //that bai
                isLoading.set(false)
            })
    }
}
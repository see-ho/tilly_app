package com.seeho.tilly.feature.home

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    fun onTilClick(id: Long) {
         Log.d("HomeViewModel", "onTilClick: $id")
    }
}

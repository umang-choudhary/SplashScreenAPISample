package com.demo.splashscreenapisample

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @author Nav Singh
 */
class MainViewModel : ViewModel() {
    private var dataLoaded: Boolean = false

    fun mockDataLoading(): Boolean {
        viewModelScope.launch(Dispatchers.IO) {
            delay(4000)
            dataLoaded = true
        }
        return dataLoaded
    }
}

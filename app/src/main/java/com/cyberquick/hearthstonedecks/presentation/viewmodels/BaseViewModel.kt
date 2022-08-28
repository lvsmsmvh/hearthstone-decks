package com.cyberquick.hearthstonedecks.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel: ViewModel() {

    protected fun <T> LiveData<T>.postValue(value: T) {
        (this as? MutableLiveData)?.postValue(value)
    }
}
package com.cyberquick.hearthstonedecks.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyberquick.hearthstonedecks.domain.common.Result
import com.cyberquick.hearthstonedecks.utils.delayIfExecutionTimeIsSmall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

open class BaseViewModel: ViewModel() {

    /**
     * We need this jobs to cancel them in the 'clear()' method,
     * so all of them will stop their work and they will not change
     * the live data anymore after 'clear()' is called.
     */
    private var jobs = mutableListOf<Job>()

    protected fun createJob() = Job().apply { jobs.add(this) }

    override fun onCleared() {
        super.onCleared()
        Log.i("tag_vm", "onCleared")
        jobs.forEach { it.cancel() }
        jobs.clear()
    }

    protected fun <T> LiveData<T>.postValue(value: T?) {
        (this as? MutableLiveData)?.postValue(value)
    }

    protected fun <T> LiveData<T>.setToDefault() {
        (this as? MutableLiveData)?.value = null
    }

    /**
     * Delay if we get error and response time was very small, because user
     * might get the idea that the app has not reacted on his click.
     */
    protected fun <T : Any> makeLoadingRequest(
        liveData: LiveData<LoadingState<T>>,
        allowInterrupt: Boolean = false,
        source: suspend (() -> Result<T>),
    ): Job {
        val job = createJob()
        viewModelScope.launch(job + Dispatchers.IO) {
            if (liveData.value.isLoading() && !allowInterrupt) return@launch
            liveData.postValue(LoadingState.Loading)
            val startTime = System.currentTimeMillis()
            val response = source()
            yield()
            if (response is Result.Error) {
                val executionTime = System.currentTimeMillis() - startTime
                delayIfExecutionTimeIsSmall(executionTime)
            }
            liveData.postValue(LoadingState.fromResult(response))
        }
        return job
    }
}
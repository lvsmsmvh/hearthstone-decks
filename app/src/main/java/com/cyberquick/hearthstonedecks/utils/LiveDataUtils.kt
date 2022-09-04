package com.cyberquick.hearthstonedecks.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

fun <A, B> transformWithDefault(
    source: LiveData<A>,
    defaultValue: B,
    mapper: (A) -> B,
): LiveData<B> {

    val mediatorLiveData = MediatorLiveData<B>()
    mediatorLiveData.value = defaultValue!!

    mediatorLiveData.addSource(source) { aValue ->
        mediatorLiveData.postValue(mapper(aValue))
    }

    return mediatorLiveData
}

fun <A, B, C> transformWithDoubleTrigger(
    firstSource: LiveData<A>,
    secondSource: LiveData<B>,
    mapper: (A?, B?) -> C,
): LiveData<C> {

    val mediatorLiveData = MediatorLiveData<C>()

    mediatorLiveData.addSource(firstSource) {
        mediatorLiveData.value = mapper(it, secondSource.value)
    }
    mediatorLiveData.addSource(secondSource) {
        mediatorLiveData.value = mapper(firstSource.value, it)
    }
    return mediatorLiveData
}

//class DoubleTrigger<A, B>(
//    a: LiveData<A>,
//    b: LiveData<B>,
//) : MediatorLiveData<Pair<A?, B?>>() {
//    init {
//        addSource(a) { aValue ->
//            val bValue = b.value
//            value = aValue to bValue
//        }
//        addSource(b) { bValue ->
//            val aValue = a.value
//            value = aValue to bValue
//        }
//    }
//}
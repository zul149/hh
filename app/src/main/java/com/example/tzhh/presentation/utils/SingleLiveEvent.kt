package com.example.tzhh.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class SingleLiveEvent<T> : MutableLiveData<T>() {
    private val observers = mutableSetOf<Observer<in T>>()

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, observer)
        observers.add(observer)
    }

    override fun setValue(value: T?) {
        super.setValue(value)
        observers.forEach {
            if (value != null) {
                it.onChanged(value)
            }
        }
    }

    fun call() {
        setValue(null)
    }
}
package com.wonddak.fakems

import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LockViewModel : ViewModel() {

    private val _lockState = MutableStateFlow(false)

    val lockState: StateFlow<Boolean>
        get() = _lockState


    fun toggleState() {
        viewModelScope.launch {
            _lockState.value = !_lockState.value
        }
    }

    fun lock() {
        viewModelScope.launch {
            _lockState.value = true
        }
    }

    fun unlock() {
        viewModelScope.launch {
            _lockState.value = false
        }
    }
}
package com.infrastructure

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

    val uiStateViewModel = MutableLiveData<UiState>()

}

enum class UiState {
    SUCCESS, LOADING, ERROR, DONE
}
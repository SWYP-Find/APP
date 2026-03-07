package com.example.swypapp.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage: SharedFlow<String> = _errorMessage.asSharedFlow()

    private val exceptionHandler = CoroutineExceptionHandler{_, throwable ->
        _isLoading.value = false
        viewModelScope.launch{
            _errorMessage.emit(throwable.message ?: "알 수 없는 오류가 발생했습니다.")
        }
    }

    protected fun execute(
        showLoading: Boolean = true,
        action: suspend () -> Unit
    ){
        viewModelScope.launch(exceptionHandler){
            if (showLoading) _isLoading.value = true
            try{
                action()
            } finally {
                if (showLoading) _isLoading.value = false
            }
        }
    }
}
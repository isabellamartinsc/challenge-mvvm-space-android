package com.devpass.spaceapp.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devpass.spaceapp.model.LaunchpadDetail
import com.devpass.spaceapp.repository.LaunchpadDetailRepository
import com.devpass.spaceapp.repository.LaunchpadDetailRepositoryImpl
import com.devpass.spaceapp.utils.NetworkResult

class LaunchpadDetailViewModel(
    val repository: LaunchpadDetailRepository = LaunchpadDetailRepositoryImpl()
) : ViewModel() {

    private val _launchpadDetail: MutableLiveData<LaunchpadDetailUIState> = MutableLiveData()
    val launchpadDetail: LiveData<LaunchpadDetailUIState> = _launchpadDetail

    private suspend fun safeLaunchpadDetailCall(id: String) {
        _launchpadDetail.postValue(LaunchpadDetailUIState.Loading)

        runCatching {
            repository.fetchLaunchpadDetails(id)
        }.onSuccess {
            if (it is NetworkResult.Success) {
                _launchpadDetail.postValue(LaunchpadDetailUIState.Success(it.data))
            }
            if (it is NetworkResult.Error) {
                _launchpadDetail.postValue(LaunchpadDetailUIState.Error(it.exception))
            }
        }.onFailure {
            _launchpadDetail.postValue(LaunchpadDetailUIState.Error(it))
        }
    }

    sealed interface LaunchpadDetailUIState {
        object Loading : LaunchpadDetailUIState
        data class Error(val error: Throwable) : LaunchpadDetailUIState
        data class Success(val data: LaunchpadDetail) : LaunchpadDetailUIState
    }
}

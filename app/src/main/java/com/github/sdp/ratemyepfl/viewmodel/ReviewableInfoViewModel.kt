package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.activity.ReviewActivity
import com.github.sdp.ratemyepfl.database.GradeInfoRepository
import kotlinx.coroutines.launch

open class ReviewableInfoViewModel(
    private val gradeInfoRepo: GradeInfoRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Id
    val id: String =
        savedStateHandle.get<String>(ReviewActivity.EXTRA_ITEM_REVIEWED_ID)!!

    val averageGrade = MutableLiveData(0.0)
    val numReviews = MutableLiveData(0)

    protected fun refreshGrade(){
        viewModelScope.launch {
            val gradeInfo = gradeInfoRepo.getGradeInfoById(id)
            if(gradeInfo != null){
                averageGrade.postValue(gradeInfo.currentGrade)
                numReviews.postValue(gradeInfo.numReviews)
            }
        }
    }

}

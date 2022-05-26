package com.github.sdp.ratemyepfl.viewmodel.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.backend.database.Storage
import com.github.sdp.ratemyepfl.backend.database.UserRepository
import com.github.sdp.ratemyepfl.backend.database.query.QueryState
import com.github.sdp.ratemyepfl.model.ImageFile
import com.github.sdp.ratemyepfl.model.user.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RankingViewModel @Inject constructor(
    private val userRepo: UserRepository,
    private val imageStorage: Storage<ImageFile>
) : ViewModel() {

    val topUsers: Triple<MutableLiveData<User>, MutableLiveData<User>, MutableLiveData<User>> =
        Triple(MutableLiveData<User>(), MutableLiveData<User>(), MutableLiveData<User>())
    val topUsersPictures = Top3UserPictures()

    init {
        viewModelScope.launch {
            refreshUsers()
        }
    }

    fun getTop3Pictures(): Triple<Flow<ImageFile>, Flow<ImageFile>, Flow<ImageFile>> {
        val users = topUsers.toList()
            .map { it.value ?: throw IllegalStateException("There are no top 3 users") }
        val images = users.map { it.getId() }
            .map { imageStorage.get(it) }
            .take(3)

        return Triple(images[0], images[1], images[2])
    }

    fun getPicture(user: User): Flow<ImageFile> =
        imageStorage.get(user.getId())

    suspend fun getTop3Users(): Flow<Triple<User, User, User>> =
        userRepo.getTopKarmaUsers().mapResult {
            Triple(it[0], it[1], it[2])
        }.filter { it !is QueryState.Loading }
            .map { top3 ->
                when (top3) {
                    is QueryState.Failure -> throw (top3.error)
                    is QueryState.Loading -> throw NoSuchElementException("No top3 users")
                    is QueryState.Success -> top3.data
                }
            }


    suspend fun refreshUsers(): Triple<User, User, User> {
        val users = getTop3Users().last()
        topUsers.first.value = users.first
        topUsers.second.value = users.second
        topUsers.third.value = users.third
        return users
    }

    data class Top3UserPictures(
        val first: MutableLiveData<ImageFile> = MutableLiveData(),
        val second: MutableLiveData<ImageFile> = MutableLiveData(),
        val third: MutableLiveData<ImageFile> = MutableLiveData()
    )
}
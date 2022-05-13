package com.github.sdp.ratemyepfl.exceptions

import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.github.sdp.ratemyepfl.viewmodel.filter.ReviewableFilter

class IncompatibleFilter(errorMessage: String) : Exception(errorMessage) {
}
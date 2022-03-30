package com.github.sdp.ratemyepfl.model.items

interface ReviewableBuilder<T: Reviewable> {
    /**
     * Build the reviewable
     *
     * @return the built reviewable, if the mandatory properties were given
     * @throws IllegalStateException: if mandatory properties are missing
     */
    fun build(): T
}
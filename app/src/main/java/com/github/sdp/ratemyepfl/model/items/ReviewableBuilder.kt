package com.github.sdp.ratemyepfl.model.items

interface ReviewableBuilder<T : Reviewable> {
    /**
     * Build the reviewable
     *
     * @return the built reviewable, if the mandatory properties were given
     * @throws IllegalStateException: if mandatory properties are missing
     */
    fun build(): T

    /**
     * Define a function that manages the same way mandatory properties, i.e.,
     * non nullable properties that were not set
     *
     * @param field: value of the property to test
     * @return the value as non-null, or throw an exception otherwise
     * @throws IllegalStateException: if the value of the field is null
     */
    infix fun <U> asMandatory(field: U?): U =
        field ?: throw IllegalStateException("A mandatory field cannot be null")
}
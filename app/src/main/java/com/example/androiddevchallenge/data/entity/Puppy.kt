package com.example.androiddevchallenge.data.entity

data class Puppy (
    val weight: Eight? = null,
    val height: Eight? = null,
    val id: Long? = null,
    val name: String? = null,
    val bredFor: String? = null,
    val breedGroup: String? = null,
    val lifeSpan: String? = null,
    val temperament: String? = null,
    val origin: String? = null,
    val referenceImageID: String? = null,
    val image: Image? = null,
    val countryCode: String? = null,
    val description: String? = null,
    val history: String? = null
){

    data class Eight (
        val imperial: String? = null,
        val metric: String? = null
    )

    data class Image (
        val id: String? = null,
        val width: Long? = null,
        val height: Long? = null,
        val url: String? = null
    )
}
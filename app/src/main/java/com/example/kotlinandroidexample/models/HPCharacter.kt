package com.example.kotlinandroidexample.models

import android.os.Parcel
import android.os.Parcelable

data class HPCharacter(
    val id: String?,
    val name: String? = null,
    val alternateNames: List<String>? = null,
    val species: String? = null,
    val gender: String? = null,
    val house: String? = null,
    val dateOfBirth: String? = null,
    val yearOfBirth: Long? = null,
    val wizard: Boolean? = null,
    val ancestry: String? = null,
    val eyeColour: String? = null,
    val hairColour: String? = null,
    val wand: Wand? = null,
    val patronus: String? = null,
    val hogwartsStudent: Boolean? = null,
    val hogwartsStaff: Boolean? = null,
    val actor: String? = null,
    val alternateActors: List<String>? = null,
    val alive: Boolean? = null,
    val image: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.createStringArrayList(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        null,
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readString(),
        parcel.createStringArrayList(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeStringList(alternateNames)
        parcel.writeString(species)
        parcel.writeString(gender)
        parcel.writeString(house)
        parcel.writeString(dateOfBirth)
        parcel.writeValue(yearOfBirth)
        parcel.writeValue(wizard)
        parcel.writeString(ancestry)
        parcel.writeString(eyeColour)
        parcel.writeString(hairColour)
        parcel.writeString(patronus)
        parcel.writeValue(hogwartsStudent)
        parcel.writeValue(hogwartsStaff)
        parcel.writeString(actor)
        parcel.writeStringList(alternateActors)
        parcel.writeValue(alive)
        parcel.writeString(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HPCharacter> {
        override fun createFromParcel(parcel: Parcel): HPCharacter {
            return HPCharacter(parcel)
        }

        override fun newArray(size: Int): Array<HPCharacter?> {
            return arrayOfNulls(size)
        }
    }
}

data class Wand(
    val wood: String?,
    val core: String?,
    val length: Float? = null
)

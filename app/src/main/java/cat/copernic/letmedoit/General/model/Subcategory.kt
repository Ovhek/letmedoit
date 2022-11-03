package cat.copernic.letmedoit.General.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Subcategory(
    val nombre : String,
    val id : String
):Parcelable

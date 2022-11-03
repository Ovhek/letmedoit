package cat.copernic.letmedoit.General.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue


data class Category(
    var nombre: String,
    var subcategories: ArrayList<Subcategory>,
    val image: String,
    val id : String
)

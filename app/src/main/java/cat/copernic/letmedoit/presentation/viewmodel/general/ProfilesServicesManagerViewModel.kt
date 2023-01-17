package cat.copernic.letmedoit.presentation.viewmodel.general

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Clase viewMode que comunica la posicion del servicion con la vista
 */
class ProfilesServicesManagerViewModel : ViewModel() {
    val pagePosition = MutableLiveData<Int>()

    /**
     * Introduce la posicion
     * @param pagePosition
     */
    fun sendPagePosition(pagePosition: Int) {
        this.pagePosition.value = pagePosition
    }
}
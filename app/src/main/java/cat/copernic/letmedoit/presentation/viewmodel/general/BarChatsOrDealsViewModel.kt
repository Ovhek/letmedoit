package cat.copernic.letmedoit.presentation.viewmodel.general

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Clase viewModel para futuras versiones
 */
class BarChatsOrDealsViewModel:  ViewModel()  {
    val option = MutableLiveData<Int>()

    /**
     * Cambia el estado
     * @param option
     */
    fun sendOption(option : Int){
        this.option.value = option
    }
}
package cat.copernic.letmedoit.domain.repositories

import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.Utils.LanguageConstants
import cat.copernic.letmedoit.data.model.*
import kotlinx.coroutines.flow.Flow


interface UserRepository {

    //get
    suspend fun getUser (idUser : String) : Flow<DataState<Users>>
    suspend fun getServices () : Flow<DataState<ArrayList<String>>>
    suspend fun getFavoriteProfiles() : Flow<DataState<ArrayList<String>>>
    suspend fun getFavoriteServices() : Flow<DataState<ArrayList<String>>>
    suspend fun getChats () : Flow<DataState<ArrayList<String>>>
    suspend fun getHistoryDeals  () : Flow<DataState<ArrayList<HistoryDeal>>>
    suspend fun getOpinions () : Flow<DataState<ArrayList<Opinions>>>

    //delete
    suspend fun deleteService (idService : String) : Flow<DataState<Boolean>>
    suspend fun deleteFavoriteProfile(idProfile: String) : Flow<DataState<Boolean>>
    suspend fun deleteFavoriteService(idService: String) : Flow<DataState<Boolean>>

    //add
    suspend fun addService(idService : String) : Flow<DataState<Boolean>>
    suspend fun addFavoriteProfiles(idProfile : String) : Flow<DataState<Boolean>>
    suspend fun addFavoriteServices(idService: String) : Flow<DataState<Boolean>>
    suspend fun addChat(idChat: String) : Flow<DataState<Boolean>>
    suspend fun addHistoryDeal(idUser: String, idDeal : String) : Flow<DataState<Boolean>>
    suspend fun addOpinion(opinion : Opinions) : Flow<DataState<Boolean>>

    //update
    suspend fun updateName(newName : String) : Flow<DataState<Boolean>>
    suspend fun updateSurname(newSurname : String) : Flow<DataState<Boolean>>
    suspend fun updateEmail(newEmail : String) : Flow<DataState<Boolean>>
    suspend fun updatePassword(oldPassword : String, newPassword : String) : Flow<DataState<Boolean>>
    suspend fun updateLanguage(language : LanguageConstants) : Flow<DataState<Boolean>>
    suspend fun updateDarkTheme(darkTheme : Boolean) : Flow<DataState<Boolean>>
    suspend fun updateAvatar(imgLink : String) : Flow<DataState<Boolean>>
    suspend fun updateCurriculum(pdfLink : String) : Flow<DataState<Boolean>>
    suspend fun updateAboutMe(aboutMe : String) : Flow<DataState<Boolean>>
    suspend fun updateContactInfo(contactInfo : ContactInfoMap) : Flow<DataState<Boolean>>
    suspend fun updateLocation(newLocation : String) : Flow<DataState<Boolean>>
    suspend fun updateRating(updatedRating : Float) : Flow<DataState<Boolean>>
}
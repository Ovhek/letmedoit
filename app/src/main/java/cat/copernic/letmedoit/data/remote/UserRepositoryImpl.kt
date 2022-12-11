package cat.copernic.letmedoit.data.remote

import android.net.Uri
import cat.copernic.letmedoit.Utils.Constants
import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.Utils.UserConstants
import cat.copernic.letmedoit.Utils.Utils
import cat.copernic.letmedoit.data.model.*
import cat.copernic.letmedoit.di.FirebaseModule
import cat.copernic.letmedoit.domain.repositories.UserRepository
import com.google.android.gms.tasks.Tasks.await
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.auth.User
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class UserRepositoryImpl @Inject constructor(
    @FirebaseModule.UsersCollection val usersCollection: CollectionReference
) : UserRepository {

    //GET
    override suspend fun getUser(idUser: String): Flow<DataState<Users?>> = flow {
        emit(DataState.Loading)
        try {
            val user: Users

            usersCollection.document(idUser).get().await().let { document ->
                user = document.toObject(Users::class.java)!!
                user.id = document.id
            }

            emit(DataState.Success(user))
            emit(DataState.Finished)
        } catch (e: Exception) {
            emit(DataState.Error(e))
            emit(DataState.Finished)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getServices(idUser: String): Flow<DataState<ArrayList<UserServices>>> =
        flow {
            emit(DataState.Loading)
            try {
                val generalArrayList =
                    usersCollection.document(idUser).collection(UserConstants.SERVICES)
                        .get()
                        .await()
                        .toObjects(UserServices::class.java)

                emit(DataState.Success(ArrayList(generalArrayList)))
                emit(DataState.Finished)
            } catch (e: Exception) {
                emit(DataState.Error(e))
                emit(DataState.Finished)
            }
        }.flowOn(Dispatchers.IO)

    override suspend fun getFavoriteProfiles(): Flow<DataState<ArrayList<UserFavoriteProfiles>>> =
        flow {
            emit(DataState.Loading)
            try {
                val generalArrayList =
                    usersCollection.document(Constants.USER_LOGGED_IN_ID)
                        .collection(UserConstants.FAVORITE_PROFILES)
                        .get()
                        .await()
                        .toObjects(UserFavoriteProfiles::class.java)

                emit(DataState.Success(ArrayList(generalArrayList)))
                emit(DataState.Finished)
            } catch (e: Exception) {
                emit(DataState.Error(e))
                emit(DataState.Finished)
            }
        }.flowOn(Dispatchers.IO)

    override suspend fun getFavoriteServices(): Flow<DataState<ArrayList<UserFavoriteServices>>> =
        flow {
            emit(DataState.Loading)
            try {
                val generalArrayList =
                    usersCollection.document(Constants.USER_LOGGED_IN_ID)
                        .collection(UserConstants.FAVORITE_SERVICES)
                        .get()
                        .await()
                        .toObjects(UserFavoriteServices::class.java)

                emit(DataState.Success(ArrayList(generalArrayList)))
                emit(DataState.Finished)
            } catch (e: Exception) {
                emit(DataState.Error(e))
                emit(DataState.Finished)
            }
        }

    override suspend fun getChats(): Flow<DataState<ArrayList<UserChats>>> = flow {
        emit(DataState.Loading)
        try {
            val generalArrayList =
                usersCollection.document(Constants.USER_LOGGED_IN_ID)
                    .collection(UserConstants.CHATS)
                    .get()
                    .await()
                    .toObjects(UserChats::class.java)

            emit(DataState.Success(ArrayList(generalArrayList)))
            emit(DataState.Finished)
        } catch (e: Exception) {
            emit(DataState.Error(e))
            emit(DataState.Finished)
        }
    }


    override suspend fun getHistoryDeals(): Flow<DataState<ArrayList<HistoryDeal>>> = flow {
        var historyDeals = ArrayList<HistoryDeal>()
        emit(DataState.Loading)
        try {

            val historyDealsColllection = usersCollection.document(Constants.USER_LOGGED_IN_ID)
                .collection(UserConstants.HISTORY_DEALS).get().await()

            var userDeals = ArrayList<UserDeals>()
            historyDealsColllection.documents.forEach { historyDeal ->
                var id = historyDeal.id
                userDeals.addAll(
                    usersCollection
                        .document(Constants.USER_LOGGED_IN_ID)
                        .collection(UserConstants.HISTORY_DEALS)
                        .document(id).collection("deals")
                        .get()
                        .await()
                        .toObjects(UserDeals::class.java)
                )
                historyDeals.add(HistoryDeal(id, userDeals))
            }
            emit(DataState.Success(ArrayList(historyDeals)))
            emit(DataState.Finished)
        } catch (e: Exception) {
            emit(DataState.Error(e))
            emit(DataState.Finished)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getOpinions(idUser: String): Flow<DataState<ArrayList<Opinion>>> = flow {
        emit(DataState.Loading)
        try {
            val opinions = usersCollection.document(idUser).collection(UserConstants.OPINIONS)
                .get()
                .await()
                .toObjects(Opinion::class.java)

            emit(DataState.Success(ArrayList(opinions)))
            emit(DataState.Finished)
        } catch (e: Exception) {
            emit(DataState.Error(e))
            emit(DataState.Finished)
        }
    }.flowOn(Dispatchers.IO)

    //DELETE
    override suspend fun deleteService(idService: String): Flow<DataState<Boolean>> = flow {
        var isSuccesful = false
        emit(DataState.Loading)
        try {
            Constants.USER_LOGGED_IN_ID.let {
                usersCollection.document(it).collection(UserConstants.SERVICES).document(idService)
                    .delete()
                    .addOnSuccessListener { data ->
                        isSuccesful = true
                    }
                    .addOnFailureListener { error ->
                        isSuccesful = false
                        throw Exception(error)
                    }
                    .await()
            }
            emit(DataState.Success(isSuccesful))
            emit(DataState.Finished)
        } catch (e: Exception) {
            emit(DataState.Error(e))
            emit(DataState.Finished)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun deleteFavoriteProfile(idProfile: String): Flow<DataState<Boolean>> = flow {
        var isSuccesful = false
        emit(DataState.Loading)
        try {
            Constants.USER_LOGGED_IN_ID.let {
                usersCollection.document(it).collection(UserConstants.FAVORITE_PROFILES)
                    .document(idProfile)
                    .delete()
                    .addOnSuccessListener { data ->
                        isSuccesful = true
                    }
                    .addOnFailureListener { error ->
                        isSuccesful = false
                        throw Exception(error)
                    }
                    .await()
            }
            emit(DataState.Success(isSuccesful))
            emit(DataState.Finished)
        } catch (e: Exception) {
            emit(DataState.Error(e))
            emit(DataState.Finished)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun deleteFavoriteService(idService: String): Flow<DataState<Boolean>> = flow {
        var isSuccesful = false
        emit(DataState.Loading)
        try {
            Constants.USER_LOGGED_IN_ID.let {
                usersCollection.document(it).collection(UserConstants.FAVORITE_SERVICES)
                    .document(idService)
                    .delete()
                    .addOnSuccessListener { data ->
                        isSuccesful = true
                    }
                    .addOnFailureListener { error ->
                        isSuccesful = false
                        throw Exception(error)
                    }
                    .await()
            }
            emit(DataState.Success(isSuccesful))
            emit(DataState.Finished)
        } catch (e: Exception) {
            emit(DataState.Error(e))
            emit(DataState.Finished)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun deleteAvatarFromStorage(imgLink: String): Flow<DataState<Boolean>> = flow {
        var isSuccessful = false
        emit(DataState.Loading)
        val sRef: StorageReference =
            FirebaseModule.storageProvider().getReferenceFromUrl(imgLink)

        try {
            sRef.delete()
                .addOnSuccessListener {
                    isSuccessful = true
                }
                .addOnFailureListener {
                    throw Exception(it)
                }
                .await()

            emit(DataState.Success(isSuccessful))
            emit(DataState.Finished)
        } catch (e: Exception) {
            emit(DataState.Error(e))
            emit(DataState.Finished)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun deleteCurriculumFromStorage(pdfLink: String): Flow<DataState<Boolean>> =
        flow {
            var isSuccessful = false
            emit(DataState.Loading)
            val sRef: StorageReference =
                FirebaseModule.storageProvider().getReferenceFromUrl(pdfLink)

            try {
                sRef.delete()
                    .addOnSuccessListener {
                        isSuccessful = true
                    }
                    .addOnFailureListener {
                        throw Exception(it)
                    }
                    .await()

                emit(DataState.Success(isSuccessful))
                emit(DataState.Finished)
            } catch (e: Exception) {
                emit(DataState.Error(e))
                emit(DataState.Finished)
            }
        }.flowOn(Dispatchers.IO)

    override suspend fun deleteDealFromHistory(
        idDeal: String,
        idUser: String
    ): Flow<DataState<Boolean>> = flow {
        emit(DataState.Loading)
        try {
            var uploadSuccesful: Boolean = false
            var newIndex = 0

            val historydeals = usersCollection.document(Constants.USER_LOGGED_IN_ID)
                .collection(UserConstants.HISTORY_DEALS)
                .document(idUser)
                .collection(UserConstants.DEALS)
                .get()
                .await()

            historydeals.documents.forEach {
                var deal = it.toObject(UserDeals::class.java)
                if (deal != null) {
                    if (deal.deal_id == idDeal)
                        usersCollection.document(Constants.USER_LOGGED_IN_ID)
                            .collection(UserConstants.HISTORY_DEALS)
                            .document(idUser)
                            .collection(UserConstants.DEALS).document(it.id)
                            .delete()
                            .await()
                }
            }
            emit(DataState.Success(uploadSuccesful))
            emit(DataState.Finished)
        } catch (e: Exception) {
            emit(DataState.Error(e))
            emit(DataState.Finished)
        }
    }.flowOn(Dispatchers.IO)


    //ADD
    override suspend fun addService(idService: String): Flow<DataState<Boolean>> = flow {
        emit(DataState.Loading)
        try {

            val newIndex = getIndex(UserConstants.SERVICES) + 1

            var uploadSuccesful: Boolean = false
            Constants.USER_LOGGED_IN_ID.let {
                usersCollection.document(it).collection(UserConstants.SERVICES)
                    .document(newIndex.toString())
                    .set(UserServices(idService), SetOptions.merge())
                    .addOnSuccessListener { uploadSuccesful = true }
                    .addOnFailureListener { uploadSuccesful = false }
                    .await()
            }
            emit(DataState.Success(uploadSuccesful))
            emit(DataState.Finished)
        } catch (e: Exception) {
            emit(DataState.Error(e))
            emit(DataState.Finished)
        }
    }.flowOn(Dispatchers.IO)

    private suspend fun getIndex(collection: String): Int {
        return try {
            usersCollection.document(Constants.USER_LOGGED_IN_ID).collection(collection).get()
                .await().documents.last().id.toInt()
        } catch (e: Exception) {
            return 0
        }
    }

    override suspend fun addFavoriteProfiles(idProfile: String): Flow<DataState<Boolean>> = flow {
        emit(DataState.Loading)
        try {
            var uploadSuccesful: Boolean = false
            Constants.USER_LOGGED_IN_ID.let {
                usersCollection.document(it).collection(UserConstants.FAVORITE_PROFILES)
                    .document(idProfile).set(UserFavoriteProfiles(idProfile), SetOptions.merge())
                    .addOnSuccessListener { uploadSuccesful = true }
                    .addOnFailureListener { uploadSuccesful = false }
                    .await()
            }
            emit(DataState.Success(uploadSuccesful))
            emit(DataState.Finished)
        } catch (e: Exception) {
            emit(DataState.Error(e))
            emit(DataState.Finished)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun addFavoriteServices(idService: String): Flow<DataState<Boolean>> = flow {
        emit(DataState.Loading)
        try {
            var uploadSuccesful: Boolean = false
            Constants.USER_LOGGED_IN_ID.let {
                usersCollection.document(it).collection(UserConstants.FAVORITE_SERVICES)
                    .document(idService).set(UserFavoriteServices(idService), SetOptions.merge())
                    .addOnSuccessListener { uploadSuccesful = true }
                    .addOnFailureListener { uploadSuccesful = false }
                    .await()
            }
            emit(DataState.Success(uploadSuccesful))
            emit(DataState.Finished)
        } catch (e: Exception) {
            emit(DataState.Error(e))
            emit(DataState.Finished)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun addChat(idChat: String): Flow<DataState<Boolean>> = flow {
        emit(DataState.Loading)
        try {
            val newIndex = getIndex(UserConstants.CHATS) + 1

            var uploadSuccesful: Boolean = false
            Constants.USER_LOGGED_IN_ID.let {
                usersCollection.document(it).collection(UserConstants.CHATS)
                    .document(newIndex.toString())
                    .set(UserChats(idChat), SetOptions.merge())
                    .addOnSuccessListener { uploadSuccesful = true }
                    .addOnFailureListener { uploadSuccesful = false }
                    .await()
            }
            emit(DataState.Success(uploadSuccesful))
            emit(DataState.Finished)
        } catch (e: Exception) {
            emit(DataState.Error(e))
            emit(DataState.Finished)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun addHistoryDeal(
        idUserOne: String,
        idUserTwo: String,
        idDeal: String
    ): Flow<DataState<Boolean>> =
        flow {
            emit(DataState.Loading)
            try {
                var uploadSuccesful: Boolean = false
                var newIndex = 0

                val historydeals =
                    usersCollection.document(idUserOne).collection(UserConstants.HISTORY_DEALS)
                        .document(idUserTwo)
                        .collection(UserConstants.DEALS)
                        .get()
                        .await()

                newIndex = if (historydeals.documents.size == 0) 0
                else historydeals.documents.last().id.toInt() + 1

                idUserOne.let {
                    usersCollection.document(it).collection(UserConstants.HISTORY_DEALS)
                        .document(idUserTwo).set(DummyData(), SetOptions.merge()).await()

                    usersCollection.document(it).collection(UserConstants.HISTORY_DEALS)
                        .document(idUserTwo).collection(UserConstants.DEALS)
                        .document(newIndex.toString())
                        .set(UserDeals(idDeal), SetOptions.merge())
                        .addOnSuccessListener { uploadSuccesful = true }
                        .addOnFailureListener { uploadSuccesful = false }
                        .await()
                }
                emit(DataState.Success(uploadSuccesful))
                emit(DataState.Finished)
            } catch (e: Exception) {
                emit(DataState.Error(e))
                emit(DataState.Finished)
            }
        }.flowOn(Dispatchers.IO)

    override suspend fun addOpinion(opinion: Opinion,idUser : String): Flow<DataState<Boolean>> = flow {
        emit(DataState.Loading)
        try {
            var uploadSuccesful: Boolean = false
            idUser.let {
                usersCollection.document(it).collection(UserConstants.OPINIONS).document(opinion.id)
                    .set(opinion, SetOptions.merge())
                    .addOnSuccessListener { uploadSuccesful = true }
                    .addOnFailureListener { uploadSuccesful = false }
                    .await()
            }

            val opinions = idUser.let {
                usersCollection.document(it)
                    .collection(UserConstants.OPINIONS)
                    .get()
                    .await().toObjects(Opinion::class.java)
            }
            var media = 0f
            opinions.forEach{
                media+=it.rating
            }
            media/opinions.size

            updateRating(media,idUser).collect{ dataState->
                when(dataState){
                    is DataState.Success<Boolean> -> {
                        emit(DataState.Success(dataState.data))
                        emit(DataState.Finished)
                    }
                    is DataState.Error -> {
                        emit(DataState.Error(dataState.exception))
                        emit(DataState.Finished)
                    }
                    is DataState.Loading -> {}
                    else -> Unit
                }
            }
        } catch (e: Exception) {
            emit(DataState.Error(e))
            emit(DataState.Finished)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun addAvatarToStorage(fileUri: Uri): Flow<DataState<String>> = flow {
        var uri = ""
        emit(DataState.Loading)
        val sRef: StorageReference =
            FirebaseModule.storageProvider().reference.child("usersImages/${Constants.USER_LOGGED_IN_ID}/avatar")

        try {
            val downloadUrl = sRef.putFile(fileUri)
                .addOnFailureListener { throw Exception(it) }.await()
                .storage.downloadUrl.await()

            uri = downloadUrl.toString()

            emit(DataState.Success(uri))
            emit(DataState.Finished)
        } catch (e: Exception) {
            emit(DataState.Error(e))
            emit(DataState.Finished)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun addCurriculumToStorage(fileUri: Uri): Flow<DataState<String>> = flow {
        var uri = ""
        emit(DataState.Loading)
        val sRef: StorageReference =
            FirebaseModule.storageProvider().reference.child("usersCurriculums/${Constants.USER_LOGGED_IN_ID}/curriculum.pdf")

        try {
            val downloadUrl = sRef.putFile(fileUri)
                .addOnFailureListener { throw Exception(it) }.await()
                .storage.downloadUrl.await()

            uri = downloadUrl.toString()

            emit(DataState.Success(uri))
            emit(DataState.Finished)
        } catch (e: Exception) {
            emit(DataState.Error(e))
            emit(DataState.Finished)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun updateName(newName: String): Flow<DataState<Boolean>> = flow {
        emit(DataState.Loading)
        try {
            var uploadSuccesful: Boolean = false
            Constants.USER_LOGGED_IN_ID.let {
                usersCollection.document(it).update(UserConstants.NAME, newName)
                    .addOnSuccessListener { uploadSuccesful = true }
                    .addOnFailureListener { uploadSuccesful = false }
                    .await()
            }
            emit(DataState.Success(uploadSuccesful))
            emit(DataState.Finished)
        } catch (e: Exception) {
            emit(DataState.Error(e))
            emit(DataState.Finished)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun updateSurname(newSurname: String): Flow<DataState<Boolean>> = flow {
        emit(DataState.Loading)
        try {
            var uploadSuccesful: Boolean = false
            Constants.USER_LOGGED_IN_ID.let {
                usersCollection.document(it).update(UserConstants.SURNAME, newSurname)
                usersCollection.document(it).update(UserConstants.SURNAME, newSurname)
                    .addOnSuccessListener { uploadSuccesful = true }
                    .addOnFailureListener { uploadSuccesful = false }
                    .await()
            }
            emit(DataState.Success(uploadSuccesful))
            emit(DataState.Finished)
        } catch (e: Exception) {
            emit(DataState.Error(e))
            emit(DataState.Finished)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun updatePassword(
        oldPassword: String,
        newPassword: String,
        email: String
    ): Flow<DataState<Boolean>> = flow<DataState<Boolean>> {
        emit(DataState.Loading)
        try {
            var changeSuccesful: Boolean = false

            val credential = EmailAuthProvider
                .getCredential(email, oldPassword)

            val user = FirebaseModule.firebaseAuthProvider().currentUser!!

            user.reauthenticate(credential)
                .addOnSuccessListener {
                    user.updatePassword(newPassword)
                        .addOnSuccessListener {
                            changeSuccesful = true
                        }
                        .addOnFailureListener {
                            throw Exception(it)
                        }
                }
                .addOnFailureListener {
                    throw Exception(it)
                }
                .await()
            emit(DataState.Success(changeSuccesful))
            emit(DataState.Finished)
        } catch (e: Exception) {
            emit(DataState.Error(e))
            emit(DataState.Finished)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun updateLanguage(language: Int): Flow<DataState<Boolean>> =
        flow<DataState<Boolean>> {
            emit(DataState.Loading)

            try {
                var uploadSuccesful: Boolean = false
                Constants.USER_LOGGED_IN_ID.let {
                    usersCollection.document(it).update(UserConstants.LANGUAGE, language)
                        .addOnSuccessListener { uploadSuccesful = true }
                        .addOnFailureListener { uploadSuccesful = false }
                        .await()
                }
                emit(DataState.Success(uploadSuccesful))
                emit(DataState.Finished)
            } catch (e: Exception) {
                emit(DataState.Error(e))
                emit(DataState.Finished)
            }
        }.flowOn(Dispatchers.IO)

    override suspend fun updateDarkTheme(darkTheme: Boolean): Flow<DataState<Boolean>> = flow {
        emit(DataState.Loading)

        try {
            var uploadSuccesful: Boolean = false
            Constants.USER_LOGGED_IN_ID.let {
                usersCollection.document(it).update(UserConstants.DARK_THEME, darkTheme)
                    .addOnSuccessListener { uploadSuccesful = true }
                    .addOnFailureListener { uploadSuccesful = false }
                    .await()
            }
            emit(DataState.Success(uploadSuccesful))
            emit(DataState.Finished)
        } catch (e: Exception) {
            emit(DataState.Error(e))
            emit(DataState.Finished)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun updateAvatar(imgLink: String): Flow<DataState<Boolean>> = flow {
        emit(DataState.Loading)

        try {
            var uploadSuccesful: Boolean = false
            Constants.USER_LOGGED_IN_ID.let {
                usersCollection.document(it).update(UserConstants.AVATAR, imgLink)
                    .addOnSuccessListener { uploadSuccesful = true }
                    .addOnFailureListener { uploadSuccesful = false }
                    .await()
            }
            emit(DataState.Success(uploadSuccesful))
            emit(DataState.Finished)
        } catch (e: Exception) {
            emit(DataState.Error(e))
            emit(DataState.Finished)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun updateCurriculum(pdfLink: String): Flow<DataState<Boolean>> = flow {
        emit(DataState.Loading)

        try {
            var uploadSuccesful: Boolean = false
            Constants.USER_LOGGED_IN_ID.let {
                usersCollection.document(it).update(UserConstants.CURRICULUM, pdfLink)
                    .addOnSuccessListener { uploadSuccesful = true }
                    .addOnFailureListener { uploadSuccesful = false }
                    .await()
            }
            emit(DataState.Success(uploadSuccesful))
            emit(DataState.Finished)
        } catch (e: Exception) {
            emit(DataState.Error(e))
            emit(DataState.Finished)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun updateAboutMe(aboutMe: String): Flow<DataState<Boolean>> = flow {
        emit(DataState.Loading)

        try {
            var uploadSuccesful: Boolean = false
            Constants.USER_LOGGED_IN_ID.let {
                usersCollection.document(it).update(UserConstants.ABOUT_ME, aboutMe)
                    .addOnSuccessListener { uploadSuccesful = true }
                    .addOnFailureListener { uploadSuccesful = false }
                    .await()
            }
            emit(DataState.Success(uploadSuccesful))
            emit(DataState.Finished)
        } catch (e: Exception) {
            emit(DataState.Error(e))
            emit(DataState.Finished)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun updateSchedule(schedule: ScheduleMap): Flow<DataState<Boolean>> = flow {
        emit(DataState.Loading)
        try {
            var uploadSuccesful: Boolean = false
            Constants.USER_LOGGED_IN_ID.let {
                usersCollection.document(it).update(UserConstants.SCHEDULE, schedule)
                    .addOnSuccessListener { uploadSuccesful = true }
                    .addOnFailureListener { uploadSuccesful = false }
                    .await()
            }
            emit(DataState.Success(uploadSuccesful))
            emit(DataState.Finished)
        } catch (e: Exception) {
            emit(DataState.Error(e))
            emit(DataState.Finished)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun updateContactInfo(contactInfo: ContactInfoMap): Flow<DataState<Boolean>> =
        flow {
            emit(DataState.Loading)

            try {
                var uploadSuccesful: Boolean = false
                Constants.USER_LOGGED_IN_ID.let {
                    usersCollection.document(it).update(UserConstants.CONTACT_INFO, contactInfo)
                        .addOnSuccessListener { uploadSuccesful = true }
                        .addOnFailureListener { uploadSuccesful = false }
                        .await()
                }
                emit(DataState.Success(uploadSuccesful))
                emit(DataState.Finished)
            } catch (e: Exception) {
                emit(DataState.Error(e))
                emit(DataState.Finished)
            }
        }.flowOn(Dispatchers.IO)

    override suspend fun updateLocation(newLocation: String): Flow<DataState<Boolean>> = flow {
        emit(DataState.Loading)

        try {
            var uploadSuccesful: Boolean = false
            Constants.USER_LOGGED_IN_ID.let {
                usersCollection.document(it).update(UserConstants.LOCATION, newLocation)
                    .addOnSuccessListener { uploadSuccesful = true }
                    .addOnFailureListener { uploadSuccesful = false }
                    .await()
            }
            emit(DataState.Success(uploadSuccesful))
            emit(DataState.Finished)
        } catch (e: Exception) {
            emit(DataState.Error(e))
            emit(DataState.Finished)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun updateRating(updatedRating: Float,idUser: String): Flow<DataState<Boolean>> = flow {
        emit(DataState.Loading)

        try {
            var uploadSuccesful = false
            idUser.let {
                usersCollection.document(it).update(UserConstants.RATING, updatedRating)
                    .addOnSuccessListener { uploadSuccesful = true }
                    .addOnFailureListener { uploadSuccesful = false }
                    .await()
            }
            emit(DataState.Success(uploadSuccesful))
            emit(DataState.Finished)
        } catch (e: Exception) {
            emit(DataState.Error(e))
            emit(DataState.Finished)
        }
    }.flowOn(Dispatchers.IO)


}
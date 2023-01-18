package cat.copernic.letmedoit.data.remote

import android.provider.ContactsContract.Data
import cat.copernic.letmedoit.Utils.Constants
import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.data.model.Deal
import cat.copernic.letmedoit.di.FirebaseModule
import cat.copernic.letmedoit.domain.repositories.DealRepository
import com.android.car.ui.toolbar.MenuItem.builder
import com.google.android.gms.tasks.Tasks.await
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.auth.User
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Clase que implementa la interfaz DealRepository que permite conectarse a la base de datos remota de Firebase y realizar operaciones CRUD en los tratos
 * Utiliza la librería de coroutines de Kotlin para manejar operaciones asíncronas y emitir flujos de datos (flow) para informar el estado de las operaciones.
 *
 * @param dealCollection referencia a la colección de tratos en la base de datos de Firebase. Inyectado mediante la anotación.
 *
 */
class DealRepositoryImpl @Inject constructor(
    @FirebaseModule.DealCollection val dealCollection: CollectionReference
) : DealRepository {

    /**
     * Inserta un trato en la base de datos de Firebase,
     * @param deal trato a insertar.
     */
    override suspend fun insertDeal(deal: Deal): Flow<DataState<Boolean>> = flow {
        emit(DataState.Loading)
        try {

            var uploadStatus: Boolean = false
            deal.id.let {
                dealCollection.document(it).set(deal, SetOptions.merge())
                    .addOnSuccessListener { uploadStatus = true }
                    .addOnFailureListener { uploadStatus = false }
                    .await()
            }
            emit(DataState.Success(uploadStatus))
            emit(DataState.Finished)
        } catch (e: Exception) {
            emit(DataState.Error(e))
            emit(DataState.Finished)
        }

    }.flowOn(Dispatchers.IO)

    /**
     * Cancela y borra un trato de la base de datos de Firebase.
     * @param id id del trato a cancelar.
     */
    override suspend fun denyDeal(id: String): Flow<DataState<Boolean>> = flow {
        emit(DataState.Loading)
        try {
            var uploadStatus: Boolean = false
            id.let {
                dealCollection.document(it).delete()
                    .addOnSuccessListener { uploadStatus = true }
                    .addOnFailureListener { uploadStatus = false }
                    .await()
            }
            emit(DataState.Success(uploadStatus))
            emit(DataState.Finished)
        } catch (e: Exception) {
            emit(DataState.Error(e))
            emit(DataState.Finished)
        }

    }.flowOn(Dispatchers.IO)

    /**
     * Cambia el estado del trato de la base de datos a "accepted".
     * @param idDeal id del trato el cual aceptar.
     */
    override suspend fun acceptDeal(idDeal: String): Flow<DataState<Boolean>> = flow {
        emit(DataState.Loading)
        try {
            var uploadStatus: Boolean = false
            idDeal.let {
                dealCollection.document(it).update("accepted", true)
                    .addOnSuccessListener { uploadStatus = true }
                    .addOnFailureListener { uploadStatus = false }
                    .await()
            }

            emit(DataState.Success(uploadStatus))
            emit(DataState.Finished)
        } catch (e: Exception) {
            emit(DataState.Error(e))
            emit(DataState.Finished)
        }

    }.flowOn(Dispatchers.IO)

    /**
     * Función encargada de conluir el trato. Actualiza el estado según:
     * - 0: Nadie ha concluido el trato.
     * - 1: El usuario 1 ha concluido el trato.
     * - 2: El usuario 2 ha concluido el trato.
     * - 3: Los dos usuarios han conlcuido el trato.
     * @param id id del trato a actualizar su estado.
     */
    override suspend fun concludeDeal(id: String): Flow<DataState<Boolean>> = flow {
        emit(DataState.Loading)
        try {
            var uploadStatus: Boolean = false

            dealCollection.document(id).get().await().toObject(Deal::class.java)?.let { deal ->
                var dealStatus = deal.conclude

                when(dealStatus){
                    0 ->{
                        if (Constants.USER_LOGGED_IN_ID == deal.users.userOneId) dealStatus = 1
                        if (Constants.USER_LOGGED_IN_ID == deal.users.userTwoId) dealStatus = 2
                    }
                    1 ->{
                        if (Constants.USER_LOGGED_IN_ID == deal.users.userTwoId) dealStatus = 3
                    }
                    2 -> {
                        if (Constants.USER_LOGGED_IN_ID == deal.users.userOneId) dealStatus = 3
                    }
                }
                dealCollection.document(id).update("conclude", dealStatus)
                    .addOnSuccessListener { uploadStatus = true }
                    .addOnFailureListener { uploadStatus = false }
                    .await()
            }
            emit(DataState.Success(uploadStatus))
            emit(DataState.Finished)
        } catch (e: Exception) {
            emit(DataState.Error(e))
            emit(DataState.Finished)
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Devuelve el trato especifico con la id pasada.
     * @param id del trato a obtener.
     */
    override suspend fun getDeal(id: String): Flow<DataState<Deal>>  = flow{
        emit(DataState.Loading)
        try {
            var tempDeal = Deal()

            val dealColRef = dealCollection.document(id).get().await()
            dealColRef.toObject(Deal::class.java).let { deal ->
                if (deal != null) {
                    tempDeal = deal
                    tempDeal.id = dealColRef.id
                }
            }
            emit(DataState.Success(tempDeal))
            emit(DataState.Finished)
        } catch (e: Exception) {
            emit(DataState.Error(e))
            emit(DataState.Finished)
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Devuelve todos los tratos de la base de datos.
     */
    override suspend fun getDeals(): Flow<DataState<List<Deal>>> = flow{
        emit(DataState.Loading)
        try {
            val deals = ArrayList<Deal>()

            val dbDeals = dealCollection.get().await()
            deals.addAll(dbDeals.toObjects(Deal::class.java))

            deals.forEachIndexed { i, deal ->
                deal.id = dbDeals.documents[i].id
            }
            emit(DataState.Success(deals))
            emit(DataState.Finished)
        } catch (e: Exception) {
            emit(DataState.Error(e))
            emit(DataState.Finished)
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Suscribe a actualizaciones sobre el trato. Cada vez que se actualiza algún valor del trato, se llama esta función.
     * @param idDeal trato sobre el cual suscribirse.
     */
    override suspend fun suscribeForUpdates(idDeal: String): Flow<DataState<Deal?>> = callbackFlow{
        send(DataState.Loading)
        try {
            var tempDeal : Deal? = null

            dealCollection.document(idDeal).addSnapshotListener{ deal, error ->
                if(error != null) close(error)
                if (deal != null) {
                    tempDeal = deal.toObject(Deal::class.java)
                    if(tempDeal == null) return@addSnapshotListener
                    tempDeal!!.id = idDeal
                    trySend(DataState.Success(tempDeal))
                    trySend(DataState.Finished)
                }
            }
        } catch (e: Exception) {
            send(DataState.Error(e))
            send(DataState.Finished)
        }
        finally {
            awaitClose()
        }
    }.flowOn(Dispatchers.IO)
}
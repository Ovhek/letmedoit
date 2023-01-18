package cat.copernic.letmedoit.data.remote

import android.net.Uri
import android.provider.ContactsContract.Data
import cat.copernic.letmedoit.Utils.Constants
import cat.copernic.letmedoit.data.model.Service
import cat.copernic.letmedoit.domain.repositories.ServiceRepository
import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.Utils.ServiceConstants
import cat.copernic.letmedoit.Utils.datahepers.CategoryMap
import cat.copernic.letmedoit.data.model.Image
import javax.inject.Inject
import cat.copernic.letmedoit.di.FirebaseModule
import com.google.android.gms.tasks.Tasks.await
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await

/**
 * Clase que implementa la interfaz ServiceRepository que permite conectarse a la base de datos remota de Firebase y realizar operaciones CRUD en los servicios.
 * Utiliza la librería de coroutines de Kotlin para manejar operaciones asíncronas y emitir flujos de datos (flow) para informar el estado de las operaciones.
 *
 * @param serviceCollection referencia a la colección de servicios en la base de datos de Firebase. Inyectado mediante la anotación.
 *
 */
class ServiceRepositoryImpl @Inject constructor(
    private val storage: FirebaseStorage,
    @FirebaseModule.ServiceCollection private val serviceCollection: CollectionReference
) : ServiceRepository {

    /**
     * Función encargada de guardar un servicio en la base de datos.
     *
     * @param service Servicio a guardar.
     */
    override suspend fun saveService(service: Service): Flow<DataState<Service>> = flow {
        emit(DataState.Loading)
        try {
            var uploadSuccesful: Boolean = false
            //Guardamos el usuario, pero si ya existe un documento con estos datos hace un merge sobreescrbiendo los datos.
            service.id.let {
                serviceCollection.document(it).set(service, SetOptions.merge())
                    .addOnSuccessListener { uploadSuccesful = true }
                    .addOnFailureListener { uploadSuccesful = false }
                    .await()

                for ((index, value) in service.image.withIndex()) {
                    serviceCollection.document(service.id)
                        .collection(Constants.SERVICES_COLLECTION_IMAGES).document(index.toString())
                        .set(value, SetOptions.merge())
                }
            }
            if (!uploadSuccesful)
                throw Exception("Error Uploading Service")

            emit(DataState.Success(service))
            emit(DataState.Finished)
        } catch (e: Exception) {
            emit(DataState.Error(e))
            emit(DataState.Finished)
        }
    }.flowOn(Dispatchers.IO)


    /**
     * Obtiene el servicio especificado.
     * @param uid id del servicio a obtener.
     */
    override suspend fun getService(uid: String): Flow<DataState<Service>> = flow {
        emit(DataState.Loading)
        try {
            var service : Service

            serviceCollection.document(uid).get().await().let { document ->
                service = document.toObject(Service::class.java)!!
                service.id = document.id
            }

            getServiceImages(service)
            emit(DataState.Success(service))
            emit(DataState.Finished)

        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Obtiene todos los servicios de la base de datos.
     */
    override suspend fun getAllServices(): Flow<DataState<List<Service>>> = flow {
        emit(DataState.Loading)
        try {
            val services = ArrayList<Service>()

            serviceCollection.get().await().documents.forEach { document ->
                var service = document.toObject(Service::class.java)
                if (service != null) {
                    service.id = document.id
                    services.add(service)
                }
            }

            services.forEach {
                getServiceImages(it)
            }

            emit(DataState.Success(services))
            emit(DataState.Finished)

        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Elimina una imagen del storage de la base de datos.
     * @param idService id del servicio donde esta la imagen.
     * @param imgIdService indice de la imagen a eliminar.
     * @param imgLink Dirrección URL de la imagen en storage.
     */
    override suspend fun removeImage(idService: String, imgIndex: Int,imgLink : String): Flow<DataState<Boolean>> = flow {
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

            serviceCollection.document(idService)
                .collection(Constants.SERVICES_COLLECTION_IMAGES).document(imgIndex.toString()).delete().await()

            emit(DataState.Success(isSuccessful))
            emit(DataState.Finished)
        } catch (e: Exception) {
            emit(DataState.Error(e))
            emit(DataState.Finished)
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Elimina un servicio de la base de datos.
     * @param idService id del servicio a eliminar.
     */
    override suspend fun removeService(idService: String): Flow<DataState<Boolean>> = flow{
        var isSuccessful = false
        emit(DataState.Loading)
        try {
            val imagesDocuments = serviceCollection.document(idService).collection(ServiceConstants.IMAGES).get().await().documents
            imagesDocuments.forEach {
                val image = it.toObject(Image::class.java)
                if (image != null) {
                    FirebaseModule.storageProvider().getReferenceFromUrl(image.img_link).delete().await()
                    serviceCollection.document(idService).collection(ServiceConstants.IMAGES).document(it.id).delete().await()
                }
            }
            serviceCollection.document(idService)
                .delete()
                .addOnSuccessListener { isSuccessful = true  }
                .addOnFailureListener { isSuccessful = false }
                .await()


            emit(DataState.Success(isSuccessful))
            emit(DataState.Finished)
        } catch (e: Exception) {
            emit(DataState.Error(e))
            emit(DataState.Finished)
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Obtiene las imagenes del servicio.
     * @param service servicio del cual obtener las imagenes.
     */
    suspend fun getServiceImages(service: Service) {
        val ref = serviceCollection.document(service.id).collection(ServiceConstants.IMAGES).orderBy("index").get().await()
        val images = ref.toObjects(Image::class.java)

        ref.documents.forEachIndexed { i, document ->
            images[i].id = document.id
        }
        service.image.removeAll(service.image.toSet())
        service.image.addAll(images)
    }

    /**
     * Actualiza el titulo de un servicio.
     * @param idService id del servicio a actualizar,
     * @param newTitle nuevo valor del titulo.
     */
    override suspend fun updateTitle(
        idService: String,
        newTitle: String
    ): Flow<DataState<Boolean>> = flow {
        emit(DataState.Loading)
        try {
            var uploadSuccesful: Boolean = false
            idService.let {
                serviceCollection.document(it).update(ServiceConstants.TITLE, newTitle)
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

    /**
     * Actualiza la descripción de un servicio.
     *
     * @param idService id del servicio a actualizar.
     * @param newDescription nuevo valor de la descripción.
     */
    override suspend fun updateDescription(
        idService: String,
        newDescription: String
    ): Flow<DataState<Boolean>> = flow {
        emit(DataState.Loading)
        try {
            var uploadSuccesful: Boolean = false
            idService.let {
                serviceCollection.document(it).update(ServiceConstants.DESCRIPTION, newDescription)
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

    /**
     * Actualiza la categoría de un servicio.
     *
     * @param idService id del servicio.
     * @param newDescription nuevo valor de la categoría de un servicio.
     */
    override suspend fun updateCategory(
        idService: String,
        newCategoryMap: CategoryMap
    ): Flow<DataState<Boolean>> = flow {
        emit(DataState.Loading)
        try {
            var uploadSuccesful: Boolean = false
            idService.let {
                serviceCollection.document(it).update(ServiceConstants.CATEGORY, newCategoryMap)
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

    /**
     * Actualiza los favoritos de un servicio.
     *
     * @param idService id del servicio.
     * @param newNum nuevo valor de favoritos de un servicio.
     */
    override suspend fun updateNLikes(idService: String, newNum: Int): Flow<DataState<Boolean>> =
        flow {
            emit(DataState.Loading)
            try {
                var uploadSuccesful: Boolean = false
                idService.let {
                    serviceCollection.document(it).update(ServiceConstants.N_LIKES, newNum)
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

    /**
     * Actualiza el último tiempo de edición del servicio.
     *
     * @param idService id del servicio a actualizar.
     * @param newEditedTime Nuevo valor de la fecha de edición.
     */
    override suspend fun updateEditedTime(
        idService: String,
        newEditedTime: String
    ): Flow<DataState<Boolean>> = flow {
        emit(DataState.Loading)
        try {
            var uploadSuccesful: Boolean = false
            idService.let {
                serviceCollection.document(it).update(ServiceConstants.EDITED_TIME, newEditedTime)
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

    /**
     * Actualiza las images de un servicio.
     *
     * @param idService id del servicio a actualizar.
     * @param newFile Nueva dirección de la imagen
     * @param index indice de la imagen a actualizar.
     */
    override suspend fun editServiceImage(
        idService : String,newFileURI : Uri, index: Int
    ): Flow<DataState<String>> = flow {

        emit(DataState.Loading)
        try {
            var newUrl = ""
            //deleteServiceImage(newFileURI)

            var service = Service()
            saveServiceImage(newFileURI, idService, index).collect{ dataState ->
                when(dataState){
                    is DataState.Success<String> -> {
                        newUrl = dataState.data
                        serviceCollection.document(idService)
                            .collection(Constants.SERVICES_COLLECTION_IMAGES).document(index.toString())
                            .set(Image(index.toString(),newUrl,index), SetOptions.merge()).await()
                    }
                    is DataState.Error -> {
                    }
                    is DataState.Loading -> {  }
                    else -> Unit
                }

            }
            emit(DataState.Success(newUrl))
            emit(DataState.Finished)
        } catch (e: Exception) {
            emit(DataState.Error(e))
            emit(DataState.Finished)
        }
    }.flowOn(Dispatchers.IO)

    lateinit var uri: String

    /**
     * Guarda la imagen de un servicio.
     *
     * @param fileURI dirección de la imagen.
     * @param serviceId Id del servicio a actualizar.
     * @param index Indice de la imagen.
     */
    override suspend fun saveServiceImage(
        fileURI: Uri,
        serviceId: String,
        index: Int
    ): Flow<DataState<String>> = flow {

        uri = ""
        emit(DataState.Loading)
        val sRef: StorageReference =
            storage.reference.child("serviceImages/${serviceId}/${index}")

        try {
            val downloadUrl = sRef.putFile(fileURI)
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
}
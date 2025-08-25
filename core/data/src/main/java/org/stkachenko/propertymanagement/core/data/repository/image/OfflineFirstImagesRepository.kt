package org.stkachenko.propertymanagement.core.data.repository.image

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.stkachenko.propertymanagement.core.data.Synchronizer
import org.stkachenko.propertymanagement.core.data.changeListSync
import org.stkachenko.propertymanagement.core.data.model.image.asEntity
import org.stkachenko.propertymanagement.core.database.dao.image.ImageDao
import org.stkachenko.propertymanagement.core.database.model.image.ImageWithAttachment
import org.stkachenko.propertymanagement.core.database.model.image.asExternalModel
import org.stkachenko.propertymanagement.core.datastore.ChangeListVersions
import org.stkachenko.propertymanagement.core.model.data.image.Image
import org.stkachenko.propertymanagement.core.network.PmNetworkDataSource
import org.stkachenko.propertymanagement.core.network.model.image.NetworkImage
import javax.inject.Inject

internal class OfflineFirstImagesRepository @Inject constructor(
    private val imageDao: ImageDao,
    private val network: PmNetworkDataSource,
) : ImagesRepository {

    override fun getImagesByRelatedId(id: String): Flow<List<Image>> =
        imageDao.getImageEntitiesByRelatedId(id)
            .map { it.map(ImageWithAttachment::asExternalModel) }

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean =
        synchronizer.changeListSync(
            versionReader = ChangeListVersions::imageVersion,
            changeListFetcher = { currentVersion ->
                network.getImageChangeList(after = currentVersion)
            },
            versionUpdater = { latestVersion ->
                copy(imageVersion = latestVersion)
            },
            modelDeleter = imageDao::deleteImages,
            modelUpdater = { changedIds ->
                val networkImages = network.getImages(ids = changedIds)
                imageDao.upsertImages(
                    entities = networkImages.map(NetworkImage::asEntity)
                )
            },
        )
}
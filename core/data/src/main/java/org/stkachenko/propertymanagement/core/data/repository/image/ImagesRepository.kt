package org.stkachenko.propertymanagement.core.data.repository.image

import kotlinx.coroutines.flow.Flow
import org.stkachenko.propertymanagement.core.data.Syncable
import org.stkachenko.propertymanagement.core.model.data.image.Image

interface ImagesRepository: Syncable {
    fun getImagesByRelatedId(id: String): Flow<List<Image>>
}
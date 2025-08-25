package org.stkachenko.propertymanagement.sync.status

interface SyncSubscriber {
    suspend fun subscribe()
}
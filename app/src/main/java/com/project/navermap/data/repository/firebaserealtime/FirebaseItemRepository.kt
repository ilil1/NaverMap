package com.project.navermap.data.repository.firebaserealtime

import android.util.Log
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.project.navermap.data.entity.firebase.ItemEntity
import com.project.navermap.di.annotation.dispatchermodule.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseItemRepository @Inject constructor(
    private val database: FirebaseDatabase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ItemRepository {

    @Volatile
    private lateinit var reference: DatabaseReference

    override fun getItemsByMarketId(marketId: String): Flow<List<ItemEntity>> = callbackFlow {
        reference = database.getReference("/marketItems/${marketId}")

        reference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    trySend(snapshot.getValue<List<ItemEntity>>() ?: emptyList())
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("TAG", "onCancelled: $error")
                }
            }
        )

        awaitClose()
    }
}
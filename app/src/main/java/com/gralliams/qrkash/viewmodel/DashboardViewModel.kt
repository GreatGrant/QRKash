package com.gralliams.qrkash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DashboardViewModel : ViewModel() {
    private val _userName = MutableLiveData<String?>()
    val userName: MutableLiveData<String?>
        get() = _userName

    private val _balance = MutableLiveData<Long>()
    val balance: LiveData<Long>
        get() = _balance

    init {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val displayName = currentUser?.displayName

        if (displayName != null) {
            _userName.value = displayName
            val userId: String = currentUser.uid
            retrieveWallet(userId)
        } else {
            _userName.value = "User"
        }
    }

    private fun retrieveWallet(userId: String) {
        val db = FirebaseFirestore.getInstance()
        val walletsCollection = db.collection("wallets")
        walletsCollection.document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val wallet = document.data
                    val balance = wallet?.get("balance") as Long
                    _balance.value = balance
                } else {
                    // Handle case when the wallet document does not exist
                }
            }
            .addOnFailureListener { e ->
                // Handle wallet retrieval failure
            }
    }
}

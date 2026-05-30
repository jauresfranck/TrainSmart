package com.example.trainsmart.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.trainsmart.data.database.TrainSmartDatabase
import com.example.trainsmart.data.entity.Seance
import com.example.trainsmart.data.repository.SeanceRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SeanceViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SeanceRepository
    val allSeances: LiveData<List<Seance>>

    init {
        val dao = TrainSmartDatabase.getDatabase(application).seanceDao()
        repository = SeanceRepository(dao)
        allSeances = repository.allSeances
    }

    fun insert(seance: Seance) = viewModelScope.launch {
        repository.insert(seance)
    }

    fun update(seance: Seance) = viewModelScope.launch {
        repository.update(seance)
    }

    fun delete(seance: Seance) = viewModelScope.launch {
        repository.delete(seance)
    }

    suspend fun getCharge7Jours(): Int = withContext(Dispatchers.IO) {
        repository.getCharge7Jours()
    }

    suspend fun getCharge28Jours(): Int = withContext(Dispatchers.IO) {
        repository.getCharge28Jours()
    }

    suspend fun calculerACWR(): Float = withContext(Dispatchers.IO) {
        repository.calculerACWR()
    }

    fun getZoneACWR(acwr: Float): String {
        return repository.getZoneACWR(acwr)
    }

    suspend fun getSeances7Jours(): List<Seance> = withContext(Dispatchers.IO) {
        repository.getSeances7Jours()
    }
}
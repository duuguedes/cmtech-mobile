package com.example.cmtech.repository

import android.util.Log
import com.example.cmtech.model.Bateria
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class BateriaRepository {
    private val BASE_URL = "http://10.0.2.2:8080/baterias"
    private val cliente = OkHttpClient()
    private val gson = Gson()

    fun buscarBaterias(callback: (List<Bateria>?, String?) -> Unit) {
        val request = Request.Builder()
            .url(BASE_URL)
            .get()
            .build()

        cliente.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("BATERIA_REPOSITORY", "Erro ao buscar baterias: ${e.message}", e)
                callback(null, e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    val errorMessage = "Erro na resposta: ${response.message}"
                    Log.e("BATERIA_REPOSITORY", errorMessage)
                    callback(null, errorMessage)
                    return
                }

                val respostaBody = response.body?.string()
                Log.i("BATERIA_REPOSITORY", "Resposta: $respostaBody")

                if (respostaBody.isNullOrEmpty()) {
                    callback(emptyList(), "Nenhuma bateria encontrado")
                    return
                }

                try {
                    val type = object : TypeToken<List<Bateria>>() {}.type
                    val listaBaterias: List<Bateria> = gson.fromJson(respostaBody, type)
                    callback(listaBaterias, null)
                } catch (e: Exception) {
                    Log.e("BATERIA_REPOSITORY", "Erro ao processar resposta: ${e.message}", e)
                    callback(emptyList(), "Erro ao processar resposta")
                }
            }
        })
    }
}
package com.example.cmtech.repository

import android.util.Log
import com.example.cmtech.model.Veiculo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException

class VeiculoRepository {

    private val BASE_URL = "http://10.0.2.2:8080/veiculos"
    private val cliente = OkHttpClient()
    private val gson = Gson()

    fun buscarVeiculos(callback: (List<Veiculo>?, String?) -> Unit) {
        val request = Request.Builder()
            .url(BASE_URL)
            .get()
            .build()

        cliente.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("VEICULO_REPOSITORY", "Erro ao buscar veiculos: ${e.message}")
                callback(null, e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                val respostaBody = response.body?.string()
                Log.i("VEICULO_REPOSITORY", "Resposta: $respostaBody")

                if (response.isSuccessful && !respostaBody.isNullOrEmpty()) {
                    val type = object : TypeToken<List<Veiculo>>() {}.type
                    val listaVeiculos: List<Veiculo> = gson.fromJson(respostaBody, type)
                    callback(listaVeiculos, null)
                } else {
                    callback(emptyList(), "Nenhum veiculo foi encontrado")
                }
            }
        })
    }

    fun gravarVeiculo(veiculo: Veiculo, callback: (Boolean, String?) -> Unit) {
        val veiculoJson = gson.toJson(veiculo)
        val requestBody = veiculoJson.toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url(BASE_URL)
            .post(requestBody)
            .build()

        cliente.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("VEICULO_REPOSITORY", "Erro ao gravar veiculo: ${e.message}")
                callback(false, e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                val respostaBody = response.body?.string()
                if (response.isSuccessful) {
                    Log.i("VEICULO_REPOSITORY", "Veiculo gravado com sucesso. Resposta: $respostaBody")
                    callback(true, null)
                } else {
                    Log.e("VEICULO_REPOSITORY", "Erro ao gravar veiculo: ${response.message}")
                    callback(false, respostaBody ?: "Erro desconhecido")
                }
            }
        })
    }

    fun editarVeiculo(veiculo: Veiculo, callback: (Boolean, String?) -> Unit) {
        val veiculoJson = gson.toJson(veiculo)
        val requestBody = veiculoJson.toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("$BASE_URL/${veiculo.id}")
            .put(requestBody)
            .build()

        cliente.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("VEICULO_REPOSITORY", "Erro ao editar veiculo: ${e.message}")
                callback(false, e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                val respostaBody = response.body?.string()
                if (response.isSuccessful) {
                    Log.i("VEICULO_REPOSITORY", "Veiculo editado com sucesso. Resposta: $respostaBody")
                    callback(true, null)
                } else {
                    Log.e("VEICULO_REPOSITORY", "Erro ao editar veiculo: ${response.message}")
                    callback(false, respostaBody ?: "Erro desconhecido")
                }
            }
        })
    }

    fun excluirVeiculo(id: Long, callback: (Boolean, String?) -> Unit) {
        val request = Request.Builder()
            .url("$BASE_URL/$id")
            .delete()
            .build()

        cliente.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("VEICULO_REPOSITORY", "Erro ao excluir veiculo: ${e.message}")
                callback(false, e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    Log.i("VEICULO_REPOSITORY", "Veiculo excluído com sucesso.")
                    callback(true, null)
                } else {
                    Log.e("VEICULO_REPOSITORY", "Erro ao excluir veiculo: ${response.message}")
                    callback(false, response.body?.string() ?: "Erro desconhecido")
                }
            }
        })
    }
}
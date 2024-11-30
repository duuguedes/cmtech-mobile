package com.example.cmtech.repository

import android.util.Log
import com.example.cmtech.model.EstacaoCarregamento
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class EstacaoCarregamentoRepository {
    private val BASE_URL = "http://10.0.2.2:8080/estacoes-carregamento"
    private val cliente = OkHttpClient()
    private val gson = Gson()

    fun buscarEstacoesCarregamento(callback: (List<EstacaoCarregamento>?, String?) -> Unit) {
        val request = Request.Builder()
            .url(BASE_URL)
            .get()
            .build()

        cliente.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("ESTACAO_CARREGAMENTO_REPOSITORY", "Erro ao buscar estacoes de carregamento: ${e.message}", e)
                callback(null, e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    val errorMessage = "Erro na resposta: ${response.message}"
                    Log.e("ESTACAO_CARREGAMENTO_REPOSITORY", errorMessage)
                    callback(null, errorMessage)
                    return
                }

                val respostaBody = response.body?.string()
                Log.i("ESTACAO_CARREGAMENTO_REPOSITORY", "Resposta: $respostaBody")

                if (respostaBody.isNullOrEmpty()) {
                    callback(emptyList(), "Nenhuma estacao encontrado")
                    return
                }

                try {
                    val type = object : TypeToken<List<EstacaoCarregamento>>() {}.type
                    val listaEstacoesCarregamento: List<EstacaoCarregamento> = gson.fromJson(respostaBody, type)
                    callback(listaEstacoesCarregamento, null)
                } catch (e: Exception) {
                    Log.e("ESTACAO_CARREGAMENTO_REPOSITORY", "Erro ao processar resposta: ${e.message}", e)
                    callback(emptyList(), "Erro ao processar resposta")
                }
            }
        })
    }
}
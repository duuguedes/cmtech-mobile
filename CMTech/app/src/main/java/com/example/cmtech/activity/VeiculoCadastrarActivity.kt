package com.example.cmtech.activity

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.cmtech.R
import com.example.cmtech.model.Veiculo
import com.example.cmtech.repository.VeiculoRepository

class VeiculoCadastrarActivity : Activity() {
    private lateinit var veiculoRepository: VeiculoRepository
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.cadastrar_veiculo)

        sharedPreferences = getSharedPreferences("VeiculoPrefs", Context.MODE_PRIVATE)

        val edtModelo = findViewById<EditText>(R.id.edtModelo)
        val edtAno = findViewById<EditText>(R.id.edtAno)
        val btnEnviar = findViewById<Button>(R.id.btnEnviar)

        // Carregar os dados salvos, se houver
        edtModelo.setText(sharedPreferences.getString("modelo", ""))
        edtAno.setText(sharedPreferences.getInt("ano", 0).toString())

        veiculoRepository = VeiculoRepository()

        btnEnviar.setOnClickListener {
            val modelo = edtModelo.text.toString()
            val ano = edtAno.text.toString().toIntOrNull() ?: 0

            val veiculo = Veiculo(null, modelo, ano)

            with(sharedPreferences.edit()) {
                putString("modelo", modelo)
                putInt("ano", ano)
                apply()
            }

            veiculoRepository.gravarVeiculo(veiculo) { sucesso, mensagem ->
                runOnUiThread {
                    if (sucesso) {
                        Toast.makeText(this, "Veículo gravado com sucesso!", Toast.LENGTH_SHORT).show()
                        edtModelo.text.clear()
                        edtAno.text.clear()

                        // Limpar dados do SharedPreferences após salvar
                        sharedPreferences.edit().clear().apply()
                    } else {
                        Toast.makeText(this, "Erro ao gravar veículo: $mensagem", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
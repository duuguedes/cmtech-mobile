package com.example.cmtech.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.cmtech.R

class HomeActivity : Activity(){

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.home)

        val btnBateria = findViewById<Button>(R.id.txtBateria)
        val btnEstacaoCarregamento = findViewById<Button>(R.id.txtEstacaoCarregamento)
        val btnVeiculo = findViewById<Button>(R.id.txtVeiculo)
        val btnCadVeiculo = findViewById<TextView>(R.id.txtCadVeiculo)

        btnBateria.setOnClickListener {
            val intent = Intent(this, BateriaActivity::class.java)
            startActivity(intent)
        }

        btnEstacaoCarregamento.setOnClickListener {
            val intent = Intent(this, EstacaoCarregamentoActivity::class.java)
            startActivity(intent)
        }

        btnVeiculo.setOnClickListener {
            val intent = Intent(this, VeiculoActivity::class.java)
            startActivity(intent)
        }

        btnCadVeiculo.setOnClickListener {
            val intent = Intent(this, VeiculoCadastrarActivity::class.java)
            startActivity(intent)
        }
    }
}
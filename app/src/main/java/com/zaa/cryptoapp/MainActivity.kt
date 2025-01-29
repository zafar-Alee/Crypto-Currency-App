package com.zaa.cryptoapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.zaa.cryptoapp.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var rvadapter: rvAdapter
    private lateinit var data: ArrayList<Model>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize RecyclerView
        data = ArrayList()
        rvadapter = rvAdapter(this, data) { model -> onItemClick(model) }
        binding.Rv.layoutManager = LinearLayoutManager(this)
        binding.Rv.adapter = rvadapter

        // Search functionality
        binding.search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                val query = p0.toString().lowercase(Locale.getDefault())
                if (query.isNotEmpty()) {
                    val filteredData = data.filter {
                        it.name.lowercase(Locale.getDefault()).contains(query)
                    }
                    if (filteredData.isEmpty()) {
                        Toast.makeText(this@MainActivity, "No data available", Toast.LENGTH_SHORT)
                            .show()
                    }
                    rvadapter.changeData(ArrayList(filteredData))
                } else {
                    rvadapter.changeData(data)
                }
            }
        })

        // Fetch API Data
        fetchApiData()
    }

    @SuppressLint("DefaultLocale", "NotifyDataSetChanged")
    private fun fetchApiData() {
        val url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd"
        val queue = Volley.newRequestQueue(this)

        val jsonArrayRequest = JsonArrayRequest(
            url,
            { response ->
                binding.progressBar.isVisible = false
                try {
                    for (i in 0 until response.length()) {
                        val dataObject = response.getJSONObject(i)

                        // Basic fields
                        val symbol = dataObject.getString("symbol")
                        val name = dataObject.getString("name")
                        val price = dataObject.getDouble("current_price")
                        val formattedPrice = String.format("$%.2f", price)

                        // Optional fields
                        val marketCap = dataObject.optLong("market_cap", 0L)
                        val marketCapRank = dataObject.optInt("market_cap_rank", 0)
                        val high24h = dataObject.optDouble("high_24h", 0.0)
                        val low24h = dataObject.optDouble("low_24h", 0.0)
                        val circulatingSupply = dataObject.optDouble("circulating_supply", 0.0)
                        val totalSupply = dataObject.optDouble("total_supply", 0.0)
                        val maxSupply = dataObject.optDouble("max_supply", 0.0)
                        val ath = dataObject.optDouble("ath", 0.0)
                        val atl = dataObject.optDouble("atl", 0.0)

                        // Add the Model to the list
                        data.add(
                            Model(
                                name,
                                symbol,
                                formattedPrice,
                                marketCap,
                                marketCapRank,
                                high24h,
                                low24h,
                                circulatingSupply,
                                totalSupply,
                                maxSupply,
                                ath,
                                atl
                            )
                        )
                    }
                    rvadapter.notifyDataSetChanged()
                } catch (e: Exception) {
                    e.printStackTrace()
                    // Log the error
                    Toast.makeText(this, "Parsing Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            },
            { error ->
                // Handle network error
                Toast.makeText(this, "Network Error: ${error.message}", Toast.LENGTH_LONG).show()
                println("Network Error: ${error.message}")
            }
        )

        queue.add(jsonArrayRequest)
    }

    @SuppressLint("MissingInflatedId")
    private fun onItemClick(model: Model) {
        // Create and show the dialog with the currency details
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.dialoge, null)

        // Set currency details in the dialog
        val nameTextView: TextView = dialogView.findViewById(R.id.currency_name_detail)
        val symbolTextView: TextView = dialogView.findViewById(R.id.currency_symbol_detail)
        val priceTextView: TextView = dialogView.findViewById(R.id.currency_price_detail)
        val marketCapTextView: TextView = dialogView.findViewById(R.id.currency_market_cap)
        val marketCapRankTextView: TextView = dialogView.findViewById(R.id.currency_market_cap_rank)
        val high24hTextView: TextView = dialogView.findViewById(R.id.currency_high_24h)
        val low24hTextView: TextView = dialogView.findViewById(R.id.currency_low_24h)
        val circulatingSupplyTextView: TextView =
            dialogView.findViewById(R.id.currency_circulating_supply)
        val totalSupplyTextView: TextView = dialogView.findViewById(R.id.currency_total_supply)
        val maxSupplyTextView: TextView = dialogView.findViewById(R.id.currency_max_supply)
        val athTextView: TextView = dialogView.findViewById(R.id.currency_ath)
        val atlTextView: TextView = dialogView.findViewById(R.id.currency_atl)

        // Set the model data into the dialog
        nameTextView.text = model.name
        symbolTextView.text = model.symbol
        priceTextView.text = model.price
        marketCapTextView.text = "Market Cap: ${model.marketCap?.let { "$${it}" } ?: "N/A"}"
        marketCapRankTextView.text = "Market Cap Rank: ${model.marketCapRank ?: "N/A"}"
        high24hTextView.text = "High (24h): ${model.high24h?.let { "$${it}" } ?: "N/A"}"
        low24hTextView.text = "Low (24h): ${model.low24h?.let { "$${it}" } ?: "N/A"}"
        circulatingSupplyTextView.text = "Circulating Supply: ${model.circulatingSupply ?: "N/A"}"
        totalSupplyTextView.text = "Total Supply: ${model.totalSupply ?: "N/A"}"
        maxSupplyTextView.text = "Max Supply: ${model.maxSupply ?: "N/A"}"
        athTextView.text = "ATH: ${model.ath ?: "N/A"}"
        atlTextView.text = "ATL: ${model.atl ?: "N/A"}"

        builder.setView(dialogView)
        builder.setPositiveButton("Close") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }
}
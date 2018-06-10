package hr.markopuk.currencyconversion

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import org.json.JSONArray
import java.net.URL



class MainActivity : AppCompatActivity() {

    private var spinnerFrom: Spinner?=null
    private var spinnerTo: Spinner?=null
    private var submit: Button?=null
    private var result: TextView?=null
    private var value:TextView?=null
    private var mapOfCurrencies: MutableMap<String,Float> = HashMap()
    private var json:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        spinnerFrom=findViewById(R.id.spinnerFrom)
        spinnerTo=findViewById(R.id.spinnerTo)
        submit=findViewById(R.id.submit)
        result=findViewById(R.id.result)
        value=findViewById(R.id.value)

        loadingData().execute()
        while (json==null){}
        val jsonArray=JSONArray(json)
        for (t in 0..jsonArray.length()-1){
            val rate = jsonArray.getJSONObject(t).getString("median_rate")
            val currency=jsonArray.getJSONObject(t).getString("currency_code")
            mapOfCurrencies[currency]=rate.toFloat()
        }
        val currencies =(mapOfCurrencies.keys).toList()
        val dataAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencies)
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerFrom?.adapter=dataAdapter
        spinnerTo?.adapter=dataAdapter


        submit?.setOnClickListener({
            val valueFrom=mapOfCurrencies[spinnerFrom?.getItemAtPosition(spinnerFrom?.getSelectedItemPosition()!!).toString()]
            val valueTo=mapOfCurrencies[spinnerTo?.getItemAtPosition(spinnerTo?.getSelectedItemPosition()!!).toString()]
            try {
                val value = value?.text.toString().toFloat()

                if (valueFrom != null && value != null && valueTo != null) {
                    result?.setText(((valueFrom / valueTo) * value).toString())
                }
            }
            catch (e:Exception){
                Toast.makeText(this,"please enter value",Toast.LENGTH_SHORT).show()
            }
        })

    }


    private inner class loadingData : AsyncTask<Void,Void,String>() {
        override fun doInBackground(vararg params: Void?): String {
            json= URL("http://hnbex.eu/api/v1/rates/daily/").readText()
            return "ok"
        }
    }



}


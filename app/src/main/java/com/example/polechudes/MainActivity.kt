package com.example.polechudes

import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    //переменные для работы со звуком
    var soundPool: SoundPool? = null
    var barabanSound = 0
    var successSound = 0
    var cancelSound = 0
    var winnerSound = 0

    //переменные для работы со словом
    var slovoOtUsera: String? = null
    var chars: Array<Char>? = null
    var chars2: Array<Char>? = null
    var chars3: Array<Char>? = null
    var chars4: Array<Char>? = null
    var chars5: Array<Char>? = null
    //var chars6: Array<Char>? = null
    var slovoNaEkrane: String? = ""
    var value: String? = null
    var char:Char? = null
    var wrongLetters: String? = ""
    var index: Int? = 0
    var index1: Int? = 0
    //var index2: Int? = 0

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var buttonRestart = findViewById<Button>(R.id.buttonRestart)
        buttonRestart.visibility = View.GONE

        soundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(
                    AudioAttributes.USAGE_ASSISTANCE_SONIFICATION
                )
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
            SoundPool.Builder()
                .setMaxStreams(3)
                .setAudioAttributes(audioAttributes)
                .build()
        } else {
            SoundPool(
                3, AudioManager.STREAM_MUSIC, 0
            )
        }
        barabanSound = soundPool!!.load(this, R.raw.baraban, 1)
        successSound = soundPool!!.load(this, R.raw.correct_letter, 1)
        cancelSound = soundPool!!.load(this, R.raw.wrong_letter, 1)
        winnerSound = soundPool!!.load(this, R.raw.winner, 1)
    }

    fun toCharacterArray(str: String): Array<Char> {
        return str.toCharArray().toTypedArray()
    }

    fun findIndex(chars: Array<Char>?, char: Char?): Int {
        return chars!!.indexOf(char)
    }

    fun onClickStartGame(view: View) {
        var etSlovo = findViewById<EditText>(R.id.etSlovo)
        var buttonStart = findViewById<Button>(R.id.buttonStart)
        var etBukva = findViewById<EditText>(R.id.etBukva)
        var buttonVvodBukva = findViewById<Button>(R.id.buttonVvodBukva)

        if(etSlovo.text.toString() == "") {
            Toast.makeText(applicationContext, "Нужно ввести слово", Toast.LENGTH_SHORT).show()
        }
        else {
            mainMelody()
            slovoOtUsera = etSlovo.text.toString()
            addZvezdi()

            etSlovo.visibility = View.GONE
            buttonStart.visibility = View.GONE
            etBukva.visibility = View.VISIBLE
            buttonVvodBukva.visibility = View.VISIBLE
        }
    }

    fun addZvezdi(){
        var tvSlovo = findViewById<TextView>(R.id.tvSlovo)
        for (n in 1..slovoOtUsera!!.length){
            slovoNaEkrane = slovoNaEkrane.plus("*")
        }
        tvSlovo.text = slovoNaEkrane
    }

    fun zapolneniePolya (){
        var neTeBukvi = findViewById<TextView>(R.id.neTeBukvi)
        var etBukva = findViewById<EditText>(R.id.etBukva)

        wrongLetters = wrongLetters.plus(etBukva.text.toString() + "; ")
        neTeBukvi.text = wrongLetters
    }

    fun someFunction(){
        var tvSlovo = findViewById<TextView>(R.id.tvSlovo)
        chars = toCharacterArray(slovoNaEkrane!!)
        chars2 = toCharacterArray(slovoOtUsera!!)
        index = findIndex(chars2,char)
        chars!![index!!] = char!!
        slovoNaEkrane = String(chars!!.toCharArray())
        tvSlovo.text = slovoNaEkrane

        //проверка на повтор буквы в слове
        var newSlovo = slovoOtUsera!!.substringAfter(char!!)
        var newSlovo2 = slovoNaEkrane!!.substringAfter(char!!)
        //var newSlovo3 = newSlovo!!.substringAfter(char!!)
        chars3 = toCharacterArray(newSlovo!!)
        chars4 = toCharacterArray(newSlovo2!!)
        //chars5 = toCharacterArray(newSlovo3!!)

        if(newSlovo?.contains(char!!) == true)        {
            funcPovtor ()
        }
        /*if(newSlovo3?.contains(char!!) == true){
            var tvSlovo = findViewById<TextView>(R.id.tvSlovo)
            index1 = findIndex(chars3,char)
            chars4!![index1!!] = char!!

            index2 = findIndex(chars5,char)
            chars6!![index2!!] = char!!

            slovoNaEkrane = slovoNaEkrane!!.substringBefore(char!!) + char + String(chars4!!.toCharArray()) + char + String(chars6!!.toCharArray())
            tvSlovo.text = slovoNaEkrane
        }*/
    }

    fun funcPovtor (){
        var tvSlovo = findViewById<TextView>(R.id.tvSlovo)
        index1 = findIndex(chars3,char)
        chars4!![index1!!] = char!!
        slovoNaEkrane = slovoNaEkrane!!.substringBefore(char!!) + char + String(chars4!!.toCharArray())
        tvSlovo.text = slovoNaEkrane
    }

    fun check () {
        if (slovoNaEkrane == slovoOtUsera)
        {
            Toast.makeText(applicationContext, "Вы победили!", Toast.LENGTH_LONG).show()
            winner()

            var etBukva = findViewById<EditText>(R.id.etBukva)
            etBukva.setEnabled(false)

            var buttonVvodBukva = findViewById<Button>(R.id.buttonVvodBukva)
            buttonVvodBukva.visibility = View.GONE

            var buttonRestart = findViewById<Button>(R.id.buttonRestart)
            buttonRestart.visibility = View.VISIBLE
        }
    }

    fun onClickCheckBukva(view: View) {
        var etBukva = findViewById<EditText>(R.id.etBukva)

        try {
            char = etBukva.text.toString().single()
            if (slovoOtUsera?.contains(char!!) == true) {
                correctLetter()
                someFunction()
                check ()
            }
            else {
                wrongLetter()
                zapolneniePolya()
            }
            etBukva.text.clear()
        }
        catch (ex: Exception) {
            Toast.makeText(applicationContext, "Нужно ввести букву!", Toast.LENGTH_SHORT).show()
        }
    }

    fun mainMelody(){
        soundPool!!.play( barabanSound,1f, 1f, 0, 0, 1f)
    }

    fun correctLetter(){
        soundPool!!.play(successSound, 1f, 1f, 0, 0, 1f)
    }

    fun wrongLetter(){
        soundPool!!.play(cancelSound, 1f, 1f, 0, 0, 1f)
    }
    fun winner(){
        soundPool!!.play(winnerSound, 1f, 1f, 0, 0, 1f)
    }

    fun onClickReStartGame(view: View) {
        val i = Intent(this@MainActivity, MainActivity::class.java)
        finish()
        overridePendingTransition(0, 0)
        startActivity(i)
        overridePendingTransition(0, 0)
    }
}
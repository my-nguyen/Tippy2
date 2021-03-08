package com.nguyen.tippy2

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import com.nguyen.tippy2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MainActivity"
        const val INITIAL_TIP = 15
    }

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.seekTip.progress = INITIAL_TIP
        binding.tvTipPercent.text = "$INITIAL_TIP%"
        updateTipDescription(INITIAL_TIP)

        binding.seekTip.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                Log.i(TAG, "onProgressChanged $progress")
                binding.tvTipPercent.text = "$progress%"
                updateTipDescription(progress)
                computeTipAndTotal()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })

        binding.etBase.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(editable: Editable?) {
                Log.i(TAG, "afterTextChanged $editable")
                computeTipAndTotal()
            }
        })
    }

    private fun updateTipDescription(tipPercent: Int) {
        val tipDescription = when (tipPercent) {
            in 0..9 -> "Poor"
            in 10..14 -> "Acceptable"
            in 15..19 -> "Good"
            in 20..24 -> "Great"
            else -> "Amazing"
        }
        binding.tvTipDescription.text = tipDescription

        // show a color on a scale from worst to best, based on the tip percentage
        val percent = tipPercent.toFloat()/binding.seekTip.max
        val colorWorstTip = ContextCompat.getColor(this, R.color.colorWorstTip)
        val colorBestTip = ContextCompat.getColor(this, R.color.colorBestTip)
        val color = ArgbEvaluator().evaluate(percent, colorWorstTip, colorBestTip) as Int
        binding.tvTipDescription.setTextColor(color)
    }

    private fun computeTipAndTotal() {
        if (binding.etBase.text.isEmpty()) {
            binding.tvTipAmount.text = ""
            binding.tvTotalAmount.text = ""
        } else {
            // get the value of the base and tip percent
            val base = binding.etBase.text.toString().toDouble()
            val tipPercent = binding.seekTip.progress
            val tipAmount = base * tipPercent / 100
            binding.tvTipAmount.text = "%.2f".format(tipAmount)
            val totalAmount = base + tipAmount
            binding.tvTotalAmount.text = "%.2f".format(totalAmount)
        }
    }
}
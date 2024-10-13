package com.font.metrics

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.font.metrics.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.etTextString.setText(FontMetricsView.DEFAULT_TEXT)
        binding.etFontSize.setText("${FontMetricsView.DEFAULT_FONT_SIZE_PX}")

        binding.updateButton.setOnClickListener {

            binding.viewWindow.setText(binding.etTextString.text.toString())
            val fontSize = try {
                binding.etFontSize.text.toString().toInt()
            } catch (e: NumberFormatException) {
                FontMetricsView.DEFAULT_FONT_SIZE_PX
            }
            binding.viewWindow.setTextSizeInPixels(fontSize)
            updateTextViews()
            hideKeyboard(currentFocus)
        }

        binding.cbTop.setOnCheckedChangeListener { _, isChecked ->
            binding.viewWindow.setTopVisible(
                isChecked
            )
        }
        binding.cbAscent.setOnCheckedChangeListener { _, isChecked ->
            binding.viewWindow.setAscentVisible(
                isChecked
            )
        }
        binding.cbBaseline.setOnCheckedChangeListener { _, isChecked ->
            binding.viewWindow.setBaselineVisible(
                isChecked
            )
        }
        binding.cbDescent.setOnCheckedChangeListener { _, isChecked ->
            binding.viewWindow.setDescentVisible(
                isChecked
            )
        }
        binding.cbBottom.setOnCheckedChangeListener { _, isChecked ->
            binding.viewWindow.setBottomVisible(
                isChecked
            )
        }
        binding.cbTextBounds.setOnCheckedChangeListener { _, isChecked ->
            binding.viewWindow.setBoundsVisible(
                isChecked
            )
        }
        binding.cbWidth.setOnCheckedChangeListener { _, isChecked ->
            binding.viewWindow.setWidthVisible(
                isChecked
            )
        }

        updateTextViews()
    }

    private fun updateTextViews() {
        with(binding) {
            with(viewWindow) {
                tvTop.text = "${fontMetrics.top}"
                tvAscent.text = "${fontMetrics.ascent}"
                tvBaseline.text = "0"
                tvDescent.text = "${fontMetrics.descent}"
                tvBottom.text = "${fontMetrics.bottom}"
                tvTextBounds.text = "w = ${textBounds.width()} h = ${textBounds.height()}"
                tvWidth.text = "$measuredTextWidth"
                tvLeadingValue.text = "${fontMetrics.leading}"
            }
        }
    }

    private fun hideKeyboard(view: View?) {
        if (view != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}

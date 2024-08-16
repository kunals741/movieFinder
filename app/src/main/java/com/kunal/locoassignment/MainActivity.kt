package com.kunal.locoassignment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AbsListView
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kunal.locoassignment.adapter.ResultAdapter
import com.kunal.locoassignment.constants.SortOptions
import com.kunal.locoassignment.databinding.ActivityMainBinding
import com.kunal.locoassignment.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private var layoutManager: GridLayoutManager? = null
    private var adapter: ResultAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        initObserver()
    }

    private fun initView() = with(binding) {
        ivListGridToggle.setOnClickListener {
            handleToggleClick()
        }
        setTextWatcher()
        setRecyclerView()
        setSpinner()
    }

    private fun initObserver() = viewModel.apply {
        searchResultLiveData.observe(this@MainActivity) {
            if (it.isNullOrEmpty()) {
                binding.llNoData.root.isVisible = true
                binding.rvResult.isVisible = false
            } else {
                binding.llNoData.root.isVisible = false
                binding.rvResult.isVisible = true
                adapter?.setData(it)
            }
        }
        progressLiveData.observe(this@MainActivity) {
            binding.progress.isVisible = it
        }
    }

    private fun setTextWatcher() = with(binding) {
        etSearchBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                viewModel.setSearchQuery(s.toString().trim())
                viewModel.clearOldData()
                viewModel.getResult(s.toString().trim())
            }
        })
    }

    private fun handleToggleClick() = with(binding) {
        if (layoutManager?.spanCount == 1) {
            layoutManager?.spanCount = 2
            binding.ivListGridToggle.setBackgroundResource(R.drawable.ic_grid_view)
        } else {
            layoutManager?.spanCount = 1
            binding.ivListGridToggle.setBackgroundResource(R.drawable.ic_list_view)
        }
        adapter?.notifyItemRangeChanged(0, adapter?.itemCount ?: 0)
    }

    private fun setRecyclerView() = with(binding) {
        layoutManager = GridLayoutManager(this@MainActivity, 1)
        rvResult.layoutManager = layoutManager
        adapter = ResultAdapter(layoutManager)
        rvResult.adapter = adapter
        rvResult.addOnScrollListener(
            viewModel.getEndlessScrollListener(
                (layoutManager as GridLayoutManager)
            )
        )
    }

    private fun setSpinner() = with(binding) {
        val adapter =
            ArrayAdapter(
                this@MainActivity,
                android.R.layout.simple_spinner_item,
                viewModel.options
            ).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
        spSort.adapter = adapter
        spSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                viewModel.selectedOption = viewModel.options[position]
                when (viewModel.selectedOption) {
                    SortOptions.YEAR -> {
                        viewModel.sortListByYear()
                    }

                    SortOptions.RATING -> {
                        viewModel.sortListByRating()
                    }
                    SortOptions.RANDOM -> {
                        viewModel.sortListByRandom()
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                //do nothing
            }
        }
    }
}
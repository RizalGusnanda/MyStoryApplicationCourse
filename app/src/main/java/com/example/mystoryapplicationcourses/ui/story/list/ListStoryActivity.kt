package com.example.mystoryapplicationcourses.ui.story.list

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mystoryapplicationcourses.MainActivity
import com.example.mystoryapplicationcourses.R
import com.example.mystoryapplicationcourses.data.utils.Preference
import com.example.mystoryapplicationcourses.databinding.ActivityStoryBinding
import com.example.mystoryapplicationcourses.ui.auth.ViewModelFactory
import com.example.mystoryapplicationcourses.ui.story.create.CreateStoryFragment
import com.example.mystoryapplicationcourses.ui.story.detail.DetailStory
import com.example.mystoryapplicationcourses.ui.story.list.adapter.LoadingAdapterState
import com.example.mystoryapplicationcourses.ui.story.list.adapter.StoryAdapter
import com.example.mystoryapplicationcourses.ui.story.map.MapActivity

class ListStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryBinding
    private lateinit var adapter: StoryAdapter
    private val listStoryViewModel: ListStoryViewModel by viewModels {
        ViewModelFactory(this)
    }

    private var isFabOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        postponeEnterTransition()

        listStoryViewModel.stories.observe(this) { data ->
            if (data != null) {
                adapter.submitData(lifecycle, data)
            }
        }

        setupAdapter()
        binding.fab.setOnClickListener {
            toggleFabMenu()
        }

        binding.fab1.setOnClickListener {
            startActivity(Intent(this, CreateStoryFragment::class.java))
            toggleFabMenu()
        }

        binding.fab2.setOnClickListener {
            Preference.logOut(this)
            startActivity(Intent(this, MainActivity::class.java))
            toggleFabMenu()
        }

        binding.fab3.setOnClickListener {
            startActivity(Intent(this, MapActivity::class.java))
            toggleFabMenu()
        }

        setupBackPressed()

        setupPopupMenu()
    }

    private fun setupAdapter() {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.storyList.layoutManager = layoutManager

        adapter = StoryAdapter { story, imageView, nameView, descView ->
            val intent = Intent(this, DetailStory::class.java).apply {
                putExtra("id", story.id)
                putExtra("name", story.name)
                putExtra("description", story.description)
                putExtra("photoUrl", story.photoUrl)
            }

            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                androidx.core.util.Pair(imageView, imageView.transitionName),
                androidx.core.util.Pair(nameView, nameView.transitionName),
                androidx.core.util.Pair(descView, descView.transitionName)
            )

            startActivity(intent, options.toBundle())
        }

        binding.storyList.adapter = adapter.withLoadStateFooter(
            footer = LoadingAdapterState {
                adapter.retry()
            }
        )
        binding.storyList.viewTreeObserver
            .addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }
    }

    private fun setupBackPressed() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_refresh -> {
                startActivity(Intent(this, ListStoryActivity::class.java))
                toggleFabMenu()
            }
        }

        return super.onOptionsItemSelected(item)
    }



    private fun setupPopupMenu() {
        binding.fab1.visibility = View.INVISIBLE
        binding.fab2.visibility = View.INVISIBLE
        binding.fab3.visibility = View.INVISIBLE

        binding.fab.setOnLongClickListener {
            toggleFabMenu()
            true
        }

        binding.fab.setOnClickListener {
            toggleFabMenu()
        }
    }

    private fun toggleFabMenu() {
        isFabOpen = !isFabOpen

        if (isFabOpen) {
            binding.fab1.visibility = View.VISIBLE
            binding.fab2.visibility = View.VISIBLE
            binding.fab3.visibility = View.VISIBLE

            binding.fab1.animate().translationY(-resources.getDimension(R.dimen.standard_55))
            binding.fab2.animate().translationY(-resources.getDimension(R.dimen.standard_105))
            binding.fab3.animate().translationY(-resources.getDimension(R.dimen.standard_105))
        } else {
            binding.fab1.visibility = View.INVISIBLE
            binding.fab2.visibility = View.INVISIBLE
            binding.fab3.visibility = View.INVISIBLE

            binding.fab1.animate().translationY(resources.getDimension(R.dimen.standard_55))
            binding.fab2.animate().translationY(resources.getDimension(R.dimen.standard_105))
            binding.fab3.animate().translationY(resources.getDimension(R.dimen.standard_105))
        }
    }
}



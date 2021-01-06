package com.jaktongdan.android.sseuaengnim

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem

class PostWriteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_write)

        supportActionBar?.title = "글 작성"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> finish()
        }

        return super.onOptionsItemSelected(item)
    }
}
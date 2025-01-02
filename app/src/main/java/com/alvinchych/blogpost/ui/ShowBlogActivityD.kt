package com.alvinchych.blogpost.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alvinchych.blogpost.R
import com.alvinchych.blogpost.api.Post
import com.alvinchych.blogpost.ui.ui.theme.BlogPostTheme
import com.alvinchych.blogpost.viewmodel.BlogViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShowBlogActivityD : ComponentActivity() {

    private val viewModel: BlogViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShowBlogScreenD(
                viewModel = viewModel,
                onPostClicked = { post ->
                    Toast.makeText(this, "Clicked ${post.title}", Toast.LENGTH_SHORT).show()
                },
                onLoadMore = {
                    viewModel.fetchBlogToStateFlow()
                }
            )
        }
        viewModel.fetchBlogToStateFlow()
    }
}

@Composable
fun ShowBlogScreenD(
    modifier: Modifier = Modifier,
    viewModel: BlogViewModel,
    onPostClicked: (Post) -> Unit,
    onLoadMore: () -> Unit
) {
    val posts by viewModel.postsState.collectAsStateWithLifecycle()
    BlogPostTheme {
        Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Text(
                    text = stringResource(R.string.adapter_with_lazy_column),
                    fontSize = 24.sp,
                    modifier = modifier.fillMaxWidth()
                )
                ShowPosts(
                    posts = posts,
                    onPostClicked = onPostClicked,
                    onLoadMore = onLoadMore
                )
            }
        }
    }
}

@Composable
fun ShowPosts(
    posts: List<Post>,
    onPostClicked: (Post) -> Unit,
    onLoadMore: () -> Unit
) {
    LazyColumn {
        items(posts) { post ->
            PostItem(post = post, onPostClicked = onPostClicked)
        }

        item {
            if (posts.isNotEmpty()) {
                LaunchedEffect(Unit) {
                    onLoadMore()
                }
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Composable
fun PostItem(
    post: Post,
    onPostClicked: (Post) -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier.clickable {
            onPostClicked(post)
        },
    ) {
        Text(
            "${post.id}. ${post.title}",
            modifier = modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Text(
            post.content,
            modifier = modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
            fontSize = 16.sp
        )
        HorizontalDivider(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ShopScreenPreview() {
    val posts = listOf(
        Post(1, 1, "title 1", "content 1"),
        Post(2, 2, "title 2", "content 2"),
        Post(3, 3, "title 3", "content 3"),
        Post(4, 4, "title 4", "content 4"),
    )
    ShowPosts(
        posts = posts,
        onPostClicked = {},
        onLoadMore = {}
    )
}
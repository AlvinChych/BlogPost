package com.alvinchych.blogpost.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.alvinchych.blogpost.ui.ui.theme.BlogPostTheme
import com.alvinchych.blogpost.viewmodel.BlogViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TopBottomBarActivity : ComponentActivity() {

    private val viewModel: BlogViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainPage(
                viewModel = viewModel,
                onBackClicked = {
                finish()
            }, onSharedClicked = {
                Toast.makeText(this, "Shared", Toast.LENGTH_SHORT).show()
            })

            // the business logic like viewModel method might be not recommended to be executed
            // inside the composable function since it might be unexpectedly executed due to the
            // re-compose routine
            viewModel.fetchBlogState()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun MainPage(viewModel: BlogViewModel? = null,
             onBackClicked: () -> Unit = {},
             onSharedClicked: () -> Unit = {}) {
    if (viewModel == null) {
        return
    }
    BlogPostTheme {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        val context = LocalContext.current
        val posts by viewModel.postsState.collectAsStateWithLifecycle()

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { Text("TopAppBar") },
                    colors = TopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                        scrolledContainerColor = MaterialTheme.colorScheme.primary
                    ),
                    navigationIcon = {
                        IconButton(onClick = onBackClicked) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Localized description"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = onSharedClicked) {
                            Icon(
                                imageVector = Icons.Filled.Share,
                                contentDescription = "Localized description"
                            )
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Home,
                                contentDescription = "Localized description"
                            )
                        },
                        label = { Text("Home") },
                        selected = currentDestination?.hierarchy?.any {
                            it.route == "home"
                        } == true,
                        onClick = {
                            navController.navigate("home") {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        },
                    )

                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Place,
                                contentDescription = "Localized description"
                            )
                        },
                        label = { Text("Location") },
                        selected = currentDestination?.hierarchy?.any {
                            it.route == "location"
                        } == true,
                        onClick = {
                            navController.navigate("location") {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    )

                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.AccountCircle,
                                contentDescription = "Localized description"
                            )
                        },
                        label = { Text("Account") },
                        selected = currentDestination?.hierarchy?.any {
                            it.route == "account"
                        } == true,
                        onClick = {
                            navController.navigate("account") {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    )

                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Localized description"
                            )
                        },
                        label = { Text("Menu") },
                        selected = currentDestination?.hierarchy?.any {
                            it.route == "menu"
                        } == true,
                        onClick = {
                            navController.navigate("menu") {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "home",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(route = "home") {
                    Column {
                        Greeting(name = "home")
                        ShowPosts (
                            posts = posts,
                            onPostClicked = { post ->
                                Toast.makeText(context, "Clicked ${post.title}", Toast.LENGTH_SHORT).show()
                            },
                            onLoadMore = {
                                viewModel.fetchBlogState()
                            }
                        )
                    }
                }
                composable(route = "location") {
                    Greeting(name = "location")
                }
                composable(route = "account") {
                    Greeting(name = "account")
                }
                composable(route = "menu") {
                    Greeting(name = "menu")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}
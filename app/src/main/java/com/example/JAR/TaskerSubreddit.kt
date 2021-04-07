package com.example.JAR

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import com.example.JAR.databinding.ActivityTaskerMainBinding
import com.joaomgcd.taskerpluginlibrary.action.TaskerPluginRunnerAction
import com.joaomgcd.taskerpluginlibrary.config.TaskerPluginConfig
import com.joaomgcd.taskerpluginlibrary.config.TaskerPluginConfigHelper
import com.joaomgcd.taskerpluginlibrary.input.TaskerInput
import com.joaomgcd.taskerpluginlibrary.input.TaskerInputField
import com.joaomgcd.taskerpluginlibrary.input.TaskerInputRoot
import com.joaomgcd.taskerpluginlibrary.output.TaskerOutputObject
import com.joaomgcd.taskerpluginlibrary.output.TaskerOutputVariable
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResult
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResultSucess
import net.dean.jraw.models.Submission
import net.dean.jraw.pagination.DefaultPaginator

/**
 * Helper class for Subreddit post getting plugin
 */
class BasicActionHelper(config: TaskerPluginConfig<GetStringInput>) :
    TaskerPluginConfigHelper<GetStringInput, Array<TaskerSubmission>, BasicActionRunner>(config) {
    override val runnerClass: Class<BasicActionRunner> get() = BasicActionRunner::class.java
    override val inputClass = GetStringInput::class.java
    override val outputClass = Array<TaskerSubmission>::class.java

}

/**
 * This activity is launched when configure is clicked in Tasker
 */
class ActivityConfigBasicAction : Activity(), TaskerPluginConfig<GetStringInput> {
    override val context: Context get() = applicationContext
    private val taskerHelper by lazy { BasicActionHelper(this) }
    val binding: ActivityTaskerMainBinding by lazy {
        ActivityTaskerMainBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.btnApply.setOnClickListener { taskerHelper.onBackPressed() }
        actionBar?.title = "Tasker Get Subreddit Config"
        taskerHelper.onCreate()
    }

    override val inputForTasker: TaskerInput<GetStringInput>
        get() = TaskerInput(GetStringInput(binding.txtEdit.text.toString()))

    override fun assignFromInput(input: TaskerInput<GetStringInput>) = input.regular.run {
        binding.txtEdit.setText(test)
    }
}

/**
 * The inputs that are expected for this task
 */
@TaskerInputRoot
class GetStringInput @JvmOverloads constructor(
    @field:TaskerInputField("subreddit") var test: String? = null

)

@TaskerOutputObject
class TaskerSubmission(
    @get:TaskerOutputVariable("post", R.string.post_id, R.string.post_id_desc) var id: String,
    @get:TaskerOutputVariable("url", R.string.url, R.string.url_desc) var url: String,
    @get:TaskerOutputVariable("title", R.string.title, R.string.title_desc) var title: String,
    @get:TaskerOutputVariable("subreddit", R.string.subreddit, R.string.sub_desc) var sub: String,
    @get:TaskerOutputVariable(
        "hint",
        R.string.post_hint,
        R.string.post_hint_desc
    ) var hint: String,
)

/**
 * This class runs with tasker
 */
class BasicActionRunner : TaskerPluginRunnerAction<GetStringInput, Array<TaskerSubmission>>() {
    override fun run(
        context: Context,
        input: TaskerInput<GetStringInput>
    ): TaskerPluginResult<Array<TaskerSubmission>> {
        val page: DefaultPaginator<Submission>?
        val sub: String? = input.regular.test
        Log.d("jar task", sub.toString())
        val split = sub.toString().split("+")
        if (sub == null ||  split.isEmpty() || split[0] == "") {
            page = JRAW.getInstance(context).frontPage().build()
        } else if (split.size == 1) {
            val results = JRAW.getInstance(context).searchSubredditsByName(sub)
            var found = false
            results.forEach {
                if (it.name.equals(split[0], ignoreCase = true)) {
                    found = true
                }
            }
            if (found) {
                page =
                    JRAW.getInstance(context).subreddit(sub).posts()
                        .build()
            } else {
                val tmp = split[0]
                throw RuntimeException("Subreddit '$tmp' doesn't exit")
            }

        } else {
            page = JRAW.getInstance(context)
                .subreddits(split[0], split[1], *split.takeLast(split.size - 2).toTypedArray())
                .posts().build()
        }


        val posts = page.next()

        //        Handler(Looper.getMainLooper()).post {
//            Toast.makeText(context, posts[0]?.url, Toast.LENGTH_LONG).show()
//        }
        val results: Array<TaskerSubmission> = posts.map {
            TaskerSubmission(
                it.id,
                it.url,
                it.title,
                it.subreddit,
                it.postHint.toString()
            )
        }.toTypedArray()

        return TaskerPluginResultSucess(results)
    }
}
package com.example.JAR

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.example.JAR.databinding.ActivityTaskerMainBinding
import com.joaomgcd.taskerpluginlibrary.action.TaskerPluginRunnerAction
import com.joaomgcd.taskerpluginlibrary.config.TaskerPluginConfig
import com.joaomgcd.taskerpluginlibrary.config.TaskerPluginConfigHelper
import com.joaomgcd.taskerpluginlibrary.input.STRING_RES_ID_NOT_SET
import com.joaomgcd.taskerpluginlibrary.input.TaskerInput
import com.joaomgcd.taskerpluginlibrary.input.TaskerInputField
import com.joaomgcd.taskerpluginlibrary.input.TaskerInputRoot
import com.joaomgcd.taskerpluginlibrary.output.TaskerOutputForConfig
import com.joaomgcd.taskerpluginlibrary.output.TaskerOutputObject
import com.joaomgcd.taskerpluginlibrary.output.TaskerOutputVariable
import com.joaomgcd.taskerpluginlibrary.output.TaskerOutputsForConfig
import com.joaomgcd.taskerpluginlibrary.output.runner.TaskerOutputForRunner
import com.joaomgcd.taskerpluginlibrary.output.runner.TaskerOutputsForRunner
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

@TaskerOutputObject()
class TaskerSubmission (
    @get:TaskerOutputVariable("url",R.string.url,R.string.url) var url:String

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
        if (input.regular.test == null) {
            page = JRAW.getInstance(context).frontPage().build()
        } else {
            val sub: String = input.regular.test.toString()
            val results = JRAW.getInstance(context).searchSubredditsByName(sub)
            var found = false
            results.forEach {
                if (it.name.equals(sub, ignoreCase = true)) {
                    found = true
                }
            }
            if (found) {
                page =
                    JRAW.getInstance(context).subreddit(sub).posts()
                        .build()
            }else {
                throw RuntimeException("Subreddit '$sub' doesn't exit")
            }


        }

        val posts = page.next()
//        Handler(Looper.getMainLooper()).post {
//            Toast.makeText(context, posts[0]?.url, Toast.LENGTH_LONG).show()
//        }
        val results:Array<TaskerSubmission> = posts.map { TaskerSubmission(it.url) }.toTypedArray()

        return TaskerPluginResultSucess(results)
    }
}
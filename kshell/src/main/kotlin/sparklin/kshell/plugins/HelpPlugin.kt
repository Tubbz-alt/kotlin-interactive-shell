package sparklin.kshell.plugins

import sparklin.kshell.console.ConsoleReader
import sparklin.kshell.BaseCommand
import sparklin.kshell.Plugin
import sparklin.kshell.KShell
import sparklin.kshell.configuration.Configuration
import sparklin.kshell.match

class HelpPlugin: Plugin {
    inner class Help(conf: Configuration): BaseCommand() {
        override val name: String by conf.get(default = "help")
        override val short: String by conf.get(default = "h")
        override val description: String = "print this summary or command-specific help"

        override val params = "[command]"

        override fun execute(line: String) {
            val args = line.split(' ')
            val commands = repl.listCommands()

            repl.apply {
                if (args.size == 1) {
                    val help = commands.joinToString(separator = "\n") { it.desc() }
                    console.println(help)
                } else {
                    val command = args[1]
                    try {
                        val res = commands.first { it.match(":$command") }
                        console.println(res.help())
                    } catch (_: NoSuchElementException) {
                        console.println("$command: no such command. Type :help for help.")
                    }
                }
            }
        }
    }

    lateinit var repl: KShell
    lateinit var console: ConsoleReader

    override fun init(repl: KShell, config: Configuration) {
        this.repl = repl
        this.console = config.getConsoleReader()

        repl.registerCommand(Help(config))
    }

    override fun cleanUp() { }
}
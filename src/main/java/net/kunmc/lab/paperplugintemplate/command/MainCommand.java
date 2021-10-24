package net.kunmc.lab.paperplugintemplate.command;

import dev.kotx.flylib.command.Command;
import org.jetbrains.annotations.NotNull;

public class MainCommand extends Command {
    public MainCommand(@NotNull String name) {
        super(name);
        children(new ReloadCommand());
    }
}

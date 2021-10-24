package net.kunmc.lab.paperplugintemplate.command;

import dev.kotx.flylib.command.Command;
import dev.kotx.flylib.command.CommandContext;
import net.kunmc.lab.paperplugintemplate.SpawnRatioModifier;

public class ReloadCommand extends Command {
    public ReloadCommand() {
        super("reload");
    }

    @Override
    public void execute(CommandContext ctx) {
        SpawnRatioModifier.instance.reloadConfig();
        SpawnRatioModifier.instance.applyConfig();
    }
}

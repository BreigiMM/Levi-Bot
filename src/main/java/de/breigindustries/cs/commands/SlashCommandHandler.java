package de.breigindustries.cs.commands;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class SlashCommandHandler extends ListenerAdapter {

    private static Logger logger = LoggerFactory.getLogger(SlashCommandHandler.class);
    private static Dotenv dotenv = Dotenv.configure().load();

    List<CommandData> botCommands = List.of(
        Commands.slash("ping", "Replies with Pong!"),
        Commands.slash("ph1", "Placeholder 1"),
        Commands.slash("ph2", "Placeholder 2"),
        Commands.slash("ph3", "Placeholder 3"),
        Commands.slash("ph4", "Placeholder 4"),
        Commands.slash("ph5", "Placeholder 5"),
        Commands.slash("ph6", "Placeholder 6"),
        Commands.slash("ph7", "Placeholder 8"),
        Commands.slash("ph9", "Placeholder 9")
    );
    List<CommandData> guildCommands = List.of(
        Commands.slash("phg1", "Placeholder Guild 1")
            .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
    );
    
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        logger.info("Received ReadyEvent");

        // Update bot commands
        event.getJDA().updateCommands().addCommands(botCommands).queue(
            success -> {
                logger.info("Global commands updated successfully!");
            },
            failure -> {
                logger.error("Global command updating failed! " + failure.getMessage());
            }
        );

        // Update guild commands
        long guildId = Long.parseLong(dotenv.get("BI_GUILD_ID"));
        Guild guild = event.getJDA().getGuildById(guildId);
        if (guild != null) {
            guild.updateCommands().addCommands(guildCommands).queue();
            logger.info("Found guild, queued command registration and deletion");
        } else {
            logger.error("Could not find guild with ID " + guildId);
        }
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            case "ping" -> event.reply("Pong!").setEphemeral(true).queue();
            case "phg1" -> ConversationViewer.displayMessage(event);
        }
    }

}

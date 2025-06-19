package de.breigindustries.cs.bot.commands;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.breigindustries.cs.bot.chatgpt.Conversation;
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
        Commands.slash("menu", "Shows Levi's menu"),
        Commands.slash("pancake", "Makes Levi eat a special treat that makes him very smart :)")
    );
    List<CommandData> guildCommands = List.of(
        Commands.slash("phg1", "Placeholder Guild 1")
            .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
    );
    
    @Override
    public void onReady(ReadyEvent event) {
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
        logger.debug("Command " + event.getCommandString() + " was called by user {}", event.getUser().getEffectiveName());

        switch (event.getName()) {
            // Bot commands
            case "ping" -> event.reply("Pong!").setEphemeral(true).queue();
            case "menu" -> Menu.showMenu(event);
            case "pancake" -> {
                Conversation conversation = Conversation.getConversationByChannel(event.getChannel());
                conversation.setPancakeMode();
                event.reply("Set to pancake mode! Beware, with great power comes great responsibility :)").setEphemeral(true).queue();
            }


            // Guild commands
            case "phg1" -> ConversationViewer.displayMessage(event);
            default -> logger.error("Command {} could not be found!", event.getCommandString());
        }
    }

}

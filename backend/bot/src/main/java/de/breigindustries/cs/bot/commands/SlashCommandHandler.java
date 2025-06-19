package de.breigindustries.cs.bot.commands;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.breigindustries.cs.bot.chatgpt.Conversation;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class SlashCommandHandler extends ListenerAdapter {

    private static Logger logger = LoggerFactory.getLogger(SlashCommandHandler.class);
    private static Dotenv dotenv = Dotenv.configure().load();

    List<CommandData> botCommands = List.of(
        Commands.slash("ping", "Replies with Pong!"),
        Commands.slash("menu", "Shows Levi's menu"),
        Commands.slash("pancake", "Gives Levi a special treat that makes him very smart :)")
            .addOption(OptionType.STRING, "secret", "Levi's secret favourite word"),
        Commands.slash("eatpancake", "Makes Levi swallow the treat and return to normal")
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
            logger.warn("Could not find guild with ID " + guildId);
        }
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        logger.debug("Command " + event.getCommandString() + " was called by user {}", event.getUser().getEffectiveName());

        // Check and remove INFO messages automatically
        Conversation checkConversation = Conversation.createEmptyConversationFromChannel(event.getChannel());
        Conversation.fillConversation(checkConversation, 100);

        // Access control to Pancake mode
        Set<String> pancakeUsers = Set.of("451369381826658336", "692837354775380011");

        switch (event.getName()) {
            // Bot commands
            case "ping" -> event.reply("Pong!").setEphemeral(true).queue();
            case "menu" -> Menu.showMenu(event);
            case "pancake" -> {
                OptionMapping secretOption = event.getOption("secret");
                String userId = event.getUser().getId();
                if (secretOption == null) {
                    if (!pancakeUsers.contains(userId)) {
                        // No argument, not a pancake user
                        event.reply("This command is not available to you! If you think this is a mistake, contact my owner.").setEphemeral(true).queue();
                        return;
                    }
                } else if (!secretOption.getAsString().equals("pspsps")) {
                    if (!pancakeUsers.contains(userId)) {
                        // Wrong argument, not a pancake user
                        event.reply("That is not his favourite word!").setEphemeral(true).queue();
                        return;
                    }
                }
                Conversation conversation = Conversation.getConversationByChannel(event.getChannel());
                conversation.setPancakeMode(event.getChannel());
                event.reply("Set to pancake mode! Beware, with great power comes great responsibility :)").setEphemeral(true).queue();
            }
            case "eatpancake" -> {
                if (!pancakeUsers.contains(event.getUser().getId())) {
                    event.reply("This command is not available to you! If you think this is a mistake, contact my owner.");
                    return;
                }
                Conversation conversation = Conversation.getConversationByChannel(event.getChannel());
                if (event.getChannel().getType() != ChannelType.PRIVATE) {
                    event.reply("This command is not available here!").setEphemeral(true).queue();
                    return;
                }
                if (!conversation.isPancakeMode()) {
                    event.reply("Maybe give me a pancake first?").setEphemeral(true).queue();
                    return;
                }
                conversation.eatPancake();
                event.reply("The pancake has been eaten. Levi is full now and might not be on full capacity.").setEphemeral(true).queue();
            }


            // Guild commands
            case "phg1" -> ConversationViewer.displayMessage(event);

            // Default
            default -> logger.error("Command {} could not be found!", event.getCommandString());
        }
    }

}

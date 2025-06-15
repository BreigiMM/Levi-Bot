package de.breigindustries.cs.bot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class ConversationViewer {
    
    public static void displayMessage(SlashCommandInteractionEvent event) {
        event.reply("Bonjour")
            .addActionRow(
                Button.primary("hello", "Primary"),
                Button.danger("cancel", "Danger"),
                Button.link("https://www.wikipedia.org", "Link"),
                Button.secondary("phg1", "Secondary"),
                Button.success("asdöflkj", "Success")
            )
            .setEphemeral(true).queue();
    }

}

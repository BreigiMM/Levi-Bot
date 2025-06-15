package de.breigindustries.cs.bot.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;

public class Menu {

    private static Dotenv dotenv = Dotenv.configure().load();
    private static Logger logger = LoggerFactory.getLogger(Menu.class);
    
    public static void showMenu(SlashCommandInteractionEvent event) {
        // Create personalized menu message
        User user = event.getUser();
        boolean admin = user.getId().equals(dotenv.get("ADMIN_USER"));
        String header = "Levi 🐾 - Menu for " + user.getEffectiveName() + "\n";

        ReplyCallbackAction replyAction = event.reply(header + "There isn't a menu yet!");
        if (admin) replyAction.setActionRow(Button.danger("showAdminMenu", "Admin"));
        
        replyAction.setEphemeral(true).queue();
    }

}

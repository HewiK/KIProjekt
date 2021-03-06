package de.fiduciagad.ari.projekt.main;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import de.fiduciagad.ari.projekt.controller.GoogleAuth;
import de.fiduciagad.ari.projekt.controller.Initializer;
import de.fiduciagad.ari.projekt.controller.MyEventBuilder;
import de.fiduciagad.ari.projekt.controller.SearchGoogleCalendar;
import de.fiduciagad.ari.projekt.controller.SearchJavaUserGroups;
import de.fiduciagad.ari.projekt.controller.SearchMeetups;
import de.fiduciagad.ari.projekt.model.MyEvent;

public class Main2
{
    private static final Logger log = LogManager.getLogger("Main");
    public static void main(String[] args) throws IOException
    {
        Initializer init = new Initializer();
        CmdLineParser parser = new CmdLineParser(init);
        try
        {
            parser.parseArgument(args);
        }
        catch (CmdLineException e)
        {
            log.error("CmdLineException: ", e.getMessage());
        }
        init.initialize();
        System.setProperty("proxyPort", init.getProxyPort());
        System.setProperty("proxyHost", init.getProxyHost());
        GoogleAuth auth = new GoogleAuth(init);
        MyEvent event = new MyEventBuilder().build();
        new SearchGoogleCalendar(event, init, auth).search();
        new SearchJavaUserGroups().searchKa(event);
        new SearchJavaUserGroups().searchKL(event);
        new SearchMeetups(event, init).searchOldMeetups();
        new SearchMeetups(event, init).searchNewMeetups();
        event.reviewAndSort(init.getBewertungskriterien());
        event.entscheidenObSpeichern();
        event.writeJson();
    }
}


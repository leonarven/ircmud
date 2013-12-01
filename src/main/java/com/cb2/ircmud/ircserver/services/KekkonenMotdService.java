package com.cb2.ircmud.ircserver.services;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.cb2.ircmud.ircserver.IrcReply;
import com.cb2.ircmud.ircserver.IrcReplyCode;
import com.cb2.ircmud.ircserver.IrcUser;

@Service
public class KekkonenMotdService extends MotdService {
	
	@Autowired
	Environment env;

	public ArrayList<IrcReply> getMotd(IrcUser user) {
	
		ArrayList<IrcReply> replies = new ArrayList<IrcReply>();
		ArrayList<String> MotdStr = new ArrayList<String>(Arrays.asList(new String(
				  "Tissit on kivoja.\n"
				+ "Niin on kuppikakutkin.\n\n"
				+ "On mahdotonta olla masentunut, jos sinulla on ilmapallo. -Nalle Puh\n\n"
				+ "..................,+777777777777 +,,,,,,,,,,,,,,,,,,,,,,,:::\n"
				+ "................?77777777 7777777777?,,,,,,,,,,,,,,,,,,,::::\n"
				+ "..............,77777777 7 777777777777+,,,,,,,,,,,,,,,,:::::\n"
				+ "............~II7777777777777777777777777,,,,,,,,,,,,,,,,::::\n"
				+ "...........:IIIIII7777777777777777777777I=,:,:,,::::::::::::\n"
				+ "..........,+??IIIII777777777777777777777II::,:::::::::::::::\n"
				+ "..........:++??IIII77777777777777777777III+:::::::::::::::::\n"
				+ "..........:++???IIIII7777777777777777777I?+=::::::::::::::::\n"
				+ "..........=++??????IIII77777777777777777II?+::::::::::::::::\n"
				+ ".,........~++??III??III77777777777777777II?+~~::::::::::::::\n"
				+ "........,,=++???II??I7777777I7777777777IIII+~~~~~~::::::::::\n"
				+ ",,........~++????I???777III777777III777777I?~~~~~~~~~:~~::::\n"
				+ ",,,,.,.~77~,=????????II?++~::~+IIIII??=:,~,,.,:~~~~~~~~~~~~~\n"
				+ ",,,,,,.II=?7:,..=??I,,:=..,.,,:~,+=:,,..::~:::..~~~~~~~~~~~~\n"
				+ ",,,,,,,I?I??I:~......~==??++=++?+~,..,~++=~,..,,~~~~~~~~~~~~\n"
				+ ",,,,,,,+=I?,?++~~=.,,????I??III~, 7=????++=~:,:.=~~~~~~~~~~~\n"
				+ ",,,,,,,,+IIII++?=~=~=??????III:,?I77=++++==~:~.=~~~~~~~~~~~~\n"
				+ ",,,,,,,,:II:I++?I+~+~+++???III:=?I77,+++==~~,=.~=~~~~~~~~~~~\n"
				+ ",,,,,,,,,+=I+++?I??++::~?II?~,II?I77I,,~~~~:.~=~===~~~~~~~~~\n"
				+ ",,,,,,,,,~?I?+++??I=+++?I7II7+,.~?++,=???,.================~\n"
				+ ",,,,,,,,,,~II=++???III?III?7IIIII~~~I??I?=:===============~=\n"
				+ ",,,,,,,,,,,,:~+++???II??I?IIIIIII?II???+?=~==============~==\n"
				+ "::::::,:::..::===++????I??II?IIIIIII?++??=~=================\n"
				+ ":::::::::...~::=~=++????I++====~:=,,,,~I?===================\n"
				+ ":::::::,....7+:~==~+?????+?IIIIIII?I++?I+===================\n"
				+ "::::::......=I+~=+,~????????III??++++???=:~==++=============\n"
				+ ":::,.........I7I=+::=????IIIIIIIII???++~..,,,,,,:~==+=======\n"
				+ "~,............7I+=+::~==+?IIIIIIII?+?+:......,.,,.,,,,======\n"
				+ "...............77+?=:::~~=+III???++++~........,,.,.,,.,~====\n"
				+ "...............~ 7?+?=~~~~~~+===~~:,............,.......,+==\n"
				+ ",................ 7 7+++~~::,,,::,=......................,++\n"
				+ "..................7 7 ?+=~::,,,,.?=.......................,+\n"
				+ "..................,   77+==::,,.,??........................,\n"
				+ "....................I 7   I:.:.? 77.........................\n"
				+ ".....................      7~+=..7 .........................\n"
				+ ",.....................     7......77........................\n"
				+ ".,..,..................I   ~~=~~,.,7........................\n"
				+ "........................  I,::,:,. =........................\n"
				+ ".........................=.,~:=~+~ 7,.......................\n"
				+ "..........................?..::,..77........................\n"
				+ ",..........................7..:~:I= :.......................\n"
				+ ".,...........................,,:~+. 7.......................\n"
				+ ".....,.......................:,:+::?7:.......,..,..,........\n"
				+ "..,...........................,?:+:. ?.......,......,.,.....\n"
				+ "......,..,.....................~:~,.= ...........,..,..,.   \n"
				+ "..........,.....................=,,:.+........,..,.,,...I   \n"
				+ "...,...,...,,......................,..:.......,..,....,~77  \n"
				+ ".......,...,..,...................~........,..........~7    \n"
				+ "....,...,....,........................................+  77:\n"
				+ "....,...,...,,.,,......,...................,..........?  ...\n").split("\n")));

		String serverName=env.getProperty("config.server.name");
		String serverVersion=env.getProperty("config.server.version");
		replies.add(new IrcReply(null, IrcReplyCode.RPL_WELCOME,  "Welcome to "+serverName+", "+user.getRepresentation()+"("+user.getRealname()+")", ""));
		replies.add(new IrcReply(null, IrcReplyCode.RPL_YOURHOST, "Your host is "+serverName+", running version "+serverVersion, ""));

		replies.add(new IrcReply(null, IrcReplyCode.RPL_BOUNCE, "RFC2812 PREFIX=(ov)@+ CHANTYPES=#&!+ MODES=3 CHANLIMIT=#&!+:21", "are supported by this server"));
		replies.add(new IrcReply(null, IrcReplyCode.RPL_BOUNCE, "NICKLEN=15 TOPICLEN=255 KICKLEN=255 CHANNELLEN=50 IDCHAN=!:5", "are supported by this server"));
		replies.add(new IrcReply(null, IrcReplyCode.RPL_BOUNCE, "PENALTY FNC EXCEPTS=e INVEX=I CASEMAPPING=ascii NETWORK=IrcMud", "are supported by this server"));

		replies.add(new IrcReply(null, IrcReplyCode.RPL_MOTDSTART, serverName+" - Message Of The Day:", ""));
		for(String str : MotdStr)
			replies.add(new IrcReply(null, IrcReplyCode.RPL_MOTD, str, ""));
		replies.add(new IrcReply(null, IrcReplyCode.RPL_ENDOFMOTD, "End of /MOTD command.", ""));
		
		return replies;
	}
}

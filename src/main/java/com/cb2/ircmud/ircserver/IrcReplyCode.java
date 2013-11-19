package com.cb2.ircmud.ircserver;

public enum IrcReplyCode {


	RPL_WELCOME("001"), //(RFC2812) :Welcome to the Internet Relay Network <nick>!<user>@<host>
	RPL_YOURHOST("002"), //(RFC2812) :Your host is <servername>, running version <version>
	RPL_CREATED("003"), //(RFC2812) :This server was created <date>
	RPL_MYINFO("004"), //(KineIRCd) <server_name> <version> <user_modes> <chan_modes> <channel_modes_with_params> <user_modes_with_params> <server_modes> <server_modes_with_params>
	RPL_BOUNCE("010"), //() <hostname> <port> :<info>
	RPL_ISUPPORT("005"), //() 
	RPL_MAP("357"), //(AustHex) 
	RPL_MAPEND("359"), //(AustHex) 
	RPL_SNOMASK("008"), //(ircu) 
	RPL_STATMEMTOT("009"), //(ircu) 
	RPL_STATMEM("010"), //(ircu) 
	RPL_YOURCOOKIE("014"), //(Hybrid?) 
	RPL_MAPMORE("623"), //(Ultimate) 
	RPL_YOURID("042"), //(IRCnet) 
	RPL_SAVENICK("043"), //(IRCnet) :<info>
	RPL_ATTEMPTINGJUNC("050"), //(aircd) 
	RPL_ATTEMPTINGREROUTE("051"), //(aircd) 
	RPL_TRACELINK("200"), //(RFC1459) Link <version>[.<debug_level>] <destination> <next_server> [V<protocol_version> <link_uptime_in_seconds> <backstream_sendq> <upstream_sendq>]
	RPL_TRACECONNECTING("201"), //(RFC1459) Try. <class> <server>
	RPL_TRACEHANDSHAKE("202"), //(RFC1459) H.S. <class> <server>
	RPL_TRACEUNKNOWN("203"), //(RFC1459) ???? <class> [<connection_address>]
	RPL_TRACEOPERATOR("204"), //(RFC1459) Oper <class> <nick>
	RPL_TRACEUSER("205"), //(RFC1459) User <class> <nick>
	RPL_TRACESERVER("206"), //(RFC1459) Serv <class> <int>S <int>C <server> <nick!user|*!*>@<host|server> [V<protocol_version>]
	RPL_TRACESERVICE("207"), //(RFC2812) Service <class> <name> <type> <active_type>
	RPL_TRACENEWTYPE("208"), //(RFC1459) <newtype> 0 <client_name>
	RPL_TRACECLASS("209"), //(RFC2812) Class <class> <count>
	RPL_TRACERECONNECT("210"), //(RFC2812) 
	RPL_STATS("210"), //(aircd) 
	RPL_STATSLINKINFO("211"), //(RFC1459) <linkname> <sendq> <sent_msgs> <sent_bytes> <recvd_msgs> <rcvd_bytes> <time_open>
	RPL_STATSCOMMANDS("212"), //(RFC1459) <command> <count> [<byte_count> <remote_count>]
	RPL_STATSCLINE("213"), //(RFC1459) C <host> * <name> <port> <class>
	RPL_STATSNLINE("226"), //(Unreal) 
	RPL_STATSILINE("215"), //(RFC1459) I <host> * <host> <port> <class>
	RPL_STATSKLINE("216"), //(RFC1459) K <host> * <username> <port> <class>
	RPL_STATSQLINE("228"), //(ircu) 
	RPL_STATSPLINE("220"), //(Hybrid) 
	RPL_STATSYLINE("218"), //(RFC1459) Y <class> <ping_freq> <connect_freq> <max_sendq>
	RPL_ENDOFSTATS("219"), //(RFC1459) <query> :<info>
	RPL_STATSBLINE("247"), //(RFC2812) 
	RPL_UMODEIS("221"), //(RFC1459) <user_modes> [<user_mode_params>]
	RPL_MODLIST("702"), //(RatBox) <?> 0x<?> <?> <?>
	RPL_SQLINE_NICK("222"), //(Unreal) 
	RPL_STATSELINE("225"), //(Unreal) 
	RPL_STATSGLINE("247"), //(ircu) 
	RPL_STATSFLINE("238"), //(ircu) 
	RPL_STATSTLINE("246"), //(ircu) 
	RPL_STATSDLINE("275"), //(ircu, Ultimate) 
	RPL_STATSZLINE("225"), //(Bahamut) 
	RPL_STATSCOUNT("226"), //(Bahamut) 
	RPL_STATSVLINE("240"), //(RFC2812) 
	RPL_SERVICEINFO("231"), //(RFC1459) 
	RPL_ENDOFSERVICES("232"), //(RFC1459) 
	RPL_RULES("621"), //(Ultimate) 
	RPL_SERVICE("233"), //(RFC1459) 
	RPL_SERVLIST("234"), //(RFC2812) <name> <server> <mask> <type> <hopcount> <info>
	RPL_SERVLISTEND("235"), //(RFC2812) <mask> <type> :<info>
	RPL_STATSVERBOSE("236"), //(ircu) 
	RPL_STATSENGINE("237"), //(ircu) 
	RPL_STATSIAUTH("239"), //(IRCnet) 
	RPL_STATSXLINE("247"), //(Hybrid, PTlink, Unreal) 
	RPL_STATSLLINE("241"), //(RFC1459) L <hostmask> * <servername> <maxdepth>
	RPL_STATSUPTIME("242"), //(RFC1459) :Server Up <days> days <hours>:<minutes>:<seconds>
	RPL_STATSOLINE("243"), //(RFC1459) O <hostmask> * <nick> [:<info>]
	RPL_STATSHLINE("244"), //(RFC1459) H <hostmask> * <servername>
	RPL_STATSSLINE("245"), //(Bahamut, IRCnet, Hybrid) 
	RPL_STATSPING("246"), //(RFC2812) 
	RPL_STATSULINE("249"), //() 
	RPL_STATSDEFINE("248"), //(IRCnet) 
	RPL_STATSDEBUG("249"), //(Hybrid) 
	RPL_STATSCONN("250"), //(ircu, Unreal) 
	RPL_LUSERCLIENT("251"), //(RFC1459) :There are <int> users and <int> invisible on <int> servers
	RPL_LUSEROP("252"), //(RFC1459) <int> :<info>
	RPL_LUSERUNKNOWN("253"), //(RFC1459) <int> :<info>
	RPL_LUSERCHANNELS("254"), //(RFC1459) <int> :<info>
	RPL_LUSERME("255"), //(RFC1459) :I have <int> clients and <int> servers
	RPL_ADMINME("256"), //(RFC1459) <server> :<info>
	RPL_ADMINLOC1("257"), //(RFC1459) :<admin_location>
	RPL_ADMINLOC2("258"), //(RFC1459) :<admin_location>
	RPL_ADMINEMAIL("259"), //(RFC1459) :<email_address>
	RPL_TRACELOG("261"), //(RFC1459) File <logfile> <debug_level>
	RPL_TRACEPING("262"), //() 
	RPL_TRACEEND("262"), //(RFC2812) <server_name> <version>[.<debug_level>] :<info>
	RPL_TRYAGAIN("263"), //(RFC2812) <command> :<info>
	RPL_LOCALUSERS("265"), //(aircd, Hybrid, Hybrid, Bahamut) 
	RPL_GLOBALUSERS("266"), //(aircd, Hybrid, Hybrid, Bahamut) 
	RPL_START_NETSTAT("267"), //(aircd) 
	RPL_NETSTAT("268"), //(aircd) 
	RPL_END_NETSTAT("269"), //(aircd) 
	RPL_PRIVS("270"), //(ircu) 
	RPL_SILELIST("271"), //(ircu) 
	RPL_ENDOFSILELIST("272"), //(ircu) 
	RPL_NOTIFY("273"), //(aircd) 
	RPL_ENDNOTIFY("274"), //(aircd) 
	RPL_STATSDELTA("274"), //(IRCnet) 
	RPL_VCHANEXIST("276"), //() 
	RPL_VCHANLIST("277"), //() 
	RPL_VCHANHELP("278"), //() 
	RPL_GLIST("280"), //(ircu) 
	RPL_ENDOFGLIST("281"), //(ircu) 
	RPL_ACCEPTLIST("281"), //() 
	RPL_ENDOFACCEPT("282"), //() 
	RPL_JUPELIST("282"), //(ircu) 
	RPL_ALIST("388"), //(Unreal) 
	RPL_ENDOFJUPELIST("283"), //(ircu) 
	RPL_ENDOFALIST("389"), //(Unreal) 
	RPL_FEATURE("284"), //(ircu) 
	RPL_GLIST_HASH("285"), //() 
	RPL_CHANINFO_HANDLE("285"), //(aircd) 
	RPL_NEWHOSTIS("285"), //(QuakeNet) 
	RPL_CHANINFO_USERS("286"), //(aircd) 
	RPL_CHKHEAD("286"), //(QuakeNet) 
	RPL_CHANINFO_CHOPS("287"), //(aircd) 
	RPL_CHANUSER("287"), //(QuakeNet) 
	RPL_CHANINFO_VOICES("288"), //(aircd) 
	RPL_PATCHHEAD("288"), //(QuakeNet) 
	RPL_CHANINFO_AWAY("289"), //(aircd) 
	RPL_PATCHCON("289"), //(QuakeNet) 
	RPL_CHANINFO_OPERS("290"), //(aircd) 
	RPL_HELPHDR("290"), //(Unreal) 
	RPL_DATASTR("290"), //(QuakeNet) 
	RPL_CHANINFO_BANNED("291"), //(aircd) 
	RPL_HELPOP("291"), //(Unreal) 
	RPL_ENDOFCHECK("291"), //(QuakeNet) 
	RPL_CHANINFO_BANS("292"), //(aircd) 
	RPL_HELPTLR("292"), //(Unreal) 
	RPL_CHANINFO_INVITE("293"), //(aircd) 
	RPL_HELPHLP("293"), //(Unreal) 
	RPL_CHANINFO_INVITES("294"), //(aircd) 
	RPL_HELPFWD("294"), //(Unreal) 
	RPL_CHANINFO_KICK("295"), //(aircd) 
	RPL_HELPIGN("295"), //(Unreal) 
	RPL_CHANINFO_KICKS("296"), //(aircd) 
	RPL_END_CHANINFO("299"), //(aircd) 
	RPL_NONE("300"), //(RFC1459) 
	RPL_AWAY("301"), //(KineIRCd) <nick> <seconds away> :<message>
	RPL_USERHOST("302"), //(RFC1459) :*1<reply> *( ' ' <reply> )
	RPL_ISON("303"), //(RFC1459) :*1<nick> *( ' ' <nick> )
	RPL_TEXT("304"), //() 
	RPL_UNAWAY("305"), //(RFC1459) :<info>
	RPL_NOWAWAY("306"), //(RFC1459) :<info>
	RPL_USERIP("340"), //(ircu) 
	RPL_WHOISREGNICK("307"), //(Bahamut, Unreal) 
	RPL_SUSERHOST("307"), //(AustHex) 
	RPL_NOTIFYACTION("308"), //(aircd) 
	RPL_WHOISADMIN("308"), //(Bahamut) 
	RPL_RULESSTART("620"), //(Ultimate) 
	RPL_NICKTRACE("309"), //(aircd) 
	RPL_WHOISSADMIN("309"), //(Bahamut) 
	RPL_ENDOFRULES("622"), //(Ultimate) 
	RPL_WHOISHELPER("309"), //(AustHex) 
	RPL_WHOISSVCMSG("310"), //(Bahamut) 
	RPL_WHOISHELPOP("310"), //(Unreal) 
	RPL_WHOISSERVICE("310"), //(AustHex) 
	RPL_WHOISUSER("311"), //(RFC1459) <nick> <user> <host> * :<real_name>
	RPL_WHOISSERVER("312"), //(RFC1459) <nick> <server> :<server_info>
	RPL_WHOISOPERATOR("313"), //(RFC1459) <nick> :<privileges>
	RPL_WHOWASUSER("314"), //(RFC1459) <nick> <user> <host> * :<real_name>
	RPL_ENDOFWHO("315"), //(RFC1459) <name> :<info>
	RPL_WHOISCHANOP("316"), //(RFC1459) 
	RPL_WHOISIDLE("317"), //(RFC1459) <nick> <seconds> :seconds idle
	RPL_ENDOFWHOIS("318"), //(RFC1459) <nick> :<info>
	RPL_WHOISCHANNELS("319"), //(RFC1459) <nick> :*( ( '@' / '+' ) <channel> ' ' )
	RPL_WHOISVIRT("320"), //(AustHex) 
	RPL_WHOIS_HIDDEN("320"), //(Anothernet) 
	RPL_WHOISSPECIAL("320"), //(Unreal) 
	RPL_LISTSTART("321"), //(RFC1459) Channels :Users Name
	RPL_LIST("322"), //(RFC1459) <channel> <#_visible> :<topic>
	RPL_LISTEND("323"), //(RFC1459) :<info>
	RPL_CHANNELMODEIS("324"), //(RFC1459) <channel> <mode> <mode_params>
	RPL_UNIQOPIS("325"), //(RFC2812) <channel> <nickname>
	RPL_CHANNELPASSIS("325"), //() 
	RPL_NOCHANPASS("326"), //() 
	RPL_CHPASSUNKNOWN("327"), //() 
	RPL_CHANNEL_URL("328"), //(Bahamut, AustHex) 
	RPL_CREATIONTIME("329"), //(Bahamut) 
	RPL_WHOWAS_TIME("330"), //() 
	RPL_WHOISACCOUNT("330"), //(ircu) <nick> <authname> :<info>
	RPL_NOTOPIC("331"), //(RFC1459) <channel> :<info>
	RPL_TOPIC("332"), //(RFC1459) <channel> :<topic>
	RPL_TOPICWHOTIME("333"), //(ircu) 
	RPL_LISTUSAGE("334"), //(ircu) 
	RPL_COMMANDSYNTAX("334"), //(Bahamut) 
	RPL_LISTSYNTAX("334"), //(Unreal) 
	RPL_WHOISBOT("617"), //(Ultimate) 
	RPL_CHANPASSOK("338"), //() 
	RPL_WHOISACTUALLY("338"), //(ircu, Bahamut) 
	RPL_BADCHANPASS("339"), //() 
	RPL_INVITING("341"), //(RFC1459) <nick> <channel>
	RPL_SUMMONING("342"), //(RFC1459) <user> :<info>
	RPL_INVITED("345"), //(GameSurge) <channel> <user being invited> <user issuing invite> :<user being invited> has been invited by <user issuing invite>
	RPL_INVITELIST("346"), //(RFC2812) <channel> <invitemask>
	RPL_ENDOFINVITELIST("347"), //(RFC2812) <channel> :<info>
	RPL_EXCEPTLIST("348"), //(RFC2812) <channel> <exceptionmask>
	RPL_ENDOFEXCEPTLIST("349"), //(RFC2812) <channel> :<info>
	RPL_VERSION("351"), //(RFC1459) <version>[.<debuglevel>] <server> :<comments>
	RPL_WHOREPLY("352"), //(RFC1459) <channel> <user> <host> <server> <nick> <H|G>[*][@|+] :<hopcount> <real_name>
	RPL_NAMREPLY("353"), //(RFC1459) ( '=' / '*' / '@' ) <channel> ' ' : [ '@' / '+' ] <nick> *( ' ' [ '@' / '+' ] <nick> )
	RPL_WHOSPCRPL("354"), //(ircu) 
	RPL_NAMREPLY_("355"), //(QuakeNet) ( '=' / '*' / '@' ) <channel> ' ' : [ '@' / '+' ] <nick> *( ' ' [ '@' / '+' ] <nick> )
	RPL_KILLDONE("361"), //(RFC1459) 
	RPL_CLOSING("362"), //(RFC1459) 
	RPL_CLOSEEND("363"), //(RFC1459) 
	RPL_LINKS("364"), //(RFC1459) <mask> <server> :<hopcount> <server_info>
	RPL_ENDOFLINKS("365"), //(RFC1459) <mask> :<info>
	RPL_ENDOFNAMES("366"), //(RFC1459) <channel> :<info>
	RPL_BANLIST("367"), //(RFC1459) <channel> <banid> [<time_left> :<reason>]
	RPL_ENDOFBANLIST("368"), //(RFC1459) <channel> :<info>
	RPL_ENDOFWHOWAS("369"), //(RFC1459) <nick> :<info>
	RPL_INFO("371"), //(RFC1459) :<string>
	RPL_MOTD("378"), //(AustHex) 
	RPL_INFOSTART("373"), //(RFC1459) 
	RPL_ENDOFINFO("374"), //(RFC1459) :<info>
	RPL_MOTDSTART("375"), //(RFC1459) :- <server> Message of the day -
	RPL_ENDOFMOTD("376"), //(RFC1459) :<info>
	RPL_KICKEXPIRED("377"), //(aircd) 
	RPL_SPAM("377"), //(AustHex) :<text>
	RPL_BANEXPIRED("378"), //(aircd) 
	RPL_WHOISHOST("616"), //(Ultimate) 
	RPL_KICKLINKED("379"), //(aircd) 
	RPL_WHOISMODES("615"), //(Ultimate) 
	RPL_BANLINKED("380"), //(aircd) 
	RPL_YOURHELPER("380"), //(AustHex) 
	RPL_YOUREOPER("381"), //(RFC1459) :<info>
	RPL_REHASHING("382"), //(RFC1459) <config_file> :<info>
	RPL_YOURESERVICE("383"), //(RFC2812) :You are service <service_name>
	RPL_MYPORTIS("384"), //(RFC1459) 
	RPL_NOTOPERANYMORE("385"), //(AustHex, Hybrid, Unreal) 
	RPL_QLIST("386"), //(Unreal) 
	RPL_IRCOPS("386"), //(Ultimate) 
	RPL_ENDOFQLIST("387"), //(Unreal) 
	RPL_ENDOFIRCOPS("387"), //(Ultimate) 
	RPL_TIME("391"), //() <server> <year> <month> <day> <hour> <minute> <second>
	RPL_USERSSTART("392"), //(RFC1459) :UserID Terminal Host
	RPL_USERS("393"), //(RFC1459) :<username> <ttyline> <hostname>
	RPL_ENDOFUSERS("394"), //(RFC1459) :<info>
	RPL_NOUSERS("395"), //(RFC1459) :<info>
	RPL_HOSTHIDDEN("396"), //(Undernet) 
	ERR_UNKNOWNERROR("400"), //() <command> [<?>] :<info>
	ERR_NOSUCHNICK("401"), //(RFC1459) <nick> :<reason>
	ERR_NOSUCHSERVER("402"), //(RFC1459) <server> :<reason>
	ERR_NOSUCHCHANNEL("403"), //(RFC1459) <channel> :<reason>
	ERR_CANNOTSENDTOCHAN("404"), //(RFC1459) <channel> :<reason>
	ERR_TOOMANYCHANNELS("405"), //(RFC1459) <channel> :<reason>
	ERR_WASNOSUCHNICK("406"), //(RFC1459) <nick> :<reason>
	ERR_TOOMANYTARGETS("407"), //(RFC1459) <target> :<reason>
	ERR_NOSUCHSERVICE("408"), //(RFC2812) <service_name> :<reason>
	ERR_NOCOLORSONCHAN("408"), //(Bahamut) 
	ERR_NOORIGIN("409"), //(RFC1459) :<reason>
	ERR_NORECIPIENT("411"), //(RFC1459) :<reason>
	ERR_NOTEXTTOSEND("412"), //(RFC1459) :<reason>
	ERR_NOTOPLEVEL("413"), //(RFC1459) <mask> :<reason>
	ERR_WILDTOPLEVEL("414"), //(RFC1459) <mask> :<reason>
	ERR_BADMASK("415"), //(RFC2812) <mask> :<reason>
	ERR_TOOMANYMATCHES("416"), //(IRCnet) <command> [<mask>] :<info>
	ERR_QUERYTOOLONG("416"), //(ircu) 
	ERR_LENGTHTRUNCATED("419"), //(aircd) 
	ERR_UNKNOWNCOMMAND("421"), //(RFC1459) <command> :<reason>
	ERR_NOMOTD("422"), //(RFC1459) :<reason>
	ERR_NOADMININFO("423"), //(RFC1459) <server> :<reason>
	ERR_FILEERROR("424"), //(RFC1459) :<reason>
	ERR_NOOPERMOTD("425"), //(Unreal) 
	ERR_TOOMANYAWAY("429"), //(Bahamut) 
	ERR_EVENTNICKCHANGE("430"), //(AustHex) 
	ERR_NONICKNAMEGIVEN("431"), //(RFC1459) :<reason>
	ERR_ERRONEUSNICKNAME("432"), //(RFC1459) <nick> :<reason>
	ERR_NICKNAMEINUSE("433"), //(RFC1459) <nick> :<reason>
	ERR_SERVICENAMEINUSE("434"), //(AustHex?) 
	ERR_NORULES("434"), //(Unreal, Ultimate) 
	ERR_SERVICECONFUSED("435"), //(Unreal) 
	ERR_BANONCHAN("435"), //(Bahamut) 
	ERR_NICKCOLLISION("436"), //(RFC1459) <nick> :<reason>
	ERR_UNAVAILRESOURCE("437"), //(RFC2812) <nick/channel/service> :<reason>
	ERR_BANNICKCHANGE("437"), //(ircu) 
	ERR_NICKTOOFAST("438"), //(ircu) 
	ERR_DEAD("438"), //(IRCnet) 
	ERR_TARGETTOOFAST("439"), //(ircu) 
	ERR_SERVICESDOWN("440"), //(Bahamut, Unreal) 
	ERR_USERNOTINCHANNEL("441"), //(RFC1459) <nick> <channel> :<reason>
	ERR_NOTONCHANNEL("442"), //(RFC1459) <channel> :<reason>
	ERR_USERONCHANNEL("443"), //(RFC1459) <nick> <channel> [:<reason>]
	ERR_NOLOGIN("444"), //(RFC1459) <user> :<reason>
	ERR_SUMMONDISABLED("445"), //(RFC1459) :<reason>
	ERR_USERSDISABLED("446"), //(RFC1459) :<reason>
	ERR_NONICKCHANGE("447"), //(Unreal) 
	ERR_NOTIMPLEMENTED("449"), //(Undernet) Unspecified
	ERR_NOTREGISTERED("451"), //(RFC1459) :<reason>
	ERR_IDCOLLISION("452"), //() 
	ERR_NICKLOST("453"), //() 
	ERR_HOSTILENAME("455"), //(Unreal) 
	ERR_ACCEPTFULL("456"), //() 
	ERR_ACCEPTEXIST("457"), //() 
	ERR_ACCEPTNOT("458"), //() 
	ERR_NOHIDING("459"), //(Unreal) 
	ERR_NOTFORHALFOPS("460"), //(Unreal) 
	ERR_NEEDMOREPARAMS("461"), //(RFC1459) <command> :<reason>
	ERR_ALREADYREGISTERED("462"), //(RFC1459) :<reason>
	ERR_NOPERMFORHOST("463"), //(RFC1459) :<reason>
	ERR_PASSWDMISMATCH("464"), //(RFC1459) :<reason>
	ERR_YOUREBANNEDCREEP("465"), //(RFC1459) :<reason>
	ERR_YOUWILLBEBANNED("466"), //(RFC1459) 
	ERR_KEYSET("467"), //(RFC1459) <channel> :<reason>
	ERR_INVALIDUSERNAME("468"), //(ircu) 
	ERR_ONLYSERVERSCANCHANGE("468"), //(Bahamut, Unreal) 
	ERR_LINKSET("469"), //(Unreal) 
	ERR_LINKCHANNEL("470"), //(Unreal) 
	ERR_KICKEDFROMCHAN("470"), //(aircd) 
	ERR_CHANNELISFULL("471"), //(RFC1459) <channel> :<reason>
	ERR_UNKNOWNMODE("472"), //(RFC1459) <char> :<reason>
	ERR_INVITEONLYCHAN("473"), //(RFC1459) <channel> :<reason>
	ERR_BANNEDFROMCHAN("474"), //(RFC1459) <channel> :<reason>
	ERR_BADCHANNELKEY("475"), //(RFC1459) <channel> :<reason>
	ERR_BADCHANMASK("476"), //(RFC2812) <channel> :<reason>
	ERR_NOCHANMODES("477"), //(RFC2812) <channel> :<reason>
	ERR_NEEDREGGEDNICK("477"), //(Bahamut, ircu, Unreal) 
	ERR_BANLISTFULL("478"), //(RFC2812) <channel> <char> :<reason>
	ERR_BADCHANNAME("479"), //(Hybrid) 
	ERR_LINKFAIL("479"), //(Unreal) 
	ERR_NOULINE("480"), //(AustHex) 
	ERR_CANNOTKNOCK("480"), //(Unreal) 
	ERR_NOPRIVILEGES("481"), //(RFC1459) :<reason>
	ERR_CHANOPRIVSNEEDED("482"), //(RFC1459) <channel> :<reason>
	ERR_CANTKILLSERVER("483"), //(RFC1459) :<reason>
	ERR_RESTRICTED("484"), //(RFC2812) :<reason>
	ERR_ISCHANSERVICE("484"), //(Undernet) 
	ERR_DESYNC("484"), //(Bahamut, Hybrid, PTlink) 
	ERR_ATTACKDENY("484"), //(Unreal) 
	ERR_UNIQOPRIVSNEEDED("485"), //(RFC2812) :<reason>
	ERR_KILLDENY("485"), //(Unreal) 
	ERR_CANTKICKADMIN("485"), //(PTlink) 
	ERR_ISREALSERVICE("485"), //(QuakeNet) 
	ERR_NONONREG("486"), //() 
	ERR_HTMDISABLED("486"), //(Unreal) 
	ERR_ACCOUNTONLY("486"), //(QuakeNet) 
	ERR_CHANTOORECENT("487"), //(IRCnet) 
	ERR_MSGSERVICES("487"), //(Bahamut) 
	ERR_TSLESSCHAN("488"), //(IRCnet) 
	ERR_VOICENEEDED("489"), //(Undernet) 
	ERR_SECUREONLYCHAN("489"), //(Unreal) 
	ERR_NOOPERHOST("491"), //(RFC1459) :<reason>
	ERR_NOSERVICEHOST("492"), //(RFC1459) 
	ERR_NOFEATURE("493"), //(ircu) 
	ERR_BADFEATURE("494"), //(ircu) 
	ERR_BADLOGTYPE("495"), //(ircu) 
	ERR_BADLOGSYS("496"), //(ircu) 
	ERR_BADLOGVALUE("497"), //(ircu) 
	ERR_ISOPERLCHAN("498"), //(ircu) 
	ERR_CHANOWNPRIVNEEDED("499"), //(Unreal) 
	ERR_UMODEUNKNOWNFLAG("501"), //(RFC1459) :<reason>
	ERR_USERSDONTMATCH("502"), //(RFC1459) :<reason>
	ERR_GHOSTEDCLIENT("503"), //(Hybrid) 
	ERR_VWORLDWARN("503"), //(AustHex) :<warning_text>
	ERR_USERNOTONSERV("504"), //() 
	ERR_SILELISTFULL("511"), //(ircu) 
	ERR_TOOMANYWATCH("512"), //(Bahamut) 
	ERR_BADPING("513"), //(ircu) 
	ERR_INVALID_ERROR("514"), //(ircu) 
	ERR_TOOMANYDCC("514"), //(Bahamut) 
	ERR_BADEXPIRE("515"), //(ircu) 
	ERR_DONTCHEAT("516"), //(ircu) 
	ERR_DISABLED("517"), //(ircu) <command> :<info/reason>
	ERR_NOINVITE("518"), //(Unreal) 
	ERR_LONGMASK("518"), //(ircu) 
	ERR_ADMONLY("519"), //(Unreal) 
	ERR_TOOMANYUSERS("519"), //(ircu) 
	ERR_OPERONLY("520"), //(Unreal) 
	ERR_MASKTOOWIDE("520"), //(ircu) 
	ERR_WHOTRUNC("520"), //(AustHex) 
	ERR_LISTSYNTAX("521"), //(Bahamut) 
	ERR_WHOSYNTAX("522"), //(Bahamut) 
	ERR_WHOLIMEXCEED("523"), //(Bahamut) 
	ERR_QUARANTINED("524"), //(ircu) 
	ERR_OPERSPVERIFY("524"), //(Unreal) 
	ERR_REMOTEPFX("525"), //(CAPAB USERCMDPFX) <nickname> :<reason>
	ERR_PFXUNROUTABLE("526"), //(CAPAB USERCMDPFX) <nickname> :<reason>
	ERR_BADHOSTMASK("550"), //(QuakeNet) 
	ERR_HOSTUNAVAIL("551"), //(QuakeNet) 
	ERR_USINGSLINE("552"), //(QuakeNet) 
	ERR_STATSSLINE("553"), //(QuakeNet) 
	RPL_LOGON("600"), //(Bahamut, Unreal) 
	RPL_LOGOFF("601"), //(Bahamut, Unreal) 
	RPL_WATCHOFF("602"), //(Bahamut, Unreal) 
	RPL_WATCHSTAT("603"), //(Bahamut, Unreal) 
	RPL_NOWON("604"), //(Bahamut, Unreal) 
	RPL_NOWOFF("605"), //(Bahamut, Unreal) 
	RPL_WATCHLIST("606"), //(Bahamut, Unreal) 
	RPL_ENDOFWATCHLIST("607"), //(Bahamut, Unreal) 
	RPL_WATCHCLEAR("608"), //(Ultimate) 
	RPL_ISOPER("610"), //(Ultimate) 
	RPL_ISLOCOP("611"), //(Ultimate) 
	RPL_ISNOTOPER("612"), //(Ultimate) 
	RPL_ENDOFISOPER("613"), //(Ultimate) 
	RPL_DCCSTATUS("617"), //(Bahamut) 
	RPL_DCCLIST("618"), //(Bahamut) 
	RPL_ENDOFDCCLIST("619"), //(Bahamut) 
	RPL_WHOWASHOST("619"), //(Ultimate) 
	RPL_DCCINFO("620"), //(Bahamut) 
	RPL_OMOTDSTART("720"), //(RatBox) :<text>
	RPL_OMOTD("721"), //(RatBox) :<text>
	RPL_ENDOFO("626"), //(Ultimate) 
	RPL_SETTINGS("630"), //(Ultimate) 
	RPL_ENDOFSETTINGS("631"), //(Ultimate) 
	RPL_DUMPING("640"), //(Unreal) 
	RPL_DUMPRPL("641"), //(Unreal) 
	RPL_EODUMP("642"), //(Unreal) 
	RPL_TRACEROUTE_HOP("660"), //(KineIRCd) <target> <hop#> [<address> [<hostname> | '*'] <usec_ping>]
	RPL_TRACEROUTE_START("661"), //(KineIRCd) <target> <target_FQDN> <target_address> <max_hops>
	RPL_MODECHANGEWARN("662"), //(KineIRCd) ['+' | '-']<mode_char> :<warning>
	RPL_CHANREDIR("663"), //(KineIRCd) <old_chan> <new_chan> :<info>
	RPL_SERVMODEIS("664"), //(KineIRCd) <server> <modes> <parameters>..
	RPL_OTHERUMODEIS("665"), //(KineIRCd) <nickname> <modes>
	RPL_ENDOF_GENERIC("666"), //(KineIRCd) <command> [<parameter> ...] :<info>
	RPL_WHOWASDETAILS("670"), //(KineIRCd) <nick> <type> :<information>
	RPL_WHOISSECURE("671"), //(KineIRCd) <nick> <type> [:<info>]
	RPL_UNKNOWNMODES("672"), //(Ithildin) <modes> :<info>
	RPL_CANNOTSETMODES("673"), //(Ithildin) <modes> :<info>
	RPL_LUSERSTAFF("678"), //(KineIRCd) <staff_online_count> :<info>
	RPL_TIMEONSERVERIS("679"), //(KineIRCd) <seconds> [<nanoseconds> | '0'] <timezone> <flags> :<info>
	RPL_NETWORKS("682"), //(KineIRCd) <name> <through_name> <hops> :<info>
	RPL_YOURLANGUAGEIS("687"), //(KineIRCd) <code(s)> :<info>
	RPL_LANGUAGE("688"), //(KineIRCd) <code> <revision> <maintainer> <flags> * :<info>
	RPL_WHOISSTAFF("689"), //(KineIRCd) :<info>
	RPL_WHOISLANGUAGE("690"), //(KineIRCd) <nick> <language codes>
	RPL_ENDOFMODLIST("703"), //(RatBox) :<text>
	RPL_HELPSTART("704"), //(RatBox) <command> :<text>
	RPL_HELPTXT("705"), //(RatBox) <command> :<text>
	RPL_ENDOFHELP("706"), //(RatBox) <command> :<text>
	RPL_ETRACEFULL("708"), //(RatBox) <?> <?> <?> <?> <?> <?> <?> :<?>
	RPL_ETRACE("709"), //(RatBox) <?> <?> <?> <?> <?> <?> :<?>
	RPL_KNOCK("710"), //(RatBox) <channel> <nick>!<user>@<host> :<text>
	RPL_KNOCKDLVR("711"), //(RatBox) <channel> :<text>
	ERR_TOOMANYKNOCK("712"), //(RatBox) <channel> :<text>
	ERR_CHANOPEN("713"), //(RatBox) <channel> :<text>
	ERR_KNOCKONCHAN("714"), //(RatBox) <channel> :<text>
	ERR_KNOCKDISABLED("715"), //(RatBox) :<text>
	RPL_TARGUMODEG("716"), //(RatBox) <nick> :<info>
	RPL_TARGNOTIFY("717"), //(RatBox) <nick> :<info>
	RPL_UMODEGMSG("718"), //(RatBox) <nick> <user>@<host> :<info>
	RPL_ENDOFOMOTD("722"), //(RatBox) :<text>
	ERR_NOPRIVS("723"), //(RatBox) <command> :<text>
	RPL_TESTMARK("724"), //(RatBox) <nick>!<user>@<host> <?> <?> :<text>
	RPL_TESTLINE("725"), //(RatBox) <?> <?> <?> :<?>
	RPL_NOTESTLINE("726"), //(RatBox) <?> :<text>
	RPL_XINFO("771"), //(Ithildin) 
	RPL_XINFOSTART("773"), //(Ithildin) 
	RPL_XINFOEND("774"), //(Ithildin) 
	ERR_CANNOTDOCOMMAND("972"), //(Unreal) 
	ERR_CANNOTCHANGEUMODE("973"), //(KineIRCd) <mode_char> :<reason>
	ERR_CANNOTCHANGECHANMODE("974"), //(KineIRCd) <mode_char> :<reason>
	ERR_CANNOTCHANGESERVERMODE("975"), //(KineIRCd) <mode_char> :<reason>
	ERR_CANNOTSENDTONICK("976"), //(KineIRCd) <nick> :<reason>
	ERR_UNKNOWNSERVERMODE("977"), //(KineIRCd) <modechar> :<info>
	ERR_SERVERMODELOCK("979"), //(KineIRCd) <target> :<info>
	ERR_BADCHARENCODING("980"), //(KineIRCd) <command> <charset> :<info>
	ERR_TOOMANYLANGUAGES("981"), //(KineIRCd) <max_langs> :<info>
	ERR_NOLANGUAGE("982"), //(KineIRCd) <language_code> :<info>
	ERR_TEXTTOOSHORT("983"), //(KineIRCd) <command> :<info>
	ERR_NUMERIC_ERR("999"); //(Bahamut) 
	
	public String num;
	
	private IrcReplyCode(String num) {
		this.num = num;
	}
	
	public String toString() {
		return this.num;
	}
}

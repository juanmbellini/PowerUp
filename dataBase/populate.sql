BEGIN;
SET DATESTYLE TO ISO, YMD;
\encoding utf8;

--Genres
INSERT INTO power_up.genres ("id", "name") VALUES (33, 'Arcade');
INSERT INTO power_up.genres ("id", "name") VALUES (32, 'Indie');
INSERT INTO power_up.genres ("id", "name") VALUES (31, 'Adventure');
INSERT INTO power_up.genres ("id", "name") VALUES (30, 'Pinball');
INSERT INTO power_up.genres ("id", "name") VALUES (26, 'Quiz/Trivia');
INSERT INTO power_up.genres ("id", "name") VALUES (25, 'Hack and slash/Beat ''em up');
INSERT INTO power_up.genres ("id", "name") VALUES (24, 'Tactical');
INSERT INTO power_up.genres ("id", "name") VALUES (16, 'Turn-based strategy (TBS)');
INSERT INTO power_up.genres ("id", "name") VALUES (15, 'Strategy');
INSERT INTO power_up.genres ("id", "name") VALUES (14, 'Sport');
INSERT INTO power_up.genres ("id", "name") VALUES (13, 'Simulator');
INSERT INTO power_up.genres ("id", "name") VALUES (12, 'Role-playing (RPG)');
INSERT INTO power_up.genres ("id", "name") VALUES (11, 'Real Time Strategy (RTS)');
INSERT INTO power_up.genres ("id", "name") VALUES (10, 'Racing');
INSERT INTO power_up.genres ("id", "name") VALUES (9, 'Puzzle');
INSERT INTO power_up.genres ("id", "name") VALUES (8, 'Platform');
INSERT INTO power_up.genres ("id", "name") VALUES (7, 'Music');
INSERT INTO power_up.genres ("id", "name") VALUES (5, 'Shooter');
INSERT INTO power_up.genres ("id", "name") VALUES (4, 'Fighting');
INSERT INTO power_up.genres ("id", "name") VALUES (2, 'Point-and-click');

--Consoles
INSERT INTO power_up.consoles ("id", "name") VALUES (132, 'Amazon Fire TV');
INSERT INTO power_up.consoles ("id", "name") VALUES (131, 'Nintendo PlayStation');
INSERT INTO power_up.consoles ("id", "name") VALUES (130, 'NX');
INSERT INTO power_up.consoles ("id", "name") VALUES (129, 'Texas Instruments TI-99');
INSERT INTO power_up.consoles ("id", "name") VALUES (128, 'PC Engine SuperGrafx');
INSERT INTO power_up.consoles ("id", "name") VALUES (127, 'Fairchild Channel F');
INSERT INTO power_up.consoles ("id", "name") VALUES (126, 'TRS-80');
INSERT INTO power_up.consoles ("id", "name") VALUES (125, 'PC-8801');
INSERT INTO power_up.consoles ("id", "name") VALUES (124, 'SwanCrystal');
INSERT INTO power_up.consoles ("id", "name") VALUES (123, 'WonderSwan Color');
INSERT INTO power_up.consoles ("id", "name") VALUES (122, 'Nuon');
INSERT INTO power_up.consoles ("id", "name") VALUES (121, 'Sharp X68000');
INSERT INTO power_up.consoles ("id", "name") VALUES (120, 'Neo Geo Pocket Color');
INSERT INTO power_up.consoles ("id", "name") VALUES (119, 'Neo Geo Pocket');
INSERT INTO power_up.consoles ("id", "name") VALUES (118, 'FM Towns');
INSERT INTO power_up.consoles ("id", "name") VALUES (117, 'Philips CD-i');
INSERT INTO power_up.consoles ("id", "name") VALUES (116, 'Acorn Archimedes');
INSERT INTO power_up.consoles ("id", "name") VALUES (115, 'Apple IIGS');
INSERT INTO power_up.consoles ("id", "name") VALUES (114, 'Amiga CD32');
INSERT INTO power_up.consoles ("id", "name") VALUES (113, 'OnLive Game System');
INSERT INTO power_up.consoles ("id", "name") VALUES (112, 'Microcomputer');
INSERT INTO power_up.consoles ("id", "name") VALUES (111, 'Imlac PDS-1');
INSERT INTO power_up.consoles ("id", "name") VALUES (110, 'PLATO');
INSERT INTO power_up.consoles ("id", "name") VALUES (109, 'CDC Cyber 70');
INSERT INTO power_up.consoles ("id", "name") VALUES (108, 'PDP-11');
INSERT INTO power_up.consoles ("id", "name") VALUES (107, 'Call-A-Computer time-shared mainframe computer system');
INSERT INTO power_up.consoles ("id", "name") VALUES (106, 'SDS Sigma 7');
INSERT INTO power_up.consoles ("id", "name") VALUES (105, 'HP 3000');
INSERT INTO power_up.consoles ("id", "name") VALUES (104, 'HP 2100');
INSERT INTO power_up.consoles ("id", "name") VALUES (103, 'PDP-7');
INSERT INTO power_up.consoles ("id", "name") VALUES (102, 'EDSAC');
INSERT INTO power_up.consoles ("id", "name") VALUES (101, 'Ferranti Nimrod Computer');
INSERT INTO power_up.consoles ("id", "name") VALUES (100, 'Analogue electronics');
INSERT INTO power_up.consoles ("id", "name") VALUES (99, 'Family Computer');
INSERT INTO power_up.consoles ("id", "name") VALUES (98, 'DEC GT40');
INSERT INTO power_up.consoles ("id", "name") VALUES (97, 'PDP-8');
INSERT INTO power_up.consoles ("id", "name") VALUES (96, 'PDP-10');
INSERT INTO power_up.consoles ("id", "name") VALUES (95, 'PDP-1');
INSERT INTO power_up.consoles ("id", "name") VALUES (94, 'Commodore Plus/4');
INSERT INTO power_up.consoles ("id", "name") VALUES (93, 'Commodore 16');
INSERT INTO power_up.consoles ("id", "name") VALUES (92, 'SteamOS');
INSERT INTO power_up.consoles ("id", "name") VALUES (91, 'Bally Astrocade');
INSERT INTO power_up.consoles ("id", "name") VALUES (90, 'Commodore PET');
INSERT INTO power_up.consoles ("id", "name") VALUES (89, 'Microvision');
INSERT INTO power_up.consoles ("id", "name") VALUES (88, 'Odyssey');
INSERT INTO power_up.consoles ("id", "name") VALUES (87, 'Virtual Boy');
INSERT INTO power_up.consoles ("id", "name") VALUES (86, 'TurboGrafx-16/PC Engine');
INSERT INTO power_up.consoles ("id", "name") VALUES (85, 'Donner Model 30');
INSERT INTO power_up.consoles ("id", "name") VALUES (84, 'SG-1000');
INSERT INTO power_up.consoles ("id", "name") VALUES (82, 'Web browser');
INSERT INTO power_up.consoles ("id", "name") VALUES (80, 'Neo Geo AES');
INSERT INTO power_up.consoles ("id", "name") VALUES (79, 'Neo Geo MVS');
INSERT INTO power_up.consoles ("id", "name") VALUES (78, 'Sega CD');
INSERT INTO power_up.consoles ("id", "name") VALUES (77, 'Sharp X1');
INSERT INTO power_up.consoles ("id", "name") VALUES (75, 'Apple II');
INSERT INTO power_up.consoles ("id", "name") VALUES (74, 'Windows Phone');
INSERT INTO power_up.consoles ("id", "name") VALUES (73, 'BlackBerry OS');
INSERT INTO power_up.consoles ("id", "name") VALUES (72, 'Ouya');
INSERT INTO power_up.consoles ("id", "name") VALUES (71, 'Commodore VIC-20');
INSERT INTO power_up.consoles ("id", "name") VALUES (70, 'Vectrex');
INSERT INTO power_up.consoles ("id", "name") VALUES (69, 'BBC Microcomputer System');
INSERT INTO power_up.consoles ("id", "name") VALUES (68, 'ColecoVision');
INSERT INTO power_up.consoles ("id", "name") VALUES (67, 'Intellivision');
INSERT INTO power_up.consoles ("id", "name") VALUES (66, 'Atari 5200');
INSERT INTO power_up.consoles ("id", "name") VALUES (65, 'Atari 8-bit');
INSERT INTO power_up.consoles ("id", "name") VALUES (64, 'Sega Master System');
INSERT INTO power_up.consoles ("id", "name") VALUES (63, 'Atari ST/STE');
INSERT INTO power_up.consoles ("id", "name") VALUES (62, 'Atari Jaguar');
INSERT INTO power_up.consoles ("id", "name") VALUES (61, 'Atari Lynx');
INSERT INTO power_up.consoles ("id", "name") VALUES (60, 'Atari 7800');
INSERT INTO power_up.consoles ("id", "name") VALUES (59, 'Atari 2600');
INSERT INTO power_up.consoles ("id", "name") VALUES (58, 'Super Famicom');
INSERT INTO power_up.consoles ("id", "name") VALUES (57, 'WonderSwan');
INSERT INTO power_up.consoles ("id", "name") VALUES (56, 'WiiWare');
INSERT INTO power_up.consoles ("id", "name") VALUES (55, 'Mobile');
INSERT INTO power_up.consoles ("id", "name") VALUES (53, 'MSX2');
INSERT INTO power_up.consoles ("id", "name") VALUES (52, 'Arcade');
INSERT INTO power_up.consoles ("id", "name") VALUES (51, 'Family Computer Disk System');
INSERT INTO power_up.consoles ("id", "name") VALUES (50, '3DO Interactive Multiplayer');
INSERT INTO power_up.consoles ("id", "name") VALUES (49, 'Xbox One');
INSERT INTO power_up.consoles ("id", "name") VALUES (48, 'PlayStation 4');
INSERT INTO power_up.consoles ("id", "name") VALUES (47, 'Virtual Console (Nintendo)');
INSERT INTO power_up.consoles ("id", "name") VALUES (46, 'PlayStation Vita');
INSERT INTO power_up.consoles ("id", "name") VALUES (45, 'PlayStation Network');
INSERT INTO power_up.consoles ("id", "name") VALUES (44, 'Tapwave Zodiac');
INSERT INTO power_up.consoles ("id", "name") VALUES (42, 'N-Gage');
INSERT INTO power_up.consoles ("id", "name") VALUES (41, 'Wii U');
INSERT INTO power_up.consoles ("id", "name") VALUES (39, 'iOS');
INSERT INTO power_up.consoles ("id", "name") VALUES (38, 'PlayStation Portable');
INSERT INTO power_up.consoles ("id", "name") VALUES (37, 'Nintendo 3DS');
INSERT INTO power_up.consoles ("id", "name") VALUES (36, 'Xbox Live Arcade');
INSERT INTO power_up.consoles ("id", "name") VALUES (35, 'Sega Game Gear');
INSERT INTO power_up.consoles ("id", "name") VALUES (34, 'Android');
INSERT INTO power_up.consoles ("id", "name") VALUES (33, 'Game Boy');
INSERT INTO power_up.consoles ("id", "name") VALUES (32, 'Sega Saturn');
INSERT INTO power_up.consoles ("id", "name") VALUES (30, 'Sega 32X');
INSERT INTO power_up.consoles ("id", "name") VALUES (29, 'Sega Mega Drive/Genesis');
INSERT INTO power_up.consoles ("id", "name") VALUES (27, 'MSX');
INSERT INTO power_up.consoles ("id", "name") VALUES (26, 'ZX Spectrum');
INSERT INTO power_up.consoles ("id", "name") VALUES (25, 'Amstrad CPC');
INSERT INTO power_up.consoles ("id", "name") VALUES (24, 'Game Boy Advance');
INSERT INTO power_up.consoles ("id", "name") VALUES (23, 'Dreamcast');
INSERT INTO power_up.consoles ("id", "name") VALUES (22, 'Game Boy Color');
INSERT INTO power_up.consoles ("id", "name") VALUES (21, 'Nintendo GameCube');
INSERT INTO power_up.consoles ("id", "name") VALUES (20, 'Nintendo DS');
INSERT INTO power_up.consoles ("id", "name") VALUES (19, 'Super Nintendo Entertainment System (SNES)');
INSERT INTO power_up.consoles ("id", "name") VALUES (18, 'Nintendo Entertainment System (NES)');
INSERT INTO power_up.consoles ("id", "name") VALUES (16, 'Amiga');
INSERT INTO power_up.consoles ("id", "name") VALUES (15, 'Commodore C64/128');
INSERT INTO power_up.consoles ("id", "name") VALUES (14, 'Mac');
INSERT INTO power_up.consoles ("id", "name") VALUES (13, 'PC DOS');
INSERT INTO power_up.consoles ("id", "name") VALUES (12, 'Xbox 360');
INSERT INTO power_up.consoles ("id", "name") VALUES (11, 'Xbox');
INSERT INTO power_up.consoles ("id", "name") VALUES (9, 'PlayStation 3');
INSERT INTO power_up.consoles ("id", "name") VALUES (8, 'PlayStation 2');
INSERT INTO power_up.consoles ("id", "name") VALUES (7, 'PlayStation');
INSERT INTO power_up.consoles ("id", "name") VALUES (6, 'PC (Microsoft Windows)');
INSERT INTO power_up.consoles ("id", "name") VALUES (5, 'Wii');
INSERT INTO power_up.consoles ("id", "name") VALUES (4, 'Nintendo 64');
INSERT INTO power_up.consoles ("id", "name") VALUES (3, 'Linux');

--Companies
INSERT INTO power_up.companies ("id", "name") VALUES (2171, 'Warhorse Studios');
INSERT INTO power_up.companies ("id", "name") VALUES (2170, 'SCE San Diego Studios');
INSERT INTO power_up.companies ("id", "name") VALUES (2169, 'Vanguard Entertainment');
INSERT INTO power_up.companies ("id", "name") VALUES (2168, 'Drinkbox Studios');
INSERT INTO power_up.companies ("id", "name") VALUES (2167, 'Nintendo SPD Group No. 3');
INSERT INTO power_up.companies ("id", "name") VALUES (2166, 'Supersonic Software');
INSERT INTO power_up.companies ("id", "name") VALUES (2165, 'Enterbrain');
INSERT INTO power_up.companies ("id", "name") VALUES (2164, 'Play THQ');
INSERT INTO power_up.companies ("id", "name") VALUES (2163, 'Agatsuma');
INSERT INTO power_up.companies ("id", "name") VALUES (2162, '5th Cell');
INSERT INTO power_up.companies ("id", "name") VALUES (2161, 'Armor Project');
INSERT INTO power_up.companies ("id", "name") VALUES (2160, 'Ironstone Partners');
INSERT INTO power_up.companies ("id", "name") VALUES (2159, 'Asylum Entertainment');
INSERT INTO power_up.companies ("id", "name") VALUES (2158, '4mm Games');
INSERT INTO power_up.companies ("id", "name") VALUES (2157, 'Gravity-i');
INSERT INTO power_up.companies ("id", "name") VALUES (2156, 'Imagination Entertainment');
INSERT INTO power_up.companies ("id", "name") VALUES (2155, 'Syfy Kids');
INSERT INTO power_up.companies ("id", "name") VALUES (2154, 'Keen Games');
INSERT INTO power_up.companies ("id", "name") VALUES (2153, 'G1M2');
INSERT INTO power_up.companies ("id", "name") VALUES (2152, 'WXP Inc');
INSERT INTO power_up.companies ("id", "name") VALUES (2151, 'Jet Black Games');
INSERT INTO power_up.companies ("id", "name") VALUES (2150, 'Aurona');
INSERT INTO power_up.companies ("id", "name") VALUES (2149, 'Zoe Mode');
INSERT INTO power_up.companies ("id", "name") VALUES (2148, 'Konami Digital Entertainment GmbH');
INSERT INTO power_up.companies ("id", "name") VALUES (2147, 'Young Horses');
INSERT INTO power_up.companies ("id", "name") VALUES (2146, 'Daoka');
INSERT INTO power_up.companies ("id", "name") VALUES (2145, 'Tango Gameworks');
INSERT INTO power_up.companies ("id", "name") VALUES (2144, 'Sproing Interactive Media');
INSERT INTO power_up.companies ("id", "name") VALUES (2143, 'Deep Silver Vienna');
INSERT INTO power_up.companies ("id", "name") VALUES (2142, 'EA Bright Light');
INSERT INTO power_up.companies ("id", "name") VALUES (2141, 'FAKT Software');
INSERT INTO power_up.companies ("id", "name") VALUES (2140, 'Pepper Games');
INSERT INTO power_up.companies ("id", "name") VALUES (2139, 'DTP Entertainment');
INSERT INTO power_up.companies ("id", "name") VALUES (2138, 'Novitas Publishing');
INSERT INTO power_up.companies ("id", "name") VALUES (2137, 'cerasus.media');
INSERT INTO power_up.companies ("id", "name") VALUES (2136, 'Octopus Studio');
INSERT INTO power_up.companies ("id", "name") VALUES (2135, 'HyberDevbox Japan');
INSERT INTO power_up.companies ("id", "name") VALUES (2134, 'War Drum Studios');
INSERT INTO power_up.companies ("id", "name") VALUES (2133, 'Taffy Entertainment');
INSERT INTO power_up.companies ("id", "name") VALUES (2132, 'Bold Games');
INSERT INTO power_up.companies ("id", "name") VALUES (2131, 'Twelve Games');
INSERT INTO power_up.companies ("id", "name") VALUES (2130, 'Extra Mile Studios');
INSERT INTO power_up.companies ("id", "name") VALUES (2129, 'Noviy Disk');
INSERT INTO power_up.companies ("id", "name") VALUES (2128, 'Ware Interactive');
INSERT INTO power_up.companies ("id", "name") VALUES (2127, 'Zuxxes');
INSERT INTO power_up.companies ("id", "name") VALUES (2126, 'Frontline Studios');
INSERT INTO power_up.companies ("id", "name") VALUES (2125, 'Toontraxx');
INSERT INTO power_up.companies ("id", "name") VALUES (2124, 'Slam Games');
INSERT INTO power_up.companies ("id", "name") VALUES (2123, 'Foxconn Technology Group');
INSERT INTO power_up.companies ("id", "name") VALUES (2122, 'One True Game Studios');
INSERT INTO power_up.companies ("id", "name") VALUES (2121, 'Iron Galaxy Studios');
INSERT INTO power_up.companies ("id", "name") VALUES (2120, 'Grounding Inc');
INSERT INTO power_up.companies ("id", "name") VALUES (2119, 'Real Arcade');
INSERT INTO power_up.companies ("id", "name") VALUES (2118, 'Oxygen Interactive Software');
INSERT INTO power_up.companies ("id", "name") VALUES (2117, 'Gamenauts');
INSERT INTO power_up.companies ("id", "name") VALUES (4127, 'Sphere');
INSERT INTO power_up.companies ("id", "name") VALUES (4126, 'Andromeda Software');
INSERT INTO power_up.companies ("id", "name") VALUES (4125, 'Elorg');
INSERT INTO power_up.companies ("id", "name") VALUES (4124, 'Vadim Gerasimov');
INSERT INTO power_up.companies ("id", "name") VALUES (4123, 'Alexey Pajitnov');
INSERT INTO power_up.companies ("id", "name") VALUES (4122, 'DR Korea');
INSERT INTO power_up.companies ("id", "name") VALUES (4121, 'Tandy');
INSERT INTO power_up.companies ("id", "name") VALUES (4120, 'AcademySoft');
INSERT INTO power_up.companies ("id", "name") VALUES (4119, 'Mirrorsoft');
INSERT INTO power_up.companies ("id", "name") VALUES (4118, 'SoMa Play');
INSERT INTO power_up.companies ("id", "name") VALUES (4117, 'Ralston-Purina');
INSERT INTO power_up.companies ("id", "name") VALUES (4116, 'Digital Café');
INSERT INTO power_up.companies ("id", "name") VALUES (4115, 'Lion Entertainment');
INSERT INTO power_up.companies ("id", "name") VALUES (4114, 'Bashou House');
INSERT INTO power_up.companies ("id", "name") VALUES (4113, 'Art Data Interactive');
INSERT INTO power_up.companies ("id", "name") VALUES (4112, 'Soft Bank');
INSERT INTO power_up.companies ("id", "name") VALUES (4111, 'Midway Studios San Diego');
INSERT INTO power_up.companies ("id", "name") VALUES (4110, 'Micronics');
INSERT INTO power_up.companies ("id", "name") VALUES (4109, 'Kabam');
INSERT INTO power_up.companies ("id", "name") VALUES (4108, 'Wild Shadow Studios');
INSERT INTO power_up.companies ("id", "name") VALUES (4107, 'SCE Australia');
INSERT INTO power_up.companies ("id", "name") VALUES (4106, 'Light Weight');
INSERT INTO power_up.companies ("id", "name") VALUES (4105, 'Kenproduction');
INSERT INTO power_up.companies ("id", "name") VALUES (4104, 'Mox Co., Ltd.');
INSERT INTO power_up.companies ("id", "name") VALUES (4103, 'SRD Co., Ltd.');
INSERT INTO power_up.companies ("id", "name") VALUES (4102, 'Aventurine SA');
INSERT INTO power_up.companies ("id", "name") VALUES (4101, 'Aventurine');
INSERT INTO power_up.companies ("id", "name") VALUES (4100, 'Aventurine SA');
INSERT INTO power_up.companies ("id", "name") VALUES (4099, 'Open Corp');
INSERT INTO power_up.companies ("id", "name") VALUES (4098, 'Bootleg');
INSERT INTO power_up.companies ("id", "name") VALUES (4097, 'Vertigo Games');
INSERT INTO power_up.companies ("id", "name") VALUES (4096, 'Vertigo Games');
INSERT INTO power_up.companies ("id", "name") VALUES (4095, 'Kouyousha');
INSERT INTO power_up.companies ("id", "name") VALUES (4094, 'Aldorlea Games');
INSERT INTO power_up.companies ("id", "name") VALUES (4093, 'H2 Interactive');
INSERT INTO power_up.companies ("id", "name") VALUES (4092, 'N3V Games');
INSERT INTO power_up.companies ("id", "name") VALUES (4091, 'PQube');
INSERT INTO power_up.companies ("id", "name") VALUES (4090, 'Havas Interactive');
INSERT INTO power_up.companies ("id", "name") VALUES (4089, 'Disastercake');
INSERT INTO power_up.companies ("id", "name") VALUES (4088, 'Masatsugu Shinozaki Group');
INSERT INTO power_up.companies ("id", "name") VALUES (4087, 'Swing');
INSERT INTO power_up.companies ("id", "name") VALUES (4086, 'Otogi LLC');
INSERT INTO power_up.companies ("id", "name") VALUES (4085, 'Bacteria Inc.');
INSERT INTO power_up.companies ("id", "name") VALUES (4084, 'Zener Works Inc.');
INSERT INTO power_up.companies ("id", "name") VALUES (4083, 'PlayStation C.A.M.P.');
INSERT INTO power_up.companies ("id", "name") VALUES (4082, 'The Sequence Group');
INSERT INTO power_up.companies ("id", "name") VALUES (4081, 'Cynergy Systems Inc');
INSERT INTO power_up.companies ("id", "name") VALUES (4080, 'Los Angles/ Seattle/ Tokyo Music Production Unit');
INSERT INTO power_up.companies ("id", "name") VALUES (4079, 'Bristol/London Music Production Unit');
INSERT INTO power_up.companies ("id", "name") VALUES (4078, 'Experis');
INSERT INTO power_up.companies ("id", "name") VALUES (4077, 'Software Project');
INSERT INTO power_up.companies ("id", "name") VALUES (4076, 'Tynesoft');
INSERT INTO power_up.companies ("id", "name") VALUES (4075, 'Software Projects');
INSERT INTO power_up.companies ("id", "name") VALUES (4074, 'TNX Music Recordings');
INSERT INTO power_up.companies ("id", "name") VALUES (4073, 'Similis');
INSERT INTO power_up.companies ("id", "name") VALUES (4072, 'LSP');
INSERT INTO power_up.companies ("id", "name") VALUES (4071, 'Bruno R. Marcos');
INSERT INTO power_up.companies ("id", "name") VALUES (4070, 'Eyebrow Interactive');
INSERT INTO power_up.companies ("id", "name") VALUES (4069, 'Derek Yu');
INSERT INTO power_up.companies ("id", "name") VALUES (4068, 'Infinitap Games');
INSERT INTO power_up.companies ("id", "name") VALUES (4067, 'WJS Design');
INSERT INTO power_up.companies ("id", "name") VALUES (4066, 'Equilibrium');
INSERT INTO power_up.companies ("id", "name") VALUES (4065, 'Curve Studios');
INSERT INTO power_up.companies ("id", "name") VALUES (4064, 'Superflat Games');
INSERT INTO power_up.companies ("id", "name") VALUES (4063, 'Limasse Five');
INSERT INTO power_up.companies ("id", "name") VALUES (4062, '4PM GAME');
INSERT INTO power_up.companies ("id", "name") VALUES (4061, 'Bojan Brbora');
INSERT INTO power_up.companies ("id", "name") VALUES (4060, 'Adult Swim');
INSERT INTO power_up.companies ("id", "name") VALUES (4059, 'Necrophone Games');
INSERT INTO power_up.companies ("id", "name") VALUES (4058, 'Mario Kart Band');
INSERT INTO power_up.companies ("id", "name") VALUES (4057, 'PTD Guideline Compliance');
INSERT INTO power_up.companies ("id", "name") VALUES (4056, 'Sessing Music Services');
INSERT INTO power_up.companies ("id", "name") VALUES (4055, 'Synapse Films');
INSERT INTO power_up.companies ("id", "name") VALUES (4054, 'Shooting Star');
INSERT INTO power_up.companies ("id", "name") VALUES (4053, 'Corbis Images');
INSERT INTO power_up.companies ("id", "name") VALUES (4052, 'Viacom Entertainment Group');
INSERT INTO power_up.companies ("id", "name") VALUES (4051, 'Kokopeli Digital Studios');
INSERT INTO power_up.companies ("id", "name") VALUES (4050, 'DePaul University students');
INSERT INTO power_up.companies ("id", "name") VALUES (4049, 'Ravensburger Interactive Media');
INSERT INTO power_up.companies ("id", "name") VALUES (4048, 'Super Empire');
INSERT INTO power_up.companies ("id", "name") VALUES (4047, 'Wanderlust Interactive');
INSERT INTO power_up.companies ("id", "name") VALUES (4046, 'Manley and Associates Inc');
INSERT INTO power_up.companies ("id", "name") VALUES (4045, 'TecMagik');
INSERT INTO power_up.companies ("id", "name") VALUES (4044, 'PitStop Productions');
INSERT INTO power_up.companies ("id", "name") VALUES (4043, 'Quantic Labs');
INSERT INTO power_up.companies ("id", "name") VALUES (4042, 'TZWART Software');
INSERT INTO power_up.companies ("id", "name") VALUES (4041, 'The Third Floor Inc.');
INSERT INTO power_up.companies ("id", "name") VALUES (4040, 'Snark Co. Ltd');
INSERT INTO power_up.companies ("id", "name") VALUES (4039, 'Xi''an Panish Informantion Technology');
INSERT INTO power_up.companies ("id", "name") VALUES (4038, 'Little Red Zombies');
INSERT INTO power_up.companies ("id", "name") VALUES (4037, 'Kylin Magical Image');
INSERT INTO power_up.companies ("id", "name") VALUES (4036, 'SomaTone Audio');
INSERT INTO power_up.companies ("id", "name") VALUES (4035, 'Polyassets United Inc.');
INSERT INTO power_up.companies ("id", "name") VALUES (4034, 'Nintendo of Europe');
INSERT INTO power_up.companies ("id", "name") VALUES (4033, 'GL33K, LLC');
INSERT INTO power_up.companies ("id", "name") VALUES (4032, 'Sapient Corporation');
INSERT INTO power_up.companies ("id", "name") VALUES (4031, 'Darkblack');
INSERT INTO power_up.companies ("id", "name") VALUES (4030, 'Mobliss');
INSERT INTO power_up.companies ("id", "name") VALUES (4029, 'Xilam');
INSERT INTO power_up.companies ("id", "name") VALUES (4028, 'Citizen Software');
INSERT INTO power_up.companies ("id", "name") VALUES (4027, 'Focus Multimedia');
INSERT INTO power_up.companies ("id", "name") VALUES (4026, 'Gakken');
INSERT INTO power_up.companies ("id", "name") VALUES (4025, 'G-Amusements');
INSERT INTO power_up.companies ("id", "name") VALUES (4024, 'StarCraft, Inc.');
INSERT INTO power_up.companies ("id", "name") VALUES (4023, 'Guildhall Leisure Services Ltd.');
INSERT INTO power_up.companies ("id", "name") VALUES (4022, 'Happy Feet Foley');
INSERT INTO power_up.companies ("id", "name") VALUES (4021, 'Eastworks');
INSERT INTO power_up.companies ("id", "name") VALUES (4020, 'Trias Digital');
INSERT INTO power_up.companies ("id", "name") VALUES (4019, 'VMC Game Labs, Montreal');
INSERT INTO power_up.companies ("id", "name") VALUES (4018, 'The Research Centaur');
INSERT INTO power_up.companies ("id", "name") VALUES (4017, 'Earbash Inc.');
INSERT INTO power_up.companies ("id", "name") VALUES (4016, 'Minor Key Games');
INSERT INTO power_up.companies ("id", "name") VALUES (4015, 'Recreational Brainware');
INSERT INTO power_up.companies ("id", "name") VALUES (4014, 'Vblank Entertainment');
INSERT INTO power_up.companies ("id", "name") VALUES (4013, 'Active Enterprises');
INSERT INTO power_up.companies ("id", "name") VALUES (4012, 'Innocuous Games');
INSERT INTO power_up.companies ("id", "name") VALUES (4011, 'Lost Boys Games');
INSERT INTO power_up.companies ("id", "name") VALUES (4010, 'Macrospace');
INSERT INTO power_up.companies ("id", "name") VALUES (4009, 'Sanritsu');
INSERT INTO power_up.companies ("id", "name") VALUES (4008, 'Sega Australia');
INSERT INTO power_up.companies ("id", "name") VALUES (4007, 'Sega France');
INSERT INTO power_up.companies ("id", "name") VALUES (4006, 'Sega Benelux');
INSERT INTO power_up.companies ("id", "name") VALUES (4005, 'Sega Spain');
INSERT INTO power_up.companies ("id", "name") VALUES (4004, 'Sega Germany');
INSERT INTO power_up.companies ("id", "name") VALUES (4003, 'Sega International');
INSERT INTO power_up.companies ("id", "name") VALUES (4002, 'Sega UK');
INSERT INTO power_up.companies ("id", "name") VALUES (4001, 'Sega of Europe');
INSERT INTO power_up.companies ("id", "name") VALUES (4000, 'Bug-Tracker INC.');
INSERT INTO power_up.companies ("id", "name") VALUES (3999, 'WOMB Music');
INSERT INTO power_up.companies ("id", "name") VALUES (3998, 'Gamebase USA');
INSERT INTO power_up.companies ("id", "name") VALUES (3997, 'Gamebase Co., Ltd.');
INSERT INTO power_up.companies ("id", "name") VALUES (3996, 'Experimental Gameplay');
INSERT INTO power_up.companies ("id", "name") VALUES (3995, 'Eidos Interactive Europe');
INSERT INTO power_up.companies ("id", "name") VALUES (3994, 'Eidos Interactive US');
INSERT INTO power_up.companies ("id", "name") VALUES (3993, 'IMGS, INC.');
INSERT INTO power_up.companies ("id", "name") VALUES (3992, 'Ion Storm Support');
INSERT INTO power_up.companies ("id", "name") VALUES (3991, 'Russel');
INSERT INTO power_up.companies ("id", "name") VALUES (3990, 'A10');
INSERT INTO power_up.companies ("id", "name") VALUES (3989, 'Playcast-media');
INSERT INTO power_up.companies ("id", "name") VALUES (3988, 'ZeniMax Australia');
INSERT INTO power_up.companies ("id", "name") VALUES (3987, 'Designware Co. Ltd.');
INSERT INTO power_up.companies ("id", "name") VALUES (3986, 'Arriba Entertainment');
INSERT INTO power_up.companies ("id", "name") VALUES (3985, 'Film Score LLC');
INSERT INTO power_up.companies ("id", "name") VALUES (3984, 'Nashville Music Scoring Orchestra');
INSERT INTO power_up.companies ("id", "name") VALUES (3983, 'Forcewick Sound Design');
INSERT INTO power_up.companies ("id", "name") VALUES (3982, 'Casting Boat');
INSERT INTO power_up.companies ("id", "name") VALUES (3981, 'Mozoo');
INSERT INTO power_up.companies ("id", "name") VALUES (3980, 'The Workshop Entertainment');
INSERT INTO power_up.companies ("id", "name") VALUES (3979, 'Konami Digital Entertainment Co. Ltd.');
INSERT INTO power_up.companies ("id", "name") VALUES (3978, 'Konami Digital Entertainment Inc.');
INSERT INTO power_up.companies ("id", "name") VALUES (3977, 'Konami Digital Entertainment B.V.');
INSERT INTO power_up.companies ("id", "name") VALUES (3976, 'An.X');
INSERT INTO power_up.companies ("id", "name") VALUES (3975, 'Konami UK Studio');
INSERT INTO power_up.companies ("id", "name") VALUES (3974, 'The Slovak National Symphony Orchestra');
INSERT INTO power_up.companies ("id", "name") VALUES (3973, 'The Bratislawa Symphnoy Choir');
INSERT INTO power_up.companies ("id", "name") VALUES (3972, 'The Bratislawa Symphony Orchestra');
INSERT INTO power_up.companies ("id", "name") VALUES (3971, 'Master Symphony Orchestra Valencia');
INSERT INTO power_up.companies ("id", "name") VALUES (3970, 'globalvoX');
INSERT INTO power_up.companies ("id", "name") VALUES (3969, 'East West');
INSERT INTO power_up.companies ("id", "name") VALUES (3968, 'Project Sam');
INSERT INTO power_up.companies ("id", "name") VALUES (3967, 'Nord Media Films Studio');
INSERT INTO power_up.companies ("id", "name") VALUES (3966, 'ESABU');
INSERT INTO power_up.companies ("id", "name") VALUES (3965, 'Musikart Studios');
INSERT INTO power_up.companies ("id", "name") VALUES (3964, 'G.A.F. Ivry');
INSERT INTO power_up.companies ("id", "name") VALUES (3963, 'Sinfonia Pop Orchestra');
INSERT INTO power_up.companies ("id", "name") VALUES (3962, 'Game Audio Factory');
INSERT INTO power_up.companies ("id", "name") VALUES (3961, 'SoftClub');
INSERT INTO power_up.companies ("id", "name") VALUES (3960, 'Team N+');
INSERT INTO power_up.companies ("id", "name") VALUES (3959, 'Sos');
INSERT INTO power_up.companies ("id", "name") VALUES (3958, 'Bandai Namco Games');
INSERT INTO power_up.companies ("id", "name") VALUES (3957, 'M2H');
INSERT INTO power_up.companies ("id", "name") VALUES (3956, 'IronMonkey Studios');
INSERT INTO power_up.companies ("id", "name") VALUES (3954, 'RSP');
INSERT INTO power_up.companies ("id", "name") VALUES (3953, 'Magic Bytes');
INSERT INTO power_up.companies ("id", "name") VALUES (3952, 'ISCO');
INSERT INTO power_up.companies ("id", "name") VALUES (3951, 'Denton Designs');
INSERT INTO power_up.companies ("id", "name") VALUES (3950, 'Blue Turtle');
INSERT INTO power_up.companies ("id", "name") VALUES (3949, 'Magifact');
INSERT INTO power_up.companies ("id", "name") VALUES (3948, 'Cybersoft');
INSERT INTO power_up.companies ("id", "name") VALUES (3947, 'Young Jump Animation');
INSERT INTO power_up.companies ("id", "name") VALUES (3946, '#30 Jutz Zollern Workroom');
INSERT INTO power_up.companies ("id", "name") VALUES (3945, 'Ubisoft Limited');
INSERT INTO power_up.companies ("id", "name") VALUES (3944, 'Latis Global Communications');
INSERT INTO power_up.companies ("id", "name") VALUES (3943, 'Ubisoft South Korea');
INSERT INTO power_up.companies ("id", "name") VALUES (3942, 'entalize');
INSERT INTO power_up.companies ("id", "name") VALUES (3941, 'Scillex');
INSERT INTO power_up.companies ("id", "name") VALUES (3940, 'U-TRAX');
INSERT INTO power_up.companies ("id", "name") VALUES (3939, 'Tonstudio Krauthausen GmbH');
INSERT INTO power_up.companies ("id", "name") VALUES (3938, 'Das Hörspielstudio XBerg GmbH');
INSERT INTO power_up.companies ("id", "name") VALUES (3937, 'mouse-power GmbH');
INSERT INTO power_up.companies ("id", "name") VALUES (3936, 'Wave London');
INSERT INTO power_up.companies ("id", "name") VALUES (3935, 'Studio Technicolor');
INSERT INTO power_up.companies ("id", "name") VALUES (3934, 'Vision Globale');
INSERT INTO power_up.companies ("id", "name") VALUES (3933, 'Studio 451 Inc.');
INSERT INTO power_up.companies ("id", "name") VALUES (3932, 'Paul Johnston Studio');
INSERT INTO power_up.companies ("id", "name") VALUES (3931, 'Studio de I''Est');
INSERT INTO power_up.companies ("id", "name") VALUES (3930, 'Hotel 2 Tango');
INSERT INTO power_up.companies ("id", "name") VALUES (3929, 'Les services d''urgence Medic');
INSERT INTO power_up.companies ("id", "name") VALUES (3928, 'Ubisoft Chengdu');
INSERT INTO power_up.companies ("id", "name") VALUES (3927, 'Viacom International');
INSERT INTO power_up.companies ("id", "name") VALUES (3926, 'Game Factory');
INSERT INTO power_up.companies ("id", "name") VALUES (3925, 'Nick Jr. Interactive');
INSERT INTO power_up.companies ("id", "name") VALUES (3924, 'Six by nine');
INSERT INTO power_up.companies ("id", "name") VALUES (3923, 'Studio Pixel');
INSERT INTO power_up.companies ("id", "name") VALUES (3922, 'Red Rocket Games');
INSERT INTO power_up.companies ("id", "name") VALUES (3921, 'Blendo Games');
INSERT INTO power_up.companies ("id", "name") VALUES (3920, 'Toho');
INSERT INTO power_up.companies ("id", "name") VALUES (3918, 'Cyberdreams, Inc. - duplicate');
INSERT INTO power_up.companies ("id", "name") VALUES (3917, 'Media Station');
INSERT INTO power_up.companies ("id", "name") VALUES (3916, 'Playskool');
INSERT INTO power_up.companies ("id", "name") VALUES (3915, 'Vortex Media Arts');
INSERT INTO power_up.companies ("id", "name") VALUES (3914, 'Playbox');
INSERT INTO power_up.companies ("id", "name") VALUES (3913, 'EnjoyUp Games');
INSERT INTO power_up.companies ("id", "name") VALUES (3912, 'Asterizm Corporation');
INSERT INTO power_up.companies ("id", "name") VALUES (3911, 'Independant - duplicate');
INSERT INTO power_up.companies ("id", "name") VALUES (3910, 'Pygmy Studio');
INSERT INTO power_up.companies ("id", "name") VALUES (3909, 'Nigoro');
INSERT INTO power_up.companies ("id", "name") VALUES (3908, 'Picsoft Studio');
INSERT INTO power_up.companies ("id", "name") VALUES (3907, 'Creative Patterns');
INSERT INTO power_up.companies ("id", "name") VALUES (3906, 'Anuman Interactive');
INSERT INTO power_up.companies ("id", "name") VALUES (3905, 'Little Worlds Studio');
INSERT INTO power_up.companies ("id", "name") VALUES (3904, 'Towa Chiki');
INSERT INTO power_up.companies ("id", "name") VALUES (3903, 'BioWare Montreal');
INSERT INTO power_up.companies ("id", "name") VALUES (3902, 'SPD Technology Group');
INSERT INTO power_up.companies ("id", "name") VALUES (3901, 'Toei Zukun Laboratory');
INSERT INTO power_up.companies ("id", "name") VALUES (3900, 'Red Light Management');
INSERT INTO power_up.companies ("id", "name") VALUES (3899, 'Dance Therapy Productions');
INSERT INTO power_up.companies ("id", "name") VALUES (3898, 'Studio Mausu');
INSERT INTO power_up.companies ("id", "name") VALUES (3897, 'Synthesis Germany');
INSERT INTO power_up.companies ("id", "name") VALUES (3896, 'La Marque Rose');
INSERT INTO power_up.companies ("id", "name") VALUES (3895, 'ZeniMax Benelux');
INSERT INTO power_up.companies ("id", "name") VALUES (3894, 'ZeniMax Germany');
INSERT INTO power_up.companies ("id", "name") VALUES (3893, 'ZeniMax France');
INSERT INTO power_up.companies ("id", "name") VALUES (3892, 'Hansoft');
INSERT INTO power_up.companies ("id", "name") VALUES (3891, 'Diligentia');
INSERT INTO power_up.companies ("id", "name") VALUES (3890, 'Standard Deviation');
INSERT INTO power_up.companies ("id", "name") VALUES (3889, 'Fat Little Indian Boy');
INSERT INTO power_up.companies ("id", "name") VALUES (3888, 'Somnio Studios');
INSERT INTO power_up.companies ("id", "name") VALUES (3887, 'Volta Creations Inc.');
INSERT INTO power_up.companies ("id", "name") VALUES (3886, '1910 Design & Communication');
INSERT INTO power_up.companies ("id", "name") VALUES (3885, 'QW Creatives');
INSERT INTO power_up.companies ("id", "name") VALUES (3884, 'Daybreak CG');
INSERT INTO power_up.companies ("id", "name") VALUES (3883, 'RIVER');
INSERT INTO power_up.companies ("id", "name") VALUES (3882, 'SDFX');
INSERT INTO power_up.companies ("id", "name") VALUES (3881, 'Game Audio Australia');

--Games
INSERT INTO power_up.games VALUES (18915, 'Blackroom', 'John Romero and Adrian Carmack reunite to make BLACKROOM, a visceral, action-packed FPS set in a holographic simulation gone rogue.', 0, '2018-12-30');
INSERT INTO power_up.games VALUES (7204, 'Project Phoenix', 'Project Phoenix is a Japanese RPG combined with a squad-based Real Time Strategy game created by a team of talented Eastern and Western developers and artists who have worked on many blockbuster videogame titles. The team we have brought together has allowed us to fuse together Japanese artistic aesthetics and Western design functionality. As a result, we are better able to draw inspiration from the best of both worlds, bringing a richer, more immersive gameplay experience worthy of JRPG fans like you!

In essence, Project Phoenix breaks new ground in many ways, including:

Being Kickstarter’s FIRST Japan-based video game project!
Bringing together top-notch talent, combining Eastern AND Western developers with TONS of AAA experience!
Being Final Fantasy composer Nobuo Uematsu’s FIRST indie game project EVER!
Having art that balances Western functionality with Japanese aesthetics!
Being a JRPG developed by JRPG veterans!
Currently, the game’s planned release date is set for June 2015 and will be available for PC, iOS, and Android mobile devices. More details will be forthcoming, but in the meantime, if you would like to be a part of our vision, you can still back this project using PayPal. Let us lead the way and show how our vision can bring about a rebirth in the JRPG genre!', 0, '2018-12-30');
INSERT INTO power_up.games VALUES (17244, 'Ghost Theory', 'Ghost Theory is currently on Kickstarter.
It is set to be a single player first-person horror game developed for Virtual Reality headsets but playable without them.

In Ghost Theory you are a paranormal investigator and must investigate haunted places based on real locations and discover the truth behind them. The locations include castles, churches, asylums, and graveyards. The locations will be open-ended so you get to choose your path through each area. But there are risks to exploring to much.

The game will focus on stealth and exploration. Instead of getting directly attacked, ghosts will stalk and harass you as you investigate locations. You will have to find ways to avoid or trap them while managing your stress as they attempt to scare you to death. You will have upgradable tools and abilities to use as you progress in the game to help you search and survive.', 0, '2018-12-30');
INSERT INTO power_up.games VALUES (14741, 'Psychonauts 2', '', 0, '2018-12-30');
INSERT INTO power_up.games VALUES (18375, 'System Shock Remastered', 'After 22 years, cyberpunk cult classic System Shock is getting an update. A "reimagined" version of the 1994 Windows PC game is in the works at Night Dive Studios for PC and Xbox One, and it''s a project that company founder Stephen Kick says he can hardly believe is really happening.', 0, '2018-03-30');
INSERT INTO power_up.games VALUES (6071, 'STORM', 'An upcoming sci-fi co-op FPS.', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (9598, 'Spy Party', 'SpyParty is a tense competitive spy game set at a high society party. It''s about subtle behavior, perception, and deception, instead of guns, car chases, and explosions. One player is the Spy, trying to accomplish missions while blending into the crowd. The other player is the Sniper, who has one bullet with which to find and terminate the Spy!

SpyParty is in Early-Access Beta right now, which means you can buy and play the game immediately, you''ll get all the updates during development, and then you''ll get the final version when it ships. The beta community is friendly and welcomes new players, and there''s a private beta forum where players share strategies.', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (9176, 'Call of Cthulhu', 'Developed by seasoned studio Cyanide, Call of Cthulhu is an RPG-Investigation game with psychological horror and stealth mechanics, set in a deeply immersive world. On a mission to find the truth behind the death of an acclaimed artist and her family on a backwater island, the player will soon uncover a more disturbing truth as the Great Dreamer, Cthulhu, prepares its awakening...', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (19831, 'Kologeon', '"Kologeon is a dark mystic action adventure set in a procedurally generated handcrafted realm tormented by demons and spirits.

You are an immortal soul that can traverse dimensions outside of physical bodies, fighting for centuries as you establish permanent monuments to mark your journey.

Kologeon builds around the ideas of an unfolding experience, freedom of exploration, and self-discovery where quests and guides are not present letting you craft your own journey.

With procedural generation we aim to create un-repetitive experiences where death is still a setback but won''t mean that you''ll have to memorize levels and replay the same thing over again. However this isn''t a roguelike. Your actions and progress stay permanent.


- Explore and interact with a world built on top of itself, coexisting with another.

- Uncover the story through a gloomy dark atmosphere, interaction with spirits, and events you witness or cause. Every place and - feature of the game is carefully crafted with a consideration of the bigger story. We want to create something deep that you could really lose yourself inside of.

- Fly, roll, and rip through demons with a fast satisfying mobility based combat involving astral projection, giant spiritual weapons, time disruption, possession and manipulation of souls. Forge weapons from your enemies and manifest their abilities.

- Enemies are able to dodge, parry, and tear you to pieces as the game is easy to pick up but difficult to master."', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (19817, 'Scorn', 'Scorn is an atmospheric first person horror adventure game set in a nightmarish universe of odd forms and somber tapestry. It is designed around an idea of "being thrown into the world". Isolated and lost inside this dream-like world you will explore different interconnected regions in a non-linear fashion. The unsettling environment is a character itself. Every location contains its own theme (story), puzzles and characters that are integral in creating a cohesive lived in world. Throughout the game you will open up new areas, acquire different skill sets, weapons, various items and try to comprehend the sights presented to you.', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (19729, 'Mr. Shifty', 'Quit creeping through shadows, stop surveilling security patterns, and ditch the fancy toys. An expert thief and master of infiltration, Mr. Shifty can get inside any place he wants with just the power of his fists…okay, teleporting helps.

A speed-stealth, kung-fu brawler about a teleporting thief, Mr. Shifty combines fast top-down gameplay with the ferocity of ''90s Hong Kong action cinema, coming to PC (Win/Mac), PS4, and Xbox One in 2017.

Pummel your way through 75 levels of heists, rescue missions, boss battles, and down-''n''-dirty brawls. Outsmart your opponents by luring them into traps and tricking them into shooting each other. Activate slow-motion to dodge when the gunfire gets too thick, and watch your back, because it only takes one bullet to bring you down.

Newly announced from indie publisher tinyBuild GAMES, Mr. Shifty is the debut title from Team Shifty; a close-knit group of four developers from Brisbane, Australia, who previously worked as a team at Halfbrick for over five years and have collectively shipped 21 games for consoles and handheld.', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (19597, 'Wilson''s Heart', 'Wilson''s Heart is an immersive first-person psychological thriller set in a 1940''s hospital that has undergone a haunting transformation. In this original VR adventure, you become Robert Wilson, a patient who awakens to the shocking discovery that his heart has been replaced with a mysterious device.', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (19596, 'Ever Oasis', '"From the team behind The Legend of Zelda: Majora''s Mask 3D comes a daring new adventure that expands on everything RPG fans love about the genre. As a chosen Seedling, your mission is to build a prosperous oasis by working with your partner, lsuna. Meet and ally with members of other tribes to complete your mission, while battling against the Chaos threatening peace in the desert. Battle enemies in real-time combat as you switch between three party members and explore a savage desert. Forage for materials in caves and puzzle-filled dungeons to earn dewadems, which are used to grow new shops and even grow gear!

Features:
- Dive into a new take on the Adventure RPG genre in a brand new IP.
- Explore a savage desert, deep caves, and puzzle-filled dungeons.
- Forage materials to make products at shops, or synthesize equipment and items at your treehouse.
- Complete missions to recruit new villagers, each with their own shops, weapons and abilities, then bring them exploring/foraging.
- Earn dewadem, currency, over time as shops sell their wares to build new Bloom Booths.
- Developed by GREZZO Co., Ltd. the team behind The Legend of Zelda: Majora''s Mask 3D
- Battle wildlife that are possessed by Chaos and other enemies in real-time combat, while actively switching between party members.
- Immerse into a beautifully created world inspired by Egyptian culture/mythology."', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (19541, 'State of Decay 2', 'The next installment in the critically acclaimed "State of Decay" franchise immerses you in an all-new, multiplayer zombie survival fantasy. Build a lasting community, working with other players or solo to overcome challenges while exploring your own unique story in a world that remembers the choices you make.', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (19531, 'Prey', 'In Prey, you awaken aboard Talos I, a space station orbiting the moon in the year 2032. You are the key subject of an experiment meant to alter humanity forever – but things have gone terribly wrong. The space station has been overrun by hostile aliens and you are now being hunted. As you dig into the dark secrets of Talos I and your own past, you must survive using the tools found on the station, your wits, weapons, and mind-bending abilities.', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (19530, 'Quake Champions', '"Quake. Is. Back. The fast, skill-based arena-style competition that turned the original Quake games into multiplayer legends is making a triumphant return with Quake Champions. Running at an impressive 120hz with unlocked framerates, id Software’s new multiplayer shooter is getting ready to take PC gaming by storm once more."', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (19470, 'Injustice 2', 'Power up and build the ultimate version of your favorite DC legends in INJUSTICE 2. With a massive selection of DC Super Heroes and Super-Villains, INJUSTICE 2 allows you to equip every iconic character with unique and powerful gear earned throughout the game. Experience an unprecedented level of control over how your favorite characters look, how they fight, and how they develop across a huge variety of game modes. This is your super Hero. Your Journey. Your Injustice.', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (19455, 'Final Fantasy XII: The Zodiac Age', '', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (19453, 'Agony', 'Agony is a first-person survival horror game currently in development.
Players will begin their journey as tormented soul within the depths of hell without any memories about his past.

The special ability to control people on their path, and even possess simple minded demons, gives the player the necessary measures to survive in the extreme conditions they are in.

By exploring hostile environment and interacting with the other weary souls of the hellscape, the hero will soon understand that there is only one way to escape from Hell, and it will require a meeting of the mystical Red Goddess.', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (19448, 'ECHO', 'After a century in stasis the girl En arrives at her destination: A Palace out of legend, a marvel of the old civilization eons gone, still awaiting its first human occupants. Out here, using forgotten technologies, she hopes to bring back a life that shouldn’t have been lost.

In ECHO everything has consequence: As you try to wield its magical technologies it becomes apparent that the Palace has a will of its own… It studies everything you do, everything you are – to use it against you.

The Palace creates Echoes – exact copies of you in every way. They behave like you and only do the things you do. So the way you play the game shapes your enemy. If you run, soon the Echoes will get faster. If you sneak, they will get stealthier. If you shoot, they will start to shoot back. The game constantly reacts to your every choice and input.

The Palace "reboots" every few minutes, resulting in a blackout. This blackout-cycle is the rhythm at which the "Echoes" get updated with your latest behavior, learning and unlearning from your actions. During the blackout the palace is blind, giving you the freedom to act without consequence. This is the time to run and gun and do all the things you don’t want the Echoes to learn.

The experience is one of being up against your own choices from the last blackout-cycle, giving you a way of shaping the game from cycle to cycle. It is up to you whether you prefer to keep a low profile or if you choose to go head on and deal with the consequences later – one thing is certain you need to keep your wits about you as you face the ultimate enemy: Yourself.', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (19440, 'Agents of Mayhem', 'Agents of Mayhem is the new open world game from Volition, the studio that brought you the Saints Row franchise. Play as a cast of unique and diverse Super Agents from all over the world, and lead them in MAYHEM’s epic fight to save the city of Seoul from the destructive schemes of the evil supervillain organization LEGION!', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (19408, 'Xenonauts 2', '', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (19297, 'Ken Follett''s: The Pillars of the Earth', 'England, 12th century: Philip the monk becomes Prior of the small abbey of Kingsbridge. At the same time, a boy called Jack is raised by his outlawed mother in the woods. His apprenticeship as a stonemason paves his way to become a great architect. Soon, his steps lead him to Kingsbridge where he will build one of the greatest cathedrals England has ever seen. Aliena and Richard have to survive on their own, after the sibling''s father, the Earl of Shiring, has been incarcerated and murdered. Aliena vows to make her brother the rightful heir and Earl of Shiring. Like Jack, their way leads them to Kingsbridge. Aliena puts her expertise as a tradeswoman to good use, supporting the cathedral''s construction and falls in love with Jack. But Kingsbridge and its people are in grave danger. Philip''s rivals, Bishop Waleran and Wiliam, a vengeful noble rejected by Aliena, see the town and its rise to importance as a thorn in their flesh. They want to see Kingsbridge burn.

The game-adaption of the world-bestseller "The Pillars of the Earth" is the first co-operative project of Daedalic Entertainment and Bastei Lübbe. This game will be more than just complementary media to the book and will instead retell the story in a new, interactive way. A team of about 20 people works to create a multi-platform adaption of this bestseller. The writers are also in contact and co-operation with the Follett Office and Ken Follett himself. Daedalic is the only studio at the time adapting such an epic reading-experience into an interactive format.

The game itself will be released in 2017, at the same time the third novel of the Kingsbridge-Series will be published. The game will be internationally available for PC, Mac, Linux, PS4, Xbox One and mobile devices.', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (6879, 'Space Hulk: Deathwing', 'Space Hulk: Deathwing is a First-Person Shooter experience of Games Workshop''s classic Space Hulk boardgame set in the universe of Warhammer 40,000, developed on Unreal Engine 4 by Streum On Studio, the team behind E.Y.E: Divine Cybermancy. Published by Focus Home Interactive and co-produced by Cyanide Studio, Space Hulk: Deathwing offers players the chance to experience a desperate battle against Genestealers in the claustrophobic tunnels of a Space Hulk, as they will gain skills, new abilities and new equipment thanks to experience earned during perilous missions. 

Space Hulks drift along the currents of the Warp, the immaterial dimension of Chaos. Made up of gigantic mangled asteroids, derelict ship wrecks and other spatial debris, they sometimes grow to the size of a moon and often contain treasured and ancient lost technologies. However, they are ripe with danger including infestations of the lethal Genestealers. 

Players take on the role of a Space Marine from one of the most secret and most feared Space Marine Companies: the Deathwing from the Dark Angels. Strap on your Terminator armour and equip the emblematic weaponry of the Space Marines to overcome the threats awaiting you in the Space Hulk. As a Librarian, you will also master the destructive powers of the Psykers. Your skills and performance in battle grant you Fervor Points, to spend on 4 skill trees allowing you to improve your abilities, unlock new powers, access powerful relics and devastating new weapons.', 0, '2016-10-31');
INSERT INTO power_up.games VALUES (6705, 'A Hat in Time', 'A Hat in Time is a 3D collect-a-thon platformer in the spirit of the beloved Nintendo and Rare games from the Nintendo 64 era. Think Banjo-Kazooie, The Legend of Zelda and Super Mario 64 all in one! The game features Hat Kid, a girl who travels time and space to protect the world from the evil Mustache Girl!', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (8729, 'Vampyr', 'Vampyr is set in early 20th century Britain as the country is gripped by the lethal Spanish flu and the streets of London are crippled by disease, violence and fear. In a disorganized and ghostly city, those foolish, desperate, or unlucky enough to walk the streets lay prey to Britain’s most elusive predators: the vampires. Emerging from the chaos, a tormented figure awakens. You are Jonathan E. Reid, a high-ranking military surgeon transformed into a vampire upon his return home from the frontline.

Explore the darkly atmospheric streets of early 20th century London, and interact with a multitude of characters with their own identities and importance. Accept and fulfill the missions they give you, but don’t forget: sooner or later, you will have to feed, and make a difficult choice... who will be your prey? Absolutely all characters in the game are potential victims of your vampiric lust. Carefully study the habits of your next victim, his or her relationships with other characters, and set up your strategy to feed, unnoticed: seduce them, change their daily habits, or make sure they end up alone in a dark street...

Be careful who you choose to hunt, as they will be gone forever, and their death will impact in a meaningful way the world that surrounds you. Feeding on human blood will not just keep you "alive"; it will also unlock new vampiric powers to use.

There will be times when exploration and seduction will only get you so far, and you''ll need to resort to engaging in Vampyr''s dynamic real-time combat. It blends hard-hitting melee combat with ranged shooting mechanics and the supernatural vampire powers. You''ll face many types of enemies: different species of vampires and creatures, as well as vampire hunters who want to hunt you down with their weapons, tools and traps. In Vampyr, your health and the "energy" that drives your supernatural powers are one and same! Using powers will drain your own blood, giving you an edge in battle but also leaving you weaker. You will have to find a way to feed during combat to replenish your strength.

Vampyr also offers a deep crafting system. Find and loot materials and components from the fresh corpses of your victims or during exploration, in order to craft and improve tools, melee and ranged weapons, as well as special ammunitions and coating to exploit the weaknesses of your enemies.

As you cling to what remains of your humanity, your decisions will ultimately shape the fate of your hero while searching for answers in the coughing, foggy aftermath of the Spanish Flu.', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (18980, 'Warhammer 40,000: Dawn of War III', 'In Dawn of War III you will have no choice but to face your foes when a catastrophic weapon is found on the mysterious world of Acheron.

With war raging and the planet under siege by the armies of greedy Ork warlord Gorgutz, ambitious Eldar seer Macha, and mighty Space Marine commander Gabriel Angelos, supremacy must ultimately be suspended for survival.', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (18869, 'Mages of Mystralia', 'Accompany Zia as she strikes off to train and learn to control her magical abilities that had her exiled. Journey across the lands to meet other exiled mages and uncover runes with magical properties that can be combined into millions of different possibilities, letting you come up with completely new and incredible spells!', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (18822, 'Pyre', '"In Pyre, you lead a band of exiles through an ancient competition spread across a vast, mystical purgatory. "', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (18289, 'Möira', 'Möira is an action platformer that draws inspiration from the classic 8-bit portable games with a modern twist, memorable characters, lots of spells and awesome secrets! You play as Rubick, an young magician apprentice who’s searching for his missing master. In order to accomplish that, Rubick must travel through the Magic Kingdoms mimicking enemies’ powers and learning how to combine them using the staff his old master left to him. However, Rubick’s adventure won’t be easy since he’s going to fight against foes so powerful that are capable of distorting the limitations and break the rules of the land of Möira itself.', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (18088, 'Insurgency: Sandstorm', 'Sandstorm is powered by Unreal Engine 4 and will be a major visual and technological upgrade to Insurgency. Although the game is being developed for console, it will stay true to its PC roots by ensuring our dedicated audience has a seamless transition to the new game. Sandstorm retains what makes Insurgency unique and challenging, bringing this experience to a new console audience.', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (19935, 'Wizard of Legend', '"Wizard of Legend is a fast paced 2D dungeon crawler where you assume the role of a powerful wizard on his quest for fame and glory! Combine powerful spells and quick reflexes to overcome all of the challenges and become a Wizard of Legend!"

"Battle your way through each challenge by defeating powerful conjured enemies! Collect valuable spells and relics and build up your magical arsenal to fit your playstyle! Achieve mastery over magic by chaining spells together to create devastating combination attacks! Face each council member in combat and earn your right to become a Wizard of Legend!"', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (22779, 'Death Trash', '"A modern post-apocalyptic role-playing game with action elements and a focus on the narrative and dialogue. A world of cyberpunk, science fiction, horror, the grotesque and trash-talk humor."

"Use realtime combat, dialogue, item crafting and psi powers to explore and survive this post-apocalyptic mystic world with larger-than-life beings.

Choose one character from a diverse selection and customize him or her through stats and making choices. A friend can join anytime for a session of local co-op.

The game is built around respecting your time and freedom."', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (22778, 'Outward', 'Outward is an open world RPG that you play alone or with friends, either online or in splitscreen. It''s more than just an open world RPG you can play coop though: it is an adventurer life simulator. You live the life of an adventurer, from his or her humble beginnings up until retirement. Your life isn''t only about killing monsters and looting: you need to eat, drink and sleep; preparing accordingly before jumping into danger. Everything is meant to feel more real and consequential, while still taking place in a high fantasy universe with magic and exotic creatures.', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (22703, 'Metal Gear Survive', 'In a struggle to stay alive on this harsh terrain filled with deadly creatures, soldiers who were previously on Motherbase now must work cooperatively in order to survive.', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (22384, 'Diabotical', 'A fast-paced multiplayer Arena FPS set in a colourful robot universe developed by former Quake and esport professionals.', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (22356, 'Fishing: Barents Sea', 'Fishing: Barents Sea is a simulator game for PC, about industrial fishing, you will be put in a role as a fishboat captain. Starting out with a small boat you inherited from your grandfather, earn money fishing to buy upgrades, bigger&amp;better boats and more. Make your grandfather proud!

Learn to catch various types of fish with different boats and sets of fishing gear. When inside a port you can visit the bar for hiring crew or get missions, visit docks to repair or buy upgrades for your boat, visit the bank to take a loan or go to the shop to buy supplies and other upgrades. In your homeport you can visit and upgrade your cabin and much more.

You will play in an open world environment over parts of northern Norway, maneuver your boat around the map to find the best fishing spots using your fish sonar, radar and GPS.

The gameplay will be an easy to learn, hard to master experience when using various fishing tools to catch your fish.

Experience an atmospheric and stylish gameworld, with day and night cycle, seasonal effects, advanced resource and weather system. Everything is calculated from real maps, real boats, real fishes inluding age, breeding areas, depth, fish quotas, collectable items and much more.', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (23851, 'Wandersong', 'Wandersong takes you on a whimsical, musical journey across the globe. Eons ago, the goddess Eya put the universe into motion with her music--now, as she does every epoch or so, she''s going to sing a new song and reset existence. You play as a lowly bard tasked with finding the pieces of a mysterious melody called the Earthsong, which is said to be able to preserve the planet. Use your singing to help characters, solve puzzles, and save the world!', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (23499, 'Earth''s Dawn', 'Aiming for a release in Q4 on Xbox One and PlayStation 4, with the PC release soon after. The PlayStation 4 version will have a physical copy available.

"Developed by OneorEight and previously released in Japan under the name Earth Wars, "Earth’s Dawn" fuses frenetic side-scrolling action mechanics with RPG elements and a hard-edged manga aesthetic to deliver a deep and unique gameplay experience.

From the outset Earth’s Dawn gives gamers the freedom to enjoy the game their way – from creating their own character; customization of skill trees to augment abilities best suited to play style; weapon and equipment crafting; alongside a unique narrative-driven branching mission system."', 0, '2015-09-17');
INSERT INTO power_up.games VALUES (23346, 'Of Kings And Men', 'Of Kings And Men is an independently developed third-person multiplayer " Persistent Medieval War" game. Set in 13th century with both European and Eastern influences, you play as a character in a vast open world full of alliances and conflict, with an emphasis on player-driven experiences.', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (23345, 'Life is Feudal: Forest Village', 'LiF:Forest Village is a city builder with survival aspects in a realistic harsh medieval world. Shape, build and expand your settlement, grow various food to prevent your villagers from avitaminosis and starvation. Possess them for additional micromanagement or simply to wander around.', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (23248, 'Frostpunk', 'In a completely frozen world, people develop steam-powered technology to oppose the overwhelming cold. Society in its current form becomes ineffective and it has to change in order to survive.', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (22039, 'The Signal From Tölva', 'The Signal From Tölva is a single-player, first-person, combat and exploration game.', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (21063, 'Project Sonic 2017', '', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (4843, 'Kingdom Come: Deliverance', 'A first-person, open world, realistic RPG that will take you to the Medieval Europe in a time of upheaval and strife

A humble, young blacksmith loses everything to war. As he tries to fulfill the dying wish of his father, Fate drags him into the thick of a conspiracy to save a kidnapped king and stop a bloody conflict. You will wander the world, fighting as a knight, lurking in the shadows as a rogue, or using the bard’s charm to persuade people to your cause. You will dive deep into a sweeping, epic, nonlinear story from Daniel Vávra, an award-winning designer from the Mafia series. Our unique, first-person combat system lets you wield sword or bow in both one-on-one skirmishes and large-scale battles. All of this – and more – brought to life beautifully with next-gen visuals delivered via CryEngine 3.', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (10757, 'Police 1013', 'The aim of Police 1013 is to bring the good guys into focus. This game isn’t for cowboys. It’s not about how many kills you get or how big the explosion is. It’s also not about the Police being mere cannon fodder as is the case with just about all games on the market. It’s about being an effective Police Officer in a City like most cities around the World. It’s about doing things the right way. It’s about showing how difficult it is but also how rewarding it is to be a good Police Officer.

We want to continue to honor the work Police Officers do. This game brings with it the opportunity to feel what it’s like and to experience what Police experience. Our mission is to show Police for what we believe they are; real people doing a real job. On that Journey there will also be massive highs for our players to really enjoy the experience.

When ever you ask a Police Officer about their job, they say "I love what I do, I love being a Police officer". This game needs to have the same effect. We want Police fans to LOVE jumping into their patrol car and to experience a whole new open world whilst waiting for thousands of random events to pop up at a moment’s notice.', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (10403, 'Raid: World War II', '', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (10232, 'Thimbleweed Park', 'Lost along a dusty stretch of highway, the town of Thimbleweed Park once boasted an opulent hotel, a vibrant business district and the state’s largest pillow factory, but now it teeters on the edge of oblivion and continues to exist for no real reason. It’s a town that makes you itch and your skin crawl. It’s a place no one ever looks for, but everyone seems to find.

Thimbleweed Park is the curious story of two washed up detectives investigating a dead body found in the river just outside of town. It’s a game where you switch between five playable characters while uncovering the dark, satirical and bizarre world of Thimbleweed Park.', 0, '2017-03-30');
INSERT INTO power_up.games VALUES (11806, 'Dark Cloud 3', 'The long-awaited sequel to Dark Cloud 2, rumored to be under development by Level-5.', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (11590, 'The Surge', 'The Surge is a sci-fi spin on Dark Souls, from the Lords of the Fallen team.

Set in a heavily dystopian future as Earth nears the end of its life, those who remain in the overpopulated cities must work to survive as social programs have become saturated by an aging population and increasing environmental diseases. The world of The Surge offers a very grim vision of the future, where the evolution of our technology, our society and our relation with the environment led to a decadent state of the Human civilization.

"Fitted with an advanced CREO exo-suit, balancing your core power against your implants and exo-suit upgrades will allow you to specialize in a huge variety of combat styles, thanks to the almost limitless combinations of addons and dozens of make-shift weapons salvaged from the industrial complex. Finely tune your gear to match your playstyle, and to best suit the challenge ahead. The Metroidvanian levels open up as your core power increases, allowing you to over-charge doors previously too strong for your suit to damage and opening new pathways for you to explore. The CREO facility is vast, and if you want to find and craft the best equipment and weapons, you will need to search every area for secrets and hidden items.

The Surge features innovative combat mechanics, allowing you to target individual limbs and dismember the exo-suit add-on or weapon you want to acquire. Collecting tech-scrap and blueprints, you will be able to return to the Operation Center to level up and craft new exo-suit addons and weapons salvaged throughout the level. The most powerful of these can be acquired from the world’s gigantic, industrial bosses. Most of what you encounter in The Surge was never originally designed to kill you, and most of the items you loot and craft were never meant for combat. However, the combined strength of the exo-suit and near-future technology of CREO mean that anything from fork-lifting addons to laser cutters can be used as lethal weapons for diving deeper into the facility."', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (11296, 'Allison Road', 'Allison Road is a survival horror game developed in Unreal Engine 4, played in first person view with optional Oculus Rift support. 

You will take on the role of the unnamed protagonist who wakes up one day without any recollection of prior events. Over the course of five nights It is your objective to uncover the whereabouts of your family, unravel the mysteries of the house, and face off against Lily and other dark entities that are nested deep within the house, while the clock is relentlessly ticking towards 3:00am. 

What would you do if you could feel something stalking you in the dark in the safety of your own home? 
If you couldn''t tell what''s real and what''s not? 

Allison Road combines old-school survival horror and adventure game mechanics with next-gen graphics and optional VR support. 

The game was initially developed by one person but then the team slowly grew to its current size of 6 people.', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (11208, 'NieR: Automata', 'NieR: Automata is an upcoming action role-playing game developed by PlatinumGames and published by Square Enix for the PlayStation 4. 

The game is set in the same universe as Nier, a spin-off of the Drakengard series.The story revolves around the main protagonist Nier as he searches for a cure for his daughter who is infected with the Black Scrawl virus, a seemingly incurable disease.', 0, '2017-06-29');
INSERT INTO power_up.games VALUES (11180, 'Robinson: The Journey', 'Go beyond boundaries in Robinson: The Journey, a brand new virtual reality game from Crytek. Harnessing the power of CRYENGINE, Robinson: The Journey will offer players an unparalleled sense of presence in a game world as they assume the role of a young boy who has crash-landed on a mysterious planet. With freedom to explore their surroundings in 360 degrees of detail, players will become pioneers by interacting with the rich ecosystem around them and unearthing incredible secrets at every turn.', 0, '2016-12-30');
INSERT INTO power_up.games VALUES (11177, 'Gang Beasts', 'Gang Beasts is a silly local multiplayer party game with surly gelatinous characters, brutal mêlée fight sequences, and absurdly hazardous environments.', 0, '2014-08-28');
INSERT INTO power_up.games VALUES (11169, 'Final Fantasy VII', '', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (11136, 'Ion', 'From the creator of "DayZ" and inspired by the cult favourite "Space Station 13"; "ION" is an emergent narrative massively-multiplayer online game in which players will build, live in and inevitably die in huge floating galactic constructions as humanity makes its first steps colonizing the universe. Technology from Improbable allows ION to have a massive interconnected universe with fully simulated environments such as power grids, air pressure and heat; all to help stave off the unending vacuum of space.', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (1877, 'Cyberpunk 2077', 'The upcoming RPG from CD Projekt RED, creators of The Witcher series of games, based on the Cyberpunk 2020 tabletop RPG created by Mike Pondsmith.', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (1595, 'Star Citizen', 'Star Citizen is an upcoming space trading and combat simulator video game for Microsoft Windows. Star Citizen will consist of two main components: first person space combat and trading in a massively multiplayer persistent universe and customizable private servers (known as Star Citizen), and a branching single-player and drop-in co-operative multiplayer campaign (known as Squadron 42). The game will feature Oculus Rift support.', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (7631, 'Overkill’s the Walking Dead', 'OVERKILL’s The Walking Dead is a co-op first person shooter with elements of action, role-playing, survival horror and stealth, that invites players to explore the hugely popular The Walking Dead universe, where they will play the role of survivors fending for themselves in a post-apocalyptic world dominated by flesh-eating walkers. In 2016 Washington will fall – what will you do?', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (7621, 'Rime', 'Rime is an upcoming open world, third-person view, adventure and puzzle video game being developed by Tequila Works for the PlayStation 4', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (7346, 'The Legend of Zelda: Breath of the Wild', '"Step into a world of discovery, exploration and adventure in The Legend of Zelda: Breath of the Wild, a boundary-breaking new game in the acclaimed series. Travel across fields, through forests and to mountain peaks as you discover what has become of the ruined kingdom of Hyrule in this stunning open-air adventure."', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (7345, 'Scalebound', 'In Scalebound, the next great adventure from critically-acclaimed developer PlatinumGames and renowned Game Director Hideki Kamiya, you will experience the journey of a young loner, Drew, who is pulled into the strange, beautiful and dangerous world of Draconis. There Drew is bonded to a fearsome and noble dragon, Thuban, the last of his kind. These two, lone-wolf heroes thrown together by fate must learn to fight as one to defeat the powerful enemies that threaten Draconis, Earth and a vast universe of parallel worlds.', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (7333, 'Crackdown 3', 'Stop crime as a super-powered Agent of justice in Crackdown 3''s hyper-powered sandbox of mayhem and destruction. Explore the heights of a futuristic city, race through the streets in a transforming vehicle, and use your powerful abilities to stop a ruthless criminal empire. Developed by original creator Dave Jones, Crackdown 3 delivers cooperative mayhem and an all-new multiplayer mode where destruction is your ultimate weapon.', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (17583, 'SpellForce 3', '"SpellForce 3 - The perfect blend between RTS and RPG!

SpellForce 3 goes back to the roots of the SpellForce saga. The story takes place before the acclaimed SpellForce: The Order of Dawn and the players becomes a part of a rich high fantasy world named Eo.

Features:
- RTS/RPG Mix: Unique gameplay combining RTS and Top Down RPG genres
- Create &amp; Customize: Create your own hero and develop his skillset as you prefer!
- Build to Fight: Build your own army and fight epic mass battles with it.
- Epic Storyline: Unravel a deep and epic story in the SpellForce universe that doesn''t require having played any of the former SpellForce games. Fans will still find a lot of interesting connections.
- Rich universe &amp; lore: Explore the world Eo and discover intrigues, secrets and a lot of loot for your hero.
- Apply some tactics: Use advance tactics with your RTS army to defeat your enemies
- Play on your own: Long Single Player Campaign of 30+ hours
- Multiplayer: Strong multiplayer component with various modes - Play cooperatively or compete against each other "', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (17480, 'Eastshade', 'Eastshade is a first-person, open world exploration game. 

You are a traveling painter, exploring the island of Eastshade. Capture the world on canvas using your artist’s easel. Talk to the inhabitants to learn about their lives. Make friends and help those in need. Discover mysteries and uncover secrets about the land. Surmount natural impasses to reach forgotten places. Experience how your actions impact the world around you.', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (17229, 'H-Hour: World''s Elite', 'H-Hour: World''s Elite™ (H-Hour) is a tactical, team-based military shooter in which cooperation among players is required for success. The game uses a third person perspective to enhance the player''s situational awareness.  Players can see where they are in relation to the world.  This battlefield perspective allows for more strategic game play.', 0, '2015-04-01');
INSERT INTO power_up.games VALUES (13555, 'Indivisible', 'Indivisible is a side-scrolling RPG in the vein of Valkyrie Profile, spanning a huge fantasy world inspired by our own world’s various cultures and mythologies.

Players will navigate beautiful environments using a variety of different traversal abilities, and engage monsters in fast-paced combat.', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (14760, 'Death''s Gambit', 'Death''s Gambit is a challenging Action-RPG where you explore an alien medieval planet filled with beasts, knights, and horrors. 

Climb big creatures that will test your resolve. Fight dangerous monsters that will make you think outside the box.', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (14363, 'Gran Turismo Sport', 'Welcome to the future of motorsports – the definitive motor racing experience is back and better than ever only on PlayStation 4.

Gran Turismo Sport is the world’s first racing experience to be built from the ground up to bring global, online competitions sanctioned by the highest governing body of international motorsports, the FIA (Federation International Automobile). Create your legacy as you represent and compete for your home country or favorite manufacturer.', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (14172, 'Everspace', 'EVERSPACE is an action-focused single-player space shooter with rock solid 6DOF controls combining roguelike elements with top-notch visuals and a captivating non-linear story as well as VR support in the final version.', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (20150, 'Katana ZERO', 'Katana ZERO is a fast paced neo-noir action platformer, focusing on tight, instant-death acrobatic combat, and a dark 80''s neon aesthetic. Aided with your trusty katana, the time manipulation drug Chronos and the rest of your assassin''s arsenal, fight your way through a fractured city, and take back what''s rightfully yours.', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (20909, 'The First Tree', '', 0, '2017-12-30');
INSERT INTO power_up.games VALUES (18253, 'Blue Angelo', 'A new sci-fi fantasy action RPG game tells the story of a genetically engineered creature able to eliminate all forms of demons.', 0, '2017-11-30');
INSERT INTO power_up.games VALUES (11542, 'Shenmue III', 'Shenmue III is an upcoming action-adventure video game and the sequel to the 2001 game Shenmue II. It follows teenage martial artist Ryo Hazuki in his quest to find his father''s killer in 1980s China. In 2015, Suzuki launched a Kickstarter crowdfunding campaign to help fund Shenmue III and the campaign met its initial target of $2 million USD in under eight hours. The campaign ended in July having raised over $6 million, becoming the most funded video game in Kickstarter history.', 0, '2017-11-30');
INSERT INTO power_up.games VALUES (18774, 'Keeper And The Soldier', 'A 2.5D stealth puzzle platformer from Jumpbutton Studio about a young girl who stumbles across something beautiful in a world ravaged by never-ending war. Keeper and the Soldier has elements of role playing and point-n-click adventure games with an emphasis on narrative, atmosphere and stealth. 

The game is set in and around a dystopian city that lies on the border of a lush forest which protects a long forgotten secret. An ongoing war at the forefront of society has twisted the citizens of the city, mentally and physically. People here are born to serve in a disheartening, gloomy existence. Like ants, it is simply their way of life to either be a worker or a soldier. 

The player controls EMI23 (or Emi for short), a young girl who is destined to be a soldier. She bonds with a curious creature called the Keeper in the outer forest who needs her help. Emi must recover pieces of an ancient device to save the archives of human history before the Keeper''s exhausted life comes to an end. 

Time will tell if her actions bring the dawn of a new era or reveal the most dangerous thing of all, the truth.

KEY FEATURES 
-Explore a rich and beautiful immersive world with a forgotten past and collect clues to its origins. 

-Stealth through guarded areas and outwit your foes while wielding powerful abilities and using the environment to your advantage. 

-Experience a touching tale of companionship told through two characters with no one left to turn to but each other. 

Keeper and the Soldier has elements of role playing and point-n-click adventure games with an emphasis on narrative, atmosphere and stealth.', 0, '2017-09-29');
INSERT INTO power_up.games VALUES (23690, 'No Place For Bravery', '"No Place for Bravery is an atmospheric minimalist roguelike where you play as a group of adventurers wandering through an eerie, devastated high fantasy world on a quest to slay the godlike entities that brought down humankind. 

It is basically an action RPG with super neat, Hyper Light Drifter inspired pixel art, minimalist controls, procedurally generated maps and a time manipulation mechanic similar to Super Time Force''s that lets the player control more than one character simultaneously by playing with one character and then rewinding time and playing with another character alongside the first one: not only the player has to plan every move, he also has to synchronize everything. 

Bravery is, like many other roguelikes, filled with unique random events. The idea is to use these events to craft the game’s mythology. The game doesn''t use regular exposition to tell it''s story, rather, the player will absorb the nuances of Bravery’s narrative by exploring its gorgeous environment, experiencing the aesthetics of its mechanics or by simply making sense of the vast array of mysterious random events. 

Also, the game gets rid of all the grinding: Skinner Box mechanics and ''''artificial'''' stats-based progression have no place here: I''m talking about a game with absolutely no numbers and very, very few written words. 

No Place for Bravery is an extremely hard and completely skill and strategy based game. The world is to be relentless, and the player feels the mediocrity before such a world in each step that is taken."', 0, '2017-09-29');
INSERT INTO power_up.games VALUES (21961, 'FOX n FORESTS', '"FOX n FORESTS is a 2D 16-Bit style action platformer with adventure and puzzle elements, empowering you to switch seasons on the fly!

Set within a mystic forest and its surroundings, FOX n FORESTS is a stunning fable offering pixel perfect Retrotainment and rewarding challenges!

The game is inspired by the glorious days of 16-Bit and all time classics like Super Ghouls ’n Ghosts, Wonder Boy in Monster World and ActRaiser 2 as well as the Castlevania and Zelda series.

You play as Rick the Fox and your weapon is a magic melee crossbow that can not only be used for ranged and close up combat, but empowers you to seamlessly switch between 2 seasons per level, completely changing the whole environment and resulting in amazing secrets, tricky skill tests and thriving exploration.

Pixel perfect platforming, screen filling bosses, rich bonus levels, powerful potions, bags of loot, stuffed shops, clever backtracking, challenging skill tests, a magic melee crossbow to shoot and slash enemies, upgradeable shots, not 4 but 5 seasons to explore, stunning 16-Bit pixel art, a catchy chip tunes soundtrack and tons of humorous retro charm combined with the luxuries of modern game design will make FOX n FORESTS a worthy revival of games from the Golden Age.
 
Easy to learn, difficult to master, impossible to not fall in love with!"', 0, '2017-08-31');
INSERT INTO power_up.games VALUES (8259, 'Tacoma', 'Discovery. Exploration. Isolation. What is life like 200,000 miles from Earth? Uncover the mysteries held by Lunar Transfer Station Tacoma and its crew, living at the edge of humanity''s reach. Tacoma, the next game from the creators of "Gone Home."', 0, '2017-06-29');
INSERT INTO power_up.games VALUES (9758, 'Luckless Seven', 'Luckless Seven is an isometric card game RPG with advanced lighting and 3D visuals. The game takes place in a fantasy near-future setting and features extensive dialogue trees, story-driven side quests, and a card battle system inspired by Pazaak.

Features:

- Immersive dialogue trees
- Card battle system
- Social leveling system
- Explorable 3D world
- Dozens of collectible cards
- Long storyline with multiple narratives

For more info, please visit www.deckpointstudio.com

To get regular updates, like our Facebook or follow us on Twitter', 0, '2017-06-29');
INSERT INTO power_up.games VALUES (9174, 'Little Nightmares', 'Little Nightmares is an adventure-suspense game about a girl named Six and her attempts to escape the strange world of The Maw.', 0, '2017-06-29');
INSERT INTO power_up.games VALUES (9046, 'Chroma Squad', 'Chroma Squad is a tactical RPG about five stunt actors who decide to quit their jobs and start their own Power Rangers-inspired TV show! Cast actors, purchase equipment and upgrades for your studio, craft weapons and giant Mechas out of cardboard and duct tape.', 0, '2015-04-29');
INSERT INTO power_up.games VALUES (22554, 'Potions: A Curious Tale', 'Potions: A Curious Tale is an adventure-crafting game where your wits are your greatest weapons and combat is not always the answer. Encounter fairy tale characters and stories from around the world as you play as Luna, a young witch who overcomes obstacles with the potions she brews.

------

Growing up is hard, and it’s even harder when you’re a witch! As Luna, you’ll solve devious puzzles and battle monsters with your arsenal of mystical potions. With a little help from your grandmother (a powerful witch in her own right) and Helios, your sharp-tongued cat familiar, you’ll explore enchanted lands inspired by your favorite fairy tales – often with an unexpected twist! 

As powerful as your potions are, you’ll find yourself up against monsters that can shrug off your most powerful attacks. You’ll have to think on your feet and act quickly to sidestep danger. Manage your resources carefully, or you may find yourself with an empty satchel when you need your elixirs most.

Features:
- Conflict resolution that goes beyond pure combat—calm, charm, tame, and scare your enemies
- Strategic combat—use potions, the environment, and even other monsters to defeat your foes
- Quirky storyline featuring rich characters from folklore from around the world—all with a unique spin
- Crafting system that rewards experimentation
- Countless environments to explore as you search for rare ingredients
- Resource management that rewards a varied play style, with a vast number of potions and ingredients
- Luna, your resident witch-in-training and strong female lead
Beautiful illustrated art style reminiscent of your favorite storybooks and animated tales', 0, '2017-06-29');
INSERT INTO power_up.games VALUES (21062, 'Sonic Mania', '"It''s the ultimate Sonic celebration! Sonic returns in a new 2D platforming high speed adventure, and he''s not alone!

Developed in collaboration between SEGA, Christian Whitehead, Headcannon, and PagodaWest Games, experience new zones and remixed classic levels with Sonic, Tails, and Knuckles!"', 0, '2017-06-29');
INSERT INTO power_up.games VALUES (7350, 'Get Even', '', 0, '2017-06-29');
INSERT INTO power_up.games VALUES (15708, 'Red Ash: The Indelible Legend', 'Red Ash: The Indelible Legend is an upcoming video game in development by Comcept and HYDE, Inc. The game, based on a concept by Keiji Inafune, is heavily influenced by the Mega Man Legends series by Capcom, which many of the Comcept staff had worked on previously', 0, '2017-05-31');
INSERT INTO power_up.games VALUES (19542, 'Oxygen Not Included', '"Oxygen Not Included is a space-colony simulation game. Manage your colonists and help them dig, build and maintain a subterranean asteroid base.

You''ll need water, warmth, food, and oxygen to keep them alive, and even more than that to keep them happy."', 0, '2017-05-08');
INSERT INTO power_up.games VALUES (17446, 'Battalion 1944', 'Battalion 1944 recaptures the core of classic multiplayer shooters and propels WW2 into the next generation. Battalion 1944 utilizes the most advanced industry technology to create a visceral and heart-thumping multiplayer experience that has been crafted by the designers who have grown up playing Medal of Honor and Call of Duty 2. 

Fight in real world locations such as the streets of Carentan, the forests of Bastogne and many more in our spiritual successor to the great multiplayer shooters of the past. Precise aim with your Kar98, covering fire with your M1 Garand and quick thinking with your Thompson are key to your success, all packaged into a competitive multiplayer environment. In short, Battalion 1944 is an infantry based first person shooter with an emphasis on raw skill. No grinding, no ''exosuits'', just you and your skill as a player. 

Join a Battalion and compete season to season with BattleRank, our global competition system. Contribute to your Battalion''s season objective to earn cosmetic rewards for your character, weapons and accessories within our realistic art style. Flaunt your success through your player profile to become a renowned player within the community. BattleRank’s highly tuned stat tracking system enables Battalion 1944 to go beyond the standard mould of shooters gone by and enables players to compete globally for rewards, glory and honour. 

Fuelled by the power of Unreal Engine 4, Battalion 1944 resurges World War 2 back into the modern gaming spotlight in our spiritual successor to the great multiplayer shooters of the past such as Call of Duty 2 and Enemy Territory. On BattleRank, it’s down to skilled soldiers like you to fight for your battalion.', 0, '2017-04-30');
INSERT INTO power_up.games VALUES (18280, 'Them''s Fightin'' Herds', 'Them’s Fightin’ Herds is a 2D Fighting game with adorable animals in an original universe designed by Lauren Faust.', 0, '2017-03-31');
INSERT INTO power_up.games VALUES (13196, 'Tears of Avia', 'Tears of Avia is a turn-based tactical RPG. Play with up to 5 classes and hundreds of skills, finding the best synergy with your party and their skill loadout will mean the difference between success and failure. Run a balanced party or roll nothing but warriors, the choice is yours. With some skills being weapon bound rather than class bound, there are endless possibilities for you to experiment from.', 0, '2017-03-31');
INSERT INTO power_up.games VALUES (10031, 'Yooka-Laylee', 'Yooka-Laylee is an all-new 3D platformer from the creative talent behind the Banjo-Kazooie and Donkey Kong Country games. We’ve come together to form Playtonic Games and create a spiritual successor to our most cherished work from the past!

Our new heroes, Yooka (the green one) and Laylee (the wisecracking bat with big nose) were conceptualised from the ground up for stellar platforming gameplay, created by the same character artist behind the rebooted Donkey Kong family and legendary N64 heroes Banjo and Kazooie.

Using an arsenal of special moves like Yooka’s tongue grapple and Laylee’s tactical sonar blast, players will explore – and expand – gorgeous 3D worlds drawn up by esteemed environment artist Steven Hurst (Banjo-Kazooie series, Viva Pinata) and discover the plethora of delicious collectibles hidden within.', 0, '2017-03-30');
INSERT INTO power_up.games VALUES (19617, 'Fallen Legion', '', 0, '2017-03-30');
INSERT INTO power_up.games VALUES (19520, 'FAR: Lone Sails', 'FAR is a vehicle adventure game. The player needs to maintain and upgrade his unique vessel to traverse a dried out sea.', 0, '2017-03-30');
INSERT INTO power_up.games VALUES (9062, 'Strafe', 'Strafe is a singleplayer 3D action experience where the player can pick up a gun and shoot hordes of things in the face.  The developers have created technology that changes the levels every time you play for endless replayability. There are billions of experiences to be had with crazy secrets to find! We give you the levels, you paint them red.', 0, '2017-03-30');
INSERT INTO power_up.games VALUES (18932, 'In the Shadows', 'In The Shadows is a puzzle platformer game where you use lights to scare away shadows creatures that transform into everyday objects you can use to your own advantage. Confront your fears, solve puzzles, find secrets.', 0, '2016-12-30');
INSERT INTO power_up.games VALUES (18930, 'Tokyo 42', 'Set in a future Tokyo, the game will see you become an assassin and uncover a dark conspiracy that will affect everyone.', 0, '2017-03-30');
INSERT INTO power_up.games VALUES (18645, 'Hand of Fate 2', 'Hand of Fate 2 brings a host of new mechanics to the table, and improves on every element of it''s hit predecessor. Companions will fight at your side, new challenges will test your skills, and a swathe of new opponents will fight against you.', 0, '2017-03-30');
INSERT INTO power_up.games VALUES (18279, 'BattleTech', 'BattleTech is a turn-based tactical ''Mech combat set in the classic 3025 era of the BattleTech Universe. From the creators of the Shadowrun Series!', 0, '2017-03-30');
INSERT INTO power_up.games VALUES (18113, 'The Church in the Darkness', 'From Richard Rouse III (The Suffering) and inspired by real-life events, The Church in the Darkness is an action-infiltration game that delves into the radical movements of the 1970s. As a former law enforcement officer, you have resolved to get into Freedom Town and check on your sister’s son. But life in Freedom Town may not be what it seems.', 0, '2017-03-30');
INSERT INTO power_up.games VALUES (22783, 'The Sexy Brutale', '"The Sexy Brutale" is the Marquis’ renowned grand casino and residence. He is well known for his highly anticipated annual masked ball and his eccentric taste in friends. But something truly evil has found its way into the heart of The Sexy Brutale during this year’s party, turning the ball into a nightmare where the guests are being murdered with no hope for escape.

As the story unfolds in real-time, the player must find answers to every enigma and end the loop, saving all the guests’ lives.', 0, '2017-03-30');
INSERT INTO power_up.games VALUES (23212, 'Aven Colony', 'Aven Colony is a city-building and management sim that tells the story of humanity’s first settlement of an extrasolar world. Land on exotic Aven Prime, where you must construct and maintain the infrastructure and ensure the well-being of your citizens, all while dealing with the often harsh realities of an exotic alien world.

On top of this, you’ll face the greatest challenge of all — keeping your people happy. How will you feed your people? Will you be able to provide them with enough jobs, entertainment, retail outlets, and other services while protecting them from the planet’s many dangers? What social policies will you enact to influence your people? The future of the colony rests on your decisions.', 0, '2017-03-30');

INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (18915, 14, '2018-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (18915, 6, '2018-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (7204, 46, '2018-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (7204, 48, '2018-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (7204, 39, '2018-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (7204, 34, '2018-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (7204, 14, '2018-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (7204, 3, '2018-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (7204, 6, '2018-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (17244, 14, '2018-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (17244, 6, '2018-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (17244, 3, '2018-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (17244, 48, '2018-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (17244, 49, '2018-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (14741, 6, '2018-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (14741, 3, '2018-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (14741, 14, '2018-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (14741, 48, '2018-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (14741, 49, '2018-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (14741, 92, '2018-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (18375, 48, '2018-03-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (18375, 6, '2018-03-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (18375, 49, '2018-03-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (6071, 6, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (9598, 6, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (9176, 48, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (9176, 49, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (9176, 6, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (19831, 3, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (19831, 14, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (19831, 6, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (19817, 6, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (19729, 49, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (19729, 48, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (19729, 14, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (19729, 6, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (19597, 6, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (19596, 37, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (19541, 6, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (19541, 49, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (19531, 48, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (19531, 49, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (19531, 6, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (19530, 6, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (19470, 49, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (19470, 48, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (19455, 48, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (19453, 6, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (19453, 48, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (19453, 49, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (19448, 6, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (19440, 6, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (19440, 49, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (19440, 48, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (19408, 6, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (19297, 48, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (19297, 49, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (19297, 39, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (19297, 3, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (19297, 14, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (19297, 6, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (6879, 48, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (6879, 49, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (6879, 6, '2016-10-31') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (6705, 14, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (6705, 6, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (8729, 48, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (8729, 49, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (8729, 6, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (18980, 6, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (18869, 6, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (18822, 48, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (18822, 6, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (18289, 6, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (18289, 14, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (18289, 3, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (18088, 3, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (18088, 14, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (18088, 6, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (18088, 49, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (18088, 48, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (19935, 3, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (19935, 14, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (19935, 6, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (22779, 3, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (22779, 14, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (22779, 6, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (22778, 49, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (22778, 6, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (22703, 6, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (22703, 49, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (22703, 48, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (22384, 6, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (22356, 6, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (23851, 6, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (23499, 6, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (23499, 48, '2016-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (23499, 48, '2015-09-17') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (23499, 49, '2016-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (23499, 49, '2015-09-17') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (23346, 6, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (23345, 6, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (23248, 6, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (22039, 14, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (22039, 6, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (21063, 6, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (21063, 130, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (21063, 49, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (21063, 48, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (4843, 14, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (4843, 3, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (4843, 6, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (4843, 48, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (4843, 49, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (10757, 48, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (10757, 49, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (10757, 6, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (10403, 6, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (10232, 49, '2017-03-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (10232, 34, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (10232, 39, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (10232, 14, '2017-03-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (10232, 3, '2017-03-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (10232, 6, '2017-03-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (11806, 48, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (11590, 48, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (11590, 49, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (11590, 6, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (11296, 6, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (11208, 6, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (11208, 48, '2017-06-29') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (11180, 6, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (11180, 48, '2016-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (11177, 3, '2014-08-28') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (11177, 14, '2014-08-28') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (11177, 48, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (11177, 6, '2014-08-28') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (11169, 48, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (11136, 49, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (1877, 48, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (1877, 49, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (1877, 6, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (1595, 6, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (1595, 3, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (7631, 48, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (7631, 49, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (7631, 6, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (7621, 48, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (7346, 41, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (7345, 49, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (7345, 49, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (7345, 49, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (7333, 49, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (17583, 6, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (17480, 6, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (17229, 48, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (17229, 6, '2015-04-01') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (13555, 48, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (13555, 49, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (13555, 3, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (13555, 14, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (13555, 6, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (14760, 14, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (14760, 48, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (14760, 6, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (14363, 48, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (14363, 48, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (14363, 48, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (14363, 48, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (14172, 49, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (14172, 14, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (14172, 6, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (20150, 14, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (20150, 6, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (20909, 6, '2017-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (18253, 6, '2017-11-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (11542, 6, '2017-11-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (11542, 48, '2017-11-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (18774, 92, '2017-09-29') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (23690, 14, '2017-09-29') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (23690, 6, '2017-09-29') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (21961, 3, '2017-08-31') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (21961, 14, '2017-08-31') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (21961, 6, '2017-08-31') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (8259, 49, '2017-06-29') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (8259, 3, '2017-06-29') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (8259, 14, '2017-06-29') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (8259, 6, '2017-06-29') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (9758, 6, '2017-06-29') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (9174, 48, '2017-06-29') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (9174, 49, '2017-06-29') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (9174, 6, '2017-06-29') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (9046, 46, '2017-06-29') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (9046, 49, '2017-06-29') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (9046, 48, '2017-06-29') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (9046, 6, '2015-04-29') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (9046, 3, '2015-04-29') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (9046, 14, '2015-04-29') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (22554, 3, '2017-06-29') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (22554, 14, '2017-06-29') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (22554, 6, '2017-06-29') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (21062, 49, '2017-06-29') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (21062, 48, '2017-06-29') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (21062, 6, '2017-06-29') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (7350, 6, '2017-06-29') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (7350, 48, '2017-06-29') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (7350, 49, '2017-06-29') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (15708, 6, '2017-05-31') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (15708, 48, '2017-05-31') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (15708, 49, '2017-05-31') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (19542, 6, '2017-05-08') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (17446, 48, '2017-04-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (17446, 49, '2017-04-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (17446, 6, '2017-04-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (18280, 6, '2017-03-31') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (18280, 14, '2017-03-31') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (18280, 3, '2017-03-31') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (13196, 6, '2017-03-31') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (13196, 14, '2017-03-31') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (10031, 6, '2017-03-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (10031, 49, '2017-03-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (10031, 48, '2017-03-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (10031, 41, '2017-03-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (10031, 3, '2017-03-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (10031, 14, '2017-03-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (19617, 46, '2017-03-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (19617, 48, '2017-03-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (19520, 49, '2017-03-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (19520, 6, '2017-03-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (9062, 92, '2017-03-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (9062, 3, '2017-03-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (9062, 6, '2017-03-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (9062, 14, '2017-03-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (18932, 6, '2016-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (18932, 14, '2016-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (18932, 3, '2016-12-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (18932, 49, '2017-03-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (18932, 48, '2017-03-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (18930, 48, '2017-03-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (18930, 49, '2017-03-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (18930, 6, '2017-03-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (18645, 49, '2017-03-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (18645, 3, '2017-03-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (18645, 14, '2017-03-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (18645, 6, '2017-03-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (18279, 6, '2017-03-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (18279, 14, '2017-03-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (18279, 3, '2017-03-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (18279, 92, '2017-03-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (18113, 49, '2017-03-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (18113, 48, '2017-03-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (18113, 14, '2017-03-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (18113, 6, '2017-03-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (22783, 49, '2017-03-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (22783, 6, '2017-03-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (22783, 48, '2017-03-30') ON CONFLICT DO NOTHING;
INSERT INTO power_up.game_consoles (game_id, console_id, release_date) VALUES (23212, 6, '2017-03-30') ON CONFLICT DO NOTHING;

COMMIT;

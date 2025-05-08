package com.ojtapp.mobile.repositories.local

import com.ojtapp.mobile.model.FileEntry
import com.ojtapp.mobile.model.FileResponse
import com.ojtapp.mobile.model.GiaRecord
import com.ojtapp.mobile.model.SetupRecord

object Dummy {
    val fileResponses = listOf(
        FileResponse(
            path = "",
            files = listOf(
                FileEntry("documents", true),
                FileEntry("downloads", true),
                FileEntry("readme.txt", false),
                FileEntry("trash", true),
                FileEntry("secret", true)
            )
        ),
        FileResponse(
            path = "/documents",
            files = listOf(
                FileEntry("taxes_2023", true),
                FileEntry("resume.pdf", false),
                FileEntry("project_ideas", true)
            )
        ),
        FileResponse(
            path = "/documents/taxes_2023",
            files = listOf(
                FileEntry("form_a.pdf", false),
                FileEntry("form_b.pdf", false),
                FileEntry("notes.txt", false),
                FileEntry("i_will_cry.xlsx", false)
            )
        ),
        FileResponse(
            path = "/documents/project_ideas",
            files = listOf(
                FileEntry("meme_generator.md", false),
                FileEntry("world_domination.plan", false)
            )
        ),
        FileResponse(
            path = "/downloads",
            files = listOf(
                FileEntry("music", true),
                FileEntry("games", true),
                FileEntry("virus_bait.exe", false),
                FileEntry("tax_evasion_software.zip", false)
            )
        ),
        FileResponse(
            path = "/downloads/music",
            files = listOf(
                FileEntry("metallica.mp3", false),
                FileEntry("karaoke_hits", true),
                FileEntry("baby_shark.mp3", false),
                FileEntry("cringe_playlist", true)
            )
        ),
        FileResponse(
            path = "/downloads/music/karaoke_hits",
            files = listOf(
                FileEntry("my_heart_will_go_on.mp3", false),
                FileEntry("bohemian_rhapsody.mp3", false),
                FileEntry("bakit_pa.mp3", false)
            )
        ),
        FileResponse(
            path = "/downloads/music/cringe_playlist",
            files = listOf(
                FileEntry("friday_rebecca_black.mp3", false),
                FileEntry("ppap_pen_pineapple_apple_pen.mp3", false)
            )
        ),
        FileResponse(
            path = "/downloads/games",
            files = listOf(
                FileEntry("doom", true),
                FileEntry("tetris.exe", false),
                FileEntry("minesweeper_legacy", true)
            )
        ),
        FileResponse(
            path = "/downloads/games/doom",
            files = listOf(
                FileEntry("doom_launcher.sh", false),
                FileEntry("hell_assets", true),
                FileEntry("satanic_settings.cfg", false)
            )
        ),
        FileResponse(
            path = "/downloads/games/doom/hell_assets",
            files = listOf(
                FileEntry("demons.txt", false),
                FileEntry("flames.png", false),
                FileEntry("bg_music.mp3", false)
            )
        ),
        FileResponse(
            path = "/downloads/games/minesweeper_legacy",
            files = listOf(
                FileEntry("mines.classic", false),
                FileEntry("boom.wav", false)
            )
        ),
        FileResponse(
            path = "/downloads/memes_dump",
            files = listOf(
                FileEntry("grumpy_cat.jpg", false),
                FileEntry("shrek_is_love.png", false),
                FileEntry("distracted_boyfriend.jpeg", false),
                FileEntry("this_is_fine.gif", false),
                FileEntry("cat_jam.mp4", false),
                FileEntry("rickroll.mp4", false),
                FileEntry("pepe_sad.png", false),
                FileEntry("elon_musk_smoking.jpg", false),
                FileEntry("doge_coin_to_the_moon.gif", false),
                FileEntry("bonk.jpg", false),
                FileEntry("woman_yelling_at_cat.jpg", false),
                FileEntry("giga_chad.png", false),
                FileEntry("baby_yoda.jpg", false),
                FileEntry("sigma_grindset.png", false),
                FileEntry("shiba_dance.gif", false),
                FileEntry("among_us_sus.mp4", false),
                FileEntry("is_this_a_pigeon.png", false),
                FileEntry("crying_jordan.jpg", false),
                FileEntry("spongebob_mocking.jpg", false),
                FileEntry("meme_template1.png", false),
                FileEntry("meme_template2.png", false),
                FileEntry("meme_template3.png", false),
                FileEntry("meme_template4.png", false),
                FileEntry("meme_template5.png", false),
                FileEntry("i_have_no_idea_what_im_doing.jpg", false),
                FileEntry("unexpected_john_cena.mp3", false)
            )
        ),
        FileResponse(
            path = "/trash",
            files = listOf(
                FileEntry("deleted_screenshot.png", false),
                FileEntry("oops.txt", false),
                FileEntry("angry_letter_to_boss.docx", false)
            )
        ),
        FileResponse(
            path = "/secret",
            files = listOf(
                FileEntry("hidden_folder", true),
                FileEntry("dont_open_this.txt", false),
                FileEntry(".env", false)
            )
        ),
        FileResponse(
            path = "/secret/hidden_folder",
            files = listOf(
                FileEntry("plans", true),
                FileEntry("spicy_meme.jpg", false)
            )
        ),
        FileResponse(
            path = "/secret/hidden_folder/plans",
            files = listOf(
                FileEntry("phase1.md", false),
                FileEntry("phase2.md", false),
                FileEntry("ultimate_revenge_plan.txt", false)
            )
        )
    )

    val fileResponsesMap = fileResponses.associateBy { it.path }

    val giaRecordsList = listOf(
        GiaRecord(1, "Eco Green Initiative", "John Doe", "New York", "2 Years", 500_000.0, "ongoing procurement", "CEST", "files/eco_green.pdf"),
        GiaRecord(2, "Tech for Kids", "Jane Smith", "San Francisco", "1 Year", 200_000.0, "ongoing procurement", "GIA", "docs/tech_kids.zip"),
        GiaRecord(3, "Clean Water Project", "Aqua Foundation", "Chicago", "3 Years", 1_000_000.0, "Pending", "CEST", "storage/clean_water_2023.docx"),
        GiaRecord(4, "Rural Electrification", "Energy Co.", "Texas", "2.5 Years", 750_000.0, "ongoing procurement", "R&D", "archives/rural_elec_2024.pptx"),
        GiaRecord(5, "Youth Empowerment", "Local NGO", "Miami", "1.5 Years", 300_000.0, "ongoing procurement", "CEST", "project_files/youth_empowerment_notes.pdf"),
        GiaRecord(6, "Food Security Plan", "AgriGrow", "Los Angeles", "2 Years", 600_000.0, "ongoing procurement", "CEST / ELCAC", "secured/agri_plan_6.dat"),
        GiaRecord(7, "Affordable Housing", "Urban Dev", "Seattle", "4 Years", 1_500_000.0, "Pending", "GIA", "assets/housing_affordables/plan.pdf"),
        GiaRecord(8, "Cyber Safety Awareness", "CyberTrust", "Boston", "1 Year", 250_000.0, "ongoing procurement", "R&D", "cyber/campaign_material.zip"),
        GiaRecord(9, "Disaster Relief Fund", "RedHelp", "New Orleans", "3 Years", 2_000_000.0, "ongoing procurement", "ELCAC", "files/disaster_relief_2022.tar.gz"),
        GiaRecord(10, "STEM Scholarships", "Future Minds", "Denver", "2 Years", 400_000.0, "Pending", "CEST", "cloud/stem_scholarship.csv"),
        GiaRecord(11, "Wildlife Protection", "GreenWorld", "Alaska", "5 Years", 1_200_000.0, "ongoing procurement", "CEST", "wildlife/reports_2025.doc"),
        GiaRecord(12, "Women in Tech", "SheCodes", "Austin", "2 Years", 350_000.0, "ongoing procurement", "CEST / ELCAC", "shecodes/final_report_2023.txt"),
        GiaRecord(13, "Urban Farming", "CityGrow", "Philadelphia", "1 Year", 150_000.0, "ongoing procurement", "GIA", "citygrow/plans/urban_farm_13.pdf"),
        GiaRecord(14, "Mental Health Initiative", "MindCare", "Portland", "3 Years", 500_000.0, "Pending", "R&D", "mindcare/mh_initiative_2024.pages"),
        GiaRecord(15, "Smart Cities Program", "FutureUrban", "Las Vegas", "4 Years", 3_000_000.0, "Pending", "CEST", "docs/smart_cities_roadmap.xlsx"),
        GiaRecord(16, "Child Nutrition Plan", "HealthyKids", "Atlanta", "2 Years", 800_000.0, "ongoing procurement", "GIA", "nutrition/child_nutrition_guidelines.json"),
        GiaRecord(17, "Renewable Energy Drive", "SunPower", "Phoenix", "3 Years", 2_500_000.0, "Completed", "R&D", "energy/renewables_2025.xml"),
        GiaRecord(18, "Digital Literacy Campaign", "TechAccess", "Detroit", "1 Year", 220_000.0, "Completed", "ELCAC", "docs/literacy_campaign_18.md"),
        GiaRecord(19, "Elderly Care Support", "SilverAge", "Orlando", "2 Years", 600_000.0, "Pending", "GIA", "support/elderly_guidebook.epub"),
        GiaRecord(20, "Flood Resilience Project", "SafeWaters", "Houston", "3.5 Years", 1_700_000.0, "Ongoing", "CEST", "resilience/flood_map_2025.kml"),
        GiaRecord(21, "Eco-Friendly Transport", "GreenRide", "San Diego", "2 Years", 900_000.0, "Pending", "R&D", "transport/greenride_poster.ai"),
        GiaRecord(22, "Coding Bootcamp", "CodeBase", "Raleigh", "1.5 Years", 400_000.0, "Ongoing", "CEST", "codebase/bootcamp_curriculum.psd"),
        GiaRecord(23, "Waste Management Reform", "CleanCity", "Baltimore", "3 Years", 1_100_000.0, "Pending", "GIA", "cleanup/reform_plan.mov"),
        GiaRecord(24, "Healthcare Access Program", "MediAid", "Cleveland", "4 Years", 2_000_000.0, "Completed", "CEST / ELCAC", "healthcare/access_plan_24.xls"),
        GiaRecord(25, "Youth Sports Fund", "PlayNow", "Kansas City", "1 Year", 350_000.0, "Ongoing", "GIA", "sports/youth_funding_form.pdf"),
        GiaRecord(26, "Reforestation Drive", "TreePlanet", "Boise", "5 Years", 2_200_000.0, "Ongoing", "ELCAC", "forest/greenwave_26.dwg"),
        GiaRecord(27, "Affordable Internet Project", "NetConnect", "Minneapolis", "2 Years", 500_000.0, "Pending", "R&D", "netconnect/files/affordable_net.txt"),
        GiaRecord(28, "Job Skills Training", "SkillUp", "Nashville", "1 Year", 450_000.0, "Completed", "CEST", "training/materials/skills.pdf"),
        GiaRecord(29, "Public Safety Initiative", "SafeStreets", "Columbus", "3 Years", 1_300_000.0, "Ongoing", "CEST / ELCAC", "safety/safe_initiative_29.html"),
        GiaRecord(30, "Biodiversity Research", "BioSphere", "Anchorage", "4 Years", 3_500_000.0, "Pending", "CEST", "bio/biodiversity_2025_report.odt")
    )

    val setupRecordsList = listOf(
        SetupRecord(1, "ByteForge", "Brain-Machine Interfaces", 3319629.48, 2023, "Folder N6", "Manila", "Region IV-B", "ENERGY_ENV", "Ongoing", "Laptops, Servers"),
        SetupRecord(2, "EcoDrive Motors", "EV Components", 2333620.8, 2020, "Folder I8", "Cebu", "Region VII", "AGRI_HORTI", "Ongoing", "Battery Packs"),
        SetupRecord(3, "FarmLink", "EV Components", 3891421.77, 2020, "Folder Z3", "Davao", "NCR", "MTL_ENGRING", "Ongoing", "Drones, Sensors"),
        SetupRecord(4, "NeuroWave", "Brain-Machine Interfaces", 2547719.47, 2021, "Folder U1", "Taguig", "Region VI", "MARINE_AQUATIC", "Terminated", "EEG Headsets"),
        SetupRecord(5, "SmartLogiX", "Logistics AI", 4816621.66, 2022, "Folder M1", "Iloilo", "Region III", "MARINE_AQUATIC", "Terminated", "Fleet Trackers"),
        SetupRecord(6, "HydroFuel Co", "EV Components", 4043296.03, 2020, "Folder Y9", "Zamboanga", "Region XII", "FOOD_PROCESSING", "Completed", "Tanks, Reactors"),
        SetupRecord(7, "BuildRight", "AgriTech Services", 6098551.58, 2024, "Folder W3", "Cavite", "CAR", "AGRI_HORTI", "Ongoing", "3D Printers"),
        SetupRecord(8, "MindSpark", "Brain-Machine Interfaces", 1798052.02, 2024, "Folder R9", "Baguio", "Region IV-A", "MARINE_AQUATIC", "Ongoing", "Tablets, VR Gear"),
        SetupRecord(9, "CleanStream", "Software, AI Tools", 2032240.98, 2020, "Folder B5", "Dumaguete", "Region VII", "HANDICRAFT", "Completed", "Filtration Systems"),
        SetupRecord(10, "GenBioWorks", "EV Components", 3361076.37, 2024, "Folder S5", "General Santos", "Region II", "HANDICRAFT", "Ongoing", "Gene Synthesizers"),
        SetupRecord(11, "Voltify", "AgriTech Services", 5103082.0, 2021, "Folder N5", "Cagayan de Oro", "Region II", "FURNITURE", "Terminated", "Shredders"),
        SetupRecord(12, "NanoCure", "EV Components", 2999514.41, 2023, "Folder R2", "Pasig", "Region XI", "HANDICRAFT", "Completed", "Injectors"),
        SetupRecord(13, "SolarTrail", "Software, AI Tools", 1865664.82, 2023, "Folder I7", "Tacloban", "Region II", "ENERGY_ENV", "Completed", "Panels, Inverters"),
        SetupRecord(14, "CitySync", "EV Components", 4473171.45, 2023, "Folder J7", "Quezon City", "Region I", "AGRI_HORTI", "Terminated", "Data Hubs"),
        SetupRecord(15, "MediPlus", "Software, AI Tools", 2812963.53, 2021, "Folder P4", "Valenzuela", "Region VI", "MTL_ENGRING", "Ongoing", "Servers, Cameras"),
        SetupRecord(16, "AutoGrow", "EV Components", 1812219.34, 2021, "Folder B9", "Bukidnon", "Region I", "FURNITURE", "Completed", "Grow Lights"),
        SetupRecord(17, "SwiftWater", "EV Components", 2572504.37, 2024, "Folder L5", "Pampanga", "CAR", "FOOD_PROCESSING", "Ongoing", "Pumps, Dams"),
        SetupRecord(18, "EduSphere", "EV Components", 3356771.97, 2021, "Folder L9", "Caloocan", "CAR", "ENERGY_ENV", "Completed", "Dashboards"),
        SetupRecord(19, "CyberNest", "AgriTech Services", 3697131.67, 2024, "Folder U8", "Makati", "Region VIII", "ENERGY_ENV", "Ongoing", "Firewalls, Agents"),
        SetupRecord(20, "AgroFleet", "Logistics AI", 5903294.88, 2022, "Folder H1", "Negros Occidental", "NCR", "FOOD_PROCESSING", "Ongoing", "Tractors, Sensors"),
        SetupRecord(21, "SkyPort", "EV Components", 5285325.72, 2021, "Folder X4", "Palawan", "Region IX", "ENERGY_ENV", "Terminated", "Drones"),
        SetupRecord(22, "RainHarvest", "Software, AI Tools", 3536223.75, 2024, "Folder F3", "La Union", "Region XI", "MARINE_AQUATIC", "Ongoing", "Storage Tanks"),
        SetupRecord(23, "ReLeaf", "Logistics AI", 5882440.63, 2023, "Folder O1", "Bohol", "Region XII", "MTL_ENGRING", "Terminated", "Seed Drones"),
        SetupRecord(24, "FoodSec", "Software, AI Tools", 2785572.13, 2020, "Folder F2", "Isabela", "Region XII", "HANDICRAFT", "Ongoing", "Sensors, Tablets"),
        SetupRecord(25, "ByteLabs", "EV Components", 1701004.78, 2021, "Folder B3", "Marikina", "Region XI", "ICT", "Ongoing", "Workstations"),
        SetupRecord(26, "PulseTech", "Brain-Machine Interfaces", 4390894.08, 2022, "Folder U3", "Davao del Norte", "Region VIII", "ICT", "Completed", "Wearables"),
        SetupRecord(27, "EcoCharge", "Software, AI Tools", 5560199.09, 2020, "Folder T4", "Batangas", "Region XII", "FURNITURE", "Ongoing", "Stations"),
        SetupRecord(28, "CityFlow", "AgriTech Services", 3492402.16, 2024, "Folder Z2", "Antipolo", "Region VI", "MTL_ENGRING", "Completed", "Planning Tools"),
        SetupRecord(29, "SecureLink", "AgriTech Services", 2122626.64, 2023, "Folder H3", "San Juan", "Region II", "MTL_ENGRING", "Completed", "Firewalls"),
        SetupRecord(30, "CleanDrop", "Software, AI Tools", 3741276.02, 2024, "Folder Z8", "Cotabato", "Region VIII", "HANDICRAFT", "Terminated", "Mobile Units")
    )
}
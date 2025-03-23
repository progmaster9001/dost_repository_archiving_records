package com.ojtapp.mobile

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

val giaRecordsList = listOf(
    GiaRecord(1, "Eco Green Initiative", "John Doe", "New York", "2 Years", "$500,000", "Ongoing", "Environmental"),
    GiaRecord(2, "Tech for Kids", "Jane Smith", "San Francisco", "1 Year", "$200,000", "Completed", "Education"),
    GiaRecord(3, "Clean Water Project", "Aqua Foundation", "Chicago", "3 Years", "$1,000,000", "Pending", "Health"),
    GiaRecord(4, "Rural Electrification", "Energy Co.", "Texas", "2.5 Years", "$750,000", "Ongoing", "Infrastructure"),
    GiaRecord(5, "Youth Empowerment", "Local NGO", "Miami", "1.5 Years", "$300,000", "Completed", "Social"),
    GiaRecord(6, "Food Security Plan", "AgriGrow", "Los Angeles", "2 Years", "$600,000", "Ongoing", "Agriculture"),
    GiaRecord(7, "Affordable Housing", "Urban Dev", "Seattle", "4 Years", "$1,500,000", "Upcoming", "Housing"),
    GiaRecord(8, "Cyber Safety Awareness", "CyberTrust", "Boston", "1 Year", "$250,000", "Completed", "Technology"),
    GiaRecord(9, "Disaster Relief Fund", "RedHelp", "New Orleans", "3 Years", "$2,000,000", "Ongoing", "Relief"),
    GiaRecord(10, "STEM Scholarships", "Future Minds", "Denver", "2 Years", "$400,000", "Pending", "Education"),
    GiaRecord(11, "Wildlife Protection", "GreenWorld", "Alaska", "5 Years", "$1,200,000", "Ongoing", "Environmental"),
    GiaRecord(12, "Women in Tech", "SheCodes", "Austin", "2 Years", "$350,000", "Completed", "Education"),
    GiaRecord(13, "Urban Farming", "CityGrow", "Philadelphia", "1 Year", "$150,000", "Ongoing", "Agriculture"),
    GiaRecord(14, "Mental Health Initiative", "MindCare", "Portland", "3 Years", "$500,000", "Pending", "Health"),
    GiaRecord(15, "Smart Cities Program", "FutureUrban", "Las Vegas", "4 Years", "$3,000,000", "Upcoming", "Infrastructure"),
    GiaRecord(16, "Child Nutrition Plan", "HealthyKids", "Atlanta", "2 Years", "$800,000", "Ongoing", "Social"),
    GiaRecord(17, "Renewable Energy Drive", "SunPower", "Phoenix", "3 Years", "$2,500,000", "Completed", "Energy"),
    GiaRecord(18, "Digital Literacy Campaign", "TechAccess", "Detroit", "1 Year", "$220,000", "Completed", "Technology"),
    GiaRecord(19, "Elderly Care Support", "SilverAge", "Orlando", "2 Years", "$600,000", "Pending", "Health"),
    GiaRecord(20, "Flood Resilience Project", "SafeWaters", "Houston", "3.5 Years", "$1,700,000", "Ongoing", "Relief"),
    GiaRecord(21, "Eco-Friendly Transport", "GreenRide", "San Diego", "2 Years", "$900,000", "Upcoming", "Environmental"),
    GiaRecord(22, "Coding Bootcamp", "CodeBase", "Raleigh", "1.5 Years", "$400,000", "Ongoing", "Education"),
    GiaRecord(23, "Waste Management Reform", "CleanCity", "Baltimore", "3 Years", "$1,100,000", "Pending", "Infrastructure"),
    GiaRecord(24, "Healthcare Access Program", "MediAid", "Cleveland", "4 Years", "$2,000,000", "Completed", "Health"),
    GiaRecord(25, "Youth Sports Fund", "PlayNow", "Kansas City", "1 Year", "$350,000", "Ongoing", "Social"),
    GiaRecord(26, "Reforestation Drive", "TreePlanet", "Boise", "5 Years", "$2,200,000", "Ongoing", "Environmental"),
    GiaRecord(27, "Affordable Internet Project", "NetConnect", "Minneapolis", "2 Years", "$500,000", "Pending", "Technology"),
    GiaRecord(28, "Job Skills Training", "SkillUp", "Nashville", "1 Year", "$450,000", "Completed", "Education"),
    GiaRecord(29, "Public Safety Initiative", "SafeStreets", "Columbus", "3 Years", "$1,300,000", "Ongoing", "Social"),
    GiaRecord(30, "Biodiversity Research", "BioSphere", "Anchorage", "4 Years", "$3,500,000", "Upcoming", "Environmental")
)

val setupRecordsList = listOf(
    SetupRecord(1, "Tech Innovators", "AI, Blockchain", "$2,000,000", 2023, "Active", "Technology"),
    SetupRecord(2, "Green Energy Ltd.", "Solar Panels, Wind Turbines", "$5,000,000", 2022, "Approved", "Renewable Energy"),
    SetupRecord(3, "Urban Builders", "Construction, Design", "$3,500,000", 2021, "Operational", "Infrastructure"),
    SetupRecord(4, "AgriGrow Solutions", "Farming Equipments", "$1,200,000", 2023, "Pending", "Agriculture"),
    SetupRecord(5, "MediTech", "Medical Devices", "$4,000,000", 2020, "Active", "Healthcare"),
    SetupRecord(6, "EduTech Hub", "E-learning Platforms", "$800,000", 2024, "Approved", "Education"),
    SetupRecord(7, "AutoGenius", "EV Manufacturing", "$6,000,000", 2023, "Operational", "Automobile"),
    SetupRecord(8, "CyberDefenders", "Security Software", "$3,000,000", 2021, "Active", "Cybersecurity"),
    SetupRecord(9, "AquaSafe", "Water Purification Tech", "$2,500,000", 2022, "Pending", "Water Management"),
    SetupRecord(10, "FoodChain Inc.", "Supply Chain Logistics", "$1,800,000", 2020, "Operational", "Logistics"),
    SetupRecord(11, "NanoHealth", "Nanotech Solutions", "$5,000,000", 2024, "Active", "Healthcare"),
    SetupRecord(12, "Future Mobility", "Autonomous Vehicles", "$7,000,000", 2023, "Approved", "Automobile"),
    SetupRecord(13, "EcoHome Builders", "Green Housing Materials", "$3,000,000", 2022, "Operational", "Construction"),
    SetupRecord(14, "SmartAgri", "Precision Agriculture", "$1,500,000", 2024, "Pending", "Agriculture"),
    SetupRecord(15, "CloudWare", "SaaS Platforms", "$2,200,000", 2023, "Active", "Technology"),
    SetupRecord(16, "HydroPower Corp", "Hydropower Plants", "$8,000,000", 2021, "Approved", "Renewable Energy"),
    SetupRecord(17, "RecycloTech", "Recycling Machinery", "$1,750,000", 2023, "Operational", "Environment"),
    SetupRecord(18, "SafeMed", "Healthcare IT Solutions", "$2,900,000", 2022, "Pending", "Healthcare"),
    SetupRecord(19, "BioHarvest", "Biotech Research", "$4,400,000", 2020, "Approved", "Biotechnology"),
    SetupRecord(20, "SkillBridge", "Online Education Tools", "$1,100,000", 2024, "Active", "Education"),
    SetupRecord(21, "VoltTech", "Battery Storage Systems", "$3,600,000", 2023, "Operational", "Energy"),
    SetupRecord(22, "UrbanSpace", "City Planning Software", "$2,400,000", 2022, "Approved", "Infrastructure"),
    SetupRecord(23, "Farm2Table", "Agri Supply Chain", "$900,000", 2023, "Pending", "Logistics"),
    SetupRecord(24, "AeroClean", "Air Purification Systems", "$1,600,000", 2021, "Operational", "Environment"),
    SetupRecord(25, "QuantumAI Labs", "Quantum Computing", "$9,000,000", 2024, "Approved", "Technology"),
    SetupRecord(26, "HealWell Devices", "Wearable Health Tech", "$2,300,000", 2022, "Active", "Healthcare"),
    SetupRecord(27, "SolarWorks", "Off-grid Solar Solutions", "$1,700,000", 2023, "Pending", "Renewable Energy"),
    SetupRecord(28, "TranspoSmart", "Smart Traffic Management", "$4,500,000", 2024, "Operational", "Infrastructure"),
    SetupRecord(29, "CyberNexus", "Advanced Cybersecurity Tools", "$3,800,000", 2023, "Active", "Cybersecurity"),
    SetupRecord(30, "FreshWave", "Water Desalination Tech", "$2,900,000", 2021, "Approved", "Water Management")
)

class LocalRecordsRepository: RecordsRepository {
    private val _setupRecords = MutableStateFlow(setupRecordsList)
    private val _giaRecords = MutableStateFlow(giaRecordsList)

    override val setupRecords = _setupRecords.asStateFlow()
    override val giaRecords = _giaRecords.asStateFlow()

    override fun getRecords(type: Type): Flow<Resource<List<Record>>> =
        (if (type == Type.GIA) giaRecords else setupRecords)
            .map { records -> Resource.Success(records) as Resource<List<Record>> }
            .onStart {
                emit(Resource.Loading)
                delay(1000)
            }

    override suspend fun updateRecord(type: Type) {
        TODO("Not yet implemented")
    }
}
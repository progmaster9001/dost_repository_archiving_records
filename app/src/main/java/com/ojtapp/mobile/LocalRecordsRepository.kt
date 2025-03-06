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
    GiaRecord(10, "STEM Scholarships", "Future Minds", "Denver", "2 Years", "$400,000", "Pending", "Education")
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
    SetupRecord(10, "FoodChain Inc.", "Supply Chain Logistics", "$1,800,000", 2020, "Operational", "Logistics")
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
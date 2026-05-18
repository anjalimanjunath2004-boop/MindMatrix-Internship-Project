package com.raktaseva.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
val PrimaryRed = Color(0xFFC62828)
val SurfaceBg = Color(0xFFF8F9FA)
val SuccessGreen = Color(0xFF2E7D32)
val TextPrimary = Color(0xFF212121)
val TextSecondary = Color(0xFF757575)

// --- DATA MODELS ---
data class Donor(
    val id: String,
    val name: String,
    val bloodType: String,
    val distance: String,
    val isEligible: Boolean = true,
    val reliabilityScore: Int = 95,
    val location: String = "Mumbai, MH"
)

data class BloodRequest(
    val id: String,
    val patientName: String,
    val bloodType: String,
    val location: String,
    val unitsNeeded: Int,
    val urgency: String = "Critical"
)

// --- THEME ---
@Composable
fun RaktaSevaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = PrimaryRed,
            background = SurfaceBg,
            surface = Color.White
        ),
        typography = Typography(),
        content = content
    )
}

// --- MAIN ACTIVITY ENTRY POINT ---
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RaktaSevaTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            Surface(shadowElevation = 16.dp, color = Color.White) {
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 0.dp,
                    modifier = Modifier.height(72.dp)
                ) {
                    val items = listOf(
                        Triple("PULSE", Icons.Default.Favorite, 0),
                        Triple("FIND", Icons.Default.Search, 1),
                        Triple("ALERTS", Icons.Default.Notifications, 2),
                        Triple("YOU", Icons.Default.Person, 3)
                    )
                    items.forEach { (label, icon, index) ->
                        NavigationBarItem(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            label = {
                                Text(label, fontSize = 9.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                            },
                            icon = { Icon(icon, null, modifier = Modifier.size(20.dp)) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = PrimaryRed,
                                selectedTextColor = PrimaryRed,
                                unselectedIconColor = Color(0xFFBDBDBD),
                                indicatorColor = PrimaryRed.copy(alpha = 0.08f)
                            )
                        )
                    }
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize().background(SurfaceBg)) {
            when (selectedTab) {
                0 -> DashboardScreen()
                1 -> DonorSearchScreen()
                2 -> EmergencyRequestsScreen()
                3 -> ProfileScreen()
            }
        }
    }
}

// --- 1. DASHBOARD SCREEN ---
@Composable
fun DashboardScreen() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // AI Health Intelligence Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = PrimaryRed)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("AI HEALTH INTELLIGENCE", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 2.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Text("\"Recovery is optimal. Stay hydrated for your next donation event.\"", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Black)
            }
        }

        // Bio-Composition Card
        Text("BIO-COMPOSITION", fontSize = 12.sp, fontWeight = FontWeight.Black, color = PrimaryRed.copy(alpha = 0.6f), letterSpacing = 2.sp)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFFBE9E7))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("HEMOGLOBIN QUALITY", color = TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Text("Prime (14.2 g/dL)", fontWeight = FontWeight.Black, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(12.dp))
                LinearProgressIndicator(progress = {0.72f}, modifier = Modifier.fillMaxWidth().height(10.dp), color = PrimaryRed, trackColor = Color(0xFFEEEEEE))
            }
        }

        // Action Grid
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            DashboardButton("Find Donors", Icons.Default.Search, Color(0xFF212121), Modifier.weight(1f))
            DashboardButton("Post Request", Icons.Default.Add, PrimaryRed, Modifier.weight(1f))
        }
    }
}

@Composable
fun DashboardButton(label: String, icon: ImageVector, color: Color, modifier: Modifier) {
    Card(modifier = modifier.height(100.dp), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = color)) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, null, tint = Color.White, modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(label.uppercase(), color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// --- 2. DONOR SEARCH SCREEN ---
@Composable
fun DonorSearchScreen() {
    var searchQuery by remember { mutableStateOf("") }
    val donors = remember {
        listOf(
            Donor("1", "Rahul Sharma", "B+", "0.8 km"),
            Donor("2", "Vikram Singh", "B+", "2.4 km"),
            Donor("3", "Priyanka C.", "B+", "3.1 km", false)
        )
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("DONOR NETWORK", fontSize = 10.sp, fontWeight = FontWeight.Black, color = TextSecondary, letterSpacing = 2.sp)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search location or name...") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            leadingIcon = { Icon(Icons.Default.Search, null) }
        )

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(donors) { donor ->
                DonorCard(donor)
            }
        }
    }
}

@Composable
fun DonorCard(donor: Donor) {
    Surface(modifier = Modifier.fillMaxWidth(), color = Color.White, shape = RoundedCornerShape(20.dp), border = BorderStroke(1.dp, Color(0xFFF5F5F5))) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(48.dp).background(Color(0xFFEEEEEE), RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Person, null, tint = Color.Gray)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(donor.name, fontWeight = FontWeight.Black, fontSize = 15.sp)
                Text(donor.location, fontSize = 11.sp, color = Color.Gray)
            }
            Text(donor.bloodType, fontWeight = FontWeight.Black, color = PrimaryRed, fontSize = 18.sp)
        }
    }
}

// --- 3. EMERGENCY ALERTS SCREEN ---
@Composable
fun EmergencyRequestsScreen() {
    val requests = listOf(
        BloodRequest("1", "Suresh Kumar", "O+", "Apollo Hospital", 2),
        BloodRequest("2", "Meena Bose", "B+", "City General", 1, "High")
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("URGENT ALERTS", fontWeight = FontWeight.Black, fontSize = 20.sp)
        requests.forEach { request ->
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(request.urgency.uppercase(), color = PrimaryRed, fontSize = 10.sp, fontWeight = FontWeight.Black)
                    Text(request.patientName, fontWeight = FontWeight.Black, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(request.location, color = Color.Gray, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {}, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = Color.Black)) {
                        Text("COMMIT TO DONATE")
                    }
                }
            }
        }
    }
}

// --- 4. PROFILE SCREEN ---
@Composable
fun ProfileScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(24.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(80.dp).background(Color(0xFFEEEEEE), RoundedCornerShape(24.dp)), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Person, null, modifier = Modifier.size(40.dp), tint = Color.Gray)
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column {
                Text("Anjali Manjunath", fontWeight = FontWeight.Black, fontSize = 22.sp)
                Text("Golden Donor Status", color = PrimaryRed, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }

        Surface(modifier = Modifier.fillMaxWidth(), color = Color.White, shape = RoundedCornerShape(24.dp), border = BorderStroke(1.dp, Color(0xFFF1F1F1))) {
            Row(modifier = Modifier.padding(20.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text("BLOOD TYPE", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.Gray)
                    Text("B Positive (B+)", fontWeight = FontWeight.Black, fontSize = 16.sp)
                }
                TextButton(onClick = {}) { Text("EDIT") }
            }
        }
    }
}
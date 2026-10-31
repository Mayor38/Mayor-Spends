package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fitness_goals")
data class FitnessGoalEntity(
    @PrimaryKey val id: Int = 1,
    val dailyCalorieTarget: Int = 750,
    val weeklyCalorieTarget: Int = 4500, // Weekly calorie burn goal
    val dailyStepTarget: Int = 10000,
    val dailyActiveMinutesTarget: Int = 45,
    val dailyWaterTargetMl: Int = 3000, // 3 Liters or approx 6 sachets
    val weeklyWorkoutDaysTarget: Int = 5,
    val currentWeightKg: Float = 78.5f,
    val targetWeightKg: Float = 73.0f,
    val currentStreak: Int = 7,
    val mayorTitle: String = "Lekki Fitness Boss",
    val arewaTitle: String = "Sarkin Motsa Jiki", // Arewa Title: King of Fitness
    val pidginLevel: String = "Senior Man"
)

@Entity(tableName = "workout_sessions")
data class WorkoutSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val category: String, // Cardio, Strength, Dance, Outdoor, HIIT, Walking
    val caloriesBurned: Int,
    val durationSeconds: Int,
    val avgHeartRate: Int,
    val distanceKm: Float = 0f,
    val timestamp: Long = System.currentTimeMillis(),
    val intensity: String = "High",
    val notes: String = ""
)

@Entity(tableName = "daily_logs")
data class DailyLogEntity(
    @PrimaryKey val date: String, // format "YYYY-MM-DD"
    val dayOfWeek: String,       // "Mon", "Tue", etc.
    val caloriesSpent: Int,
    val stepsCount: Int,
    val activeMinutes: Int,
    val waterIntakeMl: Int,
    val workoutsCount: Int
)

data class PresetWorkout(
    val id: String,
    val title: String,
    val location: String,
    val category: String,
    val baseCalPerMin: Float,
    val description: String,
    val motivationalQuote: String
)

val NAIJA_PRESET_WORKOUTS = listOf(
    PresetWorkout(
        id = "lekki_run",
        title = "Lekki-Ikoyi Bridge 10k Run",
        location = "Lekki, Lagos",
        category = "Cardio",
        baseCalPerMin = 11.5f,
        description = "Breeze through the iconic cable bridge with Lagos runners.",
        motivationalQuote = "No dulling on Lekki Bridge! Sweat na victory!"
    ),
    PresetWorkout(
        id = "afrobeats_dance",
        title = "Afrobeats High-Energy Cardio",
        location = "Lagos / Worldwide",
        category = "Dance",
        baseCalPerMin = 9.8f,
        description = "Gbese, Zanku, and Azonto rhythm high-calorie dance workout.",
        motivationalQuote = "Legwork burn pass treadmill! Move body!"
    ),
    PresetWorkout(
        id = "zuma_climb",
        title = "Zuma Rock Power Incline",
        location = "Niger State / Abuja",
        category = "Outdoor",
        baseCalPerMin = 12.0f,
        description = "Stupendous elevation climb for quads, glutes, and mental grit.",
        motivationalQuote = "Zuma Rock no dey shake, and you no go fold!"
    ),
    PresetWorkout(
        id = "gym_benders",
        title = "Iron Benders Heavy Gym Lift",
        location = "Strength Zone",
        category = "Strength",
        baseCalPerMin = 8.5f,
        description = "Compound squats, bench press, deadlifts, and pull-ups.",
        motivationalQuote = "Muscle no be by mouth, lift am!"
    ),
    PresetWorkout(
        id = "stadium_sprint",
        title = "National Stadium Track Intervals",
        location = "Surulere, Lagos",
        category = "HIIT",
        baseCalPerMin = 13.2f,
        description = "Explosive 400m intervals and bleacher plyometric jumps.",
        motivationalQuote = "Speed of a Super Eagle! Oya dash!"
    ),
    PresetWorkout(
        id = "obudu_trek",
        title = "Obudu Mountain Trail Trek",
        location = "Cross River",
        category = "Outdoor",
        baseCalPerMin = 10.0f,
        description = "Altitude trek through lush hills and cloud-covered peaks.",
        motivationalQuote = "Fresh mountain air, pure Naija endurance!"
    ),
    PresetWorkout(
        id = "kano_dala_sprint",
        title = "Kano Ancient Walls & Dala Hill Sprint",
        location = "Kano State",
        category = "Cardio",
        baseCalPerMin = 12.5f,
        description = "Ascend the historic 120-step Dala Hill and sprint the red-earth perimeter of ancient Kano.",
        motivationalQuote = "Kano Dala Hill stood for 1000 years — your endurance go last pass am!"
    ),
    PresetWorkout(
        id = "yankari_trail",
        title = "Yankari Safari Savannah Trek",
        location = "Bauchi State",
        category = "Outdoor",
        baseCalPerMin = 10.8f,
        description = "High-stride cross-country endurance run through the rich Arewa savannah.",
        motivationalQuote = "Karfin zuciya ne komai! Strength of heart conquers the savannah!"
    ),
    PresetWorkout(
        id = "dambe_warrior",
        title = "Arewa Dambe Martial Combat Conditioning",
        location = "Northern Arena",
        category = "HIIT",
        baseCalPerMin = 14.0f,
        description = "High-octane traditional Hausa Dambe stance drills, heavy core conditioning, and rapid jabs.",
        motivationalQuote = "Sannu da aiki, Jarumi! Dambe spirit burns every ounce of fat!"
    ),
    PresetWorkout(
        id = "mambilla_climb",
        title = "Mambilla High Altitude Plateau Run",
        location = "Taraba State",
        category = "Outdoor",
        baseCalPerMin = 11.8f,
        description = "High-altitude lung capacity test along tea hills and mountain peaks.",
        motivationalQuote = "Cool breeze, hot sweat, pure Arewa endurance!"
    )
)

data class FitnessLocation(
    val id: String,
    val name: String,
    val stateCity: String,
    val region: String, // "Arewa / North", "South / Coast", "East / Highlands"
    val xPercent: Float, // 0.0 to 1.0 (X coordinate on stylized map)
    val yPercent: Float, // 0.0 to 1.0 (Y coordinate on stylized map)
    val distanceKm: Float,
    val elevationGainM: Int,
    val estimatedCalories: Int,
    val category: String,
    val difficulty: String,
    val arewaVibe: String,
    val description: String
)

val AREWA_NIGERIA_LOCATIONS = listOf(
    FitnessLocation(
        id = "loc_dala_hill",
        name = "Dala Hill & Kano Ancient Wall Circuit",
        stateCity = "Kano, Kano State",
        region = "Arewa / North",
        xPercent = 0.58f,
        yPercent = 0.22f,
        distanceKm = 8.5f,
        elevationGainM = 180,
        estimatedCalories = 620,
        category = "Historic Trail & Steps",
        difficulty = "Jarumi / Elite",
        arewaVibe = "Ancient Kingdom of Kano 🏰",
        description = "Climb the 120 historic stone steps of Dala Hill, then jog along the remaining perimeter of the famous ancient city walls."
    ),
    FitnessLocation(
        id = "loc_zuma_rock",
        name = "Zuma Rock Perimeter Trail",
        stateCity = "Madalla, Niger / Abuja",
        region = "Arewa / North",
        xPercent = 0.46f,
        yPercent = 0.48f,
        distanceKm = 10.2f,
        elevationGainM = 220,
        estimatedCalories = 750,
        category = "Monolith Incline",
        difficulty = "Challenging",
        arewaVibe = "Gateway to Northern Capital ⛰️",
        description = "A rugged, majestic 10km loop encircling the iconic Zuma monolith, offering rocky terrain and inspiring northern vistas."
    ),
    FitnessLocation(
        id = "loc_kajuru_castle",
        name = "Kajuru Castle Ridge Run",
        stateCity = "Kajuru, Kaduna State",
        region = "Arewa / North",
        xPercent = 0.49f,
        yPercent = 0.36f,
        distanceKm = 7.0f,
        elevationGainM = 310,
        estimatedCalories = 580,
        category = "Mountain Ridge",
        difficulty = "Jarumi / Elite",
        arewaVibe = "Medieval Kaduna Highlands 🛡️",
        description = "A steep uphill endurance run traversing granite outcrops surrounding the fairytale medieval castle."
    ),
    FitnessLocation(
        id = "loc_shere_hills",
        name = "Shere Hills Granite Ridge",
        stateCity = "Jos, Plateau State",
        region = "Arewa / North",
        xPercent = 0.63f,
        yPercent = 0.42f,
        distanceKm = 9.4f,
        elevationGainM = 450,
        estimatedCalories = 810,
        category = "Highland Scramble",
        difficulty = "Jarumi / Elite",
        arewaVibe = "Cool Temperate Arewa Heights 🌬️",
        description = "Towering over 1,800m above sea level in the temperate Jos Plateau, offering crisp thin-air cardio and challenging rock climbs."
    ),
    FitnessLocation(
        id = "loc_yankari",
        name = "Yankari Savannah Trail",
        stateCity = "Bauchi, Bauchi State",
        region = "Arewa / North",
        xPercent = 0.72f,
        yPercent = 0.34f,
        distanceKm = 12.0f,
        elevationGainM = 90,
        estimatedCalories = 820,
        category = "Savannah Cross-Country",
        difficulty = "Moderate",
        arewaVibe = "Wilderness & Warm Springs 🌿",
        description = "Wide dirt tracks winding through open northern savannah, ending at the crystal-clear Wikki Warm Springs for post-run recovery."
    ),
    FitnessLocation(
        id = "loc_mambilla",
        name = "Mambilla Plateau Tea Estates",
        stateCity = "Gembu, Taraba State",
        region = "Arewa / North",
        xPercent = 0.82f,
        yPercent = 0.62f,
        distanceKm = 14.5f,
        elevationGainM = 540,
        estimatedCalories = 950,
        category = "High Altitude Mountain",
        difficulty = "Jarumi / Elite",
        arewaVibe = "Roof of Nigeria 🌄",
        description = "Nigeria's highest plateau with rolling green hills, refreshing mountain mist, and elite cardiovascular training elevation."
    ),
    FitnessLocation(
        id = "loc_gurara",
        name = "Gurara Falls Gorge Trail",
        stateCity = "Gurara, Niger State",
        region = "Arewa / North",
        xPercent = 0.39f,
        yPercent = 0.46f,
        distanceKm = 6.8f,
        elevationGainM = 160,
        estimatedCalories = 490,
        category = "Waterfall Canyon Trail",
        difficulty = "Moderate",
        arewaVibe = "Roaring Fresh Waters of Niger 🌊",
        description = "Lush riverside trail with natural stair steps carved into the earth next to cascading waterfall torrents."
    ),
    FitnessLocation(
        id = "loc_lekki_bridge",
        name = "Lekki-Ikoyi Link Bridge",
        stateCity = "Lagos, Lagos State",
        region = "South / Coast",
        xPercent = 0.22f,
        yPercent = 0.82f,
        distanceKm = 8.0f,
        elevationGainM = 60,
        estimatedCalories = 540,
        category = "Urban Suspension Bridge",
        difficulty = "Moderate",
        arewaVibe = "Atlantic Coastal Breeze 🌉",
        description = "The premier urban fitness artery of Lagos with breezy ocean air and steady incline over the lagoon."
    ),
    FitnessLocation(
        id = "loc_obudu",
        name = "Obudu Mountain Ranch Skyline",
        stateCity = "Obanliku, Cross River State",
        region = "South / Coast",
        xPercent = 0.68f,
        yPercent = 0.80f,
        distanceKm = 11.0f,
        elevationGainM = 490,
        estimatedCalories = 860,
        category = "Canopy Skyline Trail",
        difficulty = "Jarumi / Elite",
        arewaVibe = "Cloud Forest Frontier ☁️",
        description = "Challenging canopy walk and serpentine hill roads rising into cloud forests at 1,576 meters."
    )
)

data class NaijaQuote(
    val quote: String,
    val category: String, // "Lagos Hustle", "Senior Man Wisdom", "Proverb Remix", "Afrobeats Energy", "Challenger Spirit"
    val meaning: String = ""
)

val NAIJA_QUOTE_COLLECTION = listOf(
    NaijaQuote(
        quote = "Body no be firewood, Mayor! If you no spend this energy today, fat go spend you tomorrow!",
        category = "Senior Man Wisdom",
        meaning = "Your body is alive and dynamic; exercise keeps you vibrant and unstoppable."
    ),
    NaijaQuote(
        quote = "Mayor Spends: Sweat na the currency, fitness na the wealth! Spend am with pride!",
        category = "Lagos Hustle",
        meaning = "Every drop of sweat invested is compound interest for your health."
    ),
    NaijaQuote(
        quote = "No dulling! Even Danfo driver dey hustle for road before sunrise. Oya tie your laces!",
        category = "Lagos Hustle",
        meaning = "Early hustle wins the day. Rise up with the spirit of Lagos!"
    ),
    NaijaQuote(
        quote = "E choke! You dey burn calories like generator on pure fuel! Keep moving!",
        category = "Afrobeats Energy",
        meaning = "Pure unstoppable momentum. Your workout intensity is electrifying!"
    ),
    NaijaQuote(
        quote = "Water wey pass garri, na fitness go rescue am. Sweat out every doubt and stress!",
        category = "Proverb Remix",
        meaning = "When life brings too much pressure, physical movement clears the mind."
    ),
    NaijaQuote(
        quote = "Discipline pass motivation, my guy. When rain dey fall, champions dey lace boots!",
        category = "Senior Man Wisdom",
        meaning = "Motivation gets you started, but Naija discipline keeps you finishing strong."
    ),
    NaijaQuote(
        quote = "Small steps every day na giant leap for Naija champion. Consistency no dey lie!",
        category = "Challenger Spirit",
        meaning = "Daily micro-efforts stack into legendary physical milestones."
    ),
    NaijaQuote(
        quote = "As you spend energy, na so your stamina dey multiply. Senior Man no dey fold!",
        category = "Senior Man Wisdom",
        meaning = "The more energy you spend in training, the more vitality you generate."
    ),
    NaijaQuote(
        quote = "Run until calories raise white flag shout: 'Oya Mayor, we surrender!'",
        category = "Challenger Spirit",
        meaning = "Overpower your workout until your targets are completely crushed."
    ),
    NaijaQuote(
        quote = "Lekki Bridge no dey shift for anybody. Carry your spirit, cross that finish line!",
        category = "Lagos Hustle",
        meaning = "Conquer the iconic Lagos bridge run with mental toughness."
    ),
    NaijaQuote(
        quote = "If fitness easy, everybody for get six-pack like plantain chips. Show them what you made of!",
        category = "Proverb Remix",
        meaning = "The difficulty of fitness is exactly what makes the victory so sweet."
    ),
    NaijaQuote(
        quote = "Energy no dey finish for where Mayor dey! Spark the fire and dominate today!",
        category = "Afrobeats Energy",
        meaning = "Tap into boundless reserves of athletic passion and joy."
    ),
    NaijaQuote(
        quote = "Zuma Rock solid, your determination solid pass am. Push one more rep!",
        category = "Challenger Spirit",
        meaning = "Stand immovable like the great Zuma rock against fatigue."
    ),
    NaijaQuote(
        quote = "Gbese! Put Afrobeats on blast, turn your sweat into pure rhythm and power!",
        category = "Afrobeats Energy",
        meaning = "Sync your workout to the high vibrations of African music."
    ),
    NaijaQuote(
        quote = "Consistency na the real cheat code. Show up today, surprise yourself tomorrow!",
        category = "Senior Man Wisdom",
        meaning = "Showing up daily beats irregular bursts of perfection."
    ),
    NaijaQuote(
        quote = "Calorie spend today na longevity profit tomorrow. Spend like a billionaire!",
        category = "Lagos Hustle",
        meaning = "Spending calories is the ultimate investment in your future self."
    ),
    NaijaQuote(
        quote = "Fall down seven times, stand up eight — with three burpees on top!",
        category = "Challenger Spirit",
        meaning = "Turn setbacks into extra reps and greater resilience."
    ),
    NaijaQuote(
        quote = "Na who give up fit fail. We no dey carry last for Mayor Spends!",
        category = "Senior Man Wisdom",
        meaning = "In this journey, giving up is never an option. Forward ever!"
    ),
    NaijaQuote(
        quote = "Your only competition na the person you see inside mirror yesterday!",
        category = "Challenger Spirit",
        meaning = "Personal growth over comparison. Outdo yesterday's benchmark."
    ),
    NaijaQuote(
        quote = "Strong body, sharp mind, full Naija pride. Let’s get this workout!",
        category = "Afrobeats Energy",
        meaning = "Train for mental clarity, physical power, and authentic pride."
    ),
    NaijaQuote(
        quote = "Tiredness na visitor, fitness na the landlord. Show am who dey in charge!",
        category = "Proverb Remix",
        meaning = "Fatigue is temporary, but the strength you build is permanent."
    ),
    NaijaQuote(
        quote = "Obudu mountain cold, but the sweat inside you hot pass fire. Trek with confidence!",
        category = "Challenger Spirit",
        meaning = "Inner fire overpowers any external conditions."
    ),
    NaijaQuote(
        quote = "Every drop of sweat na payment for long life. Mayor dey spend big today!",
        category = "Lagos Hustle",
        meaning = "Invest your sweat equity into peak physical longevity."
    ),
    NaijaQuote(
        quote = "You no need permission to be great. Lace your shoes and take over the road!",
        category = "Senior Man Wisdom",
        meaning = "Step into your power with boldness and confidence."
    ),
    NaijaQuote(
        quote = "Karfin zuciya ne komai! Strength of heart is everything. When body wan fold, Arewa spirit go carry you!",
        category = "Arewa Wisdom",
        meaning = "Hausa proverb: True endurance comes from the inner resolve of the warrior."
    ),
    NaijaQuote(
        quote = "Sannu da aiki, Jarumi! Sweat today, crown tomorrow in the kingdom of fitness!",
        category = "Arewa Wisdom",
        meaning = "Hausa honorific: Commending hard work and celebrating warrior discipline."
    ),
    NaijaQuote(
        quote = "Gumi shine jari! In Mayor Spends, your sweat is your ultimate investment capital!",
        category = "Arewa Wisdom",
        meaning = "Hausa fitness concept: Sweat is capital that pays compounding health dividends."
    ),
    NaijaQuote(
        quote = "Ba gudu, ba ja da baya! Forward ever, no retreating when spending calories!",
        category = "Arewa Wisdom",
        meaning = "Famous Northern rallying cry: Bold courage and relentless progression."
    ),
    NaijaQuote(
        quote = "Lafiya jari ce! Health is the king of wealth — spend your energy like a Sardauna!",
        category = "Arewa Wisdom",
        meaning = "Ancient Hausa proverb: Physical wellness is the true foundation of greatness."
    ),
    NaijaQuote(
        quote = "Dambe warrior spirit: Stand tall, tighten your core, strike your fitness target with one blow!",
        category = "Arewa Wisdom",
        meaning = "Channeling the focus and conditioning of traditional Hausa martial artists."
    )
)

val NAIJA_MOTIVATIONAL_QUOTES = NAIJA_QUOTE_COLLECTION.map { it.quote }


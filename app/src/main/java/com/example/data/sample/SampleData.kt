package com.example.data.sample

import com.example.data.model.ChatMessage
import com.example.data.model.QuizQuestion

object SampleData {

    val defaultQuestions: List<QuizQuestion> = listOf(
        QuizQuestion(
            id = 1,
            questionText = "1. Proses pengembangan program bertujuan untuk...",
            options = listOf(
                "Membuat program asal jadi",
                "Menghasilkan produk sesuai kebutuhan pengguna",
                "Menyulitkan programmer agar teliti",
                "Menentukan merk komputer yang dipakai"
            ),
            correctIndex = 1,
            reasonWrong = "Mengapa Pilihan Lain Kurang Tepat?\nPilihan membuat program asal jadi atau sekadar menyulitkan programmer tidak mencerminkan filosofi rekayasa perangkat lunak modern. Pembuatan software selalu berorientasi pada penyelesaian masalah nyata pengguna (user-centric).",
            reasonRight = "Mengapa Harus Memilih \"Menghasilkan produk sesuai kebutuhan pengguna\"?\nTujuan utama dari siklus pengembangan perangkat lunak (SDLC) adalah menciptakan solusi digital yang efisien, relevan, dan memberi nilai tambah nyata sesuai kebutuhan pengguna yang telah diidentifikasi.",
            category = "SDLC / Rekayasa Perangkat Lunak"
        ),
        QuizQuestion(
            id = 2,
            questionText = "2. Tahap pertama dalam proses pengembangan program adalah...",
            options = listOf(
                "Pemeliharaan",
                "Implementasi",
                "Analisis kebutuhan",
                "Pengujian"
            ),
            correctIndex = 2,
            reasonWrong = "Mengapa Pilihan \"Pemeliharaan\" Kurang Tepat?\nPemeliharaan (Maintenance) merupakan tahap paling akhir dalam Siklus Hidup Pengembangan Perangkat Lunak (Software Development Life Cycle / SDLC). Tahap ini baru dilakukan setelah program selesai dibuat, diuji, dan sudah digunakan oleh pengguna secara nyata untuk memelihara, memperbaiki bug, atau melakukan peningkatan kinerja sistem. Tidak mungkin kita memelihara atau merawat sesuatu yang belum dibuat.",
            reasonRight = "Mengapa Harus Memilih \"Analisis Kebutuhan\"?\nAnalisis kebutuhan (Requirements Analysis) adalah fondasi utama dan langkah pertama. Sebelum mulai menulis kode atau merancang tampilan, pengembang harus memahami terlebih dahulu apa masalah yang ingin diselesaikan, siapa pengguna aplikasi, serta batasan dan kebutuhan sistem secara menyeluruh.",
            category = "SDLC / Rekayasa Perangkat Lunak"
        ),
        QuizQuestion(
            id = 3,
            questionText = "3. Pada tahap desain, aktivitas utama yang dilakukan adalah...",
            options = listOf(
                "Menulis kode program",
                "Membuat rancangan (flowchart, UI)",
                "Menguji program terhadap bug",
                "Merilis aplikasi ke Google Play Store"
            ),
            correctIndex = 1,
            reasonWrong = "Mengapa Pilihan \"Menulis kode program\" atau \"Menguji program\" Kurang Tepat?\nMenulis kode program adalah fokus tahap Implementasi (Coding), sedangkan menguji program dilakukan pada tahap Pengujian (Testing). Tahap desain harus diselesaikan sebelum penulisan kode dimulai.",
            reasonRight = "Mengapa Harus Memilih \"Membuat rancangan (flowchart, UI)\"?\nTahap Desain (Design) berfungsi sebagai jembatan antara analisis kebutuhan dan pengkodean. Aktivitas utamanya adalah merancang arsitektur sistem, alur logika (flowchart/ERD), wireframe, dan desain antarmuka pengguna (UI/UX).",
            category = "SDLC / Rekayasa Perangkat Lunak"
        ),
        QuizQuestion(
            id = 4,
            questionText = "4. Model pengembangan perangkat lunak yang sistematis dan berurutan dari atas ke bawah disebut...",
            options = listOf(
                "Model Waterfall",
                "Model Ad-Hoc",
                "Model Random",
                "Model Non-linier"
            ),
            correctIndex = 0,
            reasonWrong = "Mengapa Pilihan Lain Kurang Tepat?\nModel Ad-Hoc atau Random tidak memiliki struktur formal yang terjamin standar kualitasnya dalam rekayasa perangkat lunak.",
            reasonRight = "Mengapa Harus Memilih \"Model Waterfall\"?\nModel Waterfall (Air Terjun) adalah model SDLC klasik di mana setiap tahapan dikerjakan secara berurutan: Analisis -> Desain -> Implementasi -> Pengujian -> Pemeliharaan.",
            category = "SDLC / Rekayasa Perangkat Lunak"
        ),
        QuizQuestion(
            id = 5,
            questionText = "5. Tujuan utama dari tahap pengujian (testing) dalam pengembangan perangkat lunak adalah...",
            options = listOf(
                "Menambah jumlah baris kode agar terlihat rumit",
                "Menemukan dan memperbaiki bug atau kesalahan sebelum aplikasi dirilis",
                "Mempersulit pekerjaan pengembang antarmuka",
                "Mengurangi kecepatan performa server"
            ),
            correctIndex = 1,
            reasonWrong = "Mengapa Pilihan Lain Kurang Tepat?\nTesting bukan bertujuan mempersulit atau memperlambat sistem, melainkan penjaminan mutu (Quality Assurance).",
            reasonRight = "Mengapa Harus Memilih \"Menemukan dan memperbaiki bug atau kesalahan\"?\nTahap pengujian memastikan aplikasi bekerja sesuai spesifikasi kebutuhan, bebas dari cacat kritis, serta aman dan stabil saat digunakan pengguna.",
            category = "SDLC / Rekayasa Perangkat Lunak"
        )
    )

    val defaultChatMessages: List<ChatMessage> = listOf(
        ChatMessage(
            id = 1,
            text = "Halo! Saya **Studyvo Flash**, asisten AI belajar cerdasmu. Kamu bisa pilih menu cepat di atas untuk **mengubah materi menjadi soal soal**, **rangkuman**, atau **menjelaskan soal menjadi materi**!",
            isFromUser = false,
            timestamp = System.currentTimeMillis() - 60000
        )
    )

    // Sample study material about SDLC / Software Engineering
    val sampleMaterialText: String = """
        Siklus Hidup Pengembangan Perangkat Lunak (SDLC)
        
        1. Analisis Kebutuhan (Requirements Analysis): Langkah pertama dalam SDLC. Pengembang mengidentifikasi masalah, kebutuhan pengguna, dan fitur sistem sebelum menulis kode.
        2. Desain (Design): Membuat cetak biru sistem, seperti arsitektur, alur data (flowchart), dan tampilan antarmuka (UI/UX).
        3. Implementasi / Coding: Programmer menulis kode program berdasarkan rancangan dari tahap desain.
        4. Pengujian (Testing): Memeriksa program untuk menemukan dan memperbaiki bug sebelum dirilis ke pengguna.
        5. Pemeliharaan (Maintenance): Tahap paling akhir di mana aplikasi yang sudah berjalan dirawat, diperbaiki jika ada kendala, dan ditingkatkan kinerjanya.
    """.trimIndent()
}

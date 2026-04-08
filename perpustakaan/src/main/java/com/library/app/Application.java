package com.library.app;

import com.library.app.bootstrap.DatabaseInitializer;

public final class Application {
    private Application() {
    }

    public static void main(String[] args) {
        DatabaseInitializer.initialize();
        System.out.println("Inisialisasi database selesai. Aplikasi siap digunakan.");
    }
}

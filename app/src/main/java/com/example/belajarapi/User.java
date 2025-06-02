package com.example.belajarapi;

public class User {
        private String name;
        private String email;

        // Diperlukan Firebase (constructor kosong)
        public User() {}

        public User(String name, String email) {
                this.name = name;
                this.email = email;
        }

        // Getter
        public String getName() {
                return name;
        }

        public String getEmail() {
                return email;
        }
}
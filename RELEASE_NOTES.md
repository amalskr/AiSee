1. Added TalkBack accessibility check on splash screen with alert dialog to guide users to enable it
2. Added TalkBack status indicator (Enabled/Disabled) to Settings screen with direct link to Accessibility settings
3. Implemented Google Play In-App Updates with automatic Flexible/Immediate selection based on version change
4. Implemented Google Play In-App Reviews prompt on 3rd app open after signup
5. Fixed Google Sign-In user persistence so logged-in state is retained across app restarts
6. Updated Google Sign-In username to show email with (Google) suffix
7. Removed google-services.json from Git history and added to .gitignore for security
8. Added sign-out confirmation dialog to Settings screen
9. Updated sign-in screen to simplify username field and extract username from email input
10. Updated ApiError details type to JsonObject for better error handling
11. Added full name and phone number to Settings screen and user preferences
12. Added success toast message upon user registration
13. Updated splash screen navigation to handle logged-in state
14. Added InfoDialog and improved password reset feedback logic
15. Saved all login response data including metadata and token details
16. Integrated login and forgot password API endpoints
17. Saved signup response locally and display user info in Settings
18. Added phone number field to sign-up screen with validation
19. Added ErrorDialog and improved API error handling
20. Added form validation and scrolling to Sign-Up screen
21. Added networking layer with Ktor Client and registration API
22. Added WebView screen for Terms of Use
23. Added WebView screen for Check for Updates
24. Added custom fonts for improved UI consistency and branding
25. Updated Firebase project configuration
26. Improved app distribution settings
27. Integrated Firebase Crashlytics for better crash monitoring and stability tracking

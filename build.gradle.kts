name: Build Android APK

on:
  push:
    branches: [ "main", "master" ]
  workflow_dispatch:

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
    - name: Checkout Code
      uses: actions/checkout@v4

    - name: Set up JDK 17
      uses: actions/setup-java@v4
      with:
        java-version: '17'
        distribution: 'temurin'
        cache: gradle

    - name: Build APK
      run: |
        GRADLEW_PATH=$(find . -name gradlew)
        if [ -n "$GRADLEW_PATH" ]; then
          DIR=$(dirname "$GRADLEW_PATH")
          cd "$DIR"
          chmod +x gradlew
          ./gradlew assembleDebug
        else
          gradle assembleDebug
        fi

    - name: Upload APK
      uses: actions/upload-artifact@v4
      with:

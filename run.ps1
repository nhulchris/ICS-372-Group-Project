# Compile
Get-ChildItem -Recurse -Filter *.java | ForEach-Object { $_.FullName } > sources.txt

javac --module-path "C:\Users\Onlinevirus\Downloads\openjfx-26.0.1_windows-x64_bin-sdk\javafx-sdk-26.0.1\lib" `
--add-modules javafx.controls,javafx.fxml `
-d target\classes `
(Get-Content sources.txt)

# Copy resources
mkdir target\classes\fxml -ErrorAction SilentlyContinue
copy src\main\resources\fxml\* target\classes\fxml\

# Run
java --module-path "C:\Users\Onlinevirus\Downloads\openjfx-26.0.1_windows-x64_bin-sdk\javafx-sdk-26.0.1\lib" `
--add-modules javafx.controls,javafx.fxml `
-cp target\classes com.brewbite.BrewBiteApp

# Launches the StudyJarvis backend and Angular webapp in separate windows.
# Prereqs (one-time):
#   1. Postgres running locally with a studyjarvis database.
#   2. gcloud auth application-default login (for Vertex AI + GCS).
#   3. %APPDATA%\studyjarvis.properties populated (BucketName, GeminiProjectId, etc.)
#   4. Copy dev.env.example.ps1 to dev.env.ps1 and fill in the values.

$ErrorActionPreference = "Stop"
$backendDir = $PSScriptRoot
$envFile    = Join-Path $backendDir "dev.env.ps1"

if (-not (Test-Path $envFile)) {
    Write-Host "dev.env.ps1 not found." -ForegroundColor Red
    Write-Host "Copy dev.env.example.ps1 to dev.env.ps1 and fill in the values."
    exit 1
}

. $envFile

$webappDir = if ($env:STUDYJARVIS_WEBAPP_DIR) {
    $env:STUDYJARVIS_WEBAPP_DIR
} else {
    Join-Path (Split-Path (Split-Path $backendDir -Parent) -Parent) "studyjarviswebapp-tjb\studyjarviswebapp"
}

if (-not (Test-Path $webappDir)) {
    Write-Host "Webapp directory not found: $webappDir" -ForegroundColor Red
    Write-Host "Set STUDYJARVIS_WEBAPP_DIR in dev.env.ps1."
    exit 1
}

if (-not (Test-Path (Join-Path $webappDir "node_modules"))) {
    Write-Host "Webapp dependencies not installed. Running npm install ..." -ForegroundColor Yellow
    Push-Location $webappDir
    try {
        npm install
        if ($LASTEXITCODE -ne 0) {
            Write-Host "npm install failed." -ForegroundColor Red
            exit 1
        }
    } finally {
        Pop-Location
    }
}

$backendCmd = "`$host.UI.RawUI.WindowTitle = 'StudyJarvis Backend'; . '$envFile'; & '$backendDir\gradlew.bat' run"
$webappCmd  = "`$host.UI.RawUI.WindowTitle = 'StudyJarvis Webapp'; npm start"

Write-Host "Starting backend on http://localhost:7000 ..." -ForegroundColor Cyan
Start-Process pwsh -ArgumentList "-NoExit", "-Command", $backendCmd -WorkingDirectory $backendDir

Write-Host "Starting webapp on http://localhost:4200 ..." -ForegroundColor Cyan
Start-Process pwsh -ArgumentList "-NoExit", "-Command", $webappCmd -WorkingDirectory $webappDir

Write-Host ""
Write-Host "Both launched. Close each window to stop the respective service." -ForegroundColor Green

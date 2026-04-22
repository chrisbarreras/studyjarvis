# Copy this file to dev.env.ps1 and fill in the values.
# dev.env.ps1 is gitignored so secrets stay local.

$env:STUDYJARVIS_DB_URL      = "jdbc:postgresql://localhost:5432/studyjarvis"
$env:STUDYJARVIS_DB_USER     = "studyjarvis"
$env:STUDYJARVIS_DB_PASSWORD = "CHANGE_ME"
$env:STUDYJARVIS_SERVER_SECRET_KEY = "CHANGE_ME_LONG_RANDOM_STRING"

# Optional: path to the Angular webapp. Defaults to the sibling repo layout.
$env:STUDYJARVIS_WEBAPP_DIR = "D:\projects\studyjarviswebapp-tjb\studyjarviswebapp"

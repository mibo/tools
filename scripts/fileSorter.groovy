import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import java.time.*
import java.time.format.DateTimeFormatter

// Verzeichnis definieren
String directoryPath = "./files"  // Passe den Pfad nach Bedarf an

File dir = new File(directoryPath)
if (!dir.exists() || !dir.isDirectory()) {
    println "Das angegebene Verzeichnis existiert nicht oder ist kein Verzeichnis."
    return
}

// Alle Dateien im Verzeichnis durchgehen
dir.listFiles().each { file ->
    if (file.isFile()) {
        Path filePath = file.toPath()
        BasicFileAttributes attrs = Files.readAttributes(filePath, BasicFileAttributes)
        
        // Erstellungsdatum ermitteln
        Instant instant = attrs.creationTime().toInstant()
        LocalDateTime creationDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        int year = creationDate.getYear()
        int week = creationDate.get(WeekFields.of(Locale.getDefault()).weekOfYear())
        
        // Zielverzeichnis erstellen
        String targetDirPath = "${directoryPath}/${year}_KW${week}" 
        File targetDir = new File(targetDirPath)
        if (!targetDir.exists()) {
            targetDir.mkdirs()
        }
        
        // Datei verschieben
        Path targetPath = Paths.get(targetDirPath, file.getName())
        Files.move(filePath, targetPath, StandardCopyOption.REPLACE_EXISTING)
        println "${file.getName()} -> ${targetDirPath}"
    }
}


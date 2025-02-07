/*
 * Hello Minecraft! Launcher
 * Copyright (C) 2020  huangyuhui <huanghongxun2008@126.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.koishi.launcher.h2co3.core.game.mod;

import com.google.gson.JsonParseException;

import org.koishi.launcher.h2co3.core.game.download.ModLoaderType;
import org.koishi.launcher.h2co3.core.game.download.VersionNumber;
import org.koishi.launcher.h2co3.core.game.GameRepository;
import org.koishi.launcher.h2co3.core.game.mod.modinfo.FabricModMetadata;
import org.koishi.launcher.h2co3.core.game.mod.modinfo.ForgeNewModMetadata;
import org.koishi.launcher.h2co3.core.game.mod.modinfo.ForgeOldModMetadata;
import org.koishi.launcher.h2co3.core.game.mod.modinfo.LiteModMetadata;
import org.koishi.launcher.h2co3.core.game.mod.modinfo.PackMcMeta;
import org.koishi.launcher.h2co3.core.game.mod.modinfo.QuiltModMetadata;
import org.koishi.launcher.h2co3.core.utils.Pair;
import org.koishi.launcher.h2co3.core.utils.StringUtils;
import org.koishi.launcher.h2co3.core.utils.file.FileTools;
import org.koishi.launcher.h2co3.core.utils.io.CompressingUtils;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.TreeSet;

public final class ModManager {
    public static final String DISABLED_EXTENSION = ".disabled";
    public static final String OLD_EXTENSION = ".old";
    private static final Map<String, Pair<ModMetadataReader[], String>> READERS;

    static {
        TreeMap<String, Pair<ModMetadataReader[], String>> readers = new TreeMap<>();
        readers.put("zip", Pair.pair(new ModMetadataReader[]{
                ForgeOldModMetadata::fromFile,
                ForgeNewModMetadata::fromFile,
                FabricModMetadata::fromFile,
                QuiltModMetadata::fromFile,
                PackMcMeta::fromFile,
        }, ""));
        readers.put("jar", readers.get("zip"));
        readers.put("litemod", Pair.pair(new ModMetadataReader[]{
                LiteModMetadata::fromFile
        }, "LiteLoader Mod"));
        READERS = Collections.unmodifiableMap(readers);
    }

    private final GameRepository repository;
    private final String id;
    private final TreeSet<LocalModFile> localModFiles = new TreeSet<>();
    private final HashMap<LocalMod, LocalMod> localMods = new HashMap<>();
    private boolean loaded = false;

    public ModManager(GameRepository repository, String id) {
        this.repository = repository;
        this.id = id;
    }

    public static String getModName(Path file) {
        return StringUtils.removeSuffix(FileTools.getName(file), DISABLED_EXTENSION, OLD_EXTENSION);
    }

    public static boolean isFileNameMod(Path file) {
        String name = getModName(file);
        return name.endsWith(".zip") || name.endsWith(".jar") || name.endsWith(".litemod");
    }

    public static boolean isFileMod(Path modFile) {
        try (FileSystem fs = CompressingUtils.createReadOnlyZipFileSystem(modFile)) {
            if (Files.exists(fs.getPath("mcmod.info")) || Files.exists(fs.getPath("META-INF/mods.toml"))) {
                // Forge mod
                return true;
            }

            if (Files.exists(fs.getPath("fabric.mod.json"))) {
                // Fabric mod
                return true;
            }

            if (Files.exists(fs.getPath("quilt.mod.json"))) {
                // Quilt mod
                return true;
            }

            if (Files.exists(fs.getPath("litemod.json"))) {
                // Liteloader mod
                return true;
            }

            // resource pack, data pack
            return Files.exists(fs.getPath("pack.mcmeta"));
        } catch (IOException e) {
            return false;
        }
    }

    public static String getMcbbsUrl(String mcbbsId) {
        return String.format("https://www.mcbbs.net/thread-%s-1-1.html", mcbbsId);
    }

    public GameRepository getRepository() {
        return repository;
    }

    public String getVersion() {
        return id;
    }

    public Path getModsDirectory() {
        return repository.getRunDirectory(id).toPath().resolve("mods");
    }

    public LocalMod getLocalMod(String id, ModLoaderType modLoaderType) {
        return localMods.computeIfAbsent(new LocalMod(id, modLoaderType), x -> x);
    }

    private void addModInfo(Path file) {
        try {
            LocalModFile localModFile = getModInfo(file);
            if (!localModFile.isOld()) {
                localModFiles.add(localModFile);
            }
        } catch (IllegalArgumentException ignore) {
        }
    }

    public LocalModFile getModInfo(Path modFile) {
        String fileName = StringUtils.removeSuffix(FileTools.getName(modFile), DISABLED_EXTENSION, OLD_EXTENSION);
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        Pair<ModMetadataReader[], String> currentReader = READERS.get(extension);
        if (currentReader == null) {
            throw new IllegalArgumentException("File " + modFile + " is not a mod file.");
        }

        try (FileSystem fs = CompressingUtils.createReadOnlyZipFileSystem(modFile)) {
            for (ModMetadataReader reader : currentReader.getKey()) {
                try {
                    return reader.fromFile(this, modFile, fs);
                } catch (Exception ignore) {
                }
            }
        } catch (Exception ignored) {
        }

        return new LocalModFile(this,
                getLocalMod(FileTools.getNameWithoutExtension(modFile), ModLoaderType.UNKNOWN),
                modFile,
                FileTools.getNameWithoutExtension(modFile),
                new LocalModFile.Description(currentReader.getValue())
        );
    }

    public void refreshMods() throws IOException {
        localModFiles.clear();
        localMods.clear();
        if (Files.isDirectory(getModsDirectory())) {
            try (DirectoryStream<Path> modsDirectoryStream = Files.newDirectoryStream(getModsDirectory())) {
                for (Path subitem : modsDirectoryStream) {
                    if (Files.isDirectory(subitem) && VersionNumber.isIntVersionNumber(FileTools.getName(subitem))) {
                        // If the folder name is game version, forge will search mod in this subdirectory
                        try (DirectoryStream<Path> subitemDirectoryStream = Files.newDirectoryStream(subitem)) {
                            for (Path subsubitem : subitemDirectoryStream) {
                                addModInfo(subsubitem);
                            }
                        }
                    } else {
                        addModInfo(subitem);
                    }
                }
            }
        }
        loaded = true;
    }

    public Collection<LocalModFile> getMods() throws IOException {
        if (!loaded)
            refreshMods();
        return localModFiles;
    }

    public void addMod(Path file) throws IOException {
        if (!isFileNameMod(file))
            throw new IllegalArgumentException("File " + file + " is not a valid mod file.");

        if (!loaded)
            refreshMods();

        Path modsDirectory = getModsDirectory();
        Files.createDirectories(modsDirectory);

        Path newFile = modsDirectory.resolve(file.getFileName());
        FileTools.copyFile(file, newFile);

        addModInfo(newFile);
    }

    public void removeMods(LocalModFile... localModFiles) throws IOException {
        for (LocalModFile localModFile : localModFiles) {
            Files.deleteIfExists(localModFile.getFile());
        }
    }

    public void rollback(LocalModFile from, LocalModFile to) throws IOException {
        if (!loaded) {
            throw new IllegalStateException("ModManager Not loaded");
        }
        if (!localModFiles.contains(from)) {
            throw new IllegalStateException("Rolling back an unknown mod " + from.getFileName());
        }
        if (from.isOld()) {
            throw new IllegalArgumentException("Rolling back an old mod " + from.getFileName());
        }
        if (!to.isOld()) {
            throw new IllegalArgumentException("Rolling back to an old path " + to.getFileName());
        }
        if (from.getFileName().equals(to.getFileName())) {
            // We cannot roll back to the mod with the same name.
            return;
        }

        LocalMod mod = Objects.requireNonNull(from.getMod());
        if (mod != to.getMod()) {
            throw new IllegalArgumentException("Rolling back mod " + from.getFileName() + " to a different mod " + to.getFileName());
        }
        if (!mod.getFiles().contains(from)
                || !mod.getOldFiles().contains(to)) {
            throw new IllegalStateException("LocalMod state corrupt");
        }

        boolean active = from.isActive();
        from.setActive(true);
        from.setOld(true);
        to.setOld(false);
        to.setActive(active);
    }

    private Path backupMod(Path file) throws IOException {
        Path newPath = file.resolveSibling(
                StringUtils.addSuffix(
                        StringUtils.removeSuffix(FileTools.getName(file), DISABLED_EXTENSION),
                        OLD_EXTENSION
                )
        );
        if (Files.exists(file)) {
            Files.move(file, newPath, StandardCopyOption.REPLACE_EXISTING);
        }
        return newPath;
    }

    private Path restoreMod(Path file) throws IOException {
        Path newPath = file.resolveSibling(
                StringUtils.removeSuffix(FileTools.getName(file), OLD_EXTENSION)
        );
        if (Files.exists(file)) {
            Files.move(file, newPath, StandardCopyOption.REPLACE_EXISTING);
        }
        return newPath;
    }

    public Path setOld(LocalModFile modFile, boolean old) throws IOException {
        Path newPath;
        if (old) {
            newPath = backupMod(modFile.getFile());
            localModFiles.remove(modFile);
        } else {
            newPath = restoreMod(modFile.getFile());
            localModFiles.add(modFile);
        }
        return newPath;
    }

    public Path disableMod(Path file) throws IOException {
        if (isOld(file)) return file; // no need to disable an old mod.

        String fileName = FileTools.getName(file);
        if (fileName.endsWith(DISABLED_EXTENSION)) return file;

        Path disabled = file.resolveSibling(fileName + DISABLED_EXTENSION);
        if (Files.exists(file))
            Files.move(file, disabled, StandardCopyOption.REPLACE_EXISTING);
        return disabled;
    }

    public Path enableMod(Path file) throws IOException {
        if (isOld(file)) return file;
        Path enabled = file.resolveSibling(StringUtils.removeSuffix(FileTools.getName(file), DISABLED_EXTENSION));
        if (Files.exists(file))
            Files.move(file, enabled, StandardCopyOption.REPLACE_EXISTING);
        return enabled;
    }

    public boolean isOld(Path file) {
        return FileTools.getName(file).endsWith(OLD_EXTENSION);
    }

    public boolean isDisabled(Path file) {
        return FileTools.getName(file).endsWith(DISABLED_EXTENSION);
    }

    /**
     * Check if "mods" directory has mod file named "fileName" no matter the mod is disabled or not
     *
     * @param fileName name of the file whose existence is being checked
     * @return true if the file exists
     */
    public boolean hasSimpleMod(String fileName) {
        return Files.exists(getModsDirectory().resolve(StringUtils.removeSuffix(fileName, DISABLED_EXTENSION)))
                || Files.exists(getModsDirectory().resolve(StringUtils.addSuffix(fileName, DISABLED_EXTENSION)));
    }

    public Path getSimpleModPath(String fileName) {
        return getModsDirectory().resolve(fileName);
    }

    @FunctionalInterface
    private interface ModMetadataReader {
        LocalModFile fromFile(ModManager modManager, Path modFile, FileSystem fs) throws IOException, JsonParseException;
    }
}

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
package org.koishi.launcher.h2co3.core.game.mod.mcbbs;

import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import org.koishi.launcher.h2co3.core.game.download.DefaultDependencyManager;
import org.koishi.launcher.h2co3.core.game.download.DefaultGameRepository;
import org.koishi.launcher.h2co3.core.game.download.GameBuilder;
import org.koishi.launcher.h2co3.core.game.download.Version;
import org.koishi.launcher.h2co3.core.game.mod.MinecraftInstanceTask;
import org.koishi.launcher.h2co3.core.game.mod.Modpack;
import org.koishi.launcher.h2co3.core.game.mod.ModpackConfiguration;
import org.koishi.launcher.h2co3.core.game.mod.ModpackInstallTask;
import org.koishi.launcher.h2co3.core.utils.file.FileTools;
import org.koishi.launcher.h2co3.core.utils.gson.JsonUtils;
import org.koishi.launcher.h2co3.core.utils.task.Task;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class McbbsModpackLocalInstallTask extends Task<Void> {

    private static final String PATCH_NAME = "mcbbs";
    private final DefaultDependencyManager dependencyManager;
    private final File zipFile;
    private final Modpack modpack;
    private final McbbsModpackManifest manifest;
    private final String name;
    private final boolean update;
    private final DefaultGameRepository repository;
    private final MinecraftInstanceTask<McbbsModpackManifest> instanceTask;
    private final List<Task<?>> dependencies = new ArrayList<>(2);
    private final List<Task<?>> dependents = new ArrayList<>(4);

    public McbbsModpackLocalInstallTask(DefaultDependencyManager dependencyManager, File zipFile, Modpack modpack, McbbsModpackManifest manifest, String name) {
        this.dependencyManager = dependencyManager;
        this.zipFile = zipFile;
        this.modpack = modpack;
        this.manifest = manifest;
        this.name = name;
        this.repository = dependencyManager.getGameRepository();
        File run = repository.getRunDirectory(name);

        File json = repository.getModpackConfiguration(name);
        if (repository.hasVersion(name) && !json.exists())
            throw new IllegalArgumentException("VersionMod " + name + " already exists.");
        this.update = repository.hasVersion(name);


        GameBuilder builder = dependencyManager.gameBuilder().name(name);
        for (McbbsModpackManifest.Addon addon : manifest.getAddons()) {
            builder.version(addon.getId(), addon.getVersion());
        }

        dependents.add(builder.buildAsync());
        onDone().register(event -> {
            if (event.isFailed())
                repository.removeVersionFromDisk(name);
        });

        ModpackConfiguration<McbbsModpackManifest> config = null;
        try {
            if (json.exists()) {
                config = JsonUtils.GSON.fromJson(FileTools.readText(json), new TypeToken<ModpackConfiguration<McbbsModpackManifest>>() {
                }.getType());

                if (!McbbsModpackProvider.INSTANCE.getName().equals(config.getType()))
                    throw new IllegalArgumentException("VersionMod " + name + " is not a Mcbbs modpack. Cannot update this version.");
            }
        } catch (JsonParseException | IOException ignore) {
        }
        dependents.add(new ModpackInstallTask<>(zipFile, run, modpack.getEncoding(), Collections.singletonList("/overrides"), any -> true, config).withStage("h2co3.modpack"));
        instanceTask = new MinecraftInstanceTask<>(zipFile, modpack.getEncoding(), Collections.singletonList("/overrides"), manifest, McbbsModpackProvider.INSTANCE, modpack.getName(), modpack.getVersion(), repository.getModpackConfiguration(name));
        dependents.add(instanceTask.withStage("h2co3.modpack"));
    }

    @Override
    public List<Task<?>> getDependents() {
        return dependents;
    }

    @Override
    public List<Task<?>> getDependencies() {
        return dependencies;
    }

    @Override
    public void execute() throws Exception {
        Version version = repository.readVersionJson(name);
        Optional<Version> mcbbsPatch = version.getPatches().stream().filter(patch -> PATCH_NAME.equals(patch.getId())).findFirst();
        if (!update) {
            Version patch = new Version(PATCH_NAME).setLibraries(manifest.getLibraries());
            dependencies.add(repository.saveAsync(version.addPatch(patch)));
        } else if (mcbbsPatch.isPresent()) {
            // This mcbbs modpack was installed by H2CO3Launcher.
            Version patch = mcbbsPatch.get().setLibraries(manifest.getLibraries());
            dependencies.add(repository.saveAsync(version.addPatch(patch)));
        } else {
            // This mcbbs modpack was installed by other launchers.
        }

        dependencies.add(new McbbsModpackCompletionTask(dependencyManager, name, instanceTask.getResult()));
    }
}

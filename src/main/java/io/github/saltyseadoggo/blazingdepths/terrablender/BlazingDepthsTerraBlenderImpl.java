package io.github.saltyseadoggo.blazingdepths.terrablender;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;

import com.mojang.datafixers.util.Pair;

import io.github.saltyseadoggo.blazingdepths.BlazingDepths;
import io.github.saltyseadoggo.blazingdepths.init.BlazingDepthsBiomes;
import io.github.saltyseadoggo.blazingdepths.surfacerules.BlazingDepthsSurfaceRules;
import terrablender.api.BiomeProvider;
import terrablender.api.BiomeProviders;
import terrablender.api.TerraBlenderApi;
import terrablender.worldgen.TBClimate;

import java.util.Optional;
import java.util.function.Consumer;

public class BlazingDepthsTerraBlenderImpl implements TerraBlenderApi
{
        //As per Terra Blender's documentation, all of its shit has to be done in one class.
        //See: https://github.com/Glitchfiend/TerraBlender/wiki/Getting-started

    @Override
    public void onTerraBlenderInitialized() 
    {
            //The former is the overworld provider weight; the latter is the nether provider weight.
            //Because Blazing Depths doesn't add any overworld biomes, the former value must always be zero, or the game crashes!
        BiomeProviders.register(new BlazingDepthsBiomeProvider(new Identifier(BlazingDepths.MOD_ID, "biome_provider"), 0, 1));
    }

        //Class in a class? I didn't know this was possible but this is how the Terra Blender docs say to do it.
    public class BlazingDepthsBiomeProvider extends BiomeProvider {
            //The Terra Blender documentation uses the Mojang mappings class name ResourceLocation. With Yarn, it is Identifier.
        public BlazingDepthsBiomeProvider(Identifier name, int overworldWeight, int netherWeight)
        {
            super(name, overworldWeight, netherWeight);
        }

        @Override
            //ResourceKey in the Terra Blender docs example is a Mojang mappings name; its Yarn name is RegistryKey.
        public void addNetherBiomes(Registry<Biome> registry, Consumer<Pair<TBClimate.ParameterPoint, RegistryKey<Biome>>> mapper)
        {
            this.addBiome(mapper,
                //Temperature, humidity (unused by Nether biomes), continentalness (unused by Nether biomes)
            MultiNoiseUtil.ParameterRange.of(0.3F), MultiNoiseUtil.ParameterRange.of(0.0F), MultiNoiseUtil.ParameterRange.of(0.0F),
                //Erosion, weirdness, depth (all unused by Nether biomes)
            MultiNoiseUtil.ParameterRange.of(0.0F), MultiNoiseUtil.ParameterRange.of(0.0F), MultiNoiseUtil.ParameterRange.of(0.0F),
            0.0F, BlazingDepthsBiomes.SEARED_DUNES_KEY);
        }

        @Override
        public Optional<MaterialRules.MaterialRule> getOverworldSurfaceRules()
        {
            return Optional.of(BlazingDepthsSurfaceRules.makeRules());
        }
    }
}
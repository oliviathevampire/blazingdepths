package io.github.saltyseadoggo.blazingdepths.datagen;

import io.github.saltyseadoggo.blazingdepths.BlazingDepths;
import io.github.saltyseadoggo.blazingdepths.datagen.api.LanguageDataProvider;
import io.github.saltyseadoggo.blazingdepths.init.BlazingDepthsBlocks;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.*;
import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.ModelIds;
import net.minecraft.data.client.Models;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.client.TextureMap;
import net.minecraft.data.client.TexturedModel;
import net.minecraft.data.server.BlockLootTableGenerator;
import net.minecraft.data.server.recipe.CookingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.recipe.Ingredient;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class BlazingDepthsDataGen implements DataGeneratorEntrypoint {

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		fabricDataGenerator.addProvider(BlockStateDefinitionProvider::new);
		fabricDataGenerator.addProvider(BlazingLootTableDataProvider::new);
		fabricDataGenerator.addProvider(BlazingLanguageDataProvider::new);
		fabricDataGenerator.addProvider(BlazingRecipesDataProvider::new);

		BlazingBlockTagsDataProvider blockTagsProvider = fabricDataGenerator.addProvider(BlazingBlockTagsDataProvider::new);
		fabricDataGenerator.addProvider(new BlazingItemTagsDataProvider(fabricDataGenerator, blockTagsProvider));
	}

	private static class BlockStateDefinitionProvider extends FabricModelProvider {

		public BlockStateDefinitionProvider(FabricDataGenerator dataGenerator) {
			super(dataGenerator);
		}

		@Override
		public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
			//Seared Sand
			TexturedModel.Factory searedSandFactory = TexturedModel.CUBE_ALL.andThen(textures -> textures.put(TextureKey.ALL,
					new Identifier(BlazingDepths.MOD_ID, "block/seared_sand")));
			blockStateModelGenerator.registerSingleton(BlazingDepthsBlocks.SEARED_SAND, searedSandFactory);
			blockStateModelGenerator.registerParentedItemModel(BlazingDepthsBlocks.SEARED_SAND,
					ModelIds.getBlockModelId(BlazingDepthsBlocks.SEARED_SAND));

			//Seared Sandstone
			TextureMap searedSandstoneTexture = new TextureMap()
					.put(TextureKey.SIDE, new Identifier(BlazingDepths.MOD_ID, "block/seared_sandstone"))
					.put(TextureKey.END, new Identifier(BlazingDepths.MOD_ID, "block/seared_sandstone_top"))
					.put(TextureKey.WALL, new Identifier(BlazingDepths.MOD_ID, "block/seared_sandstone"));
			TexturedModel.Factory searedSandstoneFactory = TexturedModel.CUBE_COLUMN.andThen(texture -> texture
					.put(TextureKey.SIDE, new Identifier(BlazingDepths.MOD_ID, "block/seared_sandstone"))
					.put(TextureKey.END, new Identifier(BlazingDepths.MOD_ID, "block/seared_sandstone_top")));
			blockStateModelGenerator.registerSingleton(BlazingDepthsBlocks.SEARED_SANDSTONE, searedSandstoneFactory);
			blockStateModelGenerator.registerParentedItemModel(BlazingDepthsBlocks.SEARED_SANDSTONE,
					ModelIds.getBlockModelId(BlazingDepthsBlocks.SEARED_SANDSTONE));

			wall(BlazingDepthsBlocks.SEARED_SANDSTONE_WALL, searedSandstoneTexture, blockStateModelGenerator);
			stairs(BlazingDepthsBlocks.SEARED_SANDSTONE_STAIRS, searedSandstoneTexture, blockStateModelGenerator);
			slab(BlazingDepthsBlocks.SEARED_SANDSTONE_SLAB, searedSandstoneTexture, blockStateModelGenerator);

			//Smooth Seared Sandstone
			TextureMap smoothSearedSandstoneTexture = new TextureMap()
					.put(TextureKey.SIDE, new Identifier(BlazingDepths.MOD_ID, "block/seared_sandstone_top"))
					.put(TextureKey.END, new Identifier(BlazingDepths.MOD_ID, "block/seared_sandstone_top"))
					.put(TextureKey.WALL, new Identifier(BlazingDepths.MOD_ID, "block/seared_sandstone_top"));
			TexturedModel.Factory smoothSearedSandstoneFactory = TexturedModel.CUBE_ALL.andThen(textures -> textures.put(TextureKey.ALL,
					new Identifier(BlazingDepths.MOD_ID, "block/seared_sandstone_top")));
			blockStateModelGenerator.registerSingleton(BlazingDepthsBlocks.SMOOTH_SEARED_SANDSTONE, smoothSearedSandstoneFactory);
			blockStateModelGenerator.registerParentedItemModel(BlazingDepthsBlocks.SMOOTH_SEARED_SANDSTONE,
					ModelIds.getBlockModelId(BlazingDepthsBlocks.SMOOTH_SEARED_SANDSTONE));

			wall(BlazingDepthsBlocks.SMOOTH_SEARED_SANDSTONE_WALL, smoothSearedSandstoneTexture, blockStateModelGenerator);
			stairs(BlazingDepthsBlocks.SMOOTH_SEARED_SANDSTONE_STAIRS, smoothSearedSandstoneTexture, blockStateModelGenerator);
			slab(BlazingDepthsBlocks.SMOOTH_SEARED_SANDSTONE_SLAB, smoothSearedSandstoneTexture, blockStateModelGenerator);
		}

		@Override
		public void generateItemModels(ItemModelGenerator itemModelGenerator) {

		}

		private void stairs(Block block, TextureMap texture, BlockStateModelGenerator blockStateModelGenerator) {
			Identifier searedSandstoneStairsInner = Models.INNER_STAIRS.upload(block,
					texture, blockStateModelGenerator.modelCollector);
			Identifier searedSandstoneStairsStraight = Models.STAIRS.upload(block,
					texture, blockStateModelGenerator.modelCollector);
			Identifier searedSandstoneStairsOuter = Models.OUTER_STAIRS.upload(block,
					texture, blockStateModelGenerator.modelCollector);
			blockStateModelGenerator.blockStateCollector.accept(BlockStateModelGenerator.createStairsBlockState(block,
					searedSandstoneStairsInner, searedSandstoneStairsStraight, searedSandstoneStairsOuter));
			blockStateModelGenerator.registerParentedItemModel(block, searedSandstoneStairsStraight);
		}

		private void slab(Block block, TextureMap texture, BlockStateModelGenerator blockStateModelGenerator) {
			Identifier resourceLocation3 = Models.SLAB_TOP.upload(block, texture,
					blockStateModelGenerator.modelCollector);
			Identifier resourceLocation4 = Models.SLAB.upload(block, texture,
					blockStateModelGenerator.modelCollector);
			blockStateModelGenerator.blockStateCollector.accept(BlockStateModelGenerator.createSlabBlockState(block,
					resourceLocation4, resourceLocation3, ModelIds.getBlockModelId(BlazingDepthsBlocks.SMOOTH_SEARED_SANDSTONE)));
			blockStateModelGenerator.registerParentedItemModel(block, resourceLocation4);
		}

		public void wall(Block wallBlock, TextureMap texture, BlockStateModelGenerator blockStateModelGenerator) {
			Identifier identifier = Models.TEMPLATE_WALL_POST.upload(wallBlock, texture, blockStateModelGenerator.modelCollector);
			Identifier identifier2 = Models.TEMPLATE_WALL_SIDE.upload(wallBlock, texture, blockStateModelGenerator.modelCollector);
			Identifier identifier3 = Models.TEMPLATE_WALL_SIDE_TALL.upload(wallBlock, texture, blockStateModelGenerator.modelCollector);
			blockStateModelGenerator.blockStateCollector.accept(BlockStateModelGenerator.createWallBlockState(wallBlock, identifier, identifier2, identifier3));
			Identifier identifier4 = Models.WALL_INVENTORY.upload(wallBlock, texture, blockStateModelGenerator.modelCollector);
			blockStateModelGenerator.registerParentedItemModel(wallBlock, identifier4);
		}
	}

	private static class BlazingLanguageDataProvider extends LanguageDataProvider {

		protected BlazingLanguageDataProvider(FabricDataGenerator dataGenerator) {
			super(dataGenerator, "en_us");
		}

		@Override
		protected void registerTranslations() {
			this.add("itemGroup.blazing_depths.blazing_depths", "Blazing Depths");

			//Seared Sand
			this.addBlock(BlazingDepthsBlocks.SEARED_SAND, "Seared Sand");

			//Seared Sandstone
			this.addBlock(BlazingDepthsBlocks.SEARED_SANDSTONE, "Seared Sandstone");
			this.addBlock(BlazingDepthsBlocks.SEARED_SANDSTONE_SLAB, "Seared Sandstone Slab");
			this.addBlock(BlazingDepthsBlocks.SEARED_SANDSTONE_STAIRS, "Seared Sandstone Stairs");
			this.addBlock(BlazingDepthsBlocks.SEARED_SANDSTONE_WALL, "Seared Sandstone Wall");

			//Smooth Seared Sandstone
			this.addBlock(BlazingDepthsBlocks.SMOOTH_SEARED_SANDSTONE, "Smooth Seared Sandstone");
			this.addBlock(BlazingDepthsBlocks.SMOOTH_SEARED_SANDSTONE_SLAB, "Smooth Seared Sandstone Slab");
			this.addBlock(BlazingDepthsBlocks.SMOOTH_SEARED_SANDSTONE_STAIRS, "Smooth Seared Sandstone Stairs");
			this.addBlock(BlazingDepthsBlocks.SMOOTH_SEARED_SANDSTONE_WALL, "Smooth Seared Sandstone Wall");
		}
	}

	private static class BlazingLootTableDataProvider extends FabricBlockLootTableProvider {

		protected BlazingLootTableDataProvider(FabricDataGenerator dataGenerator) {
			super(dataGenerator);
		}

		@Override
		protected void generateBlockLootTables() {
			//Seared Sand
			this.addDrop(BlazingDepthsBlocks.SEARED_SAND);

			//Seared Sandstone
			this.addDrop(BlazingDepthsBlocks.SEARED_SANDSTONE);
			this.addDrop(BlazingDepthsBlocks.SEARED_SANDSTONE_SLAB, BlockLootTableGenerator::slabDrops);
			this.addDrop(BlazingDepthsBlocks.SEARED_SANDSTONE_STAIRS);
			this.addDrop(BlazingDepthsBlocks.SEARED_SANDSTONE_WALL);

			//Smooth Seared Sandstone
			this.addDrop(BlazingDepthsBlocks.SMOOTH_SEARED_SANDSTONE);
			this.addDrop(BlazingDepthsBlocks.SMOOTH_SEARED_SANDSTONE_SLAB, BlockLootTableGenerator::slabDrops);
			this.addDrop(BlazingDepthsBlocks.SMOOTH_SEARED_SANDSTONE_STAIRS);
			this.addDrop(BlazingDepthsBlocks.SMOOTH_SEARED_SANDSTONE_WALL);
		}

	}

	private static class BlazingBlockTagsDataProvider extends FabricTagProvider.BlockTagProvider {

		protected BlazingBlockTagsDataProvider(FabricDataGenerator dataGenerator) {
			super(dataGenerator);
		}

		@Override
		protected void generateTags() {
			this.getOrCreateTagBuilder(BlockTags.STAIRS).
					add(BlazingDepthsBlocks.SEARED_SANDSTONE_STAIRS).
					add(BlazingDepthsBlocks.SMOOTH_SEARED_SANDSTONE_STAIRS);
			this.getOrCreateTagBuilder(BlockTags.SLABS).
					add(BlazingDepthsBlocks.SEARED_SANDSTONE_SLAB).
					add(BlazingDepthsBlocks.SMOOTH_SEARED_SANDSTONE_SLAB);
			this.getOrCreateTagBuilder(BlockTags.WALLS).
					add(BlazingDepthsBlocks.SEARED_SANDSTONE_WALL).
					add(BlazingDepthsBlocks.SMOOTH_SEARED_SANDSTONE_WALL);
		}

	}

	private static class BlazingItemTagsDataProvider extends FabricTagProvider.ItemTagProvider {

		protected BlazingItemTagsDataProvider(FabricDataGenerator dataGenerator, BlockTagProvider blockTagProvider) {
			super(dataGenerator, blockTagProvider);
		}

		@Override
		protected void generateTags() {
			this.copy(BlockTags.STAIRS, ItemTags.STAIRS);
			this.copy(BlockTags.SLABS, ItemTags.SLABS);
			this.copy(BlockTags.WALLS, ItemTags.WALLS);
		}

	}

	private static class BlazingRecipesDataProvider extends FabricRecipeProvider {

		protected BlazingRecipesDataProvider(FabricDataGenerator dataGenerator) {
			super(dataGenerator);
		}

		@Override
		protected void generateRecipes(Consumer<RecipeJsonProvider> exporter) {
			ShapedRecipeJsonBuilder.create(BlazingDepthsBlocks.SEARED_SANDSTONE).input('#', BlazingDepthsBlocks.SEARED_SAND).pattern("##").pattern("##")
					.criterion("has_seared_sand", conditionsFromItem(BlazingDepthsBlocks.SEARED_SAND)).offerTo(exporter);
			createSlabRecipe(BlazingDepthsBlocks.SEARED_SANDSTONE_SLAB, Ingredient.ofItems(BlazingDepthsBlocks.SEARED_SANDSTONE)).criterion("has_seared_sandstone", conditionsFromItem(BlazingDepthsBlocks.SEARED_SANDSTONE)).offerTo(exporter);
			createStairsRecipe(BlazingDepthsBlocks.SEARED_SANDSTONE_STAIRS, Ingredient.ofItems(BlazingDepthsBlocks.SEARED_SANDSTONE)).criterion("has_seared_sandstone", conditionsFromItem(BlazingDepthsBlocks.SEARED_SANDSTONE)).offerTo(exporter);
			offerWallRecipe(exporter, BlazingDepthsBlocks.SEARED_SANDSTONE_WALL, BlazingDepthsBlocks.SEARED_SANDSTONE);
			offerStonecuttingRecipe(exporter, BlazingDepthsBlocks.SEARED_SANDSTONE_SLAB, BlazingDepthsBlocks.SEARED_SANDSTONE, 2);
			offerStonecuttingRecipe(exporter, BlazingDepthsBlocks.SEARED_SANDSTONE_STAIRS, BlazingDepthsBlocks.SEARED_SANDSTONE);
			offerStonecuttingRecipe(exporter, BlazingDepthsBlocks.SEARED_SANDSTONE_WALL, BlazingDepthsBlocks.SEARED_SANDSTONE);

			CookingRecipeJsonBuilder.createSmelting(Ingredient.ofItems(BlazingDepthsBlocks.SEARED_SANDSTONE), BlazingDepthsBlocks.SMOOTH_SEARED_SANDSTONE.asItem(), 0.1F, 200).criterion("has_seared_sandstone", conditionsFromItem(BlazingDepthsBlocks.SEARED_SANDSTONE)).offerTo(exporter);
			createSlabRecipe(BlazingDepthsBlocks.SMOOTH_SEARED_SANDSTONE_SLAB, Ingredient.ofItems(BlazingDepthsBlocks.SMOOTH_SEARED_SANDSTONE)).criterion("has_smooth_seared_sandstone", conditionsFromItem(BlazingDepthsBlocks.SMOOTH_SEARED_SANDSTONE)).offerTo(exporter);
			createStairsRecipe(BlazingDepthsBlocks.SMOOTH_SEARED_SANDSTONE_STAIRS, Ingredient.ofItems(BlazingDepthsBlocks.SMOOTH_SEARED_SANDSTONE)).criterion("has_smooth_seared_sandstone", conditionsFromItem(BlazingDepthsBlocks.SMOOTH_SEARED_SANDSTONE)).offerTo(exporter);
			offerWallRecipe(exporter, BlazingDepthsBlocks.SMOOTH_SEARED_SANDSTONE_WALL, BlazingDepthsBlocks.SMOOTH_SEARED_SANDSTONE);
			offerStonecuttingRecipe(exporter, BlazingDepthsBlocks.SMOOTH_SEARED_SANDSTONE_SLAB, BlazingDepthsBlocks.SMOOTH_SEARED_SANDSTONE, 2);
			offerStonecuttingRecipe(exporter, BlazingDepthsBlocks.SMOOTH_SEARED_SANDSTONE_STAIRS, BlazingDepthsBlocks.SMOOTH_SEARED_SANDSTONE);
			offerStonecuttingRecipe(exporter, BlazingDepthsBlocks.SMOOTH_SEARED_SANDSTONE_WALL, BlazingDepthsBlocks.SMOOTH_SEARED_SANDSTONE);
		}

	}

}
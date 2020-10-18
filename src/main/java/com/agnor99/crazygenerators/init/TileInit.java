package com.agnor99.crazygenerators.init;

import com.agnor99.crazygenerators.CrazyGenerators;
import com.agnor99.crazygenerators.objects.tile.*;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileInit {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, CrazyGenerators.MOD_ID);

    public static final RegistryObject<TileEntityType<QuestionGeneratorTileEntity>> QUESTION_GENERATOR = TILE_ENTITY_TYPES.register(
            "question_generator",
             () -> TileEntityType.Builder.create(QuestionGeneratorTileEntity::new, BlockInit.question_generator)
                     .build(null)
    );
    public static final RegistryObject<TileEntityType<TimingGeneratorTileEntity>> TIMING_GENERATOR = TILE_ENTITY_TYPES.register(
            "timing_generator",
            () -> TileEntityType.Builder.create(TimingGeneratorTileEntity::new, BlockInit.timing_generator)
                    .build(null)
    );
    public static final RegistryObject<TileEntityType<PositionGeneratorTileEntity>> POSITION_GENERATOR = TILE_ENTITY_TYPES.register(
            "position_generator",
            () -> TileEntityType.Builder.create(PositionGeneratorTileEntity::new, BlockInit.position_generator)
                    .build(null)
    );
    public static final RegistryObject<TileEntityType<ItemGeneratorTileEntity>> ITEM_GENERATOR = TILE_ENTITY_TYPES.register(
            "item_generator",
            () -> TileEntityType.Builder.create(ItemGeneratorTileEntity::new, BlockInit.item_generator)
                    .build(null)
    );
    public static final RegistryObject<TileEntityType<RedstoneGeneratorTileEntity>> REDSTONE_GENERATOR = TILE_ENTITY_TYPES.register(
            "redstone_generator",
            () -> TileEntityType.Builder.create(RedstoneGeneratorTileEntity::new, BlockInit.redstone_generator)
                    .build(null)
    );
    public static final RegistryObject<TileEntityType<StructureGeneratorTileEntity>> STRUCTURE_GENERATOR = TILE_ENTITY_TYPES.register(
            "structure_generator",
            () -> TileEntityType.Builder.create(StructureGeneratorTileEntity::new, BlockInit.structure_generator)
                    .build(null)
    );

}

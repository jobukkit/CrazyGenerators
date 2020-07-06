package com.agnor99.crazygenerators.init;

import com.agnor99.crazygenerators.CrazyGenerators;
import com.agnor99.crazygenerators.objects.tile.QuestionGeneratorTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

public class TileInit {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, CrazyGenerators.MOD_ID);

    public static final RegistryObject<TileEntityType<QuestionGeneratorTileEntity>> QUESTION_GENERATOR = TILE_ENTITY_TYPES.register(
            "question_generator",
             () -> TileEntityType.Builder.create(QuestionGeneratorTileEntity::new, BlockInit.question_generator)
                     .build(null)
    );
}
